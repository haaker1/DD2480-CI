package com.group21.ci;

import static org.junit.Assert.assertTrue;

import org.junit.*;

public class RepositoryTesterTest {

    /**
     * Negative test for compilation. The RepositoryTester's runTest method
     * clones the project but cannot find the repo, as the url is wrong
     */
    @Test
    public void testCompilationOnUnexistingRepo() {
        RepositoryInfo repo = new RepositoryInfo("main",
                                        "baeaaec85b5a3b480d842e1fa8a5253405f054b3",
                                        "https://github.com/alexarne/not-a-repo.git",
                                           "alexarne",
                                            "DD2480-CI");
        RepositoryTester tester = new RepositoryTester(repo);
        int exitCode = tester.runTests();
        assertTrue(exitCode != 0);
    }

    // Note: a similar positive test was made but it ended up looping by checking itself. It was removed
    // in favor of CompulingTest.testCreatedFilesFound()
}
