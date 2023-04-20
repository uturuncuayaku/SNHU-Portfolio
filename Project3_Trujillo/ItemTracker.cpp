/**
 * Author: Andres Trujillo
 * Date: 4/12/2023
 * Class: CS210
 * Purpose: Displays menu, gets user input and returns it for evaluation.
 */
#include "ReadFile.h"
#include "MenuOption.h"

using namespace std;

class Produce {
private:
	int freq;
	string name;
public:
	Produce() : freq(0), name("none") {}
	Produce(int _freq, string _name) {
		this->freq = _freq;
		this->name = _name;
	}
	int getFreq() {
		return this->freq;
	}
	string getName() {
		return this->name;
	}
};
/*
* Main entry into our program. Welcome's the user and loads the file, parses the history
* creates a mapping of the file and then displays menu of options. The user can choose
* between finding more produce or printing the list in a couple different formats.
*/
int main() {
	//Display welcome
	string welcome = "Welcome to ChadaTech's Grocery tracking system!";
	cout << welcome << endl;
	cout << setfill('*') << setw(welcome.size()) << "" << endl;

	//Opens file and retrieves contents
	string path = "E:\\Project3\\CS210_Project_Three_Input_File.txt";
	ReadFile produceItems = ReadFile();
	produceItems.openFile(path);
	unordered_map<string, int> produceFreq = produceItems.getFileContents();

	//Counts the characters in a produce name for pretty printing
	int maxProduceLen = 0;
	for (auto it = produceFreq.begin(); it != produceFreq.end(); ++it) {
		string p = it->first;
		int c = it->second;
		//cout << p << ": " << c << endl;
		if (p.size() > maxProduceLen) {
			maxProduceLen = p.size();
		}
	}

	//Creates vector of list the size of the amount of  unique produce we have in our store
	//to add to from user searches because user searches will make up the list we print from
	//instead of directly from the mapping.
	vector<Produce> groceryProduceList(produceFreq.size());
	string produceName;
	bool askUser = true;

	//Creates and displays menu and uses a switch statement to take appropriate action from
	//user options.
	auto it = produceFreq.begin();
	while (1) {
		MenuOption menu;
		int userOption = menu.display();

		switch (userOption) {
		case 1:

			//While loop will continue looping until user supplies a valid produce item, found from the grocery's purchase history.
			while (askUser) {
				cout << "Lets find some produce! Note: search is case-insensitive." << endl;
				cout << "Enter produce name: ";
				cin >> produceName;

				if (isupper(produceName[0]))
					produceName[0] = tolower(produceName[0]);

				if (produceFreq.find(produceName) != produceFreq.end()) {
					groceryProduceList.push_back(Produce(produceFreq[produceName], produceName));
					cout << "Found, " << produceName << " from purchase history: " << produceFreq[produceName] << endl;
					askUser = false;
				}
				else {
					cout << "Invalid produce name. Not found in database!" << endl;
					askUser = true;
				}
			}
			//reset variable if we are outside the loop
			askUser = true;
			cin.clear();
			cin.ignore(100, '\n');
			break;

		case 2:
			//iterators that hold the object returned from them and I save the values from 
			//the objects into local variables for easier manipulation
			for (auto it1 = groceryProduceList.begin(); it1 != groceryProduceList.end(); ++it1) {
				string name = it1->getName();
				int freq = it1->getFreq();

				if (name.compare("none") != 0){
					if (islower(name[0]))
						name[0] = toupper(name[0]);
					cout << name << " " << freq << endl;
				}
			}
			break;

		case 3:
			//same as case 2 but this time I use the frequency count to print 
			//that many stars
			for (auto it1 = groceryProduceList.begin(); it1 != groceryProduceList.end(); ++it1) {
				string name = it1->getName();
				if (name.compare("none") == 0) continue;
				int stars = it1->getFreq();

				if (name.compare("none") != 0) {
					if (islower(name[0]))
						name[0] = toupper(name[0]);
					cout << name << " ";
				}
				while (stars > 0) {
					cout << "*";
					stars--;
				}
				cout << endl;
			}
			break;
		case 4:
			exit(0);

		default:
			cout << "Error!" << endl;
			break;
		}
	}
	return 0;
}