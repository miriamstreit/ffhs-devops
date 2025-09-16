# JMeter Functional Tests

This example demonstrates how to run functional tests using JMeter. It includes a test plan to log in and retrieve a JWT token from a REST API, then use the token to access a protected resource.
It tests the login functionality and the ability to create and retrieve brewing processes. This is important since the brewing process and obvioulsy the login are the core features of the application.

## Create a JMeter Test Plan

1. **Create a New JMeter Test Plan:**

    - Open JMeter.
    - Right-click on **Test Plan** > **Add > Threads (Users) > Thread Group**.
    - Right-click on **Thread Group** > **Add > Config Element > HTTP Header Manager**.

2. **Configure HTTP Headers in HTTP Header Manager:**

    - **Name**: Content-Type | **Value**: application/json
    - **Name**: Accept | **Value**: application/json
    - **Name**: Authorization | **Value**: Bearer `${jwt}`

    > **Note**: `${jwt}` will be set in the test plan.

3. **Add HTTP Request to Retrieve JWT Token:**

    - Right-click on **Thread Group** > **Add > Logic Controller > Once Only Controller**.
    - Add an **HTTP Request** to the **Once Only Controller**:
        - **Server Name or IP**: `localhost`
        - **Port Number**: `8080`
        - **Path**: `/api/auth/login`
        - **Method**: POST
        - **Body Data**: 
        ```json
            {"email": "admin@schuum.ch", "password": "SchuumB1erTheB35t*"}
        ```

    - Add a **JSON Extractor** as a child of the **HTTP Request**:
        - **Name of created variable**: `jwt_token`
        - **JSON Path expressions**: `$.token`

    - Add a **Response Assertion** as a child of the **HTTP Request**:
        - **Field to Test**: Text Response
        - **Pattern Matching Rules**: Contains
        - **Patterns to Test**: token

4. **Add HTTP Request to Access a Protected Resource:**

    - Right-click on **Thread Group** > **Add > Logic Controller > Once Only Controller**.
    - Add an **HTTP Request** to the **Once Only Controller**:
        - **Server Name or IP**: `localhost`
        - **Port Number**: `8080`
        - **Path**: `/api/tanks`
        - **Method**: GET

    - Add a **Response Assertion** as a child of the **HTTP Request**:
        - **Field to Test**: Response Code
        - **Pattern Matching Rules**: Contains
        - **Patterns to Test**: `200`

5. **Add HTTP Request to Create a Core Process (Brewing Process):**

    - Right-click on **Thread Group** > **Add > Logic Controller > Once Only Controller**.
    - Add an **HTTP Request** to the **Once Only Controller**:
        - **Server Name or IP**: `localhost`
        - **Port Number**: `8080`
        - **Path**: `/api/brewingprocesses`
        - **Method**: POST
        - **Body Data**:
            ```json
            {
                "beerType": {
                    "beerTypeId": 12,
                    "name": "Lager",
                    "duration": 30,
                    "deleted": false
                },
                "tank": {
                    "tankId": 1,
                    "volume": 50
                },
                "startDate": "2025-01-01",
                "endDate": "2025-01-31",
                "sudNumber": "1229",
                "comment": "Das ist ein Test von JMeter",
                "color": "#e53935"
            }
            ```

    - Add a **Response Assertion** as a child of the **HTTP Request**:
        - **Field to Test**: Response Code
        - **Pattern Matching Rules**: Contains
        - **Patterns to Test**: `200`

> **Explanation**: This test is necessary to ensure that the application can create a brewing process. This is the main feature of the application, and it is important to test it to ensure that it works as expected.

6. **Add HTTP Request to Attempt Another Brewing Process (Expected Failure):**

    - Right-click on **Thread Group** > **Add > Logic Controller > Once Only Controller**.
    - Add an **HTTP Request** to the **Once Only Controller**:
        - **Server Name or IP**: `localhost`
        - **Port Number**: `8080`
        - **Path**: `/api/brewingprocesses`
        - **Method**: POST
        - **Body Data**:
            ```json
            {
                "beerType": {
                    "beerTypeId": 12,
                    "name": "Lager",
                    "duration": 30,
                    "deleted": false
                },
                "tank": {
                    "tankId": 1,
                    "volume": 50
                },
                "startDate": "2025-01-01",
                "endDate": "2025-01-31",
                "sudNumber": "1229",
                "comment": "Das ist ein Test von JMeter",
                "color": "#e53935"
            }
            ```

> **Explanation**: This Test should fail because the tank is already in use. This is a good example of a functional test that checks the application's ability to handle errors and exceptions.

    - Add a **Response Assertion** as a child of the **HTTP Request**:
        - **Field to Test**: Text Response
        - **Pattern Matching Rules**: Contains
        - **Patterns to Test**: `"Dieser Tank wird zu diesem Zeitpunkt bereits verwendet."`

7. **Add a View Results Tree Listener:**

    - Right-click on **Thread Group** > **Add > Listener > View Results Tree**.
