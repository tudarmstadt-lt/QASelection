import HTMLParser
import re
import itertools
import sys
from nltk.corpus import stopwords

cachedStopWords = stopwords.words("english")
slang_dict = {"lolz": "laughing out loud", "lol": "laugh out loud", "u": "you", "pls": "please", "plz": "please", "opp": "opposite", "stn": "station", "r": "are", "n": "and", "hmm": "", "p" : "", "dr" : "doctor", "asap": "as soon as possible", "approx": "approximately", "thanx": "thankyou", "tnx": "thankyou", "rofl": "rolling out on floor laughing", "lmao": "laughing my ass off", "aka" : "also known as", "fyi": "for your information", "btw": "by the way", "idk": "i do not know", "omg": "oh my god", "gr8": "great", "2" : "to", "coz": "because"}
contractions_dict = { "ain't": "are not","aren't": "are not","can't": "cannot","can't've": "cannot have","'cause": "because","could've": "could have","couldn't": "could not","couldn't've": "could not have","didn't": "did not","doesn't": "does not","don't": "do not","hadn't": "had not","hadn't've": "had not have","hasn't": "has not","haven't": "have not","he'd": "he had","he'd've": "he would have","he'll": "he will","he'll've": "he will have","he's": "he is","how'd": "how did","how'd'y": "how do you","how'll": "how will","how's": "how is","i'd": "i would","i'd've": "i would have","i'll": "i will","i'll've": "i will have","i'm": "i am","i've": "i have","isn't": "is not","it'd": "it would","it'd've": "it would have","it'll": "it will","it'll've": "it will have","it's": "it is","let's": "let us","ma'am": "madam","mayn't": "may not","might've": "might have","mightn't": "might not","mightn't've": "might not have","must've": "must have","mustn't": "must not","mustn't've": "must not have","needn't": "need not","needn't've": "need not have","o'clock": "of the clock","oughtn't": "ought not","oughtn't've": "ought not have","shan't": "shall not","sha'n't": "shall not","shan't've": "shall not have","she'd": "she would","she'd've": "she would have","she'll": "she will","she'll've": "she will have","she's": "she is","should've": "should have","shouldn't": "should not","shouldn't've": "should not have","so've": "so have","so's": "so is","that'd": "that had","that'd've": "that would have","that's": "that is","there'd": "there would","there'd've": "there would have","there's": "there is","they'd": "they would","they'd've": "they would have","they'll": "they will","they'll've": "they will have","they're": "they are","they've": "they have","to've": "to have","wasn't": "was not","we'd": "we would","we'd've": "we would have","we'll": "we will","we'll've": "we will have","we're": "we are","we've": "we have","weren't": "were not","what'll": "what will","what'll've": "what will have","what're": "what are","what's": "what is","what've": "what have","when's": "when is","when've": "when have","where'd": "where did","where's": "where is","where've": "where have","who'll": "who will","who'll've": "who will have","who's": "who is","who've": "who have","why's": "why is","why've": "why have","will've": "will have","won't": "will not","won't've": "will not have","would've": "would have","wouldn't": "would not","wouldn't've": "would not have","y'all": "you all","y'all'd": "you all would","y'all'd've": "you all would have","y'all're": "you all are","y'all've": "you all have","you'd": "you would","you'd've": "you would have","you'll": "you will","you'll've": "you will have","you're": "you are","you've": "you have"}
contractions_re = re.compile('(%s)' % '|'.join(contractions_dict.keys()))
punctuations = '''()-[]{};:"\,.?!<>/#$%^&*@_~|'''

def testFuncNew(line):
    result = ' '.join([word for word in line.split() if word not in cachedStopWords])
    return result

def expand_contractions(s, contractions_dict=contractions_dict):
    def replace(match):
        return contractions_dict[match.group(0)]
    return contractions_re.sub(replace, s)

def slang_remover(line):
	words = line.split()
	words = [slang_dict[word] if word in slang_dict else word for word in words]
	string = " ".join(words)

	return string

def special(line):
	words = line.split()
	for word in words:
		if 'img' in word:
			words.remove(word)
	for word in words:
		if 'desc' in word:
			words.remove(word)
	string = " ".join(words)
	return string

def URL_remover(line):
	result = re.sub(r"http\S+", "", line)
	return result
def email_remover(line):
	result = re.sub(r'[\w\.-]+@[\w\.-]+',"", line)
	return result

def punc_remover(line):
	no_punct = ""
	for char in line:
	   	if char not in punctuations:
	   		no_punct = no_punct + char
	   	else:
	   		no_punct = no_punct + ' '
	string = re.sub(' +',' ',no_punct)
	return string
def tag_remover(line):
        string = re.sub('<[^>]*>', '', line)
        return string
        
def preprocessor(line):
	reload(sys)  
	sys.setdefaultencoding('utf8')
	html_parser = HTMLParser.HTMLParser()
	string = html_parser.unescape(line)
	string = URL_remover(string)
	string = email_remover(string)
	string = tag_remover(string)
	string = special(string)
	string = punc_remover(string)
	words = string.lower().split()
	reformed = [expand_contractions(word) if word in contractions_dict else word for word in words]
	string = " ".join(reformed)
	string = ''.join(''.join(s)[:2] for _, s in itertools.groupby(string))
	string = slang_remover(string)
	string = testFuncNew(string)
	string = re.sub('\'', '', string)
	string = re.sub(' +',' ',string)

	return string
if __name__ == '__main__':
	f = open('train.txt', 'w')
	with open('/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/CQA_Updated/data/Data_format_files/Parsed_files/Auth/Original/train.txt', 'r') as reader:
		while 1:
			q_id = reader.readline()
			if not q_id:
				break
			words = q_id.split()
			num = int(words[1])
			f.write(q_id)
			question = reader.readline()
			f.write(preprocessor(question)+'\n')
			for i in xrange(num):
				s = reader.readline()
				f.write(s)
				comment = reader.readline()
				f.write(preprocessor(comment)+'\n')

	
