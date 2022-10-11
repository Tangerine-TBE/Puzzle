package com.xinlan.imageeditlibrary.editimage.model;

public class RatioItem {
	private String text;
	private Float ratio;
	private int index;
	private int iconUnSelected;
	private int iconSelected;

	public RatioItem(String text, Float ratio) {
		super();
		this.text = text;
		this.ratio = ratio;
	}

	public int getIconUnSelected() {
		return iconUnSelected;
	}

	public void setIconUnSelected(int iconUnSelected) {
		this.iconUnSelected = iconUnSelected;
	}

	public int getIconSelected() {
		return iconSelected;
	}

	public void setIconSelected(int iconSelected) {
		this.iconSelected = iconSelected;
	}

	public RatioItem(String text, Float ratio, int iconUnSelected, int iconSelected) {
		this.text = text;
		this.ratio = ratio;
		this.iconSelected = iconSelected;
		this.iconUnSelected = iconUnSelected;
	}

	public int getIcon() {
		return iconUnSelected;
	}

	public void setIcon(int icon) {
		this.iconUnSelected = icon;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Float getRatio() {
		return ratio;
	}

	public void setRatio(Float ratio) {
		this.ratio = ratio;
	}
	

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}// end class
