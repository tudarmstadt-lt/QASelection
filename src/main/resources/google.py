import gensim
import logging
import gensim.models
import string
import sys
import numpy as np
import os

def calculate(inp, model):
	replace_punctuation = string.maketrans(string.punctuation, ' '*len(string.punctuation))
	out = inp.translate(replace_punctuation)
	do = out.lower().split()
	size = model['man'].size
	final = np.zeros(size)
	count = 0
	for word in do:
		if(word in model):
			final = final + model[word]
			count = count + 1
		#else:
			#print word
	if(count != 0):
		final = final/count
	return final


if __name__ == '__main__':
	model = gensim.models.Word2Vec.load_word2vec_format('GoogleNews-vectors-negative300.bin.gz', binary=True)
	# inp = argv
	f = open('train.txt', 'w')
	f2 = open('test.txt', 'w')
	with open('parsed_clean.txt', 'r') as reader:
		while 1:
			q_id = reader.readline()
			if not q_id:
				break
			q_id = q_id.split()[0]
			question = reader.readline()
			vec = calculate(question, model)
			s = q_id
			for i  in xrange(vec.size):
				s = s+' '+str(vec[i])
			f.write(s+'\n')
			for i in xrange(10):
				s = reader.readline().split()
				c_id = s[0]
				label = s[1]
				comment = reader.readline()
				#print comment
				vec = calculate(comment, model)
				s = c_id+' '+label
				for j in xrange(vec.size):
					s = s+' '+str(vec[j])
				f.write(s+'\n')
	with open('parsed_clean2.txt', 'r') as reader:
		while 1:
			q_id = reader.readline()
			if not q_id:
				break
			q_id = q_id.split()[0]
			question = reader.readline()
			vec = calculate(question, model)
			s = q_id
			for i  in xrange(vec.size):
				s = s+' '+str(vec[i])
			f2.write(s+'\n')
			for i in xrange(10):
				s = reader.readline().split()
				c_id = s[0]
				label = s[1]
				comment = reader.readline()
				#print comment
				vec = calculate(comment, model)
				s = c_id+' '+label
				for j in xrange(vec.size):
					s = s+' '+str(vec[j])
				f2.write(s+'\n')


