# Java NIO Echo Server

A lightweight non blocking echo server built using pure Java NIO.

This project was created to understand how high performance backend systems work underneath modern frameworks by working directly with Selectors, SocketChannels, and thread pools.

## Features

* Non blocking server architecture
* Selector driven event loop
* Concurrent request processing using ExecutorService
* Minimal and readable implementation
* Built without external libraries or frameworks

## Architecture

The server uses:

* `Selector` for asynchronous socket event monitoring
* `ServerSocketChannel` for accepting incoming connections
* `SocketChannel` for client communication
* `ExecutorService` worker pool for payload handling

The main thread handles I/O readiness events while worker threads process received data independently.

## Design Tradeoff

Worker threads directly write back to the `SocketChannel`.

This keeps the implementation simple and easy to understand, but in high throughput production systems this approach may introduce concurrency concerns because `SocketChannel` is not fully safe for uncontrolled concurrent writes.

Production grade systems typically solve this using:

* outbound queues
* connection state tracking
* interest operation management
* dedicated write handling

For this project, readability and learning value were prioritized over architectural complexity.

## What I Learned

* Event driven server architecture
* Non blocking I/O with Java NIO
* Selector based concurrency models
* Thread pool execution strategies
* Tradeoffs between simplicity and scalability

## Future Improvements

* Dedicated write queues
* Backpressure handling
* Connection state management
* ByteBuffer pooling
* Reactor pattern implementation
* Benchmarking under concurrent load

## Running the Server

```bash
javac Server.java
java Server
```

Server starts on:

```text
localhost:5050
```

## Tech Stack

* Java
* Java NIO
* ExecutorService
* Concurrent Programming

Built for learning systems engineering fundamentals through implementation.
