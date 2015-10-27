/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.worldedit.command.composition;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.command.tool.BrushTool;
import com.sk89q.worldedit.command.tool.InvalidToolBindException;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.util.command.CommandExecutor;
import com.sk89q.worldedit.util.command.argument.CommandArgs;

import static com.google.common.base.Preconditions.checkNotNull;

public class FillBrushCommand<T> extends CommandExecutor<T> {

    private final CommandExecutor<? extends T> delegate;

    public FillBrushCommand(CommandExecutor<? extends T> delegate) {
        checkNotNull(delegate, "delegate");
        this.delegate = delegate;
    }

    @Override
    public T call(CommandArgs args, CommandLocals locals, String[] parentCommands) throws CommandException {
        Pattern pattern = new PatternCommand().call(args, locals, parentCommands);

        Player player = (Player) locals.get(Actor.class);
        LocalSession session = WorldEdit.getInstance().getSessionManager().get(player);

        try {
            BrushTool tool = session.getBrushTool(player.getItemInHand());
            tool.setFill(pattern);
        } catch (InvalidToolBindException e) {
            WorldEdit.getInstance().getPlatformManager().getCommandManager().getExceptionConverter().convert(e);
        }

        return delegate.call(args, locals, parentCommands);
    }
}
