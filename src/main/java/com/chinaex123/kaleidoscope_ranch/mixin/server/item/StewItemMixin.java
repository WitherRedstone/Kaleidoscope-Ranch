package com.chinaex123.kaleidoscope_ranch.mixin.server.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class StewItemMixin {

    @Inject(method = "finishUsingItem", at = @At("RETURN"))
    private void KR$applyStewEffects(ItemStack stack, Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
        if (level.isClientSide()) return;
        Item self = (Item) (Object) this;
        if (self != Items.MUSHROOM_STEW) return;
        SuspiciousStewEffects effects = stack.get(DataComponents.SUSPICIOUS_STEW_EFFECTS);
        if (effects != null) {
            for (SuspiciousStewEffects.Entry entry : effects.effects()) {
                entity.addEffect(new MobEffectInstance(entry.effect(), entry.duration()));
            }
        }
    }
}