package net.spydnel.props_and_blocks.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.spydnel.props_and_blocks.PropsBlocks;
import net.spydnel.props_and_blocks.block.LargeCandleBlock;

import static net.minecraft.block.Blocks.createLightLevelFromLitBlockState;

public class ModBlocks {

    private static LargeCandleBlock createLargeCandleBlock(MapColor color) {
        return new LargeCandleBlock(AbstractBlock.Settings.create()
                .mapColor(color)
                .nonOpaque().strength(0.1F)
                .sounds(BlockSoundGroup.CANDLE)
                .solidBlock(Blocks::never)
                .luminance(createLightLevelFromLitBlockState(15)));
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(PropsBlocks.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(PropsBlocks.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static final Block LARGE_CANDLE = registerBlock("large_candle", createLargeCandleBlock(MapColor.PALE_YELLOW));
    public static final Block LARGE_WHITE_CANDLE = registerBlock("large_white_candle", createLargeCandleBlock(MapColor.WHITE_GRAY));
    public static final Block LARGE_LIGHT_GRAY_CANDLE = registerBlock("large_light_gray_candle", createLargeCandleBlock(MapColor.LIGHT_GRAY));
    public static final Block LARGE_GRAY_CANDLE = registerBlock("large_gray_candle", createLargeCandleBlock(MapColor.GRAY));
    public static final Block LARGE_BLACK_CANDLE = registerBlock("large_black_candle", createLargeCandleBlock(MapColor.BLACK));

    public static void registerModBlocks() {

    }
}
