package edu.htl3r.schoolplanner.gui.timetable;

import edu.htl3r.schoolplanner.DateTime;

public class InputTransferObject extends TransferObject{
	private DateTime date;
	private int pos;
	
	public InputTransferObject(DateTime d, int pos) {
		setID(NORMAL);
		this.date = d;
		this.pos = pos;
	}

	public DateTime getDate() {
		return date;
	}

	public int getPos() {
		return pos;
	}
	
	
}
