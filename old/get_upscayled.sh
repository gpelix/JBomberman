#!/usr/bin/env fish

set DEST images/

rm -rf upscayled/*.png
cp -r original/*.png upscayled/

pushd upscayled/
python3 upscayl.py
mv GreenBackground_upscalyled.png ../"$DEST"/GreenBackground.png
popd

pushd images
echo (pwd)
for i in ../upscayled/*upscalyled.png
	convert $i -fuzz 15% -fill white -transparent green (string replace "../upscayled/" "" (string replace "_upscalyled" "" $i)) &
end
wait
for i in BomberMan*.png
	# Turn yellow eyes back to green
	#                           green             yellow
	begin
		convert $i -fuzz 10% -fill '#378700' -opaque '#fff000' $i.1 && mv $i.1 $i
	end &
end
wait
popd
