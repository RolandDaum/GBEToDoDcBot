package com.gbetododc.DiscordBot.Commands.settings;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class S_Settings_Setup {

    static Dotenv dotenv = Dotenv.configure().load();


    public static void setup(JDA jda) {
        Guild guild = jda.getGuildById(dotenv.get("GUILDID"));

        guild
            .upsertCommand("settings", "adjust the some settings")
            .addOptions(
                (OptionType.BOOLEAN, "test", "Test description", true)
                    .addChoice("Option 1", true)
                    .addChoice("Option 2", false)
        )
            .addOption(null, null, null)
            .queue();
    }
}
