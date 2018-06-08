package normalization;

import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYDataset;
import panel.ReduceNoiceFFTAndNormalizationZScore;
import tools.CommonFunctions;
import tools.graph.chart.XYLineChart;
import tools.graph.util.ChartUtils;

import java.io.*;

import static tools.CommonFunctions.getAverage;
import static tools.CommonFunctions.getStandardDiviation;

public class ZScore extends NormalizationToolBox{
    //曲线相关参数
    private int attackSampleNum;
    private int attackSampleStart;
    private int attackCurveStart;
    private int attackCurveNum;
    //显示进度使用
    private int currentStatus;


    public ChartPanel excuteNormalization(ReduceNoiceFFTAndNormalizationZScore reduceNoiceFFTAndNormalizationZScore, String resultPath, String lastMethod) throws IOException{
        getParametersFromPanel(reduceNoiceFFTAndNormalizationZScore);
        BufferedReader curveReader = new BufferedReader(new FileReader(resultPath+"\\"+lastMethod+".txt"));
        BufferedWriter resultWriter = new BufferedWriter(new FileWriter(resultPath+"\\Z-Score.txt"));
        //剔除不用的Curve
        for(int i = 1; i <= this.attackCurveStart-1; i++){
            curveReader.readLine();
        }
        //执行降噪
        double[] curve, result, originalTrace=null, newTrace=null;
        for(int i = 1; i <= this.attackCurveNum; i++){
            curve = CommonFunctions.powerCut(CommonFunctions.doubleStringToDoubleArray(curveReader.readLine()), this.attackSampleNum, this.attackSampleStart-1);
            result = excuteZScore(curve);
            resultWriter.append(CommonFunctions.doubleArrayToString(result));
            if(i == 50){
                originalTrace = curve;
                newTrace = result;
            }
            currentStatus = i;
        }
        curveReader.close();
        resultWriter.close();
        //执行作图
        int[] xris = new int[this.attackSampleNum];
        for(int i = 0; i <= xris.length-1; i++){
            xris[i] = i+this.attackSampleStart;
        }
        double[][] yris = new double[2][this.attackSampleNum];

        assert originalTrace != null;
        assert newTrace != null;
        System.arraycopy(originalTrace, 0, yris[0], 0, xris.length);
        System.arraycopy(newTrace, 0, yris[1], 0, xris.length);
        XYDataset xyDataset = ChartUtils.createXYSeries(2, xris, yris, new String[]{"original_trace", "new_trace"});
        XYLineChart xyLineChart = new XYLineChart();
        super.resultChartPanel = xyLineChart.getChart("Z-Score Result", "Sample", "Value", xyDataset, true);
        return super.resultChartPanel;
    }
    @Override
    public int getProcessStatus(){
        if(currentStatus < this.attackCurveNum)
            return currentStatus;
        else
            return -1;
    }

    private void getParametersFromPanel(ReduceNoiceFFTAndNormalizationZScore reduceNoiceFFTAndNormalizationZScore){
        this.attackCurveStart = Integer.parseInt(reduceNoiceFFTAndNormalizationZScore.curve_start_textfiled.getText());
        this.attackCurveNum = Integer.parseInt(reduceNoiceFFTAndNormalizationZScore.curve_number_textfiled.getText());
        this.attackSampleStart = Integer.parseInt(reduceNoiceFFTAndNormalizationZScore.sample_start_textfiled.getText());
        this.attackSampleNum = Integer.parseInt(reduceNoiceFFTAndNormalizationZScore.sample_number_textfiled.getText());
    }

    private double[] excuteZScore(double[] trace){
        double standard_diviation = getStandardDiviation(trace);
        double average = getAverage(trace);
        double[] result = new double[trace.length];
        for(int i=0;i<=trace.length-1;i++){
            result[i] = (result[i] - average) / standard_diviation;
        }
        return result;
    }
}
