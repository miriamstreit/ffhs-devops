Backend: [![Quality Gate Status](https://sonar.sw3.ch/api/project_badges/measure?project=schuumapp-backend&metric=alert_status&token=sqb_24a0ed37cee49af0fd0dd214cfef1765fb7ce5fa)](https://sonar.sw3.ch/dashboard?id=schuumapp-backend)


Frontend: [![Quality Gate Status](https://sonar.sw3.ch/api/project_badges/measure?project=schuumapp-frontend&metric=alert_status&token=sqb_aeac842f1c40406679c7e96ff5b05b33a3af8d05)](https://sonar.sw3.ch/dashboard?id=schuumapp-frontend)

# schuum-v2

## Running the app

1. Install Docker Desktop
2. Start Docker Desktop
3. Make sure the ports 8080 and 81 are free
4. `sudo docker-compose up`
5. Open the web browser with http://localhost:81
6. Login using admin@schuum.ch and 'SchuumB1erTheB35t*'

## Making changes

If you have made changes to the code, it would be best to run `docker-compose down`
and then `sudo docker-compose up --build --force-recreate` to avoid running into cache problems

## Populating the database

The empty database will be initialized by the backend once it has started up. The backend
<b> will not </b> add data if the database has already been initialized. To clear the database, you
need to run `docker-compose down` so you can start fresh.
