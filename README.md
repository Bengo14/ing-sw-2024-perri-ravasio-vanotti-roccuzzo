# Progetto Ingegneria del Software 2023/2024
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
|--------------------------|------------|
| Base rules               | ✔️         |
| Complete rules           | ✔️         |
| CLI                      | ✔️         |
| GUI                      | ✔️         |
| RMI                      | ✔️         |
| Socket                   | ✔️         |
| Chat (1FA)               | ✔️         |
| Server Persistence (2FA) | ✔️         |
| Multiple games (3FA)     | ❌         |
| Disconnection  (4FA)     | ❌         |


## Test coverage

| Package    | Class coverage | Method coverage | Line coverage  |
|------------|----------------|-----------------|----------------|
| Model       | 78% (15/19)    | 96% (220/227)   | 87%(692/789)  |
| Controller | 100% (1/1)     | 52% (20/38)     | 48%(54/112)    |

### Running the server

To run the server, navigate to the `deliverables` to `jar` where the jar file is located and run it.
This is done via the command line with the following command:

```sh
java --eneable-preview -jar AppServer.jar
```

### Running the client

To run the server, navigate to the `deliverables` to `jar` where the jar file is located and run it.
This is done via the command line with the following command:

```sh
java --eneable-preview -jar AppClient.jar
```
Note that you have to run the server before the client.