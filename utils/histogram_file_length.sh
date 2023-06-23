#!/usr/bin/env fish

wc (find .. -iname '*.java') | sort -n | sed '$ d' | awk '{ print $1 }' | python3 plotHistogram.py
