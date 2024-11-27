// GradeProcessor.cpp
#include <iostream>
#include <string>
#include <regex>

int main(int argc, char* argv[]) {
    if (argc != 3) {
        std::cerr << "Usage: GradeProcessor <StudentID> <Grade>" << std::endl;
        return 1;
    }

    std::string studentID = argv[1];
    std::string grade = argv[2];

    // Validate Student ID (numeric)
    if (!std::all_of(studentID.begin(), studentID.end(), ::isdigit)) {
        std::cerr << "Invalid Student ID format. It should be numeric." << std::endl;
        return 1;
    }

    // Validate Grade (A, A-, B+, B, B-, C, D, F)
    std::regex gradePattern("^(A-|A|B\\+|B|B-|C|D|F)$");
    if (!std::regex_match(grade, gradePattern)) {
        std::cerr << "Invalid Grade format. Use A, A-, B+, B, B-, C, D, or F." << std::endl;
        return 1;
    }

    // Processing logic (e.g., updating database)
    // For demonstration, echoing back
    std::cout << "Grade '" << grade << "' recorded for Student ID " << studentID << "." << std::endl;

    return 0;
}
