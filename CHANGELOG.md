## 更新日志

- 调整：在不减少 Human 皮肤多样性的前提下重做渲染稳定性策略；恢复 `skin1`-`skin43` 完整随机皮肤池，移除同一 `HumanRenderer` 实例中按实体动态切换 slim/classic 主模型的逻辑，并在每次渲染前重置 `PlayerModel` 可见性，降低长期游玩后模型/贴图状态串扰风险。
- 调整：将 Human 皮肤保险从“全员默认贴图”改为保守安全皮肤池；新生成 Human 仅从 `skin1`-`skin6`、`skin8`-`skin13` 中随机选择，旧存档中不在安全池内的 variant 自动回退到默认 `skin1`。
- 修复：进一步收紧 Human 皮肤渲染保险；由于纹理错误会随游玩时间扩大，临时禁用 Human 随机皮肤渲染，所有旧存档与新生成 Human 均强制使用默认 `skin1` 和 classic 模型，以优先保证长期游玩稳定性。
- 修复：新增 Human 皮肤渲染保险；将已观察到仍可能渲染异常的高风险 Laby variant `skin38`-`skin43` 标记为 fallback，旧存档中这些 variant 会渲染默认 `skin1`，新生成 Human 也不再随机选择这些皮肤。
- 修复：为规避 Human 使用 EF/EFX 翻滚动画时短暂不渲染的问题，移除 mobpatch 中的 `roll_forward` / `roll_backward` 动画引用，统一替换为 `step_right` / `step_left` 横向闪避；保留闪避行为，但避免翻滚动作将非玩家实体模型推入异常渲染/裁剪状态。
- 修复：为 Laby 皮肤中标记为 slim 的 Human variant 启用 `PLAYER_SLIM` 渲染模型；`skin38`、`skin39`、`skin41`、`skin42`、`skin43` 不再被强制按 Steve/classic 手臂宽度渲染，减少手臂和袖子贴图错位/拉伸。
- 调整：再次提高 Human 聊天框对话频率；默认 `greet_chance` 从 `0.05` 提高到 `0.08`，首次发现玩家的触发倍率从 3 倍提高到 5 倍，战斗中续聊概率约翻倍，并将续聊冷却从约 10-25 秒缩短为约 5-12 秒。
- 修复：重写 Human 开箱 AI 的箱子开关计数逻辑；现在靠近箱子时只调用一次 `incrementOpeners`，结束或被打断时只调用一次 `decrementOpeners`，避免每 tick 增加开箱计数导致箱子状态、音效或事件异常残留。
- 修复：修正 Epic Fight mobpatch 中非法武器分类 `ranged`，改为 EF 20.14.x 可识别的 `bow` / `crossbow`，避免 `Can't deserialize mob capability: ... Enum name ranged does not exist in weapon_category` 导致 Human 的 EF patch 整体加载失败、没有 EF 动画。
- 修复：降低 Human 皮肤不渲染风险；未知皮肤 variant 现在回退到 `skin1`，Human 基础渲染类型从 `entityTranslucent` 调整为 `entityCutoutNoCull`，并按玩家皮肤处理方式将所有 Human 皮肤基础身体层 alpha 修正为不透明，保留外层装饰透明效果。

- 调整：提高 Human 聊天框喊话频率；首次发现玩家的喊话概率提升到约 3 倍，并允许持续战斗中按冷却再次随机喊话，避免每个 Human 一生只说一次。
- 新增：继续扩展 Human 对话池，增加更多英文、德文、法文、中文、日文和韩文战斗/警告台词。
- 兼容性：新增 Epic Fight 可选兼容保险；`epicfight` 与 `epicfightx` 均为非硬依赖，未安装 EF 时联动代码自动休眠。
- 调整：主 `epicfight_mobpatch` 数据恢复为 `epicfight:` 原版动画基线，确保只安装 Epic Fight、不安装 EpicFight:Extra 时仍可解析。
- 新增：当检测到 `epicfightx` 时自动注册必启用内置数据包 `hostile_humans_efx`，覆盖 Human mobpatch 为 EFX 优化动画；未安装 EFX 时不会注册该覆盖包。
- 新增：扩展 Human 聊天框喊话池，除原有英文外加入德文、法文、中文、日文和韩文台词；这些内容通过 `Component.literal` 直接发送，不受客户端语言选项影响。
- 调整：按测试需求重新启用 Human 伪进阶技能的 EF/EFX 技能动画播放；触发技能时会再次优先尝试 `epicfightx:` 动画并回退 `epicfight:` 动画，同时保留服务端伤害、击退、减速与范围效果。
- 调整：临时关闭 Human 伪进阶技能的强制 EF/EFX 动画播放，仅保留服务端伤害、击退、减速与范围效果；本次日志显示正常战斗中出现大量 `GL_INVALID_OPERATION: Depth formats do not match` 渲染错误，疑似 EFX 动画同步与 Oculus/Embeddium 深度缓冲组合触发客户端崩溃。
- 修复：登录/进入存档时服务端同步 Human 追随者数据不再直接加载 `HumansServerDataClientSync` 包装类，避免部分运行环境下出现 `NoClassDefFoundError: com/craftix/hostile_humans/entity/data/HumansServerDataClientSync` 导致服务端 tick loop 崩溃。
- 新增：Human 现在可在使用 Epic Fight 近战武器时小概率触发“伪进阶技能”；命中目标后按当前武器播放对应 EFX/EF 技能动画，并附带服务端实际效果。
- 新增：长矛触发 `heartpiercer`，长剑触发 `sweeping_edge`，匕首触发 `eviscerate`，大剑触发 `the_guillotine` / `steel_whirlwind`，拳套触发 `relentless_combo`，双持剑触发 `dancing_edge`。
- 调整：进阶技能采用 12% 触发概率与 80-130 tick 冷却；效果以额外伤害、击退、短暂减速和小范围横扫为主，不依赖玩家 SkillContainer，降低与 Epic Fight 玩家技能槽系统的冲突风险。
- 调整：重新从 EpicFight:Extra 的 `AnimationsX` / `ExtraAnimations` 注册表字节码提取可用动画 ID，避免继续使用仅存在资源文件但未注册构造器的 `epicfightx:biped/combat/spear_dash` 等无效路径。
- 调整：`hostile_humans:human_tier1`、`human_tier2`、`human_roamer` 的 Epic Fight mobpatch 动画引用已切换为确认注册的 `epicfightx:` ID；长矛冲刺改用有效的 `epicfightx:biped/combat/spear_onehand_dash`。
- 调整：Human 伪格挡动画现在优先播放 EpicFight:Extra 的 `epicfightx:biped/skill/guard_*_hit`，若 EFX 未加载或对应动画不可用则回退到 Epic Fight 原版 ID。

