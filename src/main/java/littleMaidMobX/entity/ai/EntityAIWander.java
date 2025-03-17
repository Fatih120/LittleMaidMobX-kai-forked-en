package littleMaidMobX.entity.ai;

import net.minecraft.entity.EntityCreature;

public class EntityAIWander extends net.minecraft.entity.ai.EntityAIWander implements IEntityAI {

	protected boolean isEnable;

	public EntityAIWander(EntityCreature par1EntityCreature, float par2) {
		super(par1EntityCreature, par2);

		isEnable = false;
	}

	@Override
	public boolean shouldExecute() {
		return isEnable && super.shouldExecute();
	}

	@Override
	public void setEnable(boolean flag) {
		isEnable = flag;
	}

	@Override
	public boolean getEnable() {
		return isEnable;
	}

}
