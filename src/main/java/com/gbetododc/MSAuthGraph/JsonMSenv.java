package com.gbetododc.MSAuthGraph;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;
import com.gbetododc.System.Logger;
import com.gbetododc.System.Logger.LogLvl;
import com.google.gson.Gson;

public class JsonMSenv {
    static String jsonFilePathString = new File("src/main/java/com/gbetododc/MSAuthGraph/MSenv.json").getAbsolutePath();

    /**
     * Get the MSenv.json as an MSenv Obj
     * @return MSenv object
     */
    public static MSenv getMSenv() {
        try {
            String jsonDataString = new String(Files.readAllBytes(Paths.get(jsonFilePathString)));
            Gson gson = new Gson();
            MSenv Gjson = gson.fromJson(jsonDataString, MSenv.class);
            return Gjson;
        } catch (Throwable e) {
            Logger.log("MSenvJson - getMSenv", "Failed to read MSenv.json. It may be missing or currepted", LogLvl.critical);
            return null;
        }
    }
    
    /**
     * Saves the provided MSenv Obj
     * @param Gjson as MSenv Object
     * @callback saved as Boolean
     */
    public static void saveMSenv(MSenv Gjson, Consumer<Boolean> callback) {
        try (FileWriter filewriter = new FileWriter(jsonFilePathString)) {
            Gson gson = new Gson();
            filewriter.write(gson.toJson(Gjson));
            filewriter.close();

            Boolean saved = true;
            callback.accept(saved);
        }
        catch (Throwable e) {
            Boolean saved = false;
            callback.accept(saved);
        }
    }

    public class MSenv {
        private Values values;
        private ReqCredentials ReqCredentials;
    
        public Values getValues() {return values;}
        public ReqCredentials getReqCredentials() {return ReqCredentials;}
    }
    class Values {
        private String TokenType;
        private String Token;
        private String RFToken;
        private String ExpiryDate;
    
        public String getTokenType() {return TokenType;}
        public void setTokenType(String TokenType) {this.TokenType = TokenType;}
        
        public String getToken() {return Token;}
        public void setToken(String TOKEN) {this.Token = TOKEN;}
    
        public String getRFToken() {return RFToken;}
        public void setRFToken(String RFTOKEN) {this.RFToken = RFTOKEN;}
    
        public String getExpiryDate() {return ExpiryDate;}
        public void setExpiryDate(String timestamp) {this.ExpiryDate = timestamp;}

    }
    class ReqCredentials {
        private List<String> scopes;
        private String client_id;
        private String redirect_URI;
        private String url_auth;
        private String url_token;
        private String url_graph;
        private String ToDoListID;
    
        public List<String> getScopes() {return scopes;}
        public void setScopes(List<String> SCOPES) {this.scopes = SCOPES;}
    
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
