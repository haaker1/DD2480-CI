package com.group21.ci;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;

public class StatusSender {
    private String owner;
    private String repositoryName;
    private String SHA;
    private HttpClient statusHttpClient;
    private String buildIdentifier;
    private String successDescription;
    private String pendingDescription;
    private String failureDescription;
    private String errorDescription;


    private static final String STATE_ERROR = "error";
    private static final String STATE_FAILURE = "failure";
    private static final String STATE_PENDING = "pending";
    private static final String STATE_SUCCESS = "success";
    private static final String DEFAULT_ERROR_DESCRIPTION = "An error occurred during the build";
    private static final String DEFAULT_FAILURE_DESCRIPTION = "Build has failed";
    private static final String DEFAULT_PENDING_DESCRIPTION = "Build has begun on the CI server";
    private static final String DEFAULT_SUCCESS_DESCRIPTION = "Build success";
    
    /**
     * StatusSender constructor
     * @param repo    The RepositoryInfo representation of the repository
     * @param id      Local identifier for the repo/build
     */
    public StatusSender(RepositoryInfo repo, String id) {
        this.owner = repo.owner;
        this.repositoryName = repo.name;
        this.SHA = repo.commitId;
        statusHttpClient = HttpClient.newHttpClient();
        this.buildIdentifier = id;
        
        errorDescription = "";
        failureDescription = "";
        pendingDescription = "";
        successDescription = "";

        if(Config.CUSTOM_SUCCESS_DESCRIPTION != null) {
            successDescription = TextSanitizer.sanitize(Config.CUSTOM_SUCCESS_DESCRIPTION); 
        }
        if(successDescription.isEmpty()) {
            successDescription = TextSanitizer.sanitize(DEFAULT_SUCCESS_DESCRIPTION);
        }

        if(Config.CUSTOM_PENDING_DESCRIPTION != null) {
            pendingDescription = TextSanitizer.sanitize(Config.CUSTOM_PENDING_DESCRIPTION);     
        }
        if(pendingDescription.isEmpty()) {
            pendingDescription = TextSanitizer.sanitize(DEFAULT_PENDING_DESCRIPTION);
        }

        if(Config.CUSTOM_FAILURE_DESCRIPTION != null) {
            failureDescription = TextSanitizer.sanitize(Config.CUSTOM_FAILURE_DESCRIPTION);     
        }
        if(failureDescription.isEmpty()) {
            failureDescription = TextSanitizer.sanitize(DEFAULT_FAILURE_DESCRIPTION);
        }

        if(Config.CUSTOM_ERROR_DESCRIPTION != null) {
            errorDescription = TextSanitizer.sanitize(Config.CUSTOM_ERROR_DESCRIPTION);     
        }
        if(errorDescription.isEmpty()) {
            errorDescription = TextSanitizer.sanitize(DEFAULT_ERROR_DESCRIPTION);
        }

    }

    /**
     *  sendErrorStatus
     *      - send a POST request to the repo to set commit status to error,
     *      with a short description.
     *      - the error status indicates the CI crashed and the job
     *      could not be finished.
     */
    public void sendErrorStatus() {
        System.out.println("Sent ERROR status");
        String sanitizedDescription = TextSanitizer.sanitize(errorDescription);
        sendStatus(STATE_ERROR, sanitizedDescription);
    }

    /**
     *  sendFailureStatus
     *      - send a POST request to the repo to set commit status to failure,
     *      with a short description.
     *      - the failure status indicates the CI did not crash but a problem occured
     *      (such as a test failling).
     */
    public void sendFailureStatus() {
        System.out.println("Sent FAILURE status");
        String sanitizedDescription = TextSanitizer.sanitize(failureDescription);
        sendStatus(STATE_FAILURE, sanitizedDescription);
    }

    /**
     *  sendPendingStatus
     *      - send a POST request to the repo to set commit status to pending,
     *      with a short description.
     *      - the pending status indicates the CI is currently running and will update the
     *      status when it finishes.
     */
    public void sendPendingStatus() {
        System.out.println("Sent PENDING status");
        String sanitizedDescription = TextSanitizer.sanitize(pendingDescription);
        sendStatus(STATE_PENDING, sanitizedDescription);
    }

    /**
     *  sendSuccessStatus
     *      - send a POST request to the repo to set commit status to success,
     *      with a short description.
     *      - the success status indicates the CI finished its work and returned normally
     */
    public void sendSuccessStatus() {
        System.out.println("Sent SUCCESS status");
        String sanitizedDescription = TextSanitizer.sanitize(successDescription);
        sendStatus(STATE_SUCCESS, sanitizedDescription);
    }


    // Helper methods
    /**
     * sendStatus
     * A helper method that sends a request to set the commit status defined in the constructor.
     * @param state the state of the commit status (see STATE_ERROR, etc.)
     * @param description a short description for the status to be set
     */
    private void sendStatus(String state, String description) {
        try {
            HttpResponse<String> response = statusHttpClient.send(
                    requestBuilder(state, description),
                    HttpResponse.BodyHandlers.ofString()
            );
            System.out.println("Received code: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    
    }

    /**
     * getStatusUrl
     *      - a helper method that creates the URL of the API to update the commit status 
     * @return the URL of the API
     */
    private String getStatusUrl() {
        return "https://api.github.com/repos/" + owner + "/" + repositoryName + "/statuses/" + SHA;
    }

    /**
     * requestBuilder
     *      - creates, builds and returns a POST request to set the status 
     * @param status    the status to set for the commit, must be "success",
     *                  "pending", "error" or "failure"
     * @param description   a short description associated with the commit status
     * @return the built request
     */
    public HttpRequest requestBuilder(String status, String description) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(getStatusUrl()))
            // Headers
            .header("Authorization", "Bearer " + Config.GITHUB_TOKEN)
            .header("Accept", "application/vnd.github+json")
            .header("X-GitHub-Api-Version", "2022-11-28")
            // Body (inside a function declaring the HTTP method, here POST)
            .POST(HttpRequest.BodyPublishers.ofString("{\"state\":\"" + status + "\"" 
                + "," + "\"description\":" + "\"" + description + "\""
                + "," + "\"context\":\"project-continuous-integration-server\""
                + "," + "\"target_url\":\"" + Config.HISTORY_URL + "/" + buildIdentifier + "\""
                + "}"))
            .build();
            return request;
    }

    /**
     * @return the description that will be set for a success commit status
     */
    public String getSuccessDescription() {
        return this.successDescription;
    }

    /**
     * @return the description that will be set for a pending commit status
     */
    public String getPendingDescription() {
        return this.pendingDescription;
    }

    /**
     * @return the description that will be set for an errpr commit status
     */
    public String getErrorDescription() {
        return this.errorDescription;
    }

    /**
     * @return the description that will be set for a success commit status
     */
    public String getFailureDescription() {
        return this.failureDescription;
    }
    
}
