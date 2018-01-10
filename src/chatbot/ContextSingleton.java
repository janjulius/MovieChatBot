package chatbot;

import Util.Settings;
import com.google.maps.GeoApiContext;

public final class ContextSingleton {
    private static final ContextSingleton INSTANCE = new ContextSingleton();

    ContextSingleton() {
    }

    public static ContextSingleton getInstance() {
        return INSTANCE;
    }
}