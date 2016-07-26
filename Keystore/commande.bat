@echo off

del /F  /Q .\mdpi\*
del /F  /Q .\hdpi\*
del /F  /Q .\xhdpi\*
del /F  /Q .\xxhdpi\*
del /F  /Q .\title\*

FOR /f "tokens=*" %%G IN ('dir /b *ic_launcher*.svg') DO (
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=48  --export-png=".\mdpi\%%G"
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=72  --export-png=".\hdpi\%%G"
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=96  --export-png=".\xhdpi\%%G"
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=144 --export-png=".\xxhdpi\%%G"
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=216 --export-png=".\xxxhdpi\%%G"
)


FOR /f "tokens=*" %%G IN ('dir /b status_bar*.svg') DO (
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=32  --export-png=".\mdpi\%%G"
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=48  --export-png=".\hdpi\%%G"
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=64  --export-png=".\xhdpi\%%G"
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=96  --export-png=".\xxhdpi\%%G"
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=144  --export-png=".\xxxhdpi\%%G"
)

FOR /f "tokens=*" %%G IN ('dir /b drw_eye*.svg') DO (
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=32  --export-png=".\mdpi\%%G"
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=48  --export-png=".\hdpi\%%G"
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=64  --export-png=".\xhdpi\%%G"
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=96  --export-png=".\xxhdpi\%%G"
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=144  --export-png=".\xxxhdpi\%%G"
)

FOR /f "tokens=*" %%G IN ('dir /b ic_navigation*.svg') DO (
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=32  --export-png=".\mdpi\%%G"
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=48  --export-png=".\hdpi\%%G"
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=64  --export-png=".\xhdpi\%%G"
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=96  --export-png=".\xxhdpi\%%G"
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=144  --export-png=".\xxxhdpi\%%G"
)

FOR /f "tokens=*" %%G IN ('dir /b *title*.svg') DO (
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=512 --export-png=".\title\%%G"
)
FOR /f "tokens=*" %%G IN ('dir /b *title_b*.svg') DO (
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=500 --export-png=".\title\%%G"
)
FOR /f "tokens=*" %%G IN ('dir /b *title_spam*.svg') DO (
 "Z:\Software\PortableApps\InkscapePortable\App\Inkscape\inkscape.exe" %%G --export-height=120 --export-png=".\title\%%G"
)

rename .\mdpi\*.svg    *.png
rename .\hdpi\*.svg    *.png
rename .\xhdpi\*.svg   *.png
rename .\xxhdpi\*.svg  *.png
rename .\xxxhdpi\*.svg *.png

move /Y .\mdpi\*    ..\res\drawable-mdpi\
move /Y .\hdpi\*    ..\res\drawable-hdpi\
move /Y .\xhdpi\*   ..\res\drawable-xhdpi\
move /Y .\xxhdpi\*  ..\res\drawable-xxhdpi\
move /Y .\xxxhdpi\* ..\res\drawable-xxxhdpi\

rename .\title\*.svg   *.png

del /F  /Q .\mdpi\*
del /F  /Q .\hdpi\*
del /F  /Q .\xhdpi\*
del /F  /Q .\xxhdpi\*
del /F  /Q .\xxxhdpi\*
