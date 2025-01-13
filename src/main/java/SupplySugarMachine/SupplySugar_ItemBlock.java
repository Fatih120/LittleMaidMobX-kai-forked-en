package SupplySugarMachine;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

public class SupplySugar_ItemBlock extends ItemBlock {
	public SupplySugar_ItemBlock(Block p_i45328_1_) {
		super(p_i45328_1_);
		maxStackSize = 64;
		// TODO 自動生成されたコンストラクター・スタブ
	}

    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4)
    {
    	NBTTagCompound nbt =  itemstack.getTagCompound();
		//player.addChatMessage(new ChatComponentText("addInformation: "+nbt));
    	if (itemstack.hasTagCompound()) {
    		long count = nbt.getLong("sugar");
    		//list.add("砂糖の数: "+count);
			list.add(StatCollector.translateToLocalFormatted("tile.SupplySugarMachineBlock.supply_sugar_remaining", count));
    	}
    }
}
