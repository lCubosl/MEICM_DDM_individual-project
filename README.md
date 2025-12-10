# Alignment Tool
Project for my Master's degree in Computer Engineering - Mobile Computing. This work is part of the individual project for the Mobile Devices Development course.

It is an alignment tool that can be used to check camber and toe values in various vehicles by make and model. These values are stored in local storage so they can be checked later for logging and monitoring purposes.

App built in Kotlin using Jetpack Compose.

## Functionality
### Features
* check vehicle's toe values
* check vehicle's camber values
* user logging of toe and camber measurements
* user logging of vehicle make and model

#### Toe Screen
* displays toe value in degrees by single wheel
* displays average toe deviation between axle (front, rear) wheels in degrees
* logs toe value in degrees by single wheel
* logs average toe deviation between axle (front, rear) wheels in degrees
* checks leveling data and blocks user from logging unleveled data

#### Camber Screen
* displays individual camber value in degrees
* logs individual camber value in degrees
* prevents user from logging data that is not leveled
 
#### Saved Screen
* data logs for toe measurements (date, single wheel in degrees (fl,fr,rl,rr), axle (f,r))
* data logs for camber measurements (date, single wheel in degrees)
 
#### Settings Screen
* select user's vehicle by make and model (through CarAPI)
* change between light, dark, and System theme

#### Tech stack
* Kotlin
* Jetpack Compose

## Demo Screens
#### Toe Screen
#### Camber Screen
#### Saved Screen
#### Settings Screen

### TODO
* UI needs to be improved upon
* code cleanup:
  * comments and documentation
  * segmentation of composables into separate components folder
  * local storage date is CURRENT TIME IN MILLISECONDS !!! needs to be changed ASAP
  * CarApi key was causing too many issues when stored in local properties so it was temporarily moved. Investigate and put it in local.properties again
* Better UI and UX since its mostly boxes right now and default themecolors
* Phone sensors can be finicky leading to wrong measurements at times
* Actual integration with CarApi so it can display information from selected model and saved on local storage alongside toe and camber
* Additional screens
  * Standalone angle Screen for torques + angle
  * Caster Screen
