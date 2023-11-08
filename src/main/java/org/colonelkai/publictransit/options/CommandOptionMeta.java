package org.colonelkai.publictransit.options;

import org.core.command.argument.CommandArgument;
import org.core.command.argument.arguments.operation.ExactArgument;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public record CommandOptionMeta<T>(@NotNull CommandArgument<T> setterArgument, @NotNull ExactArgument nameArgument, @NotNull Method getter,
                                   @NotNull Method setter) {

}
