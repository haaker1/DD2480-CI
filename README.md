# DD2480-CI

Continuous Integration implementation, part of DD2480 Software Engineering Fundamentals (KTH, 2024).

A browsable list of all previous builds can be found here (the root of the current ngrok link): https://XXXX.ngrok-free.app

## Configuration

Any repository which implements this CI has to provide a `test.sh` script in the root directory, which is then executed in order to test the repository.

## How to Run

Clone this repository and have Maven installed. This repository is also configured with certain tests for testing the CI itself, which can be executed by `mvn test`.

- To start the CI server, do the following in one terminal:
  
  1. Create a copy of `config.env-default` and rename it to `config.env`
  2. Add your [GitHub Token](https://github.com/settings/tokens) to `config.env` (When generating the token, make sure to select the "classic" variant and tick the `repo:status` box under "Select scopes")
  3. Add the ngrok URL to `config.env`, such as `HISTORY_URL=http://XXXX-XX-XX-XX.ngrok-free.app`
  4. Do `cd ci`
  5. Run `mvn exec:java`

- To make the local server externally visible using ngrok, do the following in a separate terminal:
  
  - Configure ngrok:
    
    1. Download a zip file suitable for your operating system from [the ngrok website](https://ngrok.com/download)
    2. `sudo tar xvzf ~/Downloads/ngrok-v3-stable-linux-amd64.tgz -C /usr/local/bin`
    3. [Register an account on ngrok to get your authentication token](https://dashboard.ngrok.com/get-started/your-authtoken)
    4. `ngrok config add-authtoken YOUR_TOKEN`
  
  - Run ngrok (in a separate terminal):
    
    1. `ngrok http 8021`

- To link the CI:
  
  - Add the public link from ngrok as a Webhook to your GitHub repository and set the "Content type" to be `application/json`.

### Maven commands

Maven grants several useful commands that can be used to compile, test and package the project. All commands must be executed in the location of the `pom.xml` file, that is in `/ci`:

- To compile the project: `mvn compile`

- To test the project: `mvn test`

- To package the project: `mvn package`

The phases are executed sequantially, `mvn test` also compiles the project, and `mvn package` also compiles and tests the project.

Other useful phases can be found in the [Maven Introduction to Build Lifecycle](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html).

## Repository structure

The repository has the following structure:

- ci
  
  - src
    
    - main\java\com\group21\ci
      
      - Config.java
      
      - ContinuousIntegrationServer.java
      
      - RepositoryInfo.java
      
      - RepositoryTester.java
      
      - StatusSender.java
      
      - TextSanitizer.java
    
    - test\java\com\group21\ci
      
      - ContinuousIntegrationServerTest.java
      
      - TextSanitizerTest.java
  
  - A `target` folder will appear here after building the project, containing the executables and jar files.
  
  - A `build_history` folder will appear while using the project, containing files about the commits
  
  - A `repos` folder will appear while using the project, containing files previous repos
  
  - pom.xml

## Supported Versions

Other versions can work but are not guaranteed to. The following versions were used during development and are guaranteed to work:

- Java: OpenJDK 17.0.9 (OpenJDK Runtime Envirnoment)

- Maven: Apache Maven 3.9.1 (Red Hat 3.9.1-3)

### Implementation and testing

The webhooks is triggered by a push event, which sends a POST request to the CI server with data about the latest push. Relevant data, for example the URL to clone the repository, the branch which the push was made to and the latest commit SHA, is extracted. The repository is then cloned from the extracted URL and the correct branch is checked out. A bash script, test.sh, is executed, in which the maven command 'mvn test' is run to compile the files and run the tests. The exit status from these commands decide what commit status should be sent back as notification.

The implementation was tested both through unit testing, mock tests and user testing. For example, there is a test to check that the maven compile plugin has generated the correct files. Some tests use mock data, such as in the tests for extraction of the correct data from the payload sent from the webhook. Testing was also done through observation. When a commit was pushed to the repo, the logs of the build history could be looked at to determine if the repository was properly cloned and the tests run. The mvn test command outputs the build status and shows the tests that were ran and if they passed or not, which will all be saved to the logs. Similar tests were conducted for the commit status notification.

## Statement of Contributions

**Alex Gunnarsson**

- RepositoryTester
- Get and display build history
- Refactoring and improvement of POST-handler
- Documentation

**Anne Haaker**

- POST-handler and data extraction
- RepositoryInformation class
- Clean log files from ANSI color codes
- Documentation

**Hugo Tricot**

- StatusSender sending commit status
- Implement maven
- Documentation

**Juan Bautista Lavagnini Portela**

- Local CI
- Text sanitizer
- Documentation

Juan and Hugo worked in pair-programming most of the time.

## Essence - Team

We are somewhere between the "Collaborating" and "Performing" states. The group is actively working together and everyone mostly know what to do. Issues are self-assigned in an agile manner in order to make use of everyone's potential talents, such as some being more inclined towards frontend/backend work. The work process is not as smooth as it would have to be for us to be in the "Performing" state, with certain obstacles being uncovered due to improper planning and preparation for independent development. This leads to, for example, some backtracking where certain parts have to be reworked, which is what has to be fixed in order to get us to the next state. To further get to the "Adjourned" state, we would have to be completely finished with the project, which we almost are.

## P+

For P+, we have implemented the opportunity for the user to set their own description for the commit status (`success`, `pending`, `failure` and `error` ). The status can be set in the config.env file, by setting the values of:

- `SUCCESS_DESCRIPTION`

- `PENDING_DESCRIPTION`

- `ERROR_DESCRIPTION`

- `FAILURE_DESCRIPTION`

The custom status will go through sanitation so that dangerous characters (e.g. `\`, `"`, etc.) are not kept. For example, the message `error, shall not" be permitted` will be sanitized as `error shall not be permitted`. If no custom description has been set, the program will use a default one.

