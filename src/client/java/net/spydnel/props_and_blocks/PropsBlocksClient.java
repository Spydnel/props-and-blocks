package net.spydnel.props_and_blocks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.spydnel.props_and_blocks.block.LargeCandleBlock;
import net.spydnel.props_and_blocks.registry.ModBlocks;

public class PropsBlocksClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
				ModBlocks.LARGE_CANDLE,
				ModBlocks.LARGE_WHITE_CANDLE,
				ModBlocks.LARGE_LIGHT_GRAY_CANDLE,
				ModBlocks.LARGE_GRAY_CANDLE,
				ModBlocks.LARGE_BLACK_CANDLE);

	}
}