package littleMaidMobX.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIRestrictRain extends EntityAIBase implements IEntityAI {

    protected EntityLiving theEntity;
    protected boolean isEnable;

    public EntityAIRestrictRain(EntityLiving par1EntityLiving) {
        theEntity = par1EntityLiving;
        isEnable = false;
    }

    @Override
    public boolean shouldExecute() {
        return isEnable && theEntity.worldObj.isRaining();
    }

    @Override
    public void startExecuting() {
        theEntity.getNavigator().setAvoidSun(true);
    }

    @Override
    public void resetTask() {
        theEntity.getNavigator().setAvoidSun(false);
    }

    // 実行可能フラグ
    @Override
    public void setEnable(boolean flag) {
        isEnable = flag;
    }

    @Override
    public boolean getEnable() {
        return isEnable;
    }

}
