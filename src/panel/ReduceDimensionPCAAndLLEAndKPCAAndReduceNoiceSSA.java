package panel;

import javax.swing.*;

public class ReduceDimensionPCAAndLLEAndKPCAAndReduceNoiceSSA{
    public JTextField curve_start_textfiled;
    public JTextField sample_start_textfiled;
    public JTextField curve_number_textfiled;
    public JTextField sample_number_textfiled;
    public JTextField character_number_textfield;
    public JPanel ReduceDimensionPCAOrLDAOrKPCAParametersPanel;
    public JLabel ReduceDimensionMethodLable;
    public JLabel SSAUseLable;

    JPanel getPanel(){
        return this.ReduceDimensionPCAOrLDAOrKPCAParametersPanel;
    }

    void setLable(String method){
        this.ReduceDimensionMethodLable.setText(method + "�������ã�");
    }
    String getName(){
        return ReduceDimensionMethodLable.getText();
    }

    void rename(String method){
        if(method == "SSA")
            this.SSAUseLable.setText("��������ֵ�� ");
    }
}
