# localChatApp

This chat app was created for a University project. The assignment was to create a messaging app with a hybrid p2p/client server architecture. At the end of the project, two prototypes were created, one that could connect two peers directly and one that could connect peers to the server. However the complete app was not fully realized due to time constraints (We were pretty surprised that we still got an A for our work. I guess the journey was more important than the destination). The following were the requirements for the application:

1.	The application must be implemented in a hybrid P2P architecture. A server must manage the user logins, while the messaging will be done directly between peers.
2.	The user application must be able to identify all the peers currently in the network and send and receive messages. 
3.	The application must allow the users to save the conversations and give the option to download them into a file. 
4.	All conversations between peers must be encrypted.
5.	The application must use the UDP transport layer protocol. It must manage the transmission errors, retransmissions, buffers and queues.
6.	The application will create a socket in port 40000 for the server connection and a socket in port 50000 for the P2P connection.
7.	The server must listen to the broadcast address and be bind to its assigned IP address. 
8.	Upon login in each user must first contact the server to register and get a list of the peers as shown in Figure 1.  
9.	The server will broadcast a message to all users whenever a new user enters, or leaves, the network. This message includes each user’s IP address and registered username.
10.	Peers send messages to each other directly using a unicast message; the server is not used as a relay. 
11.	The user application must signal the server when it leaves the network.



## Getting Started

The project was created using Eclipse. To get started using Eclipse and github you can follow this tutorial: 
https://www.youtube.com/watch?v=LPT7v69guVY


### Prerequisites

Content pending

### Installing

Content pending


## Running the tests

Content pending


### Break down into end to end tests

Content pending


### And coding style tests

Content pending

## Deployment

Content pending

## Built With

Content pending


## Contributing

Content pending

## Versioning

Content pending


## Authors

Content pending


## License

Content pending

## Acknowledgments
Code from the following people was used:
* Carlos Lopez
* Cristian Sanchez
* 
* 
