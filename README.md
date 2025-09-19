## Pong
A simple pong game which is intended to have a simple AI framework.
## Functionality
* Choose which side is human and AI.
* Game Manager (play multiple games against eachother (AI vs AI) to test winrate.)
    * Possibility to play multiple games in parallell.
    * Seamless to swap between AI and Human.
    * Change game speed to get game results quicker (not recommended for human games).
    * Head-To-Head Results
* Multiple balls

## Parameters
* Gamesize ratio (30x16)
* Blocker ratio (1x5)
* Ball ratio (1x1)

## How to run the game from a terminal.
1) Create a build folder as well as a .vscode folder under the Pong folder.
2) Create a settings.json file in the .vscode folder and add
```
{
    "java.project.sourcePaths": ["src"],
    "java.project.outputPath": "build"
}
```
3) Run: `javac -d build /*.java`
4) Run: `java -cp build Main.java [X] [Y] [L] [R]`  where:
* X is the amount of simultaneous games.
* Y is the amount of balls in each game.
* L is whether the left blocker is an AI
* R is whether the right blocker is an AI.
