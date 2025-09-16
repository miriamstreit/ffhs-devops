# Analysis of the sonarqube report

## Frontend

### Issue: Replace `as` with upper case format `AS`.

In the Docker image, keywords should be written uppercase. This can be fixed easily without any issues.

### Issue: Use a specific version tag for the image.

It's important to use a fixed version tag for the underlying Docker image to ensure functionality. This can be fixed easily, however it would need to be maintained to stay up to date.

### Issue: Add either an 'id' or a 'scope' attribute to this ... tag.

Tags need to be able to be identified using their ID field. This can be fixed easily.

### Issue: Unexpected empty source

There are multiple occurences of empty style tags. These can be removed without any problems.

### Issue: Unexpected var, use let or const instead.

With newer JavaScript versions it's better to use let or const instead of var to indicate whether a variable is read only or writable. This can be fixed easily.

### Security hotspot: The nginx image runs with root as the default user. Make sure it is safe here.

The Dockerfile uses the default user (root) to run the application. This could be done with less permissions without losing functionality and should therefore be done to ensure better security.

### Security hotspot: Omitting --ignore-scripts can lead to the execution of shell scripts. Make sure it is safe here.

The Dockerfile contains an `npm install` command. The flag `--ignore-scripts` should be added so no foreign scripts can be executed. This flag will not hinder the functionality and can easily be added.

## Backend

### Issue: Remove this field injection and use constructor injection instead.

Field injection of Spring Boot was used using @Autowired. SonarQube would prefer a Constructor injection. The fix would be simple, but would need to be tested thoroughly and might have some other implications.

### Issue: Remove this unused import ...

Some test classes contained some unused imports at the time of the scan. This issue can be fixed easily and will not break anything.

### Security Hotspot: Cross-Site Request Forgery (CSRF)

The Cross-Site Request Forgery Protection that is enabled by default in Spring Boot was disabled in our code. This is because the app was never meant to be hosted on a real webserver within the scope of the project and thus the CSRF protection rather got in our way than helping. In a production environment however, this would need to be configured properly to avoid CSRF attacks.
