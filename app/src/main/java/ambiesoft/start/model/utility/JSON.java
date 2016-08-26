package ambiesoft.start.model.utility;

import android.app.Activity;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Bryanyhy on 15/8/2016.
 */
// class for handling JSON functions, mainly for the Melbourne Public Artwork dataset
public class JSON {

    // dataset JSON file for artwork in City of Melbourne, saved in assets folder
    private static final String MELBOURNE_PUBLIC_ARTWORK = "MelbournePublicArtwork.json";

    // load to JSON file from asset, return as a String
    public static String loadJSONFromAsset(Activity activity) {
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

}
