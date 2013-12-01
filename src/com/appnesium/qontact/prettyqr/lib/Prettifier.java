package com.appnesium.qontact.prettyqr.lib;


import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO; // not available in Android

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

public class Prettifier {
	static final int BLACK =-16777216; //0x000000;
	static final int BRIGHTNESS_LIMIT = 0xa0;

	private int width = 125;
	private int heigth = 125;
	
	private BufferedImage source_QR;
	private BufferedImage source_logo;

	public Prettifier()  {
		
	}
	public Prettifier(String logo_location, String qr_data) throws IOException {
		initialise(logo_location,  qr_data);
	}
	
	public Prettifier(String logo_location, String qr_data, int widht, int heigth) throws IOException {
		initialise(logo_location,  qr_data);
		
		setWidth(widht);
		setHeigth(heigth);
	}
	
	private void initialise(String logo_location, String qr_data)  throws IOException {
		createSourceQR(qr_data);
		readLogo(logo_location);
	}

	public void readLogo(String location ) throws IOException {
		source_logo = ImageIO.read(new File(location));
	}

	public void createSourceQR(String data) throws IOException {
		File file = QRCode.from(data).to(ImageType.PNG).withSize(width, heigth).file();

		source_QR = ImageIO.read(file);
	}
	
	public void readSourceQR(String location) throws IOException {
		source_QR = ImageIO.read(new File(location));
	}
	
	public void prettify(String target_location)  throws IOException{
				
		BufferedImage target_QR = transferColors();

		//ImageIO.write(source_QR, "png", new File("/home/magnus/workspace/PrettyQR/src/img/qrimg.png"));
		//ImageIO.write(target_QR, "png", new File("/home/magnus/workspace/PrettyQR/src/img/outqr.png"));

		ImageIO.write(source_logo, "png", new File(target_location));
	}

	private BufferedImage transferColors() {
	
		int qwidth = source_QR.getWidth();
		int qheight = source_QR.getHeight();
		

		BufferedImage target_QR = new BufferedImage(qwidth, qheight, source_logo.getType());
			
		int lwidth = source_logo.getWidth();
		int lheight = source_logo.getHeight();
		
		for (int x = 0; x < qwidth; x++) {
			for (int y = 0; y < qheight; y++) {

				int color = source_QR.getRGB(x, y);

				if (color == BLACK) {
					

					int logocolor = source_logo.getRGB( (int)(((float)x/qwidth)*lwidth), (int)(((float)y/qheight)*lheight));
					

					int darkenedlogocolor = limitColorWhiteness(logocolor);
						
					target_QR.setRGB(x, y, darkenedlogocolor);	
					
					
					
				} else {

					target_QR.setRGB(x, y, color);
				}
			}
		}
		
		return target_QR;
	}

	
	/**
	 * Ensures that the color is still scannable on the QR
	 * @param initial_color
	 * @return new adjusted color
	 */
	private int limitColorWhiteness(int initial_color) {
		// Decompose color into subcolors
		int green = (initial_color >> 0) & 0xff;
		int blue = (initial_color >> 8) & 0xff;
		int red = (initial_color >> 16) & 0xff;
		int alpha = (initial_color >> 24) & 0xff;

		// Cap the brighness of each color.
		if (green > BRIGHTNESS_LIMIT) green = BRIGHTNESS_LIMIT;
		if (blue > BRIGHTNESS_LIMIT) blue = BRIGHTNESS_LIMIT;
		if (red > BRIGHTNESS_LIMIT) red = BRIGHTNESS_LIMIT;
		if (alpha > BRIGHTNESS_LIMIT) alpha = BRIGHTNESS_LIMIT;
		
		//reassemble colors
		return ((((((alpha << 8) | red) << 8) | blue ) << 8) | green);
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the heigth
	 */
	public int getHeigth() {
		return heigth;
	}

	/**
	 * @param heigth the heigth to set
	 */
	public void setHeigth(int heigth) {
		this.heigth = heigth;
	}
}
