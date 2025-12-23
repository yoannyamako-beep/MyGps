package model;

public class Route {

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
        return accident ? distance * 5 : distance; // accident = coût très élevé
    }

    public boolean hasAccident() { return accident; }
    public void setAccident(boolean accident) { this.accident = accident; }

    @Override
    public String toString() {
        return from + "->" + to + (accident ? " (accident)" : "");
    }
}
