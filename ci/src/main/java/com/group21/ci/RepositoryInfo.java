package com.group21.ci;

/**
 * A class for holding information about a GitHub repository.
 */
public class RepositoryInfo {
    String ref;
    String commitId;
    String cloneUrl;
    String owner;
    String name;

    /**
     * Constructs a RepositoryInfo object containing the specified repository information.
     * @param ref the name of the current branch
     * @param commitId the SHA of the latest commit
     * @param cloneUrl the URL to clone the repository
     * @param owner the owner of the repository
     * @param name the name of the repository
     */
    public RepositoryInfo(String ref, String commitId, String cloneUrl, String owner, String name){
        this.ref = ref;
        this.commitId = commitId;
        this.cloneUrl = cloneUrl;
        this.owner = owner;
        this.name = name;
    }
    
}
