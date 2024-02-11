package com.group21.ci;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class ContinuousIntegrationServerTest 
{
    ContinuousIntegrationServer server;
    String gitHubPayload;

    @Before
    public void setUp(){
        server = new ContinuousIntegrationServer();
        gitHubPayload = "{\"ref\":\"refs/heads/main\", \"repository\": {\r\n" + //
                        "    \"id\": 754107114,\r\n" + //
                        "    \"node_id\": \"R_kgDOLPLC6g\",\r\n" + //
                        "    \"name\": \"DD2480-CI-TEST\",\r\n" + //
                        "    \"full_name\": \"haaker1/DD2480-CI-TEST\",\r\n" + //
                        "    \"private\": true,\r\n" + //
                        "    \"owner\": {\r\n" + //
                        "      \"name\": \"haaker1\",\r\n" + //
                        "      \"email\": \"104437718+haaker1@users.noreply.github.com\",\r\n" + //
                        "      \"login\": \"haaker1\",\r\n" + //
                        "      \"id\": 104437718,\r\n" + //
                        "      \"node_id\": \"U_kgDOBjmX1g\",\r\n" + //
                        "      \"avatar_url\": \"https://avatars.githubusercontent.com/u/104437718?v=4\",\r\n" + //
                        "      \"gravatar_id\": \"\",\r\n" + //
                        "      \"url\": \"https://api.github.com/users/haaker1\",\r\n" + //
                        "      \"html_url\": \"https://github.com/haaker1\",\r\n" + //
                        "      \"followers_url\": \"https://api.github.com/users/haaker1/followers\",\r\n" + //
                        "      \"following_url\": \"https://api.github.com/users/haaker1/following{/other_user}\",\r\n" + //
                        "      \"gists_url\": \"https://api.github.com/users/haaker1/gists{/gist_id}\",\r\n" + //
                        "      \"starred_url\": \"https://api.github.com/users/haaker1/starred{/owner}{/repo}\",\r\n" + //
                        "      \"subscriptions_url\": \"https://api.github.com/users/haaker1/subscriptions\",\r\n" + //
                        "      \"organizations_url\": \"https://api.github.com/users/haaker1/orgs\",\r\n" + //
                        "      \"repos_url\": \"https://api.github.com/users/haaker1/repos\",\r\n" + //
                        "      \"events_url\": \"https://api.github.com/users/haaker1/events{/privacy}\",\r\n" + //
                        "      \"received_events_url\": \"https://api.github.com/users/haaker1/received_events\",\r\n" + //
                        "      \"type\": \"User\",\r\n" + //
                        "      \"site_admin\": false\r\n" + //
                        "    },     \"clone_url\": \"https://github.com/haaker1/DD2480-CI-TEST.git\",\r\n},\"head_commit\": {\r\n" + //
                        "    \"id\": \"944955a73f18f5cbfae6d66da4688e3d1badee04\",\r\n" + //
                        "    \"tree_id\": \"4bfe6e16e9558c2d3319ba42c196f7e3df937c72\",\r\n" + //
                        "    \"distinct\": true,\r\n" + //
                        "    \"message\": \"saker\",\r\n" + //
                        "    \"timestamp\": \"2024-02-07T15:15:27+01:00\",\r\n" + //
                        "    \"url\": \"https://github.com/haaker1/DD2480-CI-TEST/commit/944955a73f18f5cbfae6d66da4688e3d1badee04\",\r\n" + //
                        "    \"author\": {\r\n" + //
                        "      \"name\": \"haaker1\",\r\n" + //
                        "      \"email\": \"haaker.anne@gmail.com\",\r\n" + //
                        "      \"username\": \"haaker1\"\r\n" + //
                        "    },\r\n" + //
                        "  }}";
    }
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    /**
     * Assert that the branch name extracted from the payload in readPostData is correct.
     */
    @Test
    public void readPostDataReturnsCorrectRepoBranch(){
        Reader inputString = new StringReader(gitHubPayload);
        BufferedReader reader = new BufferedReader(inputString);
        RepositoryInfo repo = server.readPostData(reader);
        RepositoryInfo trueRepo = new RepositoryInfo("main", "944955a73f18f5cbfae6d66da4688e3d1badee04", "https://github.com/haaker1/DD2480-CI-TEST.git", "haaker1", "DD2480-CI-TEST");
        assertEquals(trueRepo.ref, repo.ref);
    }

    /**
     * Assert that the commit id (SHA) extracted from the payload in readPostData is correct.
     */
    @Test
    public void readPostDataReturnsCorrectCommitId(){
        Reader inputString = new StringReader(gitHubPayload);
        BufferedReader reader = new BufferedReader(inputString);
        RepositoryInfo repo = server.readPostData(reader);
        RepositoryInfo trueRepo = new RepositoryInfo("main", "944955a73f18f5cbfae6d66da4688e3d1badee04", "https://github.com/haaker1/DD2480-CI-TEST.git", "haaker1", "DD2480-CI-TEST");
        assertEquals(trueRepo.commitId, repo.commitId);
    }

    /**
     * Assert that the clone URL extracted from the payload in readPostData is correct.
     */
    @Test
    public void readPostDataReturnsCorrectCloneUrl(){
        Reader inputString = new StringReader(gitHubPayload);
        BufferedReader reader = new BufferedReader(inputString);
        RepositoryInfo repo = server.readPostData(reader);
        RepositoryInfo trueRepo = new RepositoryInfo("main", "944955a73f18f5cbfae6d66da4688e3d1badee04", "https://github.com/haaker1/DD2480-CI-TEST.git", "haaker1", "DD2480-CI-TEST");
        assertEquals(trueRepo.cloneUrl, repo.cloneUrl);
    }
}
