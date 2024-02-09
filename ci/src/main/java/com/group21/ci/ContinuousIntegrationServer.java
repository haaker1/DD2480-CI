package com.group21.ci;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.BufferedReader;
import java.io.IOException;
 
import org.eclipse.jetty.server.Server;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.JSONObject;

import org.json.*;


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
        switch (request.getMethod()) {
            case "POST":
                try{
                    BufferedReader reader = request.getReader();
                    RepositoryInfo repo = readPostData(reader);
                    String print = "branch: " + repo.ref + " commit id: " + repo.commitId + " clone url: " + repo.cloneUrl;
                    response.getWriter().println(print);
                    RepositoryTester repositoryTester = new RepositoryTester(repo);
                    repositoryTester.runTests();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case "GET":
                
                break;
        
            default:
                break;
        }
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        // 2nd compile the code

        response.getWriter().println("CI job done");
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
        String ref = jsonObj.getString("ref");
        ref = ref.substring(ref.lastIndexOf("heads/")+6);
        String commitId = jsonObj.getJSONObject("head_commit").getString("id");
        String cloneUrl = jsonObj.getJSONObject("repository").getString("clone_url");
        String owner = jsonObj.getJSONObject("repository").getJSONObject("owner").getString("name");
        String name = jsonObj.getJSONObject("repository").getString("name");
        return new RepositoryInfo(ref, commitId, cloneUrl, owner, name);
    }
 
    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        String testOwner = "alexarne";
        String testRepositoryName = "DD2480-CI";
        String testSHA = "qowdpinqwdoin";
        String testBranch = "main";
        String testCloneUrl = "https://github.com/alexarne/DD2480-CI.git";
        RepositoryInfo testRepo = new RepositoryInfo(testBranch, testSHA, testCloneUrl, testOwner, testRepositoryName);
        RepositoryTester repositoryTester = new RepositoryTester(testRepo);
        repositoryTester.runTests();
        Server server = new Server(Config.PORT);
        server.setHandler(new ContinuousIntegrationServer()); 
        server.start();
        server.join();
    }
}
