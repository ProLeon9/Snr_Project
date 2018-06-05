package panel;

import javax.swing.*;

public class AlignStaticAlign{
    public JTextField curve_start_textfiled;
    public JTextField sample_start_textfiled;
    public JTextField curve_number_textfiled;
    public JTextField sample_number_textfiled;
    public JTextField base_curve_index_textfield;
    public JTextField max_deviation_textfield;
    public JPanel AlignStaticAlignParametersPanel;
    public JLabel AlignMethodLable;

    JPanel getPanel(){
        return this.AlignStaticAlignParametersPanel;
    }

    void setLable(String method){
        this.AlignMethodLable.setText(method + "≤Œ ˝≈‰÷√£∫");
    }
    String getName(){
        return this.AlignMethodLable.getText();
    }
}
