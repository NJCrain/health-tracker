# Health Tracker
Health Tracker is an app built to help maintain a healthy lifestyle, and provide some inspiration along the way.

# [Health Tracker Backend](https://github.com/NJCrain/health-tracker-backend)
To see the repo for the Server that runs the backend for this app, visit the above link. The server is deployed at https://nc-health-tracker-backend.herokuapp.com

# Change Log
## 1/8/19
* Added Finger exercise to MainActivity
* Added StopWatch to MainActivity
* Updated StopWatch to be resumable and resettable 
* Created an InspirationalImage class
* Added an image carousel that features 3 InspirationalImages
* Image carousel now features 3 unique images and captions

[Screenshots](screenshots/#1819)

## 1/9/19
* Moved Clicker Exercise to its own Activity
* Moved the Stopwatch to its own Activity
* Added ability to send an immediate notification

[Screenshots](screenshots/#1919)

## 1/10/19
* Notifications run on a set delay
* Notifications all have a unique ID when they are enabled
* Notifications can be disabled
* Migrated project to AndroidX
* Setup database and ListView on Exercise Log

[Screenshots](screenshots/#11019-and-11119)

## 1/11/19
* Updated Exercise Entity to auto-generate IDs
* Added inputs on Exercise Log so users can add their own
* Improved Layout of Exercise Log

[Screenshots](screenshots/#11019-and-11119)

## 1/15/19
* Added testing for database
* Began adding Espresso tests for Activities

## 1/16/19
* Finished UI tests with Espresso
* Added section on MainActivity for avatar
* Users can take a photo with the camera and set that as their avatar

[Screenshots](screenshots/#11619)

## 1/17/19
* Users can select an image already saved on their phone for an avatar
* App now gets extra exercise entries from a database
* When a user adds an exercise, it is saved to the database locally and on the deployed server

[Screenshots](screenshots/#11719)

## 1/18/19
* Exercises now include a location (currently via a lat and long)
* Username displayed on all pages
* Added profile page for users to edit their profile

[Screenshots](screenshots/#11819)

## 1/21/19
* Home page now features some stats
* Added use of fine location for better location accuracy

[Screenshots](screenshots/#12119)

