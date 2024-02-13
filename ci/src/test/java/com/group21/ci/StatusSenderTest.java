package com.group21.ci;

import java.net.URI;
import java.net.http.*;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class StatusSenderTest {
    private StatusSender sender;

    @Before
    public void setUp() {
        RepositoryInfo repo = new RepositoryInfo("mockRef", "mockSHA", "mockURL", "mockOwner", "mockRepo");
        sender = new StatusSender(repo, "mockId");
    }

    /**
     * Test that verifies the body of the 
     */
    @Test
    public void ExpectedStringOutputByRequestBuilderTest() {
        HttpRequest builtRequest = sender.requestBuilder("status", "description");

        HttpRequest expectedRequest = HttpRequest.newBuilder()
            .uri(URI.create("https://api.github.com/repos/mockOwner/mockRepo/statuses/mockSHA"))
            .header("Authorization", "Bearer " + Config.GITHUB_TOKEN)
            .header("Accept", "application/vnd.github+json")
            .header("X-GitHub-Api-Version", "2022-11-28")
            .POST(HttpRequest.BodyPublishers.ofString(
                "{\"state\":\"status\",\"description\":\"description\",\"context\":\"project-continuous-integration-server\""
                + "}"))
            .build();

            assertTrue(expectedRequest.equals(builtRequest));
    }

}
