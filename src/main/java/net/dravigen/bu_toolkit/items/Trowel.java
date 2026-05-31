package net.dravigen.bu_toolkit.items;

import api.item.items.ToolItem;
import api.world.BlockPos;
import btw.item.BTWTags;
import emi.shims.java.net.minecraft.util.Formatting;
import net.dravigen.bu_toolkit.api.*;
import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.dravigen.bu_toolkit.api.ListUtils.*;

public class Trowel extends ToolItem implements CustomTooltip, CustomItemBehavior {
	/// Description: allows you to select an area to mortar quickly with mortar items in your hotbar
	
	private final int maxSize;
	
	public Trowel(int id, EnumToolMaterial material, int durability, int maxSize, String texture, String name) {
		super(id, 0, material);
		this.maxSize = maxSize - 1;
		this.setTextureName(texture);
		this.setUnlocalizedName(name);
		this.setMaxDamage(durability);
		this.setItemRightClickCooldown(5);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, EntityPlayer player, int iInventorySlot,
			boolean bIsHandHeldItem) {
		if (stack != null) {
			if (stack.getTagCompound() == null) {
				stack.setTagCompound(new NBTTagCompound());
			}
			
			NBTTagCompound tagCompound = stack.getTagCompound();
			
			int[] blockPos = tagCompound.getIntArray("blockPos");
			int size = maxSize + 1;
			
			
			if (blockPos.length != 0 && player.getDistance(blockPos[0], blockPos[1], blockPos[2]) > Math.sqrt(size * size + size * size) * 1.5) {
				tagCompound.setIntArray("blockPos", new  int[]{});
			}
		}
		
		super.onUpdate(stack, world, player, iInventorySlot, bIsHandHeldItem);
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int i, int j, int k, int iFacing,
			float fClickX, float fClickY, float fClickZ) {
		if (!player.isSneaking() && player instanceof EntityPlayerMP playerMP && playerMP.theItemInWorldManager.itemCooldowns.getOrDefault(stack.itemID, 0L) <= world.getTotalWorldTime()) {
			if (stack.getTagCompound() == null) {
				stack.setTagCompound(new NBTTagCompound());
			}
			
			NBTTagCompound tagCompound = stack.getTagCompound();
			
			int[] blockPos = tagCompound.getIntArray("blockPos");
			
			if (blockPos.length == 0) {
				tagCompound.setIntArray("blockPos", new int[]{i, j, k});
			}
			else {
				int i2 = blockPos[0] - i;
				int j2 = blockPos[1] - j;
				int k2 = blockPos[2] - k;

				if (Math.abs(i2) > maxSize) i = i2 > 0 ? blockPos[0] - maxSize : blockPos[0] + maxSize;
				if (Math.abs(j2) > maxSize) j = j2 > 0 ? blockPos[1] - maxSize : blockPos[1] + maxSize;
				if (Math.abs(k2) > maxSize) k = k2 > 0 ? blockPos[2] - maxSize : blockPos[2] + maxSize;
				
				int minX = Math.min(blockPos[0], i);
				int minY = Math.min(blockPos[1], j);
				int minZ = Math.min(blockPos[2], k);
				int maxX = Math.max(blockPos[0], i);
				int maxY = Math.max(blockPos[1], j);
				int maxZ = Math.max(blockPos[2], k);
				
				Block block;
				int iItemUsed = 0;
				int iItemAvailable = player.capabilities.isCreativeMode ? Integer.MAX_VALUE : InvUtils.getStacksInInv(player, BTWTags.mortars.getItems());
				
				for (int x = minX; x <= maxX; x++) {
					if (iItemUsed >= iItemAvailable) break;
					
					for (int y = minY; y <= maxY; y++) {
						if (iItemUsed >= iItemAvailable) break;
						
						for (int z = minZ; z <= maxZ; z++) {
							if (iItemUsed >= iItemAvailable) break;
							
							block = Block.blocksList[world.getBlockId(x, y, z)];
							
							if (block != null && block.onMortarApplied(world, x, y, z)) {
								iItemUsed++;
							}
						}
					}
				}
				
				if (iItemUsed > 0) {
					world.playAuxSFX(2274, i, j, k, 0);
					
					InvUtils.useStackInInv(player, iItemUsed, BTWTags.mortars.getItems());
					
					stack.damageItem(1, player);
				}
				
				tagCompound.setIntArray("blockPos", new int[]{});
			}
		}
		
		return super.onItemUse(stack, player, world, i, j, k, iFacing, fClickX, fClickY, fClickZ);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (player.isSneaking()) {
			if (stack.getTagCompound() == null) {
				stack.setTagCompound(new NBTTagCompound());
			}
			
			NBTTagCompound tagCompound = stack.getTagCompound();
			
			tagCompound.setIntArray("blockPos", new int[]{});
		}
		
		return super.onItemRightClick(stack, world, player);
	}
	
