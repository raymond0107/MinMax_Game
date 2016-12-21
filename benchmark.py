#!/usr/bin/env python
# coding=utf-8

import os

os.system('mkdir -p results')

for i in xrange(99):
    os.system('cp ./testcase/input{0}.txt ./input.txt'.format(i))
    print("-->On test case #{0}<--".format(i))
    os.system('java homework > /dev/null')
    os.system('diff ./output.txt ./testcase/output{0}.txt'.format(i))

os.system('rm input.txt output.txt')
