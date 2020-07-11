# JavaFX MathDoku Instructions

## Running the Application:
Unzip the folder, open command line, go inside the "bin" folder and run "launch".

## Starting a new game To start a game either click on "Load Game From File" or "Load Game From Text" buttons.

**CELL COMPLETION:

	   By Mouse: Click on the cell to activate it and click on the button with a right number to enter/clear it.

      By Keyboard: The selected cell accepts numerical input from keyboard in the rage <1, size>, the active cell can be changed using ‘A’ (left), ‘W’ (up), ‘S’ (down), ‘D’ (right) keys.
		             Clearing an active cell is handled by the “backspace” key and “delete” key.


    - MISTAKE DETECTION: If you want the application to highlight your mistakes, click the appropriate checkbox in the controls Box. Click again to disable it.


    - APPLICATION CAN HANDLE - AND ÷ CAGES WITH MORE THAN TWO CELLS: Yes.


   - WIN DETECTION / ANIMATION: When the game is won, all cells will change to glowing colour and start rotating. Then a "congratulations" Alert is shown.



   - CLEARING: Click the "Clear Grid” button and when a confirmation window pops up, click “ok”, note that clearing the board cannot be undone.


   - UNDO/REDO ACTIONS: Click the “Undo” / "Redo” button in te controls Box.


   - LOADING FILES:
	
	   From File: Click “Load game from File” button, select the appropriate File from the FileChooser and then click OK.

	   From Text: Click “Load game from Text” button, write the puzzle description in the text area and click OK.

	   Incorrect Input Detection: If invalid input is detected, a popup Alert will explain how to provide a valid format. If the input is valid but still does not respect MathDoku game constraints, a popup Alert will notify the user of the problem.


   - FONT SIZES: Click one of the 3 buttons “Small”, “Medium” or “Big”, the default size is medium.

	
   - SOLVER:

	   Get hint: Click “Hint” button, the hint will be displayed in a gree coloured tile.

	   File: Solver.java


    - ADDITIONAL INFORMATION:

        I have been invited by the module leader Dr. Sebastian Stein mention here that, beacause of machine-depependend graphics settings dependent, it is possible that grid resizing when minimising the window may not work.





   Author:

   Giovanni Arcudi, 2020
