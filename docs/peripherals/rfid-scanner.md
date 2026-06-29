---
title: RFID Scanner
---

The RFID Scanner is a peripheral capable of scanning for RFID Badges in a range of **8 blocks** in player inventories.

Just like the NFC Card, a RFID Badge is capable of holding string data up to **128 characters** and the `readonly` flag can be irreversibly enabled to prevent further writes.

Interacting with a living entity will permanently inject the RFID Badge, consuming the item. The RFID Scanner will then be able to scan the entity. Injecting a new RFID Badge again will override the current data. (since 0.3.0)

You can also put dyes along the RFID Badge in a crafting table to dye the badge.

<img width="500" alt="RFID Scanner" src="/assets/peripherals/rfid_scanner.png" />

## Methods

The network name of the RFID Scanner peripheral is `rfid_scanner`, you can wrap it via the peripheral functions like `peripheral.wrap` or `peripheral.find`.

* `scan(): table` Scan the area for RFID Badges, returns a list of tables with the properties `data: string` and `distance: number`:
```lua
-- Example of returned value
{
  {
    data = "example data",
    distance = 3.14
  }, [...]
}
```

## Writing data

The RFID Badge requires the [NFC Reader](nfc-reader.md#methods) to write data