package EsetKalenko.Halcyon.client.renderers.block;

import EsetKalenko.Halcyon.Halcyon;
import EsetKalenko.Halcyon.api.node.renderers.BaseEssencePointRenderer;
import EsetKalenko.Halcyon.block.transmission.LunarEssencePointBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;


public class LunarEssencePointRenderer extends BaseEssencePointRenderer<LunarEssencePointBlockEntity> {
    public LunarEssencePointRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super( new Model<>(Halcyon.locate("textures/block/lunar_node.png")), new RelayModel(Halcyon.locate("textures/block/lunar_node.png")) );
    }
}