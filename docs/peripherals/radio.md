---
title: Radio Antenna
---

## Radio Tower

Radio Towers are multiblock structures capable of transmitting and receiving radio network messages.

A radio tower can be up to 24 blocks tall, and each block added to it increments the maximum range of the antenna by **128** blocks.

The maximum possible range is **3072** blocks.

The initial **85% range** is considered safe for data integrity. Beyond this range data will get linearly corrupted the further you go until out of range.

The antenna, also called head, transmits at its maximum range when it is above **Y96**. Below this height the range decreases exponentially.

Radio messages are limited to the default maximum size of **8 MiB** (8,388,608 bytes), changeable in the configuration file.

## Setup

To build one, you need:

* 1x Radio Tower Base
* Zero to 22x Radio Tower Segments
* 1x Radio Tower Antenna

Place the **base**, then the **segments**, and finally the **antenna** to complete the build.

<img width="220" alt="Radio Tower" src="/assets/peripherals/radio_tower.png"/>

## Methods

The network name of the Radio Tower peripheral is `radio_tower`, you can wrap it via the peripheral functions like `peripheral.wrap` or `peripheral.find`.

* `broadcast(data: string): void` Broadcast a message to the radio network.
* `canBroadcast(): boolean` Whether a Radio Tower peripheral is capable of broadcasting data.
* `isValid(): boolean` Get whether the radio tower is built correctly and is able to transmit and/or receive.
* `setFrequency(frequency: number): void` Set the frequency of the tower. It can be **any integer between 0 and 65535 (inclusive)**.
* `getFrequency(): number` Get the frequency of the tower.
* `getHeight(): number` Get the height of the tower.

## Event

Upon receiving a message from the radio network, tuned to the frequency of the tower, the `radio_message` event will be fired to all computers connected to the peripheral:

1. `string`: The event name. Always `radio_message`.
2. `string`: The side of the peripheral that received the message.
3. `string`: The message data.
4. `number`: The distance between the sender and the receiver.

## Radio Tower Range

The formula to calculate the maximum range of the radio tower is as follow:

$$\large
\text{EMR} = \begin{cases}
t_h\cdot s_r\cdot 1.0375^{(y–96)}&y \lt 96 \\
t_h\cdot s_r&y \ge 96 \\
\end{cases}
$$

Where:
- $y$ is the Y coordinate of the antenna (the top-most block).
- $t_h$ is the total height of the antenna tower.
- $s_r$ is the `radioTowerSegmentRange` configuration value (128 by default).
- $\text{EMR}$ is the EffectiveMaxRange of the tower.

Graph of the range with default configurations and maximum total height of the tower for different Y levels of the antenna:

[<img width="1200" height="800" alt="Radio Range" src="/assets/peripherals/radio_range.png" />](https://www.desmos.com/calculator/wbyh3nkjsy)

## Mini radio antenna

A single-block radio antenna can be crafted, but will only be able to broadcast to and receive from radio towers, and relies on their range.

This antenna can be attached to Pocket Computers and Turtles.

<img width="200" alt="Radio Antenna" src="/assets/peripherals/radio_antenna.png" />
