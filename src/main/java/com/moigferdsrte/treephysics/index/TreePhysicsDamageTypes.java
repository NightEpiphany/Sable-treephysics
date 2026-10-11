package com.moigferdsrte.treephysics.index;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

import static com.moigferdsrte.treephysics.TreePhysics.path;

public class TreePhysicsDamageTypes {

    public static final ResourceKey<DamageType> TREE_CRUSHING = ResourceKey.create(Registries.DAMAGE_TYPE, path("tree_crushing"));

}
