---
title: NFC Reader
hide:
  - navigation
---

<div class="infobox" markdown>

![Peripheral](../assets/blocks/nfc_reader.png)

| Label     | Value        |
|-----------|--------------|
| **ID**    | `nfc_reader` |
| **Since** | v0.1.0       |

</div>

The NFC Reader is a peripheral capable of reading and writing data from/to NFC Cards.

A NFC Card is capable of holding string data up to **128 characters** (configurable) and the `readonly` flag can be
irreversibly enabled to prevent further writes.

You can also put dyes along the NFC Card in a crafting table to dye the card.

## Methods

The network name of the NFC Reader peripheral is `nfc_reader`, you can wrap it via the peripheral functions like
`peripheral.wrap` or `peripheral.find`.

* `write(data: string, label: string|nil, readonly: boolean|nil, privateKey: string|nil): void` Submit data to write on
  the next NFC Card that interacts with the reader.
    * `data`: Data to write up to 128 characters (configurable).
    * `label`: Label to set on the card. Set to `nil` to clear.
    * `readonly`: Irreversibly flag the card as read-only, this will prevent further writes to the card.
    * `privateKey`: A 32-bytes long string. Will emit its public key on swipe. (only compatible with NFC Cards).
* `cancelWrite(): void` Cancel the submitted write command.
* `sign(data: string): string` Submit data to sign with the NFC Card holding a private key.
* `cancelSign(): void` Cancel the pending signing action.

## Events

### `nfc_data`

Upon interacting with the reader while holding a NFC Card with data, the `nfc_data` event is fired to all computers
connected to the peripheral.

1. `string`: The event name. Always `nfc_data`.
2. `string`: The side of the peripheral that read the card.
3. `string`: The card data.
4. `string`: The NFC Card public key.

### `nfc_write`

Upon attempting to write data to a NFC Card, the `nfc_write` event is fired to all computers connected to the
peripheral.

1. `string`: The event name. Always `nfc_write`.
2. `string`: The side of the peripheral that tried to write data to a card.
3. `boolean`: Whether the write attempt was successful.
4. `string`: Reason message of the attempt: `"success"` for successful write; `"readonly"` for failed write due to
   read-only card.

### `nfc_sign`

When signing a message with a NFC Card, while in signing mode, the `nfc_sign` event is fired to all computers connected
to the peripheral.

1. `string`: The event name. Always `nfc_sign`.
2. `string`: The side of the peripheral that signed the data.
3. `string`: The signature of the message.
4. `string`: The public key of the NFC Card.

## Pocket computer behaviour (v0.3.0+)

Pocket computers come with a 2-way NFC adapter pre-installed, usable by loading the module `os.nfc`
(`require("os.nfc")`).

Using a NFC Card with data on a player will enqueue the [`nfc_data`](#nfc_data) event to all pocket computers of the
target player, with the side parameter being `"internal"`.

Furthermore, pocket computers are capable of acting like NFC writers and readers, interfacing with other pocket
computers and even NFC Readers peripherals.

Interacting with a player while crouching, or interacting with a NFC Reader, will behave the same as NFC Cards.

Interacting with a NFC Reader in `write` mode (yellow status light) will emit the [`nfc_data`](#nfc_data) event on the
pocket computer.

### Methods

* `write(data: string): void` Submit data to write to the target.
    * `data`: Data to write up to 128 characters (configurable).
* `pop(): string?` Cancel the submitted write command and returns the data submitted or `nil`.
* `setPrivateKey(key: string): void` Set the private key when sending NFC data to a target.