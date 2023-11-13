#!/bin/bash

mvn clean
rm -f grepper1.output
cp comb_1/*.py .
python3 comb_1.py
grep -e "FAILURE" -e "ERROR" out.*.txt > grepper1.output
mv out.*.txt grepper1.output comb_1
rm -f *.py

mvn clean
rm -f grepper2.output
cp comb_2/*.py .
python3 comb_2.py
grep -e "FAILURE" -e "ERROR" out.*.txt > grepper2.output
mv out.*.txt grepper2.output comb_2
rm -f *.py

exit 0
