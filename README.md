# Autocorrect
#### A word-suggestion project created by Landon Moceri
## The Project
This project takes word input from the terminal and suggests words close to it using the Levenshtein edit distance.
It uses a TST to manage the dictionary, allowing for fast traversals proportional to the length of the mistyped word.
It also takes advantage of early pruning logic to avoid checking any redundant path.