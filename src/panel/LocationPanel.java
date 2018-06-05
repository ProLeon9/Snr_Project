package panel;

import javax.swing.*;

public class LocationPanel{
    public JComboBox LocationAlgorithmCombobox;
    public JComboBox LocationOperationCombobox;
    public JComboBox LocationAttackModelCombobox;
    public JTextField LocationKeyIndexTextfield;
    public JTextField LocationSampleFirstTextfield;
    public JTextField LocationSampleNumberTextfield;
    public JTextField LocationCurveFirstTextfield;
    public JTextField LocationCurveNumberTextfield;
    public JPanel LocationParametersPanel;
    public JLabel LocationMethodLable;

    JPanel getPanel(){
        return this.LocationParametersPanel;
    }

    void setLable(String method){
        this.LocationMethodLable.setText(method + "≤Œ ˝≈‰÷√£∫");
    }
}
