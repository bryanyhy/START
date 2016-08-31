package ambiesoft.start.model.utility;

import android.app.Activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Bryanyhy on 15/8/2016.
 */
// class for handling JSON functions, mainly for the Melbourne Public Artwork dataset
public class JSON {

    // dataset JSON file for artwork in City of Melbourne, saved in assets folder
    private static final String MELBOURNE_PUBLIC_ARTWORK = "MelbournePublicArtwork.json";
    // dataset JSON file for Sensor location in City of Melbourne, saved in assets folder
    private static final String PED_SENSOR_LOCATION = "PedestrianSensorLocation.json";
    // dataset JSON file for Ped Count in City of Melbourne, saved in assets folder
    private static final ArrayList<String> PED_COUNTER = new ArrayList<String>() {{
        add("PedestrianCounter_MON.json");
        add("PedestrianCounter_TUE.json");
        add("PedestrianCounter_WED.json");
        add("PedestrianCounter_THU.json");
        add("PedestrianCounter_FRI.json");
        add("PedestrianCounter_SAT.json");
        add("PedestrianCounter_SUN.json");
    }};

    // load to JSON file from asset, return as a String
    public static String loadArtworkJSONFromAsset(Activity activity) {
        String json = null;
        try {
            InputStream is = activity.getAssets().open(MELBOURNE_PUBLIC_ARTWORK);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static String loadSensorLocationJSONFromAsset(Activity activity) {
        String json = null;
        try {
            InputStream is = activity.getAssets().open(PED_SENSOR_LOCATION);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static String loadPedCountJSONFromAsset(Activity activity, int day) {
        String json = null;
        try {
            InputStream is = activity.getAssets().open(PED_COUNTER.get(day));
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
