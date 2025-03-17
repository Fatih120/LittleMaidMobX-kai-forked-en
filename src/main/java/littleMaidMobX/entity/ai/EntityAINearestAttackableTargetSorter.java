package littleMaidMobX.entity.ai;

import java.util.Comparator;

import net.minecraft.entity.Entity;

public class EntityAINearestAttackableTargetSorter implements Comparator<Entity> {

	private Entity entity;

	public EntityAINearestAttackableTargetSorter(Entity par1Entity) {
		this.entity = par1Entity;
	}

	public int compareDistanceSq(Entity e1, Entity e2) {
		double eDist1 = this.entity.getDistanceSqToEntity(e1);
		double eDist2 = this.entity.getDistanceSqToEntity(e2);
		return Double.compare(eDist1, eDist2);
	}

	public int compare(Entity o1, Entity o2) {
		return this.compareDistanceSq(o1, o2);
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

}
