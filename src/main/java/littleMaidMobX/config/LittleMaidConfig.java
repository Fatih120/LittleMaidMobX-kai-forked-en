package littleMaidMobX.config;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

public class LittleMaidConfig {

    public static final String CATEGORY_CLIENT = "client";
    public static final String CATEGORY_SPAWNING = "spawning";
    public static final String CATEGORY_MAIDS = "maids";
    public static final String CATEGORY_ITEMS = "items";
    public static final String CATEGORY_DEBUG = "debug";

    private static Configuration configuration;
    public static boolean oldStatsRendering;

    public static int spawnWeight;
    public static int minGroupSize;
    public static int maxGroupSize;
    public static boolean spawnMaidsEverywhere;

    public static boolean canDespawn;
    public static boolean checkOwnerName;
    public static boolean fixLittleMaidDupe;
    public static boolean enableSpawnEggRecipe;
    public static boolean enableDisplaySugarCount;
    public static boolean voiceDistortion;
    public static String defaultTexture;
    public static boolean deathMessage;
    public static boolean setDefaultIFFFriendly;
    public static String[] ignoreItemList;
    public static boolean enableFarmerMode;
    public static int talkInterval;

    public static boolean printDebugMessage;
//	public static boolean AlphaBlend = true;

    public static void init(FMLPreInitializationEvent event){
        configuration = new Configuration(event.getSuggestedConfigurationFile());
    }

    public static void save(){
        configuration.save();
    }

    public static void load(){
        configuration.load();
        configuration.setCategoryRequiresMcRestart(CATEGORY_SPAWNING, true);
        configuration.setCategoryComment(CATEGORY_SPAWNING, "Spawning Options (Required Restart)");
        configuration.setCategoryRequiresMcRestart(CATEGORY_ITEMS, true);
    }

    public static void sync(){
        oldStatsRendering = configuration.getBoolean("[WIP] Old Stats Rendering", CATEGORY_CLIENT, false, "Set maid gui stats drawing to old version of LittleMaid.");
        spawnWeight = configuration.getInt("Spawn Weight", CATEGORY_SPAWNING, 5, 0, 10, "Set the spawn chance of LittleMaid. set 0 to disable the spawn.");
        minGroupSize = configuration.getInt("Minimum Spawn Group Size", CATEGORY_SPAWNING, 1, 1, 10, "Minimum spawn group count.");
        maxGroupSize = configuration.getInt("Maximum Spawn Group Size", CATEGORY_SPAWNING, 3, 1, 10, "Maximum spawn group count.");
        spawnMaidsEverywhere = configuration.getBoolean("Spawn Maids Everywhere", CATEGORY_SPAWNING, false, "If true maids will spawn in all biomes, if false maids will only spawn in biomes of approved types.");

        canDespawn = configuration.getBoolean("Can Despawn", CATEGORY_MAIDS, false, "Whether or not a Maid without a contract can despawn.");
        checkOwnerName = configuration.getBoolean("Check Owner Name", CATEGORY_MAIDS, true, "Checks the name of owner, if you do multiplayer keep this true.");
        fixLittleMaidDupe = configuration.getBoolean("Fix LittleMaid Dupe", CATEGORY_MAIDS, true, "Fix LittleMaid duplication on load.");
        enableDisplaySugarCount = configuration.getBoolean("Enable Display Sugar Count", CATEGORY_MAIDS, true, "Enable the sugar count display on LittleMaid's head.");
        voiceDistortion = configuration.getBoolean("Voice Distortion", CATEGORY_MAIDS, true, "Enables LittleMaid voices to distort based of hair color.");
        defaultTexture = configuration.getString("Default Texture", CATEGORY_MAIDS, "", "Default selected Texture Package. if set to empty, uses random selection");
        deathMessage = configuration.getBoolean("Print Death Message", CATEGORY_MAIDS, true, "Prints message on the death of your maid.");
        setDefaultIFFFriendly = configuration.getBoolean("Set Default IFF Friendly", CATEGORY_MAIDS, true, "Set non enemy mobs IFF value. if set to true, non enemy default IFF values to Friendly. if set to false, non enemy default IFF values to Enemy.");
        ignoreItemList = configuration.getStringList("Ignore Item List", CATEGORY_MAIDS, new String[]{"arsmagica2"}, "");
        enableFarmerMode = configuration.getBoolean("[Deprecated] Enable Farmer Mode", CATEGORY_MAIDS, true, "Enable/Disable farmer mode. if cause the lag in farmer mode, maybe you should set to false to fix this.\n§c(Deprecated reason: Maybe this bug fixed later, i'll delete this config, but no eta)§e");
        talkInterval = configuration.getInt("Talk Interval", CATEGORY_MAIDS, 120, 0, 200, "Change LittleMaid living sound times");

        enableSpawnEggRecipe = configuration.getBoolean("Enable Spawn Egg Recipe", CATEGORY_ITEMS, true, "Enable LittleMaid spawn egg recipe.");

        printDebugMessage = configuration.getBoolean("Print Debug Message", CATEGORY_DEBUG, false, "If true will output debug messages.");
    }


    public static void check(){
        load();
        sync();
        if (configuration.hasChanged()){
            save();
        }
    }

    public static Configuration configuration(){
        return configuration;
    }
}
