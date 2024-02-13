package com.group21.ci;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    public static final String GITHUB_TOKEN = Dotenv.configure()
        .directory("..")
        .filename("config.env")
        .ignoreIfMissing()
        .load()
        .get("GITHUB_TOKEN", "");
    public static final String HISTORY_URL = Dotenv.configure()
        .directory("..")
        .filename("config.env")
        .ignoreIfMissing()
        .load()
        .get("HISTORY_URL", "http://localhost:8021/");
    public static final int PORT = 8021;
    public static final String DIRECTORY_REPOSITORIES = "./repos/";
    public static final String DIRECTORY_BUILD_HISTORY = "./build_history/";
    public static final String BUILD_LOG_FILENAME = "build_log.txt";
    public static final String BUILD_IDENTIFIER_FILENAME = "commit_identifier.txt";
    public static final String BUILD_BRANCH_FILENAME = "branch.txt";
}
