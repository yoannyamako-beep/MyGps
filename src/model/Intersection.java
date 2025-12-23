package model;

import java.util.ArrayList;
import java.util.List;

public class Intersection {

    private String name;
    private int x;
    private int y;
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

    public void addRoute(Route r) {
        routes.add(r);
    }

    public List<Route> getRoutes() {
        return routes;
    }

    @Override
    public String toString() {
        return name;
    }
}
