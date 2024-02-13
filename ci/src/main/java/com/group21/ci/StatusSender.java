package com.group21.ci;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.CompletableFuture;

public class StatusSender {
    private String owner;
    private String repositoryName;
    private String SHA;
    private String statusUrl;
    private HttpClient statusHttpClient;
    private String buildIdentifier;
    
    public StatusSender(RepositoryInfo repo, String id) {
        this.owner = repo.owner;
        this.repositoryName = repo.name;
        this.SHA = repo.commitId;
        statusUrl = getStatusUrl();
        statusHttpClient = HttpClient.newHttpClient();
        this.buildIdentifier = id;
    }

    public void sendErrorStatus() {
        System.out.println("Sent ERROR status");
        try {
            HttpResponse<String> response = statusHttpClient.send(
                requestBuilder("error", "An error occured during the build"),
                HttpResponse.BodyHandlers.ofString()
            );
            System.out.println("Received code: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendFailureStatus() {
        System.out.println("Sent FAILURE status");
        try {
            HttpResponse<String> response = statusHttpClient.send(
                requestBuilder("failure", "Build has failed"),
                HttpResponse.BodyHandlers.ofString()
            );
            System.out.println("Received code: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendPendingStatus() {
        System.out.println("Sent PENDING status");
        try {
            HttpResponse<String> response = statusHttpClient.send(
                requestBuilder("pending", "Build has begun on the CI server"),
                HttpResponse.BodyHandlers.ofString()
            );
            System.out.println("Received code: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendSuccessStatus() {
        System.out.println("Sent SUCCESS status");
        try {
            HttpResponse<String> response = statusHttpClient.send(
                requestBuilder("success", "Build success"), 
                HttpResponse.BodyHandlers.ofString()
            );
            System.out.println("Received code: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
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
                + "," + "\"target_url\":\"" + Config.HISTORY_URL + "/" + buildIdentifier + "\""
                + "}"))
            .build();
            return request;
    }
    
}
