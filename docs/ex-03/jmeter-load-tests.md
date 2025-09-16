# JMeter Load Tests for Brewing Application

This document demonstrates how to run load tests on a brewing application using JMeter. The test plan includes logging in to obtain a JWT token from a REST API and using that token to perform load testing on protected resources by creating and deleting brewing processes.
The brewing process is the main feature of the application, so it makes sense to test the application's performance under load by simulating multiple users creating and deleting brewing processes concurrently. Of course the application is now build for one user with one brewerie, but at any time the application could be used by multiple users with multiple breweries.

## Use Case and Purpose

This test plan is designed to evaluate the brewing application’s performance under load. By simulating multiple users repeatedly creating and deleting brewing processes, we can assess the application's scalability, response times, and ability to handle concurrent requests without errors. These tests are essential to ensure the application’s stability and efficiency when accessed by many users simultaneously.

## Create a JMeter Test Plan

1. **Set Up the Test Plan**:
    - Open JMeter.
    - Right-click on the **Test Plan** node.
    - Select **Add > Threads (Users) > Thread Group**.

2. **Configure the Thread Group**:
    - Set **Number of Threads (users)** to `5` to simulate 5 users.
    - Set **Ramp-Up Period** to `5` seconds to gradually initiate the load.
    - Set **Loop Count** to `100` within the **Loop Controller** to repeatedly test the creation and deletion of brewing processes.
    - Enable **Same user on each iteration** to ensure the same user session is maintained per thread.

> **Explanation**: This configuration enables JMeter to simulate a load of 5 users, each making repeated requests. The ramp-up period ensures that the load is applied gradually, preventing a sudden spike in traffic that could lead to unrealistic results.

3. **Configure HTTP Header Manager**:
    - Right-click on the **Thread Group** node.
    - Select **Add > Config Element > HTTP Header Manager**.
    - Add the following headers in the **HTTP Header Manager**:

        - **Content-Type**: `application/json`
        - **Authorization**: `Bearer ${jwt_token}`

> **Explanation**: The **Authorization** header uses the `${jwt_token}` variable, which is dynamically set after the login request. This setup mimics real-world usage, where each user must authenticate before accessing protected resources.

4. **Request JWT Token**:
    - Add a **Once Only Controller** to the **Thread Group** to ensure the token request is executed only once per user session.
    - Under the controller, add an **HTTP Request** and configure it as follows:

        - **Server Name or IP**: `localhost`
        - **Port Number**: `8080`
        - **Path**: `/api/auth/login`
        - **Method**: `POST`
        - **Body Data**: 
          ```json
          {
            "email": "admin@schuum.ch",
            "password": "SchuumB1erTheB35t*"
          }
          ```

    - Add a **JSON Extractor** as a child of the HTTP Request to extract the JWT token from the login response:
        - **Variable name**: `jwt_token`
        - **JSON Path**: `$.token`

> **Explanation**: This step authenticates each user, simulating a real-world scenario where users log in before performing actions. Ensuring a unique session per user helps test how the application handles authentication at scale.

5. **Create Brewing Process**:
    - Add a **Loop Controller** to the **Thread Group** with 100 iterations to test repeated creation of brewing processes.
    - Under the Loop Controller, add an **HTTP Request** to create a brewing process:

        - **Server Name or IP**: `localhost`
        - **Port Number**: `8080`
        - **Path**: `/api/brewingprocess`
        - **Method**: `POST`
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
                "tankId": 2,
                "volume": 30
            },
            "startDate": "2025-01-01",
            "endDate": "2025-01-31",
            "sudNumber": "1229",
            "comment": "Das ist ein Test von JMeter",
            "color": "#e53935"
          }
          ```

    - Add a **JSON Extractor** as a child of this request to retrieve the brewing process ID from the response:
        - **Variable name**: `brewing_process_id`
        - **JSON Path**: `$.brewingId`

> **Explanation**: This test simulates multiple users creating brewing processes concurrently. This is crucial for evaluating whether the application’s database and server resources can handle many simultaneous operations without performance degradation.

6. **Delete Brewing Process**:
    - Add another **HTTP Request** to delete the brewing process using the extracted `brewing_process_id`:

        - **Path**: `/api/brewingprocess/${brewing_process_id}`
        - **Method**: `DELETE`

> **Explanation**: This test checks the application’s ability to handle deletion operations under load. In a high-traffic environment, it’s essential to ensure that deletions occur correctly and efficiently, freeing up resources such as brewing tanks for new processes.

7. **View Results Tree**:
    - Add a **View Results Tree** listener to the **Thread Group** to visualize the test results and analyze response times, response codes, and success/failure rates.

> **Explanation**: The **View Results Tree** listener allows for a detailed analysis of each request and response. This is essential for identifying performance bottlenecks, error rates, and any failed requests due to high load.
