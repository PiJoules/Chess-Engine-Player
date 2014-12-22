# Chess Engine and Players
## Summary
A mediocre Java chess engine that can make players compete against each other.
## Setup
1. After cloning the repository and changing into the directory, create the jar files with
```sh
$ just jar
```
2. Test if the engine works by having 2 RandomPlayers play against each other
```sh
$ just test
```
3. Add a `player.java` file into the `Battlefield` directory to be able to play it against a `RandomPlayer`. Have it play against a RandomPlayer with
```sh
$ just battle [PlayerName]
# For example, to have the FirstOrderPlayer go against a RandomPlayer
# $ just battle FirstOrderPlayer
```
4. Remove all the class and jar files if necessary
```sh
$ just clean
```

## To do
1. I know there are some bugs in here somewhere, but I just haven't found them yet.
2. Make it so a user can play against a player
3. Add en passant
4. Add castling
5. Add 2 players instead of having a player go against a RandomPlayer