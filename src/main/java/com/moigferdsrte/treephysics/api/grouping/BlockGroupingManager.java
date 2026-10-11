package com.moigferdsrte.treephysics.api.grouping;

import com.moigferdsrte.treephysics.TreePhysics;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BlockGroupingManager extends SimplePreparableReloadListener<Map<Identifier, Map<Identifier, Set<Identifier>>>> {
    public static final BlockGroupingManager INSTANCE = new BlockGroupingManager();
    public static final FileToIdConverter GROUPING_LISTER = FileToIdConverter.json(TreePhysics.MOD_ID + "/grouping");
    public static final Map<Identifier, BlockGrouping> GROUPS_MAP = new Object2ObjectOpenHashMap<>();

    @Override
    protected Map<Identifier, Map<Identifier, Set<Identifier>>> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<Identifier, Map<String, Set<Identifier>>> map = new Object2ObjectOpenHashMap<>();

        Map<Identifier, List<Resource>> resourceStacks = GROUPING_LISTER.listMatchingResourceStacks(resourceManager);
        for (Map.Entry<Identifier, List<Resource>> entry : resourceStacks.entrySet()) {

            Identifier id = GROUPING_LISTER.fileToId(entry.getKey());
            Map<String, Set<Identifier>> grpupMap = map.computeIfAbsent(id, rl -> new Object2ObjectOpenHashMap<>());

            for (Resource resource : entry.getValue()) {
                try (Reader reader = resource.openAsReader()) {

                    JsonElement element = JsonParser.parseReader(reader);
                    if(element instanceof JsonObject object && object.has("groups") && object.get("groups") instanceof JsonObject groupsObject) {

                        for (String groupId : groupsObject.keySet()) {
                            DataResult<List<Identifier>> result = Identifier.CODEC.listOf().parse(JsonOps.INSTANCE, groupsObject.get(groupId));
                            if(result.isSuccess()) {
                                grpupMap.computeIfAbsent(groupId, s -> new ObjectOpenHashSet<>())
                                        .addAll(result.getOrThrow());
                            }

                        }

                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }

        Map<Identifier, Map<Identifier, Set<Identifier>>> finalMap = new Object2ObjectOpenHashMap<>();

        for (Identifier id : map.keySet()) {
            Map<Identifier, Set<Identifier>> groupMap = new Object2ObjectOpenHashMap<>();
            Map<String, Set<Identifier>> stringMap = map.get(id);

            for (Set<Identifier> values : stringMap.values()) {
                for (Identifier value : values) {
                    Set<Identifier> set = groupMap.computeIfAbsent(value, rl -> new ObjectOpenHashSet<>());
                    set.addAll(values.stream().filter(v -> v != value).toList());
                }
            }

            finalMap.put(id, groupMap);
        }

        return finalMap;
    }

    @Override
    protected void apply(Map<Identifier, Map<Identifier, Set<Identifier>>> groups, ResourceManager resourceManager, ProfilerFiller profiler) {
        GROUPS_MAP.clear();

        for (Map.Entry<Identifier, Map<Identifier, Set<Identifier>>> entry : groups.entrySet()) {
            Identifier id = entry.getKey();
            Map<Identifier, Set<Identifier>> groupMap = entry.getValue();
            Map<Block, Set<Block>> blockMap = new Object2ObjectOpenHashMap<>();

            for (Map.Entry<Identifier, Set<Identifier>> groupEntry : groupMap.entrySet()) {
                Identifier key = groupEntry.getKey();
                Block keyBlock = BuiltInRegistries.BLOCK.getValue(key);
                if(keyBlock == Blocks.AIR) {
                    continue;
                }

                for (Identifier value : groupEntry.getValue()) {
                    Block valueBlock = BuiltInRegistries.BLOCK.getValue(value);
                    if(valueBlock == Blocks.AIR) {
                        continue;
                    }

                    Set<Block> keySet = blockMap.computeIfAbsent(keyBlock, b -> new ObjectOpenHashSet<>());
                    Set<Block> valueSet = blockMap.computeIfAbsent(valueBlock, b -> new ObjectOpenHashSet<>());
                    keySet.add(valueBlock);
                    valueSet.add(keyBlock);
                }
            }

            GROUPS_MAP.put(id, new BlockGrouping(blockMap));
        }

    }
}
