package net.dravigen.bu_toolkit.inventory;

import api.inventory.InventoryUtils;
import api.item.items.PlaceAsBlockItem;
import net.dravigen.bu_toolkit.api.IItemStackInventory;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;

import java.util.ArrayList;

public class MixedBundleInventory implements IItemStackInventory {
	protected final int size = 9;
	public ItemStack[] inventory = new ItemStack[size];
	
	public MixedBundleInventory(ItemStack stack) {
		// Create tag compound if one doesn't exist
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		
		// Prepare inventory
		NBTTagCompound compound = stack.getTagCompound();
		
		// Read stack NBT
		readFromNBT(compound);
	}
	
	public String getRootTagString() {
		return "MixedBundleInventory";
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		// Get inventory tag
		NBTTagCompound content = compound.getCompoundTag(getRootTagString());
		if (content == null)
			return;
		
		// Begin parsing items list
		NBTTagList list = content.getTagList("ItemStacks");
		for (int i = 0; i < list.tagCount() && i < size; i++) {
			// Get ItemStack entry
			NBTTagCompound entry = (NBTTagCompound) list.tagAt(i);
			
			// Attempt to parse ItemStack data
			try {
				inventory[i] = ItemStack.loadItemStackFromNBT(entry);
			} catch (NullPointerException _npe) {
			}
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
// Iterate through inventory
		NBTTagList list = new NBTTagList();
		for (int slot = 0; slot < this.inventory.length; slot++) {
			ItemStack stack = this.inventory[slot];
			if (stack != null && stack.stackSize > 0) {
				// Write items data to NBT
				NBTTagCompound slotTag = new NBTTagCompound();
				list.appendTag(slotTag);
				slotTag.setInteger("Slot", slot);
				stack.writeToNBT(slotTag);
			}
		}
		
		NBTTagCompound backpack = new NBTTagCompound();
		backpack.setTag("ItemStacks", list);
		compound.setTag(getRootTagString(), backpack);
	
		return compound;
	}
	
	@Override
	public int getSizeInventory() {
		return size;
	}
	
	@Override
	public ItemStack getStackInSlot(int i) {
		return this.inventory[i];
	}
	
	@Override
	public ItemStack decrStackSize(int i, int j) {
		return InventoryUtils.decreaseStackSize(this, i, j);
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return null;
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack itemStack) {
		this.inventory[i] = itemStack;
	
		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}
		
		this.onInventoryChanged();
	}
	
	@Override
	public String getInvName() {
		return "";
	}
	
	@Override
	public boolean isInvNameLocalized() {
		return false;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public void onInventoryChanged() {
	
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
		return true;
	}
	
	@Override
	public void openChest() {
	
	}
	
	@Override
	public void closeChest() {
	
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemStack) {
		return itemStack.getItem() instanceof PlaceAsBlockItem;
	}
}
