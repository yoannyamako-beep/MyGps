package model;

import java.util.List;

public class Voiture {
    private Intersection position;
    private List<Intersection> trajet;

    public Voiture(Intersection start) {
        this.position = start;
    }

    public Intersection getPosition() { return position; }
    public void setTrajet(List<Intersection> trajet) { this.trajet = trajet; }
    public List<Intersection> getTrajet() { return trajet; }
}
