package dev.tonimatas.commands;

import dev.tonimatas.cjda.slash.SlashCommand;
import dev.tonimatas.config.BotFiles;
import dev.tonimatas.api.bank.Transaction;
import dev.tonimatas.util.CommandUtils;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public class TransactionsCommand implements SlashCommand {
    @Override
    public void execute(SlashCommandInteraction interaction) {
        if (CommandUtils.isNotCommandsChannel(interaction)) return;

        OptionMapping userOption = interaction.getOption("user");
        JDA jda = interaction.getJDA();
        User user = interaction.getUser();

        user = userOption != null && userOption.getAsMember() != null ? userOption.getAsUser() : user;

        if (user.isBot()) {
            MessageEmbed err = Messages.getErrorEmbed(jda, "You can't see the transactions of a bot.");
            interaction.replyEmbeds(err).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        String transactions = getTransactionsString(user);

        MessageEmbed embed = Messages.getDefaultEmbed(jda, "Transactions", transactions);
        interaction.replyEmbeds(embed).queue();
    }

    private static String getTransactionsString(User user) {
        List<Transaction> transactions = BotFiles.USER.get(user.getId()).getTransactions();

        StringBuilder text = new StringBuilder();

        text.append("**").append(user.getEffectiveName()).append(":**\n");

        int i = 1;
        for (Transaction transaction : transactions) {
            text.append(String.format("%d. ", i++))
                    .append(transaction.getTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    .append("  |  ")
                    .append(transaction.getReason())
                    .append("  |  ")
                    .append(transaction.getAmount())
                    .append("€\n");
        }

        return text.toString();
    }

    @Override
    public SlashCommandData init(SlashCommandData data) {
        return data.addOption(OptionType.USER, "user", "If you want to see the transactions of an specific user.", false);
    }

    @Override
    public String getName() {
        return "transactions";
    }

    @Override
    public String getDescription() {
        return "See your own transactions.";
    }

    @Override
    public Set<InteractionContextType> getContexts() {
        return Set.of(InteractionContextType.GUILD);
    }
}
