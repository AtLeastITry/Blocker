# How to setup/run the project

## Prerequisites:

- Java 8 JDK or higher
- An installation of Maven

## How to install maven:

I have included an installation in the submission to faser, it will be a zip file called &quot;apache-maven-3.5.4&quot; to skip the step of having to download it manually. But it might be a better idea to use a new installation instead.

1. Go to [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi) and download the binary files
2. Extract files to specified directory
3. Add path environment variable for the bin directory or keep note of location.

If any problems occur, it might be an idea to read through [https://maven.apache.org/install.html](https://maven.apache.org/install.html)

## How to run the server:

1. Open up CMD
2. Go to the &quot;server&quot; directory of the assignment
3. If you set up the path environment variable run the following commands:
  1. mvn install (this will install any packages needed)
  2. mvn clean (this will clean out any class files)
  3. mvn compile (this will compile the java files)
  4. mvn exec:java (this will start the server)
4. If you didn&#39;t set up the path environment variable, you need to replace the mvn command with the path to the exe file in quotation marks. i.e. &quot;mvndirectory/bin/mvn.exe&quot; install

## How to run the bot:

### Prerequisites:

- The server must be running

### Steps to run:

1. Open up CMD
2. Go to the &quot;clients/bot&quot; directory of the assignment
3. If you set up the path environment variable run the following commands:
  1. mvn install (this will install any packages needed)
  2. mvn clean (this will clean out any class files)
  3. mvn compile (this will compile the java files)
  4. mvn exec:java (this will start the server)
4. If you didn&#39;t set up the path environment variable, you need to replace the mvn command with the path to the exe file in quotation marks. i.e. &quot;yourdirec/bin/mvn.exe&quot; install
5. After all commands have been complete this will spin up 5 new instances of bots that will join any available games/create new ones.

## How to run the client UI

### Prerequisites:

- The server must be running

### Steps to run:

1. Go to the &quot;clients/user&quot; directory
2. Open the &quot;index.html&quot; file in any browser

# Architecture

## Basic concepts

### WebSocket

The server is built up with the use of web sockets, clients will communicate with the server via these sockets and then the server will respond to a specified subset of clients to ensure everything is up to sync in real time.

### Commands

Each action that can be made by a client is separated into a command, a command will handle one specific task, I.e. to update the game state when a player invokes a move.

### Events

An event will be fired after an action is taken, these are fire and forget concepts and are used to broadcast updates to clients. i.e. after a player makes a move the &quot;MakeMoveEvent&quot; is fired and all clients currently connected to the game will be sent a message.

### Unit tests

There are unit tests that check that the validation around moves work, unfortunately due to the nature of websockets and how they are implemented I was unable to successfully mock new instances of websocket sessions which prevented me from creating unit tests around the hosting, joining and leaving of games. There is however 6 unit tests that are used to ensure validation around player movements is correct. They are as follows:

- testFreedomCardOnOpenBlock
- testFreedomCardOnTakenBlock
- testReplacementCardOnTakenBlock
- testDoubleCardOnOpenBlocks
- testNoCardOnOpenBlocks
- testNoCardOnTakenBlock


## Packages

### Domain

This package contains all the implementations of commands and events that are used throughout the server app.

### Infrastructure

This package contains the abstractions around the commands and events

### Models

This package contains all the models that are used to represent objects on the server

#### Requests

This package contains all models that are used to represent incoming requests

#### Responses

This package contains all models that are used to represent server responses

### Services

This package contains one service, which is the MoveValidator service. The MoveValidator is used to validate moves passed in against a specific instance of a GameState.

### Socket

This package contains the Classes that are specific to the implementation of web sockets, there are only three classes in this package and they are as follows:

- Context
  - This is used to store all the information about the current instance of the server, i.e. the current users connected and the games that are currently being played
- GameServerEndpoint
  - This is the end point that clients interact with, it also contains a singleton of Context
- WebSocketServer
  - This is the class that is run to start the server, it uses tyrus to spin a new instance of the server.

### Util

This package contains utility classes, this includes the MessageDecoder and MessageEncoder classes that are used to encode and decode messages that are sent to the server via the websockets.

# UI

## Technology

The entire UI is built using the following two frameworks: VueJS and Bootstrap.

### VueJS

VueJS is a framework that allows context binding to elements on the page, it features a virtual DOM that allows for swift DOM manipulation. I used this framework to make any updates to the page so there was no need for a page refresh.

### Bootstrap

Bootstrap is a CSS framework, there are plenty of other frameworks available, but I have taken a liking to bootstrap v4 since it uses flexbox.

## Structure

The client user interface is split into two sections, the playing section and the spectating section.

### Playing

The ability to play a game can be found on the main page, as a user you will have the choice to either host a game or join any available ones.

#### Hosting

A user can host a game by clicking the Host button, this will create a new game that is set up server side and will now eb available for other players to join. Once there are at least 2 players in the game the host will be able to start the game.

To make this section a bit more user friendly the &quot;Start&quot; button is disabled until there are at least 2 players available.

#### Joining

Users can also join existing games that are waiting to be started. To join a game the user will click the &quot;Join game&quot; button on the home page, this will be disabled until there is a game available to join. Once the button is clicked a user is then able to select the game they wish to join via a select list.

### Spectating

Users can also spectate games currently in progress, the spectate page shows a list of all games that are in progress. Each game has its own separate tile includes some useful information for the user, such as the game name and the number of players currently in the game. For each game there is also a &quot;Spectate&quot; button which allows the user to see watch the game in real time.

When a user clicks the &quot;Spectate&quot; button they are taken to a new page which will show the current board and the cards available to each user.

## Summary

To make the whole experience a bit more user friendly I have made this page somewhat of a single page app, there are no redirects and all the UI will transition within one page.

Once the game is in progress the screen is split into two sections, the game board and the available cards. Each card features a hover over affect which gives a detailed description of what the card does.

Whilst a game is in progress the user who currently has the turn can select a block they wish to choose, once they have chosen the move, they can click the &quot;Submit&quot; button and the move will be posted to the server. If the turn falls to a different player, an overlay will be displayed specifying that the user needs to wait until their turn.

Once the game has ended a new overlay will be displayed which lists out the scores and the position each player has come in.

# Bot

The bot player doesn&#39;t use any form of special AI algorithms, it will first compile a list of all the available moves, it will then validate those moves, once the moves have been validated it will choose one at random. If there are no standard moves available it will attempt to use one of the influence cards, first the &quot;Freedom&quot; card and then the &quot;Replacement&quot; card