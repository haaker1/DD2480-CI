package com.group21.ci;

import java.net.URI;
import java.net.http.*;
import java.util.concurrent.CompletableFuture;

public class StatusSender {
    private String owner;
    private String repositoryName;
    private String SHA;
    private String statusUrl;
    private HttpClient statusHttpClient;
    CompletableFuture<HttpResponse<String>> response;
    
    public StatusSender(String owner, String repositoryName, String SHA) {
        this.owner = owner;
        this.repositoryName = repositoryName;
        this.SHA = SHA;
        statusUrl = getStatusUrl();
        statusHttpClient = HttpClient.newHttpClient();
    }

    public void sendErrorStatus() {
        response = statusHttpClient.sendAsync(requestBuilder("error", 
                                                "An error occured during the build"),
                                                HttpResponse.BodyHandlers.ofString());
    }

    public void sendFailureStatus() {
        response = statusHttpClient.sendAsync(requestBuilder("failure", 
                                                "Build has failed"),
                                                HttpResponse.BodyHandlers.ofString());
    }

    public void sendPendingStatus() {
        response = statusHttpClient.sendAsync(requestBuilder("pending", 
                                                "Build has begun on the CI server"),
                                                HttpResponse.BodyHandlers.ofString());
    }

    public void sendSuccessStatus() {
        response = statusHttpClient.sendAsync(requestBuilder("success", "Build success"), 
                                                HttpResponse.BodyHandlers.ofString());
    }


    // Helper methods
    private String getStatusUrl() {
        return "https://api.github.com/repos/" + owner + "/" + repositoryName + "/statuses/" + SHA;
    }

    public HttpRequest requestBuilder(String status, String description) {

        // Add a target_url?
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(getStatusUrl()))
            .header("Authorization", "Bearer " + Config.GITHUB_TOKEN)
            .header("Accept", "application/vnd.github+json")
            .header("X-GitHub-Api-Version", "2022-11-28")
            .POST(HttpRequest.BodyPublishers.ofString("{\"state\":\"" + status + "\"" 
                + "," + "\"description\":" + "\"" + description + "\""
                + "," + "\"context\":\"project-continuous-integration-server\""
                + "}"))
            .build();
            return request;
    }
    
}
