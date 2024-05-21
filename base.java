import javax.swing.*;

public class base extends JFrame{


    public base(Entry root) {
        setTitle("Huffman Tree Visualization");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        TreePanel treePanel = new TreePanel(root);
        add(treePanel);
    }

}
