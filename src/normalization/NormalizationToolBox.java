package normalization;

import org.jfree.chart.ChartPanel;
import panel.ReduceNoiceFFTAndNormalizationZScore;
import tools.PreProcess;

import java.io.IOException;


public abstract class NormalizationToolBox extends PreProcess{
        public abstract ChartPanel excuteNormalization(ReduceNoiceFFTAndNormalizationZScore reduceNoiceFFTAndNormalizationZScore, String resultPath, String lastMethod) throws IOException;
}
