package de.selebrator.complexplane.plotter;

import java.util.Arrays;

public class Bitmap {
	public int width;
	public int height;
	public int[] pixels;
	
	public Bitmap(int width, int height) {
		this.width = width;
		this.height = height;
		this.pixels = new int[this.width * this.height];
	}
	
	public void clear(int color) {
		Arrays.fill(this.pixels, color);
	}
	
	public void setPixel(int x, int y, int color) {
		if(x < 0 || y < 0 || x >= this.width || y >= this.height) {
			return;
		}
		this.pixels[x + y * this.width] = color;
	}
	
	public int getPixel(int x, int y) {
		if(x < 0 || y < 0 || x >= this.width || y >= this.height) {
			return 0;
		}
		return this.pixels[x + y * this.width];
	}
	
	public void render(Bitmap bitmap, int xOffset, int yOffset) {
		for(int x = 0; x < bitmap.width; ++x) {
			for(int y = 0; y < bitmap.height; ++y) {
				int color = bitmap.getPixel(x, y);
				if(color < 0) {
					this.setPixel(x + xOffset, y + yOffset, color);
				}
			}
		}
	}
	
	public void drawLine(int x0, int y0, int x1, int y1, int color) {
		int width = x1 - x0;
		int height = y1 - y0;
		if(Math.abs(width * width + height * height) > this.width * this.height) {
			return;
		}
		int dx0 = (width < 0) ? -1 : ((width > 0) ? 1 : 0);
		int dy0 = (height < 0) ? -1 : ((height > 0) ? 1 : 0);
		int dx2 = (width < 0) ? -1 : ((width > 0) ? 1 : 0);
		int dy2 = 0;
		int longest = Math.abs(width);
		int shortest = Math.abs(height);
		if(longest <= shortest) {
			longest = Math.abs(height);
			shortest = Math.abs(width);
			dx2 = 0;
			dy2 = ((height < 0) ? -1 : ((height > 0) ? 1 : 0));
		}
		int numerator = longest >> 1;
		for(int i = 0; i <= longest; ++i) {
			this.setPixel(x0, y0, color);
			numerator += shortest;
			if(numerator >= longest) {
				numerator -= longest;
				x0 += dx0;
				y0 += dy0;
			} else {
				x0 += dx2;
				y0 += dy2;
			}
		}
	}
	
	public void drawCircle(int x0, int y0, int radius, int color) {
		int d = (5 - radius * 4) / 4;
		int x = 0;
		int y = radius;
		do {
			this.setPixel(x0 + x, y0 + y, color);
			this.setPixel(x0 + x, y0 - y, color);
			this.setPixel(x0 - x, y0 + y, color);
			this.setPixel(x0 - x, y0 - y, color);
			this.setPixel(x0 + y, y0 + x, color);
			this.setPixel(x0 + y, y0 - x, color);
			this.setPixel(x0 - y, y0 + x, color);
			this.setPixel(x0 - y, y0 - x, color);
			if(d < 0) {
				d += 2 * x + 1;
			} else {
				d += 2 * (x - y) + 1;
				--y;
			}
		} while(++x <= y);
	}
	
	public void drawDot(int x0, int y0, int radius, int color) {
		int d = (5 - radius * 4) / 4;
		int x = 0;
		int y = radius;
		do {
			this.drawLine(x0 + x, y0 + y, x0 - x, y0 + y, color);
			this.drawLine(x0 + x, y0 - y, x0 - x, y0 - y, color);
			this.drawLine(x0 + y, y0 + x, x0 - y, y0 + x, color);
			this.drawLine(x0 + y, y0 - x, x0 - y, y0 - x, color);
			if(d < 0) {
				d += 2 * x + 1;
			} else {
				d += 2 * (x - y) + 1;
				--y;
			}
		} while(++x <= y);
	}
	
	public void drawEllipse(int x0, int y0, int rx, int ry, int color) {
		int ww = rx * rx * 2;
		int hh = ry * ry * 2;
		int fa2 = 4 * ww;
		int fb2 = 4 * hh;
		int x, y, sigma;
		
		for(x = 0, y = ry, sigma = 2 * hh + ww * (1 - 2 * ry); hh * x <= ww * y; x++) {
			this.setPixel(x0 + x, y0 + y, color);
			this.setPixel(x0 - x, y0 + y, color);
			this.setPixel(x0 + x, y0 - y, color);
			this.setPixel(x0 - x, y0 - y, color);
			if(sigma >= 0) {
				sigma += fa2 * (1 - y);
				y--;
			}
			sigma += hh * ((4 * x) + 6);
		}
		
		for(x = rx, y = 0, sigma = 2 * ww + hh * (1 - 2 * rx); ww * y <= hh * x; y++) {
			this.setPixel(x0 + x, y0 + y, color);
			this.setPixel(x0 - x, y0 + y, color);
			this.setPixel(x0 + x, y0 - y, color);
			this.setPixel(x0 - x, y0 - y, color);
			if(sigma >= 0) {
				sigma += fb2 * (1 - x);
				x--;
			}
			sigma += ww * ((4 * y) + 6);
		}
	}
}
