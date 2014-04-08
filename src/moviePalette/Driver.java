/*
 * 4/8/2014
 * Movie color palette generator
 * Copyright 2014 Jack Jamieson
 * Under the MIT License see LICENSE.txt
 * 
 */


package moviePalette;

import java.io.IOException;

import com.googlecode.javacv.FrameGrabber.Exception;

public class Driver {

	public static void main(String[] args) throws Exception, IOException  {
		
		ParseVideo p = new ParseVideo();
		p.process();

	}

}
