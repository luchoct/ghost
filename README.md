#Ghost
##Functional Specification: Optimal Ghost
In the game of Ghost, two players take turns building up an English word from left to right. Each player adds one letter per turn. The goal is to not complete the spelling of a word: if you add a letter that completes a word, or if you add a letter that produces a string that cannot be extended into a word, you lose. (Bluffing plays and "challenges" may be ignored for the purpose of this puzzle.)

This is a program that allows a user to play Ghost against the computer.

* The computer play optimally given a dictionary. 
* Allow the human to play first. 
* If the computer thinks it will win, it should play randomly among all its winning moves; if the computer thinks it will lose, it should play so as to extend the game as long as possible (choosing randomly among choices that force the maximal game length).

##Technical Specification
Java web application that provides a basic GUI for a human to play against the optimal computer player from inside a web browser. The web page makes use of AJAX to update the page as the game progresses. The web framework is JSF.

Original requirements were packaging in a WAR file to run against Tomcat 5.5.x on Sun's J2SE 6.0. 

As an optional extra you can attempt to answer this question: if the human starts the game with 'n', and the computer plays according to the strategy above, what unique word will complete the human's victory?

#Changelog
* 2013-04-27 Version 1.0 Initial Release

#Areas of improvement
This software can be optimized in several ways:
* Lazy load of the dictionary: the dictionary is loaded after first input from the user, so it contains only the words that start with the input of the user.
  * The software uses 27 times less memory (the charset).
  * There are not caveats in performance.
*There is not dictionary: Each time the player input the new character, the computer looks for the words in the file, and creates an structure suitable.
  * The use of memory is minimum.
  * The performance is reduced because the I/O operations in the file.


