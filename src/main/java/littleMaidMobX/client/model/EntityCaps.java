package littleMaidMobX.client.model;

import java.util.HashMap;
import java.util.Map;

import littleMaidMobX.entity.EntityLittleMaid;
import littleMaidMobX.entity.modes.SwingStatus;
import mmmlibx.lib.MMM_EntityCaps;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;


/**
 * Entityのデータ読み取り用のクラス
 * 別にEntityにインターフェース付けてもOK
 */
public class EntityCaps extends MMM_EntityCaps {

	private EntityLittleMaid owner;
	private static final Map<String, Integer> CAPS;

	static {
		CAPS = new HashMap<>();
		CAPS.putAll(getStaticModelCaps());
		CAPS.put("isBloodsuck", caps_isBloodsuck);
		CAPS.put("isFreedom", caps_isFreedom);
		CAPS.put("isTracer", caps_isTracer);
		CAPS.put("isPlaying", caps_isPlaying);
		CAPS.put("isLookSuger", caps_isLookSuger);
		CAPS.put("isBlocking", caps_isBlocking);
		CAPS.put("isWait", caps_isWait);
		CAPS.put("isWaitEX", caps_isWaitEX);
		CAPS.put("isOpenInv", caps_isOpenInv);
		CAPS.put("isWorking", caps_isWorking);
		CAPS.put("isWorkingDelay", caps_isWorkingDelay);
		CAPS.put("isContract", caps_isContract);
		CAPS.put("isContractEX", caps_isContractEX);
		CAPS.put("isRemainsC", caps_isRemainsC);
		CAPS.put("isClock", caps_isClock);
		CAPS.put("isMasked", caps_isMasked);
		CAPS.put("isCamouflage", caps_isCamouflage);
		CAPS.put("isPlanter", caps_isPlanter);
		CAPS.put("isOverdrive", caps_isOverdrive);
		CAPS.put("isOverdriveDelay", caps_isOverdriveDelay);
		CAPS.put("entityIdFactor", caps_entityIdFactor);
		CAPS.put("height", caps_height);
		CAPS.put("width", caps_width);
		CAPS.put("YOffset", caps_YOffset);
		CAPS.put("mountedYOffset", caps_mountedYOffset);
		CAPS.put("dominantArm", caps_dominantArm);
//		caps.put("render", caps_render);
//		caps.put("Arms", caps_Arms);
		CAPS.put("HeadMount", caps_HeadMount);
//		caps.put("HardPoint", caps_HardPoint);
		CAPS.put("stabiliser", caps_stabiliser);
		CAPS.put("Items", caps_Items);
		CAPS.put("Actions", caps_Actions);
		CAPS.put("Grounds", caps_Grounds);
		CAPS.put("Ground", caps_Ground);
		CAPS.put("Inventory", caps_Inventory);
		CAPS.put("interestedAngle", caps_interestedAngle);
//		caps.put("Entity", caps_Entity);
//		caps.put("health", caps_health);
		CAPS.put("currentArmor", caps_currentArmor);
		CAPS.put("currentEquippedItem", caps_currentEquippedItem);
	}

	public EntityCaps(EntityLittleMaid pOwner) {
		super(pOwner);
		owner = pOwner;
	}

	@Override
	public Map<String, Integer> getModelCaps() {
		return CAPS;
	}

