# Changelog for the Unofficial Forge 1.20.1 Compatibility Port

This changelog summarizes the local compatibility and feature work performed on the Hostile Humans mod for testing and possible review by the original author.

## Important Context

- This work was started from the Forge 1.18.2 source/update that was available from the August 25, 2024 release.
- At the time this local porting work began, the author of this port was not aware that the original author had later published an official 1.20.1 beta update around April 16.
- The changes in this file should therefore be understood as an independent local compatibility port and test branch based on the older 1.18.2 codebase, not as an intentional replacement for the official 1.20.1 beta.
- If the official 1.20.1 beta already includes equivalent fixes, those parts should be considered duplicated work and can be removed, rewritten, or treated only as notes for comparison.
- Before any public release, this work should be reviewed against the official 1.20.1 beta and adjusted according to the original author's preference.

## Porting and Build Compatibility

- Ported the mod from Minecraft Forge 1.18.2 to Minecraft Forge 1.20.1.
- Updated the build environment to ForgeGradle 6 and Gradle 8.7.
- Updated the target Minecraft version to 1.20.1.
- Adapted code for Forge/Minecraft 1.20.1 API changes, including entity accessors, component text creation, event usage, registry access, entity spawning, and renderer layer construction.
- Updated the project to build successfully in the current local environment.
- Verified the latest build with `gradlew build`.

## Worldgen and Runtime Fixes

- Migrated older structure/worldgen data to the 1.20.1 structure and jigsaw data format.
- Fixed crashes caused by missing or unbound Hostile Humans structures during world loading.
- Updated a `WalkNodeEvaluator` mixin injection signature for Minecraft 1.20.1 compatibility.
- Fixed a runtime/server crash caused by directly loading `HumansServerDataClientSync` in environments where that class was not safely available.
- Adjusted server data synchronization to avoid the previous `NoClassDefFoundError` crash during player login or world entry.

## Optional Epic Fight Compatibility

- Added optional Epic Fight compatibility without making Epic Fight a hard dependency.
- Added non-mandatory dependency metadata for `epicfight`.
- Added non-mandatory dependency metadata for `epicfightx`.
- Added runtime checks so Hostile Humans remains usable when Epic Fight is not installed.
- Added Epic Fight mobpatch data for:
  - `hostile_humans:human_tier1`
  - `hostile_humans:human_tier2`
  - `hostile_humans:human_roamer`
- Kept the base Epic Fight integration on verified `epicfight:` animation IDs so it can work with Epic Fight alone.
- Added a separate built-in EpicFight:Extra datapack overlay that is only registered when `epicfightx` is detected.
- Added EpicFight:Extra animation mappings through the optional overlay.
- Verified and corrected several EpicFight:Extra animation IDs by checking registered animation classes rather than relying only on resource file names.
- Corrected the EpicFight:Extra spear dash animation to use the valid ID `epicfightx:biped/combat/spear_onehand_dash`.

## Human Combat and Epic Fight-style Behavior

- Added optional generation support for Epic Fight melee weapons, including spear, longsword, dagger, greatsword, and glove/fist weapons.
- Updated Human melee weapon detection so Epic Fight melee weapons are treated as valid melee weapons by the existing AI.
- Increased the chance for dual-sword style equipment:
  - about 12% for tier 1 Humans
  - about 18% for roamer Humans
  - about 24% for tier 2 Humans
- Prevented shield or totem offhand generation when a dual-sword setup is selected.
- Slightly reduced the frequency of Epic Fight rolling behavior in combat.

## Pseudo Guard Behavior

- Added a conservative Epic Fight-style pseudo guard mechanic for Humans.
- When a Human is attacked from the front, the pseudo guard can trigger if:
  - the Human is holding a sword-like or Epic Fight melee weapon
  - weapon cooldown is above 70%
  - a 20% random chance succeeds
- On a successful pseudo guard:
  - final damage is reduced by 70%
  - a shield block sound is played
  - an Epic Fight or EpicFight:Extra guard-hit animation is attempted when available
- The guard animation system uses soft/reflection-based compatibility so it does not require Epic Fight at class-loading time.

## Pseudo Advanced Skills

- Added conservative pseudo advanced skill behavior for several Epic Fight-style weapons.
- These skills intentionally do not use the player `SkillContainer` system directly, reducing the risk of conflicts with Epic Fight's player skill implementation.
- Pseudo skills apply server-side effects such as extra damage, knockback, brief slowing, or small area attacks.
- Optional EF/EFX animations are attempted when available.
- Current weapon mappings include:
  - spear: Heartpiercer-like effect
  - longsword: Sweeping Edge-like effect
  - dagger: Eviscerate-like effect
  - greatsword: The Guillotine / Steel Whirlwind-like effects
  - glove/fist weapon: Relentless Combo-like effect
  - dual sword: Dancing Edge-like effect
- Current pseudo skill trigger behavior:
  - around 12% trigger chance after a successful hit
  - about 80-130 ticks of cooldown

## EpicFight:Extra Animation Overlay

- Added a dedicated built-in datapack overlay for EpicFight:Extra.
- The overlay is registered only when EpicFight:Extra is installed.
- The base Epic Fight mobpatch remains available for Epic Fight-only setups.
- EpicFight:Extra animations are preferred where verified.
- Epic Fight base animations remain the fallback for compatibility.

## Skins

- Added several new Human skin textures from the provided Laby/Mojang skin links.
- Removed duplicate skin entries before adding the textures.
- Integrated the new skins into the existing Human skin pool.

## Dialogue and Chat Behavior

- Expanded the Human dialogue pool.
- Added more combat and warning lines in multiple languages:
  - English
  - German
  - French
  - Chinese
  - Japanese
  - Korean
- Dialogue is sent as literal chat text and is not tied to the player's client language setting.
- Increased Human dialogue frequency by roughly 2-4 times compared with the previous behavior.
- Humans can now continue sending occasional combat lines instead of speaking only once per lifetime.
- Added a combat dialogue cooldown so repeated lines are more frequent but not constant.

## Compatibility Safeguards

- Epic Fight is not a hard dependency.
- EpicFight:Extra is not a hard dependency.
- EF/EFX-specific behavior is skipped when the corresponding mod is not loaded.
- EF/EFX animation calls use fallback logic where possible.
- If an animation lookup fails, the gameplay effect can still continue without the animation.

## Possible Follow-up Work

- Consider optional compatibility with TaCZ in a future version.
- Keep future compatibility modules optional rather than hard dependencies.
- Review any further compatibility work with the original author before public release where possible.
- Compare this local port against the official 1.20.1 beta and remove or rewrite duplicated work if needed.

## Current Known Notes and Risks

- The Epic Fight integration is still experimental and conservative.
- Human entities are not true `Player` subclasses and do not directly use Epic Fight's player skill container system.
- Pseudo skills are designed to imitate selected combat effects without fully injecting Humans into the player-only skill pipeline.
- The developer of this port is still a beginner mod developer, so some implementation choices may need review, cleanup, or correction from a more experienced maintainer.
- Some rendering crashes were observed during testing with OpenGL depth buffer errors, likely related to a rendering/mod interaction involving Oculus/Embeddium and EF/EFX-style animation rendering. This needs more testing before public release.
- Some deprecated API warnings remain during compilation.
- A mixin shadow warning remains and should be reviewed before any formal release.

## Build Verification

- `gradlew build` completed successfully in the local development environment.
- The latest test jar was built and copied into a local CurseForge test instance for runtime testing.
