#!/usr/bin/env fish

set ORIGINAL_DIRECTORY original/
set FINAL_DIRECTORY final/
set SED_FILE uml.sed

[ -d $FINAL_DIRECTORY ] || mkdir $FINAL_DIRECTORY
cp $ORIGINAL_DIRECTORY/*.drawio $FINAL_DIRECTORY/
sed -f $SED_FILE -i $FINAL_DIRECTORY/*.drawio
for i in $FINAL_DIRECTORY/*.drawio
	drawio -x $i -o (string replace ".drawio" ".pdf" $i)
	rm $i
end

# vim: set ts=4 sw=4 tw=0 noet :
