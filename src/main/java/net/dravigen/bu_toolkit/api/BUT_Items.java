package net.dravigen.bu_toolkit.api;

import btw.item.BTWItems;
import btw.item.BTWTags;
import net.dravigen.bu_toolkit.items.BuildingWand;
import net.dravigen.bu_toolkit.items.Trowel;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.Item;

public class BUT_Items {
	public static Item ironWand;
	public static Item diamondWand;
	public static Item sfsWand;
	
	public static Item ironTrowel;
	public static Item diamondTrowel;
	public static Item sfsTrowel;
	
	public static Item diamondNugget;
	
	public static int baseID = 9500;
	
	public static void registerAll() {
		ironWand = new BuildingWand(baseID++, EnumToolMaterial.IRON, 50, 64, "bu_toolkit:ironWand1", "bu.toolkit.items.ironWand");
		diamondWand = new BuildingWand(baseID++, EnumToolMaterial.EMERALD, 500, 128, "bu_toolkit:diamondWand1", "bu.toolkit.items.diamondWand");
		sfsWand = new BuildingWand(baseID++, EnumToolMaterial.SOULFORGED_STEEL, 2500, 512, "bu_toolkit:sfsWand1", "bu.toolkit.items.sfsWand");
	
		ironTrowel = new Trowel(baseID++, EnumToolMaterial.IRON, 50, 3, "bu_toolkit:ironTrowel", "bu.toolkit.items.ironTrowel");
		diamondTrowel = new Trowel(baseID++, EnumToolMaterial.EMERALD, 250, 9, "bu_toolkit:diamondTrowel", "bu.toolkit.items.diamondTrowel");
		sfsTrowel = new Trowel(baseID++, EnumToolMaterial.SOULFORGED_STEEL, 1250, 27, "bu_toolkit:sfsTrowel", "bu.toolkit.items.sfsTrowel");
		
		diamondNugget = new Item(baseID++).setUnlocalizedName("bu.toolkit.items.diamondNugget").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("bu_toolkit:diamondNugget");
		
		BTWTags.tools.add(ironWand);
		BTWTags.tools.add(diamondWand);
		BTWTags.tools.add(sfsWand);
		BTWTags.tools.add(ironTrowel);
		BTWTags.tools.add(diamondTrowel);
		BTWTags.tools.add(sfsTrowel);
		BTWTags.nuggets.add(diamondNugget);
	}
}
