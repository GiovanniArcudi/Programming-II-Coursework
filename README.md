# University of Southampton - Programming II Coursework

This repository contains the both Specifications and Source Code for the [COMP1206 - Programming II](https://www.southampton.ac.uk/courses/modules/comp1206.page) Coursework.

## ECS Java Training School

The aim of this Coursework is to construct a simple simulation of a Java Training School. This school will admit students and they will receive training by a team of specialist instructors. 

<br/>

### 0.1 - Information for markers:

***Completed parts of the Coursework:***
Parts 1-6.

***Extensions to the Coursework:***
1)  JavaDoc documentation (which can be found in the doc subfolder).

2)  Implemented the prerequisites for Subjects and ensured that Students can only enrol on a course if they have all the prerequisites for that Course.

3)  Allowed Instructors to be able to teach more than one Course in a day (up to a certain limit) and Students to enrol in more than on Course at a time (up to a certain limit). 

**Note:** Extension 3 structurally interferes with the basic specification of the Coursework. Specifically, in order to store the courses assigned to an Instructor, being them more than one now, I am using an ArrayList of Courses rather than a single Course variable. This implies that the accessor method for an Instructor's assigned courses will now return an `ArrayList<Course>` rather than a `Course` object, therefore test TestPart3 cannot be passed unless modified.

<br/>

### 0.2  - Command Line usage:
    java Administrator [ConfigurationFileNme.txt] [Number Of Days]

To run the simulation, there must be provided a configuration file in the format specified in the documentation and a certain number of days to run the simulation for. 

In the configuration file it is possible to specify the maximum number of courses that a Student can enrol in and the maximum number of courses that an Instructor can teach, but when it is not specified the default value of one Course is used.

Similarly, if prerequisites for a Subject are not specified, the subject will be automaticaly sat to not have any prerequisites.

Example way of invoking the program:```java Administrator Configuration.txt 100```.

<br/>

### 0.3 -  Configuration File format:

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

