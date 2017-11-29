package mudit.com.drivsafe.PojoClass;

/**
 * Created by admin on 11/27/2017.
 */

public class Road {
    float speed;
    float distance;
    float pressure;
    float temperature;

    public Road() {
    }

    public Road(float speed, float distance, float pressure, float temperature) {
        this.speed = speed;
        this.distance = distance;
        this.pressure = pressure;
        this.temperature = temperature;
    }



    public float getSpeed() {
        return speed;
    }

    public float getDistance() {
        return distance;
    }

    public float getPressure() {
        return pressure;
    }

    public float getTemperature() {
        return temperature;
    }
}
