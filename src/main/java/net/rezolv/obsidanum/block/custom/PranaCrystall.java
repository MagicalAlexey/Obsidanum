package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class PranaCrystall extends Block {
    // Используем DirectionProperty для всех направлений
    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    public PranaCrystall(Properties pProperties) {
        super(pProperties);
        // Устанавливаем направление по умолчанию (например, NORTH)
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING); // Добавляем свойство FACING в определение состояния блока
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Получаем направление, в котором игрок устанавливает блок
        Direction direction = context.getClickedFace();
        // Если игрок устанавливает блок на верхнюю или нижнюю грань, корректируем направление
        if (direction == Direction.UP || direction == Direction.DOWN) {
            return this.defaultBlockState().setValue(FACING, direction);
        } else {
            // Для горизонтальных направлений используем противоположное направление
            return this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        // Вращаем блок в зависимости от направления
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        // Отражаем блок в зависимости от направления
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }
}
