package com.group21.ci;

public class RepositoryInfo {
    String ref;
    String commitId;
    String cloneUrl;
    String owner;
    String name;

    public RepositoryInfo(String ref, String commitId, String cloneUrl, String owner, String name){
        this.ref = ref;
        this.commitId = commitId;
        this.cloneUrl = cloneUrl;
        this.owner = owner;
        this.name = name;
    }
    
}
