# University of Southampton - Programming II Coursework

This repository contains the Source Code for the [COMP1206 - Programming II](https://www.southampton.ac.uk/courses/modules/comp1206.page) Coursework.

## MathDoku

The aim of this Coursework is to implement a [JavaFX](https://en.wikipedia.org/wiki/JavaFX) application to play a logic puzzle game called MathDoku (also known as _[KenKen](https://en.wikipedia.org/wiki/KenKen)_).

<br/>

## Rules of the Game

A player needs to fill the cells in an `N x N` square grid with the numbers 1 to N (one number per cell), while adhering to the following constraints:

* Each number must appear **exactly once in each row**.

* Each number must appear **exactly once in each column**.

Furthermore, there are groups of adjacent cells called **cages**, which are highlighted on the grid by thicker boundaries. Within each cage is a label showing a **target** number followed by an arithmetic operator (+, -, x, ÷). There is an additional constraint associated with these cages:

* It must be possible to **obtain the target by applying the arithmetic operator to the numbers in that cage**. For - and ÷, this can be done in any order.

<br/>


***Note:*** If a cage consists of a single cell, then no arithmetic operator is shown. The label simply shows the number that must be in that cell.

<br/>

Here's an image of what a MathDoku game looks like (courtesy of [Wikipedia](https://en.wikipedia.org/wiki/KenKen):

![Image of empty MathDoku](https://upload.wikimedia.org/wikipedia/commons/f/fd/KenKenProblem.svg)

And here's what the solved puzzle looks like:

![Image of full MathDoku](https://upload.wikimedia.org/wikipedia/commons/f/fe/KenKenSolution.svg)

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

`11+ 1,7
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
2÷ 34,35`

You can find more examples of different sizes in this zip file: [examples.zip]() (4x4_divdiff.txt is the only one that contains - and ÷ cages with more than 2 cells).

***Note:*** The same puzzle could be specified in multiple ways. Specifically, the order of the cages and of the cells within a cage do not matter. You should do simple error checking on the input (e.g., whether cells within a cage are adjacent and whether each cell is part of exactly one cage) and notify the user if a mistake was detected. You do not need to check whether the puzzle can be solved.

**[7 marks]**











The simulator uses the format of the configuration file provided in the Coursework Specification. However, for the purposes of the extensions the format has been enhanced. 

The format consists of a `CLASS_NAME` and some `PROPERTIES`.

<br/>

#### Valid `CLASS_NAME` examples:

1. ```school:University of Southamptonn,2,2``` - Describes a school having _name_ = "University of Southampton", _maxEnrollableCourses_ = "2", _maxAssignableCourses_ = "2".

2. ```subject:Programming I, 1, 1, 5, 2``` - Describes a Subject having _descriprtion_ = "Programming I", _subjectID_ = "1", _specialismID_ = "1", _duration_ = "5" days, _prerequisites_ = the course whose _subjectID_ is "2".
        
3. ```student:Peter,M,60``` - Describes a Student having _name_ = "Peter", _gender_ = "M", _age_ = "20".

4. ```Teacher:Yvonne,F,55``` - Describes a Teacher having _name_ = "Yvonne", _gender_ = "F", _age_ = "55".

<br/>

#### Valid `PROPERTIES` format:

`PROPERTIES` consist in a certain number of fields, separated by commas (no extra spaces), which describe every object property. 
 The reader fetches all the lines and selects the data needed to create an Object of the specified class. 

For different classes the properties format looks as following:
 * For School objects: `[SchoolName, maxEnrollableCourses (optional), maxAssignableCourses (optional)]`.
 * For Subject objects: `[SubjectName, subjectID, specialismID, duration, prerequisite-prerequisite-prerequisite... (optionals)`].
 * For Person objects: `[SubclassNameAsString (one of: Student, Teacher, Demonstrator, OOTrainer, GUITrainer), gender('M' or 'F'), age]`.

Example `PROPERTIES`:

    school:UniversityOfSouthampton,2,2
    subject:Lab 1,2,2,2,1-3
    student:Annabelle,F,31
    Teacher:Yvonne,F,55

<br/>


## 1. Extensions

### 1.1 Subjects Prerequisite
Inspired by Coursework Specifications I have decided to implement possible prerequisites for each ```Subject``` object.

This means that the user is able, via the configuration file, to decide if a student has to obtain the certificate for other subjects (specifing which of them by ```subjectID```) to be able to enroll in a course about a specific subject.

<br/>

### 1.2 Modify the way the Configuration File is read by SimulationUtility

To implement this idea I firstly have added to the ```SimulationUtility```'s ```subjectCreator(String properties)``` method the functionality of reading properties strings with one extra property.

To do so, I had to take into account the case for which ```properties.split(",")``` is equal to 5 and store the extra property (prerequisites) in a new String.

Of course, there is no limit to the number of possible prerequisites for a Subject so, I decided that, if more than one presequisite is to be added it should be used "-" to separate the subjectIDs.

In this way, splitting the prerequisites String at every("-"), I easily obtained the prerequisite for that subject, singularily.

#### 1.2.1 - Add Prerequisites property to Subject's class
In oder to keep track of a Subject's prerequisites, I had to introduce a new property to the Subject's class: prerequisites,
an ArrayList of Integers, specifically containing all the subjectsIDs of the subjects that a student is required to take before studing this one.
In this ArrayList I added, one at a time, the subject prerequisite read in SimulationUtility's subjectCreator(String properties) method.

#### 1.2.2 - Modify the criterion for which students are enrolled in a course
Of course, once the prerequisites are stored they have to be checked every time a student is about to be enrolled in a new course. In my code, this is done in Course's enrolStudent(Student student) method.

#### 1.2.3 - Update the toString() method in School and Subject
To finish, I updated the toString() method in School and Subject, taking into account that the Subjects now have a new property that has to be printed out.

<br/>

### 1.3 - Multiple courses handling for Instructors and Students:
Because I enjoyed working with the input handling from the configuration file, I decided to also implement the second of the proposed Extensions.

Instructors are now able to teach more than one course in a day (up to a certain limit) and the students can enrol in more than one course at a time (up to a certain limit). 
    
#### 1.3.1 - Modify the way the Configuration File is read by SimulationUtility
To implement this idea I firstly have added to the SimulationUtility's schoolCreator(String properties) the functionality of reading properties strings with one or two extra properties.
To do so, I had to take into account the case for which properties.split(",") is greater than 1 and store the extra properties (maxAssignableCourses and maxEnrollableCourses) in new Strings.
If maxAssignableCourses and maxEnrollableCourses are not specified they will by default be set to 1.

#### 1.3.2 - Add Prerequisites property to School's class
In oder to keep track of the maxAssignableCourses and maxEnrollableCourses, I had to introduce 2 new properties to the School's class: int maxAssignableCourses and int axEnrollableCourses.
        
#### 1.3.3 - Modify the critarion for which students are enrolled in a course and instructors are assigned to a course
Of course, once the prerequisites are stored they have to be checked every time a student is about to be enrolled in a new course and an instructor assigned to a course.
In my code, this is done in School's assignInstructorToEachCourse() method and in School's enrolFreeStudents() method.

<br/>

### 1.4 - Person Generator:
To have a more realistic simulation I decided to implement a Person Generator.

Using the `createPerson(String)` method it creates a new `Person` object of the specified subclass, taking a random name and gender from the `RANDOM_NAMES_AND_GENDERS` 20x2 matrix and random generated age.

It also keeps in consideration that the age limit for a Student is lower than the one of an Instructor.
Being all self contained in the `PersonGenerator` class, I suggest to read the more detailed methods description from there.

<br/>

### 1.5 - Scheduling Updates:
To improve the general performance of the code I added a method to the School class: `getActiveStudents()`.
It simply returns an ArrayList of the students enrolled in at least one course. 

Despite being a very basic concept, checking if a student is enrolled to ad least one course has proven to be repeatedly useful throughout the project development, to avoid iterating through all of them many times.  

---

_Author:_ [_Giovanni Arcudi_](https://github.com/GiovanniArcudi)

