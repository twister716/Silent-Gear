# Changelog

## [Unreleased]
### Changed
- Optimize ore dictionary lookup for materials (should improve load times slightly, related to [Silent's Gems #341](https://github.com/SilentChaos512/SilentGems/issues/341))

## [0.2.1]
### Fixed
- Server crash (#12)

## [0.2.0]
Multiple internal changes, watch out for bugs!
### Added
- Bindings. There are none defined by default, but can be created with JSON files like all other part types. Use texture suffix "generic", type "binding". Most tool classes do not have a binding texture (layer will be blank).
- Lock stats subcommand (freezes an item's stats, mainly for pack/map makers) (/sgear lock_stats)
- Tool classes can be controlled with Tool Progression to some extent. Harvest levels cannot be set there, you would still need to use Gear's material JSONs. (#11)
### Changed
- Equipment JSONs can now specify any part type as a required ingredient (instead of just mains, rods, and bowstrings)
### Fixed
- Crash with Tool Progression mod (#11)

## [0.1.3]
### Added
- Configs to change repair multiplier for quick and anvil repairs
- Quark runes can now control the effect color of gear
### Changed
- Improved block matching for hammers and excavators. Ores will only match the same block, but most others can be mined together.
- Default enchanted effect color to purple (vanilla)
- Updated all tool textures with consistent shading (base mod only)

## [0.1.2]
### Added
- Configs to control vanilla gear nerfing (to disable, remove all items from the list)
- Blueprint package, an item that gives blueprints when used (pulls from a loot table)
- Rod blueprints/templates, to work around recipe conflicts (#5)
- Loot table function 'silentgear:select_tier' which can be used to generate random gear
### Changed
- Anvil repairs improved (50% of material durability), quick repair reduced to 35% of material durability
- Nerfed gear defaults to 50% max durability (up from 10%)
- Upgrades can now be applied in an anvil
- Root advancement no longer gives blueprints, player spawns with a blueprint package instead (#7)
- Stone and iron rods now accept "rodStone" and "rodIron"

## [0.1.1]
### Added
- Wool and leather grips (craft leather or a block of wool with an existing tool)
### Fixed
- Quick repair recipe matching when it shouldn't, causing some recipes to stop working (#3)
- Some tools being broken by other mods (axes on bonsai pots, for example)

## [0.1.0]
Updated for Silent Lib 3.0.0
### Added
- Main materials now display their tier in the tooltip
- More advancements!
### Changed
- New machete textures
- Blueprint outline colors, remove shift function
- Hammers and excavators are faster (50% penalty instead of 75%)
### Fixed
- Mattocks not actually working as shovels or axes
- Broken tools still appearing broken after repair

## [0.0.11]
Updated for Silent Lib 2.3.18
### Added
- Missing texture for spoon upgrade

## [0.0.10]
### Added
- Blaze/end rods are now tool rods
- Quartz-tipped upgrade
- Reach distance stat ("reach_distance" in the JSON files)
- Spoon upgrade, allows pickaxes to work as shovels
- Red card upgrade, allows items to break permanently (could be useful in machines that use tools...)
- Root advancement gives players some blueprints
- More JEI example recipes (applying tip upgrades, for instance)
- JEI info pages for many items (more to come)
### Changed
- Tool/armor items now give just a few randomized samples, instead of one for each main material (less JEI lag)
- Tooltips for tool heads improved, removed some redundant information
### Fixed
- Crafting station now works with the '+' (move items) button in JEI

Numerous other tweaks and minor fixes

## [0.0.9]
### Added
- Katanas
- Machetes
- Excavators (AOE shovels)
### Fixed
- Crafting station losing inventory when broken (#2)

## [0.0.8]
### Added
- Missing textures and models
### Changed
- Dried sinew is now a smelting recipe
### Fixed
- Some armor items not being colored

## [0.0.7] (preview 7, 1.12.2)
### Added
- New main parts: obsidian, netherrack, and terracotta
- Config to specify items that work with block-placing tools. Default list includes dank/null and a couple other items.
### Changed
- Improvements to how gear part data is passed around, which seems to improve frame rate (FPS)
- Gear can no longer be repaired with lower-tier materials
- Un-nerfed vanilla hoes. You're welcome.
### Fixed
- Materials not considering grade when displaying stats
- Armor durability being silly (added "armor durability" stat to correct this)
- Armor breaking permanently (hopefully)
- A rare(?) crash with hammers
- JEI should now recognize the crafting station as a crafting table

## [0.0.6] (preview 6, 1.12.2)
### Added
- Part analyzer, basically the same as the material grade from Silent's Gems
- Tools and armor that have ungraded parts will be assigned random grades. The grades selected have a lower average and lower maximum than those graded by the analyzer.
### Changed
- The lowest material grades now reduce stats slightly, with grade C providing no bonuses. Higher grade bonuses increased.

## [0.0.5] (preview 5, 1.12.2)
### Added
- Blueprints/templates now display an outline of the item they craft if you are holding shift
- Template textures
- More advancements! And localizations for all of them
### Changed
- Blueprints/templates have their own items now (old ones will disappear, sorry)
- Tip upgrades now have their own items (same story as above)
### Fixed
- Many(?) missing recipes

## [0.0.4] (preview 4, 1.12.2)
### Added
- Quick repair recipe, which replaces decorating
### Changed
- Block placing tools now require sneaking (fixes placing blocks when click on blocks with GUIs and such)
- Crafting materials (upgrade base, rods, sinew) have individual items now, existing ones will disappear
### Fixed
- Dagger localizations
- Command usage text

## [0.0.3] (preview 3, 1.12.2)
### Added
- User-defined materials are now working!
- Subcommand to repair held gear
### Fixed
- Example recipes done right, hopefully
- Various other minor fixes

## [0.0.2] (preview 2, 1.12.2)
### Added
- Missing blueprint recipes for daggers and sickles
- Block placing with tools handler. Entirely configurable, you can set literally any item to work with this now. Defaults to Silent Gear pickaxes, shovels, and axes.
### Fixed
- Example swords having wood guards
- NoClassDefFoundError crash, jline/internal/InputStreamReader (#1)

## [0.0.1] (preview 1, 1.12.2)
- Initial preview build

# Note

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).