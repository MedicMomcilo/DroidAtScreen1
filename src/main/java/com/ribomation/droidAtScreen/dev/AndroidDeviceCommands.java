package com.ribomation.droidAtScreen.dev;

import com.ribomation.droidAtScreen.gui.DeviceFrame;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;

public class AndroidDeviceCommands extends MouseAdapter implements KeyEventDispatcher {
	private DeviceFrame deviceFrame;
	private Point startPoint;
	private Logger log;

	public AndroidDeviceCommands(DeviceFrame deviceFrame) {
		log = Logger.getLogger(this.getClass().getName() + ":" + deviceFrame.getDevice().getName());
		this.deviceFrame = deviceFrame;
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(this);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_TYPED) {
			sendKey(e.getKeyChar());
		}
		return false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		startPoint = getScaledPoint(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Point point = getScaledPoint(e);
		if (startPoint.equals(point)) {
			switch (e.getButton()) {
				case MouseEvent.BUTTON1:
					tap(point);
					break;
				case MouseEvent.BUTTON2:
					sendKey(KeyEvent.VK_CONTEXT_MENU);
					break;
				case MouseEvent.BUTTON3:
					tapAndHold(point);
					break;
			}
		} else {
			swipe(startPoint, point);
		}
	}

	private Point getScaledPoint(MouseEvent e) {
		Point p = e.getPoint();
		p = new Point(
				(int) (p.getX() * 100) / deviceFrame.getScale(),
				(int) (p.getY() * 100) / deviceFrame.getScale()
		);
		return p;
	}

	private void tap(Point p) {
		String cmd = String.format(Locale.ENGLISH, "input tap %s %s", p.getX(), p.getY());
		deviceFrame.getDevice().sendCommand(cmd);
	}

	private void tapAndHold(Point p) {
		String cmd = String.format(Locale.ENGLISH,
				"input swipe %s %s %s %s 1000", p.getX(), p.getY(), p.getX(), p.getY());
		deviceFrame.getDevice().sendCommand(cmd);
	}

	private void swipe(Point from, Point to) {
		String cmd = String.format(Locale.ENGLISH,
				"input swipe %s %s %s %s", from.getX(), from.getY(), to.getX(), to.getY());
		deviceFrame.getDevice().sendCommand(cmd);
	}

	private void sendKey(int key) {
		int eventKey = 0;
		switch (key) {
			case KeyEvent.VK_CONTEXT_MENU:
				eventKey = AndroidKeyEvent.KEYCODE_MENU;
				break;
			case KeyEvent.VK_ESCAPE:
				eventKey = AndroidKeyEvent.KEYCODE_BACK;
				break;
		}
		if (eventKey != 0) {
			String cmd = String.format(Locale.ENGLISH, "input keyevent %s", eventKey);
			deviceFrame.getDevice().sendCommand(cmd);
		}
	}
}
