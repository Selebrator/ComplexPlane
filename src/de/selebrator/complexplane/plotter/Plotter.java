package de.selebrator.complexplane.plotter;

import de.selebrator.complexplane.ComplexNumber;
import de.selebrator.complexplane.controll.InputHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.io.IOException;

public class Plotter extends Canvas implements Runnable {
	protected static Plotter instance;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 800;
	public static final String NAME = "Cartesian coordinate system";
	public boolean running;
	public int fps;
	public int frameCap;
	public Screen screen;
	public CartesianCoordinateSystem system;
	
	public Plotter() {
		this.frameCap = 60;
		new InputHandler(this);
		this.screen = new Screen(WIDTH, HEIGHT);
		this.system = new CartesianCoordinateSystem(WIDTH, HEIGHT, -8, 8, -8, 8, 1, 1, true, false);
	}
	
	public void start() {
		this.running = true;
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		int frames = 0;
		double fpsTimer = System.currentTimeMillis();
		double nsPerTick = 1.0E9 / this.frameCap;
		double then = System.nanoTime();
		double unprocessed = 0.0;
		while(this.running) {
			boolean canRender = false;
			double now = System.nanoTime();
			unprocessed += (now - then) / nsPerTick;
			then = now;
			while(unprocessed >= 1.0) {
				canRender = true;
				--unprocessed;
			}
			try {
				Thread.sleep(1L);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			if(canRender) {
				++frames;
				this.render();
				this.tick();
			}
			if(System.currentTimeMillis() - fpsTimer > 1000.0) {
				this.fps = frames;
				frames = 0;
				fpsTimer += 1000.0;
			}
		}
	}
	
	private void tick() {
		//MouseZoom
		if(InputHandler.isMouseHeld()) {
			Point current = Point.fromPixel(this.system, InputHandler.mouseX, InputHandler.mouseY);
			Point last = Point.fromPixel(this.system, InputHandler.mouseLastX, InputHandler.mouseLastY);
			translateSystem((last.x - current.x) / this.system.lengthX, (last.y - current.y) / this.system.lengthY);
		}
		
		if(InputHandler.wheelRotation != 0) {
			scaleSystem(-InputHandler.wheelRotation * 0.02);
		}
		
		//KeyZoom
		if(InputHandler.isKey(KeyEvent.VK_ADD)) { scaleSystem(0.01); }
		if(InputHandler.isKey(KeyEvent.VK_SUBTRACT)) { scaleSystem(-0.01); }
		if(InputHandler.isKey(KeyEvent.VK_RIGHT)) { translateSystem(0.01, 0); }
		if(InputHandler.isKey(KeyEvent.VK_LEFT)) { translateSystem(-0.01, 0); }
		if(InputHandler.isKey(KeyEvent.VK_UP)) { translateSystem(0, 0.01); }
		if(InputHandler.isKey(KeyEvent.VK_DOWN)) { translateSystem(0, -0.01); }
		
		
		if(InputHandler.isKeyPressed(KeyEvent.VK_PLUS)) { scaleSystem(0.01); }
		if(InputHandler.isKeyPressed(KeyEvent.VK_MINUS)) { scaleSystem(-0.01); }
		if(InputHandler.isKeyPressed(KeyEvent.VK_D)) { translateSystem(0.01, 0); }
		if(InputHandler.isKeyPressed(KeyEvent.VK_A)) { translateSystem(-0.01, 0); }
		if(InputHandler.isKeyPressed(KeyEvent.VK_W)) { translateSystem(0, 0.01); }
		if(InputHandler.isKeyPressed(KeyEvent.VK_S)) { translateSystem(0, -0.01); }
		
		if(InputHandler.isKeyPressed(KeyEvent.VK_Z)) {
			System.out.println(this.system);
		}
		
		if(InputHandler.isKeyPressed(KeyEvent.VK_V)) {
			double x = Point.xByPixel(this.system, InputHandler.mouseX);
			double y = f(x);
			System.out.println("f(" + x + ") = " + y);
		}
		
		InputHandler.tick(); // must be after reading inputs
	}
	
	private void translateSystem(double x, double y) {
		double minX, maxX, minY, maxY;
		minX = this.system.minX + x * this.system.lengthX;
		maxX = this.system.maxX + x * this.system.lengthX;
		minY = this.system.minY + y * this.system.lengthY;
		maxY = this.system.maxY + y * this.system.lengthY;
		this.system = new CartesianCoordinateSystem(this.system.width, this.system.height, minX, maxX, minY, maxY, this.system.sclX, this.system.sclY, this.system.showAxes, this.system.showLabels);
	}
	
	/** @param direction 1 = in, -1 = out; */
	private void scaleSystem(double direction) {
		double minX, maxX, minY, maxY;
		minX = this.system.minX + direction * this.system.lengthX;
		maxX = this.system.maxX - direction * this.system.lengthX;
		minY = this.system.minY + direction * this.system.lengthY;
		maxY = this.system.maxY - direction * this.system.lengthY;
		this.system = new CartesianCoordinateSystem(this.system.width, this.system.height, minX, maxX, minY, maxY, this.system.sclX, this.system.sclY, this.system.showAxes, this.system.showLabels);
	}
	
	private void render() {
		BufferStrategy bufferStrategy = this.getBufferStrategy();
		if(bufferStrategy == null) {
			this.createBufferStrategy(3);
			this.requestFocus();
			return;
		}
		Graphics graphics = bufferStrategy.getDrawGraphics();
		this.screen.clear(0xffffffff);
		this.system.clear(0xffffffff);
		this.system.drawAxes();
		
		renderFunction();
		
		graphics.drawImage(this.screen.image, 0, 0, WIDTH, HEIGHT, null);
		graphics.dispose();
		bufferStrategy.show();
	}
	
	private static double f(double x) {
		//return (Math.sqrt( (x + (1.706E-6 / 2.0)) * (x + (1.706E-6 / 2.0)) + 3.68 * 3.68 ) - Math.sqrt( (x - (1.706E-6 / 2.0)) * (x - (1.706E-6 / 2.0)) + 3.68 * 3.68 )) / 632.8E-9;
		//return Point.xByPixel(new CartesianCoordinateSystem(WIDTH, HEIGHT, -16 + x, x, -10, 10, 1, 1, true, false), 400);
		return x;
	}
	
	private static ComplexNumber fC(double x) {
		return ComplexNumber.pow(new ComplexNumber(x, 0.0), new ComplexNumber(1.0 / x, 0));
	}
	
	private void plotFunction(int color) {
		double x;
		Pixel last, current = null;
		
		for(int i = 0; i <= WIDTH; i++) {
			x = Point.xByPixel(this.system, i);
			last = current;
			current = null;
			try {
				current = new Pixel(this.system, x, f(x));
			} catch(IllegalArgumentException ignored) {}
			
			if(last != null && current != null) {
				try {
					this.system.drawLine(last.x, last.y, current.x, current.y, color);
				} catch(NullPointerException ignored) {}
			}
		}
	}
	
	private void renderFunction() {
		plotFunction(0xff0077ff);
		
		try {
			double x = Point.xByPixel(this.system, InputHandler.mouseX);
			double y = f(x);
			int yPixel = Pixel.yByPoint(this.system, y);
			this.system.drawDot(InputHandler.mouseX, yPixel, 3, 0xff0077ff);
		} catch(IllegalArgumentException ignored) {}
		
		this.screen.render(this.system, 0, 0);
	}
	
	private void plotFunctionInverse(int color) {
		double x;
		Pixel last, current = null;
		
		for(int i = 0; i <= WIDTH; i++) {
			x = Point.xByPixel(this.system, i);
			last = current;
			current = null;
			try {
				current = new Pixel(this.system, f(x), x);
			} catch(IllegalArgumentException ignored) {}
			
			if(last != null && current != null) {
				try {
					this.system.drawLine(last.x, last.y, current.x, current.y, color);
				} catch(NullPointerException ignored) {}
			}
		}
	}
	
	private void renderFunctionInverse() {
		plotFunctionInverse(0xffff0000);
		
		try {
			double x = Point.xByPixel(this.system, InputHandler.mouseX);
			double y = f(x);
			Pixel p = new Pixel(this.system, y, x);
			this.system.drawDot(p.x, p.y, 3, 0xffff0000);
		} catch(IllegalArgumentException ignored) {}
		
		this.screen.render(this.system, 0, 0);
	}
	
	private void plotComplexFunction(int colorR, int colorI) {
		double x;
		Pixel lastR, lastI, currentR = null, currentI = null;
		
		for(int i = 0; i <= WIDTH; i++) {
			x = Point.xByPixel(this.system, i);
			lastR = currentR;
			currentR = null;
			lastI = currentI;
			currentI = null;
			ComplexNumber y = fC(x);
			try {
				currentR = new Pixel(this.system, x, y.real());
			} catch(IllegalArgumentException ignored) {}
			try {
				currentI = new Pixel(this.system, x, y.imaginary());
			} catch(IllegalArgumentException ignored) {}
			
			if(lastR != null && currentR != null) {
				try {
					this.system.drawLine(lastR.x, lastR.y, currentR.x, currentR.y, colorR);
				} catch(NullPointerException ignored) {}
			}
			if(lastI != null && currentI != null) {
				try {
					this.system.drawLine(lastI.x, lastI.y, currentI.x, currentI.y, colorI);
				} catch(NullPointerException ignored) {}
			}
		}
	}
	
	private void renderComplexFunction() {
		plotComplexFunction(0xff0077ff, 0xffff0000);
		
		double x = Point.xByPixel(this.system, InputHandler.mouseX);
		ComplexNumber y = fC(x);
		try {
			int yPixel = Pixel.yByPoint(this.system, y.real());
			this.system.drawDot(InputHandler.mouseX, yPixel, 3, 0xff0077ff);
		} catch(IllegalArgumentException ignored) {}
		try {
			int yPixel = Pixel.yByPoint(this.system, y.imaginary());
			this.system.drawDot(InputHandler.mouseX, yPixel, 3, 0xffff0000);
		} catch(IllegalArgumentException ignored) {}
		
		this.screen.render(this.system, 0, 0);
	}
	
	private void plotComplexNumber(ComplexNumber c, int color) {
		Pixel p = new Pixel(this.system, c.real(), c.imaginary());
		this.system.drawDot(p.x, p.y, 3, color);
	}
	
	private void renderComplexPlane() {
		int n = InputHandler.wheelPosition;
		System.out.println(n);
		double a = -8.0;
		ComplexNumber c = new ComplexNumber(a, 0.0);
		ComplexNumber[] roots = c.root(n);
		double radius = Math.pow(c.abs(), 1.0 / n);
		double zoom = radius + radius / 5.0;
		CartesianCoordinateSystem old = this.system;
		try {
			this.system = new CartesianCoordinateSystem(WIDTH, HEIGHT, -zoom, zoom, -zoom, zoom, old.sclX, old.sclY, old.showAxes, old.showLabels);
		} catch(IllegalArgumentException ignored) {
		}
		this.system.clear(0xffffffff);
		this.system.drawAxes();
		if(n != 0) {
			this.system.plotCircle(0.0, 0.0, radius, 0xff555555);
		}
		for(ComplexNumber root : roots) {
			this.plotComplexNumber(root, -65536);
		}
		this.screen.render(this.system, 0, 0);
	}
	
	private void plotList(Point[] list, int color, boolean connect) {
		Pixel last = null;
		Pixel current = null;
		for(Point aList : list) {
			try {
				current = new Pixel(this.system, aList);
			} catch(IllegalArgumentException ignored) {}
			if(current != null) {
				try {
					this.system.drawDot(current.x, current.y, 2, color);
				} catch(NullPointerException ignored) {}
				if(connect && last != null) {
					this.system.drawLine(last.x, last.y, current.x, current.y, color);
				}
			}
			last = current;
			current = null;
		}
	}
	
	public static void main(String[] args) {
		Plotter.instance = new Plotter();
		Dimension dimension = new Dimension(WIDTH, HEIGHT);
		instance.setMaximumSize(dimension);
		instance.setMinimumSize(dimension);
		instance.setPreferredSize(dimension);
		instance.setSize(dimension);
		JFrame frame = new JFrame();
		frame.setTitle(NAME);
		frame.setResizable(false);
		frame.add(instance);
		frame.pack();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		instance.start();
		System.out.println(f(800));
	}
}
