//============================================================================
// Name        : AdvisingProgram.cpp
// Author      : Andres
// Date        : 6/27/2024
// Version     : 4.0
// Copyright   : Copyright 2023 SNHU COCE
// University Class: Data Structures and Algorithms
// University Title: Analysis and Design
// Description : Reading courses and course pre-requisites for an advising 
// team. Generating a menu with user input validation. Then reading the 
// courses and pre-requisites from a CSV file.
//============================================================================

#include <vector>
#include <string>
#include <iostream>
#include <stdexcept>
#include <fstream>
#include <thread>
#include <chrono>

// Forward declaration the MySort namespace functions
namespace MySort {
    template<typename RandomAccessIterator>
    void merge(RandomAccessIterator begin, RandomAccessIterator mid, RandomAccessIterator end);

    template<typename RandomAccessIterator>
    void mergeSort(RandomAccessIterator begin, RandomAccessIterator end);
}


/**
 * @class Course
 * @brief Represents a university or college course with a unique course number, name, and prerequisites.
 *
 * The Course class encapsulates details about an academic course, including its course number,
 * course name, and a list of prerequisites. It provides various constructors and comparison
 * operators to facilitate course management in educational software systems.
 *
 * @note The class is designed with default constructors, copy/move semantics, and operator overloads
 * to ensure ease of use and efficiency in handling course objects.
 *
 * Example usage:
 * @code
 * std::vector<std::string> preReqs = {"MATH101", "PHYS101"};
 * Course physics("PHYS102", "Introduction to Physics II", preReqs);
 * std::cout << physics.getCourseNumber(); // Outputs: PHYS102
 * std::cout << physics.getCourseTitle(); // Outputs: Introduction to Physics II
 * @endcode
 */
class Course {

public:
    Course() = default;

    Course(const std::string course_number, const std::string course_name, const std::vector<std::string> preReqs)
        : course_number(course_number), course_name(course_name), preReqs(preReqs) {}

    // Default copy constructor and copy assignment operator
    Course(const Course& other) = default;
    Course& operator=(const Course& other) = default;

    // Default move constructor and move assignment operator
    Course(Course&& other) noexcept = default;
    Course& operator=(Course&& other) noexcept = default;

    std::string getCourseNumber() const { return this->course_number; }
    std::string getCourseTitle() const { return this->course_name; }
    std::vector<std::string> getPreReqs() const { return this->preReqs; }

    bool operator<(const Course other) const {
        return this->course_number < other.course_number;
    }

    static bool compareCourseByNumber(const Course c1, const Course c2) {
        return c1.getCourseNumber() < c2.getCourseNumber();
    }

private:
    std::string course_number;
    std::string course_name;
    std::vector<std::string> preReqs;

};

/**
 * @class ParseCSV
 * @brief Parses a CSV file to extract course information, including course numbers, names, and prerequisites.
 *
 * The ParseCSV class provides functionalities to open and read a CSV file containing course details.
 * It parses each line to extract the course number, course name, and prerequisites, and stores this information
 * in a collection of Course objects.
 *
 * Example usage:
 * @code
 * ParseCSV parser;
 * if (parser.OpenFile("courses.csv")) {
 *     parser.ReadData();
 *     std::vector<Course> courses = parser.getCourses();
 *     // Process the courses as needed
 * }
 * @endcode
 */
class ParseCSV {

public:
    ParseCSV() : course_name_index(0), pre_req_index(0) {}


    /*
    * @brief Opens the file and validates its existence in order to parse
    * the comma seperated values.
    */
    bool OpenFile(std::string file_name) {

        std::string relative_path = file_name;
        file.open(relative_path);
        if (file.is_open()) {
            //std::cout << "File found and opened.\n";
            return true;
        }
        else {
            //std::cout << "File not found.\n";
            return false;
        }
    }


    /*
    * @brief Reads the data from the open file. Creates the course object from
    * the collected information found within a single line given by the file.
    */
    void ReadData() {
        //std::cout << "Reading File" << std::endl;
        std::string line;
        while (std::getline(file, line)) {

            parseCourseNumber(line);
            parseCourseName(line);
            parsePreReqs(line);
            std::string cour_num = this->getCourseNumber();
            std::string cour_nam = this->getCourseName();
            std::vector<std::string> pr_re = this->getPreReqs();
            Course single(cour_num, cour_nam, pr_re);
            courses.push_back(single);
            this->preReqs.clear();
        }

        file.close();

    }

    /*
    * @brief The first piece of information obtained from the line within the
    * comma seperated file and we capture information up until the first comma
    * that we find and then save that index for the next piece of information.
    */
    void parseCourseNumber(const std::string line) {

        std::string tmp = line;
        for (int i = 0; i < tmp.length(); ++i) {

            if (tmp[i] == ',') {

                int course_number_end_index = i;
                course_name_index = i + 1;
                course_number = tmp.substr(0, course_number_end_index);
                return;

            }
        }
    }

