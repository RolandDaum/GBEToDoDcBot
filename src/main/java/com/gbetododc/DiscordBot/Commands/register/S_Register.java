package com.gbetododc.DiscordBot.Commands.register;

import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Invite.Guild;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateFeaturesEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class S_Register {

    static Dotenv dotenv = Dotenv.configure().load();
    static String GUILDID = dotenv.get("GUILDID");

    public static void main(SlashCommandInteractionEvent event) {
        User EventUser = event.getUser();

        removeAllRoles(event);
        
        for (int i = 0; i < event.getOptions().size(); i++) {
            String Kursname = event.getOptions().get(i).getAsString();
            Role RoleID = GetRoleIDByName(event, Kursname);

            if (RoleID == null) {
                event.getJDA().getGuildById(GUILDID)
                    .createRole().setName(Kursname)
                    .setColor(0x011765)
                    .queue(
                        (CreatedRole) -> {
                            event.getJDA().getGuildById(GUILDID)
                                .addRoleToMember(EventUser, GetRoleIDByName(event, Kursname))
                                .queue();
                        }
                    );
            } else {
                event.getJDA().getGuildById(GUILDID)
                    .addRoleToMember(EventUser, GetRoleIDByName(event, Kursname))
                    .queue();
            }
        }
        event.reply("Roles has been asinged to user: " + event.getUser().getName()).queue();
    }

    // Get the ID of a role by the role name or if not existing return null
    private static Role GetRoleIDByName(SlashCommandInteractionEvent event, String RoleName) {
        return event.getJDA().getGuildById(GUILDID).getRolesByName(RoleName, true).stream().findFirst().orElse(null);
    }

    // Remove all roles a user has
    public static void removeAllRoles(SlashCommandInteractionEvent event) {
        // TODO: Später nur die Kursrollen entfernen und nicht alle
        List<Role> EventUserRoles = event.getMember().getRoles();
        for (Role role : EventUserRoles) {
            role.getGuild().removeRoleFromMember(event.getUser(), role).queue();
        }
    }
}