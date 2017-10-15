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
Endpoints:
1. transfer/
    a. POST perform/
     This is main lalala
     Return codes & errors:
     400 (Bad request)
    b. GET list/
    Returns list of all accounts
2. account/
    a. POST create/
     Creates account with some initial amount
    b. GET list/
     Returns list of accounts


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


### Why using Guice/Dropwizard/H2/Cucumber?

Guice - keeping in mind requirement to avoid Spring I've stopped a
Dropwizard - I was planning to use Jersey & Jetty, but Dropwizard popped out, so I used it.
H2 - supports memory mode

### Why adding transfer entity to app?

### How avoiding deadlocks and guaranteeing consistency?


## Configuration
### Application configuration


### Test configuration


## What can be improved

