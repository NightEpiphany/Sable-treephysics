package com.moigferdsrte.treephysics.particle.collision_dust;

import com.moigferdsrte.treephysics.mixin.accessors.SpriteContentsAccessor;
import com.moigferdsrte.treephysics.particle.collision_particle.BlockCollisionParticle;
import com.mojang.blaze3d.platform.NativeImage;
import dev.ryanhcode.sable.mixinterface.particle.ParticleExtension;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CollisionDustParticle extends SingleQuadParticle implements ParticleExtension {
    private final SpriteSet spriteSet;
    private final BlockState state;
    private final SingleQuadParticle.Layer layer;

    private int ticks = 0;

    public CollisionDustParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, BlockState state) {
        super(level, x, y, z, spriteSet.first());
        this.spriteSet = spriteSet;
        this.state = state;

        this.gravity = 0;
        this.age = (int) Math.floor(Math.random() * 5);
        this.scale(3.5f);

        float vx = (float) ((Math.random() - 0.5f) * 2.0f) * 0.15f;
        float vz = (float) ((Math.random() - 0.5f) * 2.0f) * 0.15f;
        this.setParticleSpeed(vx, Math.random() * 0.05, vz);

        this.hasPhysics = false;
        this.setSpriteFromAge(this.spriteSet);

        float[] particleColor = getParticleColor(this.state);
        this.setColor(particleColor[0], particleColor[1], particleColor[2]);

        this.setAlpha(0.5f);
        this.layer = SingleQuadParticle.Layer.TRANSLUCENT;

        // bad!
        ParticleEngine engine = Minecraft.getInstance().particleEngine;
        engine.add(new BlockCollisionParticle(level, x, y, z, 0, Math.random() * 0.2, 0, this.state));
    }

    @Override
    public void tick() {
        if(this.ticks % 3 == 0) {
            this.setSpriteFromAge(this.spriteSet);
            super.tick();
        }

        this.ticks++;
    }

    @Override
    public int getLightCoords(float partialTick) {
        BlockPos blockpos = BlockPos.containing(this.x, this.y, this.z);
        for (Direction direction : Direction.values()) {
            BlockPos relative = blockpos.relative(direction);
            BlockState state = this.level.getBlockState(relative);
            if(state.isAir()) {
                blockpos = relative;
                break;
            }
        }
        return this.level.hasChunkAt(blockpos) ? LightCoordsUtil.getLightCoords(this.level, blockpos) : 0;
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return this.layer;
    }

    private static float[] getParticleColor(BlockState state) {
        float[] color = new float[3];
        TextureAtlasSprite texture = Minecraft.getInstance().getModelManager().getBlockStateModelSet().getParticleMaterial(state).sprite();
        NativeImage image = ((SpriteContentsAccessor) texture.contents()).treephysics$getOriginalImage();

        int[] pixels = image.getPixelsABGR();

        for (int pixel : pixels) {
            int r = pixel & 0xFF;
            int g = (pixel >> 8) & 0xFF;
            int b = (pixel >> 16) & 0xFF;
            color[0] += r / 255f;
            color[1] += g / 255f;
            color[2] += b / 255f;
        }

        color[0] /= pixels.length;
        color[1] /= pixels.length;
        color[2] /= pixels.length;

        float greyscale = (color[0] + color[1] + color[2]) / 3.0f;
        float desaturate = 0.4f;
        color[0] = Mth.lerp(desaturate, color[0], greyscale);
        color[1] = Mth.lerp(desaturate, color[1], greyscale);
        color[2] = Mth.lerp(desaturate, color[2], greyscale);

        return color;
    }

    // dont want the sub-level to affect it at all

    @Override
    public void sable$initialKickOut() {

    }

    @Override
    public void sable$moveWithInheritedVelocity() {

    }

    @Override
    public void sable$setTrackingSubLevel(ClientSubLevel subLevel, Vec3 particlePosition) {

    }

    @Override
    public SubLevel sable$getTrackingSubLevel() {
        return null;
    }
}
