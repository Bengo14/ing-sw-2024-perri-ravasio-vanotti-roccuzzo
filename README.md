# POLIMI Software Engineering Project 2023/2024
![](src/main/resources/it/polimi/sw/gianpaolocugola47/graphics/backGround/frontPage.jpeg)
Codex naturalis is a game developed by Cranio Creations, as a physical board game.

This project is a digital adaptation of the game, developed by the **GC47** team, as part of the Software Engineering course at Politecnico di Milano.

The team members are:

- [Beniamino Perri](https://github.com/Bengo14)
- [Nicolò Ravasio](https://github.com/nicoloravasio)
- [Paolo Vanotti](https://github.com/Van-Paolo)
- [Davide Roccuzzo](https://github.com/Roccuzz0)


## What was implemented

| Feature                  | Implemented |
|--------------------------|-------------|
| Base rules               | ✔️          |
| Complete rules           | ✔️          |
| CLI                      | ✔️          |
| GUI                      | ✔️          |
| RMI                      | ✔️          |
| Socket                   | ✔️          |
| Chat (1FA)               | ✔️          |
| Server Persistence (2FA) | ✔️          |
| Multiple games (3FA)     | ❌           |
| Disconnection  (4FA)     | ❌           |


## Test coverage

| Package    | Class coverage | Method coverage | Line coverage |
|------------|----------------|-----------------|---------------|
| Model      | 78% (15/19)    | 98% (224/228)   | 90%(730/810)  |
| Controller | 100% (1/1)     | 97% (37/38)     | 91%(103/112)  |

## Instructions

To run this project, you need to have a Java Runtime Environment (JRE) installed. This project requires Java version 21
or later. You can check your Java version by typing `java -version` in your command prompt or terminal.

### Running the server

To run the server, navigate to the `./deliverables/jar` folder where the jar file is located and run it.
This is done via the command line with the following command:

```sh
java --enable-preview -jar AppServer.jar
```

### Running the client

To run the client, navigate to the `./deliverables/jar` folder where the jar file is located and run it.
This is done via the command line with the following command:

```sh
java --enable-preview -jar AppClient.jar
```
Note that you have to run the server before the client.
