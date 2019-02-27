# Ghost
## Functional Specification: Optimal Ghost
In the game of Ghost, two players take turns building up an English word from left to right. Each player adds one letter per turn. The goal is to not complete the spelling of a word: if you add a letter that completes a word, or if you add a letter that produces a string that cannot be extended into a word, you lose. (Bluffing plays and "challenges" may be ignored for the purpose of this puzzle.)

This is a program that allows a user to play Ghost against the computer.

* The computer play optimally given a dictionary. 
* Allow the human to play first. 
* If the computer thinks it will win, it should play randomly among all its winning moves; if the computer thinks it will lose, it should play so as to extend the game as long as possible (choosing randomly among choices that force the maximal game length).

## Technical Specification
Java web application that provides a basic GUI for a human to play against the optimal computer player from inside a web browser. The web page makes use of AJAX to update the page as the game progresses. The web framework is JSF.

## Run

### Run locally
```
mvn package
java -jar target/dependency/jetty-runner.jar --port 8080 --path /ghost --config src/main/resources/jetty-realm.xml target/ghost.war
```
### Run on Heroku
```
https://optimal-ghost.herokuapp.com/ghost/
```
### Run on Docker
#### Build the image locally
In Windows, run Docker QuickStart Terminal.
On the root folder (where the Dockerfile is placed), run:
```
docker build . -t ghost:1.0.0
```
#### Run the image
```
docker run --rm -p 8081:8080 ghost:1.0.0
```
#### Run on Docker
```
https://localhost:8081/
```

# Changelog
* 2012-11-27 Version 1.0 Initial skeleton
* 2012-12-27 Version 1.1 Added site report
* 2012-12-01 Version 1.2 Initial Release
* 2012-12-01 Version 1.3 Lombok, java 8 and bugfixing.
* 2019-02-26 Version 2.0 Upgraded some libs, dockerized app and moved to java 11.

# Areas of improvement
This software can be optimized in several ways:
* Lazy load of the dictionary: the dictionary is loaded after first input from the user, so it contains only the words that start with the input of the user.
  * The software uses 27 times less memory (the charset).
  * There are not caveats in performance.
* There is not dictionary: Each time the player input the new character, the computer looks for the words in the file, and creates an structure suitable.
  * The use of memory is minimum.
  * The performance is reduced because the I/O operations in the file.


