package de.selebrator.complexplane.plotter;

public class CartesianCoordinateSystem extends Bitmap {
	public final double minX;
	public final double maxX;
	public final double minY;
	public final double maxY;
	public final double sclX;
	public final double sclY;
	public final boolean showAxes;
	public final boolean showLabels;
	public final double lengthX;
	public final double lengthY;
	public final int axisXPixel;
	public final int axisYPixel;
	
	public CartesianCoordinateSystem(int width, int height, double minX, double maxX, double minY, double maxY, double sclX, double sclY, boolean showAxes, boolean showLabels) {
		super(width, height);
		if(width < 1 || height < 1 || minX >= maxX || minY >= maxY || sclX < 0.0 || sclY < 0.0) {
			throw new IllegalArgumentException("Illegal Window\nwidth: " + width + ",\nheight: " + height + ",\nminX: " + minX + ",\nmaxX: " + maxX + ",\nminY: " + minY + ",\nmaxY: " + maxY + ",\nsclX: " + sclX + ",\nsclY: " + sclY + ",\nshowAxes: " + showAxes + ",\nshowLabels: : " + showLabels);
		}
		if(Double.isNaN(width) || Double.isNaN(height) || Double.isNaN(minX) || Double.isNaN(maxX) || Double.isNaN(minY) || Double.isNaN(maxY) || Double.isNaN(sclX) || Double.isNaN(sclY)) {
			throw new IllegalArgumentException("Illegal Window\nwidth: " + width + ",\nheight: " + height + ",\nminX: " + minX + ",\nmaxX: " + maxX + ",\nminY: " + minY + ",\nmaxY: " + maxY + ",\nsclX: " + sclX + ",\nsclY: " + sclY + ",\nshowAxes: " + showAxes + ",\nshowLabels: : " + showLabels);
		}
		if(Double.isInfinite(width) || Double.isInfinite(height) || Double.isInfinite(minX) || Double.isInfinite(maxX) || Double.isInfinite(minY) || Double.isInfinite(maxY) || Double.isInfinite(sclX) || Double.isInfinite(sclY)) {
			throw new IllegalArgumentException("Illegal Window\nwidth: " + width + ",\nheight: " + height + ",\nminX: " + minX + ",\nmaxX: " + maxX + ",\nminY: " + minY + ",\nmaxY: " + maxY + ",\nsclX: " + sclX + ",\nsclY: " + sclY + ",\nshowAxes: " + showAxes + ",\nshowLabels: : " + showLabels);
		}
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.sclX = sclX;
		this.sclY = sclY;
		this.showAxes = showAxes;
		this.showLabels = showLabels;
		this.lengthX = this.maxX - this.minX;
		this.lengthY = this.maxY - this.minY;
		this.axisXPixel = (int) Math.round((1 + this.minY / this.lengthY) * this.height);
		this.axisYPixel = (int) Math.round(-this.minX / this.lengthX * this.width);
	}
	
	public void drawAxes() {
		if(this.showAxes) {
			int color = 0xffaaaaaa;
			for(int x = 0; x < this.width; x++) {
				this.setPixel(x, this.axisXPixel, color);
			}
			for(int y = 0; y < this.height; y++) {
				this.setPixel(this.axisYPixel, y, color);
			}
			
			if(this.sclX > 0.0 && this.sclX / this.lengthX >= 2.0 / this.width) {
				double xEnd = Math.abs(this.maxX);
				if(Math.abs(this.minX) > xEnd) {
					xEnd = Math.abs(this.minX);
				}
				double xInterval = Pixel.xByPoint(this, this.sclX) - this.axisYPixel;
				int xLength = (int) (0.01 * this.height / 2.0) + 1;
				for(int i = 1; i * this.sclX < xEnd; i++) {
					int px = (int) (this.axisYPixel + i * xInterval);
					int nx = (int) (this.axisYPixel - i * xInterval);
					int py = this.axisXPixel + xLength;
					int ny = this.axisXPixel - xLength;
					this.drawLine(px, py, px, ny, color);
					this.drawLine(nx, py, nx, ny, color);
				}
			}
			
			if(this.sclY > 0.0 && this.sclY / this.lengthY >= 2.0 / this.height) {
				double yEnd = Math.abs(this.maxY);
				if(Math.abs(this.minY) > yEnd) {
					yEnd = Math.abs(this.minY);
				}
				double yInterval = Pixel.yByPoint(this, this.sclY) - this.axisXPixel;
				int yLength = (int) (0.01 * this.width / 2.0) + 1;
				for(int j = 0; j * this.sclY < yEnd; j++) {
					int nx = this.axisYPixel - yLength;
					int px = this.axisYPixel + yLength;
					int py = (int) (this.axisXPixel + j * yInterval);
					int ny = (int) (this.axisXPixel - j * yInterval);
					this.drawLine(nx, py, px, py, color);
					this.drawLine(nx, ny, px, ny, color);
				}
			}
		}
	}
	
	@Deprecated
	public void plotPoint(double x, double y, int color) {
		if(!Point.isLegal(x, y)) {
			return;
		}
		Pixel p = new Pixel(this, x, y);
		this.setPixel(p.x, p.y, color);
	}
	
	//TODO must be eclipse is width != height
	@Deprecated
	public void plotCircle(double x, double y, double radius, int color) {
		if(!Point.isLegal(x, y)) {
			return;
		}
		Pixel p = new Pixel(this, x, y);
		this.drawCircle(p.x, p.y, Pixel.xByPoint(this, radius - x) - p.x, color);
	}
	
	public void plotEllipse(double x, double y, double radiusX, double radiusY, int color) {
		if(!Point.isLegal(x, y)) {
			return;
		}
		Pixel p = new Pixel(this, x, y);
		this.drawEllipse(p.x, p.y, Pixel.xByPoint(this, radiusX - x) - p.x, Pixel.yByPoint(this, -radiusY - y) - p.y, color);
	}
	
	@Deprecated
	public void plotDot(double x, double y, double radius, int color) {
		if(!Point.isLegal(x, y)) {
			return;
		}
		Pixel p = new Pixel(this, x, y);
		this.drawDot(p.x, p.y, Pixel.xByPoint(this, x + radius) - p.x, color);
	}
	
	@Override
	public String toString() {
		return "width: " + width + ",\nheight: " + height + ",\nminX: " + minX + ",\nmaxX: " + maxX + ",\nminY: " + minY + ",\nmaxY: " + maxY + ",\nsclX: " + sclX + ",\nsclY: " + sclY + ",\nshowAxes: " + showAxes + ",\nshowLabels: : " + showLabels;
	}
}
