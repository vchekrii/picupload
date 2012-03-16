package ua.dod.picload.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

public class ImageResize {
	public String scaleImage(int x, String directory, String name, String date) throws IOException{
		int leastSide=1;

        BufferedImage img = ImageIO.read(new File(name));
        
        int height = img.getHeight();
        int width = img.getWidth();
        
        if (height>=width) {
        	leastSide=height;
        } else {
        	leastSide=width;
        }
        
        if (leastSide>=x){
        	BufferedImage scaledImg = Scalr.resize(img, Scalr.Method.ULTRA_QUALITY, x);
        	ImageIO.write(scaledImg, "jpg", new File(directory, "Img"+date+"_x"+x+".jpg"));
        	return "Img"+date+"_x"+x+".jpg";
        }
        
        return null;
	}

}
