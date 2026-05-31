package net.dravigen.bu_toolkit.api;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Minecraft;
import net.minecraft.src.World;

public interface CustomItemBehavior {
	default boolean shouldBreakBlock(EntityPlayer player, ItemStack stack) {
		return true;
	}
	
	default void renderAtBlockTarget(ItemStack stack, EntityPlayer player, Minecraft mc, int i, int j, int k, World world) {}
	
	default void stopRenderAtBlockTarget(ItemStack stack, EntityPlayer player, Minecraft mc) {}
	
	default void renderSpecial(ItemStack stack, EntityPlayer player, Minecraft mc) {}
	
}
