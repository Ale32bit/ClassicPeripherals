---
title: Scanner
hide:
  - navigation
---

<div class="infobox" markdown>

![Peripheral](../assets/blocks/scanner.png)

| Label     | Value     |
|-----------|-----------|
| **ID**    | `scanner` |
| **Since** | v0.5.0    |

</div>

The Scanner is a peripheral capable of reading text and colors from printed paper.

## Methods

The network name of the Scanner peripheral is `scanner`, you can wrap it via the peripheral functions like `peripheral.wrap` or `peripheral.find`.

* `hasPages(): boolean` Whether the Scanner can read the pages of the item.
* `getLabel(): string` Get the title of the printed paper.
* `getPageCount(): number` Get the number of pages.
* `eject(): void` Eject the inserted item to the front of the block.
* `scan(): table` Read the contents of the printout and returns a list of pages.

### Pages data

The `scan()` function returns a list of papers, containing two tables: one for text lines and one for colors respectively.
Each table of the text and color is a list of the lines of the page.

The colors are in the paint/blit format, that is, each hexadecimal character defines [one of the 16 colors](https://tweaked.cc/module/colors.html).