	@Override
	public Object getCapsValue(int pIndex, Object ...pArg) {
		int li = 0;
		
		switch (pIndex) {
//		case caps_Entity:
//			return owner;
//		case caps_health:
//			return (int)owner.getHealth();
//		case caps_healthFloat:
//			return owner.getHealth();
		case caps_isBloodsuck:
			return owner.isBloodsuck();
		case caps_isFreedom:
			return owner.isFreedom();
		case caps_isTracer:
			return owner.isTracer();
		case caps_isPlaying:
			return owner.isPlaying();
		case caps_isLookSuger:
			return owner.isLookSuger();
		case caps_isBlocking:
			return owner.isBlocking();
		case caps_isWait:
			return owner.isMaidWait();
		case caps_isWaitEX:
			return owner.isMaidWaitEx();
		case caps_isOpenInv:
			return owner.isOpenInventory();
		case caps_isWorking:
			return owner.isWorking();
		case caps_isWorkingDelay:
			return owner.isWorkingDelay();
		case caps_isContract:
			return owner.isContract();
		case caps_isContractEX:
			return owner.isContractEX();
		case caps_isRemainsC:
			return owner.isRemainsContract();
		case caps_isClock:
			return owner.isClockMaid();
		case caps_isMasked:
			return owner.isMaskedMaid();
		case caps_isCamouflage:
			return owner.isCamouflage();
		case caps_isPlanter:
			return owner.isPlanter();
		case caps_isOverdrive:
			return owner.maidOverDriveTime.isEnable();
		case caps_isOverdriveDelay:
			return owner.maidOverDriveTime.isDelay();
		case caps_entityIdFactor:
			return owner.entityIdFactor;
		case caps_height:
			return owner.textureData.textureBox[0] == null ? null : owner.textureData.textureBox[0].getHeight(this);
		case caps_width:
			return owner.textureData.textureBox[0] == null ? null : owner.textureData.textureBox[0].getWidth(this);
		case caps_YOffset:
			return owner.textureData.textureBox[0] == null ? null : owner.textureData.textureBox[0].getYOffset(this);
		case caps_mountedYOffset:
			return owner.textureData.textureBox[0] == null ? null : owner.textureData.textureBox[0].getMountedYOffset(this);
		case caps_dominantArm:
			return owner.maidDominantArm;
//		case caps_mountedYOffset:
//			return owner.textureModel0 == null ? null : owner.textureModel0.getHeight();
//		case caps_render:
//		case caps_Arms:
		case caps_HeadMount:
			return owner.maidInventory.getStackInSlot(17);
//		case caps_HardPoint:
		case caps_stabiliser:
			return owner.maidStabilizer;
		case caps_Items:
			ItemStack[] lstacks = new ItemStack[owner.mstatSwingStatus.length];
			for (SwingStatus ls : owner.mstatSwingStatus) {
				lstacks[li++] = ls.getItemStack(owner);
			}
			return lstacks;
		case caps_Actions:
			EnumAction[] lactions = new EnumAction[owner.mstatSwingStatus.length];
			for (SwingStatus ls : owner.mstatSwingStatus) {
				lactions[li++] = ls.isUsingItem() ? ls.getItemStack(owner).getItemUseAction() : null;
			}
			return lactions;
		case caps_Grounds:
			float[] lgrounds = new float[owner.mstatSwingStatus.length];
			for (SwingStatus ls : owner.mstatSwingStatus) {
				lgrounds[li++] = ls.onGround;
			}
			return lgrounds;
		case caps_Ground:
			// float (int pIndex, int pDefVal)
			if (owner.mstatSwingStatus.length < (Integer)pArg[0]) {
				return pArg[1];
			}
			return owner.mstatSwingStatus[(Integer)pArg[0]].onGround;
		case caps_Inventory:
			return owner.maidInventory;
		case caps_interestedAngle:
			return owner.getInterestedAngle((Float)pArg[0]);
//		case caps_currentArmor:
//			return owner.getCurrentItemOrArmor((Integer)pArg[0] + 1);
//		case caps_currentEquippedItem:
//			return owner.getCurrentEquippedItem();
		case caps_PartsVisible:
			return owner.textureData.selectValue;
		case caps_textureData:
			return owner.textureData;
		}
		
		return super.getCapsValue(pIndex, pArg);
	}

	@Override
	public boolean setCapsValue(int index, Object... args) {
        if (index == caps_PartsVisible) {
            owner.textureData.selectValue = (Integer) args[0];
        }
		return super.setCapsValue(index, args);
	}

}
