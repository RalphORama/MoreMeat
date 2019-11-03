# MoreMeat

[![Build Status](https://travis-ci.org/RalphORama/MoreMeat.svg?branch=master)](https://travis-ci.org/RalphORama/MoreMeat)

MoreMeat is a Spigot plugin for adding more food drops to your Minecraft server!


## Installation

1. Download the latest jar from the [releases page](https://github.com/RalphORama/MoreMeat/releases).  (Spigot page coming soon!)
2. Drop `MoreMeat-*.*.*.jar` into your `plugins` folder
3. Restart your server!


# Configuration

See the [wiki page](https://github.com/RalphORama/MoreMeat/wiki/Configuration) for more information about configuring the plugin.


# Usage

| Command                  | Description                                  | Permission        | Default Access              |
|--------------------------|----------------------------------------------|-------------------|-----------------------------|
| `/meat`                  | Shows plugin information.                    | `moremeat.meat`   | :heavy_check_mark: Everyone |
| `/meat <enable/disable>` | Enables/disables the plugin's functionality. | `moremeat.toggle` |            :x: OP           |
| `/meat reload`           | Reloads `config.yml`.<sup>1</sup>             | `moremeat.reload` |            :x: OP           |

<sup>1</sup> May fail silently if run ingame, check server console (see [#1](https://github.com/RalphORama/MoreMeat/issues/1)).


## Development

Working on the plugin is easy:

1. Clone the repository
2. Make your changes
3. Run `./gradlew` (default tasks are `clean`, `build`, `jar`)
4. Copy `MoreMeat-<version>.jar` from `build/` to your plugins folder.
5. Test!


## Milestones

### v1.1.0

- [ ] Custom configuration reloading class to catch `InvalidConfigurationException`
- [ ] Playtesting and bugfixing.


### v1.1.0-NBT

- [ ] Create NBT branch.
- [ ] Modify `build.gradle` and `.travis.yml` accordingly.
- [ ] Add [NBT API](https://www.spigotmc.org/resources/nbt-api.7939/) and set up a [Fat Jar](https://www.baeldung.com/gradle-fat-jar) build in Gradle.
- [ ] Use NBT tags to support Raw \<Username\> Meat -\> Cooked \<Username\> Meat.
