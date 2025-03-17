package littleMaidMobX.entity.modes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import littleMaidMobX.LittleMaidMobX;
import littleMaidMobX.config.LittleMaidConfig;
import littleMaidMobX.entity.EntityLittleMaid;
import mmmlibx.lib.MMM_Helper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import littleMaidMobX.wrapper.W_Common;

/**
 * IFFを管理するためのクラス、ほぼマルチ用。
 * username : null=ローカルプレイ時、Defaultを使う
 */
public class IFF {

	public static final int iff_Enemy = 0;
	public static final int iff_Unknown = 1;
	public static final int iff_Friendry = 2;

	/**
	 * ローカル用、若しくはマルチのデフォルト設定
	 */
	public static Map<String, Integer> DefaultIFF = new TreeMap<>();
	/**
	 * ユーザ毎のIFF
	 */
	public static Map<String, Map<String, Integer>> UserIFF = new HashMap<String, Map<String, Integer>>();

	/**
	 * IFFのゲット
	 */
	public static Map<String, Integer> getUserIFF(String pUsername) {
		if (pUsername == null) {
			return DefaultIFF;
		}
		if (MMM_Helper.isLocalPlay()) {
			pUsername = "";
		}
		
		if (!UserIFF.containsKey(pUsername)) {
			// IFFがないので作成
			if (pUsername.isEmpty()) {
				UserIFF.put(pUsername, DefaultIFF);
			} else {
				Map<String, Integer> lmap = new HashMap<String, Integer>();
				lmap.putAll(DefaultIFF);
				UserIFF.put(pUsername, lmap);
			}
		}
		// 既にある
		return UserIFF.get(pUsername);
	}

	public static void setIFFValue(String pUsername, String pName, int pValue) {
		Map<String, Integer> lmap = getUserIFF(pUsername);
		lmap.put(pName, pValue);
	}

	public static int checkEntityStatic(String pName, Entity pEntity,
			int pIndex, Map<String, Entity> pMap) {
		int liff = IFF.iff_Unknown;
		if (pEntity instanceof EntityLivingBase) {
			if (pEntity instanceof EntityLittleMaid) {
				switch (pIndex) {
				case 0:
					// 野生種
					liff = IFF.iff_Unknown;
					break;
				case 1:
					// 自分の契約者
					pName = (new StringBuilder()).append(pName).append(":Contract").toString();
					((EntityLittleMaid) pEntity).setContract(true);
					liff = IFF.iff_Friendry;
					break;
				case 2:
					// 他人の契約者
					pName = (new StringBuilder()).append(pName).append(":Others").toString();
					((EntityLittleMaid) pEntity).setContract(true);
					liff = IFF.iff_Friendry;
					break;
				}
			} else if (pEntity instanceof IEntityOwnable) {
				switch (pIndex) {
				case 0:
					// 野生種
					break;
				case 1:
					// 自分の家畜
					pName = (new StringBuilder()).append(pName).append(":Taim").toString();
					if (pEntity instanceof EntityTameable) {
						((EntityTameable) pEntity).setTamed(true);
					}
					liff = IFF.iff_Friendry;
					break;
				case 2:
					// 他人の家畜
					pName = (new StringBuilder()).append(pName).append(":Others").toString();
					if (pEntity instanceof EntityTameable) {
						((EntityTameable) pEntity).setTamed(true);
					}
					liff = IFF.iff_Unknown;
					break;
				}
				if (pIndex != 0) {
					if (pEntity instanceof EntityOcelot) {
						((EntityOcelot) pEntity).setTameSkin(1 + (new Random()).nextInt(3));
					}
				}
			}
			if (pMap != null) {
				// 表示用Entityの追加
				pMap.put(pName, pEntity);
				LittleMaidMobX.debug(pName + " added.");
			}
			
			// IFFの初期値
			if (!DefaultIFF.containsKey(pName)) {
				if (pEntity instanceof IMob) {
					liff = IFF.iff_Enemy;
				}
				DefaultIFF.put(pName, liff);
			}
		}
		
		return liff;
	}

	/**
	 * 敵味方識別判定
	 */
	public static int getIFF(String pUsername, String entityName, World world) {
		if (entityName == null) {
			return LittleMaidConfig.setDefaultIFFFriendly ? iff_Enemy : iff_Friendry;
		}
		int lt;
		Map<String, Integer> lmap = getUserIFF(pUsername);
		if (lmap.containsKey(entityName)) {
			lt = lmap.get(entityName);
		} else if (lmap != DefaultIFF && DefaultIFF.containsKey(entityName)) {
			// 未登録だけどDefaultには設定がある時は値をコピー
			lt = DefaultIFF.get(entityName);
			lmap.put(entityName, lt);
		} else {
			// 未登録Entityの場合は登録動作
			int li = entityName.indexOf(":");
			String ls;
			if (li > -1) {
				ls = entityName.substring(0, li);
			} else {
				ls = entityName;
			}
			Entity lentity = EntityList.createEntityByName(ls, world);
			li = 0;
			if (entityName.contains(":Contract")) {
				li = 1;
			} else 
			if (entityName.contains(":Taim")) {
				li = 1;
			} else
			if (entityName.contains(":Others")) {
				li = 2;
			}
			lt = checkEntityStatic(ls, lentity, li, null);
			lmap.put(entityName, lt);
		}
		return lt;
	}

