package EsetKalenko.Halcyon.client.renderers.block;

import EsetKalenko.Halcyon.Halcyon;
import EsetKalenko.Halcyon.api.node.renderers.BaseCapabilityPointRenderer;
import EsetKalenko.Halcyon.block.transmission.FluidPointBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;


public class FluidPointRenderer extends BaseCapabilityPointRenderer<FluidPointBlockEntity> {
    public FluidPointRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super( new Model<>(Halcyon.locate("textures/block/fluid_point.png")), new RelayModel<>(Halcyon.locate("textures/block/fluid_point.png")) );
    }
}