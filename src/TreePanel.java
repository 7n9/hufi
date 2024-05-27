package src;

import javax.swing.*;
import java.awt.*;
import java.util.PriorityQueue;

public class TreePanel extends JPanel {
    private Entry root;
    private String queueContents;

    public TreePanel(Entry root, String q) {
        super(false);//doublebuff
        this.root = root;
        this.queueContents = q;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

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

        //g.drawString();


        g.fillOval(x - 15, y - 15, 30, 30);


        g.setColor(Color.darkGray);
        String queueString = StringTranslate.getInstance().translateString("queue.string");
        g.drawString(queueString + ": " + queueContents, 4, getHeight() - 9);
        g.setColor(Color.lightGray);
        g.drawString(queueString + ": " + queueContents, 5, getHeight() - 10);


        if(node.c == '\0' && !node.combined.isEmpty()){
            Graphics2D g2d = (Graphics2D) g.create();
            FontMetrics fm = g2d.getFontMetrics();
            g.setColor(Color.darkGray);
            g.fillRect(x - fm.stringWidth(node.combined)/2 - 3, y - 8, fm.stringWidth(node.combined) + 6, 16);
            g.setColor(Color.lightGray);

            g.drawString(node.combined, x - fm.stringWidth(node.combined)/2, y + 5);
            //g.draw3DRect(x - fm.stringWidth(node.combined)/2 - 3, y - 8, fm.stringWidth(node.combined) + 6, 16, true);
            //g.drawRect(x - fm.stringWidth(node.combined)/2 - 3, y - 8, fm.stringWidth(node.combined) + 6, 16);
        }else {
            g.drawString(String.valueOf(node.c), x - 7, y + 5);
        }

        if (node.left != null) {
            if(node.left.last){
                g.setColor(Color.red);
            }else{
                g.setColor(Color.darkGray);
            }
            g.drawLine(x, y+15, x - xOffset, y + 70);
            g.setColor(Color.darkGray);
            int a = (x+x-xOffset)/2;
            int b = (y+y+70)/2;
            g.fillOval(a - 8, b - 15, 20, 20);
            g.setColor(Color.lightGray);
            g.drawString("0", a, b);
            drawTree(g, node.left, x - xOffset, y + 70, xOffset / 2);
        }

        if (node.right != null) {
            if(node.right.last){
                g.setColor(Color.red);
            }else{
                g.setColor(Color.darkGray);
            }
            g.drawLine(x, y+15, x + xOffset, y + 70);
            g.setColor(Color.darkGray);
            int a = (x+x+xOffset)/2;
            int b = (y+y+70)/2;
            g.fillOval(a - 8, b - 15, 20, 20);
            g.setColor(Color.lightGray);
            g.drawString("1", a, b);
            drawTree(g, node.right, x + xOffset, y + 70, xOffset / 2);
        }
    }


}