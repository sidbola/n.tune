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
* [AnimatedMenu](https://github.com/sidbola/n.tune/blob/master/app/src/main/java/com/sidbola/ntune/ui/view/AnimatedMenu.kt) is a custom view for the menu used for instrument and tuning selection at the top left of the screen
* [CircleButton](https://github.com/sidbola/n.tune/blob/master/app/src/main/java/com/sidbola/ntune/ui/view/CircleButton.kt) is an extention of the Button view that allows for circular backgrounds
* [NotesDisplay](https://github.com/sidbola/n.tune/blob/master/app/src/main/java/com/sidbola/ntune/ui/view/NotesDisplay.kt) is a compount custom view takes a tuning as a parameter and displays them at the top of the screen to show the user what they will be tuning to
* [PitchDisplay](https://github.com/sidbola/n.tune/blob/master/app/src/main/java/com/sidbola/ntune/ui/view/PitchDeviation.kt) is a custom view that animates a line representing the current frequency the user is playing and how far it deviates from the closest note in the selected tuning
* [PitchDeviation](https://github.com/sidbola/n.tune/blob/master/app/src/main/java/com/sidbola/ntune/ui/view/PitchDeviation.kt) is a compound custom view that displace a numerical deviation from the closest note in the tuning (in both Hz and ct)
* [TunerDisplay](https://github.com/sidbola/n.tune/blob/master/app/src/main/java/com/sidbola/ntune/ui/view/TunerDisplay.kt) is a compound custom view that holds all of the previously mentioned views to allow for modularity in the application for future improvements. If I ever want to make a new UI configuration or new type of tuner, I could easily make a new TunerDisplay view that fits my new requirements

## External Libraries
The app makes use of the [TarsosDSP Audio Library](https://github.com/JorenSix/TarsosDSP) for reading in and processing the audio by applying the [Fast Fourier Transform](https://en.wikipedia.org/wiki/Fast_Fourier_transform) algorithm to calculate note frequencies.

## Screenshots
<p align="center">
 <img src="https://imgur.com/3T3bMHJ.gif" width="400"/>
