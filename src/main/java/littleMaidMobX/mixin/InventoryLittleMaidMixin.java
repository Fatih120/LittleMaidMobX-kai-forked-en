package littleMaidMobX.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import littleMaidMobX.entity.EntityLittleMaid;
import littleMaidMobX.inventory.InventoryLittleMaid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xonin.backhand.api.core.IOffhandInventory;
import xonin.backhand.utils.BackhandConfig;

import java.util.ArrayList;
import java.util.List;

@Pseudo
@Mixin(InventoryLittleMaid.class)
public class InventoryLittleMaidMixin extends InventoryPlayer implements IOffhandInventory {

    //@Shadow static int maxInventorySize = 21;
    @Shadow(remap = false)
    public EntityLittleMaid entityLittleMaid;

    @Shadow(remap = false)
    public int maxInventorySize;
    @Shadow(remap = false)
    public ItemStack[] prevItems;


    @Unique
    private int lmmx$backhandSlot;

    @Unique
    private List<ItemStack> lmmx$bg2Stacks = new ArrayList<>();

    public InventoryLittleMaidMixin(EntityPlayer p_i1750_1_) {
        super(p_i1750_1_);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(EntityLittleMaid par1EntityLittleMaid, CallbackInfo ci) {
        this.lmmx$backhandSlot = mainInventory.length;
        mainInventory = new ItemStack[mainInventory.length + 1];
        maxInventorySize = mainInventory.length;
        prevItems = new ItemStack[getSizeInventory()];
    }

    @Inject(
            method = "readFromNBT",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/entity/player/InventoryPlayer;mainInventory:[Lnet/minecraft/item/ItemStack;",
                    ordinal = 0))
    private void lmmx$importBG2Items(NBTTagList p_70443_1_, CallbackInfo ci, @Local ItemStack stack,
                                         @Local(name = "j") int index) {
        if (index >= 150 && index < 168) {
            lmmx$bg2Stacks.add(stack);
        }
    }

    @Inject(method = "readFromNBT", at = @At(value = "TAIL"))
    private void lmmx$giveBG2Items(NBTTagList p_70443_1_, CallbackInfo ci) {
        for (ItemStack stack : lmmx$bg2Stacks) {
            if (!addItemStackToInventory(stack)) {
                entityLittleMaid.entityDropItem(stack, 0.0F);
            }
        }

        lmmx$bg2Stacks = null;
    }

    @Inject(method = "readFromNBT", at = @At(value = "FIELD",
            target = "Lnet/minecraft/entity/player/InventoryPlayer;mainInventory:[Lnet/minecraft/item/ItemStack;",
            opcode = Opcodes.PUTFIELD,
            shift = At.Shift.AFTER))
    private void onReadFromNBT(NBTTagList par1nbtTagList, CallbackInfo ci) {
        this.lmmx$backhandSlot = mainInventory.length;
        mainInventory = new ItemStack[mainInventory.length + 1];
        maxInventorySize = mainInventory.length;
    }

    @ModifyReturnValue(method = "getCurrentItem", at = @At("RETURN"))
    private ItemStack lmmx$getOffhandItem(ItemStack original) {
        if (currentItem == backhand$getOffhandSlot()) {
            return backhand$getOffhandItem();
        }
        return original;
    }

    @ModifyReturnValue(method = "getFirstEmptyStack", at = @At("RETURN"))
    private int lmmx$checkOffhandPickup(int original) {
        if (!BackhandConfig.OffhandPickup && original == backhand$getOffhandSlot()) {
            return -1;
        }
        return original;
    }

    @Override
    public ItemStack backhand$getOffhandItem() {
        return this.mainInventory[backhand$getOffhandSlot()];
    }

    @Override
    public void backhand$setOffhandItem(ItemStack stack) {
        this.mainInventory[backhand$getOffhandSlot()] = stack;
    }

    @Override
    public int backhand$getOffhandSlot() {
        return this.lmmx$backhandSlot;
    }
}
