package fr.umontpellier.iut.graphes;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.umontpellier.iut.rails.Route;

import java.util.*;

/**
 * (Multi) Graphe non-orienté pondéré. Le poids de chaque arête correspond à la longueur de la route correspondante.
 * Pour une paire de sommets fixée {i,j}, il est possible d'avoir plusieurs arêtes
 * d'extrémités i et j et de longueur identique, du moment que leurs routes sont différentes.
 * Par exemple, il est possible d'avoir les deux arêtes suivantes dans le graphe :
 * Arete a1 = new Arete(i,j,new RouteTerrestre(villes.get("Lima"), villes.get("Valparaiso"), Couleur.GRIS, 2))
 * et
 * Arete a2 = new Arete(i,j,new RouteTerrestre(villes.get("Lima"), villes.get("Valparaiso"), Couleur.GRIS, 2))
 * Dans cet exemple (issus du jeu), a1 et a2 sont deux arêtes différentes, même si leurs routes sont très similaires
 * (seul l'attribut nom est différent).
 */
public class Graphe {

    /**
     * Liste d'incidences :
     * mapAretes.get(1) donne l'ensemble d'arêtes incidentes au sommet dont l'identifiant est 1
     * Si mapAretes.get(u) contient l'arête {u,v} alors, mapAretes.get(v) contient aussi cette arête
     */
    private Map<Integer, HashSet<Arete>> mapAretes;

    private Object nbAretes;
    private Object nbSommets;
    private List<Integer> sequenceDegres;
    private boolean pondere;


