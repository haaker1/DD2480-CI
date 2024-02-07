package com.group21.ci;


public class StatusSender {
    private String owner;
    private String repositoryName;
    private String SHA;
    
    public StatusSender(String owner, String repositoryName, String SHA) {
        this.owner = owner;
        this.repositoryName = repositoryName;
        this.SHA = SHA;
    }

    public void sendErrorStatus() {

    }

    public void sendFailureStatus() {

    }

    public void sendPendingStatus() {

    }

    public void sendSuccessStatus() {

    }
    
}
