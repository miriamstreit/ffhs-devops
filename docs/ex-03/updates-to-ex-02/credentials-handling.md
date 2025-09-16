# Credentials Handling

After the feedback the following changes were implemented to credentials handling:

1. Secrets were changed and stored in the Jenkins Credentials Store as secret text entries.
2. Secrets were added to Jenkinsfile through environment variables.
3. As recommended by Jenkins to [Prevent String Interpolation](https://www.jenkins.io/doc/book/pipeline/jenkinsfile/#string-interpolation) double quotes were replaced with single quotes for multi-line commands.

