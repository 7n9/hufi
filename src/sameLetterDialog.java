import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class sameLetterDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel label1;

    public sameLetterDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle(StringTranslate.getInstance().translateString("dialog.title"));
        label1.setText(StringTranslate.getInstance().translateString("dialog.content"));

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }

}