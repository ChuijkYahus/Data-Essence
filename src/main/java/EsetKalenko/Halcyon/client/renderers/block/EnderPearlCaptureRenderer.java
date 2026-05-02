package EsetKalenko.Halcyon.client.renderers.block;

import com.cmdpro.databank.model.DatabankModel;
import com.cmdpro.databank.model.DatabankModels;
import com.cmdpro.databank.model.blockentity.DatabankBlockEntityModel;
import com.cmdpro.databank.model.blockentity.DatabankBlockEntityRenderer;
import EsetKalenko.Halcyon.DataNEssence;
import EsetKalenko.Halcyon.api.pearlnetwork.PearlNetworkBlockRenderHelper;
import EsetKalenko.Halcyon.block.transportation.EnderPearlCapture;
import EsetKalenko.Halcyon.block.transportation.EnderPearlCaptureBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EnderPearlCaptureRenderer extends DatabankBlockEntityRenderer<EnderPearlCaptureBlockEntity> implements PearlNetworkBlockRenderHelper {
    public EnderPearlCaptureRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super(new Model());
    }

    @Override
    public void render(EnderPearlCaptureBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();
        Direction facing = pBlockEntity.getBlockState().getValue(EnderPearlCapture.FACING);
        rotateStack(facing, pPoseStack);
        super.render(pBlockEntity, pPartialTick, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay);
        pPoseStack.popPose();
        renderPearlConnections(pBlockEntity, pPoseStack);
    }
    @Override
    public AABB getRenderBoundingBox(EnderPearlCaptureBlockEntity blockEntity) {
        return AABB.INFINITE;
    }
    public void rotateStack(Direction facing, PoseStack poseStack) {
        Vec3 rotateAround = new Vec3(0.5, 0.5, 0.5);
        if (facing.equals(Direction.DOWN)) {
            poseStack.rotateAround(Axis.XP.rotationDegrees(180), (float)rotateAround.x, (float)rotateAround.y, (float)rotateAround.z);
        }
        if (facing.equals(Direction.NORTH)) {
            poseStack.rotateAround(Axis.XP.rotationDegrees(-90), (float)rotateAround.x, (float)rotateAround.y, (float)rotateAround.z);
        }
        if (facing.equals(Direction.SOUTH)) {
            poseStack.rotateAround(Axis.XP.rotationDegrees(90), (float)rotateAround.x, (float)rotateAround.y, (float)rotateAround.z);
        }
        if (facing.equals(Direction.EAST)) {
            poseStack.rotateAround(Axis.ZP.rotationDegrees(-90), (float)rotateAround.x, (float)rotateAround.y, (float)rotateAround.z);
        }
        if (facing.equals(Direction.WEST)) {
            poseStack.rotateAround(Axis.ZP.rotationDegrees(90), (float)rotateAround.x, (float)rotateAround.y, (float)rotateAround.z);
        }
    }

    public static class Model extends DatabankBlockEntityModel<EnderPearlCaptureBlockEntity> {
        public DatabankModel model;
        public DatabankModel getModel() {
            if (model == null) {
                model = DatabankModels.models.get(DataNEssence.locate("ender_pearl_capture"));
            }
            return model;
        }

        @Override
        public ResourceLocation getTextureLocation() {
            return DataNEssence.locate("textures/block/ender_pearl_capture.png");
        }

        @Override
        public void setupModelPose(EnderPearlCaptureBlockEntity pEntity, float partialTick) {
            pEntity.animState.updateAnimDefinitions(getModel());
            animate(pEntity.animState);
        }
    }
}