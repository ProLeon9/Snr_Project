package panel;

import javax.swing.*;
import java.util.Objects;

public class ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSAAndICA{
    public JTextField curve_start_textfiled;
    public JTextField sample_start_textfiled;
    public JTextField curve_number_textfiled;
    public JTextField sample_number_textfiled;
    public JTextField character_number_textfield;
    public JPanel ReduceDimensionPCAOrLDAOrKPCAParametersPanel;
    public JLabel ReduceDimensionMethodLable;
    public JLabel SSAAndICAUseLable;

    JPanel getPanel(){
        return this.ReduceDimensionPCAOrLDAOrKPCAParametersPanel;
    }

    void setLable(String method){
        this.ReduceDimensionMethodLable.setText(method + "参数配置：");
    }
    String getName(){
        return ReduceDimensionMethodLable.getText();
    }

    void rename(String method){
        if(Objects.equals(method, "SSA")){
            this.SSAAndICAUseLable.setText("滑动窗口值： ");
            this.character_number_textfield.setText("500");
        }
        else if(Objects.equals(method, "ICA")){
            this.SSAAndICAUseLable.setVisible(false);
            this.character_number_textfield.setVisible(false);
        }
    }
}
