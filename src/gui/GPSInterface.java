package gui;

import algorithms.Dijkstra;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class GPSInterface extends JFrame {

    private Graph graph;
    private Intersection depart;
    private Intersection arrivee;
    private GraphPanel graphPanel;
    private Random random = new Random();

    public GPSInterface() {
        setTitle("GPS Dynamique - Graphe Routier");
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
        // reset des accidents
        for (Route r : graph.getRoutes()) {
            r.setAccident(false);
        }

        // nouvel accident aléatoire
        Route r = graph.getRoutes().get(random.nextInt(graph.getRoutes().size()));
        r.setAccident(true);

        recalculer();
    }
}
