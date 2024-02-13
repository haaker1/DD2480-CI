# DD2480-CI

Continuous Integration implementation, part of DD2480 Software Engineering Fundamentals (KTH, 2024)

## Configuration

Any repository which implements this CI has to provide a `test.sh` script in the root directory, which is then executed in order to test the repository.

## How to Run

Clone this repository and have Maven installed. This repository is also configured with certain tests for testing the CI itself, which can be executed by `mvn test`.

- To make the local server externally visible using ngrok, do the following in a separate terminal:

  - Configure ngrok:

    1. `curl -LO --tlsv1 https://bin.equinox.io/c/4VmDzA7iaHb/ngrok-stable-linux-amd64.zip`
    2. `unzip ngrok-stable-linux-amd64.zip`
    3. [Register an account on ngrok to get your authentication token](https://dashboard.ngrok.com/get-started/your-authtoken)
    4. `./ngrok authtoken YOUR_TOKEN`

  - Run ngrok (in a separate terminal):

    1. `./ngrok http 8021`

- To start the CI server, do the following in one terminal:

  1. Create a copy of `config.env-default` and rename it to `config.env`
  2. Add your [GitHub Token](https://github.com/settings/tokens) to `config.env` (When generating the token, make sure to select the "classic" variant and tick the `repo:status` box under "Select scopes")
  3. Add the ngrok URL to `config.env`, such as `HISTORY_URL=http://XXXX-XX-XX-XX.ngrok-free.app`
  4. Do `cd ci`
  5. Run `mvn exec:java`

- To link the CI:

  - Add the public link from ngrok as a Webhook to your GitHub repository and set the "Content type" to be `application/json`.
