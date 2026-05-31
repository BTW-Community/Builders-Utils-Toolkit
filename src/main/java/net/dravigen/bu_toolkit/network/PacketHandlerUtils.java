package net.dravigen.bu_toolkit.network;

import api.BTWAddon;
import net.dravigen.bu_toolkit.api.CustomItemWithModes;
import net.minecraft.src.ChatMessageComponent;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class PacketHandlerUtils {
	public static String WAND_SELECTION_CHANNEL = "bu_toolkit|WS";
	public static String MODE_CYCLE_CHANNEL = "bu_toolkit|MC";
	
	public static void initHandlers(BTWAddon addon) {
		addon.registerPacketHandler(WAND_SELECTION_CHANNEL, (packet, player) -> {
			DataInputStream dataStream = new DataInputStream(new ByteArrayInputStream(packet.data));
			ItemStack stack = player.getHeldItem();
			
			if (stack.getTagCompound() == null) {
				stack.setTagCompound(new NBTTagCompound());
			}
			
			int x = dataStream.readInt();
			int y = dataStream.readInt();
			int z = dataStream.readInt();
			
			int id = player.worldObj.getBlockId(x, y, z);
			int meta = player.worldObj.getBlockMetadata(x, y, z);
			ItemStack block = new ItemStack(id, 1, meta);
			ChatMessageComponent blockName = ChatMessageComponent.createFromTranslationKey(block.getUnlocalizedName() + ".name");
			
			stack.getTagCompound().setInteger("idType", id);
			stack.getTagCompound().setInteger("metaType", meta);
			stack.getTagCompound().setString("blockName", block.getUnlocalizedName() + ".name");
			
			player.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("bu.toolkit.wand.selection", blockName, id, meta));
		});
		
		addon.registerPacketHandler(MODE_CYCLE_CHANNEL, (packet, player) -> {
			ItemStack stack = player.getHeldItem();
			
			if (stack.getTagCompound() == null) {
				stack.setTagCompound(new NBTTagCompound());
			}
			
			stack.getTagCompound().setInteger("modeChange", 40);
			((CustomItemWithModes) stack.getItem()).cycleMode(stack);
		});
	}
}
