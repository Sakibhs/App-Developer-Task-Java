package net.appcolors.babulandtask;

public class WeatherModel {
   private String cityName, status, temperature;

    public WeatherModel(String cityName, String status, String temperature) {
        this.cityName = cityName;
        this.status = status;
        this.temperature = temperature;
    }

    public String getCityName() {
        return cityName;
    }

    public String getStatus() {
        return status;
    }

    public String getTemperature() {
        return temperature;
    }
}
