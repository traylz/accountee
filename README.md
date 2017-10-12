# Test task
So, I've called it accountee (short version is act).
## How to run
Just run com.gsobko.act.Application in act-rest module.
By default application runs with two accounts: #1 with 1'000'000 balance and #2 with 2'000'000 balance.

To run server on another port or change initial database state take a look at section Configuration.

To do a transfer from account #1 to account #2 call POST on localhost:8080/transfer/perform with following JSON:

    {"fromId" : 1, "toId" : 2, "amount" : 1000}


## Important notice regarding transfers
Essential feature of transfers is imdepotence - you should have some kind of protection against duplicated transfers.
This was done by introducing transferId and transfer as entity in database.
So, you can (and should) add transferId generated uniquely on calling side to request.
This will guarantee you will not perform same transfer twice.
Further discussed in 'Why's? Explaining some decisions' section

## REST API
Endpoints:
1. transfer/
    a. POST perform/
     This is main lalala
    b. GET list/
    Returns list of all accounts
2. account/
    a. POST create/
     Creates account with some initial amount
    b. GET list/
     Returns list of accounts

Return codes & errors:


## Project structure
Project consists of 4 modules:
1. act-database
2. act-core
3. act-rest
4. act-test-pack

## Test coverage
Some test coverage in cucumber scenarios (all plain & simple, see transfers.feature)


## Technology stack


## Why's? Explaining some decisions
1. Why so many modules?

To cover points of varability

2. Why using Guice/Dropwizard/H2/Cucumber?

Guice - keeping in mind requirement to avoid Spring I've stopped a
Dropwizard - I was planning to use Jersey & Jetty, but Dropwizard popped out, so I used it.
H2 - supports memory mode

3. Why adding

## Configuration



## What can be improved




