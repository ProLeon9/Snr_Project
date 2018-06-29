package reducenoice;

import evaluation.PI;
import evaluation.SNR;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYDataset;
import panel.ReduceNoiceKalmanFilter;
import tools.CommonFunctions;
import tools.Curve;
import tools.graph.chart.XYLineChart;
import tools.graph.util.ChartUtils;

import java.io.*;

import static tools.CommonFunctions.hexStringTOIntArray;


public class KalmanFilter extends ReduceNoiceToolBox{
    //曲线相关参数
    private Curve curve;
    //卡尔曼滤波相关参数
    private double KalmenValue;          //kalmenvalue,t-1 input ,t output;
    private double A;                    // const in model;the state transition matrix modle which is applied to the previous true state vector
    private double H;                    // const in model;obeservation matrix model which maps the true state vector into the measurement vector
    private double Q;                    // const in model;the covariance while we assume the process noise is drawn from a zero mean Gaussian distribution
    private double R;                    // const in model;the covariance while we assume the measurement noise is drawn from a zero mean Gaussian distribution
    private double P;                    // MSE
    //显示进度使用
    private int currentStatus;


    KalmanFilter(){
        this.curve = new Curve();
    }

    public ChartPanel excuteReduceNoice(ReduceNoiceKalmanFilter reduceNoiceKalmanFilter, String resultPath, String lastMethod) throws IOException{
        iniKalmanFilter();
        getParametersFromPanel(reduceNoiceKalmanFilter);
        BufferedReader curveReader = new BufferedReader(new FileReader(resultPath+"\\"+lastMethod+".txt"));
        BufferedWriter resultWriter = new BufferedWriter(new FileWriter(resultPath+"\\KalmanFilter.txt"));
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
            result = excuteKalmanFilter(curve);
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
        super.resultChartPanel = xyLineChart.getChart("KalmanFilter Result", "Sample", "Value", xyDataset, true);
        currentStatus++;  //保证两个线程的同步！！！
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

    private void getParametersFromPanel(ReduceNoiceKalmanFilter reduceNoiceKalmanFilter){
        this.curve.attackCurveStart = Integer.parseInt(reduceNoiceKalmanFilter.curve_start_textfiled.getText());
        this.curve.attackCurveNum = Integer.parseInt(reduceNoiceKalmanFilter.curve_number_textfiled.getText());
        this.curve.attackSampleStart = Integer.parseInt(reduceNoiceKalmanFilter.sample_start_textfiled.getText());
        this.curve.attackSampleNum = Integer.parseInt(reduceNoiceKalmanFilter.sample_number_textfiled.getText());
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
        double KG = predictP*H/(H*predictP*H+R);
        this.KalmenValue = predictValue+KG*(measurement-H*predictValue);
        this.P = (1-KG*H)*predictP;
        return KalmenValue;
    }
}