	/**
	 * 敵味方識別判定
	 */
	public static int getIFF(String username, Entity entity) {
		if (!(entity instanceof EntityLivingBase)) {
			return LittleMaidConfig.setDefaultIFFFriendly ? iff_Enemy : iff_Friendry;
		}
		String lename = EntityList.getEntityString(entity);
		String lcname = lename;
		if (lename == null) {
			// 名称未定義MOB、プレーヤーとか？
			return iff_Friendry;
			// return mod_LMM_littleMaidMob.Aggressive ? iff_Unknown :
			// iff_Friendry;
		}
		int li = 0;
		if (entity instanceof EntityLittleMaid) {
			if (((EntityLittleMaid) entity).isContract()) {
				if (((EntityLittleMaid) entity).getMaidMaster().contentEquals(username)) {
					// 自分の
					lcname = (new StringBuilder()).append(lename).append(":Contract").toString();
					li = 1;
				} else {
					// 他人の
					lcname = (new StringBuilder()).append(lename).append(":Others").toString();
					li = 2;
				}
			}
		} else if (entity instanceof IEntityOwnable) {
			String loname = W_Common.getOwnerName((IEntityOwnable)entity);
			if (!loname.isEmpty()) {
				if (loname.contentEquals(username)) {
					// 自分の
					lcname = (new StringBuilder()).append(lename).append(":Taim").toString();
					li = 1;
				} else {
					// 他人の
					lcname = (new StringBuilder()).append(lename).append(":Others").toString();
					li = 2;
				}
			}
		}
		if (!getUserIFF(username).containsKey(lcname)) {
			checkEntityStatic(lename, entity, li, null);
		}
		return getIFF(username, lcname, entity.worldObj);
	}

	public static void loadIFFs() {
		// サーバー側の
		if (!MMM_Helper.isClient) {
			// サーバー側処理
			loadIFF("");
			File lfile = MinecraftServer.getServer().getFile("");
			for (File lf : lfile.listFiles()) {
				if (lf.getName().endsWith("littleMaidMob.iff")) {
					String ls = lf.getName().substring(17, lf.getName().length() - 20);
					LittleMaidMobX.debug(ls);
					loadIFF(ls);
				}
			}
		} else {
			// クライアント側
			loadIFF(null);
		}
	}

	protected static File getFile(String pUsername) {
		File lfile;
		if (pUsername == null) {
			lfile = new File(MMM_Helper.mc.mcDataDir, "config/littleMaidMob.iff");
		} else {
			String lfilename;
			if (pUsername.isEmpty()) {
				lfilename = "config/littleMaidMob.iff";
			} else {
				lfilename = "config/littleMaidMob_".concat(pUsername).concat(".iff");
			}
			lfile = MinecraftServer.getServer().getFile(lfilename);
		}
		LittleMaidMobX.debug(lfile.getAbsolutePath());
		return lfile;
	}

	public static void loadIFF(String pUsername) {
		// IFF ファイルの読込み
		// 動作はサーバー側で想定
		File lfile = getFile(pUsername);
		if (!(lfile.exists() && lfile.canRead())) {
			return;
		}
		Map<String, Integer> lmap = getUserIFF(pUsername);
		
		try {
			FileReader fr = new FileReader(lfile);
			BufferedReader br = new BufferedReader(fr);
			
			String s;
			while ((s = br.readLine()) != null) {
				String t[] = s.split("=");
				if (t.length > 1) {
					if (t[0].startsWith("triggerWeapon")) {
						TriggerSelect.appendTriggerItem(pUsername, t[0].substring(13), t[1]);
						continue;
					}
					int i = Integer.valueOf(t[1]);
					if (i > 2) {
						i = iff_Unknown;
					}
					lmap.put(t[0], i);
				}
			}
			
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveIFF(String pUsername) {
		// IFF ファイルの書込み
		File lfile = getFile(LittleMaidMobX.proxy.isSinglePlayer() ? null : pUsername);
		Map<String, Integer> lmap = getUserIFF(pUsername);
		
		try {
			if ((lfile.exists() || lfile.createNewFile()) && lfile.canWrite()) {
				FileWriter fw = new FileWriter(lfile);
				BufferedWriter bw = new BufferedWriter(fw);
				
				// トリガーアイテムのリスト
				for (Entry<Integer, List<Item>> le : TriggerSelect.getUserTrigger(pUsername).entrySet())
				{
					StringBuilder sb = new StringBuilder();
					sb.append("triggerWeapon")
							.append(TriggerSelect.selector.get(le.getKey()))
							.append("=");
					if (!le.getValue().isEmpty()) {
						String itemName = Item.itemRegistry.getNameForObject(le.getValue().get(0));
						sb.append(itemName);
						for (int i = 1; i < le.getValue().size(); i++) {
							itemName = Item.itemRegistry.getNameForObject(le.getValue().get(i));
							sb.append(",").append(itemName);
						}
					}
					sb.append("\r\n");
					bw.write(sb.toString());
				}
				
				for (Map.Entry<String, Integer> me : lmap.entrySet()) {
					bw.write(String.format("%s=%d\r\n", me.getKey(),
							me.getValue()));
				}
				
				bw.close();
				fw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
