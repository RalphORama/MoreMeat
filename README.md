# MoreMeat

[![Build Status](https://travis-ci.org/RalphORama/MoreMeat.svg?branch=master)](https://travis-ci.org/RalphORama/MoreMeat)

MoreMeat is a Spigot plugin for adding more food drops to your Minecraft server!

## Installation & Usage

:warning: Don't use this plugin yet!  Wait for the v1.0.0 release (coming soon&trade;).

## Development

Working on the plugin is easy:

1. Clone the repository
2. Make your changes
3. Run `./gradlew` (default tasks are `clean`, `build`, `jar`)
4. Copy `MoreMeat-<version>.jar` from `build/` to your plugins folder

## Milestones

### v1.0

**Features**
- [ ] Listener for "Raw \<Meat\>" -> "Cooked \<Meat\>"
- [ ] If entity is on fire, drop cooked meat
- [ ] Player meat drops
  - [ ] Add username to item name

**Configuration options:**

- [x] Config option for entity list
- [x] Config option for global enable/disable
- [x] Per-mob item base (chicken/steak/etc.)
- [x] Per-mob min/max drops
- [x] Per-mob custom drop name
- [x] Per-mob enable/disable

**Commands:**

- [x] `/meat`
  - Prints info about the plugin
  - [x] Permission `moremeat.meat` (default)
- [x] `/meat <enable/disable>`
  - Sets global `enabled` option to `true` or `false`
  - [x] Permission `moremeat.toggle` (OP)
- [x] `/meat reload`
  - Reloads the config
  - **TODO:** Write custom reload class so we can catch config reload errors.
  - [x] Permission `moremeat.reload` (OP)
