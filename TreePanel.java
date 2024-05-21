import javax.swing.*;
import java.awt.*;

public class TreePanel extends JPanel {
    private Entry root;

    public TreePanel(Entry root) {
        super(true);//doublebuff
        this.root = root;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (root != null) {
            drawTree(g, root, getWidth() / 2, 30, getWidth() / 4);
        }
    }

    private void drawTree(Graphics g, Entry node, int x, int y, int xOffset) {
        if (node == null) {
            return;
        }
        setBackground(Color.gray);

        g.setColor(Color.darkGray);
        g.fillOval(x - 15, y - 15, 30, 30);
        g.setColor(Color.lightGray);
        g.drawString(String.valueOf(node.c), x - 7, y + 5);

        if (node.left != null) {
            g.setColor(Color.darkGray);
            g.drawLine(x, y, x - xOffset, y + 70);
            int a = (x+x-xOffset)/2;
            int b = (y+y+70)/2;
            g.fillOval(a - 8, b - 15, 20, 20);
            g.setColor(Color.lightGray);
            g.drawString("0", a, b);
            drawTree(g, node.left, x - xOffset, y + 70, xOffset / 2);
        }

        if (node.right != null) {
            g.setColor(Color.darkGray);
            g.drawLine(x, y, x + xOffset, y + 70);
            int a = (x+x+xOffset)/2;
            int b = (y+y+70)/2;
            g.fillOval(a - 8, b - 15, 20, 20);
            g.setColor(Color.lightGray);
            g.drawString("1", a, b);
            drawTree(g, node.right, x + xOffset, y + 70, xOffset / 2);
        }
    }


}