package panel;

import javax.swing.*;

public class AlignDTWAndReduceNoicePOC{
    private JTextField curve_start_textfiled;
    private JTextField sample_start_textfiled;
    private JTextField curve_number_textfiled;
    private JTextField sample_number_textfiled;
    private JTextField base_curve_index_textfield;
    private JLabel AlignOrReduceNoiceMethodLable;
    private JPanel AlignDTWOrReduceNoicePOCParametersPanel;

    JPanel getPanel(){
        return this.AlignDTWOrReduceNoicePOCParametersPanel;
    }

    void setLable(String method){
        this.AlignOrReduceNoiceMethodLable.setText(method + this.AlignOrReduceNoiceMethodLable.getText());
    }
}
