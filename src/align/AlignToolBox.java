package align;

import org.jfree.chart.ChartPanel;

import java.util.List;


public abstract class AlignToolBox{

    ChartPanel resultChartPanel;

    public abstract int getProcessStatus();

    public abstract List<double[]> getSNRAndPI();
}
