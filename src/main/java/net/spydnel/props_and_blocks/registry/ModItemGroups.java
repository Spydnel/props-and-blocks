package net.spydnel.props_and_blocks.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spydnel.props_and_blocks.PropsBlocks;

public class ModItemGroups {
    private static final ItemGroup PROPS_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(PropsBlocks.MOD_ID, "props_group"),
            FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModBlocks.LARGE_CANDLE))
            .displayName(Text.translatable("itemGroup.props_group"))
            .entries((context, entries) -> {

                entries.add(ModBlocks.LARGE_CANDLE);
                entries.add(ModBlocks.LARGE_WHITE_CANDLE);
                entries.add(ModBlocks.LARGE_LIGHT_GRAY_CANDLE);
                entries.add(ModBlocks.LARGE_GRAY_CANDLE);
                entries.add(ModBlocks.LARGE_BLACK_CANDLE);

            }).build());
    public static void registerModItemGroups() {

    }
}
