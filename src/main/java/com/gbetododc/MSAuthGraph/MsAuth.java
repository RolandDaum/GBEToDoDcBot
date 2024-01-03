package com.gbetododc.MSAuthGraph;

import com.gbetododc.DiscordBot.DiscordBot;

import io.github.cdimascio.dotenv.Dotenv;

public class MsAuth {
    static Dotenv dotenv = Dotenv.configure().load();

    public void initAUth() {
        String DCOWNERID = dotenv.get("DCOWNERID");
        DiscordBot.JDA.getUserById(DCOWNERID).openPrivateChannel().queue(
            channel -> {
                channel.sendMessage("").queue();
            }
        );
    }
}
