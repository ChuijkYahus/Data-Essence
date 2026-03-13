package com.cmdpro.datanessence.registry;

import com.cmdpro.datanessence.DataNEssence;
import com.cmdpro.datanessence.progression.advancement.FabricatorRecipeCraftedTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HalcyonCriteria {

    private static final DeferredRegister<CriterionTrigger<?>> REGISTER = DeferredRegister.create(
            Registries.TRIGGER_TYPE, DataNEssence.MOD_ID
    );

    public static FabricatorRecipeCraftedTrigger FABRICATOR_RECIPE_CRAFTED = new FabricatorRecipeCraftedTrigger();

    public static void register(IEventBus bus) {
        REGISTER.register(FabricatorRecipeCraftedTrigger.ID.getPath(), () -> FABRICATOR_RECIPE_CRAFTED);
    }
}
