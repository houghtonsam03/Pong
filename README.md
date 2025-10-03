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
2) Create a settings.json file in the .vscode folder and add:
```
{
    "java.project.sourcePaths": ["src"],
    "java.project.outputPath": "build"
}
```
3) Run: `javac -d build src/*.java`.
4) Set wanted properties in the file "settings.properties".
5) Run: `java -cp build Main`.

# Controls
1 Human player:
* W = Player move up.
* S = Player move down.

2 Human players:
* W = Left player move up.
* S = Left player move down.
* Up = Right player move up.
* Down = Right player move down.

More
* Use"ESC" to pause the game during runtime.

## Bugs
* Squishing the ball using the blocker onto the edges of the screen acts weirdly.
* Corner bounces sometimes weird.
## To-Do
* Add "settings" to startup menu
* Add "controls" to startup menu
* Make pause menu transperent
* Add "new game" to pause menu
* Add "unpause" to pause menu
* Add "controls" to pause menu
* Add "stats" to pause menu
* Add "pause" button to game screen
* Add "current games" to game screen
* Add "timer" to game screen
* Swap out shown games when gameover
* AI-Agents
