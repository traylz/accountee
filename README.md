# Test task
So, I've called it accountee (short version is act).

## How to run
Just run com.gsobko.act.Application in act-rest module.

By default application runs with two accounts: #1 with 1'000'000 balance and #2 with 2'000'000 balance.
To run server on another port or change initial database state take a look at section Configuration.
To do a transfer from account #1 to account #2 call POST on localhost:8080/transfer/perform with following JSON:

    {"fromId" : 1, "toId" : 2, "amount" : 1000}


## Important notice regarding transfers
Essential feature of transfers is idempotency - you should have some kind of protection against duplicated transfers.
This was done by introducing transferId and transfer as entity in database.
So, you can (and should) add transferId generated uniquely on calling side to request. This will guarantee you will not perform same transfer twice.
Further discussed in "Why's? Explaining some decisions" section.

## REST API
### Endpoints
1. ```transfer/```

    1. ```POST perform/``` This is main method to perform transfer between two accounts.
    Request:


        {"fromId" : 1, "toId" : 2, "amount" : 1000}


    Response:

        {"fromId" : 1, "toId" : 2, "amount" : 1000, "transferId" : "asdcf-vsfvsfv-sfv"}

    2. ```GET list/``` Returns list of all transfers

    Returns list of all accounts
2. account/
    1. ```POST create/``` Creates account with some initial amount. E.g.

    Request:

        {
            "amount" : 1000.12
        }

    Response:

        {
            "id" : 23,
            "amount" : 1000.12
        }

    2. ```GET list/```
     Returns list of accounts


### Return codes & errors
1. 200 - OK
2. 400 (Bad request) - is returned when input is invalid with some message that can be passed to user.
3. 404 (Not found)
4. 500 (Server error) - On some internal error. User sees


    Internal error occurred, please contact support with reference 123135

And  error with reference 123135 is logged with stacktrace, so that sl3 can find it easily.

## Project structure
Project consists of 4 modules (Yeah, more modules to the god of modules :) Reasoning discussed in "Why's section") :
1. act-database.
This module contains database-api & model + two implementations - #1 is simple in-memory for testing; #2 is JDBC based.
2. act-core.
This module contains all business logic.
3. act-rest
This module contains all related to rest and wiring of application.
4. act-test-pack
This module contains some e2e tests.

## Test coverage
Some test coverage in cucumber scenarios (all plain & simple, see transfers.feature)

## Technology stack
- Guice for DI.
- H2 for in-mem database.
- Dropwizard for REST.
- Cucumber for tests.

## Why's? Explaining some decisions
### Why so many modules?

To cover points of variability.
There are three distinct layers of application:
- Storage (act-database)
- Business logic layer (act-core)
- Transport + API layer (act-rest)
- e2e tests

### Why using Guice/Dropwizard/H2/Cucumber?

Guice - keeping in mind requirement to avoid Spring I've stopped at guice as most mature.

Dropwizard - I was planning to use Jersey & Jetty - that's all bundled in Dropwizard (along with Jackson).
Also found pretty good module for Guice integration

H2 - supports memory mode

Cucumber - for human readable tests.

### Why adding transfer entity to app?
1. Transfers shouldn't be done twice in case of network glitch.
2. Retry of transfer message should be safe.

### How avoiding deadlocks and guaranteeing consistency?
1. Locking resources in order: transaction -> two accounts (ordered by primary key).
2. Select for update for compatibility with read committed isolation level.

## Configuration
### Application configuration
see accountee.yml

### Test configuration
TBD

## What can be improved
TBD