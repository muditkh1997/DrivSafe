package mudit.com.drivsafe.PojoClass;

/**
 * Created by admin on 9/26/2017.
 */

public class Info {
    private String pressure;
    private String temperature;
    private String date;
    private String time;

    public Info(String pressure, String temperature, String date, String time) {
        this.pressure = pressure;
        this.temperature = temperature;
        this.date = date;
        this.time = time;
    }

    public Info() {

    }

    public String getPressure() {
        return pressure;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
