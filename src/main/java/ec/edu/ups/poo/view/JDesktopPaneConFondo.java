package ec.edu.ups.poo.view;

import javax.swing.*;
import java.awt.*;

public class JDesktopPaneConFondo extends JDesktopPane {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();

        // Cielo
        g.setColor(new Color(135, 206, 250)); // Azul claro
        g.fillRect(0, 0, w, h/2);

        // Mar
        g.setColor(new Color(0, 105, 148)); // Azul marino
        g.fillRect(0, h/2, w, h/4);

        // Arena
        g.setColor(new Color(238, 214, 175)); // Arena
        g.fillRect(0, (3*h)/4, w, h/4);

        // Sol
        g.setColor(Color.YELLOW);
        g.fillOval(w-120, 20, 80, 80);

        // Algunas olas
        g.setColor(Color.WHITE);
        for (int i = 0; i < w; i += 60) {
            g.drawArc(i, h/2 + 30, 40, 20, 0, 180);
        }

        // Sombrilla
        g.setColor(Color.RED);
        int[] xUmbrella = {80, 120, 100};
        int[] yUmbrella = {(3*h)/4, (3*h)/4, (3*h)/4 - 40};
        g.fillPolygon(xUmbrella, yUmbrella, 3);
        g.setColor(new Color(139,69,19)); // Palo marrón
        g.fillRect(99, (3*h)/4 - 40, 3, 40);

        // Toalla
        g.setColor(Color.PINK);
        g.fillRect(150, (3*h)/4 + 30, 60, 25);
    }
}