    /**
     * Construit un graphe à n sommets 0..n-1 sans arêtes
     */
    public Graphe(int n) {
        this.mapAretes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            mapAretes.put(i, new HashSet<>());
        }
    }

    /**
     * Construit un graphe vide
     */
    public Graphe() {
        this.mapAretes = new HashMap<>();
    }

    /**
     * Construit un graphe à partir d'une collection d'arêtes.
     *
     * @param aretes la collection d'arêtes
     */
    public Graphe(Collection<Arete> aretes) {
        this();
        for (Arete a : aretes) {
            ajouterArete(a);
        }
    }

    /**
     * À partir d'un graphe donné, construit un sous-graphe induit
     * par un ensemble de sommets, sans modifier le graphe donné
     *
     * @param graphe le graphe à partir duquel on construit le sous-graphe
     * @param X      l'ensemble de sommets qui définissent le sous-graphe
     *               prérequis : X inclus dans V()
     */
    public Graphe(Graphe graphe, Set<Integer> X) {
        this();
        for (int i : X) {
            for (Arete a : graphe.mapAretes.get(i)) {
                if (X.contains(a.i()) && X.contains(a.i())) {
                    ajouterArete(a);
                }
            }
        }
    }

    /**
     * @return l'ensemble de sommets du graphe
     */


    /**
     * @return l'ensemble de sommets du graphe
     */
    public Set<Integer> ensembleSommets() {
        return mapAretes.keySet();
    }

    /**
     * @return l'ordre du graphe (le nombre de sommets)
     */
    public int nbSommets() {
        return mapAretes.size();
    }

    /**
     * @return le nombre d'arêtes du graphe (ne pas oublier que this est un multigraphe : si plusieurs arêtes sont présentes entre un même coupe de sommets {i,j}, il faut
     * toutes les compter)
     */
    public int nbAretes() {
        int nbAretes = 0;
        for (HashSet<Arete> aretes : mapAretes.values()) {
            nbAretes += aretes.size();
        }
        return nbAretes / 2;
    }


    public boolean contientSommet(Integer v) {
        return mapAretes.containsKey(v);
    }

    /**
     * Ajoute un sommet au graphe s'il n'est pas déjà présent
     *
     * @param v le sommet à ajouter
     */
    public void ajouterSommet(Integer v) {
        if (!contientSommet(v)) {
            mapAretes.put(v, new HashSet<>());
        }
    }

    /**
     * Ajoute une arête au graphe si elle n'est pas déjà présente
     *
     * @param a l'arête à ajouter. Si les 2 sommets {i,j} de a ne sont pas dans l'ensemble,
     *          alors les sommets sont automatiquement ajoutés à l'ensemble de sommets du graphe
     */
    public void ajouterArete(Arete a) {
        if (!existeArete(a)) {
            ajouterSommet(a.i());
            ajouterSommet(a.j());
            mapAretes.get(a.i()).add(a);
            mapAretes.get(a.j()).add(a);
        }
    }

    /**
     * Supprime une arête du graphe si elle est présente, sinon ne fait rien
     *
     * @param a arête à supprimer
     */
    public void supprimerArete(Arete a) {
        if (existeArete(a)) {
            mapAretes.get(a.i()).remove(a);
            mapAretes.get(a.j()).remove(a);
        }
    }

    /**
     * @param a l'arête dont on veut tester l'existence
     * @return true si a est présente dans le graphe
     */
    public boolean existeArete(Arete a) {
        return mapAretes.containsKey(a.i()) && mapAretes.get(a.i()).contains(a) && mapAretes.containsKey(a.j()) && mapAretes.get(a.j()).contains(a);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer v : mapAretes.keySet()) {
            sb.append("sommet").append(v).append(" : ").append(mapAretes.get(v)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Retourne l'ensemble des sommets voisins d'un sommet donné.
     * Si le sommet n'existe pas, l'ensemble retourné est vide.
     *
     * @param v l'identifiant du sommet dont on veut le voisinage
     */
    public Set<Integer> getVoisins(int v) {
        Set<Integer> voisins = new HashSet<>();
        if (contientSommet(v)) {
            for (Arete a : mapAretes.get(v)) {
                voisins.add(a.getAutreSommet(v));
            }
        } else return voisins;
        return voisins;
    }

    /**
     * Supprime un sommet du graphe, ainsi que toutes les arêtes incidentes à ce sommet
     *
     * @param v le sommet à supprimer
     */
    public void supprimerSommet(int v) {
        if (contientSommet(v)) {
            for (Arete a : mapAretes.get(v)) {
                mapAretes.get(a.getAutreSommet(v)).remove(a);
            }
            mapAretes.remove(v);
        }
    }

    /**
     * @return le degré d'un sommet donné
     * prérequis : le sommet doit exister dans le graphe
     */

    public int degre(int v) {
        if (contientSommet(v)) {
            return mapAretes.get(v).size();
        }
        return 0;
    }

    /**
     * @return le degré max, et Integer.Min_VALUE si le graphe est vide
     */
    public int degreMax() {
        int degreMax = Integer.MIN_VALUE;
        for (Integer v : mapAretes.keySet()) {
            degreMax = Math.max(degreMax, degre(v));
        }
        return degreMax;
    }

    public boolean estSimple() {
        for (Integer v : mapAretes.keySet()) {
            for (Arete a : mapAretes.get(v)) {
                if (a.i() == a.j()) {
                    return false;
                }
            }
            for (Arete a : mapAretes.get(v)) {
                if (mapAretes.get(v).contains(a)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @return true ssi pour tous sommets i,j de this avec (i!=j), alors this contient une arête {i,j}
     */
    public boolean estComplet() {
        for (int i = 0; i<nbSommets(); i++) {
            for (int j = 0; j<nbSommets(); j++) {
                if (i != j) {
                    boolean existe = false;
                    for (Arete a : mapAretes.get(i)) {
                        if (a.getAutreSommet(i) == j) {
                            existe = true;
                            break;
                        }
                    }
                    if (!existe) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * @return true ssi this est une chaîne. Attention, être une chaîne
     * implique en particulier que l'on a une seule arête (et pas plusieurs en parallèle) entre
     * les sommets successifs de la chaîne. On considère que le graphe vide est une chaîne.
     */
    public boolean estUneChaine() {
        int nbDegre = 0;
        for (Integer v : mapAretes.keySet()) {
            if (degre(v) == 1) {
                nbDegre++;
            }
            if (degre(v) > 2) {
                return false;
            }
        }
        return nbDegre == 2 || nbDegre == 0;
    }


    /**
     * @return true ssi this est un cycle. Attention, être un cycle implique
     * en particulier que l'on a une seule arête (et pas plusieurs en parallèle) entre
     * les sommets successifs du cycle.
     * On considère que dans le cas où G n'a que 2 sommets {i,j}, et 2 arêtes parallèles {i,j}, alors G n'est PAS un cycle.
     * On considère que le graphe vide est un cycle.
     */
    public boolean estUnCycle() {
        if (mapAretes.size() == 0) {
            return true;
        }
        if (mapAretes.size() == 1) {
            return mapAretes.get(0).size() == 1;
        }
        if (mapAretes.size() == 2) {
            return mapAretes.get(0).size() == 1 && mapAretes.get(1).size() == 1;
        }
        for (Integer v : mapAretes.keySet()) {
            if (degre(v) != 2) {
                return false;
            }
        }
        return true;
    }


    // une foret est un grahe si chaque composant connexe est un arbre
    public boolean estUneForet() {
        for (Set<Integer> classeConnexite : getEnsembleClassesConnexite()) {
            if (!estUnArbre(classeConnexite)) {
                return false;
            }
        }
        return true;
    }

    private boolean estUnArbre(Set<Integer> classeConnexite) {
        if (classeConnexite.size() == 0) {
            return true;
        }
        if (classeConnexite.size() == 1) {
            return true;
        }
        if (classeConnexite.size() == 2) {
            return mapAretes.get(classeConnexite.iterator().next()).size() == 1;
        }
        for (Integer v : classeConnexite) {
            if (degre(v) != 1 && degre(v) != 2) {
                return false;
            }
        }
        return true;
    }

    public Set<Integer> getClasseConnexite(int v) {
        Set<Integer> visited = new HashSet<>();
        Set<Integer> component = new HashSet<>();
        dfs(v, visited, component);
        return component;
    }


    private void dfs(int node, Set<Integer> visited, Set<Integer> component) {
        visited.add(node);
        component.add(node);
        for (Arete a : mapAretes.get(node)) {
            int other = a.getAutreSommet(node);
            if (!visited.contains(other)) dfs(other, visited, component);
        }
    }

    public Set<Set<Integer>> getEnsembleClassesConnexite() {
        Set<Set<Integer>> ensembleClassesConnexite = new HashSet<>();
        Set<Integer> visite = new HashSet<>();
        for (Integer v : mapAretes.keySet()) {
            if (!visite.contains(v)) {
                Set<Integer> component = new HashSet<>();
                dfs(v, visite, component);
                ensembleClassesConnexite.add(component);
            }
        }
        return ensembleClassesConnexite;
    }
    /**
     * @return true si et seulement si l'arête passée en paramètre est un isthme dans le graphe.
     */
    public boolean estUnIsthme(Arete a) {
        if (!existeArete(a)) return false;
        supprimerArete(a);
        boolean result = getEnsembleClassesConnexite().size() > 1;
        ajouterArete(a);
        return result;
    }

    private boolean contientArete(Arete a) {
        if (contientSommet(a.i()) && contientSommet(a.j())) {
            for (Arete arete : mapAretes.get(a.i())) {
                if (arete.equals(a)) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean sontAdjacents(int i, int j) {
        if (contientSommet(i) && contientSommet(j)) {
            for (Arete a : mapAretes.get(i)) {
                if (a.i() == j || a.j() == j) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Fusionne les deux sommets passés en paramètre.
     * Toutes les arêtes reliant i à j doivent être supprimées (pas de création de boucle).
     * L'entier correspondant au sommet nouvellement créé sera le min{i,j}. Le voisinage du nouveau sommet
     * est l'union des voisinages des deux sommets fusionnés.
     * Si un des sommets n'est pas présent dans le graphe, alors cette fonction ne fait rien.
     */
    public void fusionnerSommets(int i, int j) {

        if (contientSommet(i) && contientSommet(j)) {
            for (Arete a : mapAretes.get(i)) {
                if (a.i() == j || a.j() == j) {
                    supprimerArete(a);
                }
            }
            for (Arete a : mapAretes.get(j)) {
                if (a.i() == i || a.j() == i) {
                    supprimerArete(a);
                }
            }
            ajouterSommet(Math.min(i, j));
            for (Arete a : mapAretes.get(i)) {
                ajouterArete(new Arete(Math.min(i, j), a.i()));
                ajouterArete(new Arete(Math.min(i, j), a.j()));
            }
            for (Arete a : mapAretes.get(j)) {
                ajouterArete(new Arete(Math.min(i, j), a.i()));
                ajouterArete(new Arete(Math.min(i, j), a.j()));
            }
            supprimerSommet(i);
            supprimerSommet(j);
        }
    }

    /**
     * @return true si et seulement si la séquence d'entiers passée en paramètre correspond à un graphe simple valide.
     * La pondération des arêtes devrait être ignorée.
     */
    public static boolean sequenceEstGraphe(List<Integer> sequence) {
        int somme = 0;
        for (int i = 0; i < sequence.size(); i++) {
            somme += sequence.get(i);
        }
        if (somme % 2 != 0) {
            return false;
        }
        Collections.sort(sequence);
        Collections.reverse(sequence);
        for (int i = 0; i < sequence.size(); i++) {
            if (sequence.get(i) < 0 || sequence.get(i) >= sequence.size()) {
                return false;
            }
            int sommeDegres = 0;
            for (int j = 0; j <= i; j++) {
                sommeDegres += sequence.get(j);
            }
            int sommeMin = 0;
            for (int j = i + 1; j < sequence.size(); j++) {
                sommeMin += Math.min(i + 1, sequence.get(j));
            }
            if (sommeDegres > i * (i + 1) + sommeMin) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return true si et seulement si la séquence d'entiers passée en paramètre correspond à un graphe valide.
     * La pondération des arêtes devrait être ignorée.
     */

    /**
     * @return true si les deux graphes passés en paramètre sont isomorphes.
     * pré-requis : les deux graphes sont des graphes simples.
     */

    public static boolean sontIsomorphes(Graphe g1, Graphe g2) {
        if (g1.nbSommets() != g2.nbSommets() || g1.nbAretes() != g2.nbAretes()) return false;

        int min = Collections.min(g1.ensembleSommets());
        int max = Collections.max(g1.ensembleSommets());
        int min2 = Collections.min(g2.ensembleSommets());
        int max2 = Collections.max(g2.ensembleSommets());
        boolean[][] matrice1 = new boolean[g1.nbSommets()][g1.nbSommets()];
        boolean[][] matrice2 = new boolean[g1.nbSommets()][g1.nbSommets()];
        for (int i = min; i < max; i++){
            for (int j : g1.getVoisins(i)){
                matrice1[i-min][j-min] = true;
            }
        }
        for (int i = min2; i < max2; i++){
            for (int j : g2.getVoisins(i)){
                matrice2[i-min2][j-min2] = true;
            }
        }
        for (int i = 0; i < matrice1.length; i++){
            for (int j = 0; j < matrice1.length; j++){
                if (matrice1[i][j] != matrice2[i][j]) return false;
            }
        }
        return true;
    }


    /**
     * Retourne un plus court chemin entre 2 sommets.
     *
     * @param depart  le sommet de départ
     * @param arrivee le sommet d'arrivée
     * @param pondere true si les arêtes sont pondérées (pas les longueurs des routes correspondantes dans le jeu)
     *                false si toutes les arêtes ont un poids de 1 (utile lorsque les routes associées sont complètement omises)
     * @return une liste d'entiers correspondant aux sommets du chemin dans l'ordre : l'élément en position 0 de la liste
     * est le sommet de départ, et l'élément à la dernière position de la liste (taille de la liste - 1) est le somme d'arrivée.
     * Si le chemin n'existe pas, retourne une liste vide (initialisée avec 0 éléments).
     */

    public List<Integer> parcoursSansRepetition(int depart, int arrivee, boolean pondere) {
        if (contientSommet(depart) && contientSommet(arrivee)) {
            List<Integer> sommets = new ArrayList<>();
            List<Integer> distances = new ArrayList<>();
            List<Integer> predecesseurs = new ArrayList<>();
            for (int i = 0; i < this.nbSommets(); i++) {
                sommets.add(i);
                distances.add(Integer.MAX_VALUE);
                predecesseurs.add(-1);
            }
            distances.set(depart, 0);
            while (!sommets.isEmpty()) {
                int min = Integer.MAX_VALUE;
                int sommetMin = -1;
                for (int i = 0; i < sommets.size(); i++) {
                    if (distances.get(sommets.get(i)) < min) {
                        min = distances.get(sommets.get(i));
                        sommetMin = sommets.get(i);
                    }
                }
                sommets.remove(sommets.indexOf(sommetMin));
                for (Arete a : mapAretes.get(sommetMin)) {
                    int sommet = a.i() == sommetMin ? a.j() : a.i();
                    if (sommets.contains(sommet)) {
                        int distance = distances.get(sommetMin) + (pondere ? a.i() : 1);
                        if (distance < distances.get(sommet)) {
                            distances.set(sommet, distance);
                            predecesseurs.set(sommet, sommetMin);
                        }
                    }
                }
            }
            List<Integer> chemin = new ArrayList<>();
            int sommet = arrivee;
            while (sommet != depart) {
                chemin.add(sommet);
                sommet = predecesseurs.get(sommet);
            }
            chemin.add(depart);
            Collections.reverse(chemin);
            return chemin;
        }
        return null;
    }


    public Object getNbAretes() {
        return nbAretes;
    }

    public Object getNbSommets() {
        return nbSommets;
    }

    public List<Integer> getSequenceDegres() {
        return sequenceDegres;
    }

    /**
     * Retourne un chemin entre 2 sommets sans répétition de sommets et sans dépasser
     * le nombre de bateaux et wagons disponibles. Cette fonction supposera que `this` est
     * bien un graphe issu du jeu avec des vraies routes (les objets routes ne sont pas null).
     * Dans cette fonction la couleur des routes n'est pas à prendre en compte.
     *
     * @param depart    le sommet de départ
     * @param arrivee   le sommet d'arrivée
     * @param nbBateaux le nombre de bateaux disponibles
     * @param nbWagons  le nombre de wagons disponibles
     * @return une liste d'entiers correspondant aux sommets du chemin, où l'élément en position 0 de la liste
     * et le sommet de départ, et l'élément à la dernière position de la liste (taille de la liste - 1) est le somme d'arrivée.
     * Si le chemin n'existe pas, retourne une liste vide (initialisée avec 0 éléments).
     * Pré-requis le graphe `this` est un graphe avec des routes (les objets routes ne sont pas null).
     */
    public List<Integer> parcoursSansRepetition(int depart, int arrivee, int nbWagons, int nbBateaux) {
        if (contientSommet(depart) && contientSommet(arrivee)) {
            List<Integer> sommets = new ArrayList<>();
            List<Integer> distances = new ArrayList<>();
            List<Integer> predecesseurs = new ArrayList<>();
            for (int i = 0; i < this.nbSommets(); i++) {
                sommets.add(i);
                distances.add(Integer.MAX_VALUE);
                predecesseurs.add(-1);
            }
            distances.set(depart, 0);
            while (!sommets.isEmpty()) {
                int min = Integer.MAX_VALUE;
                int sommetMin = -1;
                for (int i = 0; i < sommets.size(); i++) {
                    if (distances.get(sommets.get(i)) < min) {
                        min = distances.get(sommets.get(i));
                        sommetMin = sommets.get(i);
                    }
                }
                sommets.remove(sommets.indexOf(sommetMin));
                for (Arete a : mapAretes.get(sommetMin)) {
                    int sommet = a.i() == sommetMin ? a.j() : a.i();
                    if (sommets.contains(sommet)) {
                        int distance = distances.get(sommetMin) + (pondere ? a.i() : 1);
                        if (distance < distances.get(sommet)) {
                            distances.set(sommet, distance);
                            predecesseurs.set(sommet, sommetMin);
                        }
                    }
                }
            }
            List<Integer> chemin = new ArrayList<>();
            int sommet = arrivee;
            while (sommet != depart) {
                chemin.add(sommet);
                sommet = predecesseurs.get(sommet);
            }
            chemin.add(depart);
            Collections.reverse(chemin);
            return chemin;
        }
        return null;
    }

    /**
     * Retourne un chemin passant une et une seule fois par tous les sommets d'une liste donnée.
     * Les éléments de la liste en paramètres doivent apparaître dans le même ordre dans la liste de sortie.
     *
     * @param listeSommets la liste de sommets à visiter sans répétition ;
     *                     pré-requis : c'est une sous-liste de la liste retournée
     * @return une liste d'entiers correspondant aux sommets du chemin.
     * Si le chemin n'existe pas, retourne une liste vide.
     */
    public List<Integer> parcoursSansRepetition(List<Integer> listeSommets) {
        List<Integer> chemin = new ArrayList<>();
        if (listeSommets.size() == 0) {
            return chemin;
        }
        int sommet = listeSommets.get(0);
        chemin.add(sommet);

        for (int i = 1; i < listeSommets.size(); i++) {
            List<Integer> cheminTemp = parcoursSansRepetition(sommet, listeSommets.get(i), 0, 0);
            if (cheminTemp.size() == 0) {
                return cheminTemp;
            }
            chemin.addAll(cheminTemp.subList(1, cheminTemp.size()));
            sommet = listeSommets.get(i);
        }
        return chemin;
    }

    /**
     * Retourne un plus petit ensemble bloquant de routes entre deux villes. Cette fonction supposera que `this` est
     * bien un graphe issu du jeu avec des vraies routes (les objets routes ne sont pas null).
     * Dans cette fonction la couleur des routes n'est pas à prendre en compte.
     *
     * @return un ensemble de route.
     * Remarque : l'ensemble retourné doit être le plus petit en nombre de routes (et PAS en somme de leurs longueurs).
     * Remarque : il se peut qu'il y ait plusieurs ensemble de cardinalité minimum.
     * Un seul est à retourner (au choix).
     */
    public Set<Route> ensembleBloquant(int ville1, int ville2) {
        Set<Route> routes = new HashSet<>();
        List<Integer> sommets = new ArrayList<>();
        List<Integer> distances = new ArrayList<>();
        List<Integer> predecesseurs = new ArrayList<>();

        for (int i = 0; i < this.nbSommets(); i++) {
            sommets.add(i);
            distances.add(Integer.MAX_VALUE);
            predecesseurs.add(-1);
        }
        distances.set(ville1, 0);
        while (!sommets.isEmpty()) {
            int min = Integer.MAX_VALUE;
            int sommetMin = -1;
            for (int i = 0; i < sommets.size(); i++) {
                int sommetCourant = sommets.get(i);
                if (distances.get(sommetCourant) < min) {
                    min = distances.get(sommetCourant);
                    sommetMin = sommetCourant;
                }
            }
            sommets.remove(Integer.valueOf(sommetMin));
            for (Arete a : mapAretes.get(sommetMin)) {
                int sommet = a.getAutreSommet(sommetMin);
                if (sommets.contains(sommet)) {
                    int distance = distances.get(sommetMin) + (pondere ? a.i() : 1);
                    if (distance < distances.get(sommet)) {
                        distances.set(sommet, distance);
                        predecesseurs.set(sommet, sommetMin);
                    }
                }
            }
        }
        int sommet = ville2;
        while (sommet != ville1) {
            for (Arete a : mapAretes.get(sommet)) {
                int sommet2 = a.getAutreSommet(sommet);
                int poidsArete = pondere ? a.i() : 1;
                if (distances.get(sommet2) == distances.get(sommet) - poidsArete) {
                    routes.add(a.route());
                    sommet = sommet2;
                    break;
                }
            }
        }
        return routes;
    }
}