    /*
    * @brief Gets the course name from the given line of comma seperated values
    * and holds the index to the next set of data right at the comma's index.
    */
    void parseCourseName(const std::string line) {

        std::string tmp = line;
        for (int i = course_name_index; i < tmp.length(); ++i) {

            if (tmp[i] == ',') {

                int course_name_end_index = i;
                pre_req_index = course_name_end_index + 1;
                course_name = tmp.substr(course_name_index, course_name_end_index - course_name_index);
                return;
            }
        }
    }

    /*
    * @brief Obtains the prerequisites from the given line from a saved index of
    * the previously parsed string. Obtained by holding the last index before the
    * start of the next character of information. Such that we start right after
    * the comma for the next piece of information.
    */
    void parsePreReqs(std::string line) {

        std::string tmp = line;
        std::string tmp2;
        for (int i = pre_req_index; i < tmp.length(); ++i) {

            if (tmp[i] != ',') {
                tmp2 += tmp[i];
            }
            else {
                if (!tmp2.empty()) {
                    preReqs.push_back(tmp2);
                    tmp2 = "";
                }
            }
        }
        if (!tmp2.empty()) preReqs.push_back(tmp2);
    }

    // Getters for private field members
    std::string getCourseNumber() {
        return course_number;
    }
    std::string getCourseName() {
        return course_name;
    }
    std::vector<std::string> getPreReqs() {
        return preReqs;
    }
    std::vector<Course> getCourses() {
        return courses;
    }

private:
    std::ifstream file;
    int course_name_index;
    int pre_req_index;

    std::string course_number;
    std::string course_name;
    std::vector<std::string> preReqs;
    std::vector<Course> courses;

};


/**
 * @namespace MenuChoice
 * @brief Contains functions to handle different menu choices in the application.
 *
 * The MenuChoice namespace encapsulates functionalities related to various
 * menu operations that the application can perform, such as loading and processing
 * course data from a CSV file.
 */
namespace MenuChoice {

    /**
     * @brief Loads and processes course data from a CSV file.
     *
     * This function handles the task of loading course data from a specified CSV file. It
     * uses a ParseCSV object to open and read the file, extracting course information, and
     * populates the provided vectors with the parsed course data and valid course numbers.
     *
     *
     * @param path The file path to the CSV file. If empty, defaults to "ABCU_Advising_Program_Input.csv".
     * @param parser A reference to a ParseCSV object used to open and read the CSV file.
     * @param courses A reference to a vector of Course objects to be populated with the parsed data.
     * @param valid_course_numbers A reference to a vector of strings to be populated with valid course numbers.
     */
    static void One(
        std::string path,
        ParseCSV& parser,
        std::vector<Course>& courses,
        std::vector<std::string>& valid_course_numbers)
    {

        std::cout << "Loading";

        int count = 3;
        while (count-- > 0) {
            std::cout << ".";
            std::cout.flush();
            std::this_thread::sleep_for(std::chrono::milliseconds(100));
        }
        std::cout << std::endl;

        //Opening the file without a command line argument.
        if (path.empty()) {
            path = "ABCU_Advising_Program_Input.csv";
        }

        bool file_open = parser.OpenFile(path);
        if (file_open) {
            parser.ReadData();
        }
        else {
            std::cout << "File error.\n";
            std::exit(1);
        }

        //save course number, course title and pre requisites if there are any
        courses = parser.getCourses();
        valid_course_numbers.resize(courses.size());

        for (int i = 0; i < courses.size(); ++i) {
            //copy course numbers only
            valid_course_numbers[i] = courses[i].getCourseNumber();
        }

    }

    //Sorting algorithm applied to the list of courses and the vector of prerequisites for each course
    static void Two(std::vector<Course>& courses) {
        MySort::mergeSort(courses.begin(), courses.end());

        //Sorting prerequisites.
        for (int i = 0; i < courses.size(); ++i) {

            //Getting the small list of prerequisites
            std::vector<std::string> tmp = courses[i].getPreReqs();
            if (!tmp.empty()) {
                MySort::mergeSort(tmp.begin(), tmp.end());
            }
        }

        //Printing courses, "Course Number, Course Title, Prerequisite 1, Prerequisite N."
        for (int i = 0; i < courses.size(); ++i) {
            std::cout
                << courses[i].getCourseNumber()
                << ", "
                << courses[i].getCourseTitle()
                << ". ";

            //Checking a course for prerequisites
            std::vector<std::string> tmp = courses[i].getPreReqs();
            if (!tmp.empty()) {

                //Prints prerequisites for a given course
                for (int j = 0; j < tmp.size(); ++j) {
                    if (j == tmp.size() - 1) {
                        std::cout << tmp[j] << ".";
                    }
                    else {
                        std::cout << tmp[j] << ", ";

                    }
                }
            }

            std::cout << std::endl;

        }

    }

