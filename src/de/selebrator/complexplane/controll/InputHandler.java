package de.selebrator.complexplane.controll;

import java.awt.*;
import java.awt.event.*;

public class InputHandler implements MouseMotionListener, MouseWheelListener, MouseListener, KeyListener {
	public static boolean mouse;
	public static boolean mouseLast;
	public static int mouseX;
	public static int mouseY;
	public static int mouseLastX;
	public static int mouseLastY;
	public static int wheelPosition;
	public static int wheelRotation;
	
	
	
	public static boolean[] keys = new boolean[1024];
	public static boolean[] keysLast = new boolean[1024];
	
	public InputHandler(final Canvas canvas) {
		canvas.addMouseMotionListener(this);
		canvas.addMouseWheelListener(this);
		canvas.addMouseListener(this);
		canvas.addKeyListener(this);
	}
	
	public static void tick() {
		keysLast = keys.clone();
		mouseLast = mouse;
		mouseLastX = mouseX;
		mouseLastY = mouseY;
		wheelRotation = 0;
	}
	
	public static boolean isMouseHeld() {
		return mouse && mouseLast;
	}
	
	public static boolean isMousePressed() {
		return mouse && !mouseLast;
	}
	
	public static boolean isMouseReleased() {
		return !mouse && mouseLast;
	}
	
	public static boolean isKey(int keyCode) {
		return keys[keyCode];
	}
	
	public static boolean isKeyPressed(int keyCode) {
		return keys[keyCode] && !keysLast[keyCode];
	}
	
	public static boolean isKeyReleased(int keyCode) {
		return !keys[keyCode] && keysLast[keyCode];
	}
	
	@Override
	public void mouseDragged(final MouseEvent e) {
		InputHandler.mouseX = e.getX();
		InputHandler.mouseY = e.getY();
	}
	
	@Override
	public void mouseMoved(final MouseEvent e) {
		InputHandler.mouseX = e.getX();
		InputHandler.mouseY = e.getY();
	}
	
	@Override
	public void mouseWheelMoved(final MouseWheelEvent e) {
		wheelRotation += e.getWheelRotation() * e.getScrollAmount();
		InputHandler.wheelPosition += e.getWheelRotation();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		mouse = true;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		mouse = false;
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	
	}
	
	@Override
	public void keyTyped(KeyEvent event) {
	
	}
	
	@Override
	public void keyPressed(KeyEvent event) {
		keys[event.getKeyCode()] = true;
	}
	
	@Override
	public void keyReleased(KeyEvent event) {
		keys[event.getKeyCode()] = false;
	}
}
