package net.dravigen.bu_toolkit;

import api.AddonHandler;
import api.BTWAddon;
import btw.block.BTWBlocks;
import btw.crafting.recipe.RecipeManager;
import btw.item.BTWItems;
import net.dravigen.bu_toolkit.api.BUT_Items;
import net.dravigen.bu_toolkit.network.PacketHandlerUtils;
import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;

public class BU_Toolkit extends BTWAddon {
    private static BU_Toolkit instance;
	public static KeyBinding modeSwitch;

    public BU_Toolkit() {
        super();
		instance = this;
    }

    @Override
    public void initialize() {
		AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
		PacketHandlerUtils.initHandlers(this);
		BUT_Items.registerAll();
		initRecipes();
		initKeybind();
	}
	public void initKeybind() {
		modeSwitch = new KeyBinding(StatCollector.translateToLocal("bu.toolkit.keybind.modeSwitch"), Keyboard.KEY_Y);
	}
	
	private static void initRecipes() {
		RecipeManager.addMillStoneRecipe(new ItemStack(BUT_Items.diamondNugget, 4), new ItemStack(Item.diamond));
		
		RecipeManager.addSoulforgeRecipe(new ItemStack(Item.diamond), new Object[]{
				"##",
				"##",
				Character.valueOf('#'),
				BUT_Items.diamondNugget
		});
		
		RecipeManager.addRecipe(new ItemStack(BUT_Items.ironTrowel), new Object[]{
				"XN ",
				"NO ",
				"  #",
				Character.valueOf('#'),
				Item.stick,
				Character.valueOf('X'),
				Item.ingotIron,
				Character.valueOf('O'),
				BTWItems.rope,
				Character.valueOf('N'),
				BTWItems.ironNugget
		});
		
		RecipeManager.addRecipe(new ItemStack(BUT_Items.diamondTrowel), new Object[]{
				"XN ",
				"NO ",
				"  #",
				Character.valueOf('#'),
				Item.stick,
				Character.valueOf('X'),
				BTWItems.diamondIngot,
				Character.valueOf('O'),
				BTWItems.leatherStrap,
				Character.valueOf('N'),
				BUT_Items.diamondNugget
		});
		
		RecipeManager.addSoulforgeRecipe(new ItemStack(BUT_Items.sfsTrowel), new Object[]{
				"XN  ",
				"NO  ",
				"  # ",
				"   #",
				Character.valueOf('#'),
				BTWItems.haft,
				Character.valueOf('X'),
				BTWItems.soulforgedSteelIngot,
				Character.valueOf('O'),
				BTWItems.belt,
				Character.valueOf('N'),
				BTWItems.steelNugget
		});
		
		RecipeManager.addRecipe(new ItemStack(BUT_Items.ironWand), new Object[]{
				" NX",
				" ON",
				"#  ",
				Character.valueOf('#'),
				Item.stick,
				Character.valueOf('X'),
				Item.ingotIron,
				Character.valueOf('O'),
				Block.blockRedstone,
				Character.valueOf('N'),
				BTWItems.ironNugget
		});
		
		RecipeManager.addRecipe(new ItemStack(BUT_Items.diamondWand), new Object[]{
				" NX",
				" ON",
				"#  ",
				Character.valueOf('#'),
				Item.stick,
				Character.valueOf('X'),
				BTWItems.diamondIngot,
				Character.valueOf('O'),
				Block.glowStone,
				Character.valueOf('N'),
				BUT_Items.diamondNugget
			
		});
		
		RecipeManager.addSoulforgeRecipe(new ItemStack(BUT_Items.sfsWand), new Object[]{
				"  NX",
				"  ON",
				" #  ",
				"#   ",
				Character.valueOf('#'),
				BTWItems.haft,
				Character.valueOf('X'),
				BTWBlocks.soulforgedSteelBlock,
				Character.valueOf('O'),
				Item.netherStar,
				Character.valueOf('N'),
				BTWItems.soulforgedSteelIngot
		});
		
		RecipeManager.addStokedCrucibleRecipe(new ItemStack(BTWItems.ironNugget, 4),
											  new ItemStack[]{
													  new ItemStack(BUT_Items.ironWand,
																	1,
																	Short.MAX_VALUE)
											  });
		
		RecipeManager.addStokedCrucibleRecipe(new ItemStack(BTWItems.diamondIngot, 1),
											  new ItemStack[]{
													  new ItemStack(BUT_Items.diamondWand,
																	1,
																	Short.MAX_VALUE)
											  });
		
		RecipeManager.addStokedCrucibleRecipe(new ItemStack(BTWItems.soulforgedSteelIngot, 11),
											  new ItemStack[]{
													  new ItemStack(BUT_Items.sfsWand,
																	1,
																	Short.MAX_VALUE)
											  });
		
		RecipeManager.addStokedCrucibleRecipe(new ItemStack(BTWItems.ironNugget, 6),
											  new ItemStack[]{
													  new ItemStack(BUT_Items.ironTrowel,
																	1,
																	Short.MAX_VALUE)
											  });
		
		RecipeManager.addStokedCrucibleRecipe(new ItemStack(BTWItems.diamondIngot, 2),
											  new ItemStack[]{
													  new ItemStack(BUT_Items.diamondTrowel,
																	1,
																	Short.MAX_VALUE)
											  });
		
		RecipeManager.addStokedCrucibleRecipe(new ItemStack(BTWItems.soulforgedSteelIngot, 3),
											  new ItemStack[]{
													  new ItemStack(BUT_Items.sfsTrowel,
																	1,
																	Short.MAX_VALUE)
											  });
		
		
	}
}