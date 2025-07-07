package ec.edu.ups.poo.view;

import javax.swing.*;
import java.awt.*;

public class JDesktopPaneConFondo extends JDesktopPane {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();

        g.setColor(new Color(135, 206, 250));
        g.fillRect(0, 0, w, h/2);


        g.setColor(new Color(0, 105, 148));
        g.fillRect(0, h/2, w, h/4);


        g.setColor(new Color(238, 214, 175));
        g.fillRect(0, (3*h)/4, w, h/4);


        g.setColor(Color.YELLOW);
        g.fillOval(w-120, 20, 80, 80);


        g.setColor(Color.WHITE);
        for (int i = 0; i < w; i += 60) {
            g.drawArc(i, h/2 + 30, 40, 20, 0, 180);
        }


        g.setColor(Color.RED);
        int[] xUmbrella = {80, 120, 100};
        int[] yUmbrella = {(3*h)/4, (3*h)/4, (3*h)/4 - 40};
        g.fillPolygon(xUmbrella, yUmbrella, 3);
        g.setColor(new Color(139,69,19));
        g.fillRect(99, (3*h)/4 - 40, 3, 40);


        g.setColor(Color.PINK);
        g.fillRect(150, (3*h)/4 + 30, 60, 25);
    }
}