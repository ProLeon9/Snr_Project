package align;

import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYDataset;
import panel.AlignStaticAlign;
import tools.CommonFunctions;
import tools.graph.chart.XYLineChart;
import tools.graph.util.ChartUtils;

import java.io.*;

public class StaticAlign extends AlignToolBox{

    //曲线相关参数
    private int attackSampleNum;
    private int attackSampleStart;
    private int attackCurveStart;
    private int attackCurveNum;
    //StaticAlign相关参数
    private int maxWindow;
    private int baseCurveIndex;
    //显示进度使用
    private int currentStatus;


    public ChartPanel excuteAlign(AlignStaticAlign alignStaticAlign, String resultPath) throws IOException{
        getParametersFromPanel(alignStaticAlign);
        BufferedReader curveReader = new BufferedReader(new FileReader(resultPath+"\\wave.txt"));
        BufferedWriter resultWriter = new BufferedWriter(new FileWriter(resultPath+"\\StaticAlign.txt"));
        //读取baseCurve
        for(int i = 1; i <=this.baseCurveIndex-1; i++){
            curveReader.readLine();
        }
        double[] base_trace = CommonFunctions.powerCut(CommonFunctions.doubleStringToDoubleArray(curveReader.readLine()), this.attackSampleNum, this.attackSampleStart-1);
        curveReader.close();
        //剔除不用的Curve
        curveReader = new BufferedReader(new FileReader(resultPath+"\\wave.txt"));
        for(int i = 1; i <= this.attackCurveStart-1; i++){
            curveReader.readLine();
        }
        //执行对齐
        double[] curve, result, originalTrace=null, newTrace=null;
        for(int i = 1; i <= this.attackCurveNum; i++){
            curve = CommonFunctions.powerCut(CommonFunctions.doubleStringToDoubleArray(curveReader.readLine()), this.attackSampleNum, this.attackSampleStart-1);
            result = excuteStaticAlign(base_trace, curve, maxWindow);
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
        super.resultChartPanel = xyLineChart.getChart("StaticAlign Result", "Sample", "Value", xyDataset, true);
        return super.resultChartPanel;
    }

    @Override
    public int getProcessStatus(){
        if(currentStatus < this.attackCurveNum)
            return currentStatus;
        else
            return -1;
    }

    private void getParametersFromPanel(AlignStaticAlign alignStaticAlign){
        this.attackCurveStart = Integer.parseInt(alignStaticAlign.curve_start_textfiled.getText());
        this.attackCurveNum = Integer.parseInt(alignStaticAlign.curve_number_textfiled.getText());
        this.attackSampleStart = Integer.parseInt(alignStaticAlign.sample_start_textfiled.getText());
        this.attackSampleNum = Integer.parseInt(alignStaticAlign.sample_number_textfiled.getText());
        this.maxWindow = Integer.parseInt(alignStaticAlign.max_deviation_textfield.getText());
        this.baseCurveIndex = Integer.parseInt(alignStaticAlign.base_curve_index_textfield.getText());
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
            fenmu2 = fenmu2+(pc2[i]-aver2)*(pc2[i]-aver2);
        }
        double cor = Math.abs(fenzi/Math.pow(fenmu1*fenmu2, 0.5));
        return cor;
    }
}
