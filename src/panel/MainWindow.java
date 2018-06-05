package panel;

import align.AlignFactory;
import align.AlignToolBox;
import align.DTW;
import align.StaticAlign;
import location.LocationFactory;
import location.LocationToolBox;
import org.jfree.chart.ChartPanel;
import reducenoice.FFT;
import reducenoice.POC;
import reducenoice.ReduceNoiceFactory;
import reducenoice.ReduceNoiceToolBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainWindow{
    private JTabbedPane tabbedPane1;
    private JPanel mainPanel;
    private JPanel LocationPanel;
    private JPanel AlignPanel;
    private JPanel ReduceNoicePanel;
    private JPanel ReduceDimensionPanel;
    private JPanel NormalizationPanel;
    private JPanel ResultPanel;
    private JPanel LocationContainerPanel;
    private JPanel AlignContainerPanel;
    private JPanel ReduceNoiceContainerPanel;
    private JPanel ReduceDimensionContainerPanel;
    private JPanel NormalizationContainerPanel;
    private JButton LocationHelpButton;
    private JButton LocationRunButton;
    private JPanel ResultPicturePanel;
    private JLabel ResultOriginalSNRLabel;
    private JLabel ResultProcessedSNRLabel;
    private JLabel ResultOriginalPILabel;
    private JLabel ResultProcessedPILabel;
    private JButton AlignHelpButton;
    private JButton AlignRunButton;
    private JButton ReduceNoiceHelpButton;
    private JButton ReduceNoiceRunButton;
    private JButton ReduceDimensionHelpButton;
    private JButton ReduceDimensionRunButton;
    private JButton NormalizationHelpButton;
    private JButton NormalizationRunButton;
    private JCheckBox NICVCheckBox;
    private JCheckBox SOSDCheckBox;
    private JCheckBox SOSTCheckBox;
    private JCheckBox LocationRecommendCheckBox;
    private JCheckBox StaticAlignCheckBox;
    private JCheckBox DTWCheckBox;
    private JCheckBox AlignNoCheckBox;
    private JCheckBox AlignRecommendCheckBox;
    private JCheckBox FFTCheckBox;
    private JCheckBox POCCheckBox;
    private JCheckBox KalmanFilterCheckBox;
    private JCheckBox SSACheckBox;
    private JCheckBox ICACheckBox;
    private JCheckBox ReduceNoiceNoCheckBox;
    private JCheckBox ReduceNoiceRecommendCheckBox;
    private JCheckBox PCACheckBox;
    private JCheckBox LDACheckBox;
    private JCheckBox KPCACheckBox;
    private JCheckBox ReduceDimensionNoCheckBox;
    private JCheckBox ReduceDimensionRecommendCheckBox;
    private JCheckBox zScoreCheckBox;
    private JCheckBox NormalizationNoCheckBox;
    private JCheckBox NormalizationRecommendCheckBox;
    private JPanel AlignCheckBoxPanel;
    private JPanel ReduceNoiceComboBoxPanel;
    private JPanel ReduceDimensionComboboxPanel;
    private JPanel LocationComboboxPanel;
    private JPanel NormalizationComboBoxPanel;
    private JLabel ResultProcessStatusLabel;

    private static JFrame frame;
    private static MainWindow mainWindow;
    private static String curvePath;
    private static String matlabPath;
    private static String resultPath;
    private static String keyPath;
    private static String plainPath;
    private static ChartPanel resultChartPanel;
    private static Set<String> methodSet;   //存放用户选择的预处理方法
    private static List<String> methodList;   //存放将要使用的预处理方法
    private static boolean[] systemRecommendOption;   //是否需要系统推荐
    private static LocationPanel locationPanel; //存放location参数
    private static List<AlignDTWAndReduceNoicePOC> alignDTWAndReduceNoicePOC; //存放DTW和POC参数
    private static List<AlignStaticAlign> alignStaticAlign; //存放静态对齐参数
    private static List<ReduceDimensionPCAAndLDAAndKPCA> reduceDimensionPCAAndLDAAndKPCA; //存放曲线压缩参数
    private static List<ReduceNoiceFFTAndNormalizationZScore> reduceNoiceFFTAndNormalizationZScores; //存放FFT和标准化参数
    private static String lastMethod; // 存放上次进行的预处理方法，便于使用下一个方法时找到文件
    private static ExecutorService preprocessExecutor; // 存放预处理执行线程，使依次执行
    private static ExecutorService statusExecutor; // 存放更新界面线程，使和预处理线程对应并发执行


    private MainWindow(){
        NICVCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(NICVCheckBox.isSelected()){
                    setLocationContainerPanel(NICVCheckBox, "NICV");
                }
                else{
                    NICVCheckBox.setForeground(new Color(187, 187, 187));
                }
            }
        });
        SOSDCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(SOSDCheckBox.isSelected()){
                    setLocationContainerPanel(SOSDCheckBox, "SOSD");
                }
                else{
                    SOSDCheckBox.setForeground(new Color(187, 187, 187));
                }
            }
        });
        SOSTCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(SOSTCheckBox.isSelected()){
                    setLocationContainerPanel(SOSTCheckBox, "SOST");
                }
                else{
                    SOSTCheckBox.setForeground(new Color(187, 187, 187));
                }
            }
        });
        LocationRecommendCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(LocationRecommendCheckBox.isSelected()){
                    setLocationContainerPanel(LocationRecommendCheckBox, "通用");
                    systemRecommendOption[0] = true;
                }
                else{
                    LocationRecommendCheckBox.setForeground(new Color(187, 187, 187));
                    systemRecommendOption[0] = false;
                }
            }
        });
        StaticAlignCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(StaticAlignCheckBox.isSelected()){
                    setAlignContainerPanel(StaticAlignCheckBox, "静态对齐");
                }
                else{
                    StaticAlignCheckBox.setForeground(new Color(187, 187, 187));
                    for(AlignStaticAlign element : alignStaticAlign){
                        if(Objects.equals(element.getName(), "静态对齐参数配置："))
                            alignStaticAlign.remove(element);
                    }
                }
            }
        });
        DTWCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(DTWCheckBox.isSelected()){
                    setAlignContainerPanel(DTWCheckBox, "动态对齐");
                }
                else{
                    DTWCheckBox.setForeground(new Color(187, 187, 187));
                    for(AlignDTWAndReduceNoicePOC element : alignDTWAndReduceNoicePOC){
                        if(Objects.equals(element.getName(), "动态对齐参数配置："))
                            alignDTWAndReduceNoicePOC.remove(element);
                    }
                }
            }
        });
        AlignNoCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(AlignNoCheckBox.isSelected()){
                    setAlignContainerPanel(AlignNoCheckBox, "不使用");
                }
                else{
                    AlignNoCheckBox.setForeground(new Color(187, 187, 187));
                }
            }
        });
        AlignRecommendCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(AlignRecommendCheckBox.isSelected()){
                    setAlignContainerPanel(AlignRecommendCheckBox, "系统推荐");
                    systemRecommendOption[1] = true;
                }
                else{
                    AlignRecommendCheckBox.setForeground(new Color(187, 187, 187));
                    systemRecommendOption[1] = false;
                }
            }
        });
        FFTCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(FFTCheckBox.isSelected()){
                    setReduceNoiceContainerPanel(FFTCheckBox, "FFT");
                }
                else{
                    FFTCheckBox.setForeground(new Color(187, 187, 187));
                    for(ReduceNoiceFFTAndNormalizationZScore element : reduceNoiceFFTAndNormalizationZScores){
                        if(Objects.equals(element.getName(),"FFT参数配置：" ))
                            reduceNoiceFFTAndNormalizationZScores.remove(element);
                    }
                }
            }
        });
        POCCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(POCCheckBox.isSelected()){
                    setReduceNoiceContainerPanel(POCCheckBox, "POC");
                }
                else{
                    POCCheckBox.setForeground(new Color(187, 187, 187));
                    for(AlignDTWAndReduceNoicePOC element : alignDTWAndReduceNoicePOC){
                        if(Objects.equals(element.getName(), "POC参数配置："))
                            alignDTWAndReduceNoicePOC.remove(element);
                    }
                }
            }
        });
        KalmanFilterCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(KalmanFilterCheckBox.isSelected()){
                    setReduceNoiceContainerPanel(KalmanFilterCheckBox, "卡尔曼滤波");
                }
                else{
                    KalmanFilterCheckBox.setForeground(new Color(187, 187, 187));
                }
            }
        });
        SSACheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(SSACheckBox.isSelected()){
                    setReduceNoiceContainerPanel(SSACheckBox, "SSA");
                }
                else{
                    SSACheckBox.setForeground(new Color(187, 187, 187));
                }
            }
        });
        ICACheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(ICACheckBox.isSelected()){
                    setReduceNoiceContainerPanel(ICACheckBox, "ICA");
                }
                else{
                    ICACheckBox.setForeground(new Color(187, 187, 187));
                }
            }
        });
        ReduceNoiceNoCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(ReduceNoiceNoCheckBox.isSelected()){
                    setReduceNoiceContainerPanel(ReduceNoiceNoCheckBox, "不使用");
                }
                ReduceNoiceNoCheckBox.setForeground(new Color(187, 187, 187));
            }
        });
        ReduceNoiceRecommendCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(ReduceNoiceRecommendCheckBox.isSelected()){
                    setReduceNoiceContainerPanel(ReduceNoiceRecommendCheckBox, "系统推荐");
                    systemRecommendOption[2] = true;
                }
                else{
                    ReduceNoiceRecommendCheckBox.setForeground(new Color(187, 187, 187));
                    systemRecommendOption[2] = false;
                }
            }
        });
        PCACheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(PCACheckBox.isSelected()){
                    setReduceDimensionContainerPanel(PCACheckBox, "PCA");
                }
                else{
                    PCACheckBox.setForeground(new Color(187, 187, 187));
                    for(ReduceDimensionPCAAndLDAAndKPCA element : reduceDimensionPCAAndLDAAndKPCA){
                        if(Objects.equals(element.getName(), "PCA参数配置："))
                            reduceDimensionPCAAndLDAAndKPCA.remove(element);
                    }
                }
            }
        });
        LDACheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(LDACheckBox.isSelected()){
                    setReduceDimensionContainerPanel(LDACheckBox, "LDA");
                }
                else{
                    LDACheckBox.setForeground(new Color(187, 187, 187));
                    for(ReduceDimensionPCAAndLDAAndKPCA element : reduceDimensionPCAAndLDAAndKPCA){
                        if(Objects.equals(element.getName(), "LDA参数配置："))
                            reduceDimensionPCAAndLDAAndKPCA.remove(element);
                    }
                }
            }
        });
        KPCACheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(KPCACheckBox.isSelected()){
                    setReduceDimensionContainerPanel(KPCACheckBox, "KPCA");
                }
                else{
                    KPCACheckBox.setForeground(new Color(187, 187, 187));
                    for(ReduceDimensionPCAAndLDAAndKPCA element : reduceDimensionPCAAndLDAAndKPCA){
                        if(Objects.equals(element.getName(), "KPCA参数配置："))
                            reduceDimensionPCAAndLDAAndKPCA.remove(element);
                    }
                }
            }
        });
        ReduceDimensionNoCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(ReduceDimensionNoCheckBox.isSelected()){
                    setReduceDimensionContainerPanel(ReduceDimensionNoCheckBox, "不使用");
                }
                else{
                    ReduceDimensionNoCheckBox.setForeground(new Color(187, 187, 187));
                }
            }
        });
        ReduceDimensionRecommendCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(ReduceDimensionRecommendCheckBox.isSelected()){
                    setReduceDimensionContainerPanel(ReduceDimensionRecommendCheckBox, "系统推荐");
                    systemRecommendOption[3] = true;
                }
                else{
                    ReduceDimensionRecommendCheckBox.setForeground(new Color(187, 187, 187));
                    systemRecommendOption[3] = false;
                }
            }
        });
        zScoreCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(zScoreCheckBox.isSelected()){
                    setNormalizationContainerPanel(zScoreCheckBox, "Z-Score");
                }
                else{
                    zScoreCheckBox.setForeground(new Color(187, 187, 187));
                    for(ReduceNoiceFFTAndNormalizationZScore element : reduceNoiceFFTAndNormalizationZScores){
                        if(Objects.equals(element.getName(), "Z-Score参数配置："))
                            reduceNoiceFFTAndNormalizationZScores.remove(element);
                    }
                }
            }
        });
        NormalizationNoCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(NormalizationNoCheckBox.isSelected()){
                    setNormalizationContainerPanel(NormalizationNoCheckBox, "不使用");
                }
                else{
                    NormalizationNoCheckBox.setForeground(new Color(187, 187, 187));
                }
            }
        });
        NormalizationRecommendCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(NormalizationRecommendCheckBox.isSelected()){
                    setNormalizationContainerPanel(NormalizationRecommendCheckBox, "系统推荐");
                    systemRecommendOption[4] = true;
                }
                else{
                    NormalizationRecommendCheckBox.setForeground(new Color(187, 187, 187));
                    systemRecommendOption[4] = false;
                }
            }
        });

        LocationRunButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                FilePathDialog filePathDialog = new FilePathDialog();
                filePathDialog.pack();
                filePathDialog.setVisible(true);

                configTheFilesPath(filePathDialog);
                configResultPanel();

                String locationMethod = figureOutLocationMethod();
                LocationToolBox locationToolBox = new LocationFactory().createLocationToolBox(locationMethod);
                Thread locationProcessThread = new Thread(()->{
                    Thread.currentThread().setName("locationProcessThread");
                    try{
                        resultChartPanel = locationToolBox.excuteLocation(locationPanel, resultPath);
                        mainWindow.ResultPicturePanel.setLayout(new BorderLayout());
                        mainWindow.ResultPicturePanel.add(resultChartPanel, BorderLayout.CENTER);
                        mainWindow.ResultPicturePanel.updateUI();
                        Thread.sleep(3);
                    }
                    catch(InterruptedException | IOException e1){
                        e1.printStackTrace();
                    }
                });

                Thread locationProcessStatusThread = new Thread(()->{
                    Thread.currentThread().setName("locationProcessStatusThread");
                    try{
                        Thread.sleep(500); //等待locationProcessThread中excuteLocation配置好attackNumber
                        while(locationToolBox.getProcessStatus() > 0){
                            mainWindow.ResultProcessStatusLabel.setText("已处理条数："+(locationToolBox.getProcessStatus() + 1));
                            Thread.sleep(1); //尽量小一些，保证可以读取到999
                        }
                    }
                    catch(InterruptedException e1){
                        e1.printStackTrace();
                    }
                });

                locationProcessThread.start();
                locationProcessStatusThread.start();
            }
        });
        AlignRunButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                tabbedPane1.setSelectedIndex(2);
            }
        });
        ReduceNoiceRunButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                tabbedPane1.setSelectedIndex(3);
            }
        });
        ReduceDimensionRunButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                tabbedPane1.setSelectedIndex(4);
            }
        });
        NormalizationRunButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                updateMethodSet();
                PreprocessOrderConfirmDialog preprocessOrderConfirmDialog = new PreprocessOrderConfirmDialog(methodSet, systemRecommendOption);
                preprocessOrderConfirmDialog.pack();
                preprocessOrderConfirmDialog.setVisible(true);
                methodList = preprocessOrderConfirmDialog.result;

                FilePathDialog filePathDialog = new FilePathDialog();
                filePathDialog.pack();
                filePathDialog.setVisible(true);

                configTheFilesPath(filePathDialog);
                configResultPanel();

                // TODO:具体执行预处理过程
                for(int i=0; i<=methodList.size()-1; i++){
                    String factoryName = makeSureUseWhichFactory(methodList.get(i));
                    excutePreprocessDependsOnFactoryName(factoryName, methodList.get(i));
                }
            }
        });
    }

    private void setFrameContribution(JFrame frame, MainWindow mainWindow){
        Image icon = Toolkit.getDefaultToolkit().getImage("");
        frame.setIconImage(icon);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // get the screen size
        frame.setBounds((int) (screenSize.width*0.125), (int) (screenSize.height*0.15), (int) (screenSize.width*0.75), (int) (screenSize.height*0.7));
        Dimension preferSize = new Dimension((int) (screenSize.width*0.75), (int) (screenSize.height*0.7));
        frame.setPreferredSize(preferSize);
        frame.setContentPane(mainWindow.mainPanel);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void setLocationContainerPanel(JCheckBox checkBox, String method){
        mainWindow.LocationContainerPanel.removeAll();
        mainWindow.LocationContainerPanel.setLayout(new BorderLayout());

        if(Objects.equals(method, "通用")){
            mainWindow.NICVCheckBox.setSelected(false);
            mainWindow.SOSDCheckBox.setSelected(false);
            mainWindow.SOSTCheckBox.setSelected(false);
        }
        else if(Objects.equals(method, "NICV")){
            mainWindow.LocationRecommendCheckBox.setSelected(false);
            mainWindow.SOSDCheckBox.setSelected(false);
            mainWindow.SOSTCheckBox.setSelected(false);
        }
        else if(Objects.equals(method, "SOSD")){
            mainWindow.LocationRecommendCheckBox.setSelected(false);
            mainWindow.NICVCheckBox.setSelected(false);
            mainWindow.SOSTCheckBox.setSelected(false);
        }
        else if(Objects.equals(method, "SOST")){
            mainWindow.LocationRecommendCheckBox.setSelected(false);
            mainWindow.NICVCheckBox.setSelected(false);
            mainWindow.SOSDCheckBox.setSelected(false);
        }

        locationPanel.setLable(method);
        mainWindow.LocationContainerPanel.add(locationPanel.getPanel());

        checkBox.setForeground(Color.green);
        mainWindow.LocationContainerPanel.updateUI();
    }

    private void setAlignContainerPanel(JCheckBox checkBox, String method){
        mainWindow.AlignContainerPanel.removeAll();
        mainWindow.AlignContainerPanel.setLayout(new BorderLayout());

        if(Objects.equals(method, "静态对齐")){
            AlignStaticAlign temp = new AlignStaticAlign();
            temp.setLable(method);
            mainWindow.AlignContainerPanel.add(temp.getPanel());
            alignStaticAlign.add(temp);
            mainWindow.AlignRecommendCheckBox.setSelected(false);
            mainWindow.AlignNoCheckBox.setSelected(false);
        }
        else if(Objects.equals(method, "动态对齐")){
            AlignDTWAndReduceNoicePOC temp = new AlignDTWAndReduceNoicePOC();
            temp.setLable(method);
            mainWindow.AlignContainerPanel.add(temp.getPanel());
            alignDTWAndReduceNoicePOC.add(temp);
            mainWindow.AlignRecommendCheckBox.setSelected(false);
            mainWindow.AlignNoCheckBox.setSelected(false);
        }
        else if(Objects.equals(method, "不使用")){
            mainWindow.StaticAlignCheckBox.setSelected(false);
            mainWindow.DTWCheckBox.setSelected(false);
            mainWindow.AlignRecommendCheckBox.setSelected(false);
            mainWindow.AlignContainerPanel.removeAll();
        }
        else if(Objects.equals(method, "系统推荐")){
            mainWindow.StaticAlignCheckBox.setSelected(false);
            mainWindow.DTWCheckBox.setSelected(false);
            mainWindow.AlignNoCheckBox.setSelected(false);
            //TODO
        }

        checkBox.setForeground(Color.green);
        mainWindow.AlignContainerPanel.updateUI();
    }

    private void setReduceNoiceContainerPanel(JCheckBox checkBox, String method){
        mainWindow.ReduceNoiceContainerPanel.removeAll();
        mainWindow.ReduceNoiceContainerPanel.setLayout(new BorderLayout());

        if(Objects.equals(method, "FFT")){
            ReduceNoiceFFTAndNormalizationZScore temp = new ReduceNoiceFFTAndNormalizationZScore();
            temp.setLable(method);
            mainWindow.ReduceNoiceContainerPanel.add(temp.getPanel());
            reduceNoiceFFTAndNormalizationZScores.add(temp);
            mainWindow.ReduceNoiceRecommendCheckBox.setSelected(false);
            mainWindow.ReduceNoiceNoCheckBox.setSelected(false);
        }
        else if(Objects.equals(method, "POC")){
            AlignDTWAndReduceNoicePOC temp = new AlignDTWAndReduceNoicePOC();
            temp.setLable(method);
            mainWindow.ReduceNoiceContainerPanel.add(temp.getPanel());
            alignDTWAndReduceNoicePOC.add(temp);
            mainWindow.ReduceNoiceRecommendCheckBox.setSelected(false);
            mainWindow.ReduceNoiceNoCheckBox.setSelected(false);
        }
        else if(Objects.equals(method, "卡尔曼滤波")){

            mainWindow.ReduceNoiceRecommendCheckBox.setSelected(false);
            mainWindow.ReduceNoiceNoCheckBox.setSelected(false);
        }
        else if(Objects.equals(method, "SSA")){

            mainWindow.ReduceNoiceRecommendCheckBox.setSelected(false);
            mainWindow.ReduceNoiceNoCheckBox.setSelected(false);
        }
        else if(Objects.equals(method, "ICA")){

            mainWindow.ReduceNoiceRecommendCheckBox.setSelected(false);
            mainWindow.ReduceNoiceNoCheckBox.setSelected(false);
        }
        else if(Objects.equals(method, "不使用")){
            mainWindow.FFTCheckBox.setSelected(false);
            mainWindow.POCCheckBox.setSelected(false);
            mainWindow.KalmanFilterCheckBox.setSelected(false);
            mainWindow.SSACheckBox.setSelected(false);
            mainWindow.ICACheckBox.setSelected(false);
            mainWindow.ReduceNoiceRecommendCheckBox.setSelected(false);
            mainWindow.ReduceNoiceContainerPanel.removeAll();
        }
        else if(Objects.equals(method, "系统推荐")){
            mainWindow.FFTCheckBox.setSelected(false);
            mainWindow.POCCheckBox.setSelected(false);
            mainWindow.KalmanFilterCheckBox.setSelected(false);
            mainWindow.SSACheckBox.setSelected(false);
            mainWindow.ICACheckBox.setSelected(false);
            mainWindow.ReduceNoiceNoCheckBox.setSelected(false);
            //TODO
        }

        checkBox.setForeground(Color.green);
        mainWindow.ReduceNoiceContainerPanel.updateUI();
    }

    private void setReduceDimensionContainerPanel(JCheckBox checkBox, String method){
        mainWindow.ReduceDimensionContainerPanel.removeAll();
        mainWindow.ReduceDimensionContainerPanel.setLayout(new BorderLayout());
        if(Objects.equals(method, "PCA") || Objects.equals(method, "LDA") || Objects.equals(method, "KPCA")){
            ReduceDimensionPCAAndLDAAndKPCA temp = new ReduceDimensionPCAAndLDAAndKPCA();
            temp.setLable(method);
            mainWindow.ReduceDimensionContainerPanel.add(temp.getPanel());
            reduceDimensionPCAAndLDAAndKPCA.add(temp);
            mainWindow.ReduceDimensionRecommendCheckBox.setSelected(false);
            mainWindow.ReduceDimensionNoCheckBox.setSelected(false);
        }
        else if(Objects.equals(method, "不使用")){
            mainWindow.PCACheckBox.setSelected(false);
            mainWindow.LDACheckBox.setSelected(false);
            mainWindow.KPCACheckBox.setSelected(false);
            mainWindow.ReduceDimensionRecommendCheckBox.setSelected(false);
            mainWindow.ReduceNoiceContainerPanel.removeAll();
        }
        else if(Objects.equals(method, "系统推荐")){
            mainWindow.PCACheckBox.setSelected(false);
            mainWindow.LDACheckBox.setSelected(false);
            mainWindow.KPCACheckBox.setSelected(false);
            mainWindow.ReduceDimensionNoCheckBox.setSelected(false);
            //TODO
        }

        checkBox.setForeground(Color.green);
        mainWindow.ReduceDimensionContainerPanel.updateUI();
    }

    private void setNormalizationContainerPanel(JCheckBox checkBox, String method){
        mainWindow.NormalizationContainerPanel.removeAll();
        mainWindow.NormalizationContainerPanel.setLayout(new BorderLayout());

        if(Objects.equals(method, "Z-Score")){
            ReduceNoiceFFTAndNormalizationZScore temp = new ReduceNoiceFFTAndNormalizationZScore();
            temp.setLable(method);
            mainWindow.NormalizationContainerPanel.add(temp.getPanel());
            reduceNoiceFFTAndNormalizationZScores.add(temp);
            mainWindow.NormalizationRecommendCheckBox.setSelected(false);
            mainWindow.NormalizationNoCheckBox.setSelected(false);
        }
        else if(Objects.equals(method, "不使用")){
            mainWindow.zScoreCheckBox.setSelected(false);
            mainWindow.NormalizationRecommendCheckBox.setSelected(false);
            mainWindow.NormalizationContainerPanel.removeAll();
        }
        else if(Objects.equals(method, "系统推荐")){
            mainWindow.zScoreCheckBox.setSelected(false);
            mainWindow.NormalizationNoCheckBox.setSelected(false);
            //TODO
        }

        checkBox.setForeground(Color.green);
        mainWindow.NormalizationContainerPanel.updateUI();
    }

    private void updateMethodSet(){
        if(StaticAlignCheckBox.isSelected()){
            methodSet.add("静态对齐");
        }
        if(DTWCheckBox.isSelected()){
            methodSet.add("动态对齐");
        }
        if(FFTCheckBox.isSelected()){
            methodSet.add("FFT");
        }
        if(POCCheckBox.isSelected()){
            methodSet.add("POC");
        }
        if(KalmanFilterCheckBox.isSelected()){
            methodSet.add("卡尔曼滤波");
        }
        if(SSACheckBox.isSelected()){
            methodSet.add("SSA");
        }
        if(ICACheckBox.isSelected()){
            methodSet.add("ICA");
        }
        if(PCACheckBox.isSelected()){
            methodSet.add("PCA");
        }
        if(LDACheckBox.isSelected()){
            methodSet.add("LDA");
        }
        if(KPCACheckBox.isSelected()){
            methodSet.add("KPCA");
        }
        if(zScoreCheckBox.isSelected()){
            methodSet.add("Z-Score");
        }

        if(AlignRecommendCheckBox.isSelected()){
            methodSet.add("静态对齐");
            methodSet.add("动态对齐");
        }
        if(ReduceNoiceRecommendCheckBox.isSelected()){
            methodSet.add("FFT");
            methodSet.add("POC");
            methodSet.add("卡尔曼滤波");
            methodSet.add("SSA");
            methodSet.add("ICA");
        }
        if(ReduceDimensionRecommendCheckBox.isSelected()){
            methodSet.add("PCA");
            methodSet.add("LDA");
            methodSet.add("KPCA");
        }
        if(NormalizationRecommendCheckBox.isSelected()){
            methodSet.add("Z-Score");
        }
    }

    private String figureOutLocationMethod(){
        if(mainWindow.NICVCheckBox.isSelected()){
            return "NICV";
        }
        else if(mainWindow.SOSDCheckBox.isSelected()){
            return "SOSD";
        }
        else if(mainWindow.SOSTCheckBox.isSelected()){
            return "SOST";
        }
        else if(LocationRecommendCheckBox.isSelected()){
            return "NICV"; // TODO:choose which one???
        }

        return null;//TODO:throw exception!!!
    }

    private void configTheFilesPath(FilePathDialog filePathDialog){
        curvePath = filePathDialog.curvePath;
        matlabPath = filePathDialog.matlabPath;
        resultPath = filePathDialog.resultPath;
        keyPath = resultPath+"\\key.txt";
        plainPath = resultPath+"\\plain.txt";
    }

    private void configResultPanel(){
        tabbedPane1.setSelectedIndex(5);
        mainWindow.ResultPicturePanel.removeAll();
    }

    private String makeSureUseWhichFactory(String methodName){
        List<String> alignList = new ArrayList<>();
        List<String> reduceNoiceList = new ArrayList<>();
        List<String> reduceDimensionList = new ArrayList<>();
        List<String> normalizationList = new ArrayList<>();
        alignList.add("静态对齐");
        alignList.add("动态对齐");
        reduceNoiceList.add("FFT");
        reduceNoiceList.add("POC");
        reduceNoiceList.add("卡尔曼滤波");
        reduceNoiceList.add("SSA");
        reduceNoiceList.add("ICA");
        reduceDimensionList.add("PCA");
        reduceDimensionList.add("LDA");
        reduceDimensionList.add("KPCA");
        normalizationList.add("Z-Score");
        if(alignList.contains(methodName))
            return "align";
        else if(reduceNoiceList.contains(methodName))
            return "reduceNoice";
        else if(reduceDimensionList.contains(methodName))
            return "reduceDimension";
        else if(normalizationList.contains(methodName))
            return "normalization";

        return null; // TODO : 抛出异常
    }

    private void excutePreprocessDependsOnFactoryName(String factoryName, String preprocessMethod){
        if(Objects.equals(factoryName, "align")){
            excuteAlign(preprocessMethod);
        }
        else if(Objects.equals(factoryName, "reduceNoice")){
            excuteReduceNoice(preprocessMethod);
        }
        else if(Objects.equals(factoryName, "reduceDimension")){
            excuteReduceDimension(preprocessMethod);
        }
        else if(Objects.equals(factoryName, "normalization")){
            excuteNormalization(preprocessMethod);
        }
    }

    private void excuteAlign(String alignMethod){
        AlignToolBox alignToolBox = new AlignFactory().createAlignToolBox(alignMethod);
        excuteConcreteAlign(alignToolBox, alignMethod);
    }

    private void excuteReduceNoice(String reduceNoiceMethod){
        ReduceNoiceToolBox reduceNoiceToolBox = new ReduceNoiceFactory().createRedeceNoiceToolBox(reduceNoiceMethod);
        excuteConcreteReduceNoice(reduceNoiceToolBox, reduceNoiceMethod);
    }

    private void excuteReduceDimension(String reduceDimensionMethod){

    }

    private void excuteNormalization(String normalizationMethod){

    }

    private void excuteConcreteAlign(AlignToolBox alignToolBox, String alignMethod){
        if(Objects.equals(alignMethod, "静态对齐")){
            StaticAlign staticAlign = (StaticAlign) alignToolBox;
            AlignStaticAlign temp = alignStaticAlign.get(0);
            Thread alignProcessThread = new Thread(()->{
                Thread.currentThread().setName("alignProcessThread");
                try{
                    resultChartPanel = staticAlign.excuteAlign(temp, resultPath, lastMethod);
                    lastMethod = "StaticAlign";
                    mainWindow.ResultPicturePanel.removeAll();
                    mainWindow.ResultPicturePanel.setLayout(new BorderLayout());
                    mainWindow.ResultPicturePanel.add(resultChartPanel, BorderLayout.CENTER);
                    mainWindow.ResultPicturePanel.updateUI();
                    Thread.sleep(3);
                }
                catch(InterruptedException | IOException e1){
                    e1.printStackTrace();
                }
            });

            Thread alignProcessStatusThread = new Thread(()->{
                Thread.currentThread().setName("locationProcessStatusThread");
                try{
                    Thread.sleep(500); //等待locationProcessThread中excuteLocation配置好attackNumber
                    while(alignToolBox.getProcessStatus() > 0){
                        mainWindow.ResultProcessStatusLabel.setText("已处理条数："+(alignToolBox.getProcessStatus() + 1));
                        Thread.sleep(1); //尽量小一些，保证可以读取到999
                    }
                }
                catch(InterruptedException e1){
                    e1.printStackTrace();
                }
            });

            preprocessExecutor.submit(alignProcessThread);
            statusExecutor.submit(alignProcessStatusThread);
        }
        else if(Objects.equals(alignMethod, "动态对齐")){
            DTW staticAlign = (DTW) alignToolBox;
            AlignDTWAndReduceNoicePOC temp = searchDTWAndPOCPanel("动态对齐");
            Thread alignProcessThread = new Thread(()->{
                Thread.currentThread().setName("alignProcessThread");
                try{
                    resultChartPanel = staticAlign.excuteAlign(temp, resultPath, lastMethod);
                    lastMethod = "DTW"; //根据对应方法里写文件的名称
                    mainWindow.ResultPicturePanel.removeAll();
                    mainWindow.ResultPicturePanel.setLayout(new BorderLayout());
                    mainWindow.ResultPicturePanel.add(resultChartPanel, BorderLayout.CENTER);
                    mainWindow.ResultPicturePanel.updateUI();
                    Thread.sleep(3);
                }
                catch(InterruptedException | IOException e1){
                    e1.printStackTrace();
                }
            });

            Thread alignProcessStatusThread = new Thread(()->{
                Thread.currentThread().setName("alignProcessStatusThread");
                try{
                    Thread.sleep(500); //等待locationProcessThread中excuteLocation配置好attackNumber
                    while(alignToolBox.getProcessStatus() > 0){
                        mainWindow.ResultProcessStatusLabel.setText("已处理条数："+(alignToolBox.getProcessStatus() + 1));
                        Thread.sleep(1); //尽量小一些，保证可以读取到999
                    }
                }
                catch(InterruptedException e1){
                    e1.printStackTrace();
                }
            });

            preprocessExecutor.submit(alignProcessThread);
            statusExecutor.submit(alignProcessStatusThread);
        }
    }

    private void excuteConcreteReduceNoice(ReduceNoiceToolBox reduceNoiceToolBox, String reduceNoiceMethod){
        if(Objects.equals(reduceNoiceMethod, "FFT")){
            FFT fft = (FFT) reduceNoiceToolBox;
            ReduceNoiceFFTAndNormalizationZScore temp = searchFFTAndZscorePanel("FFT");
            Thread reduceNoiceProcessThread = new Thread(()->{
                Thread.currentThread().setName("reduceNoiceProcessThread");
                try{
                    resultChartPanel = fft.excuteReduceNoice(temp, resultPath, lastMethod);
                    lastMethod = "FFT";
                    mainWindow.ResultPicturePanel.removeAll();
                    mainWindow.ResultPicturePanel.setLayout(new BorderLayout());
                    mainWindow.ResultPicturePanel.add(resultChartPanel, BorderLayout.CENTER);
                    mainWindow.ResultPicturePanel.updateUI();
                    Thread.sleep(3);
                }
                catch(InterruptedException | IOException e1){
                    e1.printStackTrace();
                }
            });

            Thread reduceNoiceProcessStatusThread = new Thread(()->{
                Thread.currentThread().setName("reduceNoiceProcessStatusThread");
                try{
                    Thread.sleep(500); //等待locationProcessThread中excuteLocation配置好attackNumber
                    while(reduceNoiceToolBox.getProcessStatus() > 0){
                        mainWindow.ResultProcessStatusLabel.setText("已处理条数："+(reduceNoiceToolBox.getProcessStatus() + 1));
                        Thread.sleep(1); //尽量小一些，保证可以读取到999
                    }
                }
                catch(InterruptedException e1){
                    e1.printStackTrace();
                }
            });

            preprocessExecutor.submit(reduceNoiceProcessThread);
            statusExecutor.submit(reduceNoiceProcessStatusThread);
        }
        else if(Objects.equals(reduceNoiceMethod, "POC")){
            POC poc = (POC) reduceNoiceToolBox;
            AlignDTWAndReduceNoicePOC temp = searchDTWAndPOCPanel("POC");
            Thread reduceNoiceProcessThread = new Thread(()->{
                Thread.currentThread().setName("reduceNoiceProcessThread");
                try{
                    resultChartPanel = poc.excuteReduceNoice(temp, resultPath, lastMethod);
                    lastMethod = "POC";
                    mainWindow.ResultPicturePanel.removeAll();
                    mainWindow.ResultPicturePanel.setLayout(new BorderLayout());
                    mainWindow.ResultPicturePanel.add(resultChartPanel, BorderLayout.CENTER);
                    mainWindow.ResultPicturePanel.updateUI();
                    Thread.sleep(3);
                }
                catch(InterruptedException | IOException e1){
                    e1.printStackTrace();
                }
            });

            Thread reduceNoiceProcessStatusThread = new Thread(()->{
                Thread.currentThread().setName("reduceNoiceProcessStatusThread");
                try{
                    Thread.sleep(500); //等待locationProcessThread中excuteLocation配置好attackNumber
                    while(reduceNoiceToolBox.getProcessStatus() > 0){
                        mainWindow.ResultProcessStatusLabel.setText("已处理条数："+(reduceNoiceToolBox.getProcessStatus() + 1));
                        Thread.sleep(1); //尽量小一些，保证可以读取到999
                    }
                }
                catch(InterruptedException e1){
                    e1.printStackTrace();
                }
            });

            preprocessExecutor.submit(reduceNoiceProcessThread);
            statusExecutor.submit(reduceNoiceProcessStatusThread);
        }
    }

    private AlignDTWAndReduceNoicePOC searchDTWAndPOCPanel(String method){
        for(int i=0; i<=alignDTWAndReduceNoicePOC.size()-1; i++){
            if(alignDTWAndReduceNoicePOC.get(i).getName().contains(method))
                return alignDTWAndReduceNoicePOC.get(i);
        }
        return null; //TODO: 抛出异常
    }

    private ReduceNoiceFFTAndNormalizationZScore searchFFTAndZscorePanel(String method){
        for(int i=0; i<=reduceNoiceFFTAndNormalizationZScores.size()-1; i++){
            if(reduceNoiceFFTAndNormalizationZScores.get(i).getName().contains(method))
                return reduceNoiceFFTAndNormalizationZScores.get(i);
        }
        return null; //TODO: 抛出异常
    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException{
        // 设置UIManager
        UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
        UIManager.put("CheckBox.font", new Font("Microsoft YaHei", Font.BOLD, 18));
        UIManager.put("Button.font", new Font("Microsoft YaHei", Font.BOLD, 16));
        UIManager.put("TextField.font", new Font("Microsoft YaHei Light", Font.BOLD, 18));

        // 初始化frame、mainwindow、methodSet、其他Panel
        mainWindow = new MainWindow();
        frame = new JFrame("Preprocess");
        methodSet = new HashSet<>();
        systemRecommendOption = new boolean[5];
        locationPanel = new LocationPanel();
        alignDTWAndReduceNoicePOC = new ArrayList<>();
        alignStaticAlign = new ArrayList<>();
        reduceDimensionPCAAndLDAAndKPCA = new ArrayList<>();
        reduceNoiceFFTAndNormalizationZScores = new ArrayList<>();
        preprocessExecutor = Executors.newSingleThreadExecutor();
        statusExecutor = Executors.newSingleThreadExecutor();
        lastMethod = "wave";

        // 设置frame属性并显示
        mainWindow.setFrameContribution(frame, mainWindow);
    }
}
