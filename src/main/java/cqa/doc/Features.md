## Features

**1. String Similarity Features**
The String Similarity Features include various String Similarity metrics that find the string match between question and comment.
	* n-gram distance (n = 1,2,3)
	* cosine similarity (n = 1,2,3)
	* Jaccard similarity (n = 1,2,3)
	* QGram distance (n = 1,2,3)
	* Sorensen similarity (n = 1,2,3)
	* JaroWinkler similarity
	* Damerau distance
	* Levenshtein distance
	* Normalized Levenshtein distance
	* Longest Common Subsequence

**2. Dialogue and MetaData Features**
This set of features finds dialogue chains among users, and computes various metadata features like 
	* Position of comment in the thread
	* if a comment by the asker is an acknowledgement
	* number of words and characters in the comment and more

**3. Thread Level Features**
This set of features checks if an answer contains URLs, emails or HTML tags, if the comment has words denoting a particular class  
and features pertaining to intercomment dependence

**Word Embedding Features**
We train word embeddings of dimension 100 using [Word2Vec](http://deeplearning4j.org/word2vec) on unannotated data. From these we compute sentence vectors and various distance metrics between question and comment.

**Topic features**
We trained an LDA Topic Model using [Mallet](http://mallet.cs.umass.edu/topics.php) and find topic distributions in training and test data. From these topic vectors and words, we obtain various features. *This feature set is yet to be included in the tool*

## Evaluation
We combine all feature files, normalize it and feed it to a Support Vector Machine for binary classification. We use the SVM probability scores for ranking purposes.
The scoring measure used is Mean Average Precision (MAP). 