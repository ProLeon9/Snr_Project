package align;

import evaluation.PI;
import evaluation.SNR;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYDataset;
import panel.AlignDTWAndReduceNoicePOC;
import tools.CommonFunctions;
import tools.graph.chart.XYLineChart;
import tools.graph.util.ChartUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static tools.CommonFunctions.hexStringTOIntArray;

public class DTW extends AlignToolBox{

    //曲线相关参数
    private int attackSampleNum;
    private int attackSampleStart;
    private int attackCurveStart;
    private int attackCurveNum;
    //DTW相关参数
    private int baseCurveIndex;
    private int[][] F;
    //显示进度使用
    private int currentStatus;
    //SNR使用
    private SNR snr;
    private double[] snrResult;
    //PI使用
    private PI pi;
    private double[] piResult;


    public ChartPanel excuteAlign(AlignDTWAndReduceNoicePOC alignDTWAndReduceNoicePOC, String resultPath, String lastMethod) throws IOException{
        getParametersFromPanel(alignDTWAndReduceNoicePOC);
        BufferedReader curveReader = new BufferedReader(new FileReader(resultPath+"\\"+lastMethod+".txt"));
        BufferedWriter resultWriter = new BufferedWriter(new FileWriter(resultPath+"\\DTW.txt"));
        BufferedReader plainReader = new BufferedReader(new FileReader(resultPath+"\\"+"text_in.txt"));
        //读取baseCurve
        for(int i = 1; i <=this.baseCurveIndex-1; i++){
            curveReader.readLine();
        }
        double[] base_trace = CommonFunctions.powerCut(CommonFunctions.doubleStringToDoubleArray(curveReader.readLine()), this.attackSampleNum, this.attackSampleStart-1);
        curveReader.close();
        //剔除不用的Curve
        curveReader = new BufferedReader(new FileReader(resultPath+"\\"+lastMethod+".txt"));
        for(int i = 1; i <= this.attackCurveStart-1; i++){
            curveReader.readLine();
            plainReader.readLine();
        }
        //执行对齐
        double[] curve, result, originalTrace=null, newTrace=null;
        int[] plain;
        for(int i = 1; i <= this.attackCurveNum; i++){
            curve = CommonFunctions.powerCut(CommonFunctions.doubleStringToDoubleArray(curveReader.readLine()), this.attackSampleNum, this.attackSampleStart-1);
            result = excuteDTW(base_trace, curve);
            plain = hexStringTOIntArray(plainReader.readLine());
            if(i == 1){
                snr = new SNR(curve, result);
                pi = new PI(curve, result, attackCurveNum);
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
        currentStatus++;   //保证两个线程的同步！！！
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
        super.resultChartPanel = xyLineChart.getChart("DTW Result", "Sample", "Value", xyDataset, true);
        return super.resultChartPanel;
    }

    @Override
    public int getProcessStatus(){
        if(currentStatus < this.attackCurveNum)
            return currentStatus;
        else
            return -1;
    }

    @Override
    public List<double[]> getSNRAndPI(){
        List<double[]> result = new ArrayList<>();
        result.add(snr.getBeforeSNR());
        result.add(snr.getAfterSNR());
        result.add(pi.getBeforePI());
        result.add(pi.getAfterPI());
        return result;
    }

    private void getParametersFromPanel(AlignDTWAndReduceNoicePOC alignDTWAndReduceNoicePOC){
        this.attackCurveStart = Integer.parseInt(alignDTWAndReduceNoicePOC.curve_start_textfiled.getText());
        this.attackCurveNum = Integer.parseInt(alignDTWAndReduceNoicePOC.curve_number_textfiled.getText());
        this.attackSampleStart = Integer.parseInt(alignDTWAndReduceNoicePOC.sample_start_textfiled.getText());
        this.attackSampleNum = Integer.parseInt(alignDTWAndReduceNoicePOC.sample_number_textfiled.getText());
        this.baseCurveIndex = Integer.parseInt(alignDTWAndReduceNoicePOC.base_curve_index_textfield.getText());
    }

    private double[] excuteDTW(double[] base_trace, double[] trace){
        int base_trace_length = base_trace.length;
        int trace_length = trace.length;
        double[][] Matrix = new double[base_trace_length][trace_length];

        for(int i = 0; i < base_trace_length; i++){
            for(int j = 0; j < trace_length; j++){
                Matrix[i][j] = Math.abs(trace[j]-base_trace[i]);
            }
        }
        int[] inidian = new int[2];
        inidian[0] = 0;
        inidian[1] = 0;
        F = new int[base_trace_length][2];
        DTW_middle(Matrix, inidian);
        int F_flag = 0;
        int x_flag = 0;
        double[] pc = new double[base_trace_length];
        for(int i = 0; i < base_trace_length-1; i++){
            if(F[i][0] == F_flag){
                pc[i] = trace[F[i][1]];
                F_flag = F_flag+1;
                x_flag = i;
            }
            else{
                pc[i] = base_trace[F[x_flag+1][1]];
            }
        }
        for(int i = 0; i < base_trace_length; i++){
            System.out.println(pc[i]);
        }
        return pc;
    }

    private void DTW_middle(double[][] input, int[] inidian){
        int X_num = input.length;

        double[][] g_Matrix = new double[X_num][X_num];
        for(int i = 0; i < X_num; i++){
            for(int j = 0; j < X_num; j++){
                g_Matrix[i][j] = 0;
            }
        }
        for(int i = 0; i < X_num; i++){
            for(int j = 0; j < X_num; j++){
                double temp;
                if(i == 0){
                    temp = 0;
                    for(int k = 0; k <= j; k++){
                        temp = temp+input[i][k];
                    }
                    g_Matrix[i][j] = temp;
                }
                else{
                    temp = 0;
                    if(j == 0){
                        for(int k = 0; k <= i; k++){
                            temp = temp+input[k][j];
                        }
                        g_Matrix[i][j] = temp;
                    }
                    else{
                        double xi = g_Matrix[i][j-1]+input[i][j];
                        double xj = g_Matrix[i-1][j]+input[i][j];
                        double xk = g_Matrix[i-1][j-1]+2*input[i][j];
                        temp = 999999;
                        if(temp > xi){
                            temp = xi;
                        }
                        if(temp > xj){
                            temp = xj;
                        }
                        if(temp > xk){
                            temp = xk;
                        }

                        g_Matrix[i][j] = temp;
                    }
                }
            }
        }

        int xtemp = inidian[0];
        int ytemp = inidian[1];
        int F_flag = 0;
        while(true){
            double temp = 99999;
            int[] tmp = new int[2];
            int flag = 0;
            if(xtemp+2 > X_num || F_flag >= X_num-1){
                break;
            }
            if(ytemp+2 > X_num || F_flag >= X_num-1){
                break;
            }

            if(g_Matrix[xtemp][ytemp+1] < temp){
                temp = g_Matrix[xtemp][ytemp+1];
                flag = 0;
            }
            if(g_Matrix[xtemp+1][ytemp] < temp){
                temp = g_Matrix[xtemp+1][ytemp];
                flag = 1;
            }
            if(g_Matrix[xtemp+1][ytemp+1] < temp){
                flag = 2;
            }
            if(flag == 0){
                ytemp++;
            }
            if(flag == 1){
                xtemp++;
            }
            if(flag == 2){
                xtemp++;
                ytemp++;
            }
            tmp[0] = (xtemp);
            tmp[1] = (ytemp);
            F_flag = F_flag+1;
            F[F_flag][0] = (tmp[0]);
            F[F_flag][1] = (tmp[1]);
        }
    }

    public double[] getSNR(){
        return this.snrResult;
    }

    public double[] getPI(){
        return this.piResult;
    }
}
