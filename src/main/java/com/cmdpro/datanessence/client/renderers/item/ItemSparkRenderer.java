package com.cmdpro.datanessence.client.renderers.item;

import com.cmdpro.datanessence.api.item.ItemEssenceContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.inventory.Slot;
import org.joml.Matrix4f;
import com.cmdpro.datanessence.registry.ItemRegistry;
import com.cmdpro.datanessence.DataNEssence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@EventBusSubscriber(modid = "datanessence", value = Dist.CLIENT)
public class ItemSparkRenderer {

    private static final Random RANDOM = new Random();
    private static final HashMap<Slot, ItemSparkSet> slotSparks = new HashMap<>();
    private static final List<TrailSpark> trailSparks = new ArrayList<>();
    private static float lastMouseX = 0;
    private static float lastMouseY = 0;

    private static Set<Item> sparkingItems = null;

    private static Set<Item> getSparkingItems() {
        if (sparkingItems == null) {
            sparkingItems = new HashSet<>();
            sparkingItems.add(ItemRegistry.ILLUMINATION_ROD.get().asItem());
            sparkingItems.add(ItemRegistry.ESSENCE_SWORD.get().asItem());
            sparkingItems.add(ItemRegistry.PRIMITIVE_ANTI_GRAVITY_PACK.get().asItem());
            sparkingItems.add(ItemRegistry.GRAPPLING_HOOK.get().asItem());
            sparkingItems.add(ItemRegistry.TRANS_GRAPPLING_HOOK.get().asItem());
            sparkingItems.add(ItemRegistry.ORE_SCANNER.get().asItem());
            sparkingItems.add(ItemRegistry.ESSENCE_SHARD.get().asItem());

        }
        return sparkingItems;
    }

