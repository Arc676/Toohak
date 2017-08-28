package backend;

public class Updater implements Runnable {
	
	private Updatable updatable;

	public Updater(Updatable comp) {
		this.updatable = comp;
	}
	
	public void run() {
		long lastUpdate = System.nanoTime();
		double delta = 0;
		while (updatable.stillRunning()) {
			long now = System.nanoTime();
			delta += (now - lastUpdate) / 1000000000;
			if (delta >= 0.49) {
				updatable.update();
			}
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}