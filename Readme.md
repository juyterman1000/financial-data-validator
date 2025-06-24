# Financial API Test Framework

This is a sample REST API test framework using **RestAssured**, **TestNG**, and **Maven** to validate a fictional financial service API.

## 📦 Project Structure
financial-api-test-framework/
├── pom.xml # Maven config and dependencies
├── src/
│ ├── test/
│ │ ├── java/
│ │ │ └── com/adalakoti/api/tests/FinancialAPITest.java
│ │ └── resources/
│ │ ├── testng.xml # TestNG suite definition
│ │ └── schemas/
│ │ └── account_balance_schema.json
└── README.md # Project overview and usage
##  Technologies Used

- Java 11
- RestAssured 5.3.0
- TestNG 7.8.0
- JSON Schema Validator

## How to Run

1. **Clone the repo**:
    ```bash
    git clone <repo-url>
    ```

2. **Import the project into IntelliJ** (as a Maven project).

3. **Run tests** via:
    ```bash
    mvn test
    ```

   Or directly from `testng.xml` via IntelliJ.

## Tests Included

- **GET /accounts/{accountId}/balance**
- **POST /transactions**
- **GET /transactions**
- **POST /protocol/validate** (with ISO8583 message)

## License

MIT License (use freely for educational or evaluation purposes).