- 新增：Human 受到正面攻击时加入 Epic Fight 风格“伪格挡”受击逻辑；当主手为剑类或 Epic Fight 近战武器、武器挥击恢复度超过 70%、且 20% 概率通过时，最终伤害降低 70%。
- 新增：伪格挡触发时播放盾牌格挡音效，并在 Epic Fight 已加载时通过软反射尝试播放对应武器的格挡受击动画，包括剑/双剑、长矛、长剑和大剑；反射失败会自动降级为仅减伤与音效。

- 新增：Human 生成装备池现在可选接入 Epic Fight 武器，支持长矛、长剑、匕首、大剑和拳套；未安装 Epic Fight 时会自动跳过对应物品并回退原版装备。
- 调整：双持剑类生成概率提升为 tier1 约 12%、roamer 约 18%、tier2 约 24%，双持时不再额外生成盾牌/图腾占用副手。
- 调整：给 Epic Fight mobpatch 中的翻滚行为追加 `random_chance` 条件，略微降低战斗中 roll_forward/roll_backward 的触发频率。
- 修复：Human 自身的近战武器判断现在会识别 Epic Fight 的 spear、longsword、dagger、greatsword 和 glove，避免距离切换逻辑把这些武器当成非近战物品。

- 修复：撤回直接引用 `epicfightx:biped` 的 Human mobpatch 配置；实例日志显示 EpicFight:Extra 的部分资源缺少 mobpatch 反序列化所需的 animation constructor，导致 `hostile_humans:human_tier1`、`hostile_humans:human_tier2`、`hostile_humans:human_roamer` 整个能力补丁加载失败。
- 调整：改用 EFMCompat 中已知可解析的 recruits 人形 NPC 行为表作为保守基底，保留 `player` patched renderer、`epicfight:entity/biped` 模型/骨架、`illager` 阵营和 Hostile Humans 的 tier 属性差异，以先确保 Epic Fight 能接管 Human。

- 兼容性：新增 Epic Fight 实验性保守联动数据，使用 `epicfight_mobpatch` 将 `hostile_humans:human_tier1`、`hostile_humans:human_tier2`、`hostile_humans:human_roamer` 暂时映射到 `minecraft:zombie` 预设，用于先验证 EF 能否接管这些人形实体。
- 兼容性：将 Epic Fight 联动从 `minecraft:zombie` 预设改为自定义 humanoid patch，使用 `epicfight:entity/biped` 模型/骨架和 `player` 渲染器，并接入 `epicfight:biped` 默认动作、受击动作与简化近战行为。
- 新增：为 Human 皮肤池加入 6 张 Laby/Mojang 皮肤纹理，新增资源 `skin38.png` 至 `skin43.png`；用户提供的重复链接已去重。
- 调整：将 Hostile Humans 的 Epic Fight 数据包动画引用从原版 `epicfight:biped` 切换到 EpicFight:Extra 的 `epicfightx:biped` 优化动作，覆盖默认生活动作、受击动作和当前简化战斗行为。
- 已知问题：该 EF 联动版本仅为低侵入测试配置，尚未针对 Hostile Humans 的弓弩、盾牌、三叉戟、背包与自定义 AI 编写专用战斗行为。

- 修复：完成 Forge 1.18.2 到 Forge 1.20.1 的编译迁移，更新实体 `level()`、`onGround()`、`Component.literal`、注册表访问、事件玩家访问、实体生成和渲染 Layer 构造器等 API。
- 修复：将旧版 `worldgen/configured_structure_feature` 结构数据迁移为 1.20.1 的 `worldgen/structure` jigsaw 数据，修复进入世界时 `hostile_humans:cottage` 等结构未绑定导致的崩溃。
- 修复：更新 `WalkNodeMix` 的 `evaluateBlockPathType` 注入签名，适配 1.20.1 `WalkNodeEvaluator`，修复自然刷怪时 Mixin 应用失败导致的服务端 tick 崩溃。
- 调整：将 `mc_version` 改为 `1.20.1`，并把 Gradle wrapper 改到 ForgeGradle 6 可用的 Gradle 8.7。
- 兼容性：适配 Forge 1.20.1-47.4.6、Java 17+ 构建环境。
- 已知问题：仍存在若干弃用警告，以及 `ReferenceMix` 的 Mixin `@Shadow` 映射警告；需要后续进游戏实测验证对应 Mixin 是否在运行期命中。
- 验证：`compileJava` 与 `build` 均已通过；已用最新 jar 覆盖 `D:\curseforge\minecraft\Instances\FabricTest\mods` 中的测试实例文件。
