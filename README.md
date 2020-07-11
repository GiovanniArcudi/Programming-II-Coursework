# University of Southampton - Programming II Coursework

This repository contains the Source Code for the [COMP1206 - Programming II](https://www.southampton.ac.uk/courses/modules/comp1206.page) Coursework.

## MathDoku

The aim of this Coursework is to implement a [JavaFX](https://en.wikipedia.org/wiki/JavaFX) application to play a logic puzzle game called MathDoku (also known as _[KenKen](https://en.wikipedia.org/wiki/KenKen)_).

<br/>

## Rules of the Game

A player needs to fill the cells in an `N x N` square grid with the numbers 1 to N (one number per cell), while adhering to the following constraints:

* Each number must appear **exactly once in each row**.

* Each number must appear **exactly once in each column**.

Furthermore, there are groups of adjacent cells called **cages**, which are highlighted on the grid by thicker boundaries. Within each cage is a label showing a **target** number followed by an arithmetic operator (`+`, `-`, `x`, `÷`). There is an additional constraint associated with these cages:

* It must be possible to **obtain the target by applying the arithmetic operator to the numbers in that cage**. For `-` and `÷`, this can be done in any order.

<br/>


***Note:*** If a cage consists of a single cell, then no arithmetic operator is shown. The label simply shows the number that must be in that cell.

<br/>

Here's an image of what a MathDoku game looks like (courtesy of [Wikipedia](https://en.wikipedia.org/wiki/KenKen):

<br/>

<p align="center">
  <img src="https://upload.wikimedia.org/wikipedia/commons/f/fd/KenKenProblem.svg">
</p>

<br/>

And here's what the solved puzzle looks like:

<br/>

<p align="center">
  <img src="https://upload.wikimedia.org/wikipedia/commons/f/fe/KenKenSolution.svg">
</p>

<br/>


## Instructions

You will build your application in parts of increasing difficulty (marks will be awarded for achieving each part, but you only need to hand in your final application). There are 40 marks available in total.

<br/>

### Part One: A Skeleton GUI

First, build a dumb GUI without functionality. This GUI should have controls for the following user actions:

* Undo / redo actions.
* Clear the board.
* Load a game from file.
* Load a game from text input.
* Option to show mistakes on grid.

There should also be space in your GUI to show the grid, which you will implement in Part Two.

***Note:*** Try to make your GUI resizable, so that it adjusts to different window sizes.

**[4 marks]**

<br/>

### Part Two: The Grid

Display a MathDoku grid in your application. You could start by displaying the example grid in the image above.

***Note:*** Think carefully about how to keep your design general, so that it can cope with different layouts and grid dimensions (you can assume the smallest grid is a `2 x 2` grid and the largest is a `8 x 8` grid).

**[4 marks]**

<br/>

### Part Three: Cell Completion

Write appropriate event handling code to allow users to select cells, enter numbers into them and clear individual cells again. 

You should provide functionality to allow number entering and clearing via the keyboard (e.g., by pressing the number keys or the backspace key) and via the mouse (e.g., by showing appropriate number buttons in your GUI).

**[4 marks]**

<br/>

### Part Four: Win and Mistake Detection

Add functionality to detect when the grid has been filled correctly and solved (adhering to all the constraints in the rules of the game above). Show an appropriate message to the user when this happens.

If the user has selected the option to show mistakes on the grid, immediately highlight rows, columns or cages that do not meet the constraints as numbers are being entered (e.g., if a row contains two 2s, that entire row should be highlighted).

***Note:*** It is tricky to handle cages with the - and ÷ operators that consist of more than two cells. If you wish, you can ignore these, but for full marks, make sure that you can handle them too.

**[5 marks]**

<br/>

### Part Five: Clearing, Undo, Redo

Allow the user to clear the board using the control defined in Part One. A dialog box should be displayed to the user to confirm this action.

Also add functionality to undo the last cell modification(s) and redo previously undone modifications. Disable the buttons as appropriate when undoing or redoing is not possible.

**[4 marks]**

<br/>

### Part Six: Loading Files

Allow the user to load pre-defined MathDoku puzzles. The user should be able to do this in two ways: by choosing a specific file on their computer or by entering the puzzle through an appropriate text input control.

A pre-defined puzzle must be given in the following text format:

* Each line defines one cage of the puzzle.
* The line starts with the target followed immediately by the arithmetic operator (or none if it's a single cell) for that cage.
* This is followed by a space and a sequence of cell IDs that belong to the cage (consecutive cell IDs are separated by a comma). Here cells are numbered from 1 to (NxN), where 1 to N are the cells in the top row (left to right), N+1 to 2N are the cells in the second row from the top, and so on.

As an example, the MathDoku puzzle in the images above can be defined as following:

        11+ 1,7
        2÷ 2,3
        20x 4,10
        6x 5,6,12,18
        3- 8,9
        3÷ 11,17
        240x 13,14,19,20
        6x 15,16
        6x 21,27
        7+ 22,28,29
        30x 23,24
        6x 25,26
        9+ 30,36
        8+ 31,32,33
        2÷ 34,35

You can find more examples of different sizes in this zip file: [examples.zip](https://github.com/GiovanniArcudi/Programming-II-Coursework/blob/master/examples.zip) (4x4_divdiff.txt is the only one that contains `-` and `÷` cages with more than 2 cells).

***Note:*** The same puzzle could be specified in multiple ways. Specifically, the order of the cages and of the cells within a cage do not matter. You should do simple error checking on the input (e.g., whether cells within a cage are adjacent and whether each cell is part of exactly one cage) and notify the user if a mistake was detected. You do not need to check whether the puzzle can be solved.

**[7 marks]**

<br/>

### Part Seven: Font Sizes

Add an option to change the font size for your grid display (affecting the values in cells and the cage labels). Add at least three options (e.g., small, medium and large) and make sure that the text remains aligned within cells.

**[2 marks]**

<br/>

### Part Eight: Winning Animation

Add some appropriate animations when a win is detected. Be creative: you could animate the grid, have a colourful display or even add some fireworks over the grid.

**[2 marks]**

<br/>

### Part Nine: Solver

Add the option to automatically solve any puzzle (including those loaded from a file or text input). Use this functionality to also add a "Hint" option, which briefly reveals the correct value of one cell to the user.

**[4 marks]**

<br/>

### Part Ten: Random Game Generator

Include functionality to generate random games. This should give some appropriate options to the user (e.g., board size or difficulty level).

***Note:*** Try to ensure that a randomly generated game has exactly one unique solution.

**[4 marks]**

<br/>

## Mark Scheme

A player needs to fill the cells in an `N x N` square grid with the numbers 1 to N (one number per cell), while adhering to the following constraints:

In total there are 40 marks available, which will contribute 40% towards your overall course mark. The breakdown of available marks is as follows:

* Part One : 4 Marks
* Part Two : 4 Marks
* Part Three : 4 Marks
* Part Four : 5 Marks
* Part Five : 4 Marks
* Part Six : 7 Marks
* Part Seven : 2 Marks
* Part Eight : 2 Marks
* Part Nine : 4 Marks
* Part Ten : 4 Marks

You can find a detailed marking scheme [here](https://github.com/GiovanniArcudi/Programming-II-Coursework/blob/master/Marking%20Scheme.pdf).

---

_Author:_ [_Giovanni Arcudi_](https://github.com/GiovanniArcudi)

