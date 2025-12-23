package gui;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GraphPanel extends JPanel {

    private Graph graph;
    private List<Intersection> chemin;

    public GraphPanel(Graph graph) {
        this.graph = graph;
    }

    public void setChemin(List<Intersection> chemin) {
        this.chemin = chemin;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Dessiner les routes
        for (Route r : graph.getRoutes()) {
            int x1 = r.getFrom().getX();
            int y1 = r.getFrom().getY();
            int x2 = r.getTo().getX();
            int y2 = r.getTo().getY();

            if (r.hasAccident()) {
                g2.setColor(Color.ORANGE);
                g2.setStroke(new BasicStroke(4));
            } else {
                g2.setColor(Color.GRAY);
                g2.setStroke(new BasicStroke(1));
            }
            g2.drawLine(x1, y1, x2, y2);
        }

        // Dessiner le chemin optimal
        if (chemin != null) {
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(3));
            for (int i = 0; i < chemin.size() - 1; i++) {
                Intersection a = chemin.get(i);
                Intersection b = chemin.get(i + 1);
                g2.drawLine(a.getX(), a.getY(), b.getX(), b.getY());
            }
        }

        // Dessiner les intersections
        g2.setColor(Color.BLUE);
        for (Intersection i : graph.getIntersections()) {
            g2.fillOval(i.getX() - 8, i.getY() - 8, 16, 16);
            g2.drawString(i.getName(), i.getX() - 5, i.getY() - 10);
        }
    }
}
