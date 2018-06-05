package panel;

import javax.swing.*;

public class ReduceDimensionPCAAndLDAAndKPCA{
    private JTextField curve_start_textfiled;
    private JTextField sample_start_textfiled;
    private JTextField curve_number_textfiled;
    private JTextField sample_number_textfiled;
    private JTextField character_number_textfield;
    private JPanel ReduceDimensionPCAOrLDAOrKPCAParametersPanel;
    private JLabel ReduceDimensionMethodLable;

    JPanel getPanel(){
        return this.ReduceDimensionPCAOrLDAOrKPCAParametersPanel;
    }

    void setLable(String method){
        this.ReduceDimensionMethodLable.setText(method + "≤Œ ˝≈‰÷√£∫");
    }
    String getName(){
        return ReduceDimensionMethodLable.getText();
    }
}
