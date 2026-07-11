-- SPDX-FileCopyrightText: 2017 Daniel Ratcliffe
--
-- SPDX-License-Identifier: LicenseRef-CCPL

--[[- Use [modems][`modem`] to locate the position of the current turtle or
computers.

This works by communicating with other computers (called GPS hosts) that already
know their position, finding the distance to those computers (with
[`modem_message`]), and using that to derive its position from theirs (with a
process known as [trilateration](https://en.wikipedia.org/wiki/Trilateration).

@module gps
@since 1.31
@see gps_setup
]]

local expect = dofile("rom/modules/main/cc/expect.lua").expect
local module = {}

--- The channel which GPS requests and responses are broadcast on.
module.CHANNEL_GPS = 65534
module.FORMAT = "n n n"

local function trilaterate(A, B, C)
    local a2b = B.vPosition - A.vPosition
    local a2c = C.vPosition - A.vPosition

    if math.abs(a2b:normalize():dot(a2c:normalize())) > 0.999 then
        return nil
    end

    local d = a2b:length()
    local ex = a2b:normalize()
    local i = ex:dot(a2c)
    local ey = (a2c - ex * i):normalize()
    local j = ey:dot(a2c)
    local ez = ex:cross(ey)

    local r1 = A.nDistance
    local r2 = B.nDistance
    local r3 = C.nDistance

    local x = (r1 * r1 - r2 * r2 + d * d) / (2 * d)
    local y = (r1 * r1 - r3 * r3 - x * x + (x - i) * (x - i) + j * j) / (2 * j)

    local result = A.vPosition + ex * x + ey * y

    local zSquared = r1 * r1 - x * x - y * y
    if zSquared > 0 then
        local z = math.sqrt(zSquared)
        local result1 = result + ez * z
        local result2 = result - ez * z

        local rounded1, rounded2 = result1:round(0.01), result2:round(0.01)
        if rounded1.x ~= rounded2.x or rounded1.y ~= rounded2.y or rounded1.z ~= rounded2.z then
            return rounded1, rounded2
        else
            return rounded1
        end
    end
    return result:round(0.01)

end

local function narrow(p1, p2, fix)
    local dist1 = math.abs((p1 - fix.vPosition):length() - fix.nDistance)
    local dist2 = math.abs((p2 - fix.vPosition):length() - fix.nDistance)

    if math.abs(dist1 - dist2) < 0.01 then
        return p1, p2
    elseif dist1 < dist2 then
        return p1:round(0.01)
    else
        return p2:round(0.01)
    end
end

local function unpack(data)
    local ok, par1, par2, par3 = pcall(string.unpack, module.FORMAT, data)
    if not ok then
        return nil, par1
    end

    return { par1, par2, par3 }
end

--- Tries to retrieve the computer or turtles own location.
--
-- @tparam[opt=2] number timeout The maximum time in seconds taken to establish our
-- position.
-- @tparam[opt=false] boolean debug Print debugging messages
-- @treturn[1] number This computer's `x` position.
-- @treturn[1] number This computer's `y` position.
-- @treturn[1] number This computer's `z` position.
-- @treturn[2] nil If the position could not be established.
function module.locate(_nTimeout, _bDebug)
    expect(1, _nTimeout, "number", "nil")
    expect(2, _bDebug, "boolean", "nil")
    -- Let command computers use their magic fourth-wall-breaking special abilities
    if commands then
        return commands.getBlockPosition()
    end

    -- Open GPS channel to listen for ping responses
    local radio = peripheral.find("radio_tower")
    if radio == nil then
        if _bDebug then
            print("No radio attached")
        end
        return nil
    end

    if _bDebug then
        print("Finding position...")
    end

    local nPreviousChannel = radio.getFrequency()
    radio.setFrequency(module.CHANNEL_GPS)

    radio.broadcast("PING")

    -- Wait for the responses
    local tFixes = {}
    local pos1, pos2 = nil, nil
    local timeout = os.startTimer(_nTimeout or 2)
    while true do
        local e, p1, p2, p3 = os.pullEvent()
        if e == "radio_message" then
            -- We received a reply from a modem
            local sSide, sMessage, nDistance = p1, p2, p3
            -- Received the correct message from the correct modem: use it to determine position
            local tMessage = unpack(sMessage)
            if type(tMessage) == "table" and #tMessage == 3 and tonumber(tMessage[1]) and tonumber(tMessage[2]) and tonumber(tMessage[3]) then
                local tFix = { vPosition = vector.new(tMessage[1], tMessage[2], tMessage[3]), nDistance = nDistance }
                if _bDebug then
                    print(tFix.nDistance .. " metres from " .. tostring(tFix.vPosition))
                end
                if tFix.nDistance == 0 then
                    pos1, pos2 = tFix.vPosition, nil
                else
                    -- Insert our new position in our table, with a maximum of three items. If this is close to a
                    -- previous position, replace that instead of inserting.
                    local insIndex = math.min(3, #tFixes + 1)
                    for i, older in pairs(tFixes) do
                        if (older.vPosition - tFix.vPosition):length() < 1 then
                            insIndex = i
                            break
                        end
                    end
                    tFixes[insIndex] = tFix

                    if #tFixes >= 3 then
                        if not pos1 then
                            pos1, pos2 = trilaterate(tFixes[1], tFixes[2], tFixes[3])
                        else
                            pos1, pos2 = narrow(pos1, pos2, tFixes[3])
                        end
                    end
                end
                if pos1 and not pos2 then
                    break
                end
            end

        elseif e == "timer" then
            -- We received a timeout
            local timer = p1
            if timer == timeout then
                break
            end

        end
    end

    radio.setFrequency(nPreviousChannel)

    os.cancelTimer(timeout)

    -- Return the response
    if pos1 and pos2 then
        if _bDebug then
            print("Ambiguous position")
            print("Could be " .. pos1.x .. "," .. pos1.y .. "," .. pos1.z .. " or " .. pos2.x .. "," .. pos2.y .. "," .. pos2.z)
        end
        return nil
    elseif pos1 then
        if _bDebug then
            print("Position is " .. pos1.x .. "," .. pos1.y .. "," .. pos1.z)
        end
        return pos1.x, pos1.y, pos1.z
    else
        if _bDebug then
            print("Could not determine position")
        end
        return nil
    end
end

return module