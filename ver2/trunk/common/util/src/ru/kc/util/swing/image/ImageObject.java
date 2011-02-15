package ru.kc.util.swing.image;

import java.awt.*;


public class ImageObject {
	Image image;
	int offsetX = 0;
	int offsetY = 0;

	public ImageObject(Image image, int offsetX, int offsetY) {
		this.image = image;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public ImageObject(Image image) {
		this.image = image;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}
}
