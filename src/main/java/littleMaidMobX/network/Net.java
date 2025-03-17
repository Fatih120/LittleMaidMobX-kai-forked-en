package littleMaidMobX.network;

import static littleMaidMobX.util.Statics.LMN_Client_SetIFFValue;
import static littleMaidMobX.util.Statics.LMN_Server_DecDyePowder;
import static littleMaidMobX.util.Statics.LMN_Server_GetIFFValue;
import static littleMaidMobX.util.Statics.LMN_Server_SaveIFF;
import static littleMaidMobX.util.Statics.LMN_Server_SetIFFValue;
import static littleMaidMobX.util.Statics.LMN_Server_UpdateSlots;

import littleMaidMobX.entity.EntityLittleMaid;
import littleMaidMobX.entity.modes.IFF;
import littleMaidMobX.entity.modes.SwingStatus;
import littleMaidMobX.LittleMaidMobX;
import mmmlibx.lib.MMM_Helper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class Net {
	
	/**
	 * 渡されたデータの先頭に自分のEntityIDを付与して全てのクライアントへ送信
	 */
	public static void sendToAllEClient(EntityLittleMaid pEntity, byte[] pData)
	{
		MMM_Helper.setInt(pData, 1, pEntity.getEntityId());
		
		EntityTracker et = ((WorldServer)pEntity.worldObj).getEntityTracker();
		for(EntityPlayer player : et.getTrackingPlayers(pEntity))
		{
			Network.sendPacketToPlayer(2, player, pData);
		}
//		((WorldServer)pEntity.worldObj).getEntityTracker().func_151248_b(pEntity, new Packet250CustomPayload("LMM|Upd", pData));
	}

	/**
	 * 渡されたデータの先頭に自分のEntityIDを付与して特定ののクライアントへ送信
	 *
	public static void sendToEClient(EntityPlayer player, LMM_EntityLittleMaid pEntity, byte[] pData) {
		MMM_Helper.setInt(pData, 1, pEntity.getEntityId());
		W_Network.sendPacketToPlayer(player, pData);
//		ModLoader.serverSendPacket(pHandler, new Packet250CustomPayload("LMM|Upd", pData));
	}
	*/

	public static void sendToClient(EntityPlayer player, byte[] pData) {
		Network.sendPacketToPlayer(2, player, pData);
//		ModLoader.serverSendPacket(pHandler, new Packet250CustomPayload("LMM|Upd", pData));
	}

	/**
	 * 渡されたデータの先頭にEntityIDを付与してサーバーへ送信。
	 * 0:Mode, 1-4:EntityID, 5-:Data
	 */
	public static void sendToEServer(EntityLittleMaid pEntity, byte[] pData) {
		MMM_Helper.setInt(pData, 1, pEntity.getEntityId());
		Network.sendPacketToServer(2,pData);
//		ModLoader.clientSendPacket(new Packet250CustomPayload("LMM|Upd", pData));
		LittleMaidMobX.debug(String.format("LMM|Upd:send:%2x:%d", pData[0], pEntity.getEntityId()));
	}

	public static void sendToServer(byte[] pData) {
		Network.sendPacketToServer(2, pData);
//		ModLoader.clientSendPacket(new Packet250CustomPayload("LMM|Upd", pData));
		LittleMaidMobX.debug(String.format("LMM|Upd:%2x:NOEntity", pData[0]));
	}

	/**
	 * サーバーへIFFのセーブをリクエスト
	 */
	public static void saveIFF() {
		sendToServer(new byte[] {LMN_Server_SaveIFF});
	}

	/**
	 * littleMaidのEntityを返す。
	 */
	public static EntityLittleMaid getLittleMaid(byte[] pData, int pIndex, World pWorld)
	{
		Entity lentity = MMM_Helper.getEntity(pData, pIndex, pWorld);
		if (lentity instanceof EntityLittleMaid)
		{
			return (EntityLittleMaid)lentity;
		}
		else
		{
			return null;
		}
	}

	// 受信パケットの処理
	
	public static void serverCustomPayload(EntityPlayer playerEntity, Message pPayload)
	{
		// サーバ側の動作
		byte modeData = pPayload.data[0];
		int leid = 0;
		EntityLittleMaid lemaid = null;
		if ((modeData & 0x80) != 0) {
			leid = MMM_Helper.getInt(pPayload.data, 1);
			lemaid = getLittleMaid(pPayload.data, 1, playerEntity.worldObj);
			if (lemaid == null) return;
		}
		LittleMaidMobX.debug(String.format("LMM|Upd Srv Call[%2x:%d].", modeData, leid));
		byte[] ldata;
		int lindex;
		int lval;
		String lname;
		
		switch (modeData) {
		case LMN_Server_UpdateSlots : 
			// 初回更新とか
			// インベントリの更新
			lemaid.maidInventory.clearChanged();
			for (SwingStatus lswing : lemaid.mstatSwingStatus) {
				lswing.lastIndex = -1;
			}
			break;
			
		case LMN_Server_DecDyePowder:
			// カラー番号をクライアントから受け取る
			// インベントリから染料を減らす。
			int lcolor2 = pPayload.data[1];
			if (!playerEntity.capabilities.isCreativeMode) {
				for (int li = 0; li < playerEntity.inventory.mainInventory.length; li++) {
					ItemStack lis = playerEntity.inventory.mainInventory[li];
					if (lis != null && lis.getItem() == Items.dye) {
						if (lis.getItemDamage() == (15 - lcolor2)) {
							MMM_Helper.decPlayerInventory(playerEntity, li, 1);
						}
					}
				}
			}
			break;
			
		case LMN_Server_SetIFFValue:
			// IFFの設定値を受信
			lval = pPayload.data[1];
			lindex = MMM_Helper.getInt(pPayload.data, 2);
			lname = MMM_Helper.getStr(pPayload.data, 6);
			LittleMaidMobX.debug("setIFF-SV user:%s %s(%d)=%d", MMM_Helper.getPlayerName(playerEntity), lname, lindex, lval);
			IFF.setIFFValue(MMM_Helper.getPlayerName(playerEntity), lname, lval);
			sendIFFValue(playerEntity, lval, lindex);
			break;
		case LMN_Server_GetIFFValue:
			// IFFGUI open
			lindex = MMM_Helper.getInt(pPayload.data, 1);
			lname = MMM_Helper.getStr(pPayload.data, 5);
			lval = IFF.getIFF(MMM_Helper.getPlayerName(playerEntity), lname, playerEntity.worldObj);
			LittleMaidMobX.debug("getIFF-SV user:%s %s(%d)=%d", MMM_Helper.getPlayerName(playerEntity), lname, lindex, lval);
			sendIFFValue(playerEntity, lval, lindex);
			break;
		case LMN_Server_SaveIFF:
			// IFFファイルの保存
			IFF.saveIFF(MMM_Helper.getPlayerName(playerEntity));
			if (!playerEntity.worldObj.isRemote) {
				IFF.saveIFF("");
			}
			break;
			
		}
	}

	/**
	 * クライアントへIFFの設定値を通知する。
	 * @param pValue
	 * @param pIndex
	 */
	protected static void sendIFFValue(EntityPlayer player, int pValue, int pIndex) {
		byte[] ldata = new byte[] {
				LMN_Client_SetIFFValue,
				0,
				0, 0, 0, 0
		};
		ldata[1] = (byte)pValue;
		MMM_Helper.setInt(ldata, 2, pIndex);
		sendToClient(player, ldata);
	}
}
