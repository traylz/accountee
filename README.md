# Test task
[![Build Status](https://travis-ci.org/traylz/accountee.svg?style=flat&branch=master)](https://travis-ci.org/traylz/accountee)

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


## Show me code & tests!
Ok, here you go.

Method doing transfer is [here](act-core/src/main/java/com/gsobko/act/TransferManagerImpl.java)

Tests showing all works as expected (these test will start server before running):act-test-pack/src/test/resources/features
1. [Happy-path scenarios](act-test-pack/src/test/resources/features/simple-transfer.feature)
2. [Negative scenarios](act-test-pack/src/test/resources/features/transfer-negative.feature)
3. [Protection from duplicate transfer scenarios](act-test-pack/src/test/resources/features/double-transfer-protection.feature)

## REST API
### Endpoints
1. ```transfer/```

    1. ```POST perform/``` This is main method to perform transfer between two accounts.

    Request:

        {
            "fromId" : 1,
            "toId" : 2,
            "amount" : 1000
        }


    Response:

        {
            "fromId" : 1,
            "toId" : 2,
            "amount" : 1000,
            "transferId" : "asdcf-vsfvsfv-sfv"
        }

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
1. 200 OK - transfer successful
2. 400 (Bad request) - is returned when input is invalid with some message that can be passed to user.
3. 404 (Not found)
4. 500 (Server error) - On some internal error.
User sees ```Internal error occurred, please contact support with reference 123135```
And  error with reference 123135 is logged with stacktrace, so that sl3 can find it easily.

## Project structure
Project consists of 4 modules (Yeah, more modules to the god of modules :) Reasoning discussed in "Why's section") :
1. act-database.
This module contains
database-api & model
+ two implementations - #1 is simple in-memory for testing; #2 is JDBC based.
2. act-core.
This module contains all business logic.
3. act-rest
This module contains all related to rest and wiring of application.
4. act-test-pack
This module contains some e2e tests.

## Test coverage

There are tests in core module that covers core logic itself without rest api:
[Core tests](act-core/src/test/resources/features/transfers.feature)

There are tests for rest API (E2E), that starts server and run agains it:
[Rest tests](act-test-pack/src/test/resources/features/)

There are also unit tests:
[Unit tests for TransferManagerImpl](act-core/src/test/java/com/gsobko/act/TransferManagerImplTest.java)

## Technology stack
- Guice for DI.
- H2 for in-mem database.
- Dropwizard for REST.
- Cucumber for tests.

## Why's? Explaining some decisions
### Why so many modules?

To cover points of variability.
There are three distinct layers of application:
- Storage (act-database). Storage may change. In this project this would mean just implementing API methods for new Database.
- Business logic layer (act-core). This is where all business-related changes should be done
- Transport + API layer (act-rest). This also may change (or another transport may add in addition - grpc/jms etc...)

### Why using Guice/Dropwizard/H2/Cucumber?

Guice - keeping in mind requirement to avoid Spring I've stopped at guice as most mature.

Dropwizard - I was planning to use Jersey & Jetty - that's all bundled in Dropwizard (along with Jackson).
Also found pretty good module for Guice integration

H2 - supports memory mode + embeddable

Cucumber - for human readable tests.

### Why adding transfer entity to app?
Taking into consideration http is not reliable transport (not durable) we may find ourselves in situation when transfer operation timed-out due to network glitch.
In this situation we wouldn't know if transfer was done or not. Can we retry? How can we check if transaction was done?
If there is no Transfer entity these questions would stay unanswered.
So, adding transfer would allow us to retry transfer in case of server timeout and also we will have log of all transfers.

### How avoiding deadlocks and guaranteeing consistency?
1. Locking resources in order: transaction -> two accounts (ordered by primary key).
2. Select for update for compatibility with read committed isolation level.

## Configuration
### Application configuration
Here's sample configuration:

    server:
      applicationContextPath: /   # this is root path for app. Can be changed to e.g. /rest
      applicationConnectors:
        - type: http
          port: 8080              # port
      adminConnectors: []         # disable admin connectors, but this has some pretty cool features

    database:
      type: H2                    # H2 or TEST
      initialSql:                 # Sqls to exec on startup. Compatible with H2 only
        - CREATE TABLE account(ID INT AUTO_INCREMENT (1) PRIMARY KEY, AMOUNT DECIMAL);
        - CREATE TABLE transfer(ID VARCHAR(255) PRIMARY KEY, AMOUNT DECIMAL, FROMID INT, TOID INT);

    initialState:                 # initial state
      initialAccounts:            # accounts to create on startup
        - amount: 1000000
        - amount: 2000000

### Test configuration
Here's sample test-configuration to run in embedded mode:

    embedded=true
    config=accountee.yaml # this config will be taken to run embedded rest app

Here's how to run against already started instance:

    embedded=false
    url=http://localhost:8000

## What can be improved
Ok, here are some stuff I skipped (as this is just test project):

1. Dao full implementation for JDBC.
Just implemented methods required for tests to pass.
Also there is a tie to H2 error code (for duplicate code), if chance of changing db is high - would have extracted that to some "SQL Exception mapper", but now that would be an overkill.
2. Serialization.
There are two ways of exposing model to rest API in json -
either expose core model (via serializers) or create separate request-response model
(both ways are ok and actually I've implemented both - serializers fo account and request-response for Transfers),
but I'm inclining a little to creating separate Request-Response model as more agile.
3. Errors.
Generally a good idea to create response object instead of just String for Errors
(to pass User-message, reference or error code so that frontend system could show localized message).
4. Perf test (depending on requirements to service).
If service is going to be high-load then some perf tests would be good.
This would allow to optimize performance (with proofs).
E.g. I'm pretty sure that row-level locking + read committed would have a good speed up compared to table-level locking in case of high concurrency.
5. Logging.
This implementation is logging some info to console (incl. request without body/headers).
So in prod-project I would improve logging: log all request with bodies, add some requestID in MDC and add to log format, so that we can link logs to one request.