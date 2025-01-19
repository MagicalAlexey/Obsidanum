package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.rezolv.obsidanum.block.entity.HammerForgeEntity;
import org.jetbrains.annotations.Nullable;

public class HammerForge extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    public HammerForge(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.UP));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);

        // Проверяем, что это не клиентский уровень
        if (level.isClientSide()) {
            return;
        }

        // Определяем позицию соседнего блока (снизу и наискось)
        for (Direction horizontal : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = pos.relative(Direction.DOWN).relative(horizontal);
            BlockState neighborState = level.getBlockState(neighborPos);

            // Проверяем, что соседний блок - LeftCornerLevel, и его состояние is_pressed == true
            if (neighborState.getBlock() instanceof LeftCornerLevel && neighborState.getValue(LeftCornerLevel.IS_PRESSED)) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof HammerForgeEntity hammerForgeEntity) {
                    // Запуск анимации молота
                    hammerForgeEntity.startHammerAnimation();
                }
            }
        }
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace());
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos blockpos = pos.relative(direction.getOpposite());
        return level.getBlockState(blockpos).isFaceSturdy(level, blockpos, direction);
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new HammerForgeEntity(blockPos, blockState);
    }
}
