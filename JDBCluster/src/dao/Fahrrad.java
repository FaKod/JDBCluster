package dao;

public class Fahrrad extends Fahrzeug {

	private long id;
	private double durchmesser;
	private String colorShading;
	private String color;
	private String colorType;
	
	public double getDurchmesser() {
		return durchmesser;
	}
	public void setDurchmesser(double durchmesser) {
		this.durchmesser = durchmesser;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getColorType() {
		return colorType;
	}
	public void setColorType(String colorType) {
		this.colorType = colorType;
	}
	public String getColorShading() {
		return colorShading;
	}
	public void setColorShading(String colorShading) {
		this.colorShading = colorShading;
	}

	
}
