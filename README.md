GeMi Gatling Examples in JAVA [![Build Status](https://github.com/gemiusz/gatling-examples-maven-java/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/gemiusz/gatling-examples-maven-java/actions/workflows/maven.yml?query=branch%3Amaster)
============================================

Gatling project in JAVA 17 showing working examples and solutions - Inspired by [Gatling Community](https://community.gatling.io)
<br><br>

It includes:
* [Maven Wrapper](https://maven.apache.org/wrapper/), so that you can immediately run Maven with `./mvnw` without having
  to install it on your computer
* minimal `pom.xml`
* latest version of `io.gatling.highcharts:gatling-charts-highcharts`applied - [Maven Central Repository Search](https://search.maven.org/artifact/io.gatling.highcharts/gatling-charts-highcharts)
* latest version of `io.gatling:gatling-maven-plugin` applied - [Maven Central Repository Search](https://search.maven.org/artifact/io.gatling/gatling-maven-plugin)
* official examples: [ComputerDatabaseSimulation](src/test/java/computerdatabase/ComputerDatabaseSimulation.java), [BasicSimulation](src/test/java/computerdatabase/BasicSimulation.java), [AdvancedSimulationStep01](src/test/java/computerdatabase/advanced/AdvancedSimulationStep01.java), [AdvancedSimulationStep02](src/test/java/computerdatabase/advanced/AdvancedSimulationStep02.java), [AdvancedSimulationStep03](src/test/java/computerdatabase/advanced/AdvancedSimulationStep03.java), [AdvancedSimulationStep04](src/test/java/computerdatabase/advanced/AdvancedSimulationStep04.java), [AdvancedSimulationStep05](src/test/java/computerdatabase/advanced/AdvancedSimulationStep05.java)
* mine examples and solutions mostly based on cases from [Gatling Community](https://community.gatling.io)
<br><br><br>

### Mine examples and solutions divided into cases:
* [**Case0001JMESPathSimulation**](src/test/java/pl/gemiusz/Case0001JMESPathSimulation.java) => [JmesPath is not finding a JSON Object](https://community.gatling.io/t/jmespath-is-not-finding-a-json-object/6995)
* [**Case0002PDFdownloadSimulation**](src/test/java/pl/gemiusz/Case0002PDFdownloadSimulation.java) => [How to ensure a pdf is downloaded during a loadtest?](https://community.gatling.io/t/how-to-ensure-a-pdf-is-downloaded-during-a-loadtest/3927)
* [**Case0003UnzipJsonForFeederSimulation**](src/test/java/pl/gemiusz/Case0003UnzipJsonForFeederSimulation.java) => [Unzipping json file for feeders](https://community.gatling.io/t/unzipping-json-file-for-feeders/6996)
* [**Case0004StatusCodeSimulation**](src/test/java/pl/gemiusz/Case0004StatusCodeSimulation.java) => [withDefault Check Transforming feature](https://community.gatling.io/t/withdefault-check-transforming-feature/7008)
* [**Case0005UUIDfeederSimulation**](src/test/java/pl/gemiusz/Case0005UUIDfeederSimulation.java) => [Is there an EL function to generate uuid using java in gatling](https://community.gatling.io/t/is-there-an-el-function-to-generate-uuid-using-java-in-gatling/7028)
* [**Case0006CommandLineParametersSimulation**](src/test/java/pl/gemiusz/Case0006CommandLineParametersSimulation.java) => [Cannot Grab Command Line Arguments](https://community.gatling.io/t/cannot-grab-command-line-arguments/7025)
* [**Case0007AsyncReqSimulation**](src/test/java/pl/gemiusz/Case0007AsyncReqSimulation.java) - using `repeat` => [How to simulate an asynchronous request executing many times?](https://community.gatling.io/t/how-to-simulate-an-asynchronous-request-executing-many-times/7031)
* [**Case0008AsyncReqResourcesSimulation**](src/test/java/pl/gemiusz/Case0008AsyncReqResourcesSimulation.java) - using `resources` => [How to simulate an asynchronous request executing many times?](https://community.gatling.io/t/how-to-simulate-an-asynchronous-request-executing-many-times/7031)
* [**Case0009SessionValuesSimulation**](src/test/java/pl/gemiusz/Case0009SessionValuesSimulation.java) => [Dynamically generating param values for an API and setting it using session](https://community.gatling.io/t/dynamically-generating-param-values-for-an-api-and-setting-it-using-session/7041)
* [**Case0010JsonEditVariableSimulation**](src/test/java/pl/gemiusz/Case0010JsonEditVariableSimulation.java) => [Java - edit variable received in JSON](https://community.gatling.io/t/java-edit-variable-received-in-json/7046)
