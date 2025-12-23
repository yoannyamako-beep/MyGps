package model;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    private List<Intersection> intersections;
    private List<Route> routes;

    public Graph() {
        intersections = new ArrayList<>();
        routes = new ArrayList<>();
    }

    public void addIntersection(Intersection i) {
        intersections.add(i);
    }

    public void addRoute(Route r) {
        routes.add(r);
        r.getFrom().addRoute(r);
    }

    public void addRouteBidirectionnelle(Intersection a, Intersection b, double distance) {
        Route r1 = new Route(a, b, distance);
        Route r2 = new Route(b, a, distance);
        routes.add(r1);
        routes.add(r2);
        a.addRoute(r1);
        b.addRoute(r2);
    }

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public List<Route> getRoutes() {
        return routes;
    }
}
