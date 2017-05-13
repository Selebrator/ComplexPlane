package de.selebrator.complexplane;

public class ComplexNumber {
	private final double a;
	private final double b;
	public static int precision = 10;
	
	public static final ComplexNumber E = new ComplexNumber(Math.E, 0.0);
	public static final ComplexNumber PI = new ComplexNumber(Math.PI, 0.0);
	public static final ComplexNumber I = new ComplexNumber(0.0, 1.0);
	
	public ComplexNumber(double a, double b) {
		this.a = a;
		this.b = b;
	}
	
	public static ComplexNumber fromPolarForm(double abs, double angle) {
		return new ComplexNumber(abs * Math.cos(angle), abs * Math.sin(angle));
	}
	
	public ComplexNumber conjugate() {
		return new ComplexNumber(this.a, -this.b);
	}
	
	public double modulus() {
		return this.abs() * this.abs();
	}
	
	public double real() {
		return this.a;
	}
	
	public double imaginary() {
		return this.b;
	}
	
	public double abs() {
		return Math.sqrt(this.a * this.a + this.b * this.b);
	}
	
	public double angle() {
		return Math.atan2(this.b, this.a);
	}
	
	public boolean isReal() {
		ComplexNumber rounded = this.round(ComplexNumber.precision);
		return !Double.isNaN(rounded.a) && rounded.b == 0.0;
	}
	
	public boolean isPureImag() {
		ComplexNumber rounded = this.round(ComplexNumber.precision);
		return rounded.a == 0.0 && !Double.isNaN(rounded.b);
	}
	
	public String getRectangular() {
		if(this.isReal()) {
			return this.a + "";
		}
		if(this.isPureImag()) {
			return this.b + "i";
		}
		if(Math.signum(this.b) != -1.0) {
			return this.a + " + " + this.b + "i";
		}
		return this.a + " - " + -this.b + "i";
	}
	
	public String getRectangular(int digits) {
		return this.round(digits).getRectangular();
	}
	
	public String getPolar() {
		return this.abs() + " * e^(" + this.angle() + "i)";
	}
	
	public static String[] getPolar(ComplexNumber[] numbers) {
		String[] polars = new String[numbers.length];
		for(int i = 0; i < numbers.length; ++i) {
			polars[i] = numbers[i].getPolar();
		}
		return polars;
	}
	
	public ComplexNumber add(ComplexNumber c) {
		return new ComplexNumber(this.a + c.a, this.b + c.b);
	}
	
	public ComplexNumber substract(ComplexNumber c) {
		return new ComplexNumber(this.a - c.a, this.b - c.b);
	}
	
	public ComplexNumber multiply(ComplexNumber c) {
		return new ComplexNumber(this.a * c.a - this.b * c.b, this.b * c.a + this.a * c.b);
	}
	
	public ComplexNumber multiply(double factor) {
		return new ComplexNumber(factor * this.a, factor * this.b);
	}
	
	public ComplexNumber divide(ComplexNumber c) {
		if(c.a == 0.0 && c.b == 0.0) {
			throw new IllegalArgumentException("Cannot divide by zero");
		}
		return new ComplexNumber((this.a * c.a + this.b * c.b) / (c.a * c.a + c.b * c.b), (this.b * c.a - this.a * c.b) / (c.a * c.a + c.b * c.b));
	}
	
	public ComplexNumber divide(double d) {
		if(d == 0.0) {
			throw new IllegalArgumentException("Cannot divide by zero");
		}
		return new ComplexNumber(this.a / d, this.b / d);
	}
	
	public ComplexNumber inverse() {
		return new ComplexNumber(this.a / (this.a * this.a + this.b * this.b), -this.b / (this.a * this.a + this.b * this.b));
	}
	
	public ComplexNumber sqrt() {
		return new ComplexNumber(Math.sqrt((this.abs() + this.a) / 2.0), (this.b < 0.0) ? -1.0 : (1.0 * Math.sqrt((this.abs() - this.a) / 2.0)));
	}
	
	public ComplexNumber log() {
		return new ComplexNumber(Math.log(this.abs()), this.angle());
	}
	
	public static ComplexNumber pow(ComplexNumber b, ComplexNumber e) {
		return ComplexNumber.fromPolarForm(Math.pow(b.a * b.a + b.b * b.b, e.a / 2) * Math.exp(-e.b * b.angle()), e.a * b.angle() + (1.0 / 2.0) * e.b * Math.log(b.a * b.a + b.b * b.b));
	}
	
	public ComplexNumber[] root(int n) {
		double factor = Math.pow(this.abs(), 1.0 / n);
		ComplexNumber[] roots = new ComplexNumber[Math.abs(n)];
		for(int k = 0; k < Math.abs(n); ++k) {
			double rAngle = (this.angle() + 2.0 * k * Math.PI) / n;
			roots[k] = new ComplexNumber(Math.cos(rAngle), Math.sin(rAngle)).multiply(factor);
		}
		return roots;
	}
	
	public ComplexNumber round(int digits) {
		double factor = Math.pow(10.0, digits);
		return new ComplexNumber(Math.round(this.a * factor), Math.round(this.b * factor)).divide(factor);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ComplexNumber) {
			ComplexNumber c = (ComplexNumber) obj;
			return this.a == c.a && this.b == c.b;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return this.round(ComplexNumber.precision).getRectangular();
	}
}
