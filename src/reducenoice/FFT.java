package reducenoice;

import evaluation.PI;
import evaluation.SNR;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYDataset;
import panel.ReduceNoiceFFTAndNormalizationZScore;
import tools.CommonFunctions;
import tools.graph.chart.XYLineChart;
import tools.graph.util.ChartUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static tools.CommonFunctions.hexStringTOIntArray;

public class FFT extends ReduceNoiceToolBox{

    //曲线相关参数
    private int attackSampleNum;
    private int attackSampleStart;
    private int attackCurveStart;
    private int attackCurveNum;
    //FFT相关参数
    private static double[] Xr;
    private static double[] Xi;
    //显示进度使用
    private int currentStatus;
    //SNR使用
    private SNR snr;
    private double[] snrResult;
    //PI使用
    private PI pi;
    private double[] piResult;


    public ChartPanel excuteReduceNoice(ReduceNoiceFFTAndNormalizationZScore reduceNoiceFFTAndNormalizationZScore, String resultPath, String lastMethod) throws IOException{
        getParametersFromPanel(reduceNoiceFFTAndNormalizationZScore);
        BufferedReader curveReader = new BufferedReader(new FileReader(resultPath+"\\"+lastMethod+".txt"));
        BufferedWriter resultWriter = new BufferedWriter(new FileWriter(resultPath+"\\FFT.txt"));
        BufferedReader plainReader = new BufferedReader(new FileReader(resultPath+"\\"+"text_in.txt"));
        //剔除不用的Curve
        for(int i = 1; i <= this.attackCurveStart-1; i++){
            curveReader.readLine();
            plainReader.readLine();
        }
        //执行降噪
        double[] curve, result, originalTrace=null, newTrace=null;
        int[] plain;
        for(int i = 1; i <= this.attackCurveNum; i++){
            curve = CommonFunctions.powerCut(CommonFunctions.doubleStringToDoubleArray(curveReader.readLine()), this.attackSampleNum, this.attackSampleStart-1);
            result = excuteFFT(curve);
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
        currentStatus++;  //保证两个线程的同步！！！
        //执行作图
        int[] xris = new int[this.attackSampleNum];
        for(int i = 0; i <= xris.length-1; i++){
            xris[i] = i+this.attackSampleStart;
        }
        double[][] yris = new double[2][this.attackSampleNum];

        assert originalTrace != null;
        assert newTrace != null;
        System.arraycopy(originalTrace, 0, yris[0], 0, xris.length);
        System.arraycopy(newTrace, 0, yris[1], 0, xris.length/2);
        XYDataset xyDataset = ChartUtils.createXYSeries(2, xris, yris, new String[]{"original_trace", "new_trace"});
        XYLineChart xyLineChart = new XYLineChart();
        super.resultChartPanel = xyLineChart.getChart("FFT Result", "Sample", "Value", xyDataset, true);
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

    private double[] excuteFFT(double[] x){
        int n, m, j;
        n = x.length;
        m = 0;
        j = 1;

        //if(n < 1 || n >1024) throw new Exception("采样序列的个数小于1，或大于1024");//保证采样序列的个数介于1到1024之间
        //判断是否为2的指数次方
        if(n%2 == 0){
            m = (int) (Math.log(n)/Math.log(2)); //为2的n次方
            j = n;
        }
        else{
            for(int i = 1; i < (Math.log(n)/Math.log(2))+1; i++){
                j *= 2;
                m++;
                if(j >= n){
                    break;
                }
            }
        }
        double[] xConv = new double[j];
        for(int i = 0; i < j; i++){
            if(i < n){
                xConv[i] = x[i];
            }
            else{
                xConv[i] = 0;
            }
        }

        if(j > n)         //xConv补零
        {
            for(int i = n; i < j; i++){
                xConv[i] = 0;
            }
        }
        i2Sort(xConv, m);       //将xConv进行二进制倒序排序
        myFFT(xConv, m);

        double[] fftpower = new double[j/2];
        for(int i = 0; i < Xr.length/2; i++){
            fftpower[i] = 2*Math.pow(Math.pow(Xr[i], 2.0)+Math.pow(Xi[i], 2.0), 0.5);
        }
        fftpower[0] = 0; //第一个位置无效

		/*
		double[] fftpower = new double[j/2-1];
		for(int i=0;i<fftpower.length;i++)
			fftpower[i]=Math.pow(Math.pow(Xr[i+1], 2.0) +Math.pow(Xi[i+1], 2.0),0.5);
*/
        return fftpower;
    }


    private static void i2Sort(double[] xConv2, int m){
        int[] index = new int[xConv2.length];  //index数组用于，倒序索引
        int[] bits = new int[m];
        double[] temp = new double[xConv2.length];

        for(int i = 0; i < xConv2.length; i++)  //xConv2的原序映像
        {
            temp[i] = xConv2[i];
        }

        for(int i = 0; i < index.length; i++){
            index[i] = i;       //第i个位置，倒序前的值为i
            for(int j = 0; j < m; j++){
                bits[j] = index[i]-index[i]/2*2; //提取index[i]的第j位二进制的值
                index[i] /= 2;
            }
            index[i] = 0;       //清零第i个位置的值
            for(int j = m, power = 1; j > 0; j--){
                index[i] += bits[j-1]*power;  //第i个位置，倒序后的位置
                power *= 2;
            }
        }
        for(int i = 0; i < xConv2.length; i++)   //倒序实现
        {
            xConv2[i] = temp[index[i]];
        }
    }


    private static void myFFT(double[] xConv2, int m){
        int divBy;         //divBy等分
        double[] tempXr, tempXi;      //蝶形结果暂存器
        int n = xConv2.length;
        double pi = Math.PI;
        divBy = 1;
        Xr = new double[n];
        Xi = new double[n];
        tempXr = new double[n];
        tempXi = new double[n];
        double[] wr = new double[n/2];
        double[] wi = new double[n/2];

        for(int i = 0; i < n; i++){      //初始化Xr、Xi，之所以这样初始化，是为了方便下面的蝶形结果暂存
            Xr[i] = xConv2[i];
            Xi[i] = 0;
        }
        for(int i = 0; i < m; i++){      //共需要进行m次蝶形计算
            divBy *= 2;
            for(int k = 0; k < divBy/2; k++){    //旋转因子赋值
                wr[k] = Math.cos(k*2*pi/divBy);
                wi[k] = -Math.sin(k*2*pi/divBy);
            }
            for(int j = 0; j < n; j++){     //蝶形结果暂存
                tempXr[j] = Xr[j];
                tempXi[j] = Xi[j];
            }
            for(int k = 0; k < n/divBy; k++){     //蝶形运算：每一轮蝶形运算，都有n/2对的蝴蝶参与；n/2分为n/divBy组，每组divBy/2个。
                int wIndex = 0;       //旋转因子下标索引
                for(int j = k*divBy; j < k*divBy+divBy/2; j++){
                    double X1 = tempXr[j+divBy/2]*wr[wIndex]-tempXi[j+divBy/2]*wi[wIndex];
                    double X2 = tempXi[j+divBy/2]*wr[wIndex]+tempXr[j+divBy/2]*wi[wIndex];
                    Xr[j] = tempXr[j]+X1;
                    Xi[j] = tempXi[j]+X2;
                    Xr[j+divBy/2] = tempXr[j]-X1; //蝶形对两成员距离相差divBy/2
                    Xi[j+divBy/2] = tempXi[j]-X2;
                    wIndex++;
                }
            }
        }
    }

    public double[] getSNR(){
        return this.snrResult;
    }

    public double[] getPI(){
        return this.piResult;
    }

    public List<double[]> getSNRAndPI(){
        List<double[]> result = new ArrayList<>();
        result.add(snr.getBeforeSNR());
        result.add(snr.getAfterSNR());
        result.add(pi.getBeforePI());
        result.add(pi.getAfterPI());
        return result;
    }

}
