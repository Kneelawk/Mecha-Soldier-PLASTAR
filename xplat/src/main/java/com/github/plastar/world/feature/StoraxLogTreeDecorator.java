package com.github.plastar.world.feature;

import com.github.plastar.Constants;
import com.github.plastar.block.PBlocks;
import com.github.plastar.platform.Platform;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public class StoraxLogTreeDecorator extends TreeDecorator {
    public static final MapCodec<StoraxLogTreeDecorator> CODEC = MapCodec.unit(StoraxLogTreeDecorator::new);
    public static final TreeDecoratorType<StoraxLogTreeDecorator> TYPE =
        Platform.INSTANCE.treeDecoratorType(Constants.rl("storax_log"), CODEC);


    @Override
    protected TreeDecoratorType<?> type() {
        return TYPE;
    }

    @Override
    public void place(Context context) {
        int count = context.random().nextFloat() < 0.4 ? 2 : 1;
        for (int i = 0; i < count; ++i) {
            context.setBlock(context.logs().get(context.random().nextInt(context.logs().size())),
                PBlocks.STORAX_ACACIA_LOG.get().defaultBlockState());
        }
    }
}
