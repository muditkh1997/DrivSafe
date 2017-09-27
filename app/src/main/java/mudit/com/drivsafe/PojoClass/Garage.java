package mudit.com.drivsafe.PojoClass;

/**
 * Created by admin on 9/27/2017.
 */

public class Garage {
    double distance;
    String lat;
    String lng;
    String name;
    String phone_no;
    String place_id;

    public Garage(double distance, String lat, String lng, String name, String phone_no, String place_id) {
        this.distance = distance;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.phone_no = phone_no;
        this.place_id = place_id;
    }

    public Garage() {
    }

    public double getDistance() {
        return distance;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getName() {
        return name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getPlace_id() {
        return place_id;
    }
}
