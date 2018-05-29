package panel;

import javax.swing.*;

public class ReduceNoiceFFTAndNormalizationZScore{
    private JTextField curve_start_textfiled;
    private JTextField sample_start_textfiled;
    private JTextField curve_number_textfiled;
    private JTextField sample_number_textfiled;
    private JLabel ReduceNoiceOrNormalizationMethodLable;
    private JPanel ReduceNoiceFFTOrNormalizationZScoreParametersPanel;

    JPanel getPanel(){
        return this.ReduceNoiceFFTOrNormalizationZScoreParametersPanel;
    }

    void setLable(String method){
        this.ReduceNoiceOrNormalizationMethodLable.setText(method + this.ReduceNoiceOrNormalizationMethodLable.getText());
    }
}
