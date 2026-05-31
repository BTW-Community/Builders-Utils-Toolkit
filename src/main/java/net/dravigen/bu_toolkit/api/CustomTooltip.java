package net.dravigen.bu_toolkit.api;

import net.minecraft.src.ItemStack;
import net.minecraft.src.Minecraft;

import java.util.Map;


public interface CustomTooltip {
	default Map<String, String[]> getTooltip() {
		return null;
	}
	
	default void displayOnScreen(ItemStack stack, Minecraft mc) {}
}
