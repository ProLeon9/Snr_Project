package panel;

import javax.swing.*;

public class ReduceNoiceFFTAndNormalizationZScore{
    public JTextField curve_start_textfiled;
    public JTextField sample_start_textfiled;
    public JTextField curve_number_textfiled;
    public JTextField sample_number_textfiled;
    private JLabel ReduceNoiceOrNormalizationMethodLable;
    private JPanel ReduceNoiceFFTOrNormalizationZScoreParametersPanel;

    JPanel getPanel(){
        return this.ReduceNoiceFFTOrNormalizationZScoreParametersPanel;
    }

    void setLable(String method){
        this.ReduceNoiceOrNormalizationMethodLable.setText(method + "≤Œ ˝≈‰÷√£∫");
    }

    String getName(){
        return ReduceNoiceOrNormalizationMethodLable.getText();
    }
}
