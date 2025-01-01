package dev.tonimatas;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        String token = Config.getToken();
        
        if (!token.isEmpty()) {
            JDABuilder jdaBuilder = JDABuilder.createDefault(token);
            jdaBuilder.setAutoReconnect(true);
            JDA jda = jdaBuilder.build();

            new Thread(() -> {
                for (;;) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    Guild guild = jda.getGuildById("1166787850235289693");

                    if  (guild == null) continue;

                    for (Member member : guild.getMembers()) {
                        GuildVoiceState voiceState = member.getVoiceState();

                        if (voiceState == null) continue;

                        if (voiceState.isSelfDeafened()) {
                            if (!voiceState.inAudioChannel()) continue;

                            member.getUser().openPrivateChannel().queue(channel -> channel.sendMessage(member.getAsMention() + """
                               
                               **¿Por qué ensordecerse en Discord puede ser problemático?**
                               
                               Discord es una plataforma diseñada para facilitar la comunicación y el trabajo en equipo. Sin embargo, la función de ensordecerse, aunque útil en ciertos momentos, puede tener efectos negativos si se utiliza con demasiada frecuencia o sin reflexionar sobre sus implicaciones.
                               
                               Al ensordecerse, un usuario se desconecta completamente del canal de voz, perdiendo tanto la oportunidad de escuchar como de ser escuchado. Esto puede interpretarse como desinterés o falta de compromiso por parte del resto del grupo, afectando la dinámica de la conversación y la percepción de la colaboración. Además, momentos clave como anuncios, debates importantes o incluso bromas grupales se pierden, lo que puede aislar al usuario y crear una brecha con los demás.
                               
                               Esta práctica también puede ser un mecanismo de evasión. En lugar de enfrentar situaciones incómodas o conflictos, ensordecerse perpetúa una desconexión que puede deteriorar las relaciones dentro del grupo. A largo plazo, esta actitud puede llevar al aislamiento social, ya que otros podrían interpretar la acción como un rechazo constante.
                               
                               Ensordecerse debería ser una herramienta puntual para casos específicos, como concentrarse en una tarea urgente o evitar interrupciones externas. Usarla de manera consciente ayuda a mantener el equilibrio entre la autonomía personal y el respeto por la interacción grupal.
                               """).queue());

                            TextChannel general = guild.getTextChannelById("1276354816880279617");

                            if (general != null) {
                                general.sendMessage(member.getAsMention() + " este capullo es un cabrón de los que se ensordecen.").queue();
                            }

                            guild.kickVoiceMember(member).queue();
                        }
                    }
                }
            }).start();
        }
    }
}