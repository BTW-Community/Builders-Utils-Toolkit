package net.dravigen.bu_toolkit.api;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

import java.util.List;

public class InvUtils {
	public static void useStackInInv(EntityPlayer player, int iBlocksToUse, List<ItemStack> stackList) {
		if (!player.capabilities.isCreativeMode) {
			int iBlocksToRemove;
			
			for (Object o : player.inventoryContainer.inventorySlots) {
				Slot slot = (Slot) o;
				ItemStack stackInSlot = slot.getStack();
				iBlocksToRemove = iBlocksToUse;
				
				if (stackInSlot == null) continue;
				
				if (iBlocksToUse <= 0) break;
				
				for (ItemStack stack : stackList) {
					int id = stack.itemID;
					int meta = stack.getItemDamage();
					
					if (stackInSlot.itemID == id && stackInSlot.getItemDamage() == meta) {
						if (stackInSlot.stackSize < iBlocksToUse) {
							iBlocksToRemove = stackInSlot.stackSize;
						}
						
						slot.decrStackSize(iBlocksToRemove);
						iBlocksToUse -= iBlocksToRemove;
						
						break;
					}
				}
			}
			
			player.inventoryContainer.detectAndSendChanges();
		}
	}
	
	public static int getStacksInInv(EntityPlayer player, List<ItemStack> stackList) {
		int iBlocksAvailable = 0;
		
		for (Object o : player.inventoryContainer.inventorySlots) {
			Slot slot = (Slot) o;
			ItemStack stackInSlot = slot.getStack();
			
			if (stackInSlot == null) continue;
			
			for (ItemStack stack : stackList) {
				int id = stack.itemID;
				int meta = stack.getItemDamage();
				
				if (stackInSlot.itemID == id && stackInSlot.getItemDamage() == meta) {
					iBlocksAvailable += stackInSlot.stackSize;
					
					break;
				}
			}
		}
		
		return iBlocksAvailable;
	}
}
