package tools;

import evaluation.PI;
import evaluation.SNR;
import org.jfree.chart.ChartPanel;

import java.util.ArrayList;
import java.util.List;

public abstract class PreProcess{
    //SNR使用
    protected SNR snr;
    protected double[] snrResult;
    //PI使用
    protected PI pi;
    protected double[] piResult;

    protected ChartPanel resultChartPanel;


    public abstract int getProcessStatus();

    public List<double[]> getSNRAndPI(){
        List<double[]> result = new ArrayList<>();
        result.add(snr.getBeforeSNR());
        result.add(snr.getAfterSNR());
        result.add(pi.getBeforePI());
        result.add(pi.getAfterPI());
        return result;
    }

    public double[] getSNR(){
        return this.snrResult;
    }

    public double[] getPI(){
        return this.piResult;
    }
}
