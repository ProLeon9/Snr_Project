package reducedimension;

import org.jfree.chart.ChartPanel;
import panel.ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA;
import tools.PreProcess;


public abstract class ReduceDimensionToolBox extends PreProcess{
    public abstract ChartPanel excuteReduceDimesion(ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA, String resultPath, String lastMethod, String matlabPath) throws Exception;

    public abstract double[] getSNR();

    public abstract double[] getPI();
}
