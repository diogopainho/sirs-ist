package pt.ulisboa.tecnico.meic.sirs.securesms;

import com.squareup.otto.Bus;

/**
 * Created by diogopainho on 29/11/15.
 */
public class BusStation {
    private static Bus bus = new Bus();

    public static Bus getBus() {
        return bus;
    }
}
