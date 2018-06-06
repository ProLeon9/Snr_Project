package panel;

import javax.swing.*;

public class ReduceNoiceKalmanFilter{
    private JPanel ReduceNoiceKalmanFilterParametersPanel;
    public JTextField curve_start_textfiled;
    public JTextField sample_start_textfiled;
    public JTextField curve_number_textfiled;
    public JTextField sample_number_textfiled;
    public JLabel ReduceNoiceMethodLable;
    public JTextField QValue_textfield;
    public JTextField RValue_textfield;

    JPanel getPanel(){
        return this.ReduceNoiceKalmanFilterParametersPanel;
    }

    void setLable(String method){
        this.ReduceNoiceMethodLable.setText(method + "≤Œ ˝≈‰÷√£∫");
    }

    String getName(){
        return ReduceNoiceMethodLable.getText();
    }
}
