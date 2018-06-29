package liu.com.autoregisteravtivity;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

public class App extends Application {

    public App(){
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
