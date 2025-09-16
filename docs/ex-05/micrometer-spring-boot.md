# Exposing metrics with MicroMeter using Spring Boot

## Dependencies

We need to add more dependencies to our pom.xml file in order to work with MicroMeter and Prometheus:

> important: do not add the 'io.micrometer' and the 'io.prometheus' dependency to the parent pom.xml file, as it will not work.

```code
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

## Spring Boot Actuator

Spring Boot already ships lots of monitoring functionality through its library "spring-boot-actuator", see https://docs.spring.io/spring-boot/reference/actuator/enabling.html.

Adding the dependency is enough to enable a new endpoint which serves a rich selection of data. To enable all of the endpoints (which is _not_ safe for production but cool just to play around with) we need to set the following settings in the [application.properties](/backend/src/main/resources/application.properties) file.

```code
# Actuator Settings
management.endpoint.health.show-details=always
management.endpoints.enabled-by-default=true
management.metrics.export.prometheus.enabled=true
management.endpoints.web.base-path=/actuator
management.endpoints.web.exposure.include=*
```

This now enables us to scrape health metrics from our app in a Prometheus format and to display it on a Grafana dashboard.

## Enriching the existing metrics with our own

We have implemented two counters and one gauge to collect the following application data:

- Authentication counter: We want to be able to count the number of log ins that have occurred over time so we quickly notice any anomalies.
- Brewing process creation counter: We want to be able to count how many brewing processes have been started over time.
- Ingredients below threshold gauge: We want to easily see how many ingredients have fallen below their defined threshold amount so that we can restock in time.

### Counter

To add a counter, we need to go to the class where the function call occurs, the [BrewingProcessController](/backend/src/main/java/ch/schuum/backend/controller/BrewingProcessController.java) in this case.

We add a new variable:

```code
private Counter brewingProcessCounter;
```

... initialize it within the constructor:

```code
public BrewingProcessController(BrewingProcessService brewingProcessService, MeterRegistry meterRegistry) {
    // counter
    this.brewingProcessCounter = Counter.builder("brewing_process_creation_request_total").
            tag("version", "v1").
            description("Brewing Process Creation Count").
            register(meterRegistry);
}
```

... and then lastly call the increment function every time the function is called:

```code
@PostMapping("/brewingprocess")
BrewingProcessDto saveBrewingProcess(@RequestBody BrewingProcessDto brewingProcess) {
    brewingProcessCounter.increment();
    return brewingProcessService.createBrewingProcess(brewingProcess);
}
```

This specific metric is served under the URL APP_URL/api/actuator/metrics/brewing_process_creation_request_total. The authentication counter is reachable under APP_URL/api/actuator/metrics/auth_request_total.

### Gauge

The gauge is very similar to the counter. Because the metric that we wanted to extract would not make sense to only count whenever the function is called, we had to implement a cronjob that regularly updates the metric (every 60 seconds). Unlike the counter, the value is not incremented, but set anew every time.

```code
@Scheduled(fixedRate = 60000)
public void measureIngredientsBelowThreshold() {
    List<IngredientDto> ingredientsBelowThreshold = getAllIngredientsBelowThreshold();
    ingredientsBelowThresholdGauge.set(ingredientsBelowThreshold.size());
}
```

The gauge is of the type `AtomicInteger`:

```code
private AtomicInteger ingredientsBelowThresholdGauge;
```

... and it's initialized within the constructor with:

```code
this.ingredientsBelowThresholdGauge = meterRegistry.gauge("ingredients_below_threshold_per_minute", new AtomicInteger(0));
```

This metric is served under the URL APP_URL/api/actuator/metrics/ingredients_below_threshold_per_minute.
