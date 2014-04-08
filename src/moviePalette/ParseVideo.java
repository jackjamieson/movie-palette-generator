/*
 * 4/8/2014
 * Movie color palette generator
 * Copyright 2014 Jack Jamieson
 * Under the MIT License see LICENSE.txt
 * 
 */

package moviePalette;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.imageio.ImageIO;

import com.googlecode.javacv.FFmpegFrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;

public class ParseVideo{

	private Properties config = new Properties();

	private BufferedImage buff[];//Array of the collected frames that are merged together.

	private int buffcount = 0;//Keep a count of how many frames we have collected, used to place them into the array.
	private int frameWidth;//The width in pixels that each frame should be reduced to.
	private int everyNFrames;//Capture every nth frame, 10 is a good number.
	//Fewer will take longer(may not look better), more may not give a good representation.

	private String file;//The absolute file location.  Must escape \ characters with another \.

	public void process() throws Exception, IOException
	{
		load();
		parse();
		dump();
	}
	
	public void load() throws IOException
	{
		//Load config options from a file so we don't have to recompile every time.
		InputStream input = new FileInputStream("config.properties");
		config.load(input);

		//Set our values.
		frameWidth = Integer.parseInt(config.getProperty("frameWidth"));
		everyNFrames = Integer.parseInt(config.getProperty("everyNFrames"));
		file = config.getProperty("file");
	}

	public void parse() throws Exception
	{
		//JavaCV+FFmpeg lib used to grab frames found at https://code.google.com/p/javacv/
		FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(file);

		//Begin parsing video
		System.out.print("Parsing frames...");
		grabber.start();
		buff = new BufferedImage[(int)(grabber.getLengthInFrames()/everyNFrames) + 1];//Initialize array to the amount of frames we will be collecting.

		for(int i = 0; i <= grabber.getLengthInFrames(); i++)
		{
			if(i % everyNFrames == 0)
			{
				BufferedImage bi = grabber.grab().getBufferedImage();//Get the frame
				int height = bi.getHeight();

				BufferedImage resized = new BufferedImage(frameWidth, height, BufferedImage.TYPE_INT_RGB);//Create a new image we will copy the frame into.

				Graphics gra = resized.createGraphics();

				gra.drawImage(bi, 0, 0, frameWidth, height, null);//Resize the image to specified pixel width.
				gra.dispose();

				buff[buffcount] = resized;//Place image into the array.
				buffcount++;
			}
			else grabber.grab();//Must still grab the frames we aren't using to advance the buffer.

		}

		grabber.stop();
		System.out.println("done");
	}

	public void dump() throws IOException
	{
		System.out.print("Merging images...");
		BufferedImage result = null;//The resulting image.
		
		for(int z = 0; z < buff.length; z++)
		{
			if(z == 0)
				result = mergeImages(buff[z], buff[z+1]);//If it's the first frame we create the first result image.
			else if(z < buff.length-1)
			{
				result = mergeImages(result, buff[z+1]);//The rest of the frames we need to use the previous result and combine it with the next image.
			}
		}

		BufferedImage resizeResult = new BufferedImage(700, 300, BufferedImage.TYPE_INT_RGB);//New image to copy result into.

		Graphics graRes = resizeResult.createGraphics();

		graRes.drawImage(result, 0, 0, 700, 300, null);//Resize final image to 700x300
		graRes.dispose();

		ImageIO.write(resizeResult, "png", new File("dump/result.png"));//Our resulting image will be in dump/result.png
		System.out.println("done");

	}

	//Combines two images into one, putting them side by side.
	public BufferedImage mergeImages(BufferedImage left, BufferedImage right)
	{
		//Combine the widths of the left and right.
		BufferedImage composite = new BufferedImage(
				left.getWidth() + right.getWidth(),
				left.getHeight(),
				BufferedImage.TYPE_INT_RGB);

		Graphics gra = composite.getGraphics();
		
		gra.drawImage(left, 0, 0, null);
		gra.drawImage(right, left.getWidth(), 0, null);//Draw it.
		gra.dispose();
		
		return composite;
	}
}
