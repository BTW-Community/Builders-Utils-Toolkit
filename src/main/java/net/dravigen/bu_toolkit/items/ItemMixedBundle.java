package net.dravigen.bu_toolkit.items;

import btw.BTWMod;
import net.dravigen.bu_toolkit.inventory.ContainerMixedBundle;
import net.dravigen.bu_toolkit.inventory.MixedBundleInventory;
import net.minecraft.src.*;

public class ItemMixedBundle extends Item {
	public ItemMixedBundle(int par1) {
		super(par1);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (player.isUsingSpecialKey()) {
			if (!world.isRemote) {
				MixedBundleInventory inv = new MixedBundleInventory(stack);
				ContainerMixedBundle mixedBundle = new ContainerMixedBundle(player.inventory, inv);
				BTWMod.serverOpenCustomInterface((EntityPlayerMP) player, mixedBundle, mixedBundle.id);
			}
		}
		else {
		
		}
		
		return super.onItemRightClick(stack, world, player);
	}
}
