## 更新日志

- 调整：雇佣兵现在不再注册 `LookForChestGoal`，不会主动寻找、打开或占用箱子；普通 Human 与 Bandit 的开箱行为保持不变。
- 修复：移除不存在的内置数据包 `improved_humans` 与 `hardcore_humans` 注册，避免进世界时出现 `Missing metadata in pack builtin/hostile_humans/*` 干扰数据包仓库加载排查。
- 修复：为 Ronin、Samurai、Bandit 和 Mercenary 刷怪蛋补齐原版 `item/template_spawn_egg` 模型文件，保留原版刷怪蛋渲染并消除缺失模型警告。
- 新增：加入第一阶段 `hostile_humans:human_mercenary`（雇佣兵）实体；雇佣兵对玩家中立，会主动攻击浪人、Roamer 和盗贼，并通过村庄 POI 补生成逻辑在村庄附近生成 1-6 个巡逻个体。
- 调整：雇佣兵装备池优先使用 Epic Fight 长剑、长矛和弩，缺少 EF 武器时保守回退到铁剑，并有一定概率携带盾牌和轻量护甲。
- 新增：重做低血量撤退逻辑；当 Human 生命值低于 30% 且正在战斗时，有 30% 概率进入临时撤退，拉开到约 20-30 格距离后回血并重新加入战斗。
- 调整：提高普通 Human / Roamer 持有 Epic Fight 长剑的生成概率；武士现在会主动攻击浪人。

- 调整：Human 的长剑伪进阶技能不再沿用普通剑 `sweeping_edge`，改为播放 `longsword_airslash` 长剑专属动画，并继续保留范围打击、击退和减速效果。

- 修复：移除 `worldgen/structure_set/all_houses.json` 中的 `//` 注释，避免 Minecraft 数据包严格 JSON 解析失败导致 `hostile_humans:*` 结构集合无法注册，从而影响 `/place structure` 指令生成建筑。
- 验证：已检查 `src/main/resources/data/hostile_humans/worldgen` 下全部 JSON，当前均可被严格解析。

- 紧急调整：Human 目标优先级改为先处理当前攻击者，其次仍将玩家目标保持在高优先级，再处理 Bandit、其他 Human 和普通怪物/动物目标。
- 调整：Ronin 现在按 Roamer-like 机制处理随机游荡与临时冲突逻辑；多个 Roamer/Ronin 同场且附近存在普通 Human 时，会暂时停止彼此冲突。
- 新增：加入 `hostile_humans:human_samurai1` 与 `hostile_humans:human_samurai2` 两级武士。武士沿用浪人的打刀/太刀战斗机制，第一级不穿盔甲，第二级会生成盔甲；武士不会主动攻击其他武士，也不会主动攻击非 Bandit 的 Human。
- 新增：加入 `hostile_humans:human_bandit` 盗贼。盗贼默认使用铁、钻石或下界合金级 Epic Fight 匕首候选，若 EF 匕首不存在则回退铁剑/钻石剑；匕首 80% 概率带随机等级抢夺与锋利。
- 新增：盗贼使用 Human Tier I 风格护甲池，但只有 30% 概率生成护甲；盗贼的最高行为优先级是寻找并打开箱子，被玩家发现或被攻击后会进入战斗。
- 新增：除 Bandit 自身外，所有 Human 系实体都会主动攻击 Bandit；铁傀儡也会主动攻击 Bandit。
- 新增：Bandit 使用自定义高级战利品池，掉落绿宝石、金锭、铜锭、钻石、金苹果、附魔金苹果和下界合金锭等随机战利品；加载 Epic Fight 时额外固定掉落一本带随机 `skill` NBT 的 `epicfight:skillbook`。

- 调整：略微提高浪人生成为打刀/uchigatana 类武器的概率；浪人武器池现在按打刀、太刀、katana 三类做权重选择，打刀类权重从均分提高到约 40%。
- 调整：将所有 Human 的玩家目标选择提升为最高 `targetSelector` 优先级，高于受击反击、Human 内斗和普通怪物/动物目标。
- 调整：Roamer 在附近存在非 Roamer Human 时会临时停止把其他 Roamer 视为敌人，并优先把冲突集中到普通 Human / Ronin 等非 Roamer Human 上；附近没有非 Roamer Human 时仍保留 Roamer 内斗。

- 修复：继续收紧 Human 皮肤渲染稳定性；HumanRenderer 现在使用独立 `hostile_humans:human` 模型层，不再复用 vanilla player baked layer，并移除强制 `entityCutoutNoCull` 的 `getRenderType` 覆盖，交回 vanilla `LivingEntityRenderer` 处理可见、半透明、发光和 outline 分支。
- 修复：每次渲染 Human 前显式重置 `PlayerModel` 的可见性、骑乘、蹲伏、幼体、攻击进度和双手姿态状态，降低 EF/EFX、Oculus/Embeddium 或实体层渲染后模型状态串扰导致皮肤显示异常的概率。

- 新增：加入新实体 `hostile_humans:human_ronin`（Ronin / 浪人），沿用 Human 的基础 AI、渲染和属性体系，注册独立实体类型、客户端渲染、刷怪蛋、语言条目、自然生成入口和空战利品表。
- 新增：浪人生成为无护甲状态，主手固定使用 Epic Fight 的 `uchigatana` / `tachi` / `katana` 系列武器候选；若实例未安装 Epic Fight 或候选物品不存在，则保守回退为铁剑/钻石剑，避免 EF 缺失时崩溃。
- 新增：浪人的佩刀有 30% 概率同时附带随机等级的锋利与横扫之刃；死亡时固定额外掉落当前佩刀、24 个绿宝石和 12 条熟鳕鱼，普通随机实体战利品表已置空。
- 兼容性：为浪人新增 `human_ronin` 的 Epic Fight mobpatch，并在 EFX 内置数据包中加入对应覆盖文件；打刀、太刀和类 katana 武器会沿用现有 Human 人形 EF/EFX 战斗动画配置。
- 调整：扩展 Human 的伪进阶技能识别范围，新增太刀 `rushing_tempo` 与打刀/katana `battojutsu` / `battojutsu_dash` 触发逻辑；浪人额外模拟 5 秒空闲收刀后的拔刀强化攻击。
- 修复：修正自然生成 biome modifier 中旧的 `hostile_humans:human_spawner` ID 为实际注册的 `hostile_humans:human_group`，避免该生成配置解析失败。

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