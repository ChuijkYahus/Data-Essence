package EsetKalenko.Halcyon.registry;

import EsetKalenko.Halcyon.DataNEssence;
import EsetKalenko.Halcyon.effects.GenderEuphoria;
import EsetKalenko.Halcyon.effects.Shrunken;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MobEffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT,
            DataNEssence.MOD_ID);

    public static DeferredHolder<MobEffect, GenderEuphoria> GENDER_EUPHORIA = register("gender_euphoria", () ->
            new GenderEuphoria(MobEffectCategory.BENEFICIAL, 0xFFFFFF));
    public static DeferredHolder<MobEffect, Shrunken> SHRUNKEN = register("shrunken", () ->
            new Shrunken(MobEffectCategory.NEUTRAL, 0xFFFFFF));
    private static <T extends MobEffect> DeferredHolder<MobEffect, T> register(final String name, final Supplier<T> effect) {
        return MOB_EFFECTS.register(name, effect);
    }
}
