package normalization;

import org.jfree.chart.ChartPanel;
import panel.ReduceNoiceFFTAndNormalizationZScore;

import java.io.IOException;
import java.util.List;

public abstract class NormalizationToolBox{

    ChartPanel resultChartPanel;

    public abstract int getProcessStatus();

    public abstract ChartPanel excuteNormalization(ReduceNoiceFFTAndNormalizationZScore reduceNoiceFFTAndNormalizationZScore, String resultPath, String lastMethod) throws IOException;

    public abstract List<double[]> getSNRAndPI();
}
