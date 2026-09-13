---
title: Body Area Network API
hide:
  - navigation
---

<div class="infobox" markdown>

| Label      | Value    |
|------------|----------|
| **Module** | `os.ban` |
| **Since**  | v0.6.4   |

</div>

The Body Area Network API (BAN API) allows holdable and equippable computers of a player to communicate with each other.

The BAN API implements the [Modem API](https://tweaked.cc/peripheral/modem.html) and is not a peripheral, instead it can
be used by loading the module `os.ban` via the `require()` function.

Just like the modem peripheral, receiving messages is done by opening channels and listening for `modem_message` events.
The `side` event parameter for BAN is `"computer"`.