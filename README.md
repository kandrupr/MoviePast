## Movie Past

Movie Past is an application that allows you to look up your favorite movies, tv shows, and actors using speech recognition.
Eventually release on app store when all bugs sorted.

### Installing

Need an Android IDE - Android Studio / Eclipse
All package dependencies are in the build file
A few of the important ones

* [LeakCanary](https://github.com/square/leakcanary) for spotting memory leaks
* [Picasso](https://github.com/square/picasso) for image loading
* [GSON](https://github.com/google/gson) JSON management
* [Dialogflow/API.AI](https://github.com/dialogflow/dialogflow-android-client) for natrual language processing

Will need an API Key from TMDB. You can get one from signing up [here.](https://www.themoviedb.org/account/signup)
Put your key into a resource file and name it "TMDBAPI".

### Walkthrough
<img src="0.jpg" alt="app/src/main/assets/screenshots/0.png" style="width: 200px;"/>

### Basic commands
```
Upcoming movies
Westerns of 2005
Action movies of 2007
Best comedies
```
```
- Most actors/actresses
- Most movies
- Most tv shows
```
### What's left
* Refine Speeh model
* First and Last pages on Main Activity
* Help Button
### License

This project is licensed under the MIT License

### Acknowledgments
* All movie data taken from [The Movie Database](https://www.themoviedb.org/?language=en)
* [ViewPager](https://www.youtube.com/watch?v=kaZCrPhayL0)
* [Grid Layout](https://www.youtube.com/watch?v=HuAKlyHbKwE)
* Launcher icon made by [SmashIcons](https://www.flaticon.com/authors/smashicons) from www.flaticon.com
* Popcorn icon made by [Dimi Kazak](https://www.flaticon.com/authors/dimi-kazak) from www.flaticon.com 