package location;


import middle.MiddleValue;
import middle.MiddleValueFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYDataset;
import panel.LocationPanel;
import tools.CommonFunctions;
import tools.graph.chart.XYLineChart;
import tools.graph.util.ChartUtils;

import java.io.*;
import java.util.Objects;


public class SOSD extends LocationToolBox{
    //求中间值所用参数
    private String algorithm;
    private String leakageModel;
    private int set_num;
    //曲线相关参数
    private int attackSampleNum;
    private int attackSampleStart;
    private int attackCurveStart;
    private int attackCurveNum;
    //SOSD相关参数
    private int num;
    private int[] num2;
    private double[] sum_power;
    private double[][] E_power;
    private double[] result;
    private int bit;
    //显示进度使用
    private int currentStatus;



    @Override
    public ChartPanel excuteLocation(LocationPanel locationPanel, String resultPath) throws IOException{
        getParametersFromPanel(locationPanel);
        convertLeakageModelToInt();
        MiddleValue middleValueToolBox = new MiddleValueFactory().createMiddleValue(this.algorithm);
        ini(this.attackSampleNum, this.set_num);
        BufferedReader curveReader = new BufferedReader(new FileReader(resultPath+"\\wave.txt"));
        BufferedReader plainReader = new BufferedReader(new FileReader(resultPath+"\\text_in.txt"));
        BufferedReader keyReader = new BufferedReader(new FileReader(resultPath+"\\key.txt"));
        BufferedWriter resultWriter = new BufferedWriter(new FileWriter(resultPath+"\\SOSD.txt"));

        for(int i = 1; i <= this.attackCurveStart-1; i++){
            curveReader.readLine();
            plainReader.readLine();
        }
        int[] plain;
        double[] curve;
        double[] result = null;
        int[] key = CommonFunctions.hexStringTOIntArray(keyReader.readLine());

        int[] xris = new int[this.attackSampleNum];
        for(int i = 0; i <= xris.length-1; i++){
            xris[i] = i+this.attackSampleStart;
        }
        double[][] yris = new double[1][this.attackSampleNum];

        for(int i = 1; i <= this.attackCurveNum; i++){
            plain = CommonFunctions.hexStringTOIntArray(plainReader.readLine());
            curve = CommonFunctions.powerCut(CommonFunctions.doubleStringToDoubleArray(curveReader.readLine()), this.attackSampleNum, this.attackSampleStart-1);
            int middleValue = middleValueToolBox.getMiddleValue(locationPanel, plain, key);
            result = excuteSOSD(middleValue, curve);
            currentStatus = i;
        }
        resultWriter.append(CommonFunctions.doubleArrayToString(result));

        curveReader.close();
        plainReader.close();
        keyReader.close();
        resultWriter.close();

        assert result != null;
        System.arraycopy(result, 0, yris[0], 0, xris.length);
        XYDataset xyDataset = ChartUtils.createXYSeries(1, xris, yris, new String[]{"result"});
        XYLineChart xyLineChart = new XYLineChart();
        super.resultChartPanel = xyLineChart.getChart("NICV Result", "Sample", "Corelation", xyDataset, true);
        return super.resultChartPanel;
    }

    @Override
    public int getProcessStatus(){
        if(currentStatus < this.attackCurveNum)
            return currentStatus;
        else
            return -1;
    }

    //SOSD初始化所需数组
    private void ini(int power_length, int keybit){
        bit = keybit;
        num = 0;
        num2 = new int[keybit];
        sum_power = new double[power_length];
        E_power = new double[keybit][power_length];
        double[] v_power = new double[power_length];
        result = new double[power_length];
    }

    private void convertLeakageModelToInt(){
        if(Objects.equals(this.leakageModel, "HW")){
            this.set_num = 9;
        }
        else if(Objects.equals(this.leakageModel, "VALUE")){
            this.set_num = 256;
        }
    }

    private void getParametersFromPanel(LocationPanel locationPanel){
        this.algorithm = (String) locationPanel.LocationAlgorithmCombobox.getSelectedItem();
        String operation = (String) locationPanel.LocationOperationCombobox.getSelectedItem();
        this.leakageModel = (String) locationPanel.LocationAttackModelCombobox.getSelectedItem();
        int target_subkey = Integer.parseInt(locationPanel.LocationKeyIndexTextfield.getText());
        this.attackCurveNum = Integer.parseInt(locationPanel.LocationCurveNumberTextfield.getText());
        this.attackCurveStart = Integer.parseInt(locationPanel.LocationCurveFirstTextfield.getText());
        this.attackSampleNum = Integer.parseInt(locationPanel.LocationSampleNumberTextfield.getText());
        this.attackSampleStart = Integer.parseInt(locationPanel.LocationSampleFirstTextfield.getText());
    }

    private double[] excuteSOSD(int middle, double[] power){
        int tmp = middle;
        num2[tmp] = num2[tmp]+1;
        num = num+1;
        for(int i = 0; i < power.length; i++){
            E_power[tmp][i] = E_power[tmp][i]+power[i];
        }
        for(int i = 0; i < power.length; i++){
            sum_power[i] = sum_power[i]+power[i];
        }
        for(int i = 0; i < power.length; i++){
            double temp = 0;
            for(int j = 1; j < bit; j++){
                if(num2[j] != 0)
                //temp += Math.pow((E_power[j][i]/num2[j] - sum_power[i]/num), 2);
                {
                    temp += Math.pow((E_power[j][i]/num2[j]-E_power[0][i]/num2[0]), 2);
                }
            }
            result[i] = temp;
        }
        //降低数量积
        int len = 1;
        double base = result[0];
        while(base > 1){
            base /= 10;
            len++;
        }
        for(int i = 0; i < power.length; i++){
            result[i] = result[i]/Math.pow(10, len);
        }
        return result;
    }
}
