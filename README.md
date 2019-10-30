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
- [ ] Player meat drops
  - [ ] Add username to item name
- [x] If entity is on fire, drop cooked meat

 **NBT Advanced Features:**

- [ ] Create [fat jar](https://www.baeldung.com/gradle-fat-jar) with [NBT API](https://www.spigotmc.org/resources/nbt-api.7939/)
- [ ] Add NBT field to indicate what cooked name of meat should be?


**Configuration options:**

- [ ] "Player" configuration section
	- [ ] Enable/disable
	- [ ] Min/max drops
	- [ ] Item base
	- [ ] Name type to use? Real vs. display vs. "Raw (Human|Player|Steve)"
- [x] Config option for entity list
- [x] Config option for global enable/disable
- [x] Per-mob item base (chicken/steak/etc.)
- [x] Per-mob min/max drops
- [x] Per-mob custom drop name
- [x] Per-mob enable/disable

**Commands:**

- [x] `/meat`
  - Prints info about the plugin
  - [x] Uses plugin version from `plugin.yml`
  - [x] Permission `moremeat.meat` (default)
- [x] `/meat <enable/disable>`
  - Sets global `enabled` option to `true` or `false`
  - [x] Permission `moremeat.toggle` (OP)
- [x] `/meat reload`
  - Reloads the config
  - **TODO:** Write custom reload class so we can catch config reload errors.
  - [x] Permission `moremeat.reload` (OP)

**Reminders:**

- [ ] Set version to 1.0.0 in `plugin.yml`
- [ ] Set version to 1.0.0 in `build.gradle`
- [ ] Tag commit `v1.0.0`
- [ ] Update `README.md`
