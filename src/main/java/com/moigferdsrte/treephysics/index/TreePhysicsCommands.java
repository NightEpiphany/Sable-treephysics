package com.moigferdsrte.treephysics.index;

import com.moigferdsrte.treephysics.api.manager.ServerTreeManager;
import com.moigferdsrte.treephysics.api.manager.TreeManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.ryanhcode.sable.api.command.SubLevelArgumentType;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.Permissions;

import java.util.Collection;

public class TreePhysicsCommands {
    public static void registerCommands(CommandBuildContext context, CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> base = Commands.literal("treephysics")
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER));

        base.then(Commands.literal("untree")
                .then(Commands.literal("all")
                        .executes(TreePhysicsCommands::unsetAll))
                .then(Commands.argument("sub_level", SubLevelArgumentType.subLevels())
                        .executes(TreePhysicsCommands::unset)
                ));

        dispatcher.register(base);
    }

    private static int unsetAll(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        int count = 0;
        for (ServerLevel level : source.getServer().getAllLevels()) {
            ServerTreeManager manager = (ServerTreeManager) TreeManager.get(level);
            count += manager.removeAll();
        }

        MutableComponent message = Component.translatable("commands.treephysics.untree.success", count, count == 1 ? "" : "s");
        context.getSource().sendSuccess(() -> message, false);

        return count;
    }

    private static int unset(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        int count = 0;
        Collection<ServerSubLevel> subLevels = SubLevelArgumentType.getSubLevels(context, "sub_level");

        for (ServerSubLevel subLevel : subLevels) {
            ServerTreeManager manager = (ServerTreeManager) TreeManager.get(subLevel.getLevel());
            if(manager.isTree(subLevel)) {
                manager.unsetTree(subLevel);
                count++;
            }
        }

        MutableComponent message = Component.translatable("commands.treephysics.untree.success", count, count == 1 ? "" : "s");
        context.getSource().sendSuccess(() -> message, false);

        return count;
    }

}
