package panel;

import javax.swing.*;

public class AlignStaticAlign{
    private JTextField curve_start_textfiled;
    private JTextField sample_start_textfiled;
    private JTextField curve_number_textfiled;
    private JTextField sample_number_textfiled;
    private JTextField base_curve_index_textfield;
    private JTextField max_deviation_textfield;
    private JPanel AlignStaticAlignParametersPanel;
    private JLabel AlignMethodLable;

    JPanel getPanel(){
        return this.AlignStaticAlignParametersPanel;
    }

    void setLable(String method){
        this.AlignMethodLable.setText(method + this.AlignMethodLable.getText());
    }
}
