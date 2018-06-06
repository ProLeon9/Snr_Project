package reducenoice;

import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYDataset;
import panel.ReduceNoiceKalmanFilter;
import tools.CommonFunctions;
import tools.graph.chart.XYLineChart;
import tools.graph.util.ChartUtils;

import java.io.*;

public class KalmanFilter extends ReduceNoiceToolBox{

    //曲线相关参数
    private int attackSampleNum;
    private int attackSampleStart;
    private int attackCurveStart;
    private int attackCurveNum;
    //卡尔曼滤波相关参数
    private double KalmenValue;          //kalmenvalue,t-1 input ,t output;
    private double A;                    // const in model;the state transition matrix modle which is applied to the previous true state vector
    private double H;                    // const in model;obeservation matrix model which maps the true state vector into the measurement vector
    private double Q;                    // const in model;the covariance while we assume the process noise is drawn from a zero mean Gaussian distribution
    private double R;                    // const in model;the covariance while we assume the measurement noise is drawn from a zero mean Gaussian distribution
    private double P;                    // MSE
    private double KG;                   // kalmen gian
    //显示进度使用
    private int currentStatus;


    public ChartPanel excuteReduceNoice(ReduceNoiceKalmanFilter reduceNoiceKalmanFilter, String resultPath, String lastMethod) throws IOException{
        iniKalmanFilter();
        getParametersFromPanel(reduceNoiceKalmanFilter);
        BufferedReader curveReader = new BufferedReader(new FileReader(resultPath+"\\"+lastMethod+".txt"));
        BufferedWriter resultWriter = new BufferedWriter(new FileWriter(resultPath+"\\KalmanFilter.txt"));
        //剔除不用的Curve
        for(int i = 1; i <= this.attackCurveStart-1; i++){
            curveReader.readLine();
        }
        //执行降噪
        double[] curve, result, originalTrace = null, newTrace = null;
        for(int i = 1; i <= this.attackCurveNum; i++){
            curve = CommonFunctions.powerCut(CommonFunctions.doubleStringToDoubleArray(curveReader.readLine()), this.attackSampleNum, this.attackSampleStart-1);
            result = excuteKalmanFilter(curve);
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
        super.resultChartPanel = xyLineChart.getChart("KalmanFilter Result", "Sample", "Value", xyDataset, true);
        return super.resultChartPanel;
    }

    @Override
    public int getProcessStatus(){
        if(currentStatus < this.attackCurveNum){
            return currentStatus;
        }
        else{
            return -1;
        }
    }

    private void getParametersFromPanel(ReduceNoiceKalmanFilter reduceNoiceKalmanFilter){
        this.attackCurveStart = Integer.parseInt(reduceNoiceKalmanFilter.curve_start_textfiled.getText());
        this.attackCurveNum = Integer.parseInt(reduceNoiceKalmanFilter.curve_number_textfiled.getText());
        this.attackSampleStart = Integer.parseInt(reduceNoiceKalmanFilter.sample_start_textfiled.getText());
        this.attackSampleNum = Integer.parseInt(reduceNoiceKalmanFilter.sample_number_textfiled.getText());
        this.Q = Double.parseDouble(reduceNoiceKalmanFilter.QValue_textfield.getText());
        this.R = Double.parseDouble(reduceNoiceKalmanFilter.RValue_textfield.getText());
    }

    private double[] excuteKalmanFilter(double[] curve){
        double[] result = new double[curve.length];
        for(int i = 0; i < curve.length; i++){
            result[i] = this.basicfliter(curve[i]);
        }
        return result;
    }

    private void iniKalmanFilter(){
        this.A = 1;
        this.H = 1;
        this.P = 10;               //any value but 0
        this.KalmenValue = 0;      //any value,generally 0
    }

    private double basicfliter(double measurement){
        double predictValue = KalmenValue;
        double predictP = A*A*P+Q;
        this.KG = predictP*H/(H*predictP*H+R);
        this.KalmenValue = predictValue+KG*(measurement-H*predictValue);
        this.P = (1-this.KG*H)*predictP;
        return KalmenValue;
    }
}
