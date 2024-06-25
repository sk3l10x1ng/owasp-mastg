#!/bin/bash

mkdir -p docs/MASTG
mkdir -p docs/MASTG/Intro      
mkdir -p docs/MASTG/General
mkdir -p docs/MASTG/Android
mkdir -p docs/MASTG/iOS
mkdir -p docs/MASTG/References
mkdir -p docs/MASWE

cp Document/0x0[1-6]*.md docs/MASTG
cp Document/0x09*.md docs/MASTG
cp -r tests docs/MASTG/
cp -r techniques docs/MASTG/
cp -r tools docs/MASTG/
cp -r apps docs/MASTG/
cp -r weaknesses/** docs/MASWE/

cp Document/tests.md docs/MASTG/tests/index.md
cp Document/0x08b-Reference-Apps.md docs/MASTG/apps/index.md
cp Document/0x08a-Testing-Tools.md docs/MASTG/tools/index.md
cp Document/techniques.md docs/MASTG/techniques/index.md

mv docs/MASTG/0x0[1-3]*.md docs/MASTG/Intro
mv docs/MASTG/0x04*.md docs/MASTG/General
mv docs/MASTG/0x05*.md docs/MASTG/Android
mv docs/MASTG/0x06*.md docs/MASTG/iOS
mv docs/MASTG/0x09*.md docs/MASTG/Intro

cp -r Document/Images/ docs/assets/Images/
find docs/MASTG/tests -name "*.md" -exec sed -i 's#<img src="Images/#<img src="../../../../../assets/Images/#g' {} \;
find docs/MASTG/techniques -name "*.md" -exec sed -i 's#<img src="Images/#<img src="../../../../../assets/Images/#g' {} \;
find docs/MASTG/tools -name "*.md" -exec sed -i 's#<img src="Images/#<img src="../../../../../assets/Images/#g' {} \;
find docs/MASTG/apps -name "*.md" -exec sed -i 's#<img src="Images/#<img src="../../../../../assets/Images/#g' {} \;
find docs/MASTG -name "*.md" -exec sed -i 's#<img src="Images/#<img src="../../../assets/Images/#g' {} \;

