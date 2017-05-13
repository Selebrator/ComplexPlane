package de.selebrator.complexplane.plotter;

public class Point {
	public double x;
	public double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public static Point fromPixel(CartesianCoordinateSystem system, int x0, int y0) {
		return new Point(xByPixel(system, x0), yByPixel(system, y0));
	}
	
	public static boolean isLegal(double x, double y) {
		return !Double.isNaN(x) && !Double.isNaN(y) && !Double.isInfinite(x) && !Double.isInfinite(y);
	}
	
	public boolean isLegal() {
		return !Double.isNaN(this.x) && !Double.isNaN(this.y) && !Double.isInfinite(this.x) && !Double.isInfinite(this.y);
	}
	
	public static double xByPixel(CartesianCoordinateSystem system, int x) {
		return (x - system.axisYPixel) / (double) (system.width - system.axisYPixel) * system.maxX;
		//return x - ((system.width - x) / system.width) * system.minX;
	}
	
	public static double yByPixel(CartesianCoordinateSystem system, int y) {
		return (system.axisXPixel - y) * system.maxY / (double) system.axisXPixel;
	}
	
	@Override
	public String toString() {
		return "[" + this.x + ", " + this.y + "]";
	}
}
