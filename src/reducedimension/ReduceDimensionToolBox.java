package reducedimension;

import org.jfree.chart.ChartPanel;
import panel.ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA;

import java.util.List;

public abstract class ReduceDimensionToolBox{

    ChartPanel resultChartPanel;

    public abstract int getProcessStatus();

    public abstract ChartPanel excuteReduceDimesion(ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA, String resultPath, String lastMethod, String matlabPath) throws Exception;

    public abstract double[] getSNR();

    public abstract double[] getPI();

    public abstract List<double[]> getSNRAndPI();
}
