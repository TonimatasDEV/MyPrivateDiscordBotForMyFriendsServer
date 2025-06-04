package dev.tonimatas.util;

import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.List;
import java.util.stream.Stream;

public class Strings {
    public static List<Command.Choice> getStartWithValues(String[] values, String focusedValue) {
        return Stream.of(values)
                .filter(value -> value.startsWith(focusedValue))
                .map(value -> new Command.Choice(value, value))
                .toList();
    }
}
