#!/usr/bin/env fish

ffmpeg -i Bomberman\ SFX\ vertical_original.wav -ss 00:00:00.00 -to 00:00:00.35 -filter:a "volume=0.5" Bomberman\ SFX\ vertical.wav
ffmpeg -i Bomberman\ SFX\ horizontal_original.wav -ss 00:00:00.00 -to 00:00:00.35 -filter:a "volume=0.5" Bomberman\ SFX\ horizontal.wav
