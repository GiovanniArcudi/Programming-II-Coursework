# JavaFX MathDoku Instructions

## Running the Application
Unzip the folder, open command line, go inside the "bin" folder and run "launch".

## Starting a new game
To start a game either click on "Load Game From File" or "Load Game From Text" buttons.

## Cell completion
* By Mouse: Click on the cell to activate it and click on the button with a right number to enter/clear it.

* By Keyboard: The selected cell accepts numerical input from keyboard in the rage <1, size>, the active cell can be changed using `A` (left), `W` (up), `S` (down), `D` (right) keys.

Clearing an active cell is handled by the `Backspace` key and `Delete` key.

## Mistake detection
If you want the application to highlight your mistakes, click the appropriate checkbox in the controls Box. Click again to disable it.

## Application can handle `-` and `÷` cages with more than two cells
Yes.

## Win detection / animation
When the game is won, all cells will change to glowing colour and start rotating. Then a "congratulations" Alert is shown.

## Clearing
Click the `Clear Grid` button and when a confirmation window pops up, click `OK`, note that clearing the board cannot be undone.

## Undo / Redo actions
Click the `Undo` / `Redo` button in the controls Box.

## Loading files
* From File: Click `Load game from File` button, select the appropriate File from the FileChooser and then click `OK`.
* From Text: Click `Load game from Text` button, write the puzzle description in the text area and click `OK`.

## Incorrect Input Detection
If invalid input is detected, a popup Alert will explain how to provide a valid format. If the input is valid but still does not respect MathDoku game constraints, a popup Alert will notify the user of the problem.

## Font sizes
Click one of the 3 buttons `Small`, `Medium` or `Big`, the default size is medium.

## Solver
* Get hint: Click “Hint” button, the hint will be displayed in a gree coloured tile.
* File: Solver.java



   Author:

   Giovanni Arcudi, 2020
