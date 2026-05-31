package net.dravigen.bu_toolkit.mixin.clientServer;

import net.dravigen.bu_toolkit.api.CustomTooltip;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(ItemStack.class)
public abstract class ItemStackMx {
	@Shadow
	public abstract Item getItem();
	
	@Inject(method = "getTooltip", at = @At(value = "RETURN"))
	private void addCustomToolTipAttribute(EntityPlayer par1EntityPlayer, boolean par2, CallbackInfoReturnable<List> cir) {
		if (this.getItem() instanceof CustomTooltip customTooltip) {
			List tooltips = cir.getReturnValue();
			
			Map<String, String[]> var15 = customTooltip.getTooltip();
			
			if (!var15.isEmpty()) {
				for (Map.Entry<String, String[]> var19 : var15.entrySet()) {
					String[] baseValues = var19.getValue();
					String[] values = new String[baseValues.length - 1];
					
					System.arraycopy(baseValues, 1, values, 0, baseValues.length - 1);
					
					tooltips.add(tooltips.size() - 1, I18n.getStringParams(baseValues[0], (Object[]) values));
				}
			}
		}
	}
}
