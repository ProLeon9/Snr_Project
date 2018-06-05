package panel;

import javax.swing.*;

public class AlignDTWAndReduceNoicePOC{
    public JTextField curve_start_textfiled;
    public JTextField sample_start_textfiled;
    public JTextField curve_number_textfiled;
    public JTextField sample_number_textfiled;
    public JTextField base_curve_index_textfield;
    public JLabel AlignOrReduceNoiceMethodLable;
    public JPanel AlignDTWOrReduceNoicePOCParametersPanel;

    JPanel getPanel(){
        return this.AlignDTWOrReduceNoicePOCParametersPanel;
    }

    void setLable(String method){
        this.AlignOrReduceNoiceMethodLable.setText(method + "≤Œ ˝≈‰÷√£∫");
    }

    String getName(){
        return AlignOrReduceNoiceMethodLable.getText();
    }
}
