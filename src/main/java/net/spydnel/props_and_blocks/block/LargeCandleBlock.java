package net.spydnel.props_and_blocks.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.shape.VoxelShape;

public class LargeCandleBlock extends Block implements Waterloggable {
    public static final BooleanProperty CONNECTED;
    public static final BooleanProperty LIT;
    public static final BooleanProperty WATERLOGGED;
    private static final VoxelShape CONNECTED_SHAPE;
    private static final VoxelShape TOP_SHAPE;

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
        builder.add(CONNECTED);
        builder.add(WATERLOGGED);
    }
    public LargeCandleBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState())
                .with(LIT, false))
                .with(CONNECTED, false))
                .with(WATERLOGGED, false));
    }





    static {
        LIT = Properties.LIT;
        CONNECTED = BooleanProperty.of("connected");
        WATERLOGGED = Properties.WATERLOGGED;
        CONNECTED_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);
        TOP_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    }
}
