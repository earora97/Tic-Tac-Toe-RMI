# Tic-Tac-Toe-RMI
Distributed Systems Assignment 2

- Eavanshi Arora
- 201501115

To run it -
$./make.sh
-This will compile all the files and start rmiregistry

To start a server -
(in a new tab) $java Server
To start a client -
(in a new tab) $java Client

Features-
*Multiple games can run in parallel
*Uses one Remote Interface and two implementation classes, one each for Server and Client.
*After a match ends both the players are asked if they want a rematch.
*Player 1 is assigned to the player that registered on Server first.
*When a client wants to play a game and no other client is available, "Wait" is displayed to the client.
