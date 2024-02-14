package com.group21.ci;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;

import org.junit.Assert.*;

public class CompilingTest {
    
    @Test
    public void testCreatedFilesFound() throws IOException {
        String file = "target/maven-status/maven-compiler-plugin/compile/default-compile/createdFiles.lst";
        Scanner scanner = new Scanner(new File(file));
        scanner.useDelimiter("\n");

        assertTrue(scanner.hasNext());
        assertEquals("com/group21/ci/ContinuousIntegrationServer$1.class", 
                    scanner.next());
        assertEquals("com/group21/ci/ContinuousIntegrationServer.class", 
                    scanner.next());
        assertEquals("com/group21/ci/RepositoryTester.class", 
                    scanner.next());
        assertEquals("com/group21/ci/ContinuousIntegrationServer$2.class", 
                    scanner.next());
        assertEquals("com/group21/ci/Config.class", 
                    scanner.next());
        assertEquals("com/group21/ci/TextSanitizer.class", 
                    scanner.next());
        assertEquals("com/group21/ci/RepositoryInfo.class", 
                    scanner.next());
        assertEquals("com/group21/ci/StatusSender.class", 
                    scanner.next());

        scanner.close();
    }

    /*
     * com/group21/ci/ContinuousIntegrationServer$1.class
com/group21/ci/ContinuousIntegrationServer.class
com/group21/ci/RepositoryTester.class
com/group21/ci/ContinuousIntegrationServer$2.class
com/group21/ci/Config.class
com/group21/ci/TextSanitizer.class
com/group21/ci/RepositoryInfo.class
com/group21/ci/StatusSender.class
     */

}
