package net.dravigen.bu_toolkit.mixin.server;

import net.dravigen.bu_toolkit.api.CustomItemBehavior;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NetServerHandler.class)
public abstract class NetServerHandlerMx extends NetHandler {
	
	@Redirect(method = "handleBlockDig", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;isBlockProtected(Lnet/minecraft/src/World;IIILnet/minecraft/src/EntityPlayer;)Z"))
	private boolean preventBreak(MinecraftServer server, World world, int x, int y, int z,
			EntityPlayer player) {
		
		ItemStack stack = player.getHeldItem();
		
		if (!server.isBlockProtected(world, x, y, z, player) && stack != null && stack.getItem() instanceof CustomItemBehavior customStack) {
			return customStack.shouldBreakBlock(player, stack);
		}
		
		return server.isBlockProtected(world, x, y, z, player);
	}
}
