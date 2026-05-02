package EsetKalenko.Halcyon.item.lens;

import EsetKalenko.Halcyon.api.item.ILaserEmitterModule;
import EsetKalenko.Halcyon.block.auxiliary.LaserEmitterBlockEntity;
import EsetKalenko.Halcyon.registry.DamageTypeRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

public class HarmingLens extends Item implements ILaserEmitterModule {
    public HarmingLens(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void applyToMob(LaserEmitterBlockEntity ent, LivingEntity entity) {
        entity.hurt(entity.damageSources().source(DamageTypeRegistry.laser), 2.5f);
    }

    @Override
    public int getBeamColor() {
        return 0x381657;
    }
}
