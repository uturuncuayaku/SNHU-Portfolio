/**
 * Author: Andres Trujillo
 * Date: 4/12/2023
 * Class: CS210
 * Purpose: CPP file containing definition of Header file declarations.
 */
#include "ReadFile.h"

/*
* ReadFile::openFile opens the file or exits the program if it doesn't exist
* @param path the pathname to where the file is located.
* @return void because we return a map to the user upon request of parsed contents
*/
void ReadFile::openFile(string path) {
	//Pointer to ifstream because its easier for me to work with than an object
	//to the purchase history file of the Grocery store's produce section.
	ifstream* purchaseHistory = new ifstream();

	//IOStreams do not throw exceptions but they do set error flags.
	//The following checks directly if the stream is returning an error code
	//or it also checks to see any of the three error 
	//flags('failbit', 'eofbit', or 'badbit') are set on the stream.
	if (*purchaseHistory and !purchaseHistory->fail()) {
		cout << "Successfully opened file." << endl;
	}

	purchaseHistory->open(path);
	//Dereference the pointer and call it's member function to check if the file 
	//was opened and found. This piece of code is equivalent to (*purchaseHistory).is_open().
	if (!purchaseHistory->is_open()) {
		cout << "Error opening file! It may not exist." << endl;
		exit(1);
	}
	//Save our purchase history stream to a private field member variable 
	//because the text needs to be parsed through to generate our report.
	this->inFS = purchaseHistory;
}


/*
* ReadFile::getFileContents Returns an unordered mapping of the file contents
* @param none
* @return map of parsed produce purchase history
*/
unordered_map<string, int> ReadFile::getFileContents()
{
	//Local variables
	string produce;
	int count = 0;
	unordered_map<string, int> item_frequency;

	//Gets a line from our created file stream object
	//until the end of file.
	while (getline(*this->inFS, produce)) {
		//Checks the first letter for capitalization to
		//allow us to use a case insensitive search. It
		//then makes it lowercase using a C function from
		//the standard library.
		if(isupper(produce[0]))
			produce[0] = tolower(produce[0]);

		//We add the word to the unordered mapping and 
		//increment its type which is integer.
		item_frequency[produce]++;
	}

	//returns our map to the caller
	return item_frequency;
}



