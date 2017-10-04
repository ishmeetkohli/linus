# Linus
# Linus Music Recommendation System

### Process:

* Million song dataset was used for getting audio features.

* The whole dataset was read and features for each song were averaged out over the entire song duration.

* A median value was calculated for each feature of the songs in the dataset.

* If the value of feature of a song falls below the median it was given a value of 0 and if it was greater than or equal to the median it was given a value of 1.

* After tweaking and playing with the dataset it was decided to give greater weight to tempo of the song.

* After this step we had the vector representations of each song in the dataset.

* This was used to calculate distance between songs and an adjacency matrix was created. The distance formula used was a simple difference between the two vectors and an absolute value was taken to avoid negative distances. We avoided float values as they create a very big file.

* HDF5 format was used for storing data as this provides a fast and efficient way to store a large amount of data.

* The main algorithm for calculating distance from liked and disliked songs was written. Basically, if a song is closer to a liked song than it is to a disliked song, then it is a valid candidate song. If there are no valid candidate songs, we take into account ratios. The song closest to one of the liked songs is chosen to be played next i.e. the song with the smallest liked distance/disliked distance ratio.

* The algorithm is imported and run as a Flask server which interacts with the mobile app through http services. Flask server takes in parameters from app and returns a json file containing information about the next song to be played.

### Client Details:
*Built on Android SDK version 23
*Uses Spotify player Api beta 1.0.0

### Server Details:
* Built on python using flask server
* networkAlgorithm.py contains the main algorithm
* linusServer.py contains the server implementation
* utilities.py contains functions that will be used often
