package net.dravigen.bu_toolkit.mixin.client;

import net.dravigen.bu_toolkit.api.CustomItemWithModes;
import net.dravigen.bu_toolkit.api.CustomTooltip;
import net.dravigen.bu_toolkit.items.Trowel;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public abstract class GuiIngameMx extends Gui {
	@Shadow
	@Final
	private Minecraft mc;
	
	@Shadow
	private int remainingHighlightTicks;
	
	@Shadow
	private ItemStack highlightingItemStack;
	
	@Inject(method = "renderGameOverlay", at = @At("TAIL"))
	private void renderExtraHighlight(float par1, boolean par2, int par3, int par4, CallbackInfo ci) {
		
		ItemStack stack = mc.thePlayer.getHeldItem();
		
		if (stack == null || stack.getTagCompound() == null) return;
		
		if (stack.getItem() instanceof CustomTooltip customTooltip) {
			customTooltip.displayOnScreen(stack, this.mc);
		}
	
		if (stack.getItem() instanceof CustomItemWithModes modeItem) {
			ScaledResolution var5 = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
			int var6 = var5.getScaledWidth();
			int var7 = var5.getScaledHeight();
			FontRenderer var8 = mc.fontRenderer;
			int brightness = 255;
			
			int remainingTick = stack.getTagCompound().getInteger("modeChange");
			
			//if (remainingTick > 0) {
				String text = modeItem.getModeName(stack);
				int x = (var6 - var8.getStringWidth(text)) / 2;
				int y = var7 - 64 - var8.FONT_HEIGHT;
				
				if (!this.mc.playerController.shouldDrawHUD()) {
					y += 14;
				}
				
				/*if ((brightness = (int) ((float) remainingTick * 256.0f / 10.0f)) > 255) {
					brightness = 255;
				}*/
				if (brightness > 0) {
					GL11.glPushMatrix();
					GL11.glEnable(3042);
					GL11.glBlendFunc(770, 771);
					var8.drawStringWithShadow(text, x, y, 0xFFFFFF + (brightness << 24));
					GL11.glDisable(3042);
					GL11.glPopMatrix();
				}
			//}
		}
	}
}
