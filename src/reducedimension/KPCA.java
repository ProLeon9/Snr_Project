package reducedimension;

import evaluation.PI;
import evaluation.SNR;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYDataset;
import panel.ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA;
import tools.CommonFunctions;
import tools.filewatch.ZJPFileListener;
import tools.filewatch.ZJPFileMonitor;
import tools.graph.chart.XYLineChart;
import tools.graph.util.ChartUtils;

import java.io.*;

import static tools.CommonFunctions.doubleStringToDoubleArray;
import static tools.CommonFunctions.hexStringTOIntArray;

public class KPCA extends ReduceDimensionToolBox{
    //曲线相关参数
    private int attackSampleNum;
    private int attackSampleStart;
    private int attackCurveStart;
    private int attackCurveNum;
    //KPCA相关参数
    private int reduceDimension;
    //显示进度相关参数
    private int currentStatus;
    //SNR使用
    private SNR snr;
    private double[] snrResult;
    //PI使用
    private PI pi;
    private double[] piResult;


    @Override
    public int getProcessStatus(){
        if(currentStatus < this.attackCurveNum){
            return currentStatus;
        }
        else{
            return -1;
        }
    }

    @Override
    public ChartPanel excuteReduceDimesion(ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA, String resultPath, String lastMethod, String matlabPath) throws Exception{
        getParametersFromPanel(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA);
        BufferedReader curveReader = new BufferedReader(new FileReader(resultPath+"\\"+lastMethod+".txt"));
        BufferedWriter resultWriter = new BufferedWriter(new FileWriter(resultPath+"\\KPCA_old.txt"));

        //剔除不用的Curve
        for(int i = 1; i <= this.attackCurveStart-1; i++){
            curveReader.readLine();
        }
        //执行曲线处理
        double[] curve, originalTrace = null, newTrace = null;
        for(int i = 1; i <= this.attackCurveNum; i++){
            curve = CommonFunctions.powerCut(CommonFunctions.doubleStringToDoubleArray(curveReader.readLine()), this.attackSampleNum, this.attackSampleStart-1);
            resultWriter.append(CommonFunctions.doubleArrayToString(curve));
            currentStatus = i;
            if(i == 50){
                originalTrace = curve;
            }
        }
        curveReader.close();
        resultWriter.close();
        // 执行降维
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
        BufferedReader newCurveReader = new BufferedReader(new FileReader(resultPath + "\\KPCA.txt"));
        for(int i = 1; i <= 50; i++){
            curve = CommonFunctions.doubleStringToDoubleArray(newCurveReader.readLine());
            if(i == 50){
                newTrace = curve;
            }
        }

        //计算SNR-----------------
        BufferedReader oldCurveReader = new BufferedReader(new FileReader(resultPath+"\\KPCA_old.txt"));
        BufferedReader resultReader = new BufferedReader(new FileReader(resultPath+"\\KPCA.txt"));
        BufferedReader plainReader = new BufferedReader(new FileReader(resultPath+"\\"+"text_in.txt"));
        //剔除不用的Curve
        for(int i = 1; i <= this.attackCurveStart-1; i++){
            plainReader.readLine();
        }
        //执行SNR
        double[] result = null;
        int[] plain = null;
        for(int i = 1; i <= this.attackCurveNum; i++){
            curve = doubleStringToDoubleArray(oldCurveReader.readLine());
            result = doubleStringToDoubleArray(resultReader.readLine());
            plain = hexStringTOIntArray(plainReader.readLine());
            if(i == 1){
                snr = new SNR(curve, result);
                pi = new PI(curve, result, attackCurveNum);
            }
            snr.excuteSNR(curve, result, plain, 0);  //TODO:index可以指定为参数
            pi.excutePI(curve, result, plain, 0); //TODO:index可以指定为参数
            currentStatus = i/2 + attackCurveNum/2 -1;
        }
        //计算SNR-----------------
        oldCurveReader.close();
        resultReader.close();
        plainReader.close();
        this.snrResult = snr.getMaxSNR();
        this.piResult = pi.returnPI();
        currentStatus += 1; //为了保证执行线程和更新线程在同一个处理方式时保持同步！！！

        file = new File(resultPath+"\\"+"KPCA_old.txt");
        file.delete();
        Thread.sleep(100);
        //执行作图
        int[] xris = new int[this.attackSampleNum];
        for(int i = 0; i <= xris.length-1; i++){
            xris[i] = i+this.attackSampleStart;
        }
        double[][] yris = new double[2][this.attackSampleNum];

        assert originalTrace != null;
        assert newTrace != null;
        System.arraycopy(originalTrace, 0, yris[0], 0, xris.length);
        System.arraycopy(newTrace, 0, yris[1], 0, newTrace.length);
        XYDataset xyDataset = ChartUtils.createXYSeries(2, xris, yris, new String[]{"original_trace", "new_trace"});
        XYLineChart xyLineChart = new XYLineChart();
        super.resultChartPanel = xyLineChart.getChart("KPCA Result", "Sample", "Value", xyDataset, true);
        return super.resultChartPanel;
    }

    private void getParametersFromPanel(ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA){
        this.attackCurveStart = Integer.parseInt(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.curve_start_textfiled.getText());
        this.attackCurveNum = Integer.parseInt(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.curve_number_textfiled.getText());
        this.attackSampleStart = Integer.parseInt(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.sample_start_textfiled.getText());
        this.attackSampleNum = Integer.parseInt(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.sample_number_textfiled.getText());
        this.reduceDimension = Integer.parseInt(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.character_number_textfield.getText());
    }

    private void excuteMatlabScript(String resultPath, String matlabPath) throws IOException{
        String comstr = matlabPath+" -nosplash -nodesktop -nodisplay -r \""+"wavePath=\'"+resultPath+"\\"+"\',N="+reduceDimension+";"+"runKPCA\"";
        Runtime.getRuntime().exec(comstr);
    }

    public double[] getSNR(){
        return this.snrResult;
    }

    public double[] getPI(){
        return this.piResult;
    }

}
