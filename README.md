Intro

This java project was created because I did not want to make a web version 
and I wanted to export Strava riders into Garmin connect. While being a premium
member you can download GPX files from Strava, they will not import into Garmin
as they are missing key data elements like time stamps, etc. So this class will fix that.

Since making this program I have also started using it to import rides that I have downloaded 
from Strava so that one can create new segment without being the ride owner. You could do that
in the past, not anymore. So download from Strava, fun program on the file, upload new file,
create segments, and delete ride or keep it if thats your thing. With some extra handly work
you *could* apply digital EPO to any ride. ~ Enjoy!

TO USE, first you must compile:

javac MainClass.java

THEN RUN

java MainClass ./nameinputfile.gpx ./nameoutputfile.gpx

