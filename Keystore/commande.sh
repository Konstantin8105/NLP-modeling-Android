 #!/bin/bash
echo "Hello, World"

rm ./mdpi/*
rm ./hdpi/*
rm ./xhdpi/*
rm ./xxhdpi/*
rm ./xxxhdpi/*
rm ./picture/*
rm ./title/*



list_file=$(ls *ic_launcher*.svg)
echo $list_file
for f in $list_file; do inkscape $f --export-height=48   --export-png="./mdpi/$f.png"; done
for f in $list_file; do inkscape $f --export-height=72   --export-png="./hdpi/$f.png"; done
for f in $list_file; do inkscape $f --export-height=96   --export-png="./xhdpi/$f.png"; done
for f in $list_file; do inkscape $f --export-height=144  --export-png="./xxhdpi/$f.png"; done
for f in $list_file; do inkscape $f --export-height=216  --export-png="./xxxhdpi/$f.png"; done


list_file=$(ls icon_*.svg)
echo $list_file
for f in $list_file; do inkscape $f --export-height=32   --export-png="./mdpi/$f.png"; done
for f in $list_file; do inkscape $f --export-height=48   --export-png="./hdpi/$f.png"; done
for f in $list_file; do inkscape $f --export-height=72   --export-png="./xhdpi/$f.png"; done
for f in $list_file; do inkscape $f --export-height=96   --export-png="./xxhdpi/$f.png"; done
for f in $list_file; do inkscape $f --export-height=144  --export-png="./xxxhdpi/$f.png"; done

list_file=$(ls *status_bar*.svg drw_eye*.svg ic_navigation*.svg)
echo $list_file
for f in $list_file; do inkscape $f --export-height=32   --export-png="./mdpi/$f.png"; done
for f in $list_file; do inkscape $f --export-height=48   --export-png="./hdpi/$f.png"; done
for f in $list_file; do inkscape $f --export-height=72   --export-png="./xhdpi/$f.png"; done
for f in $list_file; do inkscape $f --export-height=96   --export-png="./xxhdpi/$f.png"; done
for f in $list_file; do inkscape $f --export-height=144  --export-png="./xxxhdpi/$f.png"; done

list_file=$(ls *picture*.svg)
echo $list_file
for f in $list_file; do inkscape $f --export-height=200   --export-png="./picture/$f.png"; done

inkscape title.svg      --export-height=512  --export-png="./title/title.png"
inkscape title_big.svg  --export-height=500  --export-png="./title/title_big.png"
inkscape title_spam.svg --export-height=500  --export-png="./title/title_spam.png"

cd ./mdpi/
rename -v 's/\.svg.png/\.png/' *.svg.png
mv * ../../res/drawable-mdpi/
cd ..
cd ./hdpi/
rename -v 's/\.svg.png/\.png/' *.svg.png
mv * ../../res/drawable-hdpi/
cd ..
cd ./xhdpi/
rename -v 's/\.svg.png/\.png/' *.svg.png
mv * ../../res/drawable-xhdpi/
cd ..
cd ./xxhdpi/
rename -v 's/\.svg.png/\.png/' *.svg.png
mv * ../../res/drawable-xxhdpi/
cd ..
cd ./xxxhdpi/
rename -v 's/\.svg.png/\.png/' *.svg.png
mv * ../../res/drawable-xxxhdpi/
cd ..
cd ./picture/
rename -v 's/\.svg.png/\.png/' *.svg.png
mv * ../../res/drawable/





