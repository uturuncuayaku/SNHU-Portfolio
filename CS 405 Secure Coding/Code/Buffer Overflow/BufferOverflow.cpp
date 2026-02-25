// BufferOverflow.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

#include <iomanip>
#include <iostream>

int main()
{
  std::cout << "Buffer Overflow Example" << std::endl;
  const std::string account_number = "CharlieBrown42";
  char user_input[20];

  while (true) {
    //Initializing variables..
    int length = 0;
    char ch;
    bool too_long = false;
    // This runs once and then everytime 
    // user input is incorrect.
    std::cout << "Enter a value: ";

    // Run's until a new line is found
    // or if length grows beyond the
    // size of the user input buffer
    // minus one for the null terminator.
    while (std::cin.get(ch) && ch != '\n') {
      if (length < static_cast<int>(sizeof(user_input))-1) {
        // saves or overwrites from the start
        user_input[length++] = ch;
      } else {
        // removes the characters
        // that may be in the std::cin buffer
        too_long = true;
        std::cout << "Too many characters!\n";
        while (ch != '\n' && std::cin.get(ch)) {}
        break;
      }
    }
    // add our terminating character and break
    if (length < 20 && !too_long) {
      user_input[length] = '\0';
      break;
    }
  }

  std::cout << "You entered: " << user_input << std::endl;
  std::cout << "Account Number = " << account_number << std::endl;
}

// Run program: Ctrl + F5 or Debug > Start Without Debugging menu
// Debug program: F5 or Debug > Start Debugging menu
