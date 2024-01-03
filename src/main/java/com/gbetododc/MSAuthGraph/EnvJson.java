package com.gbetododc.MSAuthGraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.gbetododc.DiscordBot.DiscordBot;
import com.google.gson.Gson;

import okio.Path;

public class EnvJson {
    static String jsonFilePath = DiscordBot.PROJPATH + "\\src\\main\\java\\com\\gbetododc\\MSAuthGraph\\MSenv.json";

    public static void main(String[] args) {
        try {
            String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            Gson gson = new Gson();
            MSenv json = gson.fromJson(jsonData, MSenv.class);
    
            // Access and print individual fields
            System.out.println(json.getReqCredentials().getClientId());

            json.getValues().setToken("1234TEST");
            System.out.println(json.getValues().getToken());

            save(json.toString());
        } catch (IOException e) {}
    }
    

    public static Boolean save(String JsonString) {
        try {
            Files.write(Paths.get(jsonFilePath), JsonString.getBytes());
        } catch (IOException  e) {
        }
        return null;
    }

    public class MSenv {
        private Values values;
        private ReqCredentials ReqCredentials;
    
        public Values getValues() {return values;}
        public ReqCredentials getReqCredentials() {return ReqCredentials;}
    }
    class Values {
        private String Token;
        private String RFToken;
        private String ExpiryDate;
    
        public String getToken() {return Token;}
        public void setToken(String TOKEN) {this.Token = TOKEN;}
    
        public String getRFToken() {return RFToken;}
        public void setRFToken(String RFTOKEN) {this.RFToken = RFTOKEN;}
    
        public String getExpiryDate() {return ExpiryDate;}
        public void setExpiryDate(String EXPIRYDATE) {this.ExpiryDate = EXPIRYDATE;}

    }
    class ReqCredentials {
        private String scopes;
        private String client_id;
        private String redirect_URI;
        private String url_auth;
        private String url_token;
        private String url_graph;
        private String ToDoListID;
    
        public String getScopes() {return scopes;}
        public void setScopes(String SCOPES) {this.scopes = SCOPES;}
    
        public String getClientId() {return client_id;}
        public void setClientId(String CLIENTID) {this.client_id = CLIENTID;}
    
        public String getRedirect_URI() {return redirect_URI;}
        public void setRedirect_URI(String REDIRECT_URI) {this.redirect_URI = REDIRECT_URI;}
    
        public String getUrl_auth() {return url_auth;}
        public void setUrl_auth(String URL_AUTH) {this.url_auth = URL_AUTH;}
    
        public String getUrl_token() {return url_token;}
        public void setUrl_token(String URL_TOKEN) {this.url_token = URL_TOKEN;}
    
        public String getUrl_graph() {return url_graph;}
        public void setUrl_graph(String URL_GRAPH) {this.url_graph = URL_GRAPH;}
    
        public String getToDoListID() {return ToDoListID;}
        public void setToDoListID(String TODOLIST_ID) {this.ToDoListID = TODOLIST_ID;}
    }
    
}
