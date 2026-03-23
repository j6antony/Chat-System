# Antony Johan TCP Chat

A desktop TCP chat application built in Java with a Swing interface. The app lets one user host a chat server locally and other users join that server by hostname or IP address.

## Features

- Host a chat server on port `4444`
- Join an existing server by entering its hostname
- Pick a username before entering the chat
- Exchange messages in a GUI chat window
- Close the server from a moderator window

## Tech Stack

- Java
- Java Swing
- TCP sockets
- Multithreading with `ExecutorService`

## Project Structure

```text
src/
  Main.java
  Menu.java
  Server.java
  Client.java
  Username.java
  Chat.java
  Moderator.java
```

## How To Run

### Compile

```bash
javac -d out src/*.java
```

### Start the application

```bash
java -cp out Main
```

## Usage

1. Launch the app.
2. Choose `Create A Server` to host a chat session on your machine.
3. Choose `Join A Server` from another instance of the app.
4. Enter `localhost` if the server is running on the same computer, or enter the host machine's IP address if it is running on another machine.
5. Enter a username and start chatting.

## Notes

- The default server port is `4444`.
- All users on the same network must be able to reach the host machine on that port.
- This project currently uses plain TCP sockets without encryption or persistent storage.

## Author

Johan Antony
