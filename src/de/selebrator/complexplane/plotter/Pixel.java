package de.selebrator.complexplane.plotter;

public class Pixel {
	public int x;
	public int y;
	
	public Pixel(int x0, int y0) {
		this.x = x0;
		this.y = y0;
	}
	
	public Pixel(CartesianCoordinateSystem system, double x, double y) {
		if(!Point.isLegal(x, y)) {
			throw new IllegalArgumentException("Illegal Position");
		}
		this.x = xByPoint(system, x);
		this.y = yByPoint(system, y);
	}
	
	public Pixel(CartesianCoordinateSystem system, Point point) {
		if(!point.isLegal()) {
			throw new IllegalArgumentException("Illegal Position");
		}
		this.x = xByPoint(system, point.x);
		this.y = yByPoint(system, point.y);
	}
	
	public static int xByPoint(CartesianCoordinateSystem system, double pointX) {
		if(!Double.isNaN(pointX) && !Double.isInfinite(pointX)) {
			return system.axisYPixel + (int) Math.round(pointX / system.maxX * (system.width - system.axisYPixel));
		}
		throw new IllegalArgumentException("Illegal Position");
	}
	
	public static int yByPoint(CartesianCoordinateSystem system, double pointY) {
		if(!Double.isNaN(pointY) && !Double.isInfinite(pointY)) {
			return (int) Math.round((1.0 - pointY / system.maxY) * system.axisXPixel);
		}
		throw new IllegalArgumentException("Illegal Position");
	}
	
	@Override
	public String toString() {
		return "[" + this.x + ", " + this.y + "]";
	}
}
