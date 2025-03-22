package littleMaidMobX.mixin;

import littleMaidMobX.entity.EntityLittleMaid;
import littleMaidMobX.inventory.InventoryLittleMaid;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xonin.backhand.api.core.IOffhandInventory;

@Mixin(EntityLittleMaid.class)
public abstract class EntityLittleMaidMixin {

    @Shadow(remap = false)
    private InventoryLittleMaid maidInventory;
    @Shadow(remap = false)
    private int maidDominantArm;

    @Shadow(remap = false)
    public abstract void setEquipItem(int pArm, int pIndex);

    @Inject(method = "getNextEquipItem", at = @At("TAIL"), remap = false)
    private void onGetNextEquipItem(CallbackInfoReturnable<Boolean> ci){
        int arm = maidDominantArm == 0 ? 1 : 0;
        if (((IOffhandInventory)maidInventory).backhand$getOffhandItem() != null) {
            setEquipItem(arm, ((IOffhandInventory) maidInventory).backhand$getOffhandSlot());
        }else {
            setEquipItem(arm, -1);
        }
    }

}
