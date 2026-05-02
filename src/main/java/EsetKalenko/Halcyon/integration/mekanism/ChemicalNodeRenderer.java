package EsetKalenko.Halcyon.integration.mekanism;

import EsetKalenko.Halcyon.DataNEssence;
import EsetKalenko.Halcyon.api.node.renderers.BaseCapabilityPointRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class ChemicalNodeRenderer extends BaseCapabilityPointRenderer<ChemicalNodeBlockEntity> {
    public ChemicalNodeRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super( new Model<>(DataNEssence.locate("textures/block/chemical_node.png")), new RelayModel<>(DataNEssence.locate("textures/block/chemical_node.png")) );
    }
}
