## Description

* Works fine with Spring Boot 2.1.3.RELEASE, modules are registered and (de)serialization works well
* With Spring Boot 2.1.4.RELEASE, modules are not registered anymore and test fails

## To reproduce

`./gradlew test`  -> expected to fail  
then downgradle Spring Boot version to 2.1.3.RELEASE  
then `./gradlew test` again -> expected to succeed