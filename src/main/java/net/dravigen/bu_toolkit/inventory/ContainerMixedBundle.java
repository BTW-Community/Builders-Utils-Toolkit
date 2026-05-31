package net.dravigen.bu_toolkit.inventory;

import btw.inventory.container.InventoryContainer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;

public class ContainerMixedBundle extends InventoryContainer {
	
	public int id = 532;
	
	public ContainerMixedBundle(IInventory playerInventory, IInventory containerInv) {
		super(playerInventory, containerInv, 1, 9, 62, 17, 8, 85);
		containerInv.openChest();
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return true;
	}
	
	
}
