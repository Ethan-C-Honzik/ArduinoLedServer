package ledServer;

import java.awt.AWTException;
import java.awt.*;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class ScreenRecorder {
	private Robot r;
	
	public Color[] colorSecs = new Color[16];
	private Rectangle screenRect = new Rectangle();
	private FastRGB fastRGB;
	
	//amount of pixels to skip each sample
	private int pixelSkip = 6;
	//------DEBUG-------
	//set debug mode on/off
	boolean debug = false;
	boolean pic = false;
	boolean BREAK = false;
	JFrame frame = new JFrame();

	public ScreenRecorder() {
		try {
			r = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenRect.width = screenSize.width;
		screenRect.height = screenSize.height;
		System.out.println("Detected Screen Width: " + screenSize.width);
		System.out.println("Detected Screen height: " + screenSize.height);		
		
		if(debug) {
			frame.getContentPane().setLayout(new FlowLayout());
		}
		
		for(int i = 0; i<16; i++) {
			colorSecs[i] = new Color(0,0,0);
		}
	}

	public void UpdateColorSections() {
		//pull image from desktop
		BufferedImage image = r.createScreenCapture(screenRect);
		//resize it by a factor of 10
		//image = resize(image, (int)(image.getWidth() * 0.1 + 0.5), (int)(image.getHeight() * 0.1 + 0.5));
		//apply a blur to the image
		
		
		if(debug) {
			for(int i = 0; i<16; i++) {
				if(colorSecs[i].getBlue() > 1 && (colorSecs[i].getRed() + colorSecs[i].getGreen())/2 < colorSecs[i].getBlue()) {
					BREAK = true;
					pic = true;
				}
			}
			if(pic) {
				frame.getContentPane().add(new JLabel(new ImageIcon(image)));
				frame.pack();
				frame.setVisible(true);
				pic = false;
			}
		}
		
		fastRGB = new FastRGB(image);
		for (int section = 0; section < 16; section++) {
			int pixelCount = 0;
			int avgRed = 0;
			int avgGreen = 0;
			int avgBlue = 0;
			for (int i = 0; i < image.getHeight() - pixelSkip; i+= pixelSkip) {
				//start at the beginning of the section then move to the end
				for (int j = (int)(image.getWidth() / 16 * (section + 0));
						j < (int)(image.getWidth() / 16 * (section + 1)) - pixelSkip; j+= pixelSkip) {
						int color = fastRGB.getRGB(j, i);
						avgRed += (color>>16)&0xFF;
						avgGreen += (color>>8)&0xFF;
						avgBlue += (color>>0)&0xFF;		
						pixelCount ++;
				}
			}
			avgRed = avgRed / pixelCount;
			avgGreen = avgGreen / pixelCount;
			avgBlue = avgBlue / pixelCount;
			colorSecs[section] = new Color(avgRed, avgGreen, avgBlue);
		}
		SetColorSV();
	}
	
		
	//sets the saturation and value for each color
	public void SetColorSV(){
		for(int i = 0; i < colorSecs.length; i++) {			
			float[] hsv = new float[3];
			Color.RGBtoHSB(colorSecs[i].getRed(),colorSecs[i].getGreen(),colorSecs[i].getBlue(),hsv);
			float s = 0;
			if(hsv[1] > 0.5) {
				s = 1;
			}else {
				s = hsv[1] / 0.5f;
			}
			//filtered saturation. Exponential value
			//colorSecs[i] = Color.getHSBColor(hsv[0], s, (float) Math.pow(hsv[2], 2));
			
			//filtered saturation
			colorSecs[i] = Color.getHSBColor(hsv[0], s, hsv[2]);
			
			//Raw hsv
			//colorSecs[i] = Color.getHSBColor(hsv[0], hsv[1], hsv[2]);
			
			//RAW rgb
			//colorSecs[i] = new Color(colorSecs[i].getRed(),colorSecs[i].getGreen(),colorSecs[i].getBlue());
			//System.out.println(s);
		}

	}
	
	@SuppressWarnings("unused")
	private  BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, img.getType());

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}
	
	
	
	
}
