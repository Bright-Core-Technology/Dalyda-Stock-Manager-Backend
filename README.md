# The Admin reset email is not part of the front end design but it is in the backend, add

# it to the frontend

# Add weight on the sles table

# Build and start all services

-
    - **docker-compose up -d**

- **Swagger UI**: http://localhost:8080/swagger-ui.html
-
    - **Application**: http://localhost:8080

# Check the Containers and Images in Docker

-
    - **docker ps**

'docker ps' - list of all running containers
'docker exec -it <container_id> bash' - access the container shell
'su - postgres' - switch to Postgres user
'psql -U postgres' - connect to postgres interactive shell
'\l' - view all databases
'\c <database_name>' - connect to your database
'\dt' - check the tables in the database
'SELECT * FROM <table_name>;' - select all data from table
to exit type '\q' exit the postgres shell then 'control D'

stopping all the containers
docker-compose down

when you add a new api for it to be visible, you need to restart the app first
docker-compose down app
docker-compose up -d --build app