    @SubscribeEvent
    public static void onContainerScreenRenderBackground(ContainerScreenEvent.Render.Background event) {
        AbstractContainerScreen<?> containerScreen = event.getContainerScreen();
        PoseStack poseStack = event.getGuiGraphics().pose();

        slotSparks.entrySet().removeIf(entry -> {
            entry.getValue().age++;
            return entry.getValue().age > 10;
        });

        for (Slot slot : containerScreen.getMenu().slots) {
            ItemStack stack = slot.getItem();

            if (shouldItemHaveSparks(stack)) {
                float essencePercent = getEssencePercentage(stack);

                if (essencePercent > 0.01f) {
                    int x = containerScreen.getGuiLeft() + slot.x;
                    int y = containerScreen.getGuiTop() + slot.y;

                    ItemSparkSet sparkSet = slotSparks.computeIfAbsent(slot, s -> new ItemSparkSet());
                    sparkSet.age = 0;
                    sparkSet.setEssencePercent(essencePercent);
                    sparkSet.update();
                    sparkSet.render(poseStack, x, y, essencePercent);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onScreenRenderPost(ScreenEvent.Render.Post event) {
        if (!(event.getScreen() instanceof AbstractContainerScreen<?> containerScreen)) {
            return;
        }

        // Get the dragged item
        ItemStack carriedStack = containerScreen.getMenu().getCarried();

        if (shouldItemHaveSparks(carriedStack)) {
            float essencePercent = getEssencePercentage(carriedStack);

            if (essencePercent > 0.01f) {
                PoseStack poseStack = event.getGuiGraphics().pose();

                // Get mouse position
                double mouseX = event.getMouseX();
                double mouseY = event.getMouseY();

                // movement
                float deltaX = (float) mouseX - lastMouseX;
                float deltaY = (float) mouseY - lastMouseY;
                float movement = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                // Spawn trail sparks
                if (movement > 0.1f) {
                    float spawnChance = essencePercent * 0.9f;

                    if (RANDOM.nextFloat() < spawnChance) {
                        // Spawn at mouse position with slight offset behind movement direction
                        float offsetX = -deltaX * 0.3f + (RANDOM.nextFloat() - 0.5f) * 5;
                        float offsetY = -deltaY * 0.3f + (RANDOM.nextFloat() - 0.5f) * 5;

                        trailSparks.add(new TrailSpark(
                                (float) mouseX + offsetX,
                                (float) mouseY + offsetY,
                                essencePercent
                        ));
                    }
                }

                lastMouseX = (float) mouseX;
                lastMouseY = (float) mouseY;

                // Update and render trail sparks
                trailSparks.removeIf(TrailSpark::isDead);
                renderTrailSparks(poseStack);
            }
        } else {

            trailSparks.clear();
        }
    }

    private static void renderTrailSparks(PoseStack poseStack) {
        if (trailSparks.isEmpty()) return;

        poseStack.pushPose();
        poseStack.translate(0, 0, 300);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();

        Matrix4f matrix = poseStack.last().pose();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        for (TrailSpark spark : trailSparks) {
            spark.render(builder, matrix);
        }

        BufferUploader.drawWithShader(builder.buildOrThrow());

        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();

        poseStack.popPose();
    }

    private static boolean shouldItemHaveSparks(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return getSparkingItems().contains(stack.getItem());
    }

    private static float getEssencePercentage(ItemStack stack) {
        float maxEssence = ItemEssenceContainer.getMaxEssence(stack);
        if (maxEssence <= 0) return 0.0f;

        float currentEssence = ItemEssenceContainer.getEssence(stack, DataNEssence.locate("essence"));
        return Math.min(1.0f, (float) currentEssence / maxEssence);
    }

    private static class ItemSparkSet {
        private final List<Spark> sparks = new ArrayList<>();
        private int age = 0;
        private float essencePercent = 0.0f;

        public void setEssencePercent(float percent) {
            this.essencePercent = percent;
        }

        public void update() {
            sparks.removeIf(Spark::isDead);

            // Spawn rate increases with essence level
            float spawnChance = essencePercent * 0.6f;

            if (RANDOM.nextFloat() < spawnChance) {
                float startRadius = RANDOM.nextFloat() * 8.0f;
                float startAngle = RANDOM.nextFloat() * (float) Math.PI * 2;
                float angle = RANDOM.nextFloat() * (float) Math.PI * 2;
                sparks.add(new Spark(angle, startRadius, startAngle));
            }
        }

        public void render(PoseStack poseStack, int x, int y, float essencePercent) {
            if (sparks.isEmpty()) return;

            poseStack.pushPose();
            poseStack.translate(0, 0, 0);

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.disableDepthTest();
            RenderSystem.disableCull();

            Matrix4f matrix = poseStack.last().pose();

            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder builder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

            for (Spark spark : sparks) {
                spark.render(builder, matrix, x, y, essencePercent);
            }

            BufferUploader.drawWithShader(builder.buildOrThrow());

            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();

            poseStack.popPose();
        }
    }

    private static class Spark {
        private float distance;
        private final float angle;
        private final float speed;
        private int age;
        private final int maxAge;
        private final float size;
        private final float[] color;
        private final float glowSize;
        private final float startRadius;
        private final float startAngle;

        public Spark(float angle, float startRadius, float startAngle) {
            this.angle = angle;
            this.startRadius = startRadius;
            this.startAngle = startAngle;
            this.distance = 0;
            this.speed = 0.05f + RANDOM.nextFloat() * 0.05f;
            this.maxAge = 40 + RANDOM.nextInt(40);
            this.size = 0.3f + RANDOM.nextFloat() * 0.3f;
            this.glowSize = 3.0f + RANDOM.nextFloat() * 3.0f;
            this.age = 0;

            // Purple colors
            float colorVariation = RANDOM.nextFloat() * 0.2f;
            this.color = new float[]{
                    0.6f + colorVariation,  // Red
                    0.3f + colorVariation,  // Green
                    1.0f                    // Blue
            };
        }

        public void render(BufferBuilder builder, Matrix4f matrix, int itemX, int itemY, float essencePercent) {
            age++;
            distance += speed;

            float centerX = itemX + 8;
            float centerY = itemY + 8;

            float startX = (float) Math.cos(startAngle) * startRadius;
            float startY = (float) Math.sin(startAngle) * startRadius;

            float offsetX = (float) Math.cos(angle) * distance;
            float offsetY = (float) Math.sin(angle) * distance;

            float renderX = centerX + startX + offsetX;
            float renderY = centerY + startY + offsetY;

            float lifeFactor = 1.0f - ((float) age / maxAge);
            float baseAlpha = lifeFactor * lifeFactor * lifeFactor;
            float alpha = baseAlpha * essencePercent;

            float pulse = (float) Math.sin(age * 0.1f) * 0.15f + 0.85f;
            float brightness = lifeFactor * pulse * essencePercent;

            float r = color[0] * brightness;
            float g = color[1] * brightness;
            float b = color[2] * brightness;

            int segments = 12;

            float outerGlow = glowSize * 1.5f;
            float outerAlpha = alpha * 0.15f;
            drawCircle(builder, matrix, renderX, renderY, outerGlow, r, g, b, outerAlpha, segments);

            float midAlpha = alpha * 0.35f;
            drawCircle(builder, matrix, renderX, renderY, glowSize, r, g, b, midAlpha, segments);

            float innerGlow = glowSize * 0.5f;
            float innerAlpha = alpha * 0.6f;
            drawCircle(builder, matrix, renderX, renderY, innerGlow, r, g, b, innerAlpha, segments);

            float coreSize = size * 2;
            drawCircle(builder, matrix, renderX, renderY, coreSize, 1.0f, 0.9f, 1.0f, alpha * 0.9f, segments);

            drawCircle(builder, matrix, renderX, renderY, size, 1.0f, 1.0f, 1.0f, alpha, segments);
        }

        private void drawCircle(BufferBuilder builder, Matrix4f matrix, float centerX, float centerY,
                                float radius, float r, float g, float b, float alpha, int segments) {
            for (int i = 0; i < segments; i++) {
                float angle1 = (float) (2 * Math.PI * i / segments);
                float angle2 = (float) (2 * Math.PI * (i + 1) / segments);

                float x1 = centerX + (float) Math.cos(angle1) * radius;
                float y1 = centerY + (float) Math.sin(angle1) * radius;
                float x2 = centerX + (float) Math.cos(angle2) * radius;
                float y2 = centerY + (float) Math.sin(angle2) * radius;

                builder.addVertex(matrix, centerX, centerY, 0)
                        .setColor(r, g, b, alpha);
                builder.addVertex(matrix, x1, y1, 0)
                        .setColor(r, g, b, 0.0f);
                builder.addVertex(matrix, x2, y2, 0)
                        .setColor(r, g, b, 0.0f);
                builder.addVertex(matrix, centerX, centerY, 0)
                        .setColor(r, g, b, alpha);
            }
        }

        public boolean isDead() {
            return age >= maxAge || distance > 10;
        }
    }


    private static class TrailSpark {
        private final float x;
        private final float y;
        private int age;
        private final int maxAge;
        private final float size;
        private final float[] color;
        private final float glowSize;
        private final float essencePercent;

        public TrailSpark(float x, float y, float essencePercent) {
            this.x = x;
            this.y = y;
            this.essencePercent = essencePercent;
            this.age = 0;
            this.maxAge = 20 + RANDOM.nextInt(20); // Shorter lifetime for trail
            this.size = 0.3f + RANDOM.nextFloat() * 0.3f;
            this.glowSize = 2.0f + RANDOM.nextFloat() * 2.0f;

            // Purple colors
            float colorVariation = RANDOM.nextFloat() * 0.2f;
            this.color = new float[]{
                    0.6f + colorVariation,  // Red
                    0.3f + colorVariation,  // Green
                    1.0f                    // Blue
            };
        }

        public void render(BufferBuilder builder, Matrix4f matrix) {
            age++;

            float lifeFactor = 1.0f - ((float) age / maxAge);
            float baseAlpha = lifeFactor * lifeFactor;
            float alpha = baseAlpha * essencePercent;

            float brightness = lifeFactor * essencePercent;

            float r = color[0] * brightness;
            float g = color[1] * brightness;
            float b = color[2] * brightness;

            int segments = 12;

            // Outer glow
            float outerAlpha = alpha * 0.2f;
            drawCircle(builder, matrix, x, y, glowSize, r, g, b, outerAlpha, segments);

            // Inner glow
            float innerGlow = glowSize * 0.5f;
            float innerAlpha = alpha * 0.5f;
            drawCircle(builder, matrix, x, y, innerGlow, r, g, b, innerAlpha, segments);

            // Center
            drawCircle(builder, matrix, x, y, size, 1.0f, 1.0f, 1.0f, alpha, segments);
        }

        private void drawCircle(BufferBuilder builder, Matrix4f matrix, float centerX, float centerY,
                                float radius, float r, float g, float b, float alpha, int segments) {
            for (int i = 0; i < segments; i++) {
                float angle1 = (float) (2 * Math.PI * i / segments);
                float angle2 = (float) (2 * Math.PI * (i + 1) / segments);

                float x1 = centerX + (float) Math.cos(angle1) * radius;
                float y1 = centerY + (float) Math.sin(angle1) * radius;
                float x2 = centerX + (float) Math.cos(angle2) * radius;
                float y2 = centerY + (float) Math.sin(angle2) * radius;

                builder.addVertex(matrix, centerX, centerY, 0)
                        .setColor(r, g, b, alpha);
                builder.addVertex(matrix, x1, y1, 0)
                        .setColor(r, g, b, 0.0f);
                builder.addVertex(matrix, x2, y2, 0)
                        .setColor(r, g, b, 0.0f);
                builder.addVertex(matrix, centerX, centerY, 0)
                        .setColor(r, g, b, alpha);
            }
        }

        public boolean isDead() {
            return age >= maxAge;
        }
    }
}