package dev.tonimatas.commands;

import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import revxrsal.commands.annotation.*;
import revxrsal.commands.jda.actor.SlashCommandActor;
import revxrsal.commands.jda.annotation.GuildOnly;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class PeakImpostorCommand {
    private static final SecureRandom RANDOM = new SecureRandom();
    
    @Command("peak")
    @Description("PEAK! You are a climber... or not.")
    @GuildOnly
    @Subcommand("impostor")
    public void execute(SlashCommandActor actor,
                        @Named("message-id") @Description("The message id of the party") String messageId,
                        @Named("impostors") @Description("The number of impostors") @Range(min = 1) int impostors) {
        JDA jda = actor.jda();
        Member member = actor.commandEvent().getMember();
        Role allowedRole = BotFiles.CONFIG.getAllowedGameRole(jda);
        
        if (member == null) {
            actor.replyToInteraction("Please try again later.")
                    .setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }
        
        if (member.getRoles().contains(allowedRole)) {
            actor.channel().retrieveMessageById(messageId).queue(result -> 
                    result.retrieveReactionUsers(Emoji.fromUnicode("✅")).queue(users -> {
                        List<Integer> impostorResults = getRandomImpostors(users.size(), impostors);
                        int id = 0;

                        for (User reactor : users) {
                            if (impostorResults.contains(id)) {
                                reactor.openPrivateChannel().queue(privateChannel -> 
                                        privateChannel.sendMessage("PEAK! You are an impostor.").queue());
                            } else {
                                reactor.openPrivateChannel().queue(privateChannel ->
                                        privateChannel.sendMessage("PEAK! You are a climber.").queue());
                            }
                            
                            id++;
                        }}, fail -> actor.replyToInteraction("Error sending DMs. Please try again later.").
                            setEphemeral(true).queue(Messages.deleteBeforeX(10))), fail -> 
                    actor.replyToInteraction("Error sending DMs. Please try again later.")
                            .setEphemeral(true).queue(Messages.deleteBeforeX(10)));
            
            actor.replyToInteraction("Sending DMs to all everyone who reacted to that message.")
                    .setEphemeral(true).queue(Messages.deleteBeforeX(10));
        } else {
            actor.replyToInteraction("You do not have permission to do that. Please stop!")
                    .setEphemeral(true).queue(Messages.deleteBeforeX(10));
        }
    }
    
    private List<Integer> getRandomImpostors(int players, int impostors) {
        List<Integer> result = new ArrayList<>();
        
        while (impostors > 0) {
            int impostor = RANDOM.nextInt(0, players);
            
            if (!result.contains(impostor)) {
                result.add(impostor);
                impostors--;
            }
        }
        
        return result;
    }
}
