package dev.tonimatas.commands;

import dev.tonimatas.cjda.slash.SlashCommand;
import dev.tonimatas.systems.tictactoe.Player;
import dev.tonimatas.systems.tictactoe.TicTacToeGame;
import dev.tonimatas.systems.tictactoe.TicTacToeManager;
import dev.tonimatas.util.CommandUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.Set;

public class TicTacToeCommand implements SlashCommand {
    private final TicTacToeManager manager;

    public TicTacToeCommand(TicTacToeManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute(SlashCommandInteraction interaction) {
        JDA jda = interaction.getJDA();
        String subcommand = interaction.getSubcommandName();
        String userId = interaction.getUser().getId();

        if (CommandUtils.isNotTicTacToeChannel(interaction)) return;

        switch (subcommand) {
            case "start" -> {
                Member challenger = interaction.getMember();
                Member opponent = interaction.getOption("user").getAsMember();

                if (challenger.equals(opponent)) {
                    interaction.reply("You cannot play against yourself!").setEphemeral(true).queue();
                    return;
                }

                if (manager.isPlaying(challenger.getId()) || manager.isPlaying(opponent.getId())) {
                    interaction.reply("One of the players is already in a game.").setEphemeral(true).queue();
                    return;
                }

                Player playerX = new Player(challenger, 'X');
                Player playerO = new Player(opponent, 'O');
                TicTacToeGame game = new TicTacToeGame(playerX, playerO);

                manager.startGame(challenger.getId(), game);
                manager.startGame(opponent.getId(), game);

                interaction.reply(playerX.getMention() + " vs " + playerO.getMention() + " — Game started!\n" +
                                game.getBoard().render() + "\n" +
                                "It's " + playerX.getMention() + "'s turn (**X**).")
                        .queue();
            }
            case "start-bot" -> {
                Member challenger = interaction.getMember();

                if (manager.isPlaying(challenger.getId())) {
                    interaction.reply("You are already in a game.").setEphemeral(true).queue();
                    return;
                }

                Player playerX = new Player(challenger, 'X');
                TicTacToeGame game = new TicTacToeGame(playerX);

                manager.startGame(challenger.getId(), game);

                interaction.reply(playerX.getMention() + " vs Bot - Game started!\n" +
                                game.getBoard().render() + "\n" +
                                "It's your turn (**X**).")
                        .queue();
            }
            case "play" -> {
                TicTacToeGame game = manager.getGame(userId);

                if (game == null) {
                    interaction.reply("You are not in an active game.").setEphemeral(true).queue();
                    return;
                }

                int row = interaction.getOption("row").getAsInt();
                int col = interaction.getOption("col").getAsInt();
                Player currentPlayer = game.getCurrentPlayer();

                if (!currentPlayer.getMember().getId().equals(userId)) {
                    interaction.reply("It's not your turn.").setEphemeral(true).queue();
                    return;
                }

                boolean moveOk = game.getBoard().makeMove(row, col, currentPlayer.getSymbol());
                if (!moveOk) {
                    interaction.reply("That cell is already occupied. Try another one.").setEphemeral(true).queue();
                    return;
                }

                if (game.getBoard().checkWinner() != ' ' || game.getBoard().isFull()) {
                    manager.endGame(userId);
                    char winner = game.getBoard().checkWinner();
                    if (winner != ' ') {
                        interaction.reply(currentPlayer.getMention() + " wins!\n" + game.getBoard().render()).queue();
                    } else {
                        interaction.reply("It's a draw!\n" + game.getBoard().render()).queue();
                    }
                    return;
                }

                game.switchTurn();

                if (game.isVsBot()) {
                    int[] botMove = game.botMakeMove();

                    if (botMove == null) {
                        manager.endGame(userId);
                        interaction.reply("Bot could not make a move. Game ended.").queue();
                        return;
                    }

                    if (game.isOver()) {
                        manager.endGame(userId);
                        char winner = game.getBoard().checkWinner();
                        if (winner != ' ') {
                            interaction.reply(currentPlayer.getMention() + " moved at (" + row + ", " + col + ")\n" +
                                    "Bot played at (" + botMove[0] + ", " + botMove[1] + ")\n" +
                                    game.getBoard().render() + "\n" +
                                    "Bot wins!").queue();
                        } else {
                            interaction.reply(currentPlayer.getMention() + " moved at (" + row + ", " + col + ")\n" +
                                    "Bot played at (" + botMove[0] + ", " + botMove[1] + ")\n" +
                                    game.getBoard().render() + "\n" +
                                    "It's a draw!").queue();
                        }
                        return;
                    }

                    interaction.reply(currentPlayer.getMention() + " moved at (" + row + ", " + col + ")\n" +
                            "Bot played at (" + botMove[0] + ", " + botMove[1] + ")\n" +
                            game.getBoard().render() + "\n" +
                            "Your turn, " + currentPlayer.getMention() + ".").queue();
                } else {
                    Player nextPlayer = game.getCurrentPlayer();
                    interaction.reply(currentPlayer.getMention() + " moved at (" + row + ", " + col + ")\n" +
                            game.getBoard().render() + "\n" +
                            "It's now " + nextPlayer.getMention() + "'s turn.").queue();
                }
            }
            case "quit" -> {
                TicTacToeGame game = manager.getGame(userId);
                if (game == null) {
                    interaction.reply("You are not in an active game.").setEphemeral(true).queue();
                    return;
                }

                manager.endGame(userId);
                interaction.reply("You have left the game.").queue();
            }
        }
    }

    @Override
    public String getName() {
        return "tictactoe";
    }

    @Override
    public String getDescription() {
        return "Play the TicTacToe minigame against a member or an AI.";
    }

    @Override
    public Set<InteractionContextType> getContexts() {
        return Set.of(InteractionContextType.GUILD);
    }

    @Override
    public SlashCommandData init(SlashCommandData data) {
        return data
                .addSubcommands(new SubcommandData("start", "Start a game against another user.")
                        .addOptions(new OptionData(OptionType.USER, "user", "The user to play against", true)))
                .addSubcommands(new SubcommandData("start-bot", "Start a game against the bot."))
                .addSubcommands(new SubcommandData("play", "Play your turn")
                        .addOptions(
                                new OptionData(OptionType.INTEGER, "row", "Row (0 to 2)", true),
                                new OptionData(OptionType.INTEGER, "col", "Column (0 to 2)", true)
                        ))
                .addSubcommands(new SubcommandData("quit", "Quit the current game"));

    }
}
