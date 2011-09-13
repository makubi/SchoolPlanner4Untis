package edu.htl3r.schoolplanner.gui.basti;


abstract public class TransferObject {

	public static final int INTERRUPT = 1337;
	public static final int NORMAL = 7331;
	private int id; 
	
	public void setID(int id){
		this.id = id; 
	}
	
	public boolean isBomb(){
		return (id == INTERRUPT) ? true : false; 
	}
	
	
}
