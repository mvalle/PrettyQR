package com.appnesium.qontact.prettyqr;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.ParseException;

import com.appnesium.qontact.prettyqr.lib.Prettifier;

public class Main {
	public static void main(String [ ] args) throws ParseException, IOException
	{
	// -l logo_location -d "data" -o output_location -h height -w width -s h+w -q qr_location
		Options options = new Options();
		options.addOption("l", "logo", true, "Location of the logo.");
		options.addOption("d", "data", true, "The data that is to be encoded in the QR.");
		options.addOption("o", "output", true, "The location of the ouput image.");
		options.addOption("h", "height", true, "The height of the output image.");
		options.addOption("w", "width", true, "The width of the output image.");
		options.addOption("s", "size", true, "The size of the heigth and width of the output image. Overrides -h and -w.");
		options.addOption("q", "qrcode",true, "The location of an existing QR code image. Overrides -d");

		CommandLineParser parser = new GnuParser();
		CommandLine cmd = parser.parse(options, args);
		
		
		String l = cmd.getOptionValue("l");
		String d = cmd.getOptionValue("d");
		String o = cmd.getOptionValue("o");
		String q = cmd.getOptionValue("q");
		
		int h = 0;
		try {h = Integer.decode(cmd.getOptionValue("h"));} catch (NullPointerException e) {};
		
		int w = 0;
		try {w = Integer.decode(cmd.getOptionValue("w"));} catch (NullPointerException e) {};
		
		int s = 0;
		try {s = Integer.decode(cmd.getOptionValue("s"));} catch (NullPointerException e) {};
		
		Prettifier prettifier = new Prettifier();
		
		// Logo
		if (l != null) {
			prettifier.readLogo(l);
		}
		
		// QR code
		if (q != null) {
			prettifier.readLogo(q);
		} else if (d != null) {
			prettifier.createSourceQR(d);
		} else {
			System.err.println("Error: Data to encode a QR code from or an existing QR code is required.");
			return;
		}
		
		// Size
		if( s != 0) {
			prettifier.setHeigth(s);
			prettifier.setWidth(s);
		} else if ( w != 0 || h != 0) {
			if (w == 0) {
				prettifier.setHeigth(h);
				prettifier.setWidth(h);
			} else if (h == 0) {
				prettifier.setHeigth(w);
				prettifier.setWidth(w);
			} else {
				prettifier.setHeigth(h);
				prettifier.setWidth(w);
			}
		}
		
		// Output
		// NOTE: Possible bug. Might further process o, inorder to get full path.
		if(o != null) {
			prettifier.prettify(o);
		} else {
			prettifier.prettify("prettyqr_output.png");
		}	
	}
}
