package net.dravigen.bu_toolkit.mixin.client;

import net.dravigen.bu_toolkit.items.BuildingWand;
import net.dravigen.bu_toolkit.network.PacketHandlerUtils;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Mixin(PlayerControllerMP.class)
public abstract class PlayerControllerMPMx {
	@Shadow
	@Final
	private Minecraft mc;
	
	@Unique
	private long cooldown = 0;
	
	@Inject(method = "clickBlock", at = @At("HEAD"))
	private void wandSelection(int x, int y, int z, int par4, CallbackInfo ci) throws IOException {
		EntityClientPlayerMP player = this.mc.thePlayer;
		ItemStack stack = player.getHeldItem();
		
		if (stack == null) return;
		
		if (this.cooldown < System.currentTimeMillis() && stack.getItem() instanceof BuildingWand && player.isSneaking()) {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			DataOutputStream dataStream = new DataOutputStream(byteStream);
			dataStream.writeInt(x);
			dataStream.writeInt(y);
			dataStream.writeInt(z);
			
			Minecraft.getMinecraft().getNetHandler().addToSendQueue(new Packet250CustomPayload(PacketHandlerUtils.WAND_SELECTION_CHANNEL, byteStream.toByteArray()));
			this.cooldown = System.currentTimeMillis() + 250;
		}
	}
}
