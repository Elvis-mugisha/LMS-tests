# Library Management System Tests- Project Submission

## Project Overview
The Library Management System project is designed to manage books, patrons, and transactions efficiently. The goal of this project was to implement various software testing features to ensure the system's reliability, robustness, and functionality.

## Project Objectives
- **Write unit and integration tests** to ensure thorough testing of the Library Management System.
- **Verify the adequacy of test coverage** for both unit and integration tests.
- **Evaluate the effectiveness of test cases** in identifying defects and ensuring system reliability.
- **Assess the robustness of exception handling mechanisms** tested in the project.

## Implemented Testing Features

### 1. Unit Testing
- **Tool/Framework**: JUnit
- **Description**: Unit tests were written for individual components and methods using the JUnit testing framework. Critical functionalities related to user authentication, book management, patron management, and transaction management were thoroughly tested.

### 2. Integration Testing
- **Tool/Framework**: JUnit
- **Description**: Integration tests were performed to validate the interaction between different modules and components. End-to-end scenarios such as adding a new book, updating patron information, performing transactions, and generating reports were covered.

### 3. Mocking and Stubbing
- **Tool/Framework**: Mockito
- **Description**: Mockito was used to mock dependencies and stub external dependencies in both unit and integration tests. This isolated the components under test and controlled their behavior during testing.

### 4. Parameterized Testing
- **Tool/Framework**: JUnit Parameterized Tests
- **Description**: Parameterized tests were written to test the system with different input combinations and edge cases, ensuring robustness and reliability under various scenarios.

### 5. Exception Handling
- **Tool/Framework**: JUnit
- **Description**: Tests were written to verify the error-handling behavior of methods and components. Scenarios where specific exceptions were expected were tested to ensure the system gracefully handled exceptions and maintained stability.

### 6. Code Coverage Analysis
- **Tool/Framework**: JaCoCo
- **Description**: JaCoCo was used for code coverage analysis, assessing the coverage of unit and integration tests. A high percentage of code coverage was aimed to minimize the risk of undetected defects and vulnerabilities.

### 7. Regression Testing
- **Tool/Framework**: JUnit
- **Description**: Existing tests were updated to accommodate changes or enhancements made to the Library Management System. Regression testing was performed to ensure that modifications did not introduce regressions or unintended side effects.

### 8. Testing Best Practices
- **Guidelines**: Clear naming conventions, organization, and documentation of tests.
- **Description**: Best practices were followed throughout the testing process. Tests were descriptively named, effectively organized, and thoroughly documented, including descriptions of test scenarios, expected outcomes, and any preconditions or dependencies.

## Repository Links
- **GitHub Repository**: [LMS-tests](https://github.com/Elvis-mugisha/LMS-tests)
- **Video Recording**: [Loom Video Link](#)

## Getting Started

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Elvis-mugisha/LMS-tests.git

## Install Dependencies

2. **Intsall Dependencies**
   ```terminal
 mvn clean install

## Run Test

3. **Run Test**
   ```terminal
mvn test 
   
