package reducedimension;

import org.jfree.chart.ChartPanel;
import panel.ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSA;

public abstract class ReduceDimensionToolBox{

    ChartPanel resultChartPanel;

    public abstract int getProcessStatus();

    public abstract ChartPanel excuteReduceDimesion(ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSA reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSA, String resultPath, String lastMethod, String matlabPath) throws Exception;
}
