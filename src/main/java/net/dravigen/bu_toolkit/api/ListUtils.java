package net.dravigen.bu_toolkit.api;

import api.world.BlockPos;
import net.minecraft.src.EntityPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUtils {
	public record BlockInfo(int id, int meta, int x, int y, int z){}
	public record BlockPosD(double x, double y, double z) {}
	public record SelectionD(BlockPosD pos1, BlockPosD pos2) {}
	public record Selection(BlockPos pos1, BlockPos pos2) {}
	
	public static Map<EntityPlayer, List<List<BlockInfo>>> undoWandList = new HashMap<>();
}
