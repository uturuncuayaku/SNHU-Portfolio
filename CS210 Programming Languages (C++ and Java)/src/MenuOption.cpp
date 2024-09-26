/**
 * Author: Andres Trujillo
 * Date: 4/12/2023
 * Class: CS210
 * Purpose: Displays menu, gets user input and returns it for evaluation.
 */
#include "MenuOption.h"

/*
* MenuOption::display Displays the menu and gets user option from menu
* @param none
* @return int user input
*/
int MenuOption::display()
{
	//Local variables
	string _userInput;
	int _input = 0;
	bool valid = false;

	//Will loop until we receive a valid choice selection from user.
	while (!valid) {
		cout << "Menu" << endl;
		cout << "1. Search for Produce." << endl;
		cout << "2. Print list from search history." << endl;
		cout << "3. Print histogram from the produce purchase history." << endl;
		cout << "4. Exit program." << endl;
		cout << "Select an option: ";
		
		//Captures input as string and attempts to convert to menu choice. Otherwise
		//it will throw an invalid argument exception and loop or throw
		//an out of range exception for conversion and loop back around.
		cin >> _userInput;
		try {
			_input = stoi(_userInput);
			
			if (_input <= 0 || _input >= 5) {
				valid = false;
				cout << "Please choose from one of the options." << endl;
			}
			else {
				valid = true;
				return _input;
			}
		}
		catch (const std::invalid_argument& invalidArgException) {
			cout << "User input: " << invalidArgException.what() << " ..Invalid option!" << endl;

		}
		catch (const std::out_of_range& out_of_rangeException) {
			cout << "User input: " << out_of_rangeException.what() << " ..Out of range!" << endl;

		}
	}
}
