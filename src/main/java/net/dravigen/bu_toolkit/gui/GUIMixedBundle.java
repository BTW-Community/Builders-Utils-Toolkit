package net.dravigen.bu_toolkit.gui;

import btw.inventory.container.HamperContainer;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

public class GUIMixedBundle extends GuiContainer {
	private static final ResourceLocation HAMPER_GUI_TEXTURE = new ResourceLocation("btw:textures/gui/hamper.png");
	private static final int HAMPER_GUI_HEIGHT = 167;
	private IInventory hamperInventory;
	
	
	public GUIMixedBundle(InventoryPlayer playerInventory, IInventory containerInv) {
		super(new HamperContainer(playerInventory, containerInv));
		this.ySize = 167;
		this.hamperInventory = containerInv;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		String windowName = StatCollector.translateToLocal(this.hamperInventory.getInvName());
		this.fontRenderer.drawString(windowName, this.xSize / 2 - this.fontRenderer.getStringWidth(windowName) / 2, 6, 0x404040);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 0x404040);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.renderEngine.bindTexture(HAMPER_GUI_TEXTURE);
		int xPos = (this.width - this.xSize) / 2;
		int yPos = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);
	}
}
