#!/usr/bin/env python
# -*- coding: utf-8 -*-

import HTMLParser
import math
import sys
from utils import get_char_count
from utils import get_words
from utils import get_sentences
from utils import count_syllables
from utils import count_complex_words

def bin_class(s):
	if s == 'Good':
		return 1
	else:
		return 0

class Readability:
    analyzedVars = {}

    def __init__(self, text):
        self.analyze_text(text)

    def analyze_text(self, text):
        words = get_words(text)
        char_count = get_char_count(words)
        word_count = len(words)
        sentence_count = len(get_sentences(text))
        syllable_count = count_syllables(words)
        complexwords_count = count_complex_words(text)
        avg_words_p_sentence = word_count/sentence_count
        
        self.analyzedVars = {
            'words': words,
            'char_cnt': float(char_count),
            'word_cnt': float(word_count),
            'sentence_cnt': float(sentence_count),
            'syllable_cnt': float(syllable_count),
            'complex_word_cnt': float(complexwords_count),
            'avg_words_p_sentence': float(avg_words_p_sentence)
        }

    def ARI(self):
        score = 0.0 
        if self.analyzedVars['word_cnt'] > 0.0:
            score = 4.71 * (self.analyzedVars['char_cnt'] / self.analyzedVars['word_cnt']) + 0.5 * (self.analyzedVars['word_cnt'] / self.analyzedVars['sentence_cnt']) - 21.43
        return score
        
    def FleschReadingEase(self):
        score = 0.0 
        if self.analyzedVars['word_cnt'] > 0.0:
            score = 206.835 - (1.015 * (self.analyzedVars['avg_words_p_sentence'])) - (84.6 * (self.analyzedVars['syllable_cnt']/ self.analyzedVars['word_cnt']))
        return round(score, 4)
        
    def FleschKincaidGradeLevel(self):
        score = 0.0 
        if self.analyzedVars['word_cnt'] > 0.0:
            score = 0.39 * (self.analyzedVars['avg_words_p_sentence']) + 11.8 * (self.analyzedVars['syllable_cnt']/ self.analyzedVars['word_cnt']) - 15.59
        return round(score, 4)
        
    def GunningFogIndex(self):
        score = 0.0 
        if self.analyzedVars['word_cnt'] > 0.0:
            score = 0.4 * ((self.analyzedVars['avg_words_p_sentence']) + (100 * (self.analyzedVars['complex_word_cnt']/self.analyzedVars['word_cnt'])))
        return round(score, 4)

    def SMOGIndex(self):
        score = 0.0 
        if self.analyzedVars['word_cnt'] > 0.0:
            score = (math.sqrt(self.analyzedVars['complex_word_cnt']*(30/self.analyzedVars['sentence_cnt'])) + 3)
        return score

    def ColemanLiauIndex(self):
        score = 0.0 
        if self.analyzedVars['word_cnt'] > 0.0:
            score = (5.89*(self.analyzedVars['char_cnt']/self.analyzedVars['word_cnt']))-(30*(self.analyzedVars['sentence_cnt']/self.analyzedVars['word_cnt']))-15.8
        return round(score, 4)

    def LIX(self):
        longwords = 0.0
        score = 0.0 
        if self.analyzedVars['word_cnt'] > 0.0:
            for word in self.analyzedVars['words']:
                if len(word) >= 7:
                    longwords += 1.0
            score = self.analyzedVars['word_cnt'] / self.analyzedVars['sentence_cnt'] + float(100 * longwords) / self.analyzedVars['word_cnt']
        return score

    def RIX(self):
        longwords = 0.0
        score = 0.0 
        if self.analyzedVars['word_cnt'] > 0.0:
            for word in self.analyzedVars['words']:
                if len(word) >= 7:
                    longwords += 1.0
            score = longwords / self.analyzedVars['sentence_cnt']
        return score
        

if __name__ == "__main__":        
    f = open('S_read_dev.txt','w')
    with open('/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/CQA_Updated/data/Data_format_files/Parsed_files/Auth/Original/dev.txt','r') as reader:
        while 1:
            q_id = reader.readline()
            if not q_id:
                break
            words = q_id.split()
            num = int(words[1])
            question = reader.readline()
            for i in xrange(num):
                s = reader.readline()
                words = s.split()
                label = words[1]
                comment = reader.readline()
                rd = Readability(comment)
                f.write(str(bin_class(label))+' 1:'+str(rd.ARI())+' 2:'+str(rd.FleschReadingEase())+' 3:'+str(rd.GunningFogIndex())+' 4:'+str(rd.SMOGIndex())+' 5:'+str(rd.ColemanLiauIndex())+' 6:'+str(rd.LIX())+' 7:'+str(rd.RIX())+'\n')
    f.close()

    
