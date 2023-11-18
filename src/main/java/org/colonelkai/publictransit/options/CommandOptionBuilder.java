package org.colonelkai.publictransit.options;

import net.kyori.adventure.text.Component;
import org.colonelkai.publictransit.utils.Builder;
import org.core.command.argument.CommandArgument;
import org.core.command.argument.arguments.operation.ExactArgument;
import org.core.command.argument.arguments.operation.MappedArgumentWrapper;
import org.core.command.argument.arguments.operation.OptionalArgument;
import org.core.command.argument.arguments.simple.BooleanArgument;
import org.core.command.argument.arguments.simple.EnumArgument;
import org.core.command.argument.arguments.simple.StringArgument;
import org.core.command.argument.arguments.simple.number.DoubleArgument;
import org.core.command.argument.arguments.simple.number.IntegerArgument;
import org.core.utils.ComponentUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.*;

public class CommandOptionBuilder {

    private static CommandArgument<?> build(@NotNull Class<?> clazz, @NotNull String key) {
        if (String.class.isAssignableFrom(clazz)) {
            return new StringArgument(key);
        }
        if(Double.class.isAssignableFrom(clazz) || double.class.isAssignableFrom(clazz)){
            return new DoubleArgument(key);
        }
        if(Integer.class.isAssignableFrom(clazz) || int.class.isAssignableFrom(clazz)){
            return new IntegerArgument(key);
        }
        if(Boolean.class.isAssignableFrom(clazz) || boolean.class.isAssignableFrom(clazz)){
            return new BooleanArgument(key);
        }
        if(Component.class.isAssignableFrom(clazz)){
            return new MappedArgumentWrapper<>(new StringArgument(key), ComponentUtils::fromLegacy);
        }
        if(Enum.class.isAssignableFrom(clazz)){
            return new EnumArgument<>(key, (Class<? extends Enum<?>>)clazz);
        }
        throw new RuntimeException("Unknown mapping for " + clazz.getSimpleName());
    }

    private static <T> CommandOptionMeta<T> buildFor(Class<? extends Builder<?, ?>> clazz, Method getter) {
        CommandOption options = getter.getDeclaredAnnotation(CommandOption.class);
        Optional<Method> opSetter = Arrays.stream(clazz.getDeclaredMethods()).filter(setterMethod -> {
            String name = (options.setter().equals("")) ? ("set" + getter.getName()) : options.setter();
            return setterMethod.getName().equalsIgnoreCase(name);
        }).filter(setterMethod -> 1 == setterMethod.getParameterCount()).findFirst();
        if (opSetter.isEmpty()) {
            throw new RuntimeException("No setter found for getter " + getter.getName());
        }
        Method setter = opSetter.get();
        Class<?> type = (options.argumentType().equals(Object.class)) ? setter.getParameterTypes()[0] : options.argumentType();
        CommandArgument<T> setterArgument = (CommandArgument<T>) build(type, "value");
        ExactArgument exactArgument = new ExactArgument(options.name().equals("") ? getter.getName() : options.name());
        if (setter.getParameters()[0].isAnnotationPresent(Nullable.class)) {
            setterArgument = new OptionalArgument<>(setterArgument, (T) null);
        }
        return new CommandOptionMeta<>(setterArgument, exactArgument, getter, setter);
    }

    public static Collection<? extends CommandOptionMeta<?>> buildFrom(Class<? extends Builder<?, ?>> clazz) {
        List<Method> getterMethods = Arrays
                .stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(CommandOption.class))
                .filter(method -> 0 == method.getParameters().length)
                .toList();
        return getterMethods.stream().map(method -> {
            try {
                return buildFor(clazz, method);
            } catch (RuntimeException e) {
                e.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull).toList();
    }

}
