package dev.tonimatas.listeners;

import dev.tonimatas.util.Strings;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Collections;

public class AutoCompleteListener extends ListenerAdapter {
    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("bet")) {
            String option = event.getFocusedOption().getName();
            String focusedValue = event.getFocusedOption().getValue();

            if (option.equalsIgnoreCase("bet-option")) {
                OptionMapping betType = event.getOption("bet-type");

                if (betType == null) return;
                String[] options;

                switch (betType.getAsString()) {
                    case "color" -> options = new String[]{"green", "red", "black"};
                    case "column", "dozen" -> options = new String[]{"first", "second", "third"};
                    default -> options = new String[]{};
                }

                event.replyChoices(Strings.getStartWithValues(options, focusedValue)).queue();
            } else {
                event.replyChoices(Collections.emptyList()).queue();
            }
        }
    }
}
