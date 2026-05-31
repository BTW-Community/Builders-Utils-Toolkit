package net.dravigen.bu_toolkit.mixin.client;

import net.dravigen.bu_toolkit.BU_Toolkit;
import net.minecraft.src.GameSettings;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.Arrays;

@Mixin(GameSettings.class)
public abstract class GameSettingsMx {
	
	@Shadow
	public KeyBinding[] keyBindings;
	
	@Shadow
	public abstract void loadOptions();
	
	@Unique
	private void KeyMappings$addKeyBinds() {
		keyBindings = Arrays.copyOf(keyBindings, keyBindings.length + 1);
		keyBindings[keyBindings.length - 1] = BU_Toolkit.modeSwitch;
	}
	
	@Inject(method = "<init>()V", at = @At(value = "TAIL"))
	private void KeyMapping$initTail(CallbackInfo ci) {
		KeyMappings$addKeyBinds();
	}
	
	@Inject(method = "<init>(Lnet/minecraft/src/Minecraft;Ljava/io/File;)V", at = @At(value = "TAIL"))
	private void KeyMapping$initTailLoadOpts(Minecraft par1Minecraft, File par2File, CallbackInfo ci) {
		KeyMappings$addKeyBinds();
		loadOptions();
	}
}
