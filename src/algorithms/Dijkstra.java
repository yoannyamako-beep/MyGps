package algorithms;

import model.Graph;
import model.Intersection;
import model.Route;

import java.util.*;

public class Dijkstra {

    public static List<Intersection> calculerChemin(Graph graph, Intersection depart, Intersection arrivee) {
        Map<Intersection, Double> distances = new HashMap<>();
        Map<Intersection, Intersection> precedent = new HashMap<>();
        PriorityQueue<Intersection> queue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        for (Intersection i : graph.getIntersections()) {
            distances.put(i, Double.MAX_VALUE);
        }
        distances.put(depart, 0.0);
        queue.add(depart);

        while (!queue.isEmpty()) {
            Intersection current = queue.poll();
            if (current == arrivee) break;

            for (Route r : current.getRoutes()) {
                double dist = distances.get(current) + r.getDistance();
                if (dist < distances.get(r.getTo())) {
                    distances.put(r.getTo(), dist);
                    precedent.put(r.getTo(), current);
                    queue.add(r.getTo());
                }
            }
        }

        List<Intersection> path = new ArrayList<>();
        Intersection step = arrivee;
        while (step != null) {
            path.add(0, step);
            step = precedent.get(step);
        }
        return path;
    }
}
