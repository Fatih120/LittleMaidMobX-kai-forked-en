package littleMaidMobX.client.audio;

public enum EnumSound {

	death(0x100,			"Death Voice.",							""),
	attack(0x110,			"Attack Voice.",						""),
	attack_bloodsuck(0x111, "Attack Bloodsucker Voice.",			""),
	laughter(0x120,			"Laughter Voice.",						""),
	shoot(0x130,			"Shoot Voice.",						""),
	shoot_burst(0x131,		"Burst shoot Voice.",					""),
	sighting(0x140,			"Spotted Voice.",					""),
	healing(0x150,			"Healing Voice.",						""),
	healing_potion(0x151, 	"Healing with potion Voice.",			""),
	TNT_D(0x160, 			"Enable TNT-D Voice.",					""),
//	eatGunpowder(0x161, 	"Eat Gunpowder Voice.",				""),

	eatSugar(0x200, 		"Eat Sugar Voice.",					""),
	eatSugar_MaxPower(0x201,"Eat Sugar to MAX healing Voice.",		""),
	getCake(0x210, 			"Get Cake Voice.",						""),
	Recontract(0x211,		"Recontract Voice.",					""),
	addFuel(0x220,			"Add Fuel Voice.",						""),
	cookingStart(0x221,		"Cooking Start Voice.",				""),
	cookingOver(0x222,		"Cooking Over Voice.",					""),
	installation(0x230,		"Installation Voice.",					""),
	collect_snow(0x240,		"Collecting snow Voice.",				""),

	hurt(0x300,				"Damaged Voice.",						""),
	hurt_snow(0x301,		"Damaged Voice from snowball.",		""),
	hurt_fire(0x302,		"Damaged Voice from fire.",			""),
	hurt_guard(0x303,		"Damaged Voice on Guard.",				""),
	hurt_fall(0x304,		"Damaged Voice from Fall.",			""),
	hurt_nodamege(0x309,	"No Damage Voice.",					""),

	findTarget_N(0x400,		"Found target Normal Voice.",			""),
	findTarget_B(0x401,		"Found target Bloodsucker Voice.",		""),
	findTarget_I(0x402,		"Found target Item Voice.",				""),
	findTarget_D(0x403,		"Found target Darkness Voice.",			""),

	living_daytime(0x500,	"Living Voice(Default) in Daytime.",	""),
	living_morning(0x501,	"Living Voice in Morning.",				""),
	living_night(0x502,		"Living Voice in Night.",				""),
	living_whine(0x503,		"Living Voice at Whine.",				""),
	living_rain(0x504,		"Living Voice at Rain.",				""),
	living_snow(0x505,		"Living Voice at Snow.",				""),
	living_cold(0x506,		"Living Voice at Cold.",				""),
	living_hot(0x507,		"Living Voice at Hot.",				""),
	goodmorning(0x551,		"Good morning Voice.",					""),
	goodnight(0x561,		"Good night Voice.",					""),


	Null(0, "", null);
	
	
	public final int index;
	public final String info;
	public final String defaultSound;



	EnumSound(int index, String info, String defaultSound) {
		this.index = index;
		this.info = info;
		this.defaultSound = defaultSound;
	}

	/**
	 * 指定されたインデックスのEnumSoundを返す。
	 */
	public static EnumSound getEnumSound(int index) {
		for (EnumSound le : EnumSound.values()) {
			if (le.index == index) {
				return le;
			}
		}
		return Null;
	}

}
