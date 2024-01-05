package com.gbetododc.MSAuthGraph;

import java.sql.Timestamp;

import com.gbetododc.MSAuthGraph.MSenvJson.MSenv;
import com.google.gson.Gson;

import io.github.cdimascio.dotenv.Dotenv;
import kong.unirest.Unirest;

public class MsAuth {
    static Dotenv dotenv = Dotenv.configure().load();

    public static String getAuthurl() {
        MSenv msenvJson = MSenvJson.getMSenv();
        String authurl = new String(msenvJson.getReqCredentials().getUrl_auth() + "?client_id=" + msenvJson.getReqCredentials().getClientId() + "&response_type=code&response_mode=query&scope=" + msenvJson.getReqCredentials().getScopes().get(0) + "%20" + msenvJson.getReqCredentials().getScopes().get(1) + "&redirect_uri=" + msenvJson.getReqCredentials().getRedirect_URI());
        // TODO: Foreach loop to set all the scopes
        return authurl;
    }

    public static void tokenAuthcode(String authcode, Runnable success) {
        MSenv msenvJson = MSenvJson.getMSenv();
        try {
            Unirest.post(msenvJson.getReqCredentials().getUrl_token())
                .field("grant_type", "authorization_code")
                .field("code", authcode)
                .field("client_secret", dotenv.get("MSAUTHCLIENTSECRET"))
                .field("client_id", msenvJson.getReqCredentials().getClientId())
                .field("redirect_uri", msenvJson.getReqCredentials().getRedirect_URI())
                .field("scope", (msenvJson.getReqCredentials().getScopes().get(0) + " " + msenvJson.getReqCredentials().getScopes().get(1)))// TODO: Foreach loop to set all the scopes
                .asJsonAsync(
                    response -> {
                        if (response.getStatus() == 200) {
                            saveTKresponse(response.getBody().toString());
                            success.run();
                        }
                        else {
                            System.out.println(response.getBody());
                        }
                    }
                );
        } catch (Throwable e) {
            System.out.println("ERROR WHILE SENDING AUTH REQUEST \n" + e);
        }
    }
    public static void tokenRT(String rftoken, Runnable success) {
        MSenv msenvJson = MSenvJson.getMSenv();
        try {
            Unirest.post(msenvJson.getReqCredentials().getUrl_token())
                .field("grant_type", "refresh_token")
                .field("refresh_token", rftoken)
                .field("client_secret", dotenv.get("MSAUTHCLIENTSECRET"))
                .field("client_id", msenvJson.getReqCredentials().getClientId())
                .field("redirect_uri", msenvJson.getReqCredentials().getRedirect_URI())
                .field("scope", (msenvJson.getReqCredentials().getScopes().get(0) + " " + msenvJson.getReqCredentials().getScopes().get(1))) // TODO: Foreach loop to set all the scopes
                .asJsonAsync(
                    response -> {
                        if (response.getStatus() == 200) {
                            saveTKresponse(response.getBody().toString());
                            success.run();
                        }
                        else {
                            System.out.println(response.getBody());
                        }
                    }
                );
        } catch (Throwable e) {
            System.out.println("ERROR WHILE SENDING AUTH REQUEST \n" + e);
        }
    }

    // TODO: ERROR Code Logger und verarbeitung, wenn irgendwas zu lange abgelaufen ist -> dm to devs
    public static void saveTKresponse(String tkrespString) {
        TKresp tkresp = new Gson().fromJson(tkrespString, TKresp.class);
        MSenv msenv = MSenvJson.getMSenv();
        msenv.getValues().setToken(tkresp.getAccessToken());
        msenv.getValues().setRFToken(tkresp.getRefreshToken());
        msenv.getValues().setExpiryDate((new Timestamp(System.currentTimeMillis() + (tkresp.getExpiresIn()*1000))).toString());

        MSenvJson.saveMSenv(msenv);
    }

    public class TKresp {
        // private String token_type;
        // private String scope;
        private Integer expires_in;
        // private Integer ext_expires_in;
        private String access_token;
        private String refresh_token;

        public Integer getExpiresIn() {return this.expires_in;}
        public String getAccessToken() {return this.access_token;}
        public String getRefreshToken() {return this.refresh_token;}
    }

}