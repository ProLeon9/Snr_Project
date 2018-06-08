package panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PreprocessOrderConfirmDialog extends JDialog{
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JLabel lable1;
    private JLabel lable2;
    private JLabel lable3;
    private JLabel lable4;
    private JLabel lable5;
    private JLabel lable6;
    private JLabel lable7;
    private JLabel lable8;
    private JLabel lable9;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JTextField textField8;
    private JTextField textField9;

    List<String> result;
    private Set<String> methodSet;
    private boolean[] systemRecomendOption;
    private int methodNumberIndex;
    private List<JTextField> listTextField;
    private List<JLabel> listLable;

    PreprocessOrderConfirmDialog(Set<String> methodSet, boolean[] systemRecomendOption){
        this.methodSet = methodSet;
        this.systemRecomendOption = systemRecomendOption;

        setContentPane(contentPane);
        getCurrectPanel();
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // get the screen size
        this.setBounds((int) (screenSize.width*0.25), (int) (screenSize.height*0.25), (int) (screenSize.width*0.5), (int) (screenSize.height*0.5));


        buttonOK.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK(){
        // add your code here
        getResult();
        dispose();
    }

    private void onCancel(){
        // add your code here if necessary
        dispose();
    }

    private void getResult(){
        this.result = new ArrayList<>();
        for(int i=0; i<=this.methodNumberIndex; i++)
            this.result.add("");
        for(int k=0; k<=this.methodNumberIndex; k++){
            result.set(Integer.parseInt(listTextField.get(k).getText()), listLable.get(k).getText());
        }
    }

    private void removeTheRecommendMethod(Set<String> methodSet, boolean[] systemRecomendOption){
        if(systemRecomendOption[1]){
            methodSet.remove("¾²Ì¬¶ÔÆë");
            methodSet.remove("¶¯Ì¬¶ÔÆë");
        }
        if(systemRecomendOption[2]){
            methodSet.remove("FFT");
            methodSet.remove("POC");
            methodSet.remove("¿¨¶ûÂüÂË²¨");
            methodSet.remove("SSA");
            methodSet.remove("ICA");
        }
        if(systemRecomendOption[3]){
            methodSet.remove("PCA");
            methodSet.remove("LLE");
            methodSet.remove("KPCA");
        }
        if(systemRecomendOption[4]){
            methodSet.remove("Z-Score");
        }
    }

    private void getCurrectPanel(){
        this.listTextField = new ArrayList<>();
        listTextField.add(textField1);
        listTextField.add(textField2);
        listTextField.add(textField3);
        listTextField.add(textField4);
        listTextField.add(textField5);
        listTextField.add(textField6);
        listTextField.add(textField7);
        listTextField.add(textField8);
        listTextField.add(textField9);

        this.listLable = new ArrayList<>();
        listLable.add(lable1);
        listLable.add(lable2);
        listLable.add(lable3);
        listLable.add(lable4);
        listLable.add(lable5);
        listLable.add(lable6);
        listLable.add(lable7);
        listLable.add(lable8);
        listLable.add(lable9);

        removeTheRecommendMethod(this.methodSet, this.systemRecomendOption);

        int i = 0;
        for(String element : methodSet){
            listLable.get(i++).setText(element);
        }
        int j=i;
        i -= 1;
        this.methodNumberIndex = i;
        for(; j <= 8; j++){
            listTextField.get(j).setVisible(false);
            listLable.get(j).setVisible(false);
        }
    }
}
