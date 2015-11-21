package pt.ulisboa.tecnico.meic.sirs.securesms;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Table;

/**
 * Created by diogopainho on 21/11/15.
 */
public class SecureSMSApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);

    }
}
