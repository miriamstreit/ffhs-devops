# Installing JMeter in Jenkins

## Jenkins Plugin installation

To install JMeter we referenced the [Official Jenkins Documentation](https://www.jenkins.io/doc/book/using/using-jmeter-with-jenkins/#install-the-performance-plugin).

Navigate to `Dashboard > Manage Jenkins > Plugins > Available Plugins` and search for "performance" (a plugin in which JMeter is included).

After a restart of Jenkins JMeter can be installed and used by the plugin.

## Installing JMeter

Since Jenkins is running in a docker container we need to preserve the JMeter files between restarts, so we extended the `docker-compose.yml` file to include the path `/usr/local/bin/jmeter` and map it to a path on the server running Jenkins.

After downloading JMeter using 

```bash
curl https://dlcdn.apache.org//jmeter/binaries/apache-jmeter-5.6.3.tgz --output apache-jmeter-5.6.3.tgz
```

we can extract the contents of the file with tar and move it to the desired location.

```bash
tar -xvzf apache-jmeter-5.6.3.tgz
mv apache-jmeter-5.6.3 jmeter
mv jmeter /usr/local/bin
```


## Usage

JMeter can now be accessed by running `/usr/local/bin/jmeter/bin/jmeter -n -t <jmx-file> -l <jtl-report-save-location>`.

To get the performance report we need to reference the JTL report in the post-build actions as a souce data file.