    //Option to print course title and other information
    static void Three(const std::vector<Course> courses, const std::vector<std::string> valid_courses) {

        //Get user input
        std::string course_input;
        std::cout << "What course do you want to know about? ";
        std::cin >> course_input;

        //Transform user input to uppercase if its lowercase to match our vector of strings
        if (!std::isupper(course_input[0]) && std::isalpha(course_input[0])) {
            for (int i = 0; i < course_input.length(); ++i) {
                if (std::isalpha(course_input[i])) {
                    course_input[i] = std::toupper(course_input[i]);
                }
            }
        }

        int courses_count = 0;
        int courses_size = valid_courses.size();
        //Validate the user input course is within our valid course list
        for (auto& course : valid_courses) {
            //The count to see if we are ever at the end of the valid courses list.
            //To determine if user has searched through all the courses 
            //and not found their course number.

            //Validating user input to a valid course number
            if (course == course_input) {
                for (Course courseTitle : courses) {
                    std::string valid_course = courseTitle.getCourseNumber();

                    //Successfully found a course from the user input so we retrieve the whole object
                    //to get all the information from the Course.
                    if (valid_course == course_input) {
                        std::cout << courseTitle.getCourseNumber() << ", " << courseTitle.getCourseTitle();

                        //Now checking for prerequisites because some courses don't have prerequisites
                        //and therefore won't require printing them to the terminal's display.
                        if (!courseTitle.getPreReqs().empty()) {
                            std::vector<std::string> vec_tmp = courseTitle.getPreReqs();
                            std::cout << "\nPrerequisites: ";

                            //Pretty printing prerequisites with commas unless we are at last prerequisite
                            for (int i = 0; i < vec_tmp.size(); ++i) {
                                std::cout << vec_tmp[i];
                                if (i != vec_tmp.size() - 1) {
                                    std::cout << ", ";
                                }
                            }
                        }
                        courses_count = INT_MIN;
                    }

                }

            }

            //Incrementing counter to keep track of the end of an unsuccessful search.
            courses_count++;
            if (courses_count == courses_size) {
                std::cout << "Input course not found. Try again.";
            }
        }

        std::cout << std::endl;
    }


    static void Nine() {
        std::cout << "Thank you for using the course planner!\n";
    }


}


/**
 * @class Menu
 * @brief Provides an interface for interacting with the course planner application.
 *
 * The Menu class manages the user interaction for selecting options in the course planner application.
 * It displays the main menu, processes user input, and validates menu choices.
 *
 * The class is designed to prompt the user with a set of options, validate their input, and store the
 * selected choice. It handles common input errors gracefully and ensures that the user selects a valid
 * option before proceeding.
 */
class Menu {
public:
    //Constructor which initializes variables
    Menu() : menu_choice{ 0 }, user_input{ "none" } {
        initialize_menu();
    }


    //Function to print menu
    void print_root_menu() const {
        std::cout
            << "Welcome to the course planner.\n"
            << "    1. Load Data Structure.\n"
            << "    2. Print Course List.\n"
            << "    3. Print Course.\n"
            << "    9. Exit\n";
    }


    //Function to get user input after menu is displayed
    void initialize_menu() {
        print_root_menu();
        do {
            std::cout << "What would you like to do? ";
            std::cin >> user_input;
        } while (!validate_choice(user_input));

    }


    //Function to make sure the choice is valid
    bool validate_choice(const std::string& input) {
        int choice;

        //Attempt to convert user input to integer
        try {

            // Converts the input to an integer
            choice = std::stoi(input);
        }
        //This block of code will run when the input was not a valid number
        catch (const std::invalid_argument&) {
            std::cout
                << "Invalid input. "
                << "Please enter a valid number.\n";
            print_root_menu();

            return false;
        }
        //This block of code will run when the input number is out of the range for an integer type.
        catch (const std::out_of_range&) {
            std::cout
                << "Invalid input. "
                << "The number is out of range.\n";
            print_root_menu();

            return false;
        }

        //If we are still in the function our number is valid so far,
        //we will check if the choice is one of the valid options
        //provided within the menu.
        if (choice == 1 ||
            choice == 2 ||
            choice == 3 ||
            choice == 9) {

            //This block will run when we have a valid choice set from user
            menu_choice = choice;
            return true;
        }
        //Otherwise our user's input was invalid.
        else {

            std::cout
                << "Invalid choice. "
                << "Please select a valid option.\n";
            print_root_menu();
            return false;                                          // Invalid choice
        }
    }

