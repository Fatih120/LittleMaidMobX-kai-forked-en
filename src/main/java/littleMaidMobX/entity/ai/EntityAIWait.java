package littleMaidMobX.entity.ai;

import littleMaidMobX.entity.EntityLittleMaid;
import net.minecraft.entity.ai.EntityAISit;

public class EntityAIWait extends EntityAISit {

	public EntityLittleMaid theMaid;

	public EntityAIWait(EntityLittleMaid pEntity) {
		super(pEntity);
		this.setMutexBits(5);

		theMaid = pEntity;
	}

	@Override
	public boolean shouldExecute() {
		return theMaid.isMaidWaitEx() || (!theMaid.isFreedom() && theMaid.mstatMasterEntity == null);
	}

}
