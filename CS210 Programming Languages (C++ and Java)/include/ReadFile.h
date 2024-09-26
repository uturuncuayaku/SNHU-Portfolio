/**
 * Author: Andres Trujillo
 * Date: 4/12/2023
 * Class: CS210
 * Purpose: Saves an input file stream object. Then returns
 * an unordered map to caller.
 */

#pragma once
#include <iostream>
#include <fstream>
#include <unordered_map>
#include <string>
#include <cctype>
#include <vector>
#include <iomanip>
#include <iostream>
using namespace std;

class ReadFile
{
private:
	ifstream *inFS;
public:
	ReadFile() {
		inFS = nullptr;
	}
	
	//Destructor closes our stream and deletes the allocated object.
	~ReadFile() {
		if (inFS != nullptr) {
			inFS->close();
			delete inFS;
			inFS = nullptr;
		}
	}
	void openFile(string path);
	unordered_map<string, int> getFileContents();
	
};