    //Getter for a private field member
    //returns the menu choice given by a user
    int getMenuChoice() {
        return menu_choice;
    }

    //Private field members
private:
    int menu_choice;
    std::string user_input;

};

/**
 * @namespace MySort
 * @brief Provides sorting algorithms and utilities for collections.
 *
 * The MySort namespace contains functions for performing merge sort on collections
 * that support random access iterators. It includes the merge function to combine
 * sorted subranges and the mergeSort function to recursively sort a range.
 *
 * These functions are designed to be generic and work with any collection type
 * that supports random access iterators, such as `std::vector`, `std::deque`, or arrays.
 *
 * Example usage:
 * @code
 * std::vector<int> data = { 5, 3, 8, 6, 2, 7, 4, 1 };
 * MySort::mergeSort(data.begin(), data.end());
 * // data is now sorted
 * @endcode
 */
namespace MySort {

    template<typename RandomAccessIterator>
    void merge(RandomAccessIterator begin, RandomAccessIterator mid, RandomAccessIterator end) {
        std::vector<typename RandomAccessIterator::value_type> temp;
        temp.reserve(std::distance(begin, end));

        RandomAccessIterator left = begin;
        RandomAccessIterator right = mid;

        while (left < mid && right < end) {
            if (*left < *right) {
                temp.push_back(std::move(*left));
                ++left;
            }
            else {
                temp.push_back(std::move(*right));
                ++right;
            }
        }

        temp.insert(temp.end(), std::make_move_iterator(left), std::make_move_iterator(mid));
        temp.insert(temp.end(), std::make_move_iterator(right), std::make_move_iterator(end));
        std::move(temp.begin(), temp.end(), begin);

    }


    template<typename RandomAccessIterator>
    void mergeSort(RandomAccessIterator begin, RandomAccessIterator end) {
        if (std::distance(begin, end) > 1) {
            RandomAccessIterator mid = begin + std::distance(begin, end) / 2;
            mergeSort(begin, mid);
            mergeSort(mid, end);
            merge(begin, mid, end);
        }
    }

}


/**
 * @brief Processes command line arguments to retrieve the path of a CSV file.
 *
 * This function checks if a command line argument for the path is provided and returns the path.
 * If no argument is provided, it returns an empty string, indicating that the default path or a prompt
 * should be used to obtain the file path.
 *
 * @param argv Array of command line arguments.
 * @param argc The count of command line arguments.
 * @return std::string Returns the path to the CSV file if provided, otherwise returns an empty string.
 */
std::string process_command_line_args(char* argv[], int argc) {
    std::string path;
    if (argc > 1) {
        path = argv[2];
        return path;
    }
    else {
        //std::cout << "Program can receive path as a command line argument.\n";
        return "";
    }
}

/**
 * @brief Entry point for the course planner application.
 *
 * The main function orchestrates the overall flow of the course planner application.
 * It processes command line arguments, initializes the main menu, handles user selections,
 * and performs various tasks such as loading course data, sorting courses, and displaying
 * course information.
 *
 * The application provides a menu-driven interface with the following options:
 * - Load course data from a CSV file.
 * - Print a sorted list of courses and their prerequisites.
 * - Display detailed information about a specific course.
 * - Exit the application.
 *
 * @param argc The number of command line arguments.
 * @param argv The array of command line arguments.
 * @return int Returns 0 upon successful completion.
 */
int main(int argc, char* argv[]) {
    //Checking the command line for a path name
    std::string path = process_command_line_args(argv, argc);

    //Main functions
    Menu display;
    ParseCSV parser;

    //Vector's holding parsed courses and a list 
    //of valid courses to facilitate user input validation.
    std::vector<Course> courses;
    std::vector<std::string> valid_courses;

    while (1) {
        int main_menu_choice = display.getMenuChoice();

        //Dependent upon user choice using a switch block 
        //from the menu list of options.
        switch (main_menu_choice) {

            //Option 1: Loads file and populates vector and validation helper of course numbers
        case 1:
        {
            MenuChoice::One(path, parser, courses, valid_courses);
            break;
        }

        //Option 2: Prints courses alphanumerically in ascending order
        case 2:
        {
            MenuChoice::Two(courses);
            break;
        }

        //Option 3: Prints the course title and the prerequisites for any individual course.
        case 3:
        {
            MenuChoice::Three(courses, valid_courses);
            break;
        }

        //Option 9: Exits program after displaying goodbye message.
        case 9:
        {
            MenuChoice::Nine();
            return 0;
        }

        //Default option: Error catching for debugging purposes. Exits program.
        default:
        {
            std::cout << "Error.\n";
            return 1;
        }

        }//end of switch case

        //Display menu after capturing/validating previous 
        //input and performing a corresponding action or output.
        display.initialize_menu();

    }

    return 0;
}//end of main function
