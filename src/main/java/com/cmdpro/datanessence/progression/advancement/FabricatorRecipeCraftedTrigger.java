package com.cmdpro.datanessence.progression.advancement;

import com.cmdpro.datanessence.DataNEssence;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class FabricatorRecipeCraftedTrigger extends SimpleCriterionTrigger<FabricatorRecipeCraftedTrigger.Conditions> {

    public static final ResourceLocation ID = DataNEssence.locate("fabricator_recipe_crafted");

    public void trigger(ServerPlayer player, ItemStack craftedStack) {
        this.trigger(player, (conditions -> conditions.matches(craftedStack)));
    }

    @Override
    public Codec<Conditions> codec() {
        return FabricatorRecipeCraftedTrigger.Conditions.CODEC;
    }

    public record Conditions (
            Optional<ContextAwarePredicate> player,
            Optional<ContextAwarePredicate> location,
            Optional<ItemPredicate> craftedItemPredicate
    ) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<FabricatorRecipeCraftedTrigger.Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player")
                                .forGetter(FabricatorRecipeCraftedTrigger.Conditions::player),
                        ContextAwarePredicate.CODEC.optionalFieldOf("location")
                                .forGetter(FabricatorRecipeCraftedTrigger.Conditions::location),
                        ItemPredicate.CODEC.optionalFieldOf("item")
                                .forGetter(FabricatorRecipeCraftedTrigger.Conditions::craftedItemPredicate)
                ).apply(instance, FabricatorRecipeCraftedTrigger.Conditions::new)
        );

        @Override
        public void validate(CriterionValidator validator) {
            SimpleCriterionTrigger.SimpleInstance.super.validate(validator);
        }

        public boolean matches(ItemStack craftedStack) {
            return this.craftedItemPredicate.isEmpty() || this.craftedItemPredicate.get()
                    .test(craftedStack);
        }

    }
}
