#!/usr/bin/python
import os
import sys, getopt
import re
import csv

__author__ = 'Mohit Kumar'

'''
    This python script extracts Test Case info from the comments written in
    android java test case files and put them into nice csv format.
'''


def parse_file(inputFile):

    matchObj = re.match('(.*)Test\.java', inputFile, re.IGNORECASE)
    if matchObj:
        outputFileName = matchObj.group(1) + "Tests.csv"

    f = open(outputFileName, 'w')
    writer = csv.writer(f, lineterminator='\n')

    with open(inputFile, 'r') as fh:
        testsData = fh.read()

    # Finding the pattern
    matchObjs = re.finditer('\/\*\s+(TESTCASE)\s+(\d+)\s*\n\s*Steps\:\s*\n\s*(.*)\n\s*(.*)\n\s*Result\:\s*\n\s*(.*)\n\s*Status\:\s*\n\s*(.*)\n\s*\*\/', testsData, re.IGNORECASE|re.MULTILINE)

    # Writing data to csv file
    for matchObj in matchObjs:
        row = []
        for i in range(1,7):
            row.append(matchObj.group(i))
        writer.writerow(row)

    f.close()

def parse_all_files(inputDir):
    pass


def main(argv):
    inputDir = ""
    inputFile = ""
    try:
        opts, args = getopt.getopt(argv, "hi:d:", [])
    except getopt.GetoptError:
        print "generateTestDoc.py -i <input file> -d <input directory>"
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            print 'usage: generateTestDoc.py -i <input file> -d <input directory>\n' \
                  '<input file> = * for all files in a directory'
            sys.exit()
        elif opt == '-i':
            inputFile = arg
        elif opt == '-d':
            inputDir = arg

    # Validating arguments
    if inputFile == '' and inputDir == '':
        print 'usage: generateTestDoc.py -i <input file> -d <input directory>\n' \
                  '<input file> = * for all files in a directory'
        sys.exit(2)
    if inputFile == '' and inputDir != '':
        if os.path.isdir(inputDir):
            inputFile = "*"
            parse_all_files(inputDir)
        else:
            print "Error: " + inputDir + " does not exists, Please check your arguments."
            sys.exit(2)
    if inputFile!='' and inputDir == '':
        if not os.path.isfile(inputFile):
            print "Error: " + inputFile + " does not exists, Please check your arguments."
            sys.exit(2)
        else:
            parse_file(inputFile)

if __name__ == "__main__":
    main(sys.argv[1:])





