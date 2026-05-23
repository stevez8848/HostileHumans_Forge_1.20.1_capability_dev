package com.craftix.hostile_humans;

import com.craftix.hostile_humans.entity.entities.Human;
import com.craftix.hostile_humans.entity.entities.HumanTier;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Function;

public class HumanUtil {
    public static ItemStack[] EDIBLE_ITEMS = new ItemStack[]{Items.APPLE.getDefaultInstance(), Items.BREAD.getDefaultInstance(), Items.COOKED_PORKCHOP.getDefaultInstance(), Items.COOKED_COD.getDefaultInstance(), Items.COOKED_SALMON.getDefaultInstance(), Items.COOKIE.getDefaultInstance(), Items.MELON_SLICE.getDefaultInstance(), Items.COOKED_BEEF.getDefaultInstance(), Items.COOKED_CHICKEN.getDefaultInstance(), Items.CARROT.getDefaultInstance(), Items.POTATO.getDefaultInstance(), Items.BAKED_POTATO.getDefaultInstance(), Items.GOLDEN_CARROT.getDefaultInstance(), Items.PUMPKIN_PIE.getDefaultInstance(), Items.RABBIT.getDefaultInstance(), Items.COOKED_RABBIT.getDefaultInstance(), Items.RABBIT_STEW.getDefaultInstance(), Items.MUTTON.getDefaultInstance(), Items.COOKED_MUTTON.getDefaultInstance(), Items.CHORUS_FRUIT.getDefaultInstance(), Items.BEETROOT.getDefaultInstance(), Items.DRIED_KELP.getDefaultInstance(), Items.SWEET_BERRIES.getDefaultInstance(), Items.GLOW_BERRIES.getDefaultInstance()};
    public static ItemStack[] EDIBLE_ITEMS_2 = new ItemStack[]{Items.APPLE.getDefaultInstance(), Items.BREAD.getDefaultInstance(), Items.COOKED_PORKCHOP.getDefaultInstance(), Items.COOKED_COD.getDefaultInstance(), Items.COOKED_SALMON.getDefaultInstance(), Items.COOKIE.getDefaultInstance(), Items.MELON_SLICE.getDefaultInstance(), Items.COOKED_BEEF.getDefaultInstance(), Items.COOKED_CHICKEN.getDefaultInstance(), Items.CARROT.getDefaultInstance(), Items.POTATO.getDefaultInstance(), Items.BAKED_POTATO.getDefaultInstance(), Items.GOLDEN_CARROT.getDefaultInstance(), Items.PUMPKIN_PIE.getDefaultInstance(), Items.COOKED_RABBIT.getDefaultInstance(), Items.RABBIT_STEW.getDefaultInstance(), Items.COOKED_MUTTON.getDefaultInstance(), Items.CHORUS_FRUIT.getDefaultInstance(), Items.BEETROOT.getDefaultInstance(), Items.DRIED_KELP.getDefaultInstance(), Items.SWEET_BERRIES.getDefaultInstance(), Items.GLOW_BERRIES.getDefaultInstance()};
    public static String[] greetings = {
            "Huh? INTRUDER!",
            "Who are you? GET OUT OF HERE.",
            "You will die intruder!",
            "You stepped into the wrong place.",
            "Leave this area!",
            "Leave this place!",
            "I would give up.",
            "You made a mistake coming here.",
            "Run or die!",
            "What the hell, who are you?",
            "Give up and I'll let you run.",
            "You in the wrong place mate.",
            "You think you can just barge into this place and live?",
            "Your life has come to a end stranger.",
            "Why are you here?",
            "Run or i'll slit your throat.",
            "GET OUT OF HERE!",
            "Your in the wrong place and I needed to relieve some stress, i'm going to cut you up.",
            "I have pent up anger and you're trespassing, staand stillll!",
            "LEAVE",
            "You think your slick?",
            "HEY YOU, STOP!",
            "Whoever you are, LEAVE OR DIE.",
            "I don't know who you are but you just made a mistake.",
            "Please leave this area or else.",
            "I'm going to enjoy slicing you up.",
            "I don't know who you are but I'm going to cut you up AND FEED YOUR PARTS TO WOLVES!",
            "Wrong place mate, run now while you can.",
            "Why are you doing this?",
            "You just made the biggest mistake of your life mate.",
            "I hope your ready to lose your life stranger.",
            "Thou Shan't pass, heh.",
            "How did- ? Your not welcomed here.",
            "Go back to where you came from.",
            "You're a fool to be here. You won't survive.",
            "You don't belong here.",
            "There's no escape for you.",
            "I'm only warning you once, get out of here.",
            "You're playing with fire, intruder. You'll get burned.",
            "You're in over your head, you don't stand a chance against me!",
            "You're wasting your time here.",
            "You're a brave one, I'll give you that. But bravery won't save you.",
            "Your in for a world of pain mate. I'll make you suffer!",
            "You're out of my league, you can't handle me!",
            "Mate if your asking for trouble, trouble is what you'll get.",
            "You shouldn't be here.",
            "I'll squash you like a bug, DIE!",
            "I'll make you beg for mercy.",
            "There's nothing but death for you here.",
            "Please I don't want to fight, run away.",
            "I hope you're ready.",
            "Please just run.",
            "GET OUT OF HERE.",
            "You picked a bad day to be brave.",
            "I can hear your boots from here.",
            "Turn around before this gets ugly.",
            "This place remembers every intruder.",
            "You're not leaving with what you came for.",
            "Keep staring. It won't save you.",
            "I was hoping for a quiet day.",
            "You've got nerve. Not enough, though.",
            "Back away, and maybe you keep breathing.",
            "The warning ends now.",
            "Du solltest besser sofort umkehren.",
            "Ich sehe jede Bewegung von dir.",
            "Noch kannst du lebend verschwinden.",
            "Das hier wird kein fairer Kampf.",
            "Du störst unsere Ruhe.",
            "Tu ferais mieux de faire demi-tour.",
            "Je vois chacun de tes gestes.",
            "Tu peux encore repartir vivant.",
            "Ce combat ne sera pas équitable.",
            "Tu troubles notre paix.",
            "现在回头还来得及。",
            "你的脚步声太明显了。",
            "别再靠近了。",
            "这里不欢迎陌生人。",
            "我不想废话第二遍。",
            "今ならまだ引き返せる。",
            "足音が丸聞こえだ。",
            "それ以上近づくな。",
            "見知らぬ者を歓迎する場所じゃない。",
            "二度は言わない。",
            "지금 돌아가면 늦지 않는다.",
            "네 발소리는 다 들린다.",
            "더 가까이 오지 마라.",
            "낯선 자를 환영하는 곳이 아니다.",
            "두 번 말하지 않겠다.",
            "Du bist hier nicht willkommen.",
            "Noch ein Schritt, und du bereust es.",
            "Verschwinde, solange du noch kannst.",
            "Das ist unser Gebiet.",
            "Ich habe dich gewarnt.",
            "Leg deine Waffe weg und lauf.",
            "Du hast dir den falschen Ort ausgesucht.",
            "Kein Fremder kommt hier ungestraft hinein.",
            "Raus hier, sofort!",
            "Heute endet dein Weg hier.",
            "Tu n'es pas le bienvenu ici.",
            "Encore un pas et tu le regretteras.",
            "Pars tant que tu peux encore.",
            "C'est notre territoire.",
            "Je t'avais prévenu.",
            "Baisse ton arme et fuis.",
            "Tu as choisi le mauvais endroit.",
            "Aucun intrus ne ressort d'ici indemne.",
            "Dehors, maintenant !",
            "Ton chemin s'arrête ici.",
            "你不该来这里。",
            "再往前一步，你会后悔的。",
            "趁现在还能跑，快走。",
            "这里是我们的地盘。",
            "我已经警告过你了。",
            "放下武器，然后离开。",
            "你选错地方了。",
            "闯进这里的人不会全身而退。",
            "马上离开这里！",
            "你的路到此为止。",
            "ここに来るべきじゃなかったな。",
            "もう一歩進めば後悔するぞ。",
            "逃げるなら今のうちだ。",
            "ここは俺たちの縄張りだ。",
            "警告はしたぞ。",
            "武器を下ろして立ち去れ。",
            "場所を間違えたな。",
            "侵入者を無事に帰すつもりはない。",
            "今すぐ出ていけ！",
            "お前の道はここで終わりだ。",
            "여긴 네가 올 곳이 아니다.",
            "한 걸음만 더 오면 후회하게 될 거다.",
            "도망칠 수 있을 때 도망쳐.",
            "여기는 우리 구역이다.",
            "분명히 경고했다.",
            "무기를 내려놓고 떠나라.",
            "장소를 잘못 골랐군.",
            "침입자를 그냥 돌려보내진 않는다.",
            "당장 여기서 나가!",
            "네 길은 여기서 끝이다."
    };

