# Getting Started

### Guides
The following guides illustrate how test the application

To start app go to base directory rate-limiting (if not renamed)
Run ./docker-compose up
This will run local copy of the app on port 8080

### Tests

A sample postman collection has been provided at rate-limiting/interview.postman_collection.json
Load the collection to test the available API endpoints in in the system.

By default, a burst limit of 10 tps has been set and a request limit of 20 transactions per month

### Code read through

To look through the code and run unit tests available
Either

1 - run ./gradlew test without having to open the IDE
2- Open idea or eclipse and import the gradle project

*Note: You will require java 17 to successfully run the project



