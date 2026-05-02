package EsetKalenko.Halcyon.item.lens;

import EsetKalenko.Halcyon.api.item.ILaserEmitterModule;
import EsetKalenko.Halcyon.block.auxiliary.LaserEmitterBlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

public class BurningLens extends Item implements ILaserEmitterModule {
    public BurningLens(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void applyToMob(LaserEmitterBlockEntity ent, LivingEntity entity) {
        if (entity.getRemainingFireTicks() <= 25) {
            entity.setRemainingFireTicks(25);
        }
        entity.hurt(entity.damageSources().inFire(), 1);
    }

    @Override
    public int getBeamColor() {
        return 0xfc7019;
    }
}
