package net.spydnel.props_and_blocks;

import net.fabricmc.api.ModInitializer;

import net.spydnel.props_and_blocks.registry.ModBlocks;
import net.spydnel.props_and_blocks.registry.ModItemGroups;
import net.spydnel.props_and_blocks.registry.ModItems;

public class PropsBlocks implements ModInitializer {

	public static final String MOD_ID = "props_and_blocks";

	@Override
	public void onInitialize() {
		ModBlocks.registerModBlocks();
		ModItems.registerModItems();
		ModItemGroups.registerModItemGroups();
	}
}