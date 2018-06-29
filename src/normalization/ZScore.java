package normalization;

import evaluation.PI;
import evaluation.SNR;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYDataset;
import panel.ReduceNoiceFFTAndNormalizationZScore;
import tools.CommonFunctions;
import tools.Curve;
import tools.graph.chart.XYLineChart;
import tools.graph.util.ChartUtils;

import java.io.*;

import static tools.CommonFunctions.*;


public class ZScore extends NormalizationToolBox{
    //曲线相关参数
    private Curve curve;
    //显示进度使用
    private int currentStatus;


    ZScore(){
        this.curve = new Curve();
    }

    public ChartPanel excuteNormalization(ReduceNoiceFFTAndNormalizationZScore reduceNoiceFFTAndNormalizationZScore, String resultPath, String lastMethod) throws IOException{
        getParametersFromPanel(reduceNoiceFFTAndNormalizationZScore);
        BufferedReader curveReader = new BufferedReader(new FileReader(resultPath+"\\"+lastMethod+".txt"));
        BufferedWriter resultWriter = new BufferedWriter(new FileWriter(resultPath+"\\Z-Score.txt"));
        BufferedReader plainReader = new BufferedReader(new FileReader(resultPath+"\\"+"text_in.txt"));
        //剔除不用的Curve
        for(int i = 1; i <= this.curve.attackCurveStart-1; i++){
            curveReader.readLine();
            plainReader.readLine();
        }
        //执行降噪
        double[] curve, result, originalTrace = null, newTrace = null;
        int[] plain;
        for(int i = 1; i <= this.curve.attackCurveNum; i++){
            curve = CommonFunctions.powerCut(CommonFunctions.doubleStringToDoubleArray(curveReader.readLine()), this.curve.attackSampleNum, this.curve.attackSampleStart-1);
            result = excuteZScore(curve);
            plain = hexStringTOIntArray(plainReader.readLine());
            if(i == 1){
                snr = new SNR(curve, result);
                pi = new PI(curve, result, this.curve.attackCurveNum);
            }
            snr.excuteSNR(curve, result, plain, 0);  //TODO:index可以指定为参数
            pi.excutePI(curve, result, plain, 0); //TODO:index可以指定为参数
            resultWriter.append(CommonFunctions.doubleArrayToString(result));
            if(i == 50){
                originalTrace = curve;
                newTrace = result;
            }
            currentStatus = i-1;
        }
        curveReader.close();
        resultWriter.close();
        plainReader.close();
        this.snrResult = snr.getMaxSNR();
        this.piResult = pi.returnPI();

        //执行作图
        int[] xris = new int[this.curve.attackSampleNum];
        for(int i = 0; i <= xris.length-1; i++){
            xris[i] = i+this.curve.attackSampleStart;
        }
        double[][] yris = new double[2][this.curve.attackSampleNum];

        assert originalTrace != null;
        assert newTrace != null;
        System.arraycopy(originalTrace, 0, yris[0], 0, xris.length);
        System.arraycopy(newTrace, 0, yris[1], 0, xris.length);
        XYDataset xyDataset = ChartUtils.createXYSeries(2, xris, yris, new String[]{"original_trace", "new_trace"});
        XYLineChart xyLineChart = new XYLineChart();
        super.resultChartPanel = xyLineChart.getChart("Z-Score Result", "Sample", "Value", xyDataset, true);
        currentStatus += 1; //为了保证执行线程和更新线程在同一个处理方式时保持同步！！！
        return super.resultChartPanel;
    }

    @Override
    public int getProcessStatus(){
        if(currentStatus < this.curve.attackCurveNum){
            return currentStatus;
        }
        else{
            return -1;
        }
    }

    private void getParametersFromPanel(ReduceNoiceFFTAndNormalizationZScore reduceNoiceFFTAndNormalizationZScore){
        this.curve.attackCurveStart = Integer.parseInt(reduceNoiceFFTAndNormalizationZScore.curve_start_textfiled.getText());
        this.curve.attackCurveNum = Integer.parseInt(reduceNoiceFFTAndNormalizationZScore.curve_number_textfiled.getText());
        this.curve.attackSampleStart = Integer.parseInt(reduceNoiceFFTAndNormalizationZScore.sample_start_textfiled.getText());
        this.curve.attackSampleNum = Integer.parseInt(reduceNoiceFFTAndNormalizationZScore.sample_number_textfiled.getText());
    }

    private double[] excuteZScore(double[] trace){
        double standard_diviation = getStandardDiviation(trace);
        double average = getAverage(trace);
        double[] result = new double[trace.length];
        for(int i = 0; i <= trace.length-1; i++){
            result[i] = (trace[i]-average)/standard_diviation*standard_diviation;
        }
        return result;
    }
}
