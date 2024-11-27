// AttendanceProcessor.cpp
#include <iostream>
#include <string>
#include <regex>

int main(int argc, char* argv[]) {
    if (argc != 4) {
        std::cerr << "Usage: AttendanceProcessor <StudentID> <Date> <Status>" << std::endl;
        return 1;
    }

    std::string studentID = argv[1];
    std::string date = argv[2];
    std::string status = argv[3];

    // Validate Student ID (numeric)
    if (!std::all_of(studentID.begin(), studentID.end(), ::isdigit)) {
        std::cerr << "Invalid Student ID format. It should be numeric." << std::endl;
        return 1;
    }

    // Validate Date (YYYY-MM-DD)
    std::regex datePattern("^\\d{4}-\\d{2}-\\d{2}$");
    if (!std::regex_match(date, datePattern)) {
        std::cerr << "Invalid Date format. Use YYYY-MM-DD." << std::endl;
        return 1;
    }

    // Validate Status
    if (status != "Present" && status != "Absent") {
        std::cerr << "Invalid Status. Use 'Present' or 'Absent'." << std::endl;
        return 1;
    }

    // Processing logic (e.g., updating database)
    // For demonstration, echoing back
    std::cout << "Attendance recorded for Student ID " << studentID << " on " << date << ": " << status << std::endl;

    return 0;
}
