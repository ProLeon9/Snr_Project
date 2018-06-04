package panel;

import location.LocationFactory;
import location.LocationToolBox;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;
import java.util.List;

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
    private static Set<String> methodSet;   //����û�ѡ���Ԥ��������
    private static List<String> methodList;   //��Ž�Ҫʹ�õ�Ԥ��������
    private static boolean[] systemRecommendOption;   //�Ƿ���Ҫϵͳ�Ƽ�
    private static LocationPanel locationPanel; //���ڴ��location����
    private static String curvePath;
    private static String matlabPath;
    private static String resultPath;
    private static String keyPath;
    private static String plainPath;
    private static ChartPanel resultChartPanel;


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
                    setLocationContainerPanel(LocationRecommendCheckBox, "ͨ��");
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
                    setAlignContainerPanel(StaticAlignCheckBox, "��̬����");
                }
                else{
                    StaticAlignCheckBox.setForeground(new Color(187, 187, 187));
                }
            }
        });
        DTWCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(DTWCheckBox.isSelected()){
                    setAlignContainerPanel(DTWCheckBox, "��̬����");
                }
                else{
                    DTWCheckBox.setForeground(new Color(187, 187, 187));
                }
            }
        });
        AlignNoCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(AlignNoCheckBox.isSelected()){
                    setAlignContainerPanel(AlignNoCheckBox, "��ʹ��");
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
                    setAlignContainerPanel(AlignRecommendCheckBox, "ϵͳ�Ƽ�");
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
                }
            }
        });
        KalmanFilterCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(KalmanFilterCheckBox.isSelected()){
                    setReduceNoiceContainerPanel(KalmanFilterCheckBox, "�������˲�");
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
                    setReduceNoiceContainerPanel(ReduceNoiceNoCheckBox, "��ʹ��");
                }
                ReduceNoiceNoCheckBox.setForeground(new Color(187, 187, 187));
            }
        });
        ReduceNoiceRecommendCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(ReduceNoiceRecommendCheckBox.isSelected()){
                    setReduceNoiceContainerPanel(ReduceNoiceRecommendCheckBox, "ϵͳ�Ƽ�");
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
                }
            }
        });
        ReduceDimensionNoCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(ReduceDimensionNoCheckBox.isSelected()){
                    setReduceDimensionContainerPanel(ReduceDimensionNoCheckBox, "��ʹ��");
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
                    setReduceDimensionContainerPanel(ReduceDimensionRecommendCheckBox, "ϵͳ�Ƽ�");
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
                }
            }
        });
        NormalizationNoCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(NormalizationNoCheckBox.isSelected()){
                    setNormalizationContainerPanel(NormalizationNoCheckBox, "��ʹ��");
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
                    setNormalizationContainerPanel(NormalizationRecommendCheckBox, "ϵͳ�Ƽ�");
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
                curvePath = filePathDialog.curvePath;
                matlabPath = filePathDialog.matlabPath;
                resultPath = filePathDialog.resultPath;
                keyPath = resultPath+"\\key.txt";
                plainPath = resultPath+"\\plain.txt";

                tabbedPane1.setSelectedIndex(5);
                mainWindow.ResultPicturePanel.removeAll();
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
                        Thread.sleep(100); //�ȴ�locationProcessThread��excuteLocation���ú�attackNumber
                        while(locationToolBox.getProcessStatus() > 0){
                            mainWindow.ResultProcessStatusLabel.setText("�Ѵ���������"+(locationToolBox.getProcessStatus() + 1));
                            Thread.sleep(3);
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
                tabbedPane1.setSelectedIndex(5);
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

        if(Objects.equals(method, "ͨ��")){
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

        if(Objects.equals(method, "��̬����")){
            AlignStaticAlign alignStaticAlign = new AlignStaticAlign();
            alignStaticAlign.setLable(method);
            mainWindow.AlignContainerPanel.add(alignStaticAlign.getPanel());
            mainWindow.AlignRecommendCheckBox.setSelected(false);
            mainWindow.AlignNoCheckBox.setSelected(false);
        }
        else if(Objects.equals(method, "��̬����")){
            AlignDTWAndReduceNoicePOC alignDTWAndReduceNoicePOC = new AlignDTWAndReduceNoicePOC();
            alignDTWAndReduceNoicePOC.setLable(method);
            mainWindow.AlignContainerPanel.add(alignDTWAndReduceNoicePOC.getPanel());
            mainWindow.AlignRecommendCheckBox.setSelected(false);
            mainWindow.AlignNoCheckBox.setSelected(false);
        }
        else if(Objects.equals(method, "��ʹ��")){
            mainWindow.StaticAlignCheckBox.setSelected(false);
            mainWindow.DTWCheckBox.setSelected(false);
            mainWindow.AlignRecommendCheckBox.setSelected(false);
            mainWindow.AlignContainerPanel.removeAll();
        }
        else if(Objects.equals(method, "ϵͳ�Ƽ�")){
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
            ReduceNoiceFFTAndNormalizationZScore reduceNoiceFFTAndNormalizationZScore = new ReduceNoiceFFTAndNormalizationZScore();
            reduceNoiceFFTAndNormalizationZScore.setLable(method);
            mainWindow.ReduceNoiceContainerPanel.add(reduceNoiceFFTAndNormalizationZScore.getPanel());
            mainWindow.ReduceNoiceRecommendCheckBox.setSelected(false);
            mainWindow.ReduceNoiceNoCheckBox.setSelected(false);
        }
        else if(Objects.equals(method, "POC")){
            AlignDTWAndReduceNoicePOC alignDTWAndReduceNoicePOC = new AlignDTWAndReduceNoicePOC();
            alignDTWAndReduceNoicePOC.setLable(method);
            mainWindow.ReduceNoiceContainerPanel.add(alignDTWAndReduceNoicePOC.getPanel());
            mainWindow.ReduceNoiceRecommendCheckBox.setSelected(false);
            mainWindow.ReduceNoiceNoCheckBox.setSelected(false);
        }
        else if(Objects.equals(method, "�������˲�")){

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
        else if(Objects.equals(method, "��ʹ��")){
            mainWindow.FFTCheckBox.setSelected(false);
            mainWindow.POCCheckBox.setSelected(false);
            mainWindow.KalmanFilterCheckBox.setSelected(false);
            mainWindow.SSACheckBox.setSelected(false);
            mainWindow.ICACheckBox.setSelected(false);
            mainWindow.ReduceNoiceRecommendCheckBox.setSelected(false);
            mainWindow.ReduceNoiceContainerPanel.removeAll();
        }
        else if(Objects.equals(method, "ϵͳ�Ƽ�")){
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
            ReduceDimensionPCAAndLDAAndKPCA reduceDimensionPCAAndLDAAndKPCA = new ReduceDimensionPCAAndLDAAndKPCA();
            reduceDimensionPCAAndLDAAndKPCA.setLable(method);
            mainWindow.ReduceDimensionContainerPanel.add(reduceDimensionPCAAndLDAAndKPCA.getPanel());
            mainWindow.ReduceDimensionRecommendCheckBox.setSelected(false);
            mainWindow.ReduceDimensionNoCheckBox.setSelected(false);
        }
        else if(Objects.equals(method, "��ʹ��")){
            mainWindow.PCACheckBox.setSelected(false);
            mainWindow.LDACheckBox.setSelected(false);
            mainWindow.KPCACheckBox.setSelected(false);
            mainWindow.ReduceDimensionRecommendCheckBox.setSelected(false);
            mainWindow.ReduceNoiceContainerPanel.removeAll();
        }
        else if(Objects.equals(method, "ϵͳ�Ƽ�")){
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
            ReduceNoiceFFTAndNormalizationZScore reduceNoiceFFTAndNormalizationZScore = new ReduceNoiceFFTAndNormalizationZScore();
            reduceNoiceFFTAndNormalizationZScore.setLable(method);
            mainWindow.NormalizationContainerPanel.add(reduceNoiceFFTAndNormalizationZScore.getPanel());
            mainWindow.NormalizationRecommendCheckBox.setSelected(false);
            mainWindow.NormalizationNoCheckBox.setSelected(false);
        }
        else if(Objects.equals(method, "��ʹ��")){
            mainWindow.zScoreCheckBox.setSelected(false);
            mainWindow.NormalizationRecommendCheckBox.setSelected(false);
            mainWindow.NormalizationContainerPanel.removeAll();
        }
        else if(Objects.equals(method, "ϵͳ�Ƽ�")){
            mainWindow.zScoreCheckBox.setSelected(false);
            mainWindow.NormalizationNoCheckBox.setSelected(false);
            //TODO
        }

        checkBox.setForeground(Color.green);
        mainWindow.NormalizationContainerPanel.updateUI();
    }

    private void updateMethodSet(){
        if(StaticAlignCheckBox.isSelected()){
            methodSet.add("Static Align");
        }
        if(DTWCheckBox.isSelected()){
            methodSet.add("DTW Align");
        }
        if(FFTCheckBox.isSelected()){
            methodSet.add("FFT ReduceNoice");
        }
        if(POCCheckBox.isSelected()){
            methodSet.add("POC ReduceNoice");
        }
        if(KalmanFilterCheckBox.isSelected()){
            methodSet.add("Kalman ReduceNoice");
        }
        if(SSACheckBox.isSelected()){
            methodSet.add("SSA ReduceNoice");
        }
        if(ICACheckBox.isSelected()){
            methodSet.add("ICA ReduceNoice");
        }
        if(PCACheckBox.isSelected()){
            methodSet.add("PCA ReduceDimension");
        }
        if(LDACheckBox.isSelected()){
            methodSet.add("LDA ReduceDimension");
        }
        if(KPCACheckBox.isSelected()){
            methodSet.add("KPCA ReduceDimension");
        }
        if(zScoreCheckBox.isSelected()){
            methodSet.add("ZScore Normalization");
        }

        if(AlignRecommendCheckBox.isSelected()){
            methodSet.add("Static Align");
            methodSet.add("DTW Align");
        }
        if(ReduceNoiceRecommendCheckBox.isSelected()){
            methodSet.add("FFT ReduceNoice");
            methodSet.add("POC ReduceNoice");
            methodSet.add("Kalman ReduceNoice");
            methodSet.add("SSA ReduceNoice");
            methodSet.add("ICA ReduceNoice");
        }
        if(ReduceDimensionRecommendCheckBox.isSelected()){
            methodSet.add("PCA ReduceDimension");
            methodSet.add("LDA ReduceDimension");
            methodSet.add("KPCA ReduceDimension");
        }
        if(NormalizationRecommendCheckBox.isSelected()){
            methodSet.add("ZScore Normalization");
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

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException{
        // ����UIManager
        UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
        UIManager.put("CheckBox.font", new Font("Microsoft YaHei", Font.BOLD, 18));
        UIManager.put("Button.font", new Font("Microsoft YaHei", Font.BOLD, 16));
        UIManager.put("TextField.font", new Font("Microsoft YaHei Light", Font.BOLD, 18));

        // ��ʼ��frame��mainwindow��methodSet������Panel
        mainWindow = new MainWindow();
        frame = new JFrame("Preprocess");
        methodSet = new HashSet<>();
        systemRecommendOption = new boolean[5];
        locationPanel = new LocationPanel();

        // ����frame���Բ���ʾ
        mainWindow.setFrameContribution(frame, mainWindow);
    }
}