package fr.umontpellier.iut.graphes;

import fr.umontpellier.iut.rails.Route;

import java.util.Objects;

/**
 * Classe modélisant les arêtes. Pour simplifier, vous pouvez supposer le prérequis que i!=j
 */
public record Arete(int i, int j, Route route) {

    private static Integer extremite1;
    private static Integer extremite2;
    private static Integer poids;

    public Arete(int i, int j) {
        this(i, j, null);
    }

    public boolean incidenteA(int v) {
        return i == v || j == v;
    }

    public int getAutreSommet(int v) {
        return v == i ? j : i;
    }


    @Override
    public String toString() {
        return "Arete{" +
                "i=" + i +
                ", j=" + j +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Arete)) return false;
        Arete arete = (Arete) o;
        boolean b = i == arete.i && j == arete.j || i == arete.j && j == arete.i;

        if (route == null && arete.route == null) {
            return b;
        } else if (route == null || arete.route == null) {
            return false;
        }
        return b && route.equals(arete.route);
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j, route);
    }
}