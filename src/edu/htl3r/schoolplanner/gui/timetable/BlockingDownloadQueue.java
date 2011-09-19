package edu.htl3r.schoolplanner.gui.timetable;

import java.util.concurrent.LinkedBlockingQueue;

public class BlockingDownloadQueue extends LinkedBlockingQueue<TransferObject> {

	
	private static final long serialVersionUID = 2335011325403392056L;
	private boolean isInterrupted = false;

	public void interrupt() {
		if (!isInterrupted) {
			add(new InterruptTranferObject());
			isInterrupted = true;
		}
	}

	public boolean isInterrupted() {
		return isInterrupted;
	}

}
