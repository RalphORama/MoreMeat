# MoreMeat

[![Build Status](https://travis-ci.org/RalphORama/MoreMeat.svg?branch=master)](https://travis-ci.org/RalphORama/MoreMeat)

Additional food for your players.

## Milestones

### v1.0

**Configuration options:**

- [x] Config option for entity list
- [x] Config option for global enable/disable
- [x] Per-mob item base (chicken/steak/etc.)
- [x] Per-mob min/max drops
- [x] Per-mob custom drop name
- [x] Per-mob enable/disable

**Commands:**

- [ ] `/meat`
  - Prints info about the plugin
  - [ ] Permission `moremeat.moremeat` (default)
- [ ] `/meat <enable/disable>`
  - Sets global `enabled` option to `true` or `false`
  - [ ] Permission `moremeat.toggle` (OP)
- [ ] `/meat reload`
  - Reloads the config
  - [ ] Permission `moremeat.reload` (OP)

### v2.0

- [ ] Listener for "Raw <Meat>" -> "Cooked <Meat>"
- [ ] Player meat drops
  - [ ] Add username to item name