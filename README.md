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

### Walkthrough
Press the popcorn button to start listening. Start speaking when the page notifys you that it is "Listening...".
<p>
  <img width="200" height="400" alt="Home Page" src="app/src/main/assets/screenshots/0.png"> 
  <img width="200" height="400" alt="Home Page" src="app/src/main/assets/screenshots/2.png">
</p>

It will the load results. Where you can select on one of the items to load its description...
<p>
  <img width="200" height="400" alt="Home Page" src="app/src/main/assets/screenshots/3.png">
  <img width="200" height="400" alt="Home Page" src="app/src/main/assets/screenshots/4.png"> 
</p>

On the description page, you can click on cast, similar movies, and similar tv shows to show their description or click on image to get a blown up image.
<p>
  <img width="200" height="400" alt="Home Page" src="app/src/main/assets/screenshots/5.png">
  <img width="200" height="400" alt="Home Page" src="app/src/main/assets/screenshots/6.png"> 
</p>

If the speech model can't understand what you are saying or can't find results on something, you can try a text request.
<p>
  <img width="200" height="400" alt="Home Page" src="app/src/main/assets/screenshots/1.png">
  <img width="200" height="400" alt="Home Page" src="app/src/main/assets/screenshots/7.png"> 
</p>

### What's left
* Refine Speech model
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
