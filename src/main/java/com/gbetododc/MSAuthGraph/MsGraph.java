package com.gbetododc.MSAuthGraph;

import java.util.List;
import com.google.gson.Gson;
import kong.unirest.Unirest;

public class MsGraph {
    public static void main(String[] args) {
        Unirest.get("https://graph.microsoft.com/v1.0/me/todo/lists")
            .header(
                "Authorization",
                "Bearer EwB4A8l6BAAUs5+HQn0N+h2FxWzLS31ZgQVuHsYAAYggceCAt57v4RAo2gMzgvaBxUA8pboNLrLa65NRTIs1yUdVG8aCduVXudGGSZ4FNkq8v3H0eShk0DwOBkeklXGLAGhp5h9eeftk6MzfAvwqU1ncpM1Gyx6xbPnTcWmCCGbucYN3EgXxRnJfLKq8hwAQ4m2H+9sNhTSfNvPTiQIu1UEpoCgtJ3BLp3pmR+b8Lhm+M3XgFz1wwUMOBByoXOSoexbK02/7NXi+2WEw1G/DUmOyn/ITP1rZUVic2Y5jmm0WPaxonk3ehSqzaPMMo/znhTS24l3U+LLjuGj/abkwGLNvsRV1b9EoMWILvarLrJtRyeHem1difHLi9h+zQk4DZgAACEdVax9tP4YRSALRb3k1IAcLet6qx1ISTejeYTVGa+cuqRogsxleo54+NQJ9eH+fX8XZjj6QCUkNrwmidgDaJWA2qCpz4OIysn1e+SwXb0pf06MubJB9l95bYIcwdV4kw41rlItyNhtwsONaU4FXzowqV9Qs8sY/R6aMaYXMopGoegd9Umm9Wyt8TNbMa4FJfJ/uZg9geLyAIQTkq3vVcWxMzuxD1IqXt8t9+Y9MW/pNevS+SS2tw/6V/1K55TXhRnNfRNNs+3pizPBe2jch5nKYe7yjPzke1/ANMzshg4yIRiXaNPBNHyMOCAQAawZoo5fTsPH0N1qPIQ3h4IKqvOfX2w5gJvdw5tlvxy3/87511SycWmH6VlLw92nFEN+oroDE/QKpJVbf/bv3YqWDpsAZzqiPjPpROmwW91q1XaIgjuDDn9RTcd1y8+x0lCLgyCTIX28XYq0BEsWxvjdQ6T1e4gZJDxiPJjIPOSr+skooYOJa2CiLBNo/lvRJTMSKf15T6sQNPSg+bqBL/uPIr6WYjT/yCK9FadZsrIHa8o9elqX+nhmEkYebUzC7DvyDCyJwbabgT0G1255QjOK1uFwCDiyfH7DN7BQMalZfFRbVh6d7L9JpoPAbKiWBWveKZkeBctHWy570GnvxYNHDMzlQVmAdksbgo0cXBqWq0CUPwi4AIfzi96AlANxV/6hvrYzOc76Z2SEtsSiuVRIFHjNP0o2dBlAD7Ol6LRPEmibEl4h2Be07OcLVqWAAgCncCaxPW2HRhSWXCT3t6dF7YQyLmnsC"
                )
            .asStringAsync(response -> {
                if (response.getStatus() != 200) {
                    System.out.println("ERROR " + response.getStatus() + ": " + response.getStatusText());
                } else {
                    Gson gson = new Gson();
                
                    // JSON-String in ein Java-Objekt umwandeln
                    TodoListResponse todoListResponse = gson.fromJson(response.getBody().toString(), TodoListResponse.class);
                    
                    // Auf die Daten im Java-Objekt zugreifen
                    List<TodoList> todoLists = todoListResponse.getValue();
                    for (TodoList todoList : todoLists) {
                        System.out.println("Display Name: " + todoList.getDisplayName());
                        System.out.println("Is Owner: " + todoList.isOwner());
                        System.out.println("Is Shared: " + todoList.isShared());
                        System.out.println("Wellknown List Name: " + todoList.getWellknownListName());
                        System.out.println("ID: " + todoList.getId());
                        System.out.println("---------------");
                    }
                
                }
                System.exit(0);
            });
    
    // Unirest.post("https://login.microsoftonline.com/common/oauth2/v2.0/token")
    //     .field("client_id", "7b3c0a44-153b-4676-a508-a22699ff49d0")
    //     .field("scope","Tasks.ReadWrite offline_access")
    //     .field("redirect_uri", "http://localhost:8000/register")
    //     .field("grant_type", "refresh_token")
    //     .field("client_secret", "RHW8Q~Et0D6P0xLFiP7F0HdmXymBCmrgPMS2VdeB")
    //     .field("refresh_token", "M.C105_SN1.-Con8ctbztcY*7ZgU14I97LROBgMYtxIyk!3p*FodpvdkwAT!WaiJ1vFcDiAdsWjcJY5D9Glio!pODDDgxy9SRNdqBSBcSsE9bTEor6kcWLNpQ75Pt6RJn0!8VVQlLIz9KY1GkZlJ5IhCrVB*3nt!BkCzkj5d9CXwmvjH9Flz1HIrIOIuxS3Kdp!m7MMru*A1QCQZrEICkCO8HPl5tZdwwFUpi*FsQGvrux9l5EQ2mY4gCBhpELcjAaXoLtIkkwHIG!LDOMYciQGG6loRX1QhTM*HhomCl8KkPmcn4h09M*9IwcJuxzy6g!nCkJ!HO*7IiA$$")
    //     .asJsonAsync(
    //         response -> {
    //             System.out.println(response.getBody());
    //         }
    //     );
    }

    public class TodoListResponse {
        private List<TodoList> value;

        // Getter und Setter für die Felder

        public List<TodoList> getValue() {
            return value;
        }

        public void setValue(List<TodoList> value) {
            this.value = value;
        }
    }
    public class TodoList {
        private String odataEtag;
        private String displayName;
        private boolean isOwner;
        private boolean isShared;
        private String wellknownListName;
        private String id;
    
        // Getter und Setter für die Felder
    
        public String getOdataEtag() {
            return odataEtag;
        }
    
        public void setOdataEtag(String odataEtag) {
            this.odataEtag = odataEtag;
        }
    
        public String getDisplayName() {
            return displayName;
        }
    
        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
    
        public boolean isOwner() {
            return isOwner;
        }
    
        public void setOwner(boolean owner) {
            isOwner = owner;
        }
    
        public boolean isShared() {
            return isShared;
        }
    
        public void setShared(boolean shared) {
            isShared = shared;
        }
    
        public String getWellknownListName() {
            return wellknownListName;
        }
    
        public void setWellknownListName(String wellknownListName) {
            this.wellknownListName = wellknownListName;
        }
    
        public String getId() {
            return id;
        }
    
        public void setId(String id) {
            this.id = id;
        }
    }
  
}
