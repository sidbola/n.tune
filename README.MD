# N.Tune
N.Tune is an mobile tuner application for use with stringed instruments. Currently the supported instruments are:
* Guitar
* 4 String Bass
* 5 String Bass
* Violin
* Cello
* Viola
* Ukulele
* Banjo
* Mandolin

## UI Components
All views in the app are custom views created specifically for this application. And are as follows:
* AnimatedMenu is a custom view for the menu used for instrument and tuning selection at the top left of the screen
* CircleButton is an extention of the Button view that allows for circular backgrounds
* NotesDisplay is a compount custom view takes a tuning as a parameter and displays them at the top of the screen to show the user what they will be tuning to
* PitchDisplay is a custom view that animates a line representing the current frequency the user is playing and how far it deviates from the closest note in the selected tuning
* PitchDeviation is a compound custom view that displace a numerical deviation from the closest note in the tuning (in both Hz and ct)

## External Libraries
The app makes use of the [TarsosDSP Audio Library](https://github.com/JorenSix/TarsosDSP) for reading in and processing the audio by applying the [Fast Fourier Transform](https://en.wikipedia.org/wiki/Fast_Fourier_transform) algorithm to calculate note frequencies.

## Screenshots
![Preview of Animated Menu](https://media.giphy.com/media/9SIO6XmjB9O1LK0vjL/giphy.gif)
