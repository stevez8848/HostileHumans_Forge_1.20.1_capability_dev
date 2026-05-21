Main porting work

- Ported the mod from Minecraft Forge 1.18.2 to Minecraft Forge 1.20.1.
- Updated the build environment to ForgeGradle 6 / Gradle 8.7.
- Updated the target Minecraft version to 1.20.1.
- Adapted the code for 1.20.1 API changes, including:
  - `level()` usage
  - `onGround()` usage
  - `Component.literal`
  - registry access changes
  - entity spawn/event handling changes
  - renderer layer constructor changes
  - various Forge 1.20.1 compatibility adjustments
- Migrated older worldgen structure data from the previous format to the 1.20.1 structure / jigsaw data format.
- Fixed crashes related to missing or unbound structures such as `hostile_humans:cottage`.
- Updated a `WalkNodeEvaluator` mixin injection signature for 1.20.1 compatibility.
- Fixed a server-side crash caused by directly loading `HumansServerDataClientSync` in some runtime environments.

Epic Fight compatibility

- Added optional Epic Fight compatibility without making Epic Fight a hard dependency.
- Added optional `mods.toml` dependency entries for:
  - `epicfight`, non-mandatory
  - `epicfightx`, non-mandatory
- Added safety checks so the mod still works when Epic Fight is not installed.
- Added Epic Fight mobpatch data for:
  - `hostile_humans:human_tier1`
  - `hostile_humans:human_tier2`
  - `hostile_humans:human_roamer`
- Kept the base Epic Fight data using `epicfight:` animation IDs so it can work with Epic Fight alone.
- Added a separate built-in datapack overlay for EpicFight:Extra, only registered when `epicfightx` is loaded.
- The EpicFight:Extra overlay uses `epicfightx:` animation IDs where available.
- Verified and corrected several EpicFight:Extra animation IDs by checking registered animation classes rather than relying only on resource paths.
- Corrected the spear dash animation to use the valid EFX path `epicfightx:biped/combat/spear_onehand_dash`.

Human combat changes for Epic Fight-style behavior

- Added optional generation support for Epic Fight melee weapons:
  - spear
  - longsword
  - dagger
  - greatsword
  - glove / fist weapon
- Increased the chance for dual sword-style equipment:
  - around 12% for tier 1 humans
  - around 18% for roamer humans
  - around 24% for tier 2 humans
- Prevented shield/totem offhand generation when a dual-sword setup is selected.
- Updated melee weapon detection so Human AI recognizes Epic Fight weapons as melee weapons.
- Slightly reduced rolling frequency in Epic Fight mobpatch behavior.
- Added a conservative “pseudo guard” mechanic:
  - if a Human is attacked from the front
  - and the weapon cooldown is above 70%
  - and a 20% random chance succeeds
  - the final damage is reduced by 70%
  - a shield block sound is played
  - an Epic Fight / EpicFight:Extra guard-hit animation is attempted if available
- Added pseudo advanced skill behavior for Epic Fight-style weapons:
  - spear can trigger a Heartpiercer-like effect
  - longsword can trigger a Sweeping Edge-like effect
  - dagger can trigger an Eviscerate-like effect
  - greatsword can trigger The Guillotine / Steel Whirlwind-like effects
  - glove can trigger a Relentless Combo-like effect
  - dual sword can trigger a Dancing Edge-like effect
- These pseudo skills are intentionally conservative and do not attempt to directly use the player SkillContainer system. They mainly apply server-side effects such as extra damage, knockback, brief slowing, or small area attacks, while optionally playing EF/EFX animations when available.

Skin additions

- Added several new Human skin textures from the skin links provided during testing.
- Duplicate skin links were removed before adding them.
- The new textures were added to the existing Human skin pool.

Dialogue / chat additions

- Expanded Human chat dialogue.
- Increased the frequency of Human combat dialogue by roughly 2-4 times compared with the previous behavior.
- Humans can now continue sending occasional combat lines instead of speaking only once per lifetime.
- Added more dialogue lines in multiple languages:
  - English
  - German
  - French
  - Chinese
  - Japanese
  - Korean
- These lines are sent directly as literal chat messages and are not tied to the client language setting.

Compatibility and stability fixes

- Added a compatibility safety layer so Epic Fight integration is dormant when Epic Fight is not installed.
- Added an EpicFight:Extra-specific animation datapack that only activates when EpicFight:Extra is present.
- Fixed a crash related to server/client sync class loading.
- Investigated a sudden in-game crash where logs showed repeated OpenGL depth buffer errors, likely related to a rendering/mod interaction involving Oculus/Embeddium/Epic Fight-style animation rendering rather than the core Hostile Humans logic.
- The latest test build currently passes `gradlew build`.

Current build status

- Target: Minecraft Forge 1.20.1
- Forge loader range: `[47,)`
- Java: tested with Java 21 in the local build environment
- Build result: successful
- Current local version string: `1.4.13-PatreonNames`
