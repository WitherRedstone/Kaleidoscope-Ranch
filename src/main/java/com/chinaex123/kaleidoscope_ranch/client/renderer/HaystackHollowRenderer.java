package com.chinaex123.kaleidoscope_ranch.client.renderer;

import com.chinaex123.kaleidoscope_ranch.blockentity.HaystackHollowBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class HaystackHollowRenderer implements BlockEntityRenderer<HaystackHollowBlockEntity> {

    public HaystackHollowRenderer(BlockEntityRendererProvider.Context context) {}

    private static final float ITEM_SCALE = 0.45f;
    private static final float X_ROTATION = -90f;

    // 渲染区域（贴近方块边缘，大蛋也能铺满）
    private static final float MIN_XZ = 0.12f;
    private static final float MAX_XZ = 0.88f;

    // 底层起始高度
    private static final float BASE_Y = 0.08f;

    // 竖直堆叠时每颗的间距（紧贴）
    private static final float STACK_HEIGHT = 0.075f;

    private static final int COLS = 3;
    private static final int GRID_COUNT = COLS * COLS;
    private static final int STACK_LAYERS = 2;
    private static final int MAX_RENDER = 16;
    private static final int MAX_ITEMS = 144;

    private static final int[] GRID_ORDER = {4, 1, 3, 5, 7, 0, 2, 6, 8};

    // 平面散乱程度（格子大、蛋大，偏移小一点避免穿模）
    private static final float JITTER = 0.05f;

    // 每层相对下一层的水平错位幅度
    private static final float LAYER_SCATTER = 0.06f;

    // 每层 Y 高度随机浮动
    private static final float LAYER_Y_JITTER = 0.012f;

    @Override
    public void render(HaystackHollowBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (!blockEntity.hasItems()) {
            return;
        }

        int totalItems = blockEntity.getTotalItemCount();
        if (totalItems == 0) return;

        int eggCount = Math.min(MAX_RENDER, (int) Math.ceil(totalItems * (double) MAX_RENDER / MAX_ITEMS));

        ItemStack eggStack = new ItemStack(Items.EGG);
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        RandomSource random = RandomSource.create(blockEntity.getBlockPos().asLong());

        float cellW = (MAX_XZ - MIN_XZ) / COLS;

        int rendered = 0;

        for (int layer = 0; layer < STACK_LAYERS && rendered < eggCount; layer++) {
            for (int orderIdx = 0; orderIdx < GRID_COUNT && rendered < eggCount; orderIdx++) {
                int gridIndex = GRID_ORDER[orderIdx];
                int gridX = gridIndex % COLS;
                int gridZ = gridIndex / COLS;

                float baseX = MIN_XZ + cellW * (gridX + 0.5f) + (random.nextFloat() - 0.5f) * JITTER;
                float baseZ = MIN_XZ + cellW * (gridZ + 0.5f) + (random.nextFloat() - 0.5f) * JITTER;

                baseX = Math.clamp(baseX, MIN_XZ, MAX_XZ);
                baseZ = Math.clamp(baseZ, MIN_XZ, MAX_XZ);

                float baseYaw = random.nextFloat() * 360f;
                float baseTilt = (random.nextFloat() - 0.5f) * 12f;

                float offsetX = (random.nextFloat() - 0.5f) * LAYER_SCATTER;
                float offsetZ = (random.nextFloat() - 0.5f) * LAYER_SCATTER;

                float y = BASE_Y + layer * STACK_HEIGHT
                        + (random.nextFloat() - 0.5f) * LAYER_Y_JITTER;

                float yaw = baseYaw + (random.nextFloat() - 0.5f) * 60f;
                float tilt = baseTilt + (random.nextFloat() - 0.5f) * 10f;

                poseStack.pushPose();
                poseStack.translate(
                        Math.clamp(baseX + offsetX, MIN_XZ, MAX_XZ),
                        y,
                        Math.clamp(baseZ + offsetZ, MIN_XZ, MAX_XZ)
                );
                poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
                poseStack.mulPose(Axis.XP.rotationDegrees(X_ROTATION + tilt));
                poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);

                itemRenderer.renderStatic(eggStack, ItemDisplayContext.FIXED,
                        packedLight, OverlayTexture.NO_OVERLAY,
                        poseStack, bufferSource, blockEntity.getLevel(), 0);

                poseStack.popPose();
                rendered++;
            }
        }
    }
}