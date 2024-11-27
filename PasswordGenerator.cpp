#include <iostream>
#include <string>

std::string generatePassword(const std::string& facultyName) {
    std::string password = facultyName.substr(0, 4) + "@1234";
    return password;
}

int main(int argc, char* argv[]) {
    if (argc < 2) {
        std::cerr << "Usage: PasswordGenerator <facultyName>" << std::endl;
        return 1;
    }

    std::string facultyName = argv[1];

    std::string password = generatePassword(facultyName);
    std::cout << password << std::endl;

    return 0;
}
