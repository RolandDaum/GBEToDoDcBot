package com.gbetododc.MSAuthGraph;

import java.sql.Timestamp;
import java.util.List;
import java.util.function.Consumer;
import com.gbetododc.MSAuthGraph.MSenvJson.MSenv;
import com.gbetododc.System.Logger;
import com.gbetododc.System.Logger.LogLvl;
import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import kong.unirest.Unirest;

public class MsAuth {
    static Dotenv dotenv = Dotenv.configure().load();

    public static String getAuthurl() {
        MSenv msenvJson = MSenvJson.getMSenv();
        List<String> scopeList = msenvJson.getReqCredentials().getScopes();
        String scopes = "";
        for (String scope : scopeList) {
            if (scopeList.getLast().equals(scope)) {
                scopes += scope;
                break;
            } else {
                scopes += (scope + "%20");
            }
        }
        String authurl = new String(
            msenvJson.getReqCredentials().getUrl_auth() + 
            "?client_id=" + msenvJson.getReqCredentials().getClientId() + 
            "&response_type=code" + 
            "&response_mode=query&scope=" + scopes +
            "&redirect_uri=" + msenvJson.getReqCredentials().getRedirect_URI()
        );
        return authurl;
    }

    // /**
    // * Get a Token and Refresh Token via the authcode
    // *
    // * @param authcode Authcode as a String
    // * @return 
    // */
    public static void tokenAuthcode(String authcode, Consumer<Boolean> callback) {
        MSenv msenvJson = MSenvJson.getMSenv();
        if (msenvJson == null) {
            Boolean success = false;
            callback.accept(success);
            return;
        }
        List<String> scopeList = msenvJson.getReqCredentials().getScopes();
        String scopes = "";
        for (String scope : scopeList) {
            if (scopeList.getLast().equals(scope)) {
                scopes += scope;
                break;
            } else {
                scopes += (scope + " ");
            }
        }
        try {
            Unirest.post(msenvJson.getReqCredentials().getUrl_token())
                .field("grant_type", "authorization_code")
                .field("code", authcode)
                .field("client_secret", dotenv.get("MSAUTHCLIENTSECRET"))
                .field("client_id", msenvJson.getReqCredentials().getClientId())
                .field("redirect_uri", msenvJson.getReqCredentials().getRedirect_URI())
                .field("scope", scopes)
                .asJsonAsync(
                    response -> {
                        Boolean success;
                        Integer statuscode = response.getStatus();
                        String respBody = response.getBody().toString();
                        switch (statuscode) {
                            case 200:
                                saveTKresponse(respBody);
                                success = true;
                                callback.accept(success);
                                break;
                        
                            case 400:
                                TKerrorResp errorResp = getTKerrorResp(respBody);
                                success = false;
                                callback.accept(success);
                                Logger.log(
                                    "MsAuth - RFTokenRq", 
                                    errorResp.getTimestamp() + " " + errorResp.getError() + "\n" + 
                                    errorResp.getErrorDescription() + "\n" +
                                    errorResp.getErrorUri(), 
                                    LogLvl.moderate
                                );
                                break;
                            default:
                                success = false;
                                callback.accept(success);
                                Logger.log("MsAuth - TokenReq with RF Token", "Error " + statuscode + "\n" + respBody, LogLvl.critical);
                                break;
                        }
                        Unirest.shutDown();
                    }
                );
        } catch (Exception error) {
            System.out.println("ERROR");
            error.printStackTrace();
        }

    }
    /**
     * Refresh the ms auth cred
     * @param callback
     */
    public static void tokenRT(Consumer<Boolean> callback) {
        MSenv msenvJson = MSenvJson.getMSenv();
        if (msenvJson == null) {
            Boolean success = false;
            callback.accept(success);
            return;
        }
        List<String> scopeList = msenvJson.getReqCredentials().getScopes();
        String scopes = "";
        for (String scope : scopeList) {
            if (scopeList.getLast().equals(scope)) {
                scopes += scope;
                break;
            } else {
                scopes += (scope + " ");
            }
        }
        try {
            Unirest.post(msenvJson.getReqCredentials().getUrl_token())
                .field("grant_type", "refresh_token")
                .field("refresh_token", msenvJson.getValues().getRFToken())
                .field("client_secret", dotenv.get("MSAUTHCLIENTSECRET"))
                .field("client_id", msenvJson.getReqCredentials().getClientId())
                .field("redirect_uri", msenvJson.getReqCredentials().getRedirect_URI())
                .field("scope", scopes)
                .asJsonAsync(
                    response -> {
                        Boolean success;
                        Integer statuscode = response.getStatus();
                        String respBody = response.getBody().toString();
                        switch (statuscode) {
                            case 200:
                                saveTKresponse(respBody);
                                success = true;
                                callback.accept(success);
                                break;
                        
                            case 400:
                                success = false;
                                callback.accept(success);
                                TKerrorResp errorResp = getTKerrorResp(respBody);
                                Logger.log(
                                    "MsAuth - RFTokenRq", 
                                    errorResp.getTimestamp() + " " + errorResp.getError() + "\n" + 
                                    errorResp.getErrorDescription() + "\n" +
                                    errorResp.getErrorUri(), 
                                    LogLvl.moderate
                                );
                                break;

                            default:
                                success = false;
                                callback.accept(success);
                                Logger.log("MsAuth - TokenReq with RF Token", "Error " + statuscode + "\n" + respBody, LogLvl.critical);
                                break;
                        }
                        Unirest.shutDown();
                    }
                );
        } catch (Exception error) {
            System.out.println("ERROR");
            error.printStackTrace();
        }
    }

    public static void saveTKresponse(String tkrespString) {
        TKresp tkresp = new Gson().fromJson(tkrespString, TKresp.class);
        MSenv msenvJson = MSenvJson.getMSenv();
        if (msenvJson.equals(null)) {
            return;
        }
        msenvJson.getValues().setToken(tkresp.getAccessToken());
        msenvJson.getValues().setRFToken(tkresp.getRefreshToken());
        msenvJson.getValues().setExpiryDate((new Timestamp(System.currentTimeMillis() + (tkresp.getExpiresIn()*1000))).toString());

        MSenvJson.saveMSenv(msenvJson);
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

    public static TKerrorResp getTKerrorResp(String errorResp) {
        Gson gson = new Gson();
        TKerrorResp tkerrorresp = gson.fromJson(errorResp, TKerrorResp.class);
        return tkerrorresp;
    }
    public class TKerrorResp {
        private String error;
        private String error_description;
        private List<Integer> error_codes;  // Use List for error_codes
        private String timestamp;
        private String trace_id;
        private String correlation_id;
        private String error_uri;
    
        public String getError() { return this.error; }
        public String getErrorDescription() { return this.error_description; }
        public List<Integer> getErrorCodes() { return this.error_codes; }
        public String getTimestamp() { return this.timestamp; }
        public String getTraceId() { return this.trace_id; }
        public String getCorrelationId() { return this.correlation_id; }
        public String getErrorUri() { return this.error_uri; }
    }
    
}