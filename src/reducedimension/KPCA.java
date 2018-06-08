package reducedimension;

import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYDataset;
import panel.ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSA;
import tools.CommonFunctions;
import tools.filewatch.ZJPFileListener;
import tools.filewatch.ZJPFileMonitor;
import tools.graph.chart.XYLineChart;
import tools.graph.util.ChartUtils;

import java.io.*;

public class KPCA extends ReduceDimensionToolBox{
    //������ز���
    private int attackSampleNum;
    private int attackSampleStart;
    private int attackCurveStart;
    private int attackCurveNum;
    //KPCA��ز���
    private int reduceDimension;
    //��ʾ������ز���
    private int currentStatus;


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
    public ChartPanel excuteReduceDimesion(ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSA reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSA, String resultPath, String lastMethod, String matlabPath) throws Exception{
        getParametersFromPanel(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSA);
        BufferedReader curveReader = new BufferedReader(new FileReader(resultPath+"\\"+lastMethod+".txt"));
        BufferedWriter resultWriter = new BufferedWriter(new FileWriter(resultPath+"\\KPCA_old.txt"));

        //�޳����õ�Curve
        for(int i = 1; i <= this.attackCurveStart-1; i++){
            curveReader.readLine();
        }
        //ִ�����ߴ���
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
        // ִ�н�ά
        excuteMatlabScript(resultPath, matlabPath);
        // �ȴ�matlab�ű������ټ�����ȡ�ļ���50����ֵ��newTrace������ͼ
        ZJPFileMonitor m = new ZJPFileMonitor(5000);
        m.monitor(System.getProperty("user.dir"), new ZJPFileListener());
        m.start();

        File file = new File(System.getProperty("user.dir")+"\\done.txt");
        while(!file.exists()){
            Thread.sleep(500);
        }
        Thread.sleep(100);
        // ��ȡ��������ߵĵ�50��
        BufferedReader newCurveReader = new BufferedReader(new FileReader(resultPath + "\\KPCA.txt"));
        for(int i = 1; i <= 50; i++){
            curve = CommonFunctions.doubleStringToDoubleArray(newCurveReader.readLine());
            if(i == 50){
                newTrace = curve;
            }
        }
        file.delete();
        file = new File(resultPath+"\\"+"KPCA_old.txt");
        file.delete();
        //ִ����ͼ
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

    private void getParametersFromPanel(ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSA reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSA){
        this.attackCurveStart = Integer.parseInt(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSA.curve_start_textfiled.getText());
        this.attackCurveNum = Integer.parseInt(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSA.curve_number_textfiled.getText());
        this.attackSampleStart = Integer.parseInt(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSA.sample_start_textfiled.getText());
        this.attackSampleNum = Integer.parseInt(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSA.sample_number_textfiled.getText());
        this.reduceDimension = Integer.parseInt(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSA.character_number_textfield.getText());
    }

    private void excuteMatlabScript(String resultPath, String matlabPath) throws IOException{
        String comstr = matlabPath+" -nosplash -nodesktop -nodisplay -r \""+"wavePath=\'"+resultPath+"\\"+"\',N="+reduceDimension+";"+"runKPCA\"";
        Runtime.getRuntime().exec(comstr);
    }
}
