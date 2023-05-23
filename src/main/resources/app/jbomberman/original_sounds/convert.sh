#!/usr/bin/env fish
for i in *.mp3;
	ffmpeg -i $i -acodec pcm_u8 -ar 22050 (string replace ".mp3" ".wav" $i)
end
