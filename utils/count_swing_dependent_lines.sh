#!/usr/bin/env fish

cd (status dirname)/../src
echo "SWING:"
grep -v '\*'  (grep -rn . -e '\(swing\|awt\)'  | cut -f 1 -d':' | uniq) | wc -l
echo "JAVAFX:"
grep -v '\*' (grep -rn . -e 'javafx'  | cut -f 1 -d':' | uniq) | wc -l
