package com.cmdpro.datanessence.client.renderers.block;
import com.cmdpro.databank.model.DatabankModel;
import com.cmdpro.databank.model.DatabankModels;
import com.cmdpro.databank.model.blockentity.DatabankBlockEntityModel;
import com.cmdpro.databank.model.blockentity.DatabankBlockEntityRenderer;
import com.cmdpro.datanessence.DataNEssence;
import com.cmdpro.datanessence.block.processing.FabricatorBlockEntity;
import com.cmdpro.datanessence.client.shaders.MachineOutputShader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

public class FabricatorRenderer extends DatabankBlockEntityRenderer<FabricatorBlockEntity> {
    public FabricatorRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super(new Model());
    }

    @Override
    public void render(FabricatorBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        super.render(pBlockEntity, pPartialTick, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay);

        pPoseStack.pushPose();
        pPoseStack.translate(0.5D, 1.5D, 0.5D);
        pPoseStack.mulPose(Axis.YP.rotationDegrees((pBlockEntity.getLevel().getLevelData().getGameTime() % 360)+pPartialTick));
        pPoseStack.scale(0.75F, 0.75F, 0.75F);
        Minecraft.getInstance().getItemRenderer().renderStatic(pBlockEntity.item, ItemDisplayContext.FIXED, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, pPoseStack, MachineOutputShader.createMachineOutputBufferSource(), pBlockEntity.getLevel(), 0);
        pPoseStack.popPose();
        if (pBlockEntity.isCrafting()) {
            renderCraftingCircle(pBlockEntity, pPartialTick, pPoseStack, pBufferSource);
        }
    }

    private void renderCraftingCircle(FabricatorBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource) {
        pPoseStack.pushPose();

        // around the cube
        pPoseStack.translate(0.5D, 0.9D, 0.5D);

        VertexConsumer vertexConsumer = pBufferSource.getBuffer(RenderType.lightning());

        // Params
        int spiralArms = 2;
        //TODO if it lags or idk does something bad, reduce pointsPerArm
        int pointsPerArm = 128;
        float spiralSpeed = 0.05F;
        float rotationSpeed = 0.5F;
        float time = (pBlockEntity.getLevel().getLevelData().getGameTime() + pPartialTick) * spiralSpeed;
        float rotation = (pBlockEntity.getLevel().getLevelData().getGameTime() + pPartialTick) * rotationSpeed;

        // Color
        float red = 0.6F;
        float green = 0.3F;
        float blue = 1.0F;

        for (int arm = 0; arm < spiralArms; arm++) {
            float armOffset = (float) (2 * Math.PI * arm / spiralArms);

            for (int i = 0; i < pointsPerArm - 1; i++) {
                float t1 = (float) i / pointsPerArm;
                float t2 = (float) (i + 1) / pointsPerArm;

                float wave1 = ((t1 + time) % 1.0F);
                float wave2 = ((t2 + time) % 1.0F);

                if (wave1 > 0.95F) continue;

                float spiralTightness = -1F;
                float angle1 = (wave1 * spiralTightness * (float) Math.PI * 2) + armOffset + rotation;
                float angle2 = (wave2 * spiralTightness * (float) Math.PI * 2) + armOffset + rotation;

                float minRadius = 0.25F;
                float maxRadius = 1F;
                float radius1 = minRadius + wave1 * (maxRadius - minRadius);
                float radius2 = minRadius + wave2 * (maxRadius - minRadius);

                float x1 = (float) Math.cos(angle1) * radius1;
                float z1 = (float) Math.sin(angle1) * radius1;
                float x2 = (float) Math.cos(angle2) * radius2;
                float z2 = (float) Math.sin(angle2) * radius2;

                float alpha1 = (1.0F - wave1) * (1.0F - wave1);
                float alpha2 = (1.0F - wave2) * (1.0F - wave2);

                alpha1 *= (0.5F + 0.5F * (1.0F - wave1));
                alpha2 *= (0.5F + 0.5F * (1.0F - wave2));

                float height = 0.2F * (1.0F - wave1 * 0.5F);

                // twists the thing
                float twistAngle = wave1 * (float) Math.PI * 4.0F; // 2 full rotations


                float radialX = (float) Math.cos(angle1);
                float radialZ = (float) Math.sin(angle1);

                float upY = height / 2;

                float cosTwist = (float) Math.cos(twistAngle);
                float sinTwist = (float) Math.sin(twistAngle);

                float tangentX = -radialZ;
                float tangentZ = radialX;

                float up1X = tangentX * sinTwist * upY;
                float up1Y = cosTwist * upY;
                float up1Z = tangentZ * sinTwist * upY;

                float down1X = -up1X;
                float down1Y = -up1Y;
                float down1Z = -up1Z;

                pPoseStack.pushPose();
                PoseStack.Pose pose = pPoseStack.last();

                vertexConsumer.addVertex(pose, x1 + down1X, down1Y, z1 + down1Z)
                        .setColor(red, green, blue, alpha1)
                        .setNormal(pose, radialX, 0.0F, radialZ);
                vertexConsumer.addVertex(pose, x2 + down1X, down1Y, z2 + down1Z)
                        .setColor(red, green, blue, alpha2)
                        .setNormal(pose, radialX, 0.0F, radialZ);
                vertexConsumer.addVertex(pose, x2 + up1X, up1Y, z2 + up1Z)
                        .setColor(red, green, blue, alpha2 * 0.5F)
                        .setNormal(pose, radialX, 0.0F, radialZ);
                vertexConsumer.addVertex(pose, x1 + up1X, up1Y, z1 + up1Z)
                        .setColor(red, green, blue, alpha1 * 0.5F)
                        .setNormal(pose, radialX, 0.0F, radialZ);

                // Back face
                vertexConsumer.addVertex(pose, x1 + down1X, down1Y, z1 + down1Z)
                        .setColor(red, green, blue, alpha1)
                        .setNormal(pose, -radialX, 0.0F, -radialZ);
                vertexConsumer.addVertex(pose, x1 + up1X, up1Y, z1 + up1Z)
                        .setColor(red, green, blue, alpha1 * 0.5F)
                        .setNormal(pose, -radialX, 0.0F, -radialZ);
                vertexConsumer.addVertex(pose, x2 + up1X, up1Y, z2 + up1Z)
                        .setColor(red, green, blue, alpha2 * 0.5F)
                        .setNormal(pose, -radialX, 0.0F, -radialZ);
                vertexConsumer.addVertex(pose, x2 + down1X, down1Y, z2 + down1Z)
                        .setColor(red, green, blue, alpha2)
                        .setNormal(pose, -radialX, 0.0F, -radialZ);

                pPoseStack.popPose();
            }
        }

    }

    public static class Model extends DatabankBlockEntityModel<FabricatorBlockEntity> {
        public DatabankModel model;
        public DatabankModel getModel() {
            if (model == null) {
                model = DatabankModels.models.get(DataNEssence.locate("fabricator"));
            }
            return model;
        }
        @Override
        public ResourceLocation getTextureLocation() {
            return DataNEssence.locate("textures/block/fabricator.png");
        }
        @Override
        public void setupModelPose(FabricatorBlockEntity pEntity, float partialTick) {
            pEntity.animState.updateAnimDefinitions(getModel());
            if (!pEntity.item.isEmpty()) {
                pEntity.animState.setAnim("ready");
            } else {
                pEntity.animState.setAnim("idle");
            }
            animate(pEntity.animState);
        }
    }
}