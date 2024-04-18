# Simple Subscribing Rest Endpoint

Illustrates how to do a subscribing (SSE) endpoint using Spring WebFlux.

## Usage

1. Start the application with `./gradlew bootRun` and then open a browser to `http://localhost:8080/messages`.
2. Open another terminal and run `curl -X POST http://localhost:8080/messages -d "Hello, world!"` to send a message to the browser.

There are a couple examples in the [message http file](./messages/messages.http) that you can run in IntelliJ IDEA.