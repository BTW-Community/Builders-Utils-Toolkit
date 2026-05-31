package net.dravigen.bu_toolkit.items;

import api.item.items.ToolItem;
import api.world.BlockPos;
import emi.shims.java.net.minecraft.util.Formatting;
import net.dravigen.bu_toolkit.api.*;
import net.dravigen.bu_toolkit.api.ListUtils.BlockInfo;
import net.minecraft.src.*;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

import java.util.*;

import static net.dravigen.bu_toolkit.api.ListUtils.*;

public class BuildingWand extends ToolItem implements CustomTooltip, CustomItemWithModes, CustomItemBehavior {
	/** Description: extrude an area of the same kind of block
	 * sneaking revert last change
	 * special key ignore block type
	 */
	
	/** TODO:
	 * - Container selection
	 * - Different modes
	 * - randomizer
	 */
	
	
	public static String EXTRUDE_SIMPLE = "bu.toolkit.wand.mode.extrudeSimple";
	public static String EXTRUDE_LARGE = "bu.toolkit.wand.mode.extrudeLarge";
	public static String FILL_MODE = "bu.toolkit.wand.mode.fill";
	
	private static final String[] MODES = new String[]{EXTRUDE_SIMPLE, EXTRUDE_LARGE};
	
	public static byte SINGLE = 0;
	public static byte RANDOM = 1;
	
	public final int maxBlocks;
	
	private byte blockUsed = SINGLE;
	
