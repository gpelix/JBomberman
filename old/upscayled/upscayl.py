import sys
import os

import PIL.Image

sys.path.insert(0, '/home/gp/src/python/colors')

import color

times = 2

for i in os.listdir():
    if os.path.isfile(i) and i.endswith('.png'):
        rgba = PIL.Image.open(i)
        rgb = rgba.convert('RGB')
        with open(i, "wb") as f:
            rgb.save(f)
        img = color.Image.from_file(i)
        img = img.scale(times)
        img.save_to_file(i.split('.')[0] + '_upscalyled.png')
        rgb = PIL.Image.open(i.split('.')[0] + '_upscalyled.png')
        rgba = rgb.convert('RGBA')
        with open(i.split('.')[0] + '_upscalyled.png', "wb") as f:
            rgba.save(f)
