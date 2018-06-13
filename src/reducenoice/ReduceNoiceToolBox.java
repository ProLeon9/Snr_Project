package reducenoice;

import org.jfree.chart.ChartPanel;

import java.util.ArrayList;
import java.util.List;

public abstract class ReduceNoiceToolBox{

    ChartPanel resultChartPanel;

    public abstract int getProcessStatus();

    public abstract List<double[]> getSNRAndPI();
}
