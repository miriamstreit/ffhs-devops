# Report Display

To allow viewing the reports in Jenkins the default settings had to be changed since Jenkins blocked scripts and stylesheets by default. 

The relevand setting is set in the docker-compose file which starts Jenkins. The following environment variable was set to pass the relevant arguments to Jenkins:

```yaml
JAVA_OPTS: "-Dhudson.model.DirectoryBrowserSupport.CSP=\"sandbox allow-same-origin allow-scripts; default-src 'none'; img-src 'self' data:; font-src 'self'; style-src 'self' 'unsafe-inline'; script-src 'unsafe-inline' 'self'\""
```

Now Jenkins no longer blocks the JS and CSS files which the JMeter and ZAP Reports are generating to display the results in a readable way.
