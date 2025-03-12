# GitHub Repositories - Recruitment Task

This API retrieves a list of non-fork GitHub repositories, including the owner's name, branches, and the last commit SHA.

## 🛠️ Used Tools
- **Java 21**
- **Spring Boot v3.4.0**
- **Maven**
- **IntelliJ IDEA**

## 👥 Who Can Use It?
Anyone interested in working with this API.

## 🚀 How to Use It?

1. Open Git Bash.
2. Navigate to the directory where you want to clone the repository.
3. Execute the following command:
   ```sh
   git clone https://github.com/BartKempa/rest-client-task.git

    Press Enter to create a local clone.
    To use the production profile, configure the datasource in src/main/resources/application.yml:

    github:
      token: Bearer "your_token"
      baseUrl: https://api.github.com/

    Run the application.

🌍 API Base URL

The API is available at:

http://localhost:8080

📌 Endpoint
Retrieve a List of Non-Fork GitHub Repositories

GET /api/v1/repositories/{username}
Accept: application/json

Example request:

curl -X GET "http://localhost:8080/api/v1/repositories/bartkempa" -H "Accept: application/json"

