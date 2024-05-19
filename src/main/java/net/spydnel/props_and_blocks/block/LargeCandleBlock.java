package net.spydnel.props_and_blocks.block;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import net.spydnel.props_and_blocks.registry.ModBlockTags;
import org.jetbrains.annotations.Nullable;

public class LargeCandleBlock extends Block implements Waterloggable {
    public static final BooleanProperty CONNECTED;
    public static final BooleanProperty HAS_PUDDLE;
    public static final BooleanProperty LIT;
    public static final BooleanProperty WATERLOGGED;
    private static final VoxelShape CONNECTED_SHAPE;
    private static final VoxelShape TOP_SHAPE;

    public LargeCandleBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState())
                .with(LIT, false))
                .with(CONNECTED, false))
                .with(HAS_PUDDLE, false))
                .with(WATERLOGGED, false));
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getAbilities().allowModifyWorld && player.getStackInHand(hand).isEmpty() && (Boolean)state.get(LIT)) {
            extinguish(player, state, world, pos);
            return ActionResult.success(world.isClient);
        } else if (player.getAbilities().allowModifyWorld && player.getStackInHand(hand).isOf(Items.FLINT_AND_STEEL) && isNotLit(state)) {
            setLit(world, state, pos, true);
            world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }

    public static void extinguish(@Nullable PlayerEntity player, BlockState state, WorldAccess world, BlockPos pos) {
        setLit(world, state, pos, false);

        world.addParticle(ParticleTypes.SMOKE, (double)pos.getX() + 0.5, (double)pos.getY() + 0.95, (double)pos.getZ() + 0.5, 0.0, 0.10000000149011612, 0.0);

        world.playSound((PlayerEntity)null, pos, SoundEvents.BLOCK_CANDLE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);
        world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if ((Boolean)state.get(LIT)) {

            spawnCandleParticles(world, new Vec3d(pos.getX() + 0.5, pos.getY() + 0.95, pos.getZ() + 0.5), random);

        }
    }

    private static void spawnCandleParticles(World world, Vec3d vec3d, Random random) {
        float f = random.nextFloat();
        if (f < 0.3F) {
            world.addParticle(ParticleTypes.SMOKE, vec3d.x, vec3d.y, vec3d.z, 0.0, 0.0, 0.0);
            if (f < 0.17F) {
                world.playSound(vec3d.x + 0.5, vec3d.y + 0.5, vec3d.z + 0.5, SoundEvents.BLOCK_CANDLE_AMBIENT, SoundCategory.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
            }
        }

        world.addParticle(ParticleTypes.FLAME, vec3d.x, vec3d.y, vec3d.z, 0.0, 0.0, 0.0);
    }

    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        if (!world.isClient && projectile.isOnFire() && this.isNotLit(state)) {
            setLit(world, state, hit.getBlockPos(), true);
        }

    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        boolean connected = (ctx.getWorld().getBlockState(ctx.getBlockPos().up()).isIn(ModBlockTags.LARGE_CANDLES));
        boolean onFloor = (ctx.getWorld().getBlockState(ctx.getBlockPos().down()).isSideSolidFullSquare(ctx.getWorld(), ctx.getBlockPos(), Direction.UP));
        boolean waterlogged = (ctx.getWorld().getFluidState(ctx.getBlockPos()).isOf(Fluids.WATER));
        return (BlockState) this.getDefaultState().with(CONNECTED, connected).with(HAS_PUDDLE, onFloor).with(WATERLOGGED, waterlogged);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if ((Boolean)state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        boolean onFloor = (neighborState.isSideSolidFullSquare(world, pos, Direction.UP));
        boolean connected = (neighborState.isIn(ModBlockTags.LARGE_CANDLES));
        if (direction == Direction.UP) {
            return (BlockState) state.with(CONNECTED, connected).with(LIT, !connected && ((Boolean) state.get(LIT)));
        }
        if (direction == Direction.DOWN) {
            return (BlockState) state.with(HAS_PUDDLE, onFloor);
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{LIT, CONNECTED, WATERLOGGED, HAS_PUDDLE});
    }

    public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!(Boolean)state.get(WATERLOGGED) && fluidState.getFluid() == Fluids.WATER) {
            BlockState blockState = (BlockState)state.with(WATERLOGGED, true);
            if ((Boolean)state.get(LIT)) {
                extinguish((PlayerEntity)null, blockState, world, pos);
            } else {
                world.setBlockState(pos, blockState, 3);
            }

            world.scheduleFluidTick(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
            return true;
        } else {
            return false;
        }
    }

    protected boolean isNotLit(BlockState state) {
        return !(Boolean)state.get(WATERLOGGED) && !(Boolean)state.get(LIT) && !(Boolean)state.get(CONNECTED);
    }

    private static void setLit(WorldAccess world, BlockState state, BlockPos pos, boolean lit) {
        world.setBlockState(pos, (BlockState)state.with(LIT, lit), 11);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if ((Boolean)state.get(CONNECTED)) {
            return CONNECTED_SHAPE;
        }
        return TOP_SHAPE;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    static {
        LIT = Properties.LIT;
        CONNECTED = BooleanProperty.of("connected");
        HAS_PUDDLE = BooleanProperty.of("has_puddle");
        WATERLOGGED = Properties.WATERLOGGED;
        CONNECTED_SHAPE = Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);
        TOP_SHAPE = Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 11.0, 10.0);
    }
}
