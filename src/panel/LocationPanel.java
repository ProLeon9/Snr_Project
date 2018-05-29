package panel;

import javax.swing.*;

public class LocationPanel{
    private JComboBox LocationAlgorithmCombobox;
    private JComboBox LocationOperationCombobox;
    private JComboBox LocationAttackModelCombobox;
    private JTextField LocationKeyIndexTextfield;
    private JTextField LocationSampleFirstTextfield;
    private JTextField LocationSampleNumberTextfield;
    private JTextField LocationCurveFirstTextfield;
    private JTextField LocationCurveNumberTextfield;
    private JPanel LocationParametersPanel;
    private JTextField subkey_lengthTextfiled;
    private JLabel LocationMethodLable;

    JPanel getPanel(){
        return this.LocationParametersPanel;
    }

    void setLable(String method){
        this.LocationMethodLable.setText(method + this.LocationMethodLable.getText());
    }
}
