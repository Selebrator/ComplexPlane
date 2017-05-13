package de.selebrator.complexplane.plotter;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Screen extends Bitmap {
	public BufferedImage image;
	
	public Screen(int width, int height) {
		super(width, height);
		this.image = new BufferedImage(this.width, this.height, 2);
		this.pixels = ((DataBufferInt) this.image.getRaster().getDataBuffer()).getData();
	}
}
