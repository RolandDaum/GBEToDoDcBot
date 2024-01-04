package com.gbetododc.MSAuthGraph;

import java.util.Map;

import com.gbetododc.DiscordBot.DiscordBot;
import com.gbetododc.MSAuthGraph.MSenvJson.MSenv;

import io.github.cdimascio.dotenv.Dotenv;

public class MsAuth {
    static Dotenv dotenv = Dotenv.configure().load();

    public static String getAuthurl() {
        MSenv msenvJson = MSenvJson.getMSenv();
        String authurl = new String(msenvJson.getReqCredentials().getUrl_auth() + "?client_id=" + msenvJson.getReqCredentials().getClientId() + "&response_type=code&response_mode=query&scope=" + msenvJson.getReqCredentials().getScopes() + "&redirect_uri=" + msenvJson.getReqCredentials().getRedirect_URI());
        return authurl;
    }
    public static void initAuth(String authcode) {
        Long DCOWNERID = Long.parseLong(dotenv.get("DCOWNERID"));

        DiscordBot.JDA.openPrivateChannelById(DCOWNERID).queue(
            success -> {
                success.sendMessage(":warning:    You have to reauth idk, das muss eigentlich erst hin, wenn man keine API anfragen mehr machen kann und der hier auch keine RT/Tokens bekmmt aber anywayd").queue();
                // success.sendMessage("[OPEN](" + authreqURI + ")").queue();
            }
        );        
        // DiscordBot.JDA.openPrivateChannelById(DCOWNERID)
        // DiscordBot.JDA.openPrivateChannelById(DCOWNERID).queue(
        //     channel -> {
        //         channel.sendMessage("[OPEN LINK](" + authreqURI + ")");
        //     }
        // );
        
        
        
        // get Expiering timestamp from json and compare it to the current time -> start auth || force to reauth/initAUth <- better methot/way
        // Timestamp timestamp = Timestamp.valueOf("2024-01-02 17:45:19.588");
        // System.out.println(timestamp);
        // System.out.println(System.currentTimeMillis());
        // System.out.println(new Timestamp(System.currentTimeMillis()));
    
        
    }
}
