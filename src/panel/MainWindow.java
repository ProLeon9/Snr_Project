package panel;

import align.AlignFactory;
import align.AlignToolBox;
import align.DTW;
import align.StaticAlign;
import com.itextpdf.text.DocumentException;
import location.LocationFactory;
import location.LocationToolBox;
import normalization.NormalizationFactory;
import normalization.NormalizationToolBox;
import normalization.ZScore;
import org.jfree.chart.ChartPanel;
import reducedimension.ReduceDimensionFactory;
import reducedimension.ReduceDimensionToolBox;
import reducenoice.*;
import tools.DrawCurve;
import tools.GenerateReoport;
import tools.GetRoute;
import tools.PreProcess;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainWindow {
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
    private JCheckBox LLECheckBox;
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
    private JButton normal_button;
    private JButton compress_button;
    private JButton notrigger_button;

    private static MainWindow mainWindow;
    private static String matlabPath;
    private static String resultPath;
    private static String icaPath;
    private static ChartPanel resultChartPanel;
    private static Set<String> methodSet;   //存放用户选择的预处理方法
    private static List<String> methodList;   //存放将要使用的预处理方法
    private static boolean[] systemRecommendOption;   //是否需要系统推荐
    private static LocationPanel locationPanel; //存放location参数
    private static List<ReduceNoiceKalmanFilter> reduceNoiceKalmanFilter; // 存放卡尔曼滤波参数
    private static List<AlignDTWAndReduceNoicePOC> alignDTWAndReduceNoicePOC; //存放DTW和POC参数
    private static List<AlignStaticAlign> alignStaticAlign; //存放静态对齐参数
    private static List<ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA> reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA; //存放曲线压缩参数
    private static List<ReduceNoiceFFTAndNormalizationZScore> reduceNoiceFFTAndNormalizationZScores; //存放FFT和标准化参数
    private static String lastMethod; // 存放上次进行的预处理方法，便于使用下一个方法时找到文件
    private static ExecutorService preprocessExecutor; // 存放预处理执行线程，使依次执行
    private static ExecutorService statusExecutor; // 存放更新界面线程，使和预处理线程对应并发执行
    private static List<double[]> SNRBeforeResult;  //存放处理前snr值
    private static List<double[]> SNRAfterResult;  //存放处理后snr值
    private static List<double[]> PIBeforeResult;  //存放处理前pi值
    private static List<double[]> PIAfterResult;  //存放处理后pi值


    private MainWindow () {
        NICVCheckBox.addItemListener(e->{
            if (NICVCheckBox.isSelected()) {
                setLocationContainerPanel(NICVCheckBox, "NICV");
            }
            else {
                NICVCheckBox.setForeground(new Color(187, 187, 187));
            }
        });
        SOSDCheckBox.addItemListener(e->{
            if (SOSDCheckBox.isSelected()) {
                setLocationContainerPanel(SOSDCheckBox, "SOSD");
            }
            else {
                SOSDCheckBox.setForeground(new Color(187, 187, 187));
            }
        });
        SOSTCheckBox.addItemListener(e->{
            if (SOSTCheckBox.isSelected()) {
                setLocationContainerPanel(SOSTCheckBox, "SOST");
            }
            else {
                SOSTCheckBox.setForeground(new Color(187, 187, 187));
            }
        });
        LocationRecommendCheckBox.addItemListener(e->{
            if (LocationRecommendCheckBox.isSelected()) {
                setLocationContainerPanel(LocationRecommendCheckBox, "通用");
                systemRecommendOption[0] = true;
            }
            else {
                LocationRecommendCheckBox.setForeground(new Color(187, 187, 187));
                systemRecommendOption[0] = false;
            }
        });
        StaticAlignCheckBox.addItemListener(e->{
            if (StaticAlignCheckBox.isSelected()) {
                setAlignContainerPanel(StaticAlignCheckBox, "静态对齐");
            }
            else {
                StaticAlignCheckBox.setForeground(new Color(187, 187, 187));
                for (int i = 0; i <= alignStaticAlign.size() - 1; i++) {
                    if (Objects.equals(alignStaticAlign.get(i).getName(), "静态对齐参数配置：")) {
                        alignStaticAlign.remove(i);
                        i = 0;
                    }
                }
            }
        });
        DTWCheckBox.addItemListener(e->{
            if (DTWCheckBox.isSelected()) {
                setAlignContainerPanel(DTWCheckBox, "动态对齐");
            }
            else {
                DTWCheckBox.setForeground(new Color(187, 187, 187));
                for (int i = 0; i <= alignDTWAndReduceNoicePOC.size() - 1; i++) {
                    if (Objects.equals(alignDTWAndReduceNoicePOC.get(i).getName(), "动态对齐参数配置：")) {
                        alignDTWAndReduceNoicePOC.remove(i);
                        i = 0;
                    }
                }
            }
        });
        AlignNoCheckBox.addItemListener(e->{
            if (AlignNoCheckBox.isSelected()) {
                setAlignContainerPanel(AlignNoCheckBox, "不使用");
            }
            else {
                AlignNoCheckBox.setForeground(new Color(187, 187, 187));
            }
        });
        AlignRecommendCheckBox.addItemListener(e->{
            if (AlignRecommendCheckBox.isSelected()) {
                setAlignContainerPanel(AlignRecommendCheckBox, "系统推荐");
                systemRecommendOption[1] = true;
            }
            else {
                AlignRecommendCheckBox.setForeground(new Color(187, 187, 187));
                systemRecommendOption[1] = false;
            }
        });
        FFTCheckBox.addItemListener(e->{
            if (FFTCheckBox.isSelected()) {
                setReduceNoiceContainerPanel(FFTCheckBox, "FFT");
            }
            else {
                FFTCheckBox.setForeground(new Color(187, 187, 187));
                for (int i = 0; i <= reduceNoiceFFTAndNormalizationZScores.size() - 1; i++) {
                    if (Objects.equals(reduceNoiceFFTAndNormalizationZScores.get(i).getName(), "FFT参数配置：")) {
                        reduceNoiceFFTAndNormalizationZScores.remove(i);
                        i = 0;
                    }
                }
            }
        });
        POCCheckBox.addItemListener(e->{
            if (POCCheckBox.isSelected()) {
                setReduceNoiceContainerPanel(POCCheckBox, "POC");
            }
            else {
                POCCheckBox.setForeground(new Color(187, 187, 187));
                for (int i = 0; i <= alignDTWAndReduceNoicePOC.size() - 1; i++) {
                    if (Objects.equals(alignDTWAndReduceNoicePOC.get(i).getName(), "POC参数配置：")) {
                        alignDTWAndReduceNoicePOC.remove(i);
                        i = 0;
                    }
                }
            }
        });
        KalmanFilterCheckBox.addItemListener(e->{
            if (KalmanFilterCheckBox.isSelected()) {
                setReduceNoiceContainerPanel(KalmanFilterCheckBox, "卡尔曼滤波");
            }
            else {
                KalmanFilterCheckBox.setForeground(new Color(187, 187, 187));
                for (int i = 0; i <= reduceNoiceKalmanFilter.size() - 1; i++) {
                    if (Objects.equals(reduceNoiceKalmanFilter.get(i).getName(), "卡尔曼滤波参数配置：")) {
                        reduceNoiceKalmanFilter.remove(i);
                        i = 0;
                    }
                }
            }
        });
        SSACheckBox.addItemListener(e->{
            if (SSACheckBox.isSelected()) {
                setReduceNoiceContainerPanel(SSACheckBox, "SSA");
            }
            else {
                SSACheckBox.setForeground(new Color(187, 187, 187));
                for (int i = 0; i <= reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.size() - 1; i++) {
                    if (Objects.equals(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.get(i).getName(), "SSA参数配置：")) {
                        reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.remove(i);
                        i = 0;
                    }
                }
            }
        });
        ICACheckBox.addItemListener(e->{
            if (ICACheckBox.isSelected()) {
                setReduceNoiceContainerPanel(ICACheckBox, "ICA");
            }
            else {
                ICACheckBox.setForeground(new Color(187, 187, 187));
                for (int i = 0; i <= reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.size() - 1; i++) {
                    if (Objects.equals(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.get(i).getName(), "ICA参数配置：")) {
                        reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.remove(i);
                        i = 0;
                    }
                }
            }
        });
        ReduceNoiceNoCheckBox.addItemListener(e->{
            if (ReduceNoiceNoCheckBox.isSelected()) {
                setReduceNoiceContainerPanel(ReduceNoiceNoCheckBox, "不使用");
            }
            ReduceNoiceNoCheckBox.setForeground(new Color(187, 187, 187));
        });
        ReduceNoiceRecommendCheckBox.addItemListener(e->{
            if (ReduceNoiceRecommendCheckBox.isSelected()) {
                setReduceNoiceContainerPanel(ReduceNoiceRecommendCheckBox, "系统推荐");
                systemRecommendOption[2] = true;
            }
            else {
                ReduceNoiceRecommendCheckBox.setForeground(new Color(187, 187, 187));
                systemRecommendOption[2] = false;
            }
        });
        PCACheckBox.addItemListener(e->{
            if (PCACheckBox.isSelected()) {
                setReduceDimensionContainerPanel(PCACheckBox, "PCA");
            }
            else {
                PCACheckBox.setForeground(new Color(187, 187, 187));
                for (int i = 0; i <= reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.size() - 1; i++) {
                    if (Objects.equals(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.get(i).getName(), "PCA参数配置：")) {
                        reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.remove(i);
                        i = 0;
                    }
                }
            }
        });
        LLECheckBox.addItemListener(e->{
            if (LLECheckBox.isSelected()) {
                setReduceDimensionContainerPanel(LLECheckBox, "LLE");
            }
            else {
                LLECheckBox.setForeground(new Color(187, 187, 187));
                for (int i = 0; i <= reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.size() - 1; i++) {
                    if (Objects.equals(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.get(i).getName(), "LLE参数配置：")) {
                        reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.remove(i);
                        i = 0;
                    }
                }
            }
        });
        KPCACheckBox.addItemListener(e->{
            if (KPCACheckBox.isSelected()) {
                setReduceDimensionContainerPanel(KPCACheckBox, "KPCA");
            }
            else {
                KPCACheckBox.setForeground(new Color(187, 187, 187));
                for (int i = 0; i <= reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.size() - 1; i++) {
                    if (Objects.equals(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.get(i).getName(), "KPCA参数配置：")) {
                        reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.remove(i);
                        i = 0;
                    }
                }
            }
        });
        ReduceDimensionNoCheckBox.addItemListener(e->{
            if (ReduceDimensionNoCheckBox.isSelected()) {
                setReduceDimensionContainerPanel(ReduceDimensionNoCheckBox, "不使用");
            }
            else {
                ReduceDimensionNoCheckBox.setForeground(new Color(187, 187, 187));
            }
        });
        ReduceDimensionRecommendCheckBox.addItemListener(e->{
            if (ReduceDimensionRecommendCheckBox.isSelected()) {
                setReduceDimensionContainerPanel(ReduceDimensionRecommendCheckBox, "系统推荐");
                systemRecommendOption[3] = true;
            }
            else {
                ReduceDimensionRecommendCheckBox.setForeground(new Color(187, 187, 187));
                systemRecommendOption[3] = false;
            }
        });
        zScoreCheckBox.addItemListener(e->{
            if (zScoreCheckBox.isSelected()) {
                setNormalizationContainerPanel(zScoreCheckBox, "Z-Score");
            }
            else {
                zScoreCheckBox.setForeground(new Color(187, 187, 187));
                for (int i = 0; i <= reduceNoiceFFTAndNormalizationZScores.size() - 1; i++) {
                    if (Objects.equals(reduceNoiceFFTAndNormalizationZScores.get(i).getName(), "Z-Score参数配置：")) {
                        reduceNoiceFFTAndNormalizationZScores.remove(i);
                        i = 0;
                    }
                }
            }
        });
        NormalizationNoCheckBox.addItemListener(e->{
            if (NormalizationNoCheckBox.isSelected()) {
                setNormalizationContainerPanel(NormalizationNoCheckBox, "不使用");
            }
            else {
                NormalizationNoCheckBox.setForeground(new Color(187, 187, 187));
            }
        });
        NormalizationRecommendCheckBox.addItemListener(e->{
            if (NormalizationRecommendCheckBox.isSelected()) {
                setNormalizationContainerPanel(NormalizationRecommendCheckBox, "系统推荐");
                systemRecommendOption[4] = true;
            }
            else {
                NormalizationRecommendCheckBox.setForeground(new Color(187, 187, 187));
                systemRecommendOption[4] = false;
            }
        });

        LocationRunButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                FilePathDialog filePathDialog = new FilePathDialog();
                filePathDialog.pack();
                filePathDialog.setVisible(true);

                configTheFilesPath(filePathDialog);
                try {
                    configResultPanel();
                }
                catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

                String locationMethod = figureOutLocationMethod();
                LocationToolBox locationToolBox = new LocationFactory().createLocationToolBox(locationMethod);
                Thread locationProcessThread = new Thread(()->{
                    Thread.currentThread().setName("locationProcessThread");
                    try {
                        resultChartPanel = locationToolBox.excuteLocation(locationPanel, resultPath);
                        mainWindow.ResultPicturePanel.setLayout(new BorderLayout());
                        mainWindow.ResultPicturePanel.add(resultChartPanel, BorderLayout.CENTER);
                        mainWindow.ResultPicturePanel.updateUI();
                        Thread.sleep(3);
                    }
                    catch (InterruptedException | IOException e1) {
                        e1.printStackTrace();
                    }
                });

                Thread locationProcessStatusThread = new Thread(()->{
                    Thread.currentThread().setName("locationProcessStatusThread");
                    try {
                        Thread.sleep(500); //等待locationProcessThread中excuteLocation配置好attackNumber
                        while (locationToolBox.getProcessStatus() > 0) {
                            mainWindow.ResultProcessStatusLabel.setText("已处理条数：" + (locationToolBox.getProcessStatus() + 1));
                            Thread.sleep(1); //尽量小一些，保证可以读取到999
                        }
                    }
                    catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                });

                locationProcessThread.start();
                locationProcessStatusThread.start();
            }
        });
        AlignRunButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                tabbedPane1.setSelectedIndex(2);
            }
        });
        ReduceNoiceRunButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                tabbedPane1.setSelectedIndex(3);
            }
        });
        ReduceDimensionRunButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                // 设置文本显示效果
                UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("宋体", Font.ITALIC, 21)));
                JOptionPane.showMessageDialog(null, "注意：如果在执行曲线压缩预处理过程中，matalb命令行窗口提示找不到合适的特征点，那么就要调整压缩后点数！");
                tabbedPane1.setSelectedIndex(4);
            }
        });
        NormalizationRunButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                updateMethodSet();
                PreprocessOrderConfirmDialog preprocessOrderConfirmDialog = new PreprocessOrderConfirmDialog(methodSet, systemRecommendOption);
                preprocessOrderConfirmDialog.pack();
                preprocessOrderConfirmDialog.setVisible(true);
                methodList = preprocessOrderConfirmDialog.result;

                FilePathDialog filePathDialog = new FilePathDialog();
                filePathDialog.pack();
                filePathDialog.setVisible(true);

                configTheFilesPath(filePathDialog);
                try {
                    configResultPanel();
                }
                catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

                // TODO:具体执行预处理过程

                //防止点击取消后方法仍在methodSet中出现
                if (methodList == null) {
                    methodSet = new HashSet<>();
                }

                for (int i = 0; i <= methodList.size() - 1; i++) {
                    String factoryName = makeSureUseWhichFactory(methodList.get(i));
                    excutePreprocessDependsOnFactoryName(factoryName, methodList.get(i));
                    try {
                        Thread.sleep(300);
                    }
                    catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                Thread getReportThread = new Thread(()->{
                    Thread.currentThread().setName("getResultReportThread");
                    try {
                        Thread.sleep(500);
                        createReport();
                    }
                    catch (InterruptedException | IOException | DocumentException e1) {
                        e1.printStackTrace();
                    }
                });
                preprocessExecutor.submit(getReportThread);
            }
        });
        LocationHelpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                // 设置文本显示效果
                UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("宋体", Font.ITALIC, 21)));
                JOptionPane.showMessageDialog(null, "定位操作模块：选择定位方法，并输入相应参数后，点击运行即可\n" + "     注意：需要自行输入的数字全部要求是非负整数");
            }
        });
        AlignHelpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                // 设置文本显示效果
                UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("宋体", Font.ITALIC, 21)));
                JOptionPane.showMessageDialog(null, "对齐操作模块：选择对齐方法，并输入相应参数后，点击下一步进行后续选择\n" + "  注意：需要自行输入的数字全部要求是非负整数（也可以不选择对齐方法）");
            }
        });
        ReduceNoiceHelpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                // 设置文本显示效果
                UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("宋体", Font.ITALIC, 21)));
                JOptionPane.showMessageDialog(null, "降噪操作模块：选择降噪方法，并输入相应参数后，点击下一步进行后续选择\n" + "  注意：需要自行输入的数字全部要求是非负整数（也可以不选择降噪" + "方法）");
            }
        });
        ReduceDimensionHelpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                // 设置文本显示效果
                UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("宋体", Font.ITALIC, 21)));
                JOptionPane.showMessageDialog(null, "降维操作模块：选择降维方法，并输入相应参数后，点击下一步进行后续选择\n" + "  注意：需要自行输入的数字全部要求是非负整数（也可以不选择降维" + "方法）");
            }
        });
        NormalizationHelpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                // 设置文本显示效果
                UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("宋体", Font.ITALIC, 21)));
                JOptionPane.showMessageDialog(null, "标准化操作模块：选择标准化方法，并输入相应参数后，点击运行进行顺序选择\n" + "  注意：需要自行输入的数字全部要求是非负整数（也可以不选择标准化方法）");
            }
        });
        normal_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                try {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + new GetRoute().realPath + "\\help\\SNR_IntroBook.pdf");
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        compress_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                try {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + new GetRoute().realPath + "\\help\\SNR_TechBook.pdf");
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        notrigger_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                try {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + new GetRoute().realPath + "\\help\\SNR_TestBook.pdf");
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        FFTCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                // 设置文本显示效果
                UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("宋体", Font.ITALIC, 21)));
                JOptionPane.showMessageDialog(null, "注意：FFT会使曲线点数减半，如果组合方法中含有FFT，那么后续的预处理方法的点数一定要设置为FFT处理点数的一半！");
            }
        });
    }

    private void setFrameContribution (JFrame frame, MainWindow mainWindow) {
        java.awt.Image icon = Toolkit.getDefaultToolkit().getImage("");
        frame.setIconImage(icon);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // get the screen size
        frame.setBounds((int) (screenSize.width * 0.125), (int) (screenSize.height * 0.15), (int) (screenSize.width * 0.75), (int) (screenSize.height * 0.7));
        Dimension preferSize = new Dimension((int) (screenSize.width * 0.75), (int) (screenSize.height * 0.7));
        frame.setPreferredSize(preferSize);
        frame.setContentPane(mainWindow.mainPanel);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void setLocationContainerPanel (JCheckBox checkBox, String method) {
        mainWindow.LocationContainerPanel.removeAll();
        mainWindow.LocationContainerPanel.setLayout(new BorderLayout());

        if (Objects.equals(method, "通用")) {
            mainWindow.NICVCheckBox.setSelected(false);
            mainWindow.SOSDCheckBox.setSelected(false);
            mainWindow.SOSTCheckBox.setSelected(false);
        }
        else if (Objects.equals(method, "NICV")) {
            mainWindow.LocationRecommendCheckBox.setSelected(false);
            mainWindow.SOSDCheckBox.setSelected(false);
            mainWindow.SOSTCheckBox.setSelected(false);
        }
        else if (Objects.equals(method, "SOSD")) {
            mainWindow.LocationRecommendCheckBox.setSelected(false);
            mainWindow.NICVCheckBox.setSelected(false);
            mainWindow.SOSTCheckBox.setSelected(false);
        }
        else if (Objects.equals(method, "SOST")) {
            mainWindow.LocationRecommendCheckBox.setSelected(false);
            mainWindow.NICVCheckBox.setSelected(false);
            mainWindow.SOSDCheckBox.setSelected(false);
        }

        locationPanel.setLable(method);
        mainWindow.LocationContainerPanel.add(locationPanel.getPanel());

        checkBox.setForeground(Color.green);
        mainWindow.LocationContainerPanel.updateUI();
    }

    private void setAlignContainerPanel (JCheckBox checkBox, String method) {
        mainWindow.AlignContainerPanel.removeAll();
        mainWindow.AlignContainerPanel.setLayout(new BorderLayout());

        if (Objects.equals(method, "静态对齐")) {
            AlignStaticAlign temp = new AlignStaticAlign();
            temp.setLable(method);
            mainWindow.AlignContainerPanel.add(temp.getPanel());
            alignStaticAlign.add(temp);
            mainWindow.AlignRecommendCheckBox.setSelected(false);
            mainWindow.AlignNoCheckBox.setSelected(false);
        }
        else if (Objects.equals(method, "动态对齐")) {
            AlignDTWAndReduceNoicePOC temp = new AlignDTWAndReduceNoicePOC();
            temp.setLable(method);
            mainWindow.AlignContainerPanel.add(temp.getPanel());
            alignDTWAndReduceNoicePOC.add(temp);
            mainWindow.AlignRecommendCheckBox.setSelected(false);
            mainWindow.AlignNoCheckBox.setSelected(false);
        }
        else if (Objects.equals(method, "不使用")) {
            mainWindow.StaticAlignCheckBox.setSelected(false);
            mainWindow.DTWCheckBox.setSelected(false);
            mainWindow.AlignRecommendCheckBox.setSelected(false);
            mainWindow.AlignContainerPanel.removeAll();
        }
        else if (Objects.equals(method, "系统推荐")) {
            mainWindow.StaticAlignCheckBox.setSelected(false);
            mainWindow.DTWCheckBox.setSelected(false);
            mainWindow.AlignNoCheckBox.setSelected(false);
            //TODO
        }

        checkBox.setForeground(Color.green);
        mainWindow.AlignContainerPanel.updateUI();
    }

    private void setReduceNoiceContainerPanel (JCheckBox checkBox, String method) {
        mainWindow.ReduceNoiceContainerPanel.removeAll();
        mainWindow.ReduceNoiceContainerPanel.setLayout(new BorderLayout());

        if (Objects.equals(method, "FFT")) {
            ReduceNoiceFFTAndNormalizationZScore temp = new ReduceNoiceFFTAndNormalizationZScore();
            temp.setLable(method);
            mainWindow.ReduceNoiceContainerPanel.add(temp.getPanel());
            reduceNoiceFFTAndNormalizationZScores.add(temp);
            mainWindow.ReduceNoiceRecommendCheckBox.setSelected(false);
            mainWindow.ReduceNoiceNoCheckBox.setSelected(false);
        }
        else if (Objects.equals(method, "POC")) {
            AlignDTWAndReduceNoicePOC temp = new AlignDTWAndReduceNoicePOC();
            temp.setLable(method);
            mainWindow.ReduceNoiceContainerPanel.add(temp.getPanel());
            alignDTWAndReduceNoicePOC.add(temp);
            mainWindow.ReduceNoiceRecommendCheckBox.setSelected(false);
            mainWindow.ReduceNoiceNoCheckBox.setSelected(false);
        }
        else if (Objects.equals(method, "卡尔曼滤波")) {
            ReduceNoiceKalmanFilter temp = new ReduceNoiceKalmanFilter();
            temp.setLable(method);
            mainWindow.ReduceNoiceContainerPanel.add(temp.getPanel());
            reduceNoiceKalmanFilter.add(temp);
            mainWindow.ReduceNoiceRecommendCheckBox.setSelected(false);
            mainWindow.ReduceNoiceNoCheckBox.setSelected(false);
        }
        else if (Objects.equals(method, "SSA") || Objects.equals(method, "ICA")) {

            ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA temp = new ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA();
            temp.setLable(method);
            temp.rename(method);
            mainWindow.ReduceNoiceContainerPanel.add(temp.getPanel());
            reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.add(temp);
            mainWindow.ReduceNoiceRecommendCheckBox.setSelected(false);
            mainWindow.ReduceNoiceNoCheckBox.setSelected(false);
        }
        else if (Objects.equals(method, "不使用")) {
            mainWindow.FFTCheckBox.setSelected(false);
            mainWindow.POCCheckBox.setSelected(false);
            mainWindow.KalmanFilterCheckBox.setSelected(false);
            mainWindow.SSACheckBox.setSelected(false);
            mainWindow.ICACheckBox.setSelected(false);
            mainWindow.ReduceNoiceRecommendCheckBox.setSelected(false);
            mainWindow.ReduceNoiceContainerPanel.removeAll();
        }
        else if (Objects.equals(method, "系统推荐")) {
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

    private void setReduceDimensionContainerPanel (JCheckBox checkBox, String method) {
        mainWindow.ReduceDimensionContainerPanel.removeAll();
        mainWindow.ReduceDimensionContainerPanel.setLayout(new BorderLayout());
        if (Objects.equals(method, "PCA") || Objects.equals(method, "LLE") || Objects.equals(method, "KPCA")) {
            ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA temp = new ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA();
            temp.setLable(method);
            mainWindow.ReduceDimensionContainerPanel.add(temp.getPanel());
            reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.add(temp);
            mainWindow.ReduceDimensionRecommendCheckBox.setSelected(false);
            mainWindow.ReduceDimensionNoCheckBox.setSelected(false);
        }
        else if (Objects.equals(method, "不使用")) {
            mainWindow.PCACheckBox.setSelected(false);
            mainWindow.LLECheckBox.setSelected(false);
            mainWindow.KPCACheckBox.setSelected(false);
            mainWindow.ReduceDimensionRecommendCheckBox.setSelected(false);
            mainWindow.ReduceNoiceContainerPanel.removeAll();
        }
        else if (Objects.equals(method, "系统推荐")) {
            mainWindow.PCACheckBox.setSelected(false);
            mainWindow.LLECheckBox.setSelected(false);
            mainWindow.KPCACheckBox.setSelected(false);
            mainWindow.ReduceDimensionNoCheckBox.setSelected(false);
            //TODO
        }

        checkBox.setForeground(Color.green);
        mainWindow.ReduceDimensionContainerPanel.updateUI();
    }

    private void setNormalizationContainerPanel (JCheckBox checkBox, String method) {
        mainWindow.NormalizationContainerPanel.removeAll();
        mainWindow.NormalizationContainerPanel.setLayout(new BorderLayout());

        if (Objects.equals(method, "Z-Score")) {
            ReduceNoiceFFTAndNormalizationZScore temp = new ReduceNoiceFFTAndNormalizationZScore();
            temp.setLable(method);
            mainWindow.NormalizationContainerPanel.add(temp.getPanel());
            reduceNoiceFFTAndNormalizationZScores.add(temp);
            mainWindow.NormalizationRecommendCheckBox.setSelected(false);
            mainWindow.NormalizationNoCheckBox.setSelected(false);
        }
        else if (Objects.equals(method, "不使用")) {
            mainWindow.zScoreCheckBox.setSelected(false);
            mainWindow.NormalizationRecommendCheckBox.setSelected(false);
            mainWindow.NormalizationContainerPanel.removeAll();
        }
        else if (Objects.equals(method, "系统推荐")) {
            mainWindow.zScoreCheckBox.setSelected(false);
            mainWindow.NormalizationNoCheckBox.setSelected(false);
            //TODO
        }

        checkBox.setForeground(Color.green);
        mainWindow.NormalizationContainerPanel.updateUI();
    }

    private void updateMethodSet () {
        if (StaticAlignCheckBox.isSelected()) {
            methodSet.add("静态对齐");
        }
        if (DTWCheckBox.isSelected()) {
            methodSet.add("动态对齐");
        }
        if (FFTCheckBox.isSelected()) {
            methodSet.add("FFT");
        }
        if (POCCheckBox.isSelected()) {
            methodSet.add("POC");
        }
        if (KalmanFilterCheckBox.isSelected()) {
            methodSet.add("卡尔曼滤波");
        }
        if (SSACheckBox.isSelected()) {
            methodSet.add("SSA");
        }
        if (ICACheckBox.isSelected()) {
            methodSet.add("ICA");
        }
        if (PCACheckBox.isSelected()) {
            methodSet.add("PCA");
        }
        if (LLECheckBox.isSelected()) {
            methodSet.add("LLE");
        }
        if (KPCACheckBox.isSelected()) {
            methodSet.add("KPCA");
        }
        if (zScoreCheckBox.isSelected()) {
            methodSet.add("Z-Score");
        }

        if (AlignRecommendCheckBox.isSelected()) {
            methodSet.add("静态对齐");
            methodSet.add("动态对齐");
        }
        if (ReduceNoiceRecommendCheckBox.isSelected()) {
            methodSet.add("FFT");
            methodSet.add("POC");
            methodSet.add("卡尔曼滤波");
            methodSet.add("SSA");
            methodSet.add("ICA");
        }
        if (ReduceDimensionRecommendCheckBox.isSelected()) {
            methodSet.add("PCA");
            methodSet.add("LLE");
            methodSet.add("KPCA");
        }
        if (NormalizationRecommendCheckBox.isSelected()) {
            methodSet.add("Z-Score");
        }
    }

    private String figureOutLocationMethod () {
        if (mainWindow.NICVCheckBox.isSelected()) {
            return "NICV";
        }
        else if (mainWindow.SOSDCheckBox.isSelected()) {
            return "SOSD";
        }
        else if (mainWindow.SOSTCheckBox.isSelected()) {
            return "SOST";
        }
        else if (LocationRecommendCheckBox.isSelected()) {
            return "NICV"; // TODO:choose which one???
        }

        return null;//TODO:throw exception!!!
    }

    private void configTheFilesPath (FilePathDialog filePathDialog) {
        matlabPath = filePathDialog.matlabPath;
        resultPath = filePathDialog.resultPath;
        icaPath = filePathDialog.ICAPath;
    }

    private void configResultPanel () throws InterruptedException {
        mainWindow.ResultPicturePanel.removeAll();
        tabbedPane1.setSelectedIndex(5);
    }

    private String makeSureUseWhichFactory (String methodName) {
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
        reduceDimensionList.add("LLE");
        reduceDimensionList.add("KPCA");
        normalizationList.add("Z-Score");
        if (alignList.contains(methodName)) {
            return "align";
        }
        else if (reduceNoiceList.contains(methodName)) {
            return "reduceNoice";
        }
        else if (reduceDimensionList.contains(methodName)) {
            return "reduceDimension";
        }
        else if (normalizationList.contains(methodName)) {
            return "normalization";
        }

        return null; // TODO : 抛出异常
    }

    private void excutePreprocessDependsOnFactoryName (String factoryName, String preprocessMethod) {
        if (Objects.equals(factoryName, "align")) {
            excuteAlign(preprocessMethod);
        }
        else if (Objects.equals(factoryName, "reduceNoice")) {
            excuteReduceNoice(preprocessMethod);
        }
        else if (Objects.equals(factoryName, "reduceDimension")) {
            excuteReduceDimension(preprocessMethod);
        }
        else if (Objects.equals(factoryName, "normalization")) {
            excuteNormalization(preprocessMethod);
        }
    }

    private void excuteAlign (String alignMethod) {
        AlignToolBox alignToolBox = new AlignFactory().createAlignToolBox(alignMethod);
        excuteConcreteAlign(alignToolBox, alignMethod);
    }

    private void excuteReduceNoice (String reduceNoiceMethod) {
        ReduceNoiceToolBox reduceNoiceToolBox = new ReduceNoiceFactory().createRedeceNoiceToolBox(reduceNoiceMethod);
        excuteConcreteReduceNoice(reduceNoiceToolBox, reduceNoiceMethod);
    }

    private void excuteReduceDimension (String reduceDimensionMethod) {
        ReduceDimensionToolBox reduceDimensionToolBox = new ReduceDimensionFactory().createReduceDimensionToolBox(reduceDimensionMethod);
        excuteConcreteReduceDimension(reduceDimensionToolBox, reduceDimensionMethod);
    }

    private void excuteNormalization (String normalizationMethod) {
        NormalizationToolBox normalizationToolBox = new NormalizationFactory().createNormalizationToolBox(normalizationMethod);
        excuteConcreterNormalization(normalizationToolBox, normalizationMethod);
    }

    private void excuteConcreteAlign (AlignToolBox alignToolBox, String alignMethod) {
        if (Objects.equals(alignMethod, "静态对齐")) {
            StaticAlign staticAlign = (StaticAlign) alignToolBox;
            AlignStaticAlign temp = alignStaticAlign.get(0);
            Thread alignProcessThread = new Thread(()->{
                Thread.currentThread().setName("alignProcessThread");
                try {
                    resultChartPanel = staticAlign.executeAlign(temp, resultPath, lastMethod);
                    lastMethod = "StaticAlign";
                    double[] SNRReulst = staticAlign.getSNR();
                    mainWindow.ResultOriginalSNRLabel.setText("预处理前SNR： " + String.format("%.4f", SNRReulst[0]));
                    mainWindow.ResultProcessedSNRLabel.setText("预处理后SNR： " + String.format("%.4f", SNRReulst[1]));
                    double[] PIResult = staticAlign.getPI();
                    mainWindow.ResultOriginalPILabel.setText("预处理前PI： " + String.format("%.4f", PIResult[0]));
                    mainWindow.ResultProcessedPILabel.setText("预处理后PI： " + String.format("%.4f", PIResult[1]));
                    List<double[]> SPresult = staticAlign.getSNRAndPI();
                    saveSNRAndPIResult(SPresult);
                    mainWindow.ResultPicturePanel.removeAll();
                    mainWindow.ResultPicturePanel.setLayout(new BorderLayout());
                    mainWindow.ResultPicturePanel.add(resultChartPanel, BorderLayout.CENTER);
                    mainWindow.ResultPicturePanel.updateUI();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            Thread alignProcessStatusThread = new Thread(()->{
                Thread.currentThread().setName("alignProcessStaticAlignStatusThread");
                try {
                    preprocessStatus(alignToolBox);
                }
                catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            });

            preprocessExecutor.submit(alignProcessThread);
            statusExecutor.submit(alignProcessStatusThread);
        }
        else if (Objects.equals(alignMethod, "动态对齐")) {
            DTW dtw = (DTW) alignToolBox;
            AlignDTWAndReduceNoicePOC temp = searchDTWAndPOCPanel("动态对齐");
            Thread alignProcessThread = new Thread(()->{
                Thread.currentThread().setName("alignProcessThread");
                try {
                    resultChartPanel = dtw.excuteAlign(temp, resultPath, lastMethod);
                    lastMethod = "DTW"; //根据对应方法里写文件的名称
                    double[] SNRReulst = dtw.getSNR();
                    mainWindow.ResultOriginalSNRLabel.setText("预处理前SNR： " + String.format("%.4f", SNRReulst[0]));
                    mainWindow.ResultProcessedSNRLabel.setText("预处理后SNR： " + String.format("%.4f", SNRReulst[1]));
                    double[] PIReulst = dtw.getPI();
                    mainWindow.ResultOriginalPILabel.setText("预处理前PI： " + String.format("%.4f", PIReulst[0]));
                    mainWindow.ResultProcessedPILabel.setText("预处理后PI： " + String.format("%.4f", PIReulst[1]));
                    List<double[]> SPresult = dtw.getSNRAndPI();
                    saveSNRAndPIResult(SPresult);
                    mainWindow.ResultPicturePanel.removeAll();
                    mainWindow.ResultPicturePanel.setLayout(new BorderLayout());
                    mainWindow.ResultPicturePanel.add(resultChartPanel, BorderLayout.CENTER);
                    mainWindow.ResultPicturePanel.updateUI();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            Thread alignProcessStatusThread = new Thread(()->{
                Thread.currentThread().setName("alignProcessDTWStatusThread");
                try {
                    preprocessStatus(alignToolBox);
                }
                catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            });

            preprocessExecutor.submit(alignProcessThread);
            statusExecutor.submit(alignProcessStatusThread);
        }
    }

    private void excuteConcreteReduceNoice (ReduceNoiceToolBox reduceNoiceToolBox, String reduceNoiceMethod) {
        if (Objects.equals(reduceNoiceMethod, "FFT")) {
            FFT fft = (FFT) reduceNoiceToolBox;
            ReduceNoiceFFTAndNormalizationZScore temp = searchFFTAndZscorePanel("FFT");
            Thread reduceNoiceProcessThread = new Thread(()->{
                Thread.currentThread().setName("reduceNoiceProcessThread");
                try {
                    resultChartPanel = fft.excuteReduceNoice(temp, resultPath, lastMethod);
                    lastMethod = "FFT";
                    double[] SNRReulst = fft.getSNR();
                    mainWindow.ResultOriginalSNRLabel.setText("预处理前SNR： " + String.format("%.4f", SNRReulst[0]));
                    mainWindow.ResultProcessedSNRLabel.setText("预处理后SNR： " + String.format("%.4f", SNRReulst[1]));
                    double[] PIReulst = fft.getPI();
                    mainWindow.ResultOriginalPILabel.setText("预处理前PI： " + String.format("%.4f", PIReulst[0]));
                    mainWindow.ResultProcessedPILabel.setText("预处理后PI： " + String.format("%.4f", PIReulst[1]));
                    List<double[]> SPresult = fft.getSNRAndPI();
                    saveSNRAndPIResult(SPresult);
                    mainWindow.ResultPicturePanel.removeAll();
                    mainWindow.ResultPicturePanel.setLayout(new BorderLayout());
                    mainWindow.ResultPicturePanel.add(resultChartPanel, BorderLayout.CENTER);
                    mainWindow.ResultPicturePanel.updateUI();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            Thread reduceNoiceProcessStatusThread = new Thread(()->{
                Thread.currentThread().setName("reduceNoiceProcessFFTStatusThread");
                try {
                    preprocessStatus(reduceNoiceToolBox);
                }
                catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            });

            preprocessExecutor.submit(reduceNoiceProcessThread);
            statusExecutor.submit(reduceNoiceProcessStatusThread);
        }
        else if (Objects.equals(reduceNoiceMethod, "POC")) {
            POC poc = (POC) reduceNoiceToolBox;
            AlignDTWAndReduceNoicePOC temp = searchDTWAndPOCPanel("POC");
            Thread reduceNoiceProcessThread = new Thread(()->{
                Thread.currentThread().setName("reduceNoiceProcessThread");
                try {
                    resultChartPanel = poc.excuteReduceNoice(temp, resultPath, lastMethod);
                    lastMethod = "POC";
                    double[] SNRReulst = poc.getSNR();
                    mainWindow.ResultOriginalSNRLabel.setText("预处理前SNR： " + String.format("%.4f", SNRReulst[0]));
                    mainWindow.ResultProcessedSNRLabel.setText("预处理后SNR： " + String.format("%.4f", SNRReulst[1]));
                    double[] PIReulst = poc.getPI();
                    mainWindow.ResultOriginalPILabel.setText("预处理前PI： " + String.format("%.4f", PIReulst[0]));
                    mainWindow.ResultProcessedPILabel.setText("预处理后PI： " + String.format("%.4f", PIReulst[1]));
                    List<double[]> SPresult = poc.getSNRAndPI();
                    saveSNRAndPIResult(SPresult);
                    mainWindow.ResultPicturePanel.removeAll();
                    mainWindow.ResultPicturePanel.setLayout(new BorderLayout());
                    mainWindow.ResultPicturePanel.add(resultChartPanel, BorderLayout.CENTER);
                    mainWindow.ResultPicturePanel.updateUI();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            Thread reduceNoiceProcessStatusThread = new Thread(()->{
                Thread.currentThread().setName("reduceNoiceProcessPOCStatusThread");
                try {
                    preprocessStatus(reduceNoiceToolBox);
                }
                catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            });

            preprocessExecutor.submit(reduceNoiceProcessThread);
            statusExecutor.submit(reduceNoiceProcessStatusThread);
        }
        else if (Objects.equals(reduceNoiceMethod, "卡尔曼滤波")) {
            KalmanFilter kalmanFilter = (KalmanFilter) reduceNoiceToolBox;
            ReduceNoiceKalmanFilter temp = reduceNoiceKalmanFilter.get(0);
            Thread reduceNoiceProcessThread = new Thread(()->{
                Thread.currentThread().setName("reduceNoiceProcessThread");
                try {
                    resultChartPanel = kalmanFilter.excuteReduceNoice(temp, resultPath, lastMethod);
                    lastMethod = "KalmanFilter";
                    double[] SNRReulst = kalmanFilter.getSNR();
                    mainWindow.ResultOriginalSNRLabel.setText("预处理前SNR： " + String.format("%.4f", SNRReulst[0]));
                    mainWindow.ResultProcessedSNRLabel.setText("预处理后SNR： " + String.format("%.4f", SNRReulst[1]));
                    double[] PIReulst = kalmanFilter.getPI();
                    mainWindow.ResultOriginalPILabel.setText("预处理前PI： " + String.format("%.4f", PIReulst[0]));
                    mainWindow.ResultProcessedPILabel.setText("预处理后PI： " + String.format("%.4f", PIReulst[1]));
                    List<double[]> SPresult = kalmanFilter.getSNRAndPI();
                    saveSNRAndPIResult(SPresult);
                    mainWindow.ResultPicturePanel.removeAll();
                    mainWindow.ResultPicturePanel.setLayout(new BorderLayout());
                    mainWindow.ResultPicturePanel.add(resultChartPanel, BorderLayout.CENTER);
                    mainWindow.ResultPicturePanel.updateUI();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            Thread reduceNoiceProcessStatusThread = new Thread(()->{
                Thread.currentThread().setName("reduceNoiceProcessKalmanFilterStatusThread");
                try {
                    preprocessStatus(reduceNoiceToolBox);
                }
                catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            });

            preprocessExecutor.submit(reduceNoiceProcessThread);
            statusExecutor.submit(reduceNoiceProcessStatusThread);
        }
        else if (Objects.equals(reduceNoiceMethod, "SSA")) {
            Thread reduceNoiceProcessThread = new Thread(()->{
                SSA ssa = (SSA) reduceNoiceToolBox;
                ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA temp = searchPCAAndSSAPanel("SSA");
                Thread.currentThread().setName("ReduceNoiceProcessThread");
                try {
                    resultChartPanel = ssa.excuteReduceNoice(temp, resultPath, lastMethod, matlabPath);
                    lastMethod = "SSA";
                    double[] SNRReulst = ssa.getSNR();
                    mainWindow.ResultOriginalSNRLabel.setText("预处理前SNR： " + String.format("%.4f", SNRReulst[0]));
                    mainWindow.ResultProcessedSNRLabel.setText("预处理后SNR： " + String.format("%.4f", SNRReulst[1]));
                    double[] PIReulst = ssa.getPI();
                    mainWindow.ResultOriginalPILabel.setText("预处理前PI： " + String.format("%.4f", PIReulst[0]));
                    mainWindow.ResultProcessedPILabel.setText("预处理后PI： " + String.format("%.4f", PIReulst[1]));
                    List<double[]> SPresult = ssa.getSNRAndPI();
                    saveSNRAndPIResult(SPresult);
                    mainWindow.ResultPicturePanel.removeAll();
                    mainWindow.ResultPicturePanel.setLayout(new BorderLayout());
                    mainWindow.ResultPicturePanel.add(resultChartPanel, BorderLayout.CENTER);
                    mainWindow.ResultPicturePanel.updateUI();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread reduceNoiceProcessStatusThread = new Thread(()->{
                Thread.currentThread().setName("ReduceNoiceProcessSSAStatusThread");
                try {
                    preprocessStatus(reduceNoiceToolBox);
                }
                catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            });

            preprocessExecutor.submit(reduceNoiceProcessThread);
            statusExecutor.submit(reduceNoiceProcessStatusThread);
        }

        else if (Objects.equals(reduceNoiceMethod, "ICA")) {
            Thread reduceNoiceProcessThread = new Thread(()->{
                ICA ica = (ICA) reduceNoiceToolBox;
                ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA temp = searchPCAAndSSAPanel("ICA");
                Thread.currentThread().setName("ReduceNoiceProcessThread");
                try {
                    resultChartPanel = ica.excuteReduceNoice(temp, resultPath, lastMethod, matlabPath, icaPath);
                    lastMethod = "ICA";
                    double[] SNRReulst = ica.getSNR();
                    mainWindow.ResultOriginalSNRLabel.setText("预处理前SNR： " + String.format("%.4f", SNRReulst[0]));
                    mainWindow.ResultProcessedSNRLabel.setText("预处理后SNR： " + String.format("%.4f", SNRReulst[1]));
                    double[] PIReulst = ica.getPI();
                    mainWindow.ResultOriginalPILabel.setText("预处理前PI： " + String.format("%.4f", PIReulst[0]));
                    mainWindow.ResultProcessedPILabel.setText("预处理后PI： " + String.format("%.4f", PIReulst[1]));
                    List<double[]> SPresult = ica.getSNRAndPI();
                    saveSNRAndPIResult(SPresult);
                    mainWindow.ResultPicturePanel.removeAll();
                    mainWindow.ResultPicturePanel.setLayout(new BorderLayout());
                    mainWindow.ResultPicturePanel.add(resultChartPanel, BorderLayout.CENTER);
                    mainWindow.ResultPicturePanel.updateUI();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread reduceNoiceProcessStatusThread = new Thread(()->{
                Thread.currentThread().setName("ReduceNoiceProcessICAStatusThread");
                try {
                    preprocessStatus(reduceNoiceToolBox);
                }
                catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            });

            preprocessExecutor.submit(reduceNoiceProcessThread);
            statusExecutor.submit(reduceNoiceProcessStatusThread);
        }
    }

    private void excuteConcreteReduceDimension (ReduceDimensionToolBox reduceDimensionToolBox, String reduceDimensionMethod) {
        Thread reduceDimensionProcessThread = new Thread(()->{
            Thread.currentThread().setName("ReduceDimensionProcessThread");
            ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA temp = reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.get(reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.size() - 1);
            try {
                resultChartPanel = reduceDimensionToolBox.excuteReduceDimesion(temp, resultPath, lastMethod, matlabPath);
                lastMethod = reduceDimensionMethod;
                double[] SNRReulst = reduceDimensionToolBox.getSNR();
                mainWindow.ResultOriginalSNRLabel.setText("预处理前SNR： " + String.format("%.4f", SNRReulst[0]));
                mainWindow.ResultProcessedSNRLabel.setText("预处理后SNR： " + String.format("%.4f", SNRReulst[1]));
                double[] PIReulst = reduceDimensionToolBox.getPI();
                mainWindow.ResultOriginalPILabel.setText("预处理前PI： " + String.format("%.4f", PIReulst[0]));
                mainWindow.ResultProcessedPILabel.setText("预处理后PI： " + String.format("%.4f", PIReulst[1]));
                List<double[]> SPresult = reduceDimensionToolBox.getSNRAndPI();
                saveSNRAndPIResult(SPresult);
                mainWindow.ResultPicturePanel.removeAll();
                mainWindow.ResultPicturePanel.setLayout(new BorderLayout());
                mainWindow.ResultPicturePanel.add(resultChartPanel, BorderLayout.CENTER);
                mainWindow.ResultPicturePanel.updateUI();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread reduceDimensionProcessStatusThread = new Thread(()->{
            Thread.currentThread().setName("ReduceDimensionProcessStatusThread");
            try {
                preprocessStatus(reduceDimensionToolBox);
            }
            catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        });

        preprocessExecutor.submit(reduceDimensionProcessThread);
        statusExecutor.submit(reduceDimensionProcessStatusThread);
    }

    private void excuteConcreterNormalization (NormalizationToolBox normalizationToolBox, String normalizationMethod) {
        if (Objects.equals(normalizationMethod, "Z-Score")) {
            ZScore zScore = (ZScore) normalizationToolBox;
            ReduceNoiceFFTAndNormalizationZScore temp = searchFFTAndZscorePanel("Z-Score");
            Thread normalizationProcessThread = new Thread(()->{
                Thread.currentThread().setName("normalizationProcessThread");
                try {
                    resultChartPanel = zScore.excuteNormalization(temp, resultPath, lastMethod);
                    lastMethod = "Z-Score";
                    double[] SNRReulst = zScore.getSNR();
                    mainWindow.ResultOriginalSNRLabel.setText("预处理前SNR： " + String.format("%.4f", SNRReulst[0]));
                    mainWindow.ResultProcessedSNRLabel.setText("预处理后SNR： " + String.format("%.4f", SNRReulst[1]));
                    double[] PIReulst = zScore.getPI();
                    mainWindow.ResultOriginalPILabel.setText("预处理前PI： " + String.format("%.4f", PIReulst[0]));
                    mainWindow.ResultProcessedPILabel.setText("预处理后PI： " + String.format("%.4f", PIReulst[1]));
                    List<double[]> SPresult = zScore.getSNRAndPI();
                    saveSNRAndPIResult(SPresult);
                    mainWindow.ResultPicturePanel.removeAll();
                    mainWindow.ResultPicturePanel.setLayout(new BorderLayout());
                    mainWindow.ResultPicturePanel.add(resultChartPanel, BorderLayout.CENTER);
                    mainWindow.ResultPicturePanel.updateUI();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            Thread normalizationProcessStatusThread = new Thread(()->{
                Thread.currentThread().setName("normalizationProcessZ-ScoreStatusThread");
                try {
                    preprocessStatus(normalizationToolBox);
                }
                catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            });

            preprocessExecutor.submit(normalizationProcessThread);
            statusExecutor.submit(normalizationProcessStatusThread);
        }
    }

    private AlignDTWAndReduceNoicePOC searchDTWAndPOCPanel (String method) {
        for (int i = 0; i <= alignDTWAndReduceNoicePOC.size() - 1; i++) {
            if (alignDTWAndReduceNoicePOC.get(i).getName().contains(method)) {
                return alignDTWAndReduceNoicePOC.get(i);
            }
        }
        return null; //TODO: 抛出异常
    }

    private ReduceNoiceFFTAndNormalizationZScore searchFFTAndZscorePanel (String method) {
        for (int i = 0; i <= reduceNoiceFFTAndNormalizationZScores.size() - 1; i++) {
            if (reduceNoiceFFTAndNormalizationZScores.get(i).getName().contains(method)) {
                return reduceNoiceFFTAndNormalizationZScores.get(i);
            }
        }
        return null; //TODO: 抛出异常
    }

    private ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA searchPCAAndSSAPanel (String method) {
        for (int i = 0; i <= reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.size() - 1; i++) {
            if (reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.get(i).getName().contains(method)) {
                return reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA.get(i);
            }
        }
        return null; //TODO: 抛出异常
    }

    private void createReport () throws DocumentException, IOException {
        DrawCurve dc = new DrawCurve();
        BufferedImage[][] result_image = new BufferedImage[methodList.size()][4];
        for (int i = 0; i <= methodList.size() - 1; i++) {
            result_image[i][0] = dc.generateImage(SNRBeforeResult.get(i));
            result_image[i][1] = dc.generateImage(SNRAfterResult.get(i));
            result_image[i][2] = dc.generateImage(PIBeforeResult.get(i));
            result_image[i][3] = dc.generateImage(PIAfterResult.get(i));
        }
        GenerateReoport rg = new GenerateReoport();
        rg.writeReport(resultPath, methodList, result_image);
    }

    private void saveSNRAndPIResult (List<double[]> SPresult) {
        SNRBeforeResult.add(SPresult.get(0));
        SNRAfterResult.add(SPresult.get(1));
        PIBeforeResult.add(SPresult.get(2));
        PIAfterResult.add(SPresult.get(3));
    }

    private void preprocessStatus (PreProcess preprocess) throws InterruptedException {
        Thread.sleep(500); //等待locationProcessThread中excuteLocation配置好attackNumber
        while (preprocess.getProcessStatus() > 0) {
            mainWindow.ResultProcessStatusLabel.setText("已处理条数：" + (preprocess.getProcessStatus() + 1));
            Thread.sleep(1); //尽量小一些，保证可以读取到999
        }
    }

    public static void main (String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        // 设置UIManager
        UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
        UIManager.put("CheckBox.font", new Font("Microsoft YaHei", Font.BOLD, 18));
        UIManager.put("Button.font", new Font("Microsoft YaHei", Font.BOLD, 16));
        UIManager.put("TextField.font", new Font("Microsoft YaHei Light", Font.BOLD, 18));

        // 初始化frame、mainwindow、methodSet、其他Panel
        mainWindow = new MainWindow();
        JFrame frame = new JFrame("Preprocess");
        methodSet = new HashSet<>();
        systemRecommendOption = new boolean[5];
        locationPanel = new LocationPanel();
        reduceNoiceKalmanFilter = new ArrayList<>();
        alignDTWAndReduceNoicePOC = new ArrayList<>();
        alignStaticAlign = new ArrayList<>();
        reduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA = new ArrayList<>();
        reduceNoiceFFTAndNormalizationZScores = new ArrayList<>();
        preprocessExecutor = Executors.newSingleThreadExecutor();
        statusExecutor = Executors.newSingleThreadExecutor();
        lastMethod = "wave";
        SNRBeforeResult = new ArrayList<>();
        SNRAfterResult = new ArrayList<>();
        PIBeforeResult = new ArrayList<>();
        PIAfterResult = new ArrayList<>();

        // 设置frame属性并显示
        mainWindow.setFrameContribution(frame, mainWindow);
    }
}
