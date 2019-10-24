
## Executor exercise - V1 - 24/10/2019
This is a specification for a programming exercise to develop an Executor that simulate some functionalities of the Java Executor. The final result should be a package that contains the Executor class and any additional packages or classes and a client code that use this package.
## Overall functional requirements
- Apply tasks on multi threads Applier.
- Get notified on finished Tasks.
## Phase 01:
In this phase it is required to develop the applyTask function.
### Implementation requirements
- Create function that takes a Supplier and a Consumer as input and get the value from the Supplier, pass it to the Consumer.
- These functions should be applied on multiple threads with queue management System.
## Phase 02:
In this phase, the client can get notification on finished tasks if subscribed, and there will be another version from applyTask function.
### Implementation requirements
- Notification on finished tasks should be done in two ways:
	- Using Observables.
	- Using Streams.
- Create another version of applyTask that returns CompletableFuture.
- Ability to change the number of threads.
### Notes
- It is prefered to apply the concept of the <b>Fluent interface</b> in the excercise.
### Helpful resources
- [Functional Interfaces in Java 8](https://www.baeldung.com/java-8-functional-interfaces)
- [Parallel and Asynchronous Programming with Streams and CompletableFuture](https://www.youtube.com/watch?v=0hQvWIdwnw4&t=10140s)