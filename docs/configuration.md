## Settings

### enderModemNerf

This setting toggles the nerf of the Ender Modems.

Enabling this limits how the peripheral works, disabling it restores the original behaviour.

### betterRedstoneRelayEvent

This setting toggles the improved `redstone` event when emitted by Redstone Relays.

## Default configuration

```toml title="classicperipherals.toml"
# The maximum height of the radio tower. Including base and head.
# range: 2 - 2147483647
# default: 24
radioTowerMaxHeight = 24
# The minimum height of the radio tower. Including base and head.
# range: 2 - 2147483647
# default: 2
radioTowerMinHeight = 2
# Range per segment.
# range: 1 - 2147483647
# default: 128
radioTowerSegmentRange = 128
# The loss factor of the radio tower.
# range: 0.0 - 1.0
# default: 0.15
radioTowerLossFactor = 0.15
# The maximum message size in bytes.
# range: 1 - 2147483647
# default: 8388608
radioTowerMaxMessageSize = 8388608
# Whether the Mini Antenna can broadcast instead of just receiving.
# default: true
antennaCanBroadcast = true
# The maximum size in bytes of the NFC card and RFID badge data.
# range: 1 - 2147483647
# default: 128
nfcMaxDataSize = 128
# The maximum range of the RFID scanner.
# range: 1 - 2147483647
# default: 8
rfidScanRange = 8
# Enable the Ender Modem nerf.
# default: true
enderModemNerf = true
# Ender Modem cross-dimensional range.
# range: 1.0 - 3.4028234663852886E38
# default: 8.0
enderModemCrossDimensionalRange = 8.0
# Enable the improvement of the Redstone Relay event.
# default: true
betterRedstoneRelayEvent = true
```