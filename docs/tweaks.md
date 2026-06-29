---
title: Tweaks
hide:
  - navigation
---

These tweaks modify the original behavior of **CC: Tweaked**.

## Ender Modem nerf

The Ender Modem has been heavily weakened while retaining its unique features:

- Range decreased to 2x of normal wireless modems, unaffected by weather.
- Cross-dimensional messaging is limited to *local* block coordinates. That is:
  - If an Ender Modem is placed in the overworld at the coordinates **150 64 150**, the messages are transmitted to the other dimensions from the same block coordinates, with a range of 8 blocks. Coordinate scaling still applies.
  - The Nether will receive the messages at coordinates **18 64 18** (1:8 scaling).
  - The End will receive the messages at coordinates **150 64 150** (1:1 scaling).

## Redstone Relay event enhancement

Whenever a Redstone Relay peripheral receives a redstone input change, the `redstone` event is fired alone without any parameter.

This tweak adds the network name of the peripheral to the event parameters to facilitate the redstone input lookup.