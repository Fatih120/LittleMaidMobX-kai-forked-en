package littleMaidMobX.mixin;

import littleMaidMobX.entity.EntityLittleMaid;
import littleMaidMobX.inventory.ContainerInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xonin.backhand.api.core.IOffhandInventory;

@Mixin(ContainerInventory.class)
public class ContainerInventoryMixin extends ContainerPlayer {
    public ContainerInventoryMixin(InventoryPlayer p_i1819_1_, boolean p_i1819_2_, EntityPlayer p_i1819_3_) {
        super(p_i1819_1_, p_i1819_2_, p_i1819_3_);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void onInit(IInventory iinventory, EntityLittleMaid pEntity, CallbackInfo ci){
        addSlotToContainer(new Slot(pEntity.maidInventory, ((IOffhandInventory)pEntity.maidInventory).backhand$getOffhandSlot(), 8 + 90, 8 + 36));
    }
}
