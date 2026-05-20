Subject: Request for review and permission regarding an unofficial Forge 1.20.1 compatibility port of Hostile Humans

Hello Craft1x,

I hope you are doing well.

I am contacting you regarding your Minecraft mod, Hostile Humans. I have been working on a local compatibility port from Forge 1.18.2 to Forge 1.20.1, together with some optional compatibility experiments for Epic Fight and EpicFight:Extra.

I should also clarify the starting point of this work. When I began this port, I was not aware that you had recently published an official 1.20.1 beta update around April 16. The code I worked from was based on the previously available 1.18.2 source/update from August 25, 2024, which I believed to be the latest public version at the time. This was not intended to bypass or replace your own 1.20.1 beta work. If your official beta has already solved some of the same problems, I would be happy to compare my changes against it, discard duplicated work, or reframe my work as compatibility notes/patches instead of a separate port.

Before publishing or sharing anything publicly, I would like to ask for your opinion and permission. I want to make sure the project is handled respectfully, that your original authorship is clearly credited, and that the licensing terms are followed correctly.

I noticed that the project includes a GNU General Public License v2.0 license file. My understanding is that GPL-2.0 allows modified versions to be made and redistributed as long as the modified work remains under GPL-2.0, the source code is made available, the original notices are preserved, and modified files are clearly marked as changed. However, I also noticed that the current `mods.toml` metadata says `license="All rights reserved"`. Because of that, I would like to ask how you would prefer this port/fork to be handled before I release anything.

In summary, the local work includes:

- A port started from the August 25, 2024 Forge 1.18.2 codebase/source state, before I knew about the later official 1.20.1 beta.
- A Forge 1.20.1 port of Hostile Humans.
- Fixes for several crashes encountered during testing.
- Updated worldgen/structure data for Minecraft 1.20.1.
- Optional Epic Fight compatibility that does not make Epic Fight a hard dependency.
- Optional EpicFight:Extra animation support through a separate built-in datapack overlay.
- Conservative Human combat behavior inspired by Epic Fight, including pseudo guard and pseudo advanced skill effects.
- Additional Human skins.
- Expanded multilingual Human chat dialogue.
- Compatibility safeguards so the mod can still run without Epic Fight installed.

I have prepared a detailed changelog as a separate attachment/file named `CHANGELOG.md`. Please see that file for the full list of changes and implementation notes.

Because CurseForge web comments do not support attaching files, I cannot directly include the source code, built mod file, or full changelog there. If you are willing to review them, please contact me on Discord and I can send you the source code, the test mod jar, and the detailed change list there.

Could you please review the changelog and let me know your opinion on the changes? In particular, I would appreciate your guidance on the following points:

- Whether you would prefer that I compare this work against your official 1.20.1 beta first and only keep non-duplicated fixes or compatibility additions.
- Whether you are comfortable with this Forge 1.20.1 port existing as an unofficial fork.
- Whether you would allow it to be published publicly.
- Whether you would prefer that I submit the changes to you instead of publishing a separate fork.
- Whether you would prefer that only a patch/diff be shared rather than a full modified jar.
- Whether any of the Epic Fight / EpicFight:Extra compatibility behavior should be removed, renamed, or changed.
- How you would like the license metadata conflict between the GPL-2.0 license file and the `All rights reserved` mod metadata to be handled.

I should also mention that I am still a beginner mod developer. Some of the implementation choices may be rough, overly conservative, or not aligned with how you would prefer the original project to evolve. If you notice any mistakes, poor design choices, unsafe compatibility logic, or misunderstandings of your codebase, I would sincerely appreciate your corrections and advice. I also ask for your understanding if any part of the port is immature.

There are also some possible follow-up ideas I would like to explore only if you are comfortable with them. For example, I am interested in adding optional compatibility with TaCZ in the future, so Human entities could better interact with gun-based combat systems in modpacks. I would treat this as optional compatibility, not a hard dependency, and I would appreciate your opinion before going further with that direction.

If you allow publication, I will make sure to:

- clearly credit you / Craft1x as the original author of Hostile Humans
- preserve the original license and copyright notices
- keep the modified source code available
- mark the project clearly as an unofficial port/fork unless you prefer different wording
- avoid implying that it is an official update from you
- remove or adjust any changes you are not comfortable with
- follow any additional requirements you request

I made this port because I appreciate Hostile Humans and wanted to keep it playable in a newer Forge 1.20.1 environment. Thank you for creating the original mod.

Please let me know what you think when you have time.

Best regards,

[Your Name]
