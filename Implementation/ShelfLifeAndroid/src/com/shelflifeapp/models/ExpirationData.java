package com.shelflifeapp.models;

public class ExpirationData 
{
	private final int NO_DATA = -1;
	
	private int shelfOpened = NO_DATA;
	private int shelfUnopened = NO_DATA;
	
	private int fridgeOpened = NO_DATA;
	private int fridgeUnopened = NO_DATA;
	
	private int freezerOpened = NO_DATA;
	private int freezerUnopened = NO_DATA;
	
	public ExpirationData(int shelfOpened, int shelfUnopened, int fridgeOpened, int fridgeUnopened, int freezerOpened, int freezerUnopened)
	{
		this.setShelfOpened(shelfOpened);
		this.setShelfUnopened(shelfUnopened);
		
		this.setFridgeOpened(fridgeOpened);
		this.setFridgeUnopened(fridgeUnopened);
		
		this.setFreezerOpened(freezerOpened);
		this.setFreezerUnopened(freezerUnopened);
	}

	public int getShelfOpened() {
		return shelfOpened;
	}

	public void setShelfOpened(int shelfOpened) {
		this.shelfOpened = shelfOpened;
	}

	public int getShelfUnopened() {
		return shelfUnopened;
	}

	public void setShelfUnopened(int shelfUnopened) {
		this.shelfUnopened = shelfUnopened;
	}

	public int getFridgeOpened() {
		return fridgeOpened;
	}

	public void setFridgeOpened(int fridgeOpened) {
		this.fridgeOpened = fridgeOpened;
	}

	public int getFridgeUnopened() {
		return fridgeUnopened;
	}

	public void setFridgeUnopened(int fridgeUnopened) {
		this.fridgeUnopened = fridgeUnopened;
	}

	public int getFreezerOpened() {
		return freezerOpened;
	}

	public void setFreezerOpened(int freezerOpened) {
		this.freezerOpened = freezerOpened;
	}

	public int getFreezerUnopened() {
		return freezerUnopened;
	}

	public void setFreezerUnopened(int freezerUnopened) {
		this.freezerUnopened = freezerUnopened;
	}
	
}