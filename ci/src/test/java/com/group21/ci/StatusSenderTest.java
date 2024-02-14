package com.group21.ci;

import java.net.URI;
import java.net.http.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
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

    @Test
    public void checkCustomStatusSetIfGiven() {
        RepositoryInfo repo = new RepositoryInfo("mockRef", "mockSHA", "mockURL", "mockOwner", "mockRepo");
        Config.CUSTOM_FAILURE_DESCRIPTION = "failure it is then";
        Config.CUSTOM_ERROR_DESCRIPTION = "error shall not be permitted";
        Config.CUSTOM_PENDING_DESCRIPTION = "be patient it is working";
        Config.CUSTOM_SUCCESS_DESCRIPTION = "your greatest success";

        StatusSender statusSender = new StatusSender(repo, "mockId");

        assertEquals(statusSender.getSuccessDescription(), Config.CUSTOM_SUCCESS_DESCRIPTION);
        assertEquals(statusSender.getPendingDescription(), Config.CUSTOM_PENDING_DESCRIPTION);
        assertEquals(statusSender.getErrorDescription(), Config.CUSTOM_ERROR_DESCRIPTION);
        assertEquals(statusSender.getFailureDescription(), Config.CUSTOM_FAILURE_DESCRIPTION); 
    }

    @Test
    public void checkCustomStatusIsSanitized() {
        RepositoryInfo repo = new RepositoryInfo("mockRef", "mockSHA", "mockURL", "mockOwner", "mockRepo");
        Config.CUSTOM_FAILURE_DESCRIPTION = "failure @ it is then";
        Config.CUSTOM_ERROR_DESCRIPTION = "error, shall not\" be permitted";
        Config.CUSTOM_PENDING_DESCRIPTION = "-be patient it is working";
        Config.CUSTOM_SUCCESS_DESCRIPTION = "your greatest success!";

        StatusSender statusSender = new StatusSender(repo, "mockId");

        assertEquals(statusSender.getSuccessDescription(), "your greatest success");
        assertEquals(statusSender.getPendingDescription(), "-be patient it is working");
        assertEquals(statusSender.getErrorDescription(), "error shall not be permitted");
        assertEquals(statusSender.getFailureDescription(), "failure it is then");
    }

    @Test
    public void checkNoStatusMadeIsResetted() {
        RepositoryInfo repo = new RepositoryInfo("mockRef", "mockSHA", "mockURL", "mockOwner", "mockRepo");
        Config.CUSTOM_FAILURE_DESCRIPTION = null;
        Config.CUSTOM_ERROR_DESCRIPTION = null;
        Config.CUSTOM_PENDING_DESCRIPTION = null;
        Config.CUSTOM_SUCCESS_DESCRIPTION = null;

        StatusSender statusSender = new StatusSender(repo, "mockId");

        assertEquals(statusSender.getSuccessDescription(), "Build success");
        assertEquals(statusSender.getPendingDescription(), "Build has begun on the CI server");
        assertEquals(statusSender.getErrorDescription(), "An error occurred during the build");
        assertEquals(statusSender.getFailureDescription(), "Build has failed");
    }

    @Test
    public void checkemptyStatusIsResetted() {
        RepositoryInfo repo = new RepositoryInfo("mockRef", "mockSHA", "mockURL", "mockOwner", "mockRepo");
        Config.CUSTOM_FAILURE_DESCRIPTION = "";
        Config.CUSTOM_ERROR_DESCRIPTION = "";
        Config.CUSTOM_PENDING_DESCRIPTION = "";
        Config.CUSTOM_SUCCESS_DESCRIPTION = "";

        StatusSender statusSender = new StatusSender(repo, "mockId");

        assertEquals(statusSender.getSuccessDescription(), "Build success");
        assertEquals(statusSender.getPendingDescription(), "Build has begun on the CI server");
        assertEquals(statusSender.getErrorDescription(), "An error occurred during the build");
        assertEquals(statusSender.getFailureDescription(), "Build has failed");
    }

}
