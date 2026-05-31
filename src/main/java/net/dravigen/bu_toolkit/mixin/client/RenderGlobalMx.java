package net.dravigen.bu_toolkit.mixin.client;

import net.dravigen.bu_toolkit.api.ListUtils;
import net.minecraft.src.ICamera;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.Vec3;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static net.dravigen.bu_toolkit.api.ListUtils.*;
import static net.dravigen.bu_toolkit.api.ToolkitUtils.*;


@Mixin(RenderGlobal.class)
public abstract class RenderGlobalMx {
	@Inject(method = "renderEntities", at = @At("HEAD"))
	private void render(Vec3 par1Vec3, ICamera par2ICamera, float par3, CallbackInfo ci) {
		double playerRenderX = par1Vec3.xCoord;
		double playerRenderY = par1Vec3.yCoord;
		double playerRenderZ = par1Vec3.zCoord;
		GL11.glPushMatrix();
		GL11.glTranslated(-playerRenderX, -playerRenderY, -playerRenderZ);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		GL11.glColor3d(255, 255, 0);
		GL11.glLineWidth(5);
		
		for (BlockPosD posD : List.copyOf(renderedBox)) {
			drawCube(posD);
		}
		
		for (Selection selection : List.copyOf(renderedBigBox)) {
			drawBigCube(selection);
		}
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		
		GL11.glPopMatrix();
	}
}
