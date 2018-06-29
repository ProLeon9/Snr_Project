package reducenoice;

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

import static tools.CommonFunctions.hexStringTOIntArray;


public class FFT extends ReduceNoiceToolBox{
    //������ز���
    private Curve curve;
    //FFT��ز���
    private static double[] Xr;
    private static double[] Xi;
    //��ʾ����ʹ��
    private int currentStatus;


    public FFT(){
        this.curve = new Curve();
    }

    public ChartPanel excuteReduceNoice(ReduceNoiceFFTAndNormalizationZScore reduceNoiceFFTAndNormalizationZScore, String resultPath, String lastMethod) throws IOException{
        getParametersFromPanel(reduceNoiceFFTAndNormalizationZScore);
        BufferedReader curveReader = new BufferedReader(new FileReader(resultPath+"\\"+lastMethod+".txt"));
        BufferedWriter resultWriter = new BufferedWriter(new FileWriter(resultPath+"\\FFT.txt"));
        BufferedReader plainReader = new BufferedReader(new FileReader(resultPath+"\\"+"text_in.txt"));
        //�޳����õ�Curve
        for(int i = 1; i <= this.curve.attackCurveStart-1; i++){
            curveReader.readLine();
            plainReader.readLine();
        }
        //ִ�н���
        double[] curve, result, originalTrace = null, newTrace = null;
        int[] plain;
        for(int i = 1; i <= this.curve.attackCurveNum; i++){
            curve = CommonFunctions.powerCut(CommonFunctions.doubleStringToDoubleArray(curveReader.readLine()), this.curve.attackSampleNum, this.curve.attackSampleStart-1);
            result = excuteFFT(curve);
            plain = hexStringTOIntArray(plainReader.readLine());
            if(i == 1){
                snr = new SNR(curve, result);
                pi = new PI(curve, result, this.curve.attackCurveNum);
            }
            snr.excuteSNR(curve, result, plain, 0);  //TODO:index����ָ��Ϊ����
            pi.excutePI(curve, result, plain, 0); //TODO:index����ָ��Ϊ����
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

        //ִ����ͼ
        int[] xris = new int[this.curve.attackSampleNum];
        for(int i = 0; i <= xris.length-1; i++){
            xris[i] = i+this.curve.attackSampleStart;
        }
        double[][] yris = new double[2][this.curve.attackSampleNum];

        assert originalTrace != null;
        assert newTrace != null;
        System.arraycopy(originalTrace, 0, yris[0], 0, xris.length);
        System.arraycopy(newTrace, 0, yris[1], 0, xris.length/2);
        XYDataset xyDataset = ChartUtils.createXYSeries(2, xris, yris, new String[]{"original_trace", "new_trace"});
        XYLineChart xyLineChart = new XYLineChart();
        super.resultChartPanel = xyLineChart.getChart("FFT Result", "Sample", "Value", xyDataset, true);
        currentStatus++;  //��֤�����̵߳�ͬ��������
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

    private double[] excuteFFT(double[] x){
        int n, m, j;
        n = x.length;
        m = 0;
        j = 1;

        //if(n < 1 || n >1024) throw new Exception("�������еĸ���С��1�������1024");//��֤�������еĸ�������1��1024֮��
        //�ж��Ƿ�Ϊ2��ָ���η�
        if(n%2 == 0){
            m = (int) (Math.log(n)/Math.log(2)); //Ϊ2��n�η�
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

        if(j > n)         //xConv����
        {
            for(int i = n; i < j; i++){
                xConv[i] = 0;
            }
        }
        i2Sort(xConv, m);       //��xConv���ж����Ƶ�������
        myFFT(xConv, m);

        double[] fftpower = new double[j/2];
        for(int i = 0; i < Xr.length/2; i++){
            double tempResult = 2*Math.pow(Math.pow(Xr[i], 2.0)+Math.pow(Xi[i], 2.0), 0.5);
            if(Double.isNaN(tempResult)){
                fftpower[i] = fftpower[i-1];
            }
            else{
                fftpower[i] = tempResult;
            }
        }
        fftpower[0] = 0; //��һ��λ����Ч

		/*
        double[] fftpower = new double[j/2-1];
		for(int i=0;i<fftpower.length;i++)
			fftpower[i]=Math.pow(Math.pow(Xr[i+1], 2.0) +Math.pow(Xi[i+1], 2.0),0.5);
*/
        return fftpower;
    }


    private static void i2Sort(double[] xConv2, int m){
        int[] index = new int[xConv2.length];  //index�������ڣ���������
        int[] bits = new int[m];
        double[] temp = new double[xConv2.length];

        System.arraycopy(xConv2, 0, temp, 0, xConv2.length);

        for(int i = 0; i < index.length; i++){
            index[i] = i;       //��i��λ�ã�����ǰ��ֵΪi
            for(int j = 0; j < m; j++){
                bits[j] = index[i]-index[i]/2*2; //��ȡindex[i]�ĵ�jλ�����Ƶ�ֵ
                index[i] /= 2;
            }
            index[i] = 0;       //�����i��λ�õ�ֵ
            for(int j = m, power = 1; j > 0; j--){
                index[i] += bits[j-1]*power;  //��i��λ�ã�������λ��
                power *= 2;
            }
        }
        for(int i = 0; i < xConv2.length; i++)   //����ʵ��
        {
            xConv2[i] = temp[index[i]];
        }
    }


    private static void myFFT(double[] xConv2, int m){
        int divBy;         //divBy�ȷ�
        double[] tempXr, tempXi;      //���ν���ݴ���
        int n = xConv2.length;
        double pi = Math.PI;
        divBy = 1;
        Xr = new double[n];
        Xi = new double[n];
        tempXr = new double[n];
        tempXi = new double[n];
        double[] wr = new double[n/2];
        double[] wi = new double[n/2];

        for(int i = 0; i < n; i++){      //��ʼ��Xr��Xi��֮����������ʼ������Ϊ�˷�������ĵ��ν���ݴ�
            Xr[i] = xConv2[i];
            Xi[i] = 0;
        }
        for(int i = 0; i < m; i++){      //����Ҫ����m�ε��μ���
            divBy *= 2;
            for(int k = 0; k < divBy/2; k++){    //��ת���Ӹ�ֵ
                wr[k] = Math.cos(k*2*pi/divBy);
                wi[k] = -Math.sin(k*2*pi/divBy);
            }
            for(int j = 0; j < n; j++){     //���ν���ݴ�
                tempXr[j] = Xr[j];
                tempXi[j] = Xi[j];
            }
            for(int k = 0; k < n/divBy; k++){     //�������㣺ÿһ�ֵ������㣬����n/2�Եĺ������룻n/2��Ϊn/divBy�飬ÿ��divBy/2����
                int wIndex = 0;       //��ת�����±�����
                for(int j = k*divBy; j < k*divBy+divBy/2; j++){
                    double X1 = tempXr[j+divBy/2]*wr[wIndex]-tempXi[j+divBy/2]*wi[wIndex];
                    double X2 = tempXi[j+divBy/2]*wr[wIndex]+tempXr[j+divBy/2]*wi[wIndex];
                    Xr[j] = tempXr[j]+X1;
                    Xi[j] = tempXi[j]+X2;
                    Xr[j+divBy/2] = tempXr[j]-X1; //���ζ�����Ա�������divBy/2
                    Xi[j+divBy/2] = tempXi[j]-X2;
                    wIndex++;
                }
            }
        }
    }
}
