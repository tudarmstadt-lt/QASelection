import sys
#method returns binary class
def bin_class(string):
	if string == 'Good':
		return 'true'
	else:
		return 'false'

if __name__ == '__main__':
	f = open(str(sys.argv[2]),'w')
	with open(str(sys.argv[1]), 'r') as reader:
		while 1:
			q_id = reader.readline()
			if not q_id:
				break
			words = q_id.split()
			num = int(words[1])
			question = reader.readline()
			for i in xrange(num):
				s = reader.readline()
				splited = s.split()
				f.write(words[0]+' '+splited[0]+' '+str(i+1)+' '+str(1.0/(i+1))+' '+bin_class(splited[1])+'\n')            #scorer script format
				comment = reader.readline()
	f.close()