package panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Objects;

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

    private static JFrame frame;
    private static MainWindow mainWindow;


    private MainWindow(){

        NICVCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(NICVCheckBox.isSelected())
                    setLocationContainerPanel("NICV");
            }
        });
        SOSDCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(SOSDCheckBox.isSelected())
                    setLocationContainerPanel("SOSD");
            }
        });
        SOSTCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(SOSTCheckBox.isSelected())
                    setLocationContainerPanel("SOST");
            }
        });
        LocationRecommendCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(LocationRecommendCheckBox.isSelected())
                    setLocationContainerPanel("ͨ��");
            }
        });
        StaticAlignCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(StaticAlignCheckBox.isSelected())
                    setAlignContainerPanel("��̬����");
            }
        });
        DTWCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(DTWCheckBox.isSelected())
                    setAlignContainerPanel("��̬����");
            }
        });
        AlignNoCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(AlignNoCheckBox.isSelected())
                    setAlignContainerPanel("��ʹ��");
            }
        });
        AlignRecommendCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(AlignRecommendCheckBox.isSelected())
                    setAlignContainerPanel("ϵͳ�Ƽ�");
            }
        });
        FFTCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(FFTCheckBox.isSelected())
                    setReduceNoiceContainerPanel("FFT");
            }
        });
        POCCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(POCCheckBox.isSelected())
                    setReduceNoiceContainerPanel("POC");
            }
        });
        KalmanFilterCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(KalmanFilterCheckBox.isSelected())
                    setReduceNoiceContainerPanel("�������˲�");
            }
        });
        SSACheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(SSACheckBox.isSelected())
                    setReduceNoiceContainerPanel("SSA");
            }
        });
        ICACheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(ICACheckBox.isSelected())
                    setReduceNoiceContainerPanel("ICA");
            }
        });
        ReduceNoiceNoCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(ReduceNoiceNoCheckBox.isSelected())
                    setReduceNoiceContainerPanel("��ʹ��");
            }
        });
        ReduceNoiceRecommendCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(ReduceNoiceRecommendCheckBox.isSelected())
                    setReduceNoiceContainerPanel("ϵͳ�Ƽ�");
            }
        });
        PCACheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(PCACheckBox.isSelected())
                    setReduceDimensionContainerPanel("PCA");
            }
        });
        LDACheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(LDACheckBox.isSelected())
                    setReduceDimensionContainerPanel("LDA");
            }
        });
        KPCACheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(KPCACheckBox.isSelected())
                    setReduceDimensionContainerPanel("KPCA");
            }
        });
        ReduceDimensionNoCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(ReduceDimensionNoCheckBox.isSelected())
                    setReduceDimensionContainerPanel("��ʹ��");
            }
        });
        ReduceDimensionRecommendCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(ReduceDimensionRecommendCheckBox.isSelected())
                    setReduceDimensionContainerPanel("ϵͳ�Ƽ�");
            }
        });
        zScoreCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(zScoreCheckBox.isSelected())
                    setNormalizationContainerPanel("Z-Score");
            }
        });
        NormalizationNoCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(NormalizationNoCheckBox.isSelected())
                    setNormalizationContainerPanel("��ʹ��");
            }
        });
        NormalizationRecommendCheckBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(NormalizationRecommendCheckBox.isSelected())
                    setNormalizationContainerPanel("ϵͳ�Ƽ�");
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

    private void setLocationContainerPanel(String method){
        mainWindow.LocationContainerPanel.removeAll();
        mainWindow.LocationContainerPanel.setLayout(new BorderLayout());

        LocationPanel locationPanel = new LocationPanel();
        locationPanel.setLable(method);
        mainWindow.LocationContainerPanel.add(locationPanel.getPanel());

        mainWindow.LocationContainerPanel.updateUI();
    }

    private void setAlignContainerPanel(String method){
        mainWindow.AlignContainerPanel.removeAll();
        mainWindow.AlignContainerPanel.setLayout(new BorderLayout());

        if(Objects.equals(method, "��̬����")){
            AlignStaticAlign alignStaticAlign = new AlignStaticAlign();
            alignStaticAlign.setLable(method);
            mainWindow.AlignContainerPanel.add(alignStaticAlign.getPanel());
        }
        else if(Objects.equals(method, "��̬����")){
            AlignDTWAndReduceNoicePOC alignDTWAndReduceNoicePOC = new AlignDTWAndReduceNoicePOC();
            alignDTWAndReduceNoicePOC.setLable(method);
            mainWindow.AlignContainerPanel.add(alignDTWAndReduceNoicePOC.getPanel());
        }
        else if(Objects.equals(method, "��ʹ��")){
            mainWindow.AlignContainerPanel.removeAll();
        }
        else if(Objects.equals(method, "ϵͳ�Ƽ�")){
            //TODO
        }

        mainWindow.AlignContainerPanel.updateUI();
    }

    private void setReduceNoiceContainerPanel(String method){
        mainWindow.ReduceNoiceContainerPanel.removeAll();
        mainWindow.ReduceNoiceContainerPanel.setLayout(new BorderLayout());

        if(Objects.equals(method, "FFT")){
            ReduceNoiceFFTAndNormalizationZScore reduceNoiceFFTAndNormalizationZScore = new ReduceNoiceFFTAndNormalizationZScore();
            reduceNoiceFFTAndNormalizationZScore.setLable(method);
            mainWindow.ReduceNoiceContainerPanel.add(reduceNoiceFFTAndNormalizationZScore.getPanel());
        }
        else if(Objects.equals(method, "POC")){
            AlignDTWAndReduceNoicePOC alignDTWAndReduceNoicePOC = new AlignDTWAndReduceNoicePOC();
            alignDTWAndReduceNoicePOC.setLable(method);
            mainWindow.ReduceNoiceContainerPanel.add(alignDTWAndReduceNoicePOC.getPanel());
        }
        else if(Objects.equals(method, "�������˲�")){

        }
        else if(Objects.equals(method, "SSA")){

        }
        else if(Objects.equals(method, "ICA")){

        }
        else if(Objects.equals(method, "��ʹ��")){
            mainWindow.ReduceNoiceContainerPanel.removeAll();
        }
        else if(Objects.equals(method, "ϵͳ�Ƽ�")){
            //TODO
        }

        mainWindow.ReduceNoiceContainerPanel.updateUI();
    }

    private void setReduceDimensionContainerPanel(String method){
        mainWindow.ReduceDimensionContainerPanel.removeAll();
        mainWindow.ReduceDimensionContainerPanel.setLayout(new BorderLayout());
        if(Objects.equals(method, "PCA")){
            ReduceDimensionPCAAndLDAAndKPCA reduceDimensionPCAAndLDAAndKPCA = new ReduceDimensionPCAAndLDAAndKPCA();
            reduceDimensionPCAAndLDAAndKPCA.setLable(method);
            mainWindow.ReduceDimensionContainerPanel.add(reduceDimensionPCAAndLDAAndKPCA.getPanel());
        }
        else if(Objects.equals(method, "LDA")){
            ReduceDimensionPCAAndLDAAndKPCA reduceDimensionPCAAndLDAAndKPCA = new ReduceDimensionPCAAndLDAAndKPCA();
            reduceDimensionPCAAndLDAAndKPCA.setLable(method);
            mainWindow.ReduceDimensionContainerPanel.add(reduceDimensionPCAAndLDAAndKPCA.getPanel());
        }
        else if(Objects.equals(method, "KPCA")){
            ReduceDimensionPCAAndLDAAndKPCA reduceDimensionPCAAndLDAAndKPCA = new ReduceDimensionPCAAndLDAAndKPCA();
            reduceDimensionPCAAndLDAAndKPCA.setLable(method);
            mainWindow.ReduceDimensionContainerPanel.add(reduceDimensionPCAAndLDAAndKPCA.getPanel());
        }
        else if(Objects.equals(method, "��ʹ��")){
            mainWindow.ReduceNoiceContainerPanel.removeAll();
        }
        else if(Objects.equals(method, "ϵͳ�Ƽ�")){
            //TODO
        }


        mainWindow.ReduceDimensionContainerPanel.updateUI();
    }

    private void setNormalizationContainerPanel(String method){
        mainWindow.NormalizationContainerPanel.removeAll();
        mainWindow.NormalizationContainerPanel.setLayout(new BorderLayout());

        if(Objects.equals(method, "Z-Score")){
            ReduceNoiceFFTAndNormalizationZScore reduceNoiceFFTAndNormalizationZScore = new ReduceNoiceFFTAndNormalizationZScore();
            reduceNoiceFFTAndNormalizationZScore.setLable(method);
            mainWindow.NormalizationContainerPanel.add(reduceNoiceFFTAndNormalizationZScore.getPanel());
        }
        else if(Objects.equals(method, "��ʹ��")){
            mainWindow.NormalizationContainerPanel.removeAll();
        }
        else if(Objects.equals(method, "ϵͳ�Ƽ�")){
            //TODO
        }

        mainWindow.NormalizationContainerPanel.updateUI();
    }


    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException{
        // ����UIManager
        UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
        UIManager.put("CheckBox.font", new Font("Microsoft YaHei", Font.BOLD, 18));
        UIManager.put("Button.font", new Font("Microsoft YaHei", Font.BOLD, 16));
        UIManager.put("TextField.font", new Font("Microsoft YaHei Light", Font.BOLD, 18));

        // ��ʼ��frame��mainwindow
        mainWindow = new MainWindow();
        frame = new JFrame("Preprocess");

        // ����frame���Բ���ʾ
        mainWindow.setFrameContribution(frame, mainWindow);
    }
}
