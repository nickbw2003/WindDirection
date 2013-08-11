package com.nickbw2003.winddirection.data;

public class WeatherInfo {

	private String LocationName;
	private Double WindDirection;
	private Double WindSpeed;
	private Double Longitude;
	private Double Latitude;
	
	public String getLocationName() {
		return LocationName;
	}
	
	public void setLocationName(String LocationName) {
		this.LocationName = LocationName;
	}
	
	public Double getWindDirection() {
		return WindDirection;
	}
	
	public void setWindDirection(Double WindDirection) {
		this.WindDirection = WindDirection;
	}
	
	public Double getWindSpeed() {
		return WindSpeed;
	}
	
	public void setWindSpeed(Double WindSpeed) {
		this.WindSpeed = WindSpeed;
	}
	
	public Double getLongitude() {
		return Longitude;
	}
	
	public void setLongitude(Double Longitude) {
		this.Longitude = Longitude;
	}
	
	public Double getLatitude() {
		return Latitude;
	}
	
	public void setLatitude(Double Latitude) {
		this.Latitude = Latitude;
	}

	public String getWindDirectionString()
	{
		if (WindDirection > 348.75 && WindDirection < 11.25)
		{
			return "N";
		}
		else if (WindDirection > 11.25 && WindDirection < 33.75)
		{
			return "NNE";
		}
		else if (WindDirection > 33.75 && WindDirection < 56.25)
		{
			return "NE";
		}
		else if (WindDirection > 56.25 && WindDirection < 78.75)
		{
			return "ENE";
		}
		else if (WindDirection > 78.75 && WindDirection < 101.25)
		{
			return "E";
		}
		else if (WindDirection > 101.25 && WindDirection < 123.75)
		{
			return "ESE";
		}
		else if (WindDirection > 123.75 && WindDirection < 146.25)
		{
			return "SE";
		}
		else if (WindDirection > 146.25 && WindDirection < 168.75)
		{
			return "SSE";
		}
		else if (WindDirection > 168.75 && WindDirection < 191.25)
		{
			return "S";
		}
		else if (WindDirection > 191.25 && WindDirection < 213.75)
		{
			return "SSW";
		}
		else if (WindDirection > 213.75 && WindDirection < 236.25)
		{
			return "SW";
		}
		else if (WindDirection > 236.25 && WindDirection < 258.75)
		{
			return "WSW";
		}
		else if (WindDirection > 258.75 && WindDirection < 281.25)
		{
			return "W";
		}
		else if (WindDirection > 281.25 && WindDirection < 303.75)
		{
			return "WNW";
		}
		else if (WindDirection > 303.75 && WindDirection < 326.25)
		{
			return "NW";
		}
		else if (WindDirection > 326.25 && WindDirection < 348.75)
		{
			return "NNW";
		}
		else
		{
			return "Undefined";
		}
	}
	
}
