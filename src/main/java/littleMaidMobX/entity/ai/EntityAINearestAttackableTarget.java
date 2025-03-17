package littleMaidMobX.entity.ai;

import littleMaidMobX.entity.modes.EntityModeBase;
import littleMaidMobX.entity.EntityLittleMaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;

import java.util.List;

public class EntityAINearestAttackableTarget extends net.minecraft.entity.ai.EntityAINearestAttackableTarget {

	protected EntityLittleMaid theMaid;
	protected Entity targetEntity;
	protected Class<?> targetClass;
	protected int targetChance;
	protected EntityAINearestAttackableTargetSorter theNearestAttackableTargetSorter;

	private boolean fretarget;
	private int fcanAttack;
	private int fretryCounter;

	public EntityAINearestAttackableTarget(EntityLittleMaid littleMaid, Class<?> clazz, int targetChance, boolean checkSight) {
		this(littleMaid, clazz, targetChance, checkSight, false);
	}

	public EntityAINearestAttackableTarget(EntityLittleMaid littleMaid, Class<?> clazz, int targetChance, boolean checkSight, boolean nearbyOnly) {
		super(littleMaid, clazz, targetChance, checkSight, nearbyOnly, null);
		targetClass = clazz;
		this.targetChance = targetChance;
		theNearestAttackableTargetSorter = new EntityAINearestAttackableTargetSorter(littleMaid);
		fretarget = nearbyOnly;
		theMaid = littleMaid;

		setMutexBits(1);
	}


	@Override
	public boolean shouldExecute() {
		boolean ret = false;

		if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
//			return false;
//		} else if (theMaid.getAttackTarget() != null) {
//			return true;
		} else {
			double lfollowRange = this.getTargetDistance();
			List entityList = this.taskOwner.worldObj.getEntitiesWithinAABB(targetClass, taskOwner.boundingBox.expand(lfollowRange, 8.0D, lfollowRange));
			if (theMaid.mstatMasterEntity != null && !theMaid.isBloodsuck()) {
				// ソーターを主中心へ
				theNearestAttackableTargetSorter.setEntity(theMaid.mstatMasterEntity);
			} else {
				// 自分中心にソート
				theNearestAttackableTargetSorter.setEntity(theMaid);
			}
			entityList.sort(theNearestAttackableTargetSorter);
            for (Object o : entityList) {
                Entity var3 = (Entity) o;
                if (var3.isEntityAlive() && this.isSuitableTargetLM(var3, false)) {
                    this.targetEntity = var3;
                    ret = true;
                }
            }
		}

		// 主に対する攻撃を行ったモブを最優先で狙う
		// http://forum.minecraftuser.jp/viewtopic.php?t=23347&start=460#p234230
		if (theMaid.isContract() && theMaid.mstatMasterEntity != null) {
			EntityLivingBase lentity = theMaid.mstatMasterEntity.getAITarget();
			if (this.isSuitableTargetLM(lentity, false)) {
				theMaid.setRevengeTarget(lentity);
				ret = true;
			}
		}

		return ret;
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		if (targetEntity instanceof EntityLivingBase) {
			theMaid.setAttackTarget((EntityLivingBase)targetEntity);
		} else {
			theMaid.setTarget(targetEntity);
		}
		fcanAttack = 0;
		fretryCounter = 0;
	}

//	@Override
	protected boolean isSuitableTargetLM(Entity pTarget, boolean par2) {
		// LMM用にカスタム
		// 非生物も対象のため別クラス
		if (pTarget == null) {
			return false;
		}

		if (pTarget == taskOwner) {
			return false;
		}
		/*if (pTarget == theMaid.mstatMasterEntity) {
			return false;
		}*/

		if (!pTarget.isEntityAlive()) {
			return false;
		}

		EntityModeBase lailm = theMaid.getActiveModeClass();
		if (lailm != null && lailm.isSearchEntity()) {
			if (!lailm.checkEntity(theMaid.getMaidModeInt(), pTarget)) {
				return false;
			}
		} else {
			if (theMaid.getIFF(pTarget)) {
				return false;
			}
		}
/*
		// 基点から一定距離離れている場合も攻撃しない
		if (!taskOwner.func_110176_b(MathHelper.floor_double(pTarget.posX), MathHelper.floor_double(pTarget.posY), MathHelper.floor_double(pTarget.posZ))) {
//		if (!taskOwner.isWithinHomeDistance(MathHelper.floor_double(par1EntityLiving.posX), MathHelper.floor_double(par1EntityLiving.posY), MathHelper.floor_double(par1EntityLiving.posZ))) {
			return false;
		}
*/
		// ターゲットが見えない
		if (shouldCheckSight && !taskOwner.getEntitySenses().canSee(pTarget)) {
			return false;
		}

		// 攻撃中止判定？
		if (this.fretarget) {
			if (--this.fretryCounter <= 0) {
				this.fcanAttack = 0;
			}

			if (this.fcanAttack == 0) {
				this.fcanAttack = this.canEasilyReach(pTarget) ? 1 : 2;
			}

			if (this.fcanAttack == 2) {
				return false;
			}
		}

		return true;
	}

	// 最終位置が攻撃の間合いでなければ失敗
	protected boolean canEasilyReach(Entity entity) {
		this.fretryCounter = 10 + this.taskOwner.getRNG().nextInt(5);
		PathEntity pathEntity = taskOwner.getNavigator().getPathToXYZ(entity.posX, entity.posY, entity.posZ);
//		PathEntity var2 = this.taskOwner.getNavigator().getPathToEntityLiving(par1EntityLiving);

		if (pathEntity == null) {
			return false;
		} else {
			PathPoint pathPoint = pathEntity.getFinalPathPoint();

			if (pathPoint == null) {
				return false;
			} else {
				int x = pathPoint.xCoord - MathHelper.floor_double(entity.posX);
				int z = pathPoint.zCoord - MathHelper.floor_double(entity.posZ);
				return (double)(x * x + z * z) <= 2.25;
			}
		}
	}


}
