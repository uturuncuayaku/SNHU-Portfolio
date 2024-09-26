# Journal Entry 
### What was the problem you were solving in the projects for this course? 
The problem at hand was to create a course planner from a set of coursese within the computer science department. 
These courses would have prerequisites, course title, and course name. This information was saved for later 
retrieval using a data structure implemented within C++ code. The AdvisingProgram.cpp contains all the methods
and functions needed to load and search from this list of courses. 

### How did you approach the problem? Consider why data structures are important to understand. 
I approached the problem by writing down pseudocode and generating a time complexity analysis of the code. The
main functions handled the menu choices or validated menu choices, after getting user input. This was 
reflected within the pseudocode but took a lot of code to properly implement. I made sure searches were
case insensitive and that no spaces were allowed within the course name, because it would prompt the user
to input the course number again after returning an error message and to the main menu of options. Saving the
data was important because I needed to understand how vector's work within the C++ STL to save course 
information such as course title, course name, and finally a list of prerequisites. This was achieved by creating
a class of Courses and that class encapsulating the information which would be compared with user input later when
they were seeking course information to plan their studies. 

### How did you overcome any roadblocks you encountered while going through the activities or project?
I overcame roadblocks by debugging my program using print statements that displayed values out to me while
I parsed the data to understand how to retrieve data from a comma seperated file effectively. This required me 
to save the index of the next piece of information. Then the last piece of information which was the 
prerequisites had me look for commas or the end of the line to properly filter out information that was to 
be saved within my courses vector.

### How was your work on the project expanded your approach to designing software and developing programs? 
I expanded my approach to designing software and developing programs to clearly outlining relevant pseudocode as
a testing portion of code to see what made the most sense for the scope of the problem. Since, I only had to save
and compare against a small number of courses it made the most sense to me to walk linearly through a vector 
instead of creating a binary tree of the information which would have increased the deployment time due to the 
added complexity. Keeping the scope and range of information clear helped me establish the algorithms neccessary 
to create a fast, complexity free program. This programs algorithms and functions could be pieced away in a
modular fashion to create a library of functions but I may have been unable to modularize and work forward as quickly
if I had not taken into account the small list of courses. This made choosing merge sort a valid option even though 
it is complex and memory exhaustive but still works on an average case of nlogn time complexity.

### How has your work on this project evolved the way you write programs that are maintainable, readable, and adaptable? 
The evolution of my code writing is the work of many before me such as Donald Knuth, the creators of LeetCode, the
amazing professors at Stanford and HackerRank that make up all the principal/junior engineers of all these incredible
platforms that are to numerous to name. Their work alongside the specific challenges posed by this class have gifted
me incredible insights into the inner workings of our technological lives within the modern world. They have all shown
me that determination and science go hand in hand together to create robust and enriching learning experiences while 
we accomplish our responsibilities within our daily life. This has made me evolve into a researcher and forever student
of the informatics and engineering departments. They show that maintanable actually means planning, readable means succinct 
that adaptable programs means clear, single purpose thoughts. These all translate to code through pseudocode, variable
naming and smaller functions with great comments.
