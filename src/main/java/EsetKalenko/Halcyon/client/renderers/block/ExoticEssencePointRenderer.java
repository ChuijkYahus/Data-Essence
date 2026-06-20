package EsetKalenko.Halcyon.client.renderers.block;

import EsetKalenko.Halcyon.Halcyon;
import EsetKalenko.Halcyon.api.node.renderers.BaseEssencePointRenderer;
import EsetKalenko.Halcyon.block.transmission.ExoticEssencePointBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;


public class ExoticEssencePointRenderer extends BaseEssencePointRenderer<ExoticEssencePointBlockEntity> {
    public ExoticEssencePointRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super( new Model<>(Halcyon.locate("textures/block/exotic_essence_point.png")), new RelayModel(Halcyon.locate("textures/block/exotic_essence_point.png")) );
    }
}