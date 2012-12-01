This software can be optimized in several ways:
- Lazy load of the dictionary: the dictionary is loaded after first input from the 
			user, so it contains only the words that start with the input of the user.
			The software uses 27 times less memory (the charset).
			It doesn't have an adverse effect in performance.
- There is not dictionary: Each time the player input the new character, the 
			computer looks for the words in the file, and creates an structure suitable.
			The use of memory is minimum.
			The performance is reduced because the I/O operations in the file.
	
  