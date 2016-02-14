# Othello Bot

This is an Othello bot I made for a class project that uses alpha beta pruning and heuristics to play Othello strategically. The bot, AlphaBetaBot3, won the class competition, and after I played it against several other online bots (and won) it seems to be a rather good bot. Don't take my word for it, play it and find out! ;)

# Installation

To install simply clone the directory and compile the code.

```bash
git clone $REPO
cd Othello
javac *.java
```

# Usage

java OthelloGame BlackBotClassName WhiteBotClassName BOARD_SIZE NUMBER_GAMES PLAYBACK_DISPLAY PLAYBACK_DELAY

Where:
BOARD_SIZE is a positive integer
NUMBER_GAMES is a positive integer
PLAYBACK_DISPLAY is a binary value, 0 or 1
PLAYBACK_DELAY is a positive integer that represents the delay when making a move (usually used when playing against a bot) in milliseconds

For example:

```bash
java OthelloGame AlphaBetaBot3 HumanPlayer 8 1 1 1000
```
NOTE:
- Currently, when running the display every game will open a new display window without closing the old one, so don't run too many games with the display on at once
- When using HumanPlayer with the display on you can click on the display to make the cooresponding move

# Contributors

Matthew Whitehead (assistant professor at Colorado College) wrote the majority of the framework code. 


