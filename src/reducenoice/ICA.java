package reducenoice;

import evaluation.PI;
import evaluation.SNR;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYDataset;
import panel.ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA;
import tools.CommonFunctions;
import tools.Curve;
import tools.filewatch.ZJPFileListener;
import tools.filewatch.ZJPFileMonitor;
import tools.graph.chart.XYLineChart;
import tools.graph.util.ChartUtils;

import java.io.*;

import static tools.CommonFunctions.doubleStringToDoubleArray;
import static tools.CommonFunctions.hexStringTOIntArray;

public class ICA extends ReduceNoiceToolBox{
    //曲线相关参数
    private Curve curve;
    //显示进度相关参数
    private int currentStatus;


    public ICA(){
        this.curve = new Curve();
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

    public ChartPanel excuteReduceNoice(ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA, String resultPath, String lastMethod, String matlabPath, String icaPath) throws Exception{
        getParametersFromPanel(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA);
        BufferedReader curveReader = new BufferedReader(new FileReader(resultPath+"\\"+lastMethod+".txt"));
        BufferedReader curve2Reader = new BufferedReader(new FileReader(icaPath));
        BufferedWriter resultWriter = new BufferedWriter(new FileWriter(resultPath+"\\ICA_old1.txt"));
        BufferedWriter result2Writer = new BufferedWriter(new FileWriter(resultPath+"\\ICA_old2.txt"));

        //剔除不用的Curve
        for(int i = 1; i <= this.curve.attackCurveStart-1; i++){
            curveReader.readLine();
            curve2Reader.readLine();
        }
        //执行曲线处理
        double[] curve, originalTrace = null, newTrace = null;
        for(int i = 1; i <= this.curve.attackCurveNum; i++){
            curve = CommonFunctions.powerCut(CommonFunctions.doubleStringToDoubleArray(curveReader.readLine()), this.curve.attackSampleNum, this.curve.attackSampleStart-1);
            resultWriter.append(CommonFunctions.doubleArrayToString(curve));
            curve = CommonFunctions.powerCut(CommonFunctions.doubleStringToDoubleArray(curve2Reader.readLine()), this.curve.attackSampleNum, this.curve.attackSampleStart-1);
            result2Writer.append(CommonFunctions.doubleArrayToString(curve));
            currentStatus = i/2;
            if(i == 50){
                originalTrace = curve;
            }
        }
        curveReader.close();
        curve2Reader.close();
        resultWriter.close();
        result2Writer.close();
        // 执行降噪
        excuteMatlabScript(resultPath, matlabPath);
        // 等待matlab脚本跑完再继续读取文件第50条赋值给newTrace进行作图
        ZJPFileMonitor m = new ZJPFileMonitor(5000);
        m.monitor(System.getProperty("user.dir"), new ZJPFileListener());
        m.start();

        File file = new File(System.getProperty("user.dir")+"\\done.txt");
        while(!file.exists()){
            Thread.sleep(100);
        }
        file.delete();
        Thread.sleep(100);
        // 读取处理后曲线的第50条
        BufferedReader newCurveReader = new BufferedReader(new FileReader(resultPath+"\\ICA.txt"));
        for(int i = 1; i <= 50; i++){
            curve = CommonFunctions.doubleStringToDoubleArray(newCurveReader.readLine());
            if(i == 50){
                newTrace = curve;
            }
        }

        //计算SNR和PI-----------------
        BufferedReader oldCurveReader = new BufferedReader(new FileReader(resultPath+"\\ICA_old1.txt"));
        BufferedReader resultReader = new BufferedReader(new FileReader(resultPath+"\\ICA.txt"));
        BufferedReader plainReader = new BufferedReader(new FileReader(resultPath+"\\"+"text_in.txt"));
        //剔除不用的Curve
        for(int i = 1; i <= this.curve.attackCurveStart-1; i++){
            plainReader.readLine();
        }
        //执行SNR
        double[] result;
        int[] plain;
        for(int i = 1; i <= this.curve.attackCurveNum; i++){
            curve = doubleStringToDoubleArray(oldCurveReader.readLine());
            result = doubleStringToDoubleArray(resultReader.readLine());
            plain = hexStringTOIntArray(plainReader.readLine());
            if(i == 1){
                snr = new SNR(curve, result);
                pi = new PI(curve, result, this.curve.attackCurveNum);
            }
            snr.excuteSNR(curve, result, plain, 0);  //TODO:index可以指定为参数
            pi.excutePI(curve, result, plain, 0); //TODO:index可以指定为参数
            currentStatus = i/2+this.curve.attackCurveNum/2-1;
        }
        //计算SNR和PI-----------------
        oldCurveReader.close();
        resultReader.close();
        plainReader.close();
        this.snrResult = snr.getMaxSNR();
        this.piResult = pi.returnPI();


        file = new File(resultPath+"\\"+"ICA_old1.txt");
        file.delete();
        file = new File(resultPath+"\\"+"ICA_old2.txt");
        file.delete();
        Thread.sleep(100);
        //执行作图
        int[] xris = new int[this.curve.attackSampleNum];
        for(int i = 0; i <= xris.length-1; i++){
            xris[i] = i+this.curve.attackSampleStart;
        }
        double[][] yris = new double[2][this.curve.attackSampleNum];

        assert originalTrace != null;
        assert newTrace != null;
        System.arraycopy(originalTrace, 0, yris[0], 0, xris.length);
        System.arraycopy(newTrace, 0, yris[1], 0, newTrace.length);
        XYDataset xyDataset = ChartUtils.createXYSeries(2, xris, yris, new String[]{"original_trace", "new_trace"});
        XYLineChart xyLineChart = new XYLineChart();
        super.resultChartPanel = xyLineChart.getChart("ICA Result", "Sample", "Value", xyDataset, true);
        currentStatus += 1; //为了保证执行线程和更新线程在同一个处理方式时保持同步！！！
        return super.resultChartPanel;
    }

    private void getParametersFromPanel(ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA){
        this.curve.attackCurveStart = Integer.parseInt(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.curve_start_textfiled.getText());
        this.curve.attackCurveNum = Integer.parseInt(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.curve_number_textfiled.getText());
        this.curve.attackSampleStart = Integer.parseInt(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.sample_start_textfiled.getText());
        this.curve.attackSampleNum = Integer.parseInt(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.sample_number_textfiled.getText());
    }

    private void excuteMatlabScript(String resultPath, String matlabPath) throws IOException{
        String comstr = matlabPath+" -nosplash -nodesktop -nodisplay -r \""+"wavePath=\'"+resultPath+"\\"+"\'"+";"+"runICA\"";
        Runtime.getRuntime().exec(comstr);
    }
}
