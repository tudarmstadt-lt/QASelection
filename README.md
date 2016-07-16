# QARank - Answer Selection and Ranking Tool for Community Question Answering sites
QARank is licensed under ASL 2.0 and other lenient licenses, allowing its use for academic and commercial purposes without restrictions.

## Download and Run QARank
* Download the jar file of the project from here.
* QARank requires a training file, a test file and unannotated data to train models.
* The system was trained and tested on SemEval 2016 - Task 3: Community Question Answering data.
* The training+dev data can be downloaded from [here](http://alt.qcri.org/semeval2016/task3/data/uploads/semeval2016-task3-cqa-ql-traindev-v3.2.zip).
* The unannotated data can be downloaded from [here](http://alt.qcri.org/semeval2016/task3/data/uploads/QL-unannotated-data-subtaskA.xml.zip).
* Download the train, test and unannotated xml files in one place.
* Run QARank jar as
```
java -Xmx10g -jar QARank.jar [train-file-path] [test-file-path] [unannotated-file-path]
```
* The system will generate all folders and required files.
* The final MAP scores of the system and the SVM accuracy can be found in **result_files/final_scores.txt** file.
* Users can run the system on a different dataset, given the training and test files are in the format as in SemEval 2016 - Task 3.  
* The evaluation scripts used in the system can be looked up [here](http://alt.qcri.org/semeval2016/task3/data/uploads/semeval2016_task3_submissions_and_score.zip).
