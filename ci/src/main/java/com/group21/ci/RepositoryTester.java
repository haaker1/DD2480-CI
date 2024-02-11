package com.group21.ci;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.regex.Pattern;

public class RepositoryTester {
    private String URL;
    private String SHA;
    private String branch;
    private String owner;
    private String repositoryName;

    public RepositoryTester(RepositoryInfo repo) {
        this.owner = repo.owner;
        this.repositoryName = repo.name;
        this.URL = repo.cloneUrl;
        this.SHA = repo.commitId;
        this.branch = repo.ref;
    }
    
    /**
     * Clone and test the repository and save data from the processes ran in log files.
     * @return the exit code from the processes ran
     */
    public int runTests() {
        String id = generateUniqueIdentifier();
        String dir = Config.DIRECTORY_REPOSITORIES + id;
        File logFile = new File(Config.DIRECTORY_BUILD_HISTORY + id + "/" + Config.BUILD_LOG_FILENAME);
        File SHAFile = new File(Config.DIRECTORY_BUILD_HISTORY + id + "/" + Config.BUILD_IDENTIFIER_FILENAME);
        File branchFile = new File(Config.DIRECTORY_BUILD_HISTORY + id + "/" + Config.BUILD_BRANCH_FILENAME);
        logFile.getParentFile().mkdirs();
        
        //Os detection
        String osName = System.getProperty("os.name").toLowerCase();
        boolean isWindows = osName.contains("windows");

        // Clone, checkout the branch that was pushed to and run test.sh
        int exitCode = -99;
        try {
            logFile.createNewFile();
            SHAFile.createNewFile();
            branchFile.createNewFile();
            if (isWindows){
                ProcessBuilder process = new ProcessBuilder("cmd", "/c", "git", "clone", URL, dir);
                process.redirectErrorStream(true);
                process.redirectOutput(Redirect.appendTo(logFile));
                process.redirectError(Redirect.appendTo(logFile));
                process.start().waitFor();
                process.directory(new File(dir));
                process.command("ls");
                process.start().waitFor();
                process.command("cmd", "/c", "test.bat");
                exitCode = process.start().waitFor();
            }
            else{
                ProcessBuilder process = new ProcessBuilder("git", "clone", URL, dir);
                process.redirectErrorStream(true);
                process.redirectOutput(Redirect.appendTo(logFile));
                process.redirectError(Redirect.appendTo(logFile));
                process.start().waitFor();
                process.directory(new File(dir));
                process.command("git", "checkout", branch);
                process.start().waitFor();
                process.command("bash",  "test.sh");
                exitCode = process.start().waitFor();
            }
            
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            try {
                FileOutputStream fs = new FileOutputStream(logFile, true);
                PrintStream ps = new PrintStream(fs);
                e.printStackTrace(ps);
                ps.close();
                fs.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        
        // Delete repo regardless
        try {
            if (isWindows){
                ProcessBuilder process = new ProcessBuilder("cmd", "/c", "rmdir", "/s", "/q", id);
                process.directory(new File(Config.DIRECTORY_REPOSITORIES));
                process.start().waitFor();
            }
            else{
                ProcessBuilder process = new ProcessBuilder("rm", "-rf", id);
                process.directory(new File(Config.DIRECTORY_REPOSITORIES));
                process.start().waitFor();
            }
            
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        appendToFile(SHA, SHAFile);
        appendToFile(branch, branchFile);
        appendToFile(id + ": Exit code " + exitCode, logFile);
        System.out.println(id + ": Exit code " + exitCode);
        cleanFile(logFile);
        return exitCode;
    }

    /**
     * Append data to end of a file.
     * @param data the data to write to the file
     * @param file the file that should be written to
     */
    private void appendToFile(String data, File file) {
        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.newLine();
            bw.close();
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Generate a unique identifier that is the current time in milliseconds.
     * @return a string that is a unique identifier.
     */
    private String generateUniqueIdentifier() {
        return "" + System.currentTimeMillis();
    }

    /**
     * Clean a file from ANSI color codes. 
     * @param file the file that should be processed
     */
    private void cleanFile(File file){
        StringBuilder buffer = new StringBuilder();
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                buffer.append(System.lineSeparator());
            }
            String data = buffer.toString();
            reader.close();
            String cleanData = data.replaceAll("\\x1b\\[[0-9;]*m", "");
            FileWriter fw = new FileWriter(file, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(cleanData);
            bw.newLine();
            bw.close();
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}


