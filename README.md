# Delta Reporter TestNG Maven Plugin #

This plugin provides a custom listener and method interceptors to send data from your tests into Delta Reporter

It can handle the next events:

- onTestRunStarted
- onTestContextStart
- onTestStart
- onTestSuccess
- onTestFailure
- onTestSkipped
- onTestContextFinish
- onTestSuiteContextFinish
- onTestHook

## Integration

### Add Maven dependency
```
<dependency>
    <groupId>com.deltareporter</groupId>
    <artifactId>delta-reporter-testng-maven-plugin</artifactId>
    <version>latest</version>
</dependency>
```

### Create a delta.properties file and place it at src/main/resources
```
delta_enabled=true
delta_service_url=http://localhost:5001
delta_test_type=Integration Tests
delta_project=Delta Reporter Demo
```

### Add DeltaListener into your tests (there are two options)
#### 1. Include using surefire plugin
```
<plugins>
    [...]
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>2.22.1</version>
        <configuration>
          <properties>
            <property>
              <name>listener</name>
              <value>com.deltareporter.listener.DeltaListener</value>
            </property>
          </properties>
        </configuration>
      </plugin>
    [...]
</plugins>
```
#### 2. Include using TestNG xml
```
<suite>
    [...]
      <listeners>
        <listener class-name="com.deltareporter.listener.DeltaListener"/>
      </listeners>
    [...]
</suite>
```

#### Run sample test in this repo 

To run the sample test from this repo, run the following command: 

`mvn clean test -Dsurefire.suiteXmlFiles=suites/TestSuite1.xml`