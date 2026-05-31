package net.dravigen.bu_toolkit.api;

import net.minecraft.src.I18n;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

public interface CustomItemWithModes {
	String[] getModes();
	
	default int getMode(ItemStack stack) {
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("mode")) {
			return Math.min(stack.getTagCompound().getInteger("mode"), getModes().length - 1);
		}
		
		return 0;
	}
	
	default String getModeNameUnlocalized(ItemStack stack) {
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("mode")) {
			return getModes()[getMode(stack)];
		}
		
		return getModes()[0];
	}
	
	default String getModeName(ItemStack stack) {
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("mode")) {
			return I18n.getString(getModes()[getMode(stack)]);
		}
		
		return I18n.getString(getModes()[0]);
	}
	
	default void setMode(ItemStack stack, int mode) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		
		stack.getTagCompound().setInteger("mode", mode);
	}
	
	default void cycleMode(ItemStack stack) {
		int mode = getMode(stack) + 1;
		
		if (getModes().length <= mode) {
			mode = 0;
		}
		
		setMode(stack, mode);
	}
	
	default void displayModeChange(ItemStack stack) {
	
	}
}
