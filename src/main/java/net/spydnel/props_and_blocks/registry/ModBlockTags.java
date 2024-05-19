package net.spydnel.props_and_blocks.registry;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.spydnel.props_and_blocks.PropsBlocks;

public class ModBlockTags {
    public static final TagKey<Block> LARGE_CANDLES = TagKey.of(RegistryKeys.BLOCK, new Identifier(PropsBlocks.MOD_ID, "large_candles"));
}
