package align;

import evaluation.PI;
import evaluation.SNR;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYDataset;
import panel.AlignStaticAlign;
import tools.CommonFunctions;
import tools.Curve;
import tools.graph.chart.XYLineChart;
import tools.graph.util.ChartUtils;

import java.io.*;

import static tools.CommonFunctions.hexStringTOIntArray;


public class StaticAlign extends AlignToolBox{
    //曲线相关参数
    private Curve curve;
    //显示进度使用
    private int currentStatus;


    StaticAlign(){
        this.curve = new Curve();
    }

    public ChartPanel executeAlign(AlignStaticAlign alignStaticAlign, String resultPath, String lastMethod) throws IOException{
        getParametersFromPanel(alignStaticAlign);
        BufferedReader curveReader = new BufferedReader(new FileReader(resultPath+"\\"+lastMethod+".txt"));
        BufferedWriter resultWriter = new BufferedWriter(new FileWriter(resultPath+"\\StaticAlign.txt"));
        BufferedReader plainReader = new BufferedReader(new FileReader(resultPath+"\\"+"text_in.txt"));
        //读取baseCurve
        for(int i = 1; i <= this.curve.baseCurveIndex-1; i++){
            curveReader.readLine();
        }
        double[] base_trace = CommonFunctions.powerCut(CommonFunctions.doubleStringToDoubleArray(curveReader.readLine()), this.curve.attackSampleNum, this.curve.attackSampleStart-1);
        curveReader.close();
        //剔除不用的Curve
        curveReader = new BufferedReader(new FileReader(resultPath+"\\"+lastMethod+".txt"));
        for(int i = 1; i <= this.curve.attackCurveStart-1; i++){
            curveReader.readLine();
            plainReader.readLine();
        }
        //执行对齐
        double[] curve, result, originalTrace = null, newTrace = null;
        int[] plain;
        for(int i = 1; i <= this.curve.attackCurveNum; i++){
            curve = CommonFunctions.powerCut(CommonFunctions.doubleStringToDoubleArray(curveReader.readLine()), this.curve.attackSampleNum, this.curve.attackSampleStart-1);
            result = excuteStaticAlign(base_trace, curve, this.curve.maxWindow);
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
        super.resultChartPanel = xyLineChart.getChart("StaticAlign Result", "Sample", "Value", xyDataset, true);
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

    private void getParametersFromPanel(AlignStaticAlign alignStaticAlign){
        this.curve.attackCurveStart = Integer.parseInt(alignStaticAlign.curve_start_textfiled.getText());
        this.curve.attackCurveNum = Integer.parseInt(alignStaticAlign.curve_number_textfiled.getText());
        this.curve.attackSampleStart = Integer.parseInt(alignStaticAlign.sample_start_textfiled.getText());
        this.curve.attackSampleNum = Integer.parseInt(alignStaticAlign.sample_number_textfiled.getText());
        this.curve.maxWindow = Integer.parseInt(alignStaticAlign.max_deviation_textfield.getText());
        this.curve.baseCurveIndex = Integer.parseInt(alignStaticAlign.base_curve_index_textfield.getText());
    }

    private double[] excuteStaticAlign(double[] base_trace, double[] trace, int max_deviation){
        int base_trace_length = base_trace.length;
        double[] cor = new double[max_deviation];
        for(int i = 0; i < max_deviation; i++){
            double[] tmp = new double[base_trace_length];
            for(int j = 0; j < base_trace_length; j++){
                tmp[j] = trace[(j+i)%base_trace_length];
            }
            cor[i] = corr(base_trace, tmp);
        }
        double cor_max = 0;
        int cor_max_index = 0;
        for(int i = 0; i < max_deviation; i++){
            if(cor[i] > cor_max){
                cor_max = cor[i];
                cor_max_index = i;
            }
        }
        double[] tmp = new double[base_trace_length];
        for(int j = 0; j < base_trace_length; j++){
            tmp[j] = trace[(j+cor_max_index)%base_trace_length];
        }
        return tmp;
    }

    private double corr(double[] pc1, double[] pc2){
        int x = pc1.length;
        double sum1 = 0;
        double sum2 = 0;
        for(int i = 0; i < x; i++){
            sum1 = sum1+pc1[i];
            sum2 = sum2+pc2[i];
        }
        double aver1 = sum1/x;
        double aver2 = sum2/x;
        double fenzi = 0;
        double fenmu1 = 0;
        double fenmu2 = 0;
        for(int i = 0; i < x; i++){
            fenzi = fenzi+(pc1[i]-aver1)*(pc2[i]-aver2);
            fenmu1 = fenmu1+(pc1[i]-aver1)*(pc1[i]-aver1);
            fenmu2 = fenmu2+(pc2[i]-aver1)*(pc2[i]-aver1);
        }
        return Math.abs(fenzi/Math.pow(fenmu1*fenmu2, 0.5));
    }
}
