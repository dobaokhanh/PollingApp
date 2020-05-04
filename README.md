# Polling Application
Polling Application is an application where we could create polls for other users to vote

## Table of contents
* [Technologies](#Technologies)
* [Analysis](#Analysis)
* [Status](#Status)

## Technologies and platforms
* Java
* Spring Boot
* Spring Security
* JWT
* Hibernate
* PostgreSQL
* Heroku

## Analysis
### Case description
####	Actors of the solution:
 *	Users
 *	Administrators
####	Roles of actors:
 *	Users: use the system to create polls, to cast a votes.
####	Tasks the actors would like to do:
 *	Users: create accounts, create/update/delete polls, cast votes .
####	Data input:
 *	Data input under various types (string, date/time, numbers, etc.) by authorized actors.
####	Output data type:
 *	Data is saved to database.
 *	Data is displayed as a web page.

### Table of functional requirement

Priority level
 * 1- Must have
 * 2- Should have
 * 3- Nice to have

|Reference|Description|Priority
|---------|-----------|--------|
|F1|Authentication|1|
|F2|Show all polls|1|
|F3|Create poll|1|
|F4|Create choices|1|
|F5|Cast votes|1|
|F5|Delete polls|2|
|F6|Show error page|3|

## Status
Pending. Maybe continuing after 1 months. 
