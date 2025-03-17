package littleMaidMobX.mixin;

import littleMaidMobX.entity.EntityLittleMaid;
import littleMaidMobX.inventory.InventoryLittleMaid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xonin.backhand.api.core.IOffhandInventory;

@Pseudo
@Mixin(InventoryLittleMaid.class)
public class InventoryLittleMaidMixin extends InventoryPlayer implements IOffhandInventory {

    @Unique
    private int lmmx$backhandSlot;

    public InventoryLittleMaidMixin(EntityPlayer p_i1750_1_) {
        super(p_i1750_1_);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(EntityLittleMaid par1EntityLittleMaid, CallbackInfo ci) {
        this.lmmx$backhandSlot = 1;
    }

    @Inject(method = "readFromNBT", at = @At(value = "FIELD",
            target = "Lnet/minecraft/entity/player/InventoryPlayer;mainInventory:[Lnet/minecraft/item/ItemStack;",
            opcode = Opcodes.PUTFIELD,
            shift = At.Shift.AFTER))
    private void onReadFromNBT(NBTTagList par1nbtTagList, CallbackInfo ci) {
        this.lmmx$backhandSlot = 1;
    }


    @Override
    public ItemStack backhand$getOffhandItem() {
        return this.mainInventory[this.lmmx$backhandSlot];
    }

    @Override
    public void backhand$setOffhandItem(ItemStack stack) {
        this.mainInventory[this.lmmx$backhandSlot] = stack;
    }

    @Override
    public int backhand$getOffhandSlot() {
        return this.lmmx$backhandSlot;
    }
}
