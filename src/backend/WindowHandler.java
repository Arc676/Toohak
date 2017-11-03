package backend;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import main.ServerView;

public class WindowHandler implements WindowListener {

	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}

	public void windowClosing(WindowEvent e) {
		if (e.getWindow() instanceof ServerView) {
			((ServerView) e.getWindow()).closeServer();
		}
	}

}