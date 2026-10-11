package com.moigferdsrte.treephysics.networking;

import com.moigferdsrte.treephysics.TreePhysicsClient;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static com.moigferdsrte.treephysics.TreePhysics.path;

public record UpdateClientTrees(ResourceKey<Level> dimension, List<UUID> trees) implements CustomPacketPayload {
    public static final Type<UpdateClientTrees> TYPE = new Type<>(path("update_client_trees"));

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateClientTrees> CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.DIMENSION), UpdateClientTrees::dimension,
            UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs.list()), UpdateClientTrees::trees,
            UpdateClientTrees::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void applyClient() {
        TreePhysicsClient.TREE_MANAGER.setTrees(this.dimension, new HashSet<>(this.trees));
    }

}
