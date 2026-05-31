package net.dravigen.bu_toolkit.mixin.client;

import api.world.BlockPos;
import net.dravigen.bu_toolkit.BU_Toolkit;
import net.dravigen.bu_toolkit.api.*;
import net.dravigen.bu_toolkit.items.BuildingWand;
import net.dravigen.bu_toolkit.network.PacketHandlerUtils;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


@Mixin(EntityClientPlayerMP.class)
public abstract class EntityClientPlayerMPMx extends EntityPlayerSP {
	public EntityClientPlayerMPMx(Minecraft par1Minecraft, World par2World, Session par3Session, int par4) {
		super(par1Minecraft, par2World, par3Session, par4);
	}
	
	boolean isPressed = false;
	
	@Inject(method = "onUpdate", at = @At("HEAD"))
	private void entityTick(CallbackInfo ci) {
		ToolkitUtils.renderedBox.clear();
		ToolkitUtils.renderedBigBox.clear();
		ItemStack stack = this.getHeldItem();
		Minecraft mc = this.mc;
		
		if (stack != null && stack.getItem() instanceof CustomItemWithModes) {
			stack.getTagCompound().setInteger("modeChange", stack.getTagCompound().getInteger("modeChange") - 1);
			
			if (BU_Toolkit.modeSwitch.isPressed()) {
				if (!isPressed) {
					mc.getNetHandler().addToSendQueue(new Packet250CustomPayload(PacketHandlerUtils.MODE_CYCLE_CHANNEL, new byte[]{}));
					
					isPressed = true;
				}
			}
			else isPressed = false;
		}
		
		if (stack != null && stack.getItem() instanceof CustomItemBehavior customStack) {
			if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE) {
				int i = mc.objectMouseOver.blockX;
				int j = mc.objectMouseOver.blockY;
				int k = mc.objectMouseOver.blockZ;
				World world = this.worldObj;
				
				customStack.renderAtBlockTarget(stack, this, mc, i, j, k, world);
			}
			else {
				customStack.stopRenderAtBlockTarget(stack, this, mc);
			}
			
			customStack.renderSpecial(stack, this, mc);
		}
	}
}
