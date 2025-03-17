package littleMaidMobX.entity;

import net.minecraft.util.DamageSource;

public interface IEntityLittleMaidAvatarBase
{
	// --------------------------------------------------------------------------------------------------------
	// EntityPlayer へのアクセス
	// --------------------------------------------------------------------------------------------------------
    float W_applyArmorCalculations(DamageSource par1DamageSource, float par2);
	float W_applyPotionDamageCalculations(DamageSource par1DamageSource, float par2);
	void W_damageArmor(float pDamage);
	void W_damageEntity(DamageSource par1DamageSource, float par2);


	// --------------------------------------------------------------------------------------------------------
	// LMM 専用処理
	// --------------------------------------------------------------------------------------------------------
    void setValueVector();

	void getValueVectorFire(double atx, double aty, double atz, double atl);

	boolean getIsItemTrigger();

	boolean isUsingItemLittleMaid();

	void getValue();

	boolean getIsItemReload();

	EntityLittleMaid getMaid();
}