	@Override
	public boolean isToolTypeEfficientVsBlockType(Block var1) {
		return false;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltipList, boolean bAdvanced) {
		String iMortarAvailable;
		
		if (player.capabilities.isCreativeMode) {
			iMortarAvailable = "Infinite";
		}
		else {
			iMortarAvailable = Integer.toString(InvUtils.getStacksInInv(player, BTWTags.mortars.getItems()));
		}
		
		tooltipList.add(Formatting.GRAY +
								I18n.getStringParams("bu.toolkit.general.tooltip.itemsAvailable", iMortarAvailable));
		
		tooltipList.add("");
		
		GameSettings settings = Minecraft.getMinecraft().gameSettings;
		
		if (!Keyboard.isKeyDown(settings.keyBindSneak.keyCode)) {
			tooltipList.add(I18n.getStringParams("bu.toolkit.general.tooltip.infoKey",
												 GameSettings.getKeyDisplayString(settings.keyBindSneak.keyCode)));
		}
		else {
			tooltipList.add(I18n.getStringParams("bu.toolkit.trowel.tooltip.usageHelp1",
												 GameSettings.getKeyDisplayString(settings.keyBindSneak.keyCode),
												 GameSettings.getKeyDisplayString(settings.keyBindUseItem.keyCode)));
		}
	}
	
	@Override
	public Map<String, String[]> getTooltip() {
		Map<String, String[]> tooltips = new HashMap<>();
		String size = String.valueOf(this.maxSize + 1);
		tooltips.put("maxSize", new String[]{"bu.toolkit.trowel.tooltip.maxSize", size, size, size});
		return tooltips;
	}
	
	@Override
	public void renderAtBlockTarget(ItemStack stack, EntityPlayer player, Minecraft mc, int i, int j, int k, World world) {
		if (stack.getTagCompound() != null) {
			int[] blockPos = stack.getTagCompound().getIntArray("blockPos");
			
			if (blockPos.length != 0) {
				int i2 = blockPos[0] - i;
				int j2 = blockPos[1] - j;
				int k2 = blockPos[2] - k;
				
				if (Math.abs(i2) > maxSize) i = i2 > 0 ? blockPos[0] - maxSize : blockPos[0] + maxSize;
				if (Math.abs(j2) > maxSize) j = j2 > 0 ? blockPos[1] - maxSize : blockPos[1] + maxSize;
				if (Math.abs(k2) > maxSize) k = k2 > 0 ? blockPos[2] - maxSize : blockPos[2] + maxSize;
				
				int minX = Math.min(blockPos[0], i);
				int minY = Math.min(blockPos[1], j);
				int minZ = Math.min(blockPos[2], k);
				int maxX = Math.max(blockPos[0], i);
				int maxY = Math.max(blockPos[1], j);
				int maxZ = Math.max(blockPos[2], k);
				
				int iBlocks = (maxX + 1 - minX) * (maxY + 1 - minY) * (maxZ + 1 - minZ);
				stack.getTagCompound().setIntArray("blockUsage", new int[]{maxX + 1 - minX, maxY + 1 - minY, maxZ + 1 - minZ, iBlocks});
				
				ToolkitUtils.renderedBigBox.add(new Selection(new BlockPos(minX, minY, minZ), new BlockPos(maxX, maxY, maxZ)));
			}
		}
	}
	
	@Override
	public void stopRenderAtBlockTarget(ItemStack stack, EntityPlayer player, Minecraft mc) {
		stack.getTagCompound().setIntArray("blockUsage", new int[]{});
	}
	
	@Override
	public void displayOnScreen(ItemStack stack, Minecraft mc) {
		if (stack.getTagCompound().getIntArray("blockPos").length != 0 && stack.getTagCompound().getIntArray("blockUsage").length != 0) {
			ScaledResolution var5 = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
			int var6 = var5.getScaledWidth();
			int var7 = var5.getScaledHeight();
			FontRenderer var8 = mc.fontRenderer;
			int brightness = 255;
			
			int[] infos = stack.getTagCompound().getIntArray("blockUsage");
			
			String text = infos[0] + "x" + infos[1] + "x" + infos[2] + " (" + infos[3] + ")";
			int x = (var6 - var8.getStringWidth(text)) / 2;
			int y = var7 - 64 - var8.FONT_HEIGHT;
			
			if (!mc.playerController.shouldDrawHUD()) {
				y += 14;
			}
			
			GL11.glPushMatrix();
			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 771);
			var8.drawStringWithShadow(text, x, y, 0xFFFFFF + (brightness << 24));
			GL11.glDisable(3042);
			GL11.glPopMatrix();
		}
	}
}