	public BuildingWand(int iITemID, EnumToolMaterial material, int durability, int maxBlocks, String texture, String name) {
		super(iITemID, 0, material);
		this.maxBlocks = maxBlocks;
		this.setTextureName(texture);
		this.setUnlocalizedName(name);
		this.setMaxDamage(durability);
		this.setItemRightClickCooldown(5);
	}
	
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		this.setMode(stack, 0);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltipList, boolean bAdvanced) {
		if (blockUsed == SINGLE) {
			int idToPlace = 0;
			int metaToPlace = 0;
			String blockName = "none";
			
			if (stack.getTagCompound() != null) {
				idToPlace = stack.getTagCompound().getInteger("idType");
				metaToPlace = stack.getTagCompound().getInteger("metaType");
				blockName = I18n.getString(stack.getTagCompound().getString("blockName"));
				
			}
			
			List<ItemStack> stackList = new ArrayList<>();
			stackList.add(new ItemStack(idToPlace, 1 ,metaToPlace));
			
			String iBlocksAvailable;
			
			if (player.capabilities.isCreativeMode) {
				iBlocksAvailable = "Infinite";
			}
			else {
				iBlocksAvailable = Integer.toString(InvUtils.getStacksInInv(player, stackList));
			}
			
			tooltipList.add(Formatting.GRAY +
									I18n.getStringParams("bu.toolkit.wand.tooltip.blockToUse",
														 blockName,
														 idToPlace,
														 metaToPlace));
			tooltipList.add(Formatting.GRAY +
									I18n.getStringParams("bu.toolkit.general.tooltip.itemsAvailable", iBlocksAvailable));
	
		}
		
		tooltipList.add(Formatting.GRAY +
								I18n.getStringParams("bu.toolkit.general.tooltip.mode", this.getModeName(stack)));
		
		tooltipList.add("");
		
		GameSettings settings = Minecraft.getMinecraft().gameSettings;
		
		if (!Keyboard.isKeyDown(settings.keyBindSneak.keyCode)) {
			tooltipList.add(I18n.getStringParams("bu.toolkit.general.tooltip.infoKey",
												 GameSettings.getKeyDisplayString(settings.keyBindSneak.keyCode)));
		}
		else {
			tooltipList.add(I18n.getStringParams("bu.toolkit.general.tooltip.useKey",
												 GameSettings.getKeyDisplayString(settings.keyBindUseItem.keyCode)));
			tooltipList.add(I18n.getStringParams("bu.toolkit.wand.tooltip.usageHelp2",
												 GameSettings.getKeyDisplayString(settings.keyBindSneak.keyCode),
												 GameSettings.getKeyDisplayString(settings.keyBindAttack.keyCode)));
			tooltipList.add(I18n.getStringParams("bu.toolkit.wand.tooltip.usageHelp3",
												 GameSettings.getKeyDisplayString(settings.keyBindSneak.keyCode),
												 GameSettings.getKeyDisplayString(settings.keyBindUseItem.keyCode)));
			tooltipList.add(I18n.getStringParams("bu.toolkit.wand.tooltip.usageHelp4",
												 GameSettings.getKeyDisplayString(settings.keyBindSpecial.keyCode)));
		}
	}
	
	@Override
	public boolean isToolTypeEfficientVsBlockType(Block var1) {
		return false;
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int i, int j, int k, int iFacing,
			float fClickX, float fClickY, float fClickZ) {
		if (player instanceof EntityPlayerMP playerMP && playerMP.theItemInWorldManager.itemCooldowns.getOrDefault(stack.itemID, 0L) <= world.getTotalWorldTime()) {
			if (stack.getTagCompound() != null) {
				int idToPlace = stack.getTagCompound().getInteger("idType");
				int metaToPlace = stack.getTagCompound().getInteger("metaType");
				String mode = this.getModeNameUnlocalized(stack);
				List<ItemStack> stackList = new ArrayList<>();
				stackList.add(new ItemStack(idToPlace, 1, metaToPlace));
				
				if (idToPlace != 0) {
					if (!player.isSneaking() && (mode.equals(EXTRUDE_SIMPLE) || mode.equals(EXTRUDE_LARGE))) {
						List<BlockPos> blocksToPlace = getBlocksToExtrude(player,
																		  world,
																		  i,
																		  j,
																		  k,
																		  iFacing,
																		  stackList,
																		  mode.equals(EXTRUDE_LARGE));
						
						if (!blocksToPlace.isEmpty()) {
							List<BlockInfo> undo = new ArrayList<>();
							int x;
							int y;
							int z;
							
							for (BlockPos blockPos : blocksToPlace) {
								x = blockPos.x;
								y = blockPos.y;
								z = blockPos.z;
								
								switch (iFacing) {
									case 0 -> y--;
									case 1 -> y++;
									case 2 -> z--;
									case 3 -> z++;
									case 4 -> x--;
									case 5 -> x++;
								}
								
								undo.add(new BlockInfo(idToPlace, metaToPlace, x, y, z));
								world.setBlock(x, y, z, idToPlace, metaToPlace, 3);
							}
							
							InvUtils.useStackInInv(player, blocksToPlace.size(), stackList);
							
							stack.damageItem(1, player);
							
							List<List<BlockInfo>> list = undoWandList.getOrDefault(player, new ArrayList<>());
							list.add(undo);
							undoWandList.put(player, list);
						}
					}
				}
			}
		}
		
		return super.onItemUse(stack, player, world, i, j, k, iFacing, fClickX, fClickY, fClickZ);
	}
	
	public @NotNull List<BlockPos> getBlocksToExtrude(EntityPlayer player, World world, int i, int j, int k, int iFacing,
			List<ItemStack> stackList, boolean bCheckDiagonals) {
		int iBlocksAvailable = player.capabilities.isCreativeMode ? Integer.MAX_VALUE : InvUtils.getStacksInInv(player, stackList);
		
		Queue<BlockPos> blocksToCheck = new LinkedList<>();
		List<String> visited = new LinkedList<>();
		List<BlockPos> blocksToPlace = new ArrayList<>();
		
		blocksToCheck.add(new BlockPos(i, j, k));
		visited.add(i + "," + j + "," + k);
		
		int initialID = world.getBlockId(i, j, k);
		int initialMETA = world.getBlockMetadata(i, j, k);
		
		int[][] planeOff;
		
		if (bCheckDiagonals) {
			switch (iFacing) {
				case 0, 1 -> planeOff = new int[][]{{1, 0, 0}, {1, 0, 1}, {0, 0, 1}, {-1, 0, 1}, {-1, 0, 0}, {-1, 0, -1}, {0, 0, -1}, {1, 0, -1}};
				case 2, 3 -> planeOff = new int[][]{{1, 0, 0}, {1, -1, 0}, {0, -1, 0}, {-1, -1, 0}, {-1, 0, 0}, {-1, 1, 0}, {0, 1, 0}, {1, 1, 0}};
				case 4, 5 -> planeOff = new int[][]{{0, 0, 1}, {0, -1, 1}, {0, -1, 0}, {0, -1, -1}, {0, 0, -1}, {0, 1, -1}, {0, 1, 0}, {0, 1, 1}};
				default -> planeOff = new int[][]{{0, 0, 0}};
			}
		}
		else {
			switch (iFacing) {
				case 0, 1 -> planeOff = new int[][]{{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}};
				case 2, 3 -> planeOff = new int[][]{{1, 0, 0}, {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}};
				case 4, 5 -> planeOff = new int[][]{{0, 1, 0}, {0, -1, 0}, {0, 0, 1}, {0, 0, -1}};
				default -> planeOff = new int[][]{{0, 0, 0}};
			}
		}
		
		BlockPos current;
		BlockPos neighbor;
		String neighborPos;
		int nextX;
		int nextY;
		int nextZ;
		int newBlockX;
		int newBlockY;
		int newBlockZ;
		int targetBlockId;
		int neighborId;
		int neighborMeta;
		
		while (!blocksToCheck.isEmpty() && blocksToPlace.size() < this.maxBlocks && blocksToPlace.size() < iBlocksAvailable) {
			current = blocksToCheck.poll();
			blocksToPlace.add(current);
			
			for (int[] offset : planeOff) {
				nextX = current.x + offset[0];
				nextY = current.y + offset[1];
				nextZ = current.z + offset[2];
				
				neighbor = new BlockPos(nextX, nextY, nextZ);
				neighborPos = nextX + "," + nextY + "," + nextZ;
				
				if (nextY < 0 || nextY >= world.getHeight() || visited.contains(neighborPos)) continue;
				
				newBlockX = nextX;
				newBlockY = nextY;
				newBlockZ = nextZ;
				
				switch (iFacing) {
					case 0 -> newBlockY--;
					case 1 -> newBlockY++;
					case 2 -> newBlockZ--;
					case 3 -> newBlockZ++;
					case 4 -> newBlockX--;
					case 5 -> newBlockX++;
				}
				
				targetBlockId = world.getBlockId(newBlockX, newBlockY, newBlockZ);
				
				if (targetBlockId == 0 ||
						Block.blocksList[targetBlockId].isReplaceableVegetation(world,
																				newBlockX,
																				newBlockY,
																				newBlockZ)) {
					neighborId = world.getBlockId(nextX, nextY, nextZ);
					neighborMeta = world.getBlockMetadata(nextX, nextY, nextZ);
					
					if (player.isUsingSpecialKey() && neighborId != 0 || (neighborId == initialID && neighborMeta == initialMETA)) {
						blocksToCheck.add(neighbor);
						visited.add(neighborPos);
					}
				}
			}
		}
		
		return blocksToPlace;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!world.isRemote) {
			if (player.isSneaking()) {
				if (!undoWandList.getOrDefault(player, new ArrayList<>()).isEmpty()) {
					List<List<BlockInfo>> lists = undoWandList.get(player);
					List<BlockInfo> blocksToUndo = lists.get(lists.size() - 1);
					lists.remove(lists.size() - 1);
					
					Map<Integer[], Integer> items = new HashMap<>();
					
					for (BlockInfo pos : blocksToUndo) {
						int x = pos.x();
						int y = pos.y();
						int z = pos.z();
						
						if (world.getBlockId(x, y, z) == pos.id() && world.getBlockMetadata(x, y, z) == pos.meta()) {
							if (!player.capabilities.isCreativeMode) {
								Integer[] item = {pos.id(), pos.meta()};
								items.put(item, items.getOrDefault(item, 0) + 1);
							}
							
							world.setBlockToAir(x, y, z);
						}
					}
					
					for (Map.Entry<Integer[], Integer> item : items.entrySet()) {
						ItemStack stackToReturn = new ItemStack(item.getKey()[0], item.getValue(), item.getKey()[1]);
						
						if (!player.inventory.addItemStackToInventory(stackToReturn)) {
							player.dropPlayerItem(stackToReturn);
						}
					}
					
					stack.damageItem(1, player);
				}
			}
		}
		
		return super.onItemRightClick(stack, world, player);
	}
	
	@Override
	public Map<String, String[]> getTooltip() {
		Map<String, String[]> tooltips = new HashMap<>();
		tooltips.put("blockLimit", new String[]{"bu.toolkit.wand.tooltip.blockLimit", String.valueOf(this.maxBlocks)});
		return tooltips;
	}
	
	@Override
	public void renderSpecial(ItemStack stack, EntityPlayer player, Minecraft mc) {
		if (player.isSneaking() && !undoWandList.getOrDefault(player, new ArrayList<>()).isEmpty()) {
			List<List<BlockInfo>> lists = undoWandList.get(player);
			List<BlockInfo> blocksToUndo = lists.get(lists.size() - 1);
			World world = player.worldObj;
			
			for (BlockInfo pos : blocksToUndo) {
				int x = pos.x();
				int y = pos.y();
				int z = pos.z();
				
				if (world.getBlockId(x, y, z) == pos.id() && world.getBlockMetadata(x, y, z) == pos.meta()) {
					ToolkitUtils.renderedBox.add(new ListUtils.BlockPosD(x, y, z));
				}
			}
		}
	}
	
	@Override
	public void renderAtBlockTarget(ItemStack stack, EntityPlayer player, Minecraft mc, int i, int j, int k, World world) {
		if (stack.getTagCompound() != null) {
			int idToPlace = stack.getTagCompound().getInteger("idType");
			int metaToPlace = stack.getTagCompound().getInteger("metaType");
			String mode = this.getModeNameUnlocalized(stack);
			
			if (idToPlace != 0) {
				if (!player.isSneaking() && (mode.equals(EXTRUDE_SIMPLE) || mode.equals(EXTRUDE_LARGE))) {
					int iFacing = mc.objectMouseOver.sideHit;
					List<ItemStack> stackList = new ArrayList<>();
					stackList.add(new ItemStack(idToPlace, 1, metaToPlace));
					
					List<BlockPos> blocksToPlace = this.getBlocksToExtrude(player, world, i, j, k, iFacing, stackList, mode.equals(EXTRUDE_LARGE));
					
					for (BlockPos pos : blocksToPlace) {
						int x = 0;
						int y = 0;
						int z = 0;
						
						switch (iFacing) {
							case 0 -> y--;
							case 1 -> y++;
							case 2 -> z--;
							case 3 -> z++;
							case 4 -> x--;
							case 5 -> x++;
						}
						
						ToolkitUtils.renderedBox.add(new ListUtils.BlockPosD(pos.x + x, pos.y + y, pos.z + z));
					}
				}
			}
		}
	}
	
	@Override
	public String[] getModes() {
		return MODES;
	}
	
	@Override
	public boolean shouldBreakBlock(EntityPlayer player, ItemStack stack) {
		return player.isSneaking();
	}
}
