import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Random;
import java.awt.BorderLayout;



// CLASSES DU MODELE

class Intersection {
    private String name;
    private int x, y;
    private List<Route> routes;

    public Intersection(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.routes = new ArrayList<>();
    }

    public String getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }

    public void addRoute(Route r) { routes.add(r); }
    public List<Route> getRoutes() { return routes; }

    @Override
    public String toString() { return name; }
}

class Route {
    private Intersection from;
    private Intersection to;
    private double distance;
    private boolean accident = false;

    public Route(Intersection from, Intersection to, double distance) {
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public Intersection getFrom() { return from; }
    public Intersection getTo() { return to; }

    public double getDistance() {
        return accident ? distance * 5 : distance; // accident = coût élevé
    }

    public boolean hasAccident() { return accident; }
    public void setAccident(boolean accident) { this.accident = accident; }

    @Override
    public String toString() {
        return from + "->" + to + (accident ? " (accident)" : "");
    }
}

class Graph {
    private List<Intersection> intersections = new ArrayList<>();
    private List<Route> routes = new ArrayList<>();

    public void addIntersection(Intersection i) { intersections.add(i); }
    public void addRoute(Route r) { routes.add(r); r.getFrom().addRoute(r); }

    public void addRouteBidirectionnelle(Intersection a, Intersection b, double distance) {
        Route r1 = new Route(a, b, distance);
        Route r2 = new Route(b, a, distance);
        addRoute(r1);
        addRoute(r2);
    }

    public List<Intersection> getIntersections() { return intersections; }
    public List<Route> getRoutes() { return routes; }
}

// ALGORITHME DE DIJKSTRA

class Dijkstra {
    public static List<Intersection> calculerChemin(Graph g, Intersection depart, Intersection arrivee) {
        Map<Intersection, Double> dist = new HashMap<>();
        Map<Intersection, Intersection> prev = new HashMap<>();
        PriorityQueue<Intersection> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));

        for (Intersection i : g.getIntersections()) {
            dist.put(i, Double.MAX_VALUE);
            prev.put(i, null);
        }
        dist.put(depart, 0.0);
        pq.add(depart);

        while (!pq.isEmpty()) {
            Intersection u = pq.poll();
            if (u == arrivee) break;

            for (Route r : u.getRoutes()) {
                double alt = dist.get(u) + r.getDistance();
                Intersection v = r.getTo();
                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    prev.put(v, u);
                    pq.remove(v); // mise à jour
                    pq.add(v);
                }
            }
        }

        // reconstruire le chemin
        List<Intersection> chemin = new ArrayList<>();
        Intersection u = arrivee;
        while (u != null) {
            chemin.add(0, u);
            u = prev.get(u);
        }
        return chemin;
    }
}

// PANNEAU DE DESSIN

class GraphPanel extends JPanel {
    private Graph graph;
    private List<Intersection> chemin;

    public GraphPanel(Graph graph) { this.graph = graph; }

    public void setChemin(List<Intersection> chemin) { this.chemin = chemin; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // dessiner les routes
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

        // dessiner le chemin optimal
        if (chemin != null) {
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(3));
            for (int i = 0; i < chemin.size() - 1; i++) {
                Intersection a = chemin.get(i);
                Intersection b = chemin.get(i + 1);
                g2.drawLine(a.getX(), a.getY(), b.getX(), b.getY());
            }
        }

        // dessiner les intersections
        g2.setColor(Color.BLUE);
        for (Intersection i : graph.getIntersections()) {
            g2.fillOval(i.getX() - 8, i.getY() - 8, 16, 16);
            g2.drawString(i.getName(), i.getX() - 5, i.getY() - 10);
        }
    }
}

// INTERFACE GPS

class GPSInterface extends JFrame {
    private Graph graph;
    private Intersection depart, arrivee;
    private GraphPanel graphPanel;
    private Random random = new Random();

    public GPSInterface() {
        setTitle("GPS Dynamique");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initialiserGraphe();

        graphPanel = new GraphPanel(graph);
        add(graphPanel, BorderLayout.CENTER);

        JButton accidentBtn = new JButton("Accident aléatoire");
        accidentBtn.addActionListener(e -> accidentAleatoire());
        add(accidentBtn, BorderLayout.SOUTH);

        recalculer();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initialiserGraphe() {
        graph = new Graph();

        Intersection A = new Intersection("A", 100, 100);
        Intersection B = new Intersection("B", 300, 80);
        Intersection C = new Intersection("C", 500, 200);
        Intersection D = new Intersection("D", 300, 350);
        Intersection E = new Intersection("E", 100, 250);

        graph.addIntersection(A);
        graph.addIntersection(B);
        graph.addIntersection(C);
        graph.addIntersection(D);
        graph.addIntersection(E);

        graph.addRouteBidirectionnelle(A, B, 5);
        graph.addRouteBidirectionnelle(B, C, 5);
        graph.addRouteBidirectionnelle(C, D, 5);
        graph.addRouteBidirectionnelle(D, E, 5);
        graph.addRouteBidirectionnelle(E, A, 5);
        graph.addRouteBidirectionnelle(B, D, 10);
        graph.addRouteBidirectionnelle(A, C, 15);

        depart = A;
        arrivee = D;
    }

    private void recalculer() {
        List<Intersection> chemin = Dijkstra.calculerChemin(graph, depart, arrivee);
        graphPanel.setChemin(chemin);
    }

    private void accidentAleatoire() {
        // reset accidents
        for (Route r : graph.getRoutes()) r.setAccident(false);

        // nouvel accident aléatoire
        Route r = graph.getRoutes().get(random.nextInt(graph.getRoutes().size()));
        r.setAccident(true);

        recalculer();
    }
}

// MAIN

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GPSInterface::new);
    }
}
