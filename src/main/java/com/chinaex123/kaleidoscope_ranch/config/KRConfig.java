package com.chinaex123.kaleidoscope_ranch.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class KRConfig {
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.DoubleValue MOOD_INITIAL;
    public static final ModConfigSpec.DoubleValue MOOD_MIN;
    public static final ModConfigSpec.DoubleValue MOOD_MAX;
    public static final ModConfigSpec.DoubleValue MOOD_ABSOLUTE_MAX;
    public static final ModConfigSpec.DoubleValue HUNGER_INITIAL;
    public static final ModConfigSpec.DoubleValue HUNGER_MIN;
    public static final ModConfigSpec.DoubleValue HUNGER_MAX;
    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> FAVOR_EXP_TABLE;
    public static final ModConfigSpec.IntValue FAVOR_RISE_PET_EXP_MIN;
    public static final ModConfigSpec.IntValue FAVOR_RISE_PET_EXP_MAX;
    public static final ModConfigSpec.IntValue FAVOR_RISE_MOOD_EXP_MIN;
    public static final ModConfigSpec.IntValue FAVOR_RISE_MOOD_EXP_MAX;
    public static final ModConfigSpec.IntValue FAVOR_RISE_HUNGER_EXP_MIN;
    public static final ModConfigSpec.IntValue FAVOR_RISE_HUNGER_EXP_MAX;
    public static final ModConfigSpec.IntValue FAVOR_DECLINE_HURT_EXP_MIN;
    public static final ModConfigSpec.IntValue FAVOR_DECLINE_HURT_EXP_MAX;
    public static final ModConfigSpec.IntValue FAVOR_DECLINE_RAIN_EXP;
    public static final ModConfigSpec.DoubleValue MOOD_RISE_PET_BONUS;
    public static final ModConfigSpec.DoubleValue MOOD_RISE_ROSE_SALT_BLOCK_BONUS;
    public static final ModConfigSpec.IntValue MOOD_RISE_ROSE_SALT_BLOCK_RADIUS;
    public static final ModConfigSpec.IntValue MOOD_RISE_ROSE_SALT_BLOCK_TICK_INTERVAL;
    public static final ModConfigSpec.DoubleValue MOOD_RISE_MUD_BONUS;
    public static final ModConfigSpec.IntValue MOOD_RISE_MUD_TICK_INTERVAL;
    public static final ModConfigSpec.IntValue MOOD_DECLINE_DIVERSE_SPECIES_RADIUS;
    public static final ModConfigSpec.IntValue MOOD_DECLINE_DIVERSE_SPECIES_REQUIRED;
    public static final ModConfigSpec.IntValue MOOD_DECLINE_DIVERSE_SPECIES_TICK_INTERVAL;
    public static final ModConfigSpec.DoubleValue MOOD_DECLINE_DIVERSE_SPECIES_AMOUNT;
    public static final ModConfigSpec.IntValue MOOD_DECLINE_FECES_RADIUS;
    public static final ModConfigSpec.IntValue MOOD_DECLINE_FECES_TICK_INTERVAL;
    public static final ModConfigSpec.DoubleValue MOOD_DECLINE_FECES_AMOUNT;
    public static final ModConfigSpec.IntValue MOOD_DECLINE_RAIN_TICK_INTERVAL;
    public static final ModConfigSpec.DoubleValue MOOD_DECLINE_RAIN_AMOUNT;
    public static final ModConfigSpec.DoubleValue MOOD_DECLINE_HURT_AMOUNT;
    public static final ModConfigSpec.IntValue MOOD_DECLINE_RECOVERY_TICK_INTERVAL;
    public static final ModConfigSpec.DoubleValue MOOD_DECLINE_RECOVERY_AMOUNT;
    public static final ModConfigSpec.DoubleValue HUNGER_RISE_BASE_BONUS;
    public static final ModConfigSpec.DoubleValue HUNGER_RISE_BONUS_DECAY;
    public static final ModConfigSpec.IntValue HUNGER_RISE_FEED_COOLDOWN_TICKS;
    public static final ModConfigSpec.DoubleValue HUNGER_DECLINE_DAILY_AMOUNT;
    public static final ModConfigSpec.DoubleValue HUNGER_DECLINE_FECES_AMOUNT;
    public static final ModConfigSpec.IntValue HUNGER_DECLINE_FECES_TICK_INTERVAL;
    public static final ModConfigSpec.IntValue HUNGER_DECLINE_FECES_RADIUS;
    public static final ModConfigSpec.DoubleValue HUNGER_NATURAL_DECLINE_PER_MINUTE;

    public static final ModConfigSpec.DoubleValue WILD_INITIAL_MOOD_MIN;
    public static final ModConfigSpec.DoubleValue WILD_INITIAL_MOOD_MAX;
    public static final ModConfigSpec.DoubleValue WILD_INITIAL_HUNGER_MIN;
    public static final ModConfigSpec.DoubleValue WILD_INITIAL_HUNGER_MAX;
    public static final ModConfigSpec.IntValue WILD_TAME_FAVOR_THRESHOLD;
    public static final ModConfigSpec.DoubleValue FLEE_DETECT_RADIUS;
    public static final ModConfigSpec.DoubleValue FLEE_SPEED;
    public static final ModConfigSpec.DoubleValue FLEE_DISTANCE;
    public static final ModConfigSpec.DoubleValue FLEE_DISTANCE_VARIANCE;
    public static final ModConfigSpec.IntValue HAYSTACK_DETECT_RADIUS;
    public static final ModConfigSpec.IntValue HAYSTACK_COLLECT_RADIUS;
    public static final ModConfigSpec.IntValue HAYSTACK_COLLECT_INTERVAL;
    public static final ModConfigSpec.DoubleValue TROUGH_DETECT_RADIUS;
    public static final ModConfigSpec.IntValue TROUGH_FEED_COOLDOWN_TICKS;
    public static final ModConfigSpec.DoubleValue TROUGH_FEED_GAIN;
    public static final ModConfigSpec.IntValue PIG_TREASURE_INTERVAL;
    public static final ModConfigSpec.DoubleValue PIG_TREASURE_CHANCE;
    public static final ModConfigSpec.IntValue PIG_TREASURE_FAVOR_THRESHOLD;
    public static final ModConfigSpec.DoubleValue LOOT_TIER1_MOOD_THRESHOLD;
    public static final ModConfigSpec.IntValue LOOT_TIER1_FAVOR_THRESHOLD;
    public static final ModConfigSpec.DoubleValue LOOT_TIER1_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue LOOT_TIER2_MOOD_THRESHOLD;
    public static final ModConfigSpec.IntValue LOOT_TIER2_FAVOR_THRESHOLD;
    public static final ModConfigSpec.DoubleValue LOOT_TIER2_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue LOOT_TIER3_MOOD_THRESHOLD;
    public static final ModConfigSpec.IntValue LOOT_TIER3_FAVOR_THRESHOLD;
    public static final ModConfigSpec.DoubleValue LOOT_TIER3_MULTIPLIER;
    public static final ModConfigSpec.IntValue SHEEP_SHEAR_COOLDOWN_TICKS;
    public static final ModConfigSpec.IntValue MOOSHROOM_STEW_EFFECT_DURATION_TICKS;
    public static final ModConfigSpec.DoubleValue BREED_EXTRA_OFFSPRING_CHANCE;
    public static final ModConfigSpec.IntValue BREED_EXTRA_OFFSPRING_COUNT;
    public static final ModConfigSpec.DoubleValue BREED_SHEEP_EXTRA_2_CHANCE;
    public static final ModConfigSpec.DoubleValue BREED_SHEEP_EXTRA_3_CHANCE;
    public static final ModConfigSpec.DoubleValue BREED_SHEEP_EXTRA_4_CHANCE;
    public static final ModConfigSpec.DoubleValue BREED_SHEEP_EXTRA_5_CHANCE;
    public static final ModConfigSpec.DoubleValue BREED_GOAT_EXTRA_2_CHANCE;
    public static final ModConfigSpec.DoubleValue BREED_GOAT_EXTRA_3_CHANCE;
    public static final ModConfigSpec.DoubleValue BREED_GOAT_EXTRA_4_CHANCE;
    public static final ModConfigSpec.DoubleValue BREED_GOAT_EXTRA_5_CHANCE;
    public static final ModConfigSpec.DoubleValue BREED_GOAT_EXTRA_6_CHANCE;
    public static final ModConfigSpec.IntValue BREED_PIG_BASE_COUNT;
    public static final ModConfigSpec.DoubleValue BREED_PIG_TIER1_CHANCE;
    public static final ModConfigSpec.IntValue BREED_PIG_TIER1_MIN;
    public static final ModConfigSpec.IntValue BREED_PIG_TIER1_MAX;
    public static final ModConfigSpec.DoubleValue BREED_PIG_TIER2_CHANCE;
    public static final ModConfigSpec.IntValue BREED_PIG_TIER2_MIN;
    public static final ModConfigSpec.IntValue BREED_PIG_TIER2_MAX;
    public static final ModConfigSpec.DoubleValue BREED_PIG_TIER3_CHANCE;
    public static final ModConfigSpec.IntValue BREED_PIG_TIER3_MIN;
    public static final ModConfigSpec.IntValue BREED_PIG_TIER3_MAX;
    public static final ModConfigSpec.DoubleValue BREED_PIG_TIER4_CHANCE;
    public static final ModConfigSpec.IntValue BREED_PIG_TIER4_MIN;
    public static final ModConfigSpec.IntValue BREED_PIG_TIER4_MAX;
    public static final ModConfigSpec.DoubleValue BREED_PIG_TIER5_CHANCE;
    public static final ModConfigSpec.IntValue BREED_PIG_TIER5_MIN;
    public static final ModConfigSpec.IntValue BREED_PIG_TIER5_MAX;
    public static final ModConfigSpec.DoubleValue BREED_RABBIT_TIER1_CHANCE;
    public static final ModConfigSpec.IntValue BREED_RABBIT_TIER1_COUNT;
    public static final ModConfigSpec.DoubleValue BREED_RABBIT_TIER2_CHANCE;
    public static final ModConfigSpec.IntValue BREED_RABBIT_TIER2_MIN;
    public static final ModConfigSpec.IntValue BREED_RABBIT_TIER2_MAX;
    public static final ModConfigSpec.DoubleValue BREED_RABBIT_TIER3_CHANCE;
    public static final ModConfigSpec.IntValue BREED_RABBIT_TIER3_MIN;
    public static final ModConfigSpec.IntValue BREED_RABBIT_TIER3_MAX;
    public static final ModConfigSpec.DoubleValue BREED_RABBIT_TIER4_CHANCE;
    public static final ModConfigSpec.IntValue BREED_RABBIT_TIER4_COUNT;
    public static final ModConfigSpec.DoubleValue BREED_RABBIT_TIER5_CHANCE;
    public static final ModConfigSpec.IntValue BREED_RABBIT_TIER5_COUNT;
    public static final ModConfigSpec.DoubleValue BREED_RABBIT_TIER6_CHANCE;
    public static final ModConfigSpec.IntValue BREED_RABBIT_TIER6_MIN;
    public static final ModConfigSpec.IntValue BREED_RABBIT_TIER6_MAX;
    public static final ModConfigSpec.IntValue DONKEY_MAX_BREED_COUNT;
    public static final ModConfigSpec.DoubleValue BREED_LLAMA_EXTRA_CHANCE;
    public static final ModConfigSpec.IntValue BREED_LLAMA_EXTRA_COUNT;
    public static final ModConfigSpec.DoubleValue BREED_OFFSPRING_MOOD_MIN;
    public static final ModConfigSpec.DoubleValue BREED_OFFSPRING_MOOD_MAX;
    public static final ModConfigSpec.DoubleValue BREED_OFFSPRING_HUNGER_MIN;
    public static final ModConfigSpec.DoubleValue BREED_OFFSPRING_HUNGER_MAX;
    public static final ModConfigSpec.IntValue BREED_OFFSPRING_FAVOR_LEVEL;
    public static final ModConfigSpec.IntValue BREED_COOLDOWN_TICKS;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("心情值").push("Mood");
        MOOD_INITIAL = builder
                .comment("初始心情值")
                .comment("Initial mood value")
                .defineInRange("initial", 100.0, 0.0, Integer.MAX_VALUE);
        MOOD_MIN = builder
                .comment("最低心情值")
                .comment("Minimum mood value")
                .defineInRange("min", 0.0, 0.0, Integer.MAX_VALUE);
        MOOD_MAX = builder
                .comment("最高心情值")
                .comment("Maximum mood value")
                .defineInRange("max", 100.0, 0.0, Integer.MAX_VALUE);
        MOOD_ABSOLUTE_MAX = builder
                .comment("极限最高心情值")
                .comment("Absolute maximum mood value")
                .defineInRange("absoluteMax", 120.0, 0.0, Integer.MAX_VALUE);

        builder.comment("心情值提升").push("Mood Rise");
        MOOD_RISE_PET_BONUS = builder
                .comment("抚摸心情加成")
                .comment("Petting mood bonus")
                .defineInRange("petBonus", 1.0, 0.0, Integer.MAX_VALUE);
        MOOD_RISE_ROSE_SALT_BLOCK_BONUS = builder
                .comment("玫瑰盐块心情加成")
                .comment("Rose salt block mood bonus")
                .defineInRange("roseSaltBlockBonus", 0.5, 0.0, Integer.MAX_VALUE);
        MOOD_RISE_ROSE_SALT_BLOCK_RADIUS = builder
                .comment("玫瑰盐块检测半径")
                .comment("Rose salt block detection radius")
                .defineInRange("roseSaltBlockRadius", 2, 1, 32);
        MOOD_RISE_ROSE_SALT_BLOCK_TICK_INTERVAL = builder
                .comment("玫瑰盐块加成tick间隔")
                .comment("Rose salt block bonus tick interval")
                .defineInRange("roseSaltBlockTickInterval", 3000, 100, Integer.MAX_VALUE);
        MOOD_RISE_MUD_BONUS = builder
                .comment("泥巴心情恢复量(仅favor_mud标签生物)")
                .comment("Mud mood recovery amount (only for favor_mud tagged entities)")
                .defineInRange("mudBonus", 1.0, 0.0, Integer.MAX_VALUE);
        MOOD_RISE_MUD_TICK_INTERVAL = builder
                .comment("泥巴心情恢复tick间隔(仅favor_mud标签生物)")
                .comment("Mud mood recovery tick interval (only for favor_mud tagged entities)")
                .defineInRange("mudTickInterval", 3600, 100, Integer.MAX_VALUE);
        MOOD_DECLINE_RECOVERY_TICK_INTERVAL = builder
                .comment("自然心情恢复tick间隔")
                .comment("Mood recovery tick interval")
                .defineInRange("recoveryTickInterval", 3000, 100, Integer.MAX_VALUE);
        MOOD_DECLINE_RECOVERY_AMOUNT = builder
                .comment("自然心情恢复量")
                .comment("Mood recovery amount")
                .defineInRange("recoveryAmount", 1.0, 0.0, Integer.MAX_VALUE);
        builder.pop();

        builder.comment("心情下降").push("Mood Decline");
        MOOD_DECLINE_DIVERSE_SPECIES_RADIUS = builder
                .comment("物种多样性检测半径")
                .comment("Diverse species detection radius")
                .defineInRange("diverseSpeciesRadius", 4, 1, 32);
        MOOD_DECLINE_DIVERSE_SPECIES_REQUIRED = builder
                .comment("触发物种多样性惩罚所需物种数")
                .comment("Required species count to trigger diverse species penalty")
                .defineInRange("diverseSpeciesRequired", 3, 1, Integer.MAX_VALUE);
        MOOD_DECLINE_DIVERSE_SPECIES_TICK_INTERVAL = builder
                .comment("物种多样性惩罚tick间隔")
                .comment("Diverse species penalty tick interval")
                .defineInRange("diverseSpeciesTickInterval", 6000, 100, Integer.MAX_VALUE);
        MOOD_DECLINE_DIVERSE_SPECIES_AMOUNT = builder
                .comment("物种多样性惩罚量")
                .comment("Diverse species penalty amount")
                .defineInRange("diverseSpeciesAmount", 1.0, 0.0, Integer.MAX_VALUE);
        MOOD_DECLINE_FECES_RADIUS = builder
                .comment("粪便检测半径")
                .comment("Feces detection radius")
                .defineInRange("fecesRadius", 6, 1, 32);
        MOOD_DECLINE_FECES_TICK_INTERVAL = builder
                .comment("粪便惩罚tick间隔")
                .comment("Feces penalty tick interval")
                .defineInRange("fecesTickInterval", 4800, 100, Integer.MAX_VALUE);
        MOOD_DECLINE_FECES_AMOUNT = builder
                .comment("每份粪便惩罚量")
                .comment("Penalty amount per feces")
                .defineInRange("fecesAmount", 2.0, 0.0, Integer.MAX_VALUE);
        MOOD_DECLINE_RAIN_TICK_INTERVAL = builder
                .comment("雨天惩罚tick间隔")
                .comment("Rain penalty tick interval")
                .defineInRange("rainTickInterval", 600, 100, Integer.MAX_VALUE);
        MOOD_DECLINE_RAIN_AMOUNT = builder
                .comment("雨天惩罚量")
                .comment("Rain penalty amount")
                .defineInRange("rainAmount", 1.0, 0.0, Integer.MAX_VALUE);
        MOOD_DECLINE_HURT_AMOUNT = builder
                .comment("受伤惩罚量")
                .comment("Hurt penalty amount")
                .defineInRange("hurtAmount", 10.0, 0.0, Integer.MAX_VALUE);
        builder.pop();

        builder.pop();


        builder.comment("饥饿值配置").push("Hunger");
        HUNGER_INITIAL = builder
                .comment("初始饥饿值")
                .comment("Initial hunger value")
                .defineInRange("initial", 0.0, 0.0, Integer.MAX_VALUE);
        HUNGER_MIN = builder
                .comment("最低饥饿值")
                .comment("Minimum hunger value")
                .defineInRange("min", 0.0, 0.0, Integer.MAX_VALUE);
        HUNGER_MAX = builder
                .comment("最高饥饿值")
                .comment("Maximum hunger value")
                .defineInRange("max", 10.0, 0.0, Integer.MAX_VALUE);

        builder.comment("饥饿提升配置").push("Hunger Rise");
        HUNGER_RISE_BASE_BONUS = builder
                .comment("基础喂食增益")
                .comment("Base feeding bonus")
                .defineInRange("baseBonus", 1.0, 0.0, Integer.MAX_VALUE);
        HUNGER_RISE_BONUS_DECAY = builder
                .comment("连续喂食增益衰减")
                .comment("Consecutive feeding bonus decay")
                .defineInRange("bonusDecay", 0.2, 0.0, Integer.MAX_VALUE);
        HUNGER_RISE_FEED_COOLDOWN_TICKS = builder
                .comment("手动喂食冷却(tick)")
                .comment("Manual feeding cooldown (ticks)")
                .defineInRange("feedCooldownTicks", 9600, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.comment("饥饿下降配置").push("Hunger Decline");
        HUNGER_DECLINE_DAILY_AMOUNT = builder
                .comment("每日饥饿下降量")
                .comment("Daily hunger decline amount")
                .defineInRange("dailyAmount", 2.0, 0.0, Integer.MAX_VALUE);
        HUNGER_DECLINE_FECES_AMOUNT = builder
                .comment("每份粪便饥饿下降量")
                .comment("Hunger decline amount per feces")
                .defineInRange("fecesAmount", 0.2, 0.0, Integer.MAX_VALUE);
        HUNGER_DECLINE_FECES_TICK_INTERVAL = builder
                .comment("粪便饥饿下降tick间隔")
                .comment("Feces hunger decline tick interval")
                .defineInRange("fecesTickInterval", 3600, 100, Integer.MAX_VALUE);
        HUNGER_DECLINE_FECES_RADIUS = builder
                .comment("粪便饥饿检测半径")
                .comment("Feces hunger detection radius")
                .defineInRange("fecesRadius", 8, 1, 32);
        builder.pop();

        builder.push("naturalDecline");
        HUNGER_NATURAL_DECLINE_PER_MINUTE = builder
                .comment("每分钟自然饥饿下降量")
                .comment("Natural hunger decline per minute")
                .defineInRange("perMinute", 0.1, 0.0, 10.0);
        builder.pop();

        builder.pop();


        builder.comment("好感度配置").push("Favor");
        FAVOR_EXP_TABLE = builder
                .comment("每级升级所需经验: 0→1, 1→2, 2→3, ..., 10→11, 11→12")
                .comment("升级后当前等级经验归零，每个等级独立计算")
                .comment("Exp required per level: 0→1, 1→2, 2→3, ..., 10→11, 11→12")
                .comment("Exp resets to 0 on level up, each level is independent")
                .defineList("expTable", List.of(
                        5, 10, 25, 50, 75,
                        100, 125, 150, 175, 200,
                        250, 500
                        ), o -> o instanceof Integer && (Integer) o > 0);

        builder.comment("好感经验提升配置").push("Favor Rise");
        FAVOR_RISE_PET_EXP_MIN = builder
                .comment("抚摸经验最小值")
                .comment("Petting exp minimum")
                .defineInRange("petExpMin", 1, 1, Integer.MAX_VALUE);
        FAVOR_RISE_PET_EXP_MAX = builder
                .comment("抚摸经验最大值")
                .comment("Petting exp maximum")
                .defineInRange("petExpMax", 12, 1, Integer.MAX_VALUE);
        FAVOR_RISE_MOOD_EXP_MIN = builder
                .comment("心情提升每点经验最小值")
                .comment("Mood rise exp per point minimum")
                .defineInRange("moodExpMin", 1, 0, Integer.MAX_VALUE);
        FAVOR_RISE_MOOD_EXP_MAX = builder
                .comment("心情提升每点经验最大值")
                .comment("Mood rise exp per point maximum")
                .defineInRange("moodExpMax", 5, 0, Integer.MAX_VALUE);
        FAVOR_RISE_HUNGER_EXP_MIN = builder
                .comment("饥饿提升每点经验最小值")
                .comment("Hunger rise exp per point minimum")
                .defineInRange("hungerExpMin", 1, 0, Integer.MAX_VALUE);
        FAVOR_RISE_HUNGER_EXP_MAX = builder
                .comment("饥饿提升每点经验最大值")
                .comment("Hunger rise exp per point maximum")
                .defineInRange("hungerExpMax", 10, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.comment("好感经验下降配置").push("Favor Decline");
        FAVOR_DECLINE_HURT_EXP_MIN = builder
                .comment("受伤经验惩罚最小值")
                .comment("Hurt exp penalty minimum")
                .defineInRange("hurtExpMin", 10, 0, Integer.MAX_VALUE);
        FAVOR_DECLINE_HURT_EXP_MAX = builder
                .comment("受伤经验惩罚最大值")
                .comment("Hurt exp penalty maximum")
                .defineInRange("hurtExpMax", 30, 0, Integer.MAX_VALUE);
        FAVOR_DECLINE_RAIN_EXP = builder
                .comment("淋雨经验下降量(每20秒)")
                .comment("Rain exp decline amount (per 20s)")
                .defineInRange("rainExp", 1, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.pop();


        builder.comment("野生系统配置").push("Wild");
        WILD_INITIAL_MOOD_MIN = builder
                .comment("野生初始心情最小值")
                .comment("Wild initial mood minimum")
                .defineInRange("initialMoodMin", 50.0, 0.0, Integer.MAX_VALUE);
        WILD_INITIAL_MOOD_MAX = builder
                .comment("野生初始心情最大值")
                .comment("Wild initial mood maximum")
                .defineInRange("initialMoodMax", 100.0, 0.0, Integer.MAX_VALUE);
        WILD_INITIAL_HUNGER_MIN = builder
                .comment("野生初始饥饿最小值")
                .comment("Wild initial hunger minimum")
                .defineInRange("initialHungerMin", 0.0, 0.0, Integer.MAX_VALUE);
        WILD_INITIAL_HUNGER_MAX = builder
                .comment("野生初始饥饿最大值")
                .comment("Wild initial hunger maximum")
                .defineInRange("initialHungerMax", 9.0, 0.0, Integer.MAX_VALUE);
        WILD_TAME_FAVOR_THRESHOLD = builder
                .comment("驯化好感等级阈值")
                .comment("Taming favor level threshold")
                .defineInRange("tameFavorThreshold", 3, 0, 12);
        builder.pop();


        builder.comment("逃跑AI配置").push("Flee AI");
        FLEE_DETECT_RADIUS = builder
                .comment("逃跑检测半径")
                .comment("Flee detection radius")
                .defineInRange("detectRadius", 5.0, 1.0, 32.0);
        FLEE_SPEED = builder
                .comment("逃跑速度")
                .comment("Flee speed")
                .defineInRange("speed", 1.2, 0.1, Integer.MAX_VALUE);
        FLEE_DISTANCE = builder
                .comment("逃跑距离")
                .comment("Flee distance")
                .defineInRange("distance", 5.0, 1.0, 32.0);
        FLEE_DISTANCE_VARIANCE = builder
                .comment("逃跑距离随机偏移")
                .comment("Flee distance random variance")
                .defineInRange("distanceVariance", 5.0, 0.0, 32.0);
        builder.pop();


        builder.comment("喂食槽配置").push("Feeding Trough");
        TROUGH_DETECT_RADIUS = builder
                .comment("喂食槽检测半径")
                .comment("Trough detection radius")
                .defineInRange("detectRadius", 8.0, 1.0, 32.0);
        TROUGH_FEED_COOLDOWN_TICKS = builder
                .comment("喂食槽喂食冷却(tick)")
                .comment("Trough feeding cooldown (ticks)")
                .defineInRange("feedCooldownTicks", 1200, 0, Integer.MAX_VALUE);
        TROUGH_FEED_GAIN = builder
                .comment("喂食槽每次进食增加的饥饿值")
                .comment("Hunger gain per trough feed")
                .defineInRange("feedGain", 1.0, 0.0, 10.0);
        builder.pop();


        builder.comment("干草窝配置").push("Haystack Hollow");
        HAYSTACK_DETECT_RADIUS = builder
                .comment("干草窝搜索半径(禽类寻找干草窝下蛋的范围)")
                .comment("Haystack detection radius (range for poultry to find haystack for laying eggs)")
                .defineInRange("detectRadius", 16, 1, 32);
        HAYSTACK_COLLECT_RADIUS = builder
                .comment("干草窝收集蛋半径(干草窝主动吸收地面蛋物品的范围)")
                .comment("Haystack egg collect radius (range for haystack to absorb egg items on the ground)")
                .defineInRange("collectRadius", 8, 1, 32);
        HAYSTACK_COLLECT_INTERVAL = builder
                .comment("干草窝收集蛋频率(tick)")
                .comment("Haystack egg collect interval (tick)")
                .defineInRange("collectInterval", 200, 1, Integer.MAX_VALUE);
        builder.pop();


        builder.comment("哞菇配置").push("Mooshroom");
        MOOSHROOM_STEW_EFFECT_DURATION_TICKS = builder
                .comment("蘑菇煲效果持续时间(tick)")
                .comment("Mushroom stew effect duration (ticks)")
                .defineInRange("stewEffectDurationTicks", 100, 1, Integer.MAX_VALUE);
        builder.pop();

        builder.comment("繁殖配置").push("Breeding");
        BREED_EXTRA_OFFSPRING_CHANCE = builder
                .comment("牛/哞菇双方好感≥10时额外繁殖子代的概率")
                .comment("Chance of extra offspring when both cow/mooshroom parents have favor >= 10")
                .defineInRange("extraOffspringChance", 0.0156, 0.0, 1.0);
        BREED_EXTRA_OFFSPRING_COUNT = builder
                .comment("额外繁殖子代数量")
                .comment("Number of extra offspring")
                .defineInRange("extraOffspringCount", 1, 1, Integer.MAX_VALUE);
        BREED_SHEEP_EXTRA_2_CHANCE = builder
                .comment("羊双方好感≥10时额外繁殖2只子代的概率")
                .comment("Chance of 2 extra offspring when both sheep parents have favor >= 10")
                .defineInRange("sheepExtra2Chance", 0.20, 0.0, 1.0);
        BREED_SHEEP_EXTRA_3_CHANCE = builder
                .comment("羊双方好感≥10时额外繁殖3只子代的概率")
                .comment("Chance of 3 extra offspring when both sheep parents have favor >= 10")
                .defineInRange("sheepExtra3Chance", 0.10, 0.0, 1.0);
        BREED_SHEEP_EXTRA_4_CHANCE = builder
                .comment("羊双方好感≥10时额外繁殖4只子代的概率")
                .comment("Chance of 4 extra offspring when both sheep parents have favor >= 10")
                .defineInRange("sheepExtra4Chance", 0.05, 0.0, 1.0);
        BREED_SHEEP_EXTRA_5_CHANCE = builder
                .comment("羊双方好感≥10时额外繁殖5只子代的概率")
                .comment("Chance of 5 extra offspring when both sheep parents have favor >= 10")
                .defineInRange("sheepExtra5Chance", 0.0156, 0.0, 1.0);
        BREED_GOAT_EXTRA_2_CHANCE = builder
                .comment("山羊双方好感≥10时额外繁殖2只子代的概率")
                .comment("Chance of 2 extra offspring when both goat parents have favor >= 10")
                .defineInRange("goatExtra2Chance", 0.20, 0.0, 1.0);
        BREED_GOAT_EXTRA_3_CHANCE = builder
                .comment("山羊双方好感≥10时额外繁殖3只子代的概率")
                .comment("Chance of 3 extra offspring when both goat parents have favor >= 10")
                .defineInRange("goatExtra3Chance", 0.10, 0.0, 1.0);
        BREED_GOAT_EXTRA_4_CHANCE = builder
                .comment("山羊双方好感≥10时额外繁殖4只子代的概率")
                .comment("Chance of 4 extra offspring when both goat parents have favor >= 10")
                .defineInRange("goatExtra4Chance", 0.05, 0.0, 1.0);
        BREED_GOAT_EXTRA_5_CHANCE = builder
                .comment("山羊双方好感≥10时额外繁殖5只子代的概率")
                .comment("Chance of 5 extra offspring when both goat parents have favor >= 10")
                .defineInRange("goatExtra5Chance", 0.0156, 0.0, 1.0);
        BREED_GOAT_EXTRA_6_CHANCE = builder
                .comment("山羊双方好感≥10时额外繁殖6只子代的概率")
                .comment("Chance of 6 extra offspring when both goat parents have favor >= 10")
                .defineInRange("goatExtra6Chance", 0.0039, 0.0, 1.0);

        BREED_PIG_BASE_COUNT = builder
                .comment("猪双方好感≥10时保底繁殖子代数量")
                .comment("Base offspring count when both pig parents have favor >= 10")
                .defineInRange("pigBaseCount", 3, 1, Integer.MAX_VALUE);
        BREED_PIG_TIER1_CHANCE = builder
                .comment("猪繁殖第1档概率(50%→4-8只)")
                .comment("Pig breed tier1 chance (50% -> 4-8)")
                .defineInRange("pigTier1Chance", 0.50, 0.0, 1.0);
        BREED_PIG_TIER1_MIN = builder
                .defineInRange("pigTier1Min", 4, 1, Integer.MAX_VALUE);
        BREED_PIG_TIER1_MAX = builder
                .defineInRange("pigTier1Max", 8, 1, Integer.MAX_VALUE);
        BREED_PIG_TIER2_CHANCE = builder
                .comment("猪繁殖第2档概率(30%→9-11只)")
                .comment("Pig breed tier2 chance (30% -> 9-11)")
                .defineInRange("pigTier2Chance", 0.30, 0.0, 1.0);
        BREED_PIG_TIER2_MIN = builder
                .defineInRange("pigTier2Min", 9, 1, Integer.MAX_VALUE);
        BREED_PIG_TIER2_MAX = builder
                .defineInRange("pigTier2Max", 11, 1, Integer.MAX_VALUE);
        BREED_PIG_TIER3_CHANCE = builder
                .comment("猪繁殖第3档概率(8%→12-15只)")
                .comment("Pig breed tier3 chance (8% -> 12-15)")
                .defineInRange("pigTier3Chance", 0.08, 0.0, 1.0);
        BREED_PIG_TIER3_MIN = builder
                .defineInRange("pigTier3Min", 12, 1, Integer.MAX_VALUE);
        BREED_PIG_TIER3_MAX = builder
                .defineInRange("pigTier3Max", 15, 1, Integer.MAX_VALUE);
        BREED_PIG_TIER4_CHANCE = builder
                .comment("猪繁殖第4档概率(1.56%→16-17只)")
                .comment("Pig breed tier4 chance (1.56% -> 16-17)")
                .defineInRange("pigTier4Chance", 0.0156, 0.0, 1.0);
        BREED_PIG_TIER4_MIN = builder
                .defineInRange("pigTier4Min", 16, 1, Integer.MAX_VALUE);
        BREED_PIG_TIER4_MAX = builder
                .defineInRange("pigTier4Max", 17, 1, Integer.MAX_VALUE);
        BREED_PIG_TIER5_CHANCE = builder
                .comment("猪繁殖第5档概率(0.39%→18-20只)")
                .comment("Pig breed tier5 chance (0.39% -> 18-20)")
                .defineInRange("pigTier5Chance", 0.0039, 0.0, 1.0);
        BREED_PIG_TIER5_MIN = builder
                .defineInRange("pigTier5Min", 18, 1, Integer.MAX_VALUE);
        BREED_PIG_TIER5_MAX = builder
                .defineInRange("pigTier5Max", 20, 1, Integer.MAX_VALUE);

        BREED_RABBIT_TIER1_CHANCE = builder
                .comment("兔繁殖第1档概率(50%→2只)")
                .comment("Rabbit breed tier1 chance (50% -> 2)")
                .defineInRange("rabbitTier1Chance", 0.50, 0.0, 1.0);
        BREED_RABBIT_TIER1_COUNT = builder
                .defineInRange("rabbitTier1Count", 2, 1, Integer.MAX_VALUE);
        BREED_RABBIT_TIER2_CHANCE = builder
                .comment("兔繁殖第2档概率(25%→3-4只)")
                .comment("Rabbit breed tier2 chance (25% -> 3-4)")
                .defineInRange("rabbitTier2Chance", 0.25, 0.0, 1.0);
        BREED_RABBIT_TIER2_MIN = builder
                .defineInRange("rabbitTier2Min", 3, 1, Integer.MAX_VALUE);
        BREED_RABBIT_TIER2_MAX = builder
                .defineInRange("rabbitTier2Max", 4, 1, Integer.MAX_VALUE);
        BREED_RABBIT_TIER3_CHANCE = builder
                .comment("兔繁殖第3档概率(15%→5-6只)")
                .comment("Rabbit breed tier3 chance (15% -> 5-6)")
                .defineInRange("rabbitTier3Chance", 0.15, 0.0, 1.0);
        BREED_RABBIT_TIER3_MIN = builder
                .defineInRange("rabbitTier3Min", 5, 1, Integer.MAX_VALUE);
        BREED_RABBIT_TIER3_MAX = builder
                .defineInRange("rabbitTier3Max", 6, 1, Integer.MAX_VALUE);
        BREED_RABBIT_TIER4_CHANCE = builder
                .comment("兔繁殖第4档概率(8%→7只)")
                .comment("Rabbit breed tier4 chance (8% -> 7)")
                .defineInRange("rabbitTier4Chance", 0.08, 0.0, 1.0);
        BREED_RABBIT_TIER4_COUNT = builder
                .defineInRange("rabbitTier4Count", 7, 1, Integer.MAX_VALUE);
        BREED_RABBIT_TIER5_CHANCE = builder
                .comment("兔繁殖第5档概率(1.56%→8只)")
                .comment("Rabbit breed tier5 chance (1.56% -> 8)")
                .defineInRange("rabbitTier5Chance", 0.0156, 0.0, 1.0);
        BREED_RABBIT_TIER5_COUNT = builder
                .defineInRange("rabbitTier5Count", 8, 1, Integer.MAX_VALUE);
        BREED_RABBIT_TIER6_CHANCE = builder
                .comment("兔繁殖第6档概率(0.39%→9-10只)")
                .comment("Rabbit breed tier6 chance (0.39% -> 9-10)")
                .defineInRange("rabbitTier6Chance", 0.0039, 0.0, 1.0);
        BREED_RABBIT_TIER6_MIN = builder
                .defineInRange("rabbitTier6Min", 9, 1, Integer.MAX_VALUE);
        BREED_RABBIT_TIER6_MAX = builder
                .defineInRange("rabbitTier6Max", 10, 1, Integer.MAX_VALUE);
        DONKEY_MAX_BREED_COUNT = builder
                .comment("驴最大繁殖次数")
                .comment("Max breed count for donkey")
                .defineInRange("donkeyMaxBreedCount", 15, 1, Integer.MAX_VALUE);
        BREED_LLAMA_EXTRA_CHANCE = builder
                .comment("羊驼双方好感≥10时额外繁殖概率")
                .comment("Chance of extra offspring when both llama parents have favor >= 10")
                .defineInRange("llamaExtraChance", 0.0156, 0.0, 1.0);
        BREED_LLAMA_EXTRA_COUNT = builder
                .comment("羊驼额外繁殖子代数量")
                .comment("Extra offspring count for llama")
                .defineInRange("llamaExtraCount", 2, 1, Integer.MAX_VALUE);

        BREED_OFFSPRING_MOOD_MIN = builder
                .comment("繁殖子代初始心情最小值")
                .comment("Bred offspring initial mood minimum")
                .defineInRange("offspringMoodMin", 50.0, 0.0, 120.0);
        BREED_OFFSPRING_MOOD_MAX = builder
                .comment("繁殖子代初始心情最大值")
                .comment("Bred offspring initial mood maximum")
                .defineInRange("offspringMoodMax", 120.0, 0.0, 120.0);
        BREED_OFFSPRING_HUNGER_MIN = builder
                .comment("繁殖子代初始饥饿最小值")
                .comment("Bred offspring initial hunger minimum")
                .defineInRange("offspringHungerMin", 1.0, 0.0, 10.0);
        BREED_OFFSPRING_HUNGER_MAX = builder
                .comment("繁殖子代初始饥饿最大值")
                .comment("Bred offspring initial hunger maximum")
                .defineInRange("offspringHungerMax", 3.0, 0.0, 10.0);
        BREED_OFFSPRING_FAVOR_LEVEL = builder
                .comment("繁殖子代初始好感等级")
                .comment("Bred offspring initial favor level")
                .defineInRange("offspringFavorLevel", 12, 0, 12);
        BREED_COOLDOWN_TICKS = builder
                .comment("繁殖冷却时间(tick)")
                .comment("Breeding cooldown (ticks)")
                .defineInRange("cooldownTicks", 24000, 0, Integer.MAX_VALUE);
        builder.pop();


        builder.comment("猪寻宝配置").push("Pig Treasure Hunt");
        PIG_TREASURE_INTERVAL = builder
                .comment("寻宝间隔(tick)")
                .comment("Treasure hunt interval (ticks)")
                .defineInRange("interval", 6000, 100, Integer.MAX_VALUE);
        PIG_TREASURE_CHANCE = builder
                .comment("寻宝概率")
                .comment("Treasure hunt chance")
                .defineInRange("chance", 0.3, 0.0, 1.0);
        PIG_TREASURE_FAVOR_THRESHOLD = builder
                .comment("寻宝好感等级阈值")
                .comment("Treasure hunt favor level threshold")
                .defineInRange("favorThreshold", 10, 0, 12);
        builder.pop();


        builder.comment("战利品倍率配置").push("Loot Multiplier");
        builder.comment("第一阶").push("Tier1");
        LOOT_TIER1_MOOD_THRESHOLD = builder
                .comment("心情值阈值")
                .comment("Mood threshold")
                .defineInRange("moodThreshold", 100.0, 0.0, 120.0);
        LOOT_TIER1_FAVOR_THRESHOLD = builder
                .comment("好感等级阈值")
                .comment("Favor level threshold")
                .defineInRange("favorThreshold", 8, 0, 12);
        LOOT_TIER1_MULTIPLIER = builder
                .comment("额外掉落倍率")
                .comment("Extra loot multiplier")
                .defineInRange("multiplier", 1.0, 0.0, 10.0);
        builder.pop();
        builder.comment("第二阶").push("Tier2");
        LOOT_TIER2_MOOD_THRESHOLD = builder
                .comment("心情值阈值")
                .comment("Mood threshold")
                .defineInRange("moodThreshold", 110.0, 0.0, 120.0);
        LOOT_TIER2_FAVOR_THRESHOLD = builder
                .comment("好感等级阈值")
                .comment("Favor level threshold")
                .defineInRange("favorThreshold", 10, 0, 12);
        LOOT_TIER2_MULTIPLIER = builder
                .comment("额外掉落倍率")
                .comment("Extra loot multiplier")
                .defineInRange("multiplier", 2.0, 0.0, 10.0);
        builder.pop();
        builder.comment("第三阶").push("Tier3");
        LOOT_TIER3_MOOD_THRESHOLD = builder
                .comment("心情值阈值")
                .comment("Mood threshold")
                .defineInRange("moodThreshold", 120.0, 0.0, 120.0);
        LOOT_TIER3_FAVOR_THRESHOLD = builder
                .comment("好感等级阈值")
                .comment("Favor level threshold")
                .defineInRange("favorThreshold", 12, 0, 12);
        LOOT_TIER3_MULTIPLIER = builder
                .comment("额外掉落倍率")
                .comment("Extra loot multiplier")
                .defineInRange("multiplier", 3.0, 0.0, 10.0);
        builder.pop();

        builder.pop();

        builder.comment("羊剪毛配置").push("Sheep Shearing");
        SHEEP_SHEAR_COOLDOWN_TICKS = builder
                .comment("剪毛冷却时间(tick)")
                .comment("Shearing cooldown (ticks)")
                .defineInRange("shearCooldownTicks", 9600, 0, Integer.MAX_VALUE);
        builder.pop();

        SPEC = builder.build();
    }
}