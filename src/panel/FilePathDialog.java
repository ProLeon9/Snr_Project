package panel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;

public class FilePathDialog extends JDialog{
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField MatlabPathTextField;
    private JButton MatlabPathOpenButton;
    private JTextField CurvePathTextField;
    private JTextField ResultPathTextField;
    private JButton CurvePathOpenButton;
    private JButton ResultPathOpenButton;
    private JButton ICACurvePathOpenButton;
    private JTextField ICACurvePathTextField;
    private JPanel ICAPathPanel;

    public String curvePath;
    public String resultPath;
    public String matlabPath;
    public String lastPath;
    public String ICAPath;

    public FilePathDialog(){
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // get the screen size
        Dimension preferSize = new Dimension((int) (screenSize.width*0.6), (int) (screenSize.height*0.3));
        this.setBounds((int) (screenSize.width*0.2), (int) (screenSize.height*0.35), (int) (screenSize.width*0.6), (int) (screenSize.height*0.3));
        this.setPreferredSize(preferSize);

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
        CurvePathOpenButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                curvePath = GetPath("file", lastPath);
                CurvePathTextField.setText(curvePath);
                lastPath = curvePath;
            }
        });
        ResultPathOpenButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                resultPath = GetPath("folder", lastPath);
                ResultPathTextField.setText(resultPath);
            }
        });
        MatlabPathOpenButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                matlabPath = GetPath("file", null);
                MatlabPathTextField.setText(matlabPath);
            }
        });
        ICACurvePathOpenButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                ICAPath = GetPath("file", lastPath);
                ICACurvePathTextField.setText(ICAPath);
            }
        });
    }

    private void onOK(){
        // add your code here
        dispose();
    }

    private void onCancel(){
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args){
        FilePathDialog dialog = new FilePathDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private String GetPath(String type, String lastPath){
        String filePath = "";

        //暂时设置为Windows风格
        try{
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch(ClassNotFoundException|InstantiationException|UnsupportedLookAndFeelException|IllegalAccessException e){
            e.printStackTrace();
        }

        JFileChooser fileChooser = new JFileChooser(lastPath);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Traces set", "txt", "trs");

        switch(type){
            case "folder":
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                break;
            case "file":
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setMultiSelectionEnabled(false);
                fileChooser.setFileFilter(filter);
                break;
        }

        //恢复初始风格
        try{
            UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
        }
        catch(Exception e){
            // TODO: Exception needs to be dealed with
        }

        int returnVal = fileChooser.showOpenDialog(null); //保证窗口图标一致
        if(returnVal == JFileChooser.APPROVE_OPTION){
            filePath = fileChooser.getSelectedFile().getAbsolutePath();
        }

        return filePath;
    }
}