    public static boolean isStructureDisabled(String value) {
        return Arrays.asList(Config.disabledStructures.get().replace(" ", "").split(",")).contains(value);
    }

    public static boolean isRangedWeapon(ItemStack value) {
        return value.is(Items.CROSSBOW) || value.is(Items.BOW);
    }

    public static boolean isMeleeWeapon(ItemStack value) {
        return !value.isEmpty() && (value.getItem() instanceof SwordItem || value.getItem() instanceof AxeItem || isEpicFightMeleeWeapon(value));
    }

    public static boolean isEpicFightMeleeWeapon(ItemStack value) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(value.getItem());
        if (itemId == null || !"epicfight".equals(itemId.getNamespace())) {
            return false;
        }

        String path = itemId.getPath();
        return path.contains("spear")
                || path.contains("longsword")
                || path.contains("dagger")
                || path.contains("greatsword")
                || path.contains("tachi")
                || path.contains("uchigatana")
                || path.contains("katana")
                || path.equals("glove");
    }

    public static boolean isTrident(ItemStack value) {
        return !value.isEmpty() && (value.getItem() instanceof TridentItem);
    }

    public static boolean isShield(ItemStack value) {
        return !value.isEmpty() && (value.getItem() instanceof ShieldItem);
    }

    public static boolean isConsumable(ItemStack stack) {
        return stack.getCount() > 0 && (stack.getUseAnimation() == UseAnim.EAT || stack.getUseAnimation() == UseAnim.DRINK);
    }

    public static boolean canStartEating(Human human) {
    	if (!human.isAlive()) return false;
    	if (human.isUsingItem()) return false;
    	if (human.eatingColldown != 0) return false;
    	if (human.timesHealedInCombat >= 4) return false;
    	if (human.wantsToSwim() && human.getTier() == HumanTier.LEVEL2 && !human.hasEffect(MobEffects.WATER_BREATHING)) return true;
        if (isLowHpInCombat(human)) return true;
        if (human.toAvoid != null) return false;
        if (!isLowHp(human)) return false;
        if (human.isUsingItem()) return false;
        if (human.getTarget() != null) return false;
        if (human.tickCount < 20 * 6 + human.lastCombatTime) return false;

        return true;
    }

    public static boolean isEmptyFood(ItemStack stack) {
        return stack.isEmpty() || stack.is(Items.GLASS_BOTTLE) || stack.is(Items.BOWL);
    }

    public static boolean isLookingAtTarget(LivingEntity mob, Entity target) {
        Vec3 vec3 = mob.getViewVector(1.0F).normalize();
        Vec3 vec31 = new Vec3(target.getX() - mob.getX(), target.getEyeY() - mob.getEyeY(), target.getZ() - mob.getZ());
        double d0 = vec31.length();
        vec31 = vec31.normalize();
        double d1 = vec3.dot(vec31);
        return d1 > 1.0D - /*0.025D*/ 0.4 / d0 && mob.hasLineOfSight(target);
    }

    @NotNull
    public static ItemStack createSwordBanner() {
        var banner = Items.WHITE_BANNER.getDefaultInstance();
        // minecraft:white_banner{BlockEntityTag: {Patterns: [ {Pattern: "flo", Color:4}, {Pattern: "hh", Color:7}, {Pattern: "cs", Color:0}, {Pattern: "br", Color:7}, {Pattern: "bl", Color:7}, {Pattern: "cbo", Color:7} ]}}

        CompoundTag blockData = banner.getOrCreateTagElement("BlockEntityTag");
        ListTag patterns = new ListTag();

        CompoundTag c1;
        c1 = new CompoundTag();
        c1.putInt("Color", 4);
        c1.putString("Pattern", "flo");
        patterns.add(c1);

        c1 = new CompoundTag();
        c1.putInt("Color", 7);
        c1.putString("Pattern", "hh");
        patterns.add(c1);

        c1 = new CompoundTag();
        c1.putInt("Color", 0);
        c1.putString("Pattern", "cs");
        patterns.add(c1);

        c1 = new CompoundTag();
        c1.putInt("Color", 7);
        c1.putString("Pattern", "br");
        patterns.add(c1);

        c1 = new CompoundTag();
        c1.putInt("Color", 7);
        c1.putString("Pattern", "bl");
        patterns.add(c1);

        c1 = new CompoundTag();
        c1.putInt("Color", 7);
        c1.putString("Pattern", "cbo");
        patterns.add(c1);

        blockData.put("Patterns", patterns);
        return banner;
    }

    public static boolean isLadder(BlockState state, LivingEntity entity, BlockPos pos) {
        return state.isLadder(entity.level(), pos, entity);
    }

    public static int createLadderNodeFor(int nodeID, Node[] nodes, Node origin, Function<BlockPos, Node> nodeGetter, BlockGetter getter, Mob mob) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(origin.x, origin.y + 1, origin.z);
        if (isLadder(getter.getBlockState(pos), mob, pos)) {
            Node node = nodeGetter.apply(pos);
            if (node != null && !node.closed) {
                node.costMalus = 0;
                node.type = BlockPathTypes.WALKABLE;
                if (nodeID + 1 < nodes.length)
                    nodes[nodeID++] = node;
            }
        }
        pos.set(pos.getX(), pos.getY() - 2, pos.getZ());
        if (isLadder(getter.getBlockState(pos), mob, pos)) {
            Node node = nodeGetter.apply(pos);
            if (node != null && !node.closed) {
                node.costMalus = 0;
                node.type = BlockPathTypes.WALKABLE;
                if (nodeID + 1 < nodes.length)
                    nodes[nodeID++] = node;
            }
        }
        return nodeID;
    }

    public static boolean isLowHpInCombat(LivingEntity human) {
        if (human.getOffhandItem().getItem() == Items.TOTEM_OF_UNDYING)
            return false;
        
        if (human instanceof Human huma && !huma.needsFood() && huma.getTarget() == null)
        	return false;

        return human.getHealth() < human.getMaxHealth() * (Config.healCombatPercent.get());
    }

    public static boolean isLowHp(LivingEntity human) {
        if (human.getOffhandItem().getItem() == Items.TOTEM_OF_UNDYING)
            return false;
        
        if (human instanceof Human huma && !huma.needsFood())
        	return false;

        return human.getHealth() < human.getMaxHealth() * Config.fleeHpPercent.get();
    }

    public static boolean shouldFightCreeper(LivingEntity human) {

        return String.valueOf(human.getId()).hashCode() % 100 < 20;
    }
}
