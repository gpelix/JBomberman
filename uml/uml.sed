#!/usr/bin/sed -f

# extends/implements arrows fix
s/endArrow=block/endArrow=block;endFill=0/g
s/strokeColor=#000082/strokeColor=#000000/g
s/strokeColor=#008200/strokeColor=#000000/g

# Fix composition diamond
s/diamondThinstartSize=12/diamondThin;startSize=14;startFill=0/g

# I hate the red color of enums
s/strokeColor=#820000/strokeColor=#000000/g
