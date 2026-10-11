package com.moigferdsrte.treephysics.particle.collision_particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BlockCollisionParticle extends SingleQuadParticle {
    private final SingleQuadParticle.Layer layer;
    private final BlockPos pos;
    private final float uo;
    private final float vo;

    public BlockCollisionParticle(
            ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockState state
    ) {
        this(level, x, y, z, xSpeed, ySpeed, zSpeed, state, BlockPos.containing(x, y, z));
    }

    public BlockCollisionParticle(
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed,
            BlockState state,
            BlockPos pos
    ) {
        super(
                level,
                x,
                y,
                z,
                xSpeed,
                ySpeed,
                zSpeed,
                Minecraft.getInstance().getModelManager().getBlockStateModelSet().getParticleMaterial(state).sprite()
        );
        this.pos = pos;
        this.gravity = 1.0F;
        this.rCol = 0.6F;
        this.gCol = 0.6F;
        this.bCol = 0.6F;
        if (!state.is(Blocks.GRASS_BLOCK)) {
            BlockTintSource tintSource = Minecraft.getInstance().getBlockColors().getTintSource(state, 0);
            int i = tintSource == null ? -1 : tintSource.colorAsTerrainParticle(state, level, pos);
            this.rCol *= (float)(i >> 16 & 0xFF) / 255.0F;
            this.gCol *= (float)(i >> 8 & 0xFF) / 255.0F;
            this.bCol *= (float)(i & 0xFF) / 255.0F;
        }

        this.quadSize /= 2.0F;
        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;

        this.xd = xSpeed + (Math.random() * 2.0 - 1.0) * 0.4F;
        this.yd = ySpeed + (Math.random() * 2.0 - 1.0) * 0.4F;
        this.zd = zSpeed + (Math.random() * 2.0 - 1.0) * 0.4F;
        this.layer = SingleQuadParticle.Layer.bySprite(this.sprite);
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return this.layer;
    }

    @Override
    protected float getU0() {
        return this.sprite.getU((this.uo + 1.0F) / 4.0F);
    }

    @Override
    protected float getU1() {
        return this.sprite.getU(this.uo / 4.0F);
    }

    @Override
    protected float getV0() {
        return this.sprite.getV(this.vo / 4.0F);
    }

    @Override
    protected float getV1() {
        return this.sprite.getV((this.vo + 1.0F) / 4.0F);
    }

    @Override
    public int getLightCoords(float partialTick) {
        int i = super.getLightCoords(partialTick);
        return i == 0 && this.level.hasChunkAt(this.pos) ? LightCoordsUtil.getLightCoords(this.level, this.pos) : i;
    }

}
