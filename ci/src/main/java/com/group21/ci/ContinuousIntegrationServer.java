package com.group21.ci;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.JSONException;
import org.json.JSONObject;


/** 
 Skeleton of a ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.
*/
public class ContinuousIntegrationServer extends AbstractHandler
{
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
        throws IOException, ServletException
    {
        
        System.out.println(target);
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        switch (request.getMethod()) {
            case "POST":
                BufferedReader reader = request.getReader();
                RepositoryInfo repo = readPostData(reader);
                if (repo == null) {
                    System.out.println("Unknown POST request");
                    response.getWriter().println("Unknown POST request, assumed to be ping");
                    break;
                }
                String print = "Received commit\nBranch: " + repo.ref + "\nCommit ID: " + repo.commitId + "\nClone URL: " + repo.cloneUrl;
                response.getWriter().println(print);
                final ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    public void run() {
                        RepositoryTester repositoryTester = new RepositoryTester(repo);
                        StatusSender statusSender = new StatusSender(repo, repositoryTester.getIdentifier());
                        statusSender.sendPendingStatus();
                        int exitCode = repositoryTester.runTests();
                        if (exitCode == 0) {
                            statusSender.sendSuccessStatus();
                        } else {
                            statusSender.sendFailureStatus();
                        }
                    }
                });

                break;
            case "GET":
                PrintWriter writer = response.getWriter();
                writer.println(getHTMLPage(target));
                break;
        
            default:
                break;
        }
    }

    /**
     * Return the appropriate HTML page for the given target URL.
     * Variants exist for build history browsing (/) and specific
     * build log viewing (/{id}).
     * @param target The target URL relative to root.
     * @return The HTML page as a string.
     */
    public String getHTMLPage(String target) {
        String htmlRespone = "<html>";
        if (target.equals("/")) {
            System.out.println("Printing build history");
            htmlRespone += "<h1>Previous Builds:</h1>";
            File file = new File(Config.DIRECTORY_BUILD_HISTORY);
            String[] directories = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File current, String name) {
                    return new File(current, name).isDirectory();
                }
            });
            if (directories != null) {
                for (String directory : directories) {
                    htmlRespone += "<a href=\"/" + directory + "\"><h3>" + directory + "</h3></a>";
                }
            }
        } else if (target.matches("^/[0-9]+$")) {
            String buildId = target.substring(1);
            System.out.println("Printing build history for " + buildId);
            htmlRespone += "<h1>Build " + buildId + "</h1>";
            try {
                Scanner branchReader = new Scanner(new File(Config.DIRECTORY_BUILD_HISTORY + buildId + "/" + Config.BUILD_BRANCH_FILENAME));
                Scanner SHAReader = new Scanner(new File(Config.DIRECTORY_BUILD_HISTORY + buildId + "/" + Config.BUILD_IDENTIFIER_FILENAME));
                Scanner logReader = new Scanner(new File(Config.DIRECTORY_BUILD_HISTORY + buildId + "/" + Config.BUILD_LOG_FILENAME));
                java.util.Date date = new java.util.Date(Long.parseLong(buildId));
                htmlRespone += "<h2>Build Date: " + date.toString() + "</h2>";
                htmlRespone += "<h2>Branch: " + (branchReader.hasNextLine() ? branchReader.nextLine() : "") + "</h2>";
                htmlRespone += "<h2>SHA: " + (SHAReader.hasNextLine() ? SHAReader.nextLine() : "") + "</h2>";
                htmlRespone += "<h2>Build log:</h2>";
                while (logReader.hasNextLine()) {
                    htmlRespone += logReader.nextLine() + "<br>";
                }
                branchReader.close();
                SHAReader.close();
                logReader.close();
            } catch (FileNotFoundException e) {
                htmlRespone += "Build ID not found...";
            }
        } else {
            if (!target.equals("/favicon.ico")) 
                System.out.println("Unknown get request for " + target);
        }
        htmlRespone += "</html>";
        return htmlRespone;
    }

    /**
     * Retreive JSON object from POST request
     * @param reader a BufferedReader with the data from the incoming request
     * @return the JSON object containing data from request.
     */
    public RepositoryInfo readPostData(BufferedReader reader){
        StringBuilder buffer = new StringBuilder();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                buffer.append(System.lineSeparator());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String data = buffer.toString();
        JSONObject jsonObj = new JSONObject(data);
        try {
            String ref = jsonObj.getString("ref");
            ref = ref.substring(ref.lastIndexOf("heads/")+6);
            String commitId = jsonObj.getJSONObject("head_commit").getString("id");
            String cloneUrl = jsonObj.getJSONObject("repository").getString("clone_url");
            String owner = jsonObj.getJSONObject("repository").getJSONObject("owner").getString("name");
            String name = jsonObj.getJSONObject("repository").getString("name");
            return new RepositoryInfo(ref, commitId, cloneUrl, owner, name);
        } catch (JSONException e) {
            return null;
        }
    }
 
    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        String testOwner = "alexarne";
        String testRepositoryName = "DD2480-CI";
        String testSHA = "e9ee1e63e303bbf6f5b010b8fa28d1d71d04fd7a";
        String testBranch = "issue/37-refactor-status/";
        String testCloneUrl = "https://github.com/alexarne/DD2480-CI.git";
        RepositoryInfo testRepo = new RepositoryInfo(testBranch, testSHA, testCloneUrl, testOwner, testRepositoryName);
        RepositoryTester repositoryTester = new RepositoryTester(testRepo);
        // repositoryTester.runTests();
        StatusSender ss = new StatusSender(testRepo, "1707768154560");
        // ss.sendSuccessStatus();
        Server server = new Server(Config.PORT);
        server.setHandler(new ContinuousIntegrationServer()); 
        server.start();
        server.join();
    }
}
