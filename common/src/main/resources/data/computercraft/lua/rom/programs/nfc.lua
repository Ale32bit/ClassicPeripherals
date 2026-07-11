local nfcReader = peripheral.find("nfc_reader")

local args = { ... }

local function printUsages()
    local programName = arg[0] or fs.getName(shell.getRunningProgram())
    print("Usages:")
    print(programName .. " read")
    print(programName .. " write <data> [label]")
end

local function assertReader()
    if not nfcReader then
        error("NFC Reader not found", 0)
    end
end

local function readNfc()
    assertReader()
    print("Waiting for NFC card...")
    local _, name, data = os.pullEvent("nfc_data")
    print(string.format("[%s] %s", name, data))
end

local function writeNfc(data, label)
    assertReader()
    local nfcReaderName = peripheral.getName(nfcReader)
    nfcReader.write(data, label)
    print("Waiting for NFC write completion...")
    local _, name, success, reason
    while true do
        _, name, success, reason = os.pullEvent("nfc_write")
        if name == nfcReaderName then
            break
        end
    end

    if success then
        print("Write complete!")
    else
        error("Could not write NFC data: " .. reason, 0)
    end
end

local command = args[1]

if not command then
    printUsages()
    return
end

if command == "read" then
    readNfc()
elseif command == "write" then
    local data = args[2]
    local label = args[3]

    if not data then
        printUsages()
        return
    end

    writeNfc(data, label)
else
    printUsages()
end