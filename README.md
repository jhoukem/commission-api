# Malt's Hiring Exercise

Welcome to Malt's hiring exercise for back-end developers!

This project is provided as a shell aiming to help you focus on the most interesting parts of the
exercise, without losing time properly setting up a Spring application. That being said, feel free
to start from scratch if you prefer to. Just keep in mind that we do want the exercise to be made
with Spring Boot.

Other than that you may:

- use either Java or Kotlin, whichever you prefer (the project is configured for both languages) 
- make it fully-reactive or stick to a good old threaded implementation (the later being pre-configured)
- skip setting up a persistence mechanism. Really, we don't care about it for this exercise so don't
  lose your time and use something like the provided InMemoryStore class.

## How to get started

The first dependency to install is Java, which you can install as follows, using
[SDKMAN](https://sdkman.io/):

```sh
$ curl -s "https://get.sdkman.io" | bash
$ sdk env
  # ... and follow printed instructions, which should look like:
  # sdk install java SOME_VERSION
```

The usual warning: don't blindly execute script provided that way on the the Internet. So don't
take our word for it and check that https://get.sdkman.io is indeed a harmless script ;-)

You may also download and install Java manually from https://adoptium.net. The required version
is specified in [.sdkmanrc](.sdkmanrc).

Now you can execute the following to compile and test the project (it will download Gradle first)
:
```sh
$ ./gradlew build
```

And you can start the application with:
```sh
./$ gradlew bootRun
```

Once started, an URL should be displayed for you to hit the server:
```
...
2022-02-22 14:37:43.523  INFO 134091 --- [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2022-02-22 14:37:43.529  INFO 134091 --- [  restartedMain] com.malt.hiringexercise.ApplicationKt    : Started ApplicationKt in 0.973 seconds (JVM running for 1.301)

Everything's fine! Open this link to have the server return something ;-)
http://localhost:8080

```
