import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class base extends JFrame{


    public base() {
        ProperDisplay pd = new ProperDisplay();
        setContentPane(pd.mainpanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setTitle("Huffman Tree Visualization");
        setSize(800, 600);
        setLocationRelativeTo(null);
//        TreePanel treePanel = new TreePanel(root);
//        add(treePanel);
    }

}
