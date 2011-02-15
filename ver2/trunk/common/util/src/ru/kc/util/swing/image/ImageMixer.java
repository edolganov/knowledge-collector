package ru.kc.util.swing.image;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

public class ImageMixer {

	public static Image getCombinedImage(ImageObject... simpleImages) {
		int maxW = 0;
		int maxH = 0;
		for (ImageObject img : simpleImages) {
			int offsetX = (img.offsetX > 0) ? img.offsetX : 0;
			int offsetY = (img.offsetY > 0) ? img.offsetY : 0;
			int width = img.image.getWidth(null) + offsetX;
			int height = img.image.getHeight(null) + offsetY;
			if (width > maxW)
				maxW = width;
			if (height > maxH)
				maxH = height;
		}
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gs.getDefaultConfiguration();
		BufferedImage bufferedImage = gc.createCompatibleImage(maxW, maxH,
				Transparency.BITMASK);
		Graphics g = bufferedImage.getGraphics();
		for (ImageObject img : simpleImages) {
			int offsetX = (img.offsetX > 0) ? img.offsetX : 0;
			int offsetY = (img.offsetY > 0) ? img.offsetY : 0;
			g.drawImage(img.image, offsetX, offsetY, null);
		}

		return bufferedImage;
	}

}
