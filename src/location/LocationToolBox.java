package location;

import org.jfree.chart.ChartPanel;
import panel.LocationPanel;

import java.io.IOException;

public abstract class LocationToolBox{

    public ChartPanel resultChartPanel;

    public abstract ChartPanel excuteLocation(LocationPanel locationPanel, String resultPath) throws IOException;

    public abstract int getProcessStatus();
}
