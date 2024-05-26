package src;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseLanguageDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JComboBox comboBox1;


    public ChooseLanguageDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
    }

    private void onOK() {

        switch (comboBox1.getSelectedIndex()){
            case 0:
                Huffman.languageChoosen = "en.lang";
                break;
            case 1:
                Huffman.languageChoosen = "pl.lang";
                break;
        }
        dispose();
    }
}
