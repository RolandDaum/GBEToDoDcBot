package com.gbetododc.DiscordBot;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BrokenBarrierException;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.jetbrains.annotations.NotNull;

import io.github.cdimascio.dotenv.Dotenv;
import kotlin.jvm.internal.markers.KMutableIterable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.Invite.Guild;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotCommands extends ListenerAdapter {
    static Dotenv dotenv = Dotenv.configure().load();

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        if(event.getName().equals("register")){

            SlashRegister(event);
            // System.out.println(event.getOptions().size());
            // System.out.println(event.getOptions().get(0).getAsString());



        }
    }
    
    private Boolean SlashRegister(SlashCommandInteractionEvent event) {
        String GUILDID = dotenv.get("GUILDID");
        Member UserID = event.getGuild().getMember(event.getUser());

        for (int i = 0; i < event.getOptions().size(); i++) {
            String Kursname = event.getOptions().get(i).getAsString();

            Role RoleID = event.getJDA().getGuildById(GUILDID).getRolesByName(Kursname, true).stream().findFirst().orElse(null);

            if (RoleID == null) {
                event.getJDA().getGuildById(GUILDID)
                    .createRole().setName(Kursname)
                    .setColor(0x992d22)
                    .queue(
                        (CreatedRole) -> {
                            event.getJDA()
                                .getGuildById(GUILDID)
                                .addRoleToMember(UserID, event.getJDA().getGuildById(GUILDID).getRolesByName(Kursname, true).stream().findFirst().orElse(null))
                                .queue();
                        }
                    );
            } else {
                event.getJDA().getGuildById(GUILDID)
                    .addRoleToMember(UserID, event.getJDA().getGuildById(GUILDID).getRolesByName(Kursname, true).stream().findFirst().orElse(null))
                    .queue();
            }
        }
        event.reply("Roles has been asinged to user: " + event.getMember().getNickname()).queue();
        return null;
    }
}