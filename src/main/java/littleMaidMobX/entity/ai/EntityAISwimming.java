package littleMaidMobX.entity.ai;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;

public class EntityAISwimming extends net.minecraft.entity.ai.EntityAISwimming {

	protected EntityLiving theEntity;
	
	public EntityAISwimming(EntityLiving par1EntityLiving) {
		super(par1EntityLiving);
		theEntity = par1EntityLiving;
	}

	@Override
	public boolean shouldExecute() {
		// 足がつくなら泳がない
		return (theEntity.getNavigator().noPath() ?
				(!theEntity.onGround || theEntity.isInsideOfMaterial(Material.water)) : theEntity.isInWater())
				|| theEntity.handleLavaMovement();
	}

}
