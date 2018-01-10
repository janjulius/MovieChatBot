package chatbot;

import Util.Settings;
import com.google.maps.GeoApiContext;

public final class Singleton {
    private static final Singleton INSTANCE = new Singleton();

    private Singleton() {}

    public static Singleton getInstance() {
        return INSTANCE;
    }
}