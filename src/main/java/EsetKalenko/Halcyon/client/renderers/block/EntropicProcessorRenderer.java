package EsetKalenko.Halcyon.client.renderers.block;

import EsetKalenko.Halcyon.Halcyon;
import com.cmdpro.databank.model.DatabankModel;
import com.cmdpro.databank.model.DatabankModels;
import com.cmdpro.databank.model.blockentity.DatabankBlockEntityModel;
import com.cmdpro.databank.model.blockentity.DatabankBlockEntityRenderer;
import EsetKalenko.Halcyon.block.processing.EntropicProcessor;
import EsetKalenko.Halcyon.block.processing.EntropicProcessorBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class EntropicProcessorRenderer extends DatabankBlockEntityRenderer<EntropicProcessorBlockEntity>{
    public EntropicProcessorRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super(new Model());
    }

    @Override
    public void render(EntropicProcessorBlockEntity entropicProcessor, float pPartialTick, PoseStack poseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        Direction facing = entropicProcessor.getBlockState().getValue(EntropicProcessor.FACING);
        Vec3 rotateAround = new Vec3(0.5, 0.5, 0.5);

        if (facing.equals(Direction.NORTH))
            poseStack.rotateAround(Axis.YP.rotationDegrees(0), (float) rotateAround.x, (float) rotateAround.y, (float) rotateAround.z);
        if (facing.equals(Direction.SOUTH))
            poseStack.rotateAround(Axis.YP.rotationDegrees(180), (float) rotateAround.x, (float) rotateAround.y, (float) rotateAround.z);
        if (facing.equals(Direction.EAST))
            poseStack.rotateAround(Axis.YP.rotationDegrees(-90), (float) rotateAround.x, (float) rotateAround.y, (float) rotateAround.z);
        if (facing.equals(Direction.WEST))
            poseStack.rotateAround(Axis.YP.rotationDegrees(90), (float) rotateAround.x, (float) rotateAround.y, (float) rotateAround.z);

        super.render(entropicProcessor, pPartialTick, poseStack, pBufferSource, pPackedLight, pPackedOverlay);
    }


    public static class Model extends DatabankBlockEntityModel<EntropicProcessorBlockEntity> {
        public DatabankModel model;
        public DatabankModel getModel() {
            if (model == null) {
                model = DatabankModels.models.get(Halcyon.locate("entropic_processor"));
            }
            return model;
        }
        @Override
        public ResourceLocation getTextureLocation() {
            return Halcyon.locate("textures/block/entropic_processor.png");
        }

        @Override
        public void setupModelPose(EntropicProcessorBlockEntity pEntity, float partialTick) {
            pEntity.animState.updateAnimDefinitions(getModel());
            if (pEntity.workTime >= 0) {
                pEntity.animState.setAnim("working");
            } else {
                pEntity.animState.setAnim("idle");
            }
            animate(pEntity.animState);
        }
    }
}
