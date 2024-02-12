# DD2480-CI

Continuous Integration implementation, part of DD2480 Software Engineering Fundamentals (KTH, 2024)

## Configuration

Any repository which implements this CI has to provide a `test.sh` script in the root directory, which is then executed in order to test the repository.

## How to Run

Clone this repository and have Maven installed. This repository is also configured with certain tests for testing the CI itself, which can be executed by `mvn test`.

- To start the CI server, do the following in one terminal:

  1. Create a copy of `config.env-default` and rename it to `config.env`
  2. Fill in the fields of `config.env`
  3. Do `cd ci`
  4. Run `mvn exec:java`

- To make the local server externally visible using ngrok, do the following in a separate terminal:

  - Configure ngrok:

    1. `curl -LO --tlsv1 https://bin.equinox.io/c/4VmDzA7iaHb/ngrok-stable-linux-amd64.zip`
    2. `unzip ngrok-stable-linux-amd64.zip`
    3. [Register an account on ngrok to get your authentication token](https://dashboard.ngrok.com/get-started/your-authtoken)
    4. `./ngrok authtoken YOUR_TOKEN`

  - Run ngrok (in a separate terminal):

    1. `./ngrok http 8021`

- With the CI server running, add the public link as a Webhook to your GitHub repository and set the "Content type" to be `application/json`.
