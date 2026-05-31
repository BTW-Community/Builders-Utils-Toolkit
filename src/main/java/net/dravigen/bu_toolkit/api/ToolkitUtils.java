package net.dravigen.bu_toolkit.api;

import net.dravigen.bu_toolkit.api.ListUtils.BlockPosD;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static net.dravigen.bu_toolkit.api.ListUtils.*;

public class ToolkitUtils {
	public static List<BlockPosD> renderedBox = new ArrayList<>();
	public static List<Selection> renderedBigBox = new ArrayList<>();
	
	
	static int[][] checks = new int[][]{{1, 0, 0}, {0, 0, 1}, {-1, 0, 0}, {0, 0, -1}, {0, 1, 0}, {0, -1, 0}};
	
	public static void drawCube(BlockPosD posD) {
		drawCube(posD.x(), posD.y(), posD.z());
	}
	
	public static void drawCube(double x, double y, double z) {
		BlockPosD posl1 = new BlockPosD(x - 0.001, y - 0.001, z - 0.001);
		BlockPosD posh3 = new BlockPosD(x + 1.001, y + 1.001, z + 1.001);
		
		boolean[] faceNeedingRender = new boolean[6];
		int i = 0;
		BlockPosD posD;
		
		for (int[] faceToCheck : checks) {
			posD = new BlockPosD(x + faceToCheck[0], y + faceToCheck[1], z + faceToCheck[2]);
			
			if (!isBlockRendered(posD)) {
				faceNeedingRender[i] = true;
			}
			
			i++;
		}
		
		BlockPosD posl2 = new BlockPosD(posh3.x(), posl1.y(), posl1.z());
		BlockPosD posl3 = new BlockPosD(posh3.x(), posl1.y(), posh3.z());
		BlockPosD posl4 = new BlockPosD(posl1.x(), posl1.y(), posh3.z());
		BlockPosD posh1 = new BlockPosD(posl1.x(), posh3.y(), posl1.z());
		BlockPosD posh2 = new BlockPosD(posh3.x(), posh3.y(), posl1.z());
		BlockPosD posh4 = new BlockPosD(posl1.x(), posh3.y(), posh3.z());
		
		boolean bEast = faceNeedingRender[0];
		boolean bSouth = faceNeedingRender[1];
		boolean bWest = faceNeedingRender[2];
		boolean bNorth = faceNeedingRender[3];
		boolean bUp = faceNeedingRender[4];
		boolean bDown = faceNeedingRender[5];
		
		GL11.glBegin(GL11.GL_LINES);
		
		// Draw vertical edges
		if ((bEast && bSouth) || (!bEast && !bSouth && !isBlockRendered(new BlockPosD(x + 1, y, z + 1)))) drawLine(posl3, posh3);
		if ((bSouth && bWest) || (!bSouth && !bWest && !isBlockRendered(new BlockPosD(x - 1, y, z + 1)))) drawLine(posl4, posh4);
		if ((bWest && bNorth) || (!bWest && !bNorth && !isBlockRendered(new BlockPosD(x - 1, y, z - 1)))) drawLine(posl1, posh1);
		if ((bNorth && bEast) || (!bNorth && !bEast && !isBlockRendered(new BlockPosD(x + 1, y, z - 1)))) drawLine(posl2, posh2);
		
		// Draw bottom face
		if ((bDown && bEast) || (!bDown && ! bEast && !isBlockRendered(new BlockPosD(x + 1, y - 1, z)))) drawLine(posl2, posl3);
		if ((bDown && bSouth) || (!bDown && ! bSouth && !isBlockRendered(new BlockPosD(x, y - 1, z + 1)))) drawLine(posl3, posl4);
		if ((bDown && bWest) || (!bDown && ! bWest && !isBlockRendered(new BlockPosD(x - 1, y - 1, z)))) drawLine(posl4, posl1);
		if ((bDown && bNorth) || (!bDown && ! bNorth && !isBlockRendered(new BlockPosD(x, y - 1, z - 1)))) drawLine(posl1, posl2);
		
		// Draw top face
		if ((bUp && bEast) || (!bUp && !bEast && !isBlockRendered(new BlockPosD(x + 1, y + 1, z)))) drawLine(posh2, posh3);
		if ((bUp && bSouth) || (!bUp && !bSouth && !isBlockRendered(new BlockPosD(x, y + 1, z + 1)))) drawLine(posh3, posh4);
		if ((bUp && bWest) || (!bUp && !bWest && !isBlockRendered(new BlockPosD(x - 1, y + 1, z)))) drawLine(posh4, posh1);
		if ((bUp && bNorth) || (!bUp && !bNorth && !isBlockRendered(new BlockPosD(x, y + 1, z - 1)))) drawLine(posh1, posh2);
		
		GL11.glEnd();
	}
	
	public static void drawBigCube(Selection selection) {
		BlockPosD pos1 = new BlockPosD(selection.pos1().x - 0.001, selection.pos1().y - 0.001, selection.pos1().z - 0.001);
		BlockPosD pos2 = new BlockPosD(selection.pos2().x + 1.001, selection.pos2().y + 1.001, selection.pos2().z + 1.001);
		
		BlockPosD pos3 = new BlockPosD(pos1.x(), pos2.y(), pos2.z());
		BlockPosD pos6 = new BlockPosD(pos2.x(), pos1.y(), pos1.z());
		BlockPosD pos7 = new BlockPosD(pos1.x(), pos2.y(), pos1.z());
		BlockPosD pos5 = new BlockPosD(pos1.x(), pos1.y(), pos2.z());
		BlockPosD pos4 = new BlockPosD(pos2.x(), pos1.y(), pos2.z());
		BlockPosD pos8 = new BlockPosD(pos2.x(), pos2.y(), pos1.z());
		
		GL11.glBegin(GL11.GL_LINES);
		
		drawLine(pos1, pos6);
		drawLine(pos1, pos7);
		drawLine(pos1, pos5);
		
		drawLine(pos5, pos4);
		drawLine(pos6, pos4);
		drawLine(pos6, pos8);
		
		drawLine(pos2, pos3);
		drawLine(pos2, pos4);
		drawLine(pos2, pos8);
		
		drawLine(pos8, pos7);
		drawLine(pos3, pos7);
		drawLine(pos3, pos5);
		
		GL11.glEnd();
	}
	
	private static boolean isBlockRendered(BlockPosD posD) {
		return renderedBox.contains(posD);
	}
	
	private static void drawLine(BlockPosD pos1, BlockPosD pos2) {
		drawLine(pos1.x(), pos1.y(), pos1.z(), pos2.x(), pos2.y(), pos2.z());
	}
	
	private static void drawLine(double x, double y, double z, double x1, double y1, double z1) {
		GL11.glVertex3d(x, y, z);
		GL11.glVertex3d(x1, y1, z1);
	}
}
