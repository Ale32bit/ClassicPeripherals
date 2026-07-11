-- SPDX-FileCopyrightText: 2017 Daniel Ratcliffe
--
-- SPDX-License-Identifier: LicenseRef-CCPL

local gps = require("cp.radiogps")

local function printUsage()
    local programName = arg[0] or fs.getName(shell.getRunningProgram())
    print("Usages:")
    print(programName .. " host")
    print(programName .. " host <x> <y> <z>")
    print(programName .. " locate")
end

local function pack(x, y, z)
    return string.pack(gps.FORMAT, x, y, z)
end

local tArgs = { ... }
if #tArgs < 1 then
    printUsage()
    return
end

local sCommand = tArgs[1]
if sCommand == "locate" then
    -- "gps locate"
    -- Just locate this computer (this will print the results)
    gps.locate(2, true)

elseif sCommand == "host" then
    -- "gps host"
    -- Act as a GPS host
    if pocket then
        print("GPS Hosts must be stationary")
        return
    end

    local radio = peripheral.find("radio_tower")
    if radio == nil then
        print("No radio tower found. 1 required.")
        return
    end

    if not radio.isValid() or radio.getHeight() == 1 then
        print("The radio tower is invalid.")
        return
    end

    local sRadioSide = peripheral.getName(radio)

    -- Determine position
    local x, y, z
    if #tArgs >= 4 then
        -- Position is manually specified
        x = tonumber(tArgs[2])
        y = tonumber(tArgs[3])
        z = tonumber(tArgs[4])
        if x == nil or y == nil or z == nil then
            printUsage()
            return
        end
        print("Position is " .. x .. "," .. y .. "," .. z)
    else
        -- Position is to be determined using locate
        x, y, z = gps.locate(2, true)
        if x == nil then
            print("Run \"radiogps host <x> <y> <z>\" to set position manually")
            return
        end
    end

    -- Open a channel
    print("Opening frequency on radio " .. sRadioSide)
    radio.setFrequency(gps.CHANNEL_GPS)

    -- Serve requests indefinitely
    local nServed = 0
    while true do
        local e, p1, p2 = os.pullEvent("radio_message")
        if e == "radio_message" then
            -- We received a message from a modem
            local sSide, sMessage = p1, p2
            if sSide == sRadioSide and sMessage == "PING" then
                radio.broadcast(pack(x, y, z))

                -- Print the number of requests handled
                nServed = nServed + 1
                if nServed > 1 then
                    local _, y = term.getCursorPos()
                    term.setCursorPos(1, y - 1)
                end
                print(nServed .. " GPS requests served")
            end
        end
    end
else
    printUsage()
end
