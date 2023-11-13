#!/bin/bash

rm -f *.output
python3 comb_1.py
grep -e "FAILURE" -e "ERROR" out.*.txt > grepper.output

exit 0
