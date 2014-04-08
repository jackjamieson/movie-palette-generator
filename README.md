movie-palette-generator
=======================

Takes the frames of a video file and compresses them into a single image in order to see the overall color palette.


###Setting up
If you wish to just use the program, you can run it with the precompiled files in /bin.  Edit the **config.properties** with the correct file name and change the frame size and every n frames if you wish.  I got decent results with the provided values.  

**WARNING ABOUT EVERY N FRAMES** Using less than 10 will take a lot longer, more than 10 may look bad.

###Files tested and working:

* AVI(interleaved)
* WMV
* MKV

###Files tested and NOT working:
* MP4
* AVI(non-interleaved)


###Results

* [The Breakfast Club](http://i.imgur.com/tcxo1nR.png)
* [Fantastic Mr. Fox](http://i.imgur.com/rPAPehX.png)

