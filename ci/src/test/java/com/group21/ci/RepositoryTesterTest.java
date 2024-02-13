package com.group21.ci;

import static org.junit.Assert.assertTrue;

import org.junit.*;

public class RepositoryTesterTest {

    /**
     * Positive test for compilation. The RepositoryTester's runTest method
     * clones and compiles the project on a known, working commit, and returns
     * exit code 0 (all went well)
     */
    @Test
    public void testCompilationOnWorkingCommit() {
        RepositoryInfo repo = new RepositoryInfo("main",
                                        "baeaaec85b5a3b480d842e1fa8a5253405f054b3",
                                        "https://github.com/alexarne/DD2480-CI.git",
                                           "alexarne",
                                            "DD2480-CI");
        RepositoryTester tester = new RepositoryTester(repo);
        int exitCode = tester.runTests();
        assertTrue(exitCode == 0);
    }


    /**
     * Negative test for compilation. The RepositoryTester's runTest method
     * clones the project but cannot find the commit, whose id is wrong
     */
    @Test
    public void testCompilationOnUnexistingCommit() {
        RepositoryInfo repo = new RepositoryInfo("main",
                                        "baeaaec85b5a3b480d842e1fa8a5253405f054b3",
                                        "https://github.com/alexarne/not-a-repo.git",
                                           "alexarne",
                                            "DD2480-CI");
        RepositoryTester tester = new RepositoryTester(repo);
        int exitCode = tester.runTests();
        assertTrue(exitCode != 0);
    }

}
