package edu.htl3r.schoolplanner.gui.basti;

import java.util.concurrent.LinkedBlockingQueue;

import edu.htl3r.schoolplanner.DateTime;

public class BlockingDownloadQueue extends LinkedBlockingQueue<DateTime[]> {

	public static final DateTime[] INTERRUPT = { new DateTime(), new DateTime(), new DateTime(), new DateTime() };

	private boolean isInterrupted = false;

	public void interrupt() {
		if (!isInterrupted) {
			this.add(INTERRUPT);
			isInterrupted = true;
		}
	}

	public boolean isInterrupted() {
		return isInterrupted;
	}

}
