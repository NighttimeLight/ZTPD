import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;

import java.util.Objects;

public class ProstyListener implements UpdateListener {
    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement epStatement, EPRuntime epRuntime) {
        String lastYear = null;
        if (newEvents != null) {
            for (int i = 0; i < newEvents.length; i++) {
                String yr = "" + newEvents[i].get("_year");
                String val = "" + newEvents[i].get("kursZamkniecia");
                String count = "" + newEvents[i].get("liczba");
                if (!Objects.equals(yr, lastYear)) {
                    System.out.println("===== YEAR: " + yr + " =====");
                    lastYear = yr;
                }
                System.out.println("kursZamkniecia: " + val + " - " + count + " razy");
            }
        }
    }
}
