package net.rezolv.obsidanum.effect.effects;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.rezolv.obsidanum.sound.SoundsObs;


public class Confusion extends MobEffect {
    public Confusion(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        // Проверяем, активен ли эффект, и воспроизводим звук
//        if (pLivingEntity.hasEffect(this)) {
//            pLivingEntity.level().playSound(
//                    null,
//                    pLivingEntity.blockPosition(),
//                    SoundsObs.FLASH.get(),
//                    pLivingEntity.getSoundSource(),
//                    1.0F,
//                    1.0F
//            );
//        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        // Проверяем эффект каждый тик
        return true;
    }
}
