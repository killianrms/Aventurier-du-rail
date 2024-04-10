package fr.umontpellier.iut.graphes;


import fr.umontpellier.iut.rails.Joueur;
import fr.umontpellier.iut.rails.Route;
import fr.umontpellier.iut.rails.RouteMaritime;
import fr.umontpellier.iut.rails.RouteTerrestre;
import fr.umontpellier.iut.rails.data.Couleur;
import fr.umontpellier.iut.rails.data.Ville;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.security.PrivilegedAction;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GrapheTest {
    private Graphe  graphe;

    @BeforeEach
    void setUp() {
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(0, 1));
        aretes.add(new Arete(0, 3));
        aretes.add(new Arete(1, 2));
        aretes.add(new Arete(2, 3));
        aretes.add(new Arete(8, 42));
        graphe = new Graphe(aretes);
    }

    @Test
    void testNbAretes() {
        assertEquals(5, graphe.nbAretes());
    }


    @Test
    void testContientSommet() {
        assertTrue(graphe.contientSommet(0));
        assertTrue(graphe.contientSommet(1));
        assertTrue(graphe.contientSommet(2));
        assertTrue(graphe.contientSommet(3));
        assertTrue(graphe.contientSommet(8));
        assertTrue(graphe.contientSommet(42));
        assertFalse(graphe.contientSommet(7));
    }

    @Test
    void testAjouterSommet() {
        int nbSommets = graphe.nbSommets();
        graphe.ajouterSommet(59);
        assertTrue(graphe.contientSommet(59));
        assertEquals(nbSommets + 1, graphe.nbSommets());
        graphe.ajouterSommet(59);
        assertEquals(nbSommets + 1, graphe.nbSommets());
    }

    @Test
    void testAjouterArete() {
        int nbAretes = graphe.nbAretes();
        graphe.ajouterArete(new Arete(0, 3));
        assertEquals(nbAretes, graphe.nbAretes());
        graphe.ajouterArete(new Arete(9, 439, null));
        assertEquals(nbAretes + 1, graphe.nbAretes());
        graphe.ajouterArete(new Arete(0, 3, new RouteMaritime(new Ville("Athina", true), new Ville("Marseille", true), Couleur.ROUGE, 2) {
        }));
        assertEquals(nbAretes + 2, graphe.nbAretes());
    }

    @Test
    void testSupprimerArete() {
        int nbAretes = graphe.nbAretes();
        graphe.supprimerArete(new Arete(0, 3));
        assertEquals(nbAretes - 1, graphe.nbAretes());
        graphe.supprimerArete(new Arete(0, 3));
        assertEquals(nbAretes - 1, graphe.nbAretes());
        graphe.supprimerArete(new Arete(0, 3, null));
        assertEquals(nbAretes - 1, graphe.nbAretes());
    }

    @Test
    void testSupprimerSommet() {
        int nbSommets = graphe.nbSommets();
        int nbAretes = graphe.nbAretes();
        graphe.supprimerSommet(42);
        assertEquals(nbSommets - 1, graphe.nbSommets());
        assertEquals(nbAretes - 1, graphe.nbAretes());
        graphe.supprimerSommet(2);
        assertEquals(nbSommets - 2, graphe.nbSommets());
        assertEquals(nbAretes - 3, graphe.nbAretes());
    }

    @Test
    void testExisteArete() {
        assertTrue(graphe.existeArete(new Arete(0, 1)));
        assertTrue(graphe.existeArete(new Arete(0, 3)));
        assertTrue(graphe.existeArete(new Arete(1, 2)));
        assertTrue(graphe.existeArete(new Arete(2, 3)));
        assertTrue(graphe.existeArete(new Arete(8, 42)));
        assertFalse(graphe.existeArete(new Arete(0, 2)));
        assertFalse(graphe.existeArete(new Arete(0, 4)));
        assertFalse(graphe.existeArete(new Arete(1, 3)));
        assertFalse(graphe.existeArete(new Arete(2, 4)));
        assertFalse(graphe.existeArete(new Arete(8, 43)));
    }

    @Test
    void testGetVoisins() {
        Set<Integer> voisins0 = graphe.getVoisins(0);
        Set<Integer> voisins1 = graphe.getVoisins(1);
        Set<Integer> voisins2 = graphe.getVoisins(2);
        Set<Integer> voisins3 = graphe.getVoisins(3);
        Set<Integer> voisins8 = graphe.getVoisins(8);
        Set<Integer> voisins42 = graphe.getVoisins(42);
        assertEquals(2, voisins0.size());
        assertEquals(2, voisins1.size());
        assertEquals(2, voisins2.size());
        assertEquals(2, voisins3.size());
        assertEquals(1, voisins8.size());
        assertEquals(1, voisins42.size());
        assertTrue(voisins0.contains(1));
        assertTrue(voisins0.contains(3));
        assertTrue(voisins1.contains(0));
        assertTrue(voisins1.contains(2));
        assertTrue(voisins2.contains(1));
        assertTrue(voisins2.contains(3));
        assertTrue(voisins3.contains(0));
        assertTrue(voisins3.contains(2));
        assertTrue(voisins8.contains(42));
        assertTrue(voisins42.contains(8));
    }
    @Test
    void testEstSimple(){
        assertFalse(graphe.estSimple());
        graphe.ajouterArete(new Arete(3, 0));
        assertFalse(graphe.estSimple());
        graphe.supprimerArete(new Arete(3, 0));
        assertTrue(graphe.estSimple());
    }
    @Test
    void testEstComplet(){
        Graphe graphe1 = new Graphe();
        Graphe graphe2 = new Graphe();
        Graphe graphe3 = new Graphe();
        Graphe graphe4 = new Graphe();
        graphe4.ajouterArete(new Arete(0,1));
        graphe4.ajouterArete(new Arete(1,2));
        graphe4.ajouterArete(new Arete(2,3));
        graphe4.ajouterArete(new Arete(3,0, new RouteMaritime(new Ville("Athina", true), new Ville("Marseille", true), Couleur.ROUGE, 2) {
        }));
        graphe4.ajouterArete(new Arete(3,0, new RouteMaritime(new Ville("Athina", true), new Ville("Marseille", true), Couleur.VERT, 2) {
        }));
        graphe4.ajouterArete(new Arete(3,1));
        assertEquals(false, graphe4.estComplet());
        graphe1.ajouterArete(new Arete(0,1, new RouteMaritime(new Ville("Athina", true), new Ville("Marseille", true), Couleur.ROUGE, 2) {
        }));
        graphe1.ajouterArete(new Arete(0,1, new RouteMaritime(new Ville("Athina", true), new Ville("Marseille", true), Couleur.VERT, 2) {
        }));
        graphe1.ajouterArete(new Arete(1,2));
        graphe1.ajouterArete(new Arete(2,0));
        assertEquals(true, graphe1.estComplet());
        graphe2.ajouterArete(new Arete(0,1));
        graphe2.ajouterArete(new Arete(1,2));
        graphe2.ajouterArete(new Arete(2,0));
        assertEquals(true, graphe2.estComplet());
        graphe3.ajouterArete(new Arete(0,1));
        graphe3.ajouterArete(new Arete(1,2));
        assertEquals(false, graphe3.estComplet());
    }

    @Test
    void testEstUneChaine(){
        assertFalse(graphe.estUneChaine());
        graphe.supprimerSommet(8);
        graphe.supprimerSommet(42);
        graphe.supprimerSommet(3);
        graphe.supprimerSommet(1);
        graphe.supprimerSommet(2);
        graphe.supprimerSommet(0);
        graphe.ajouterSommet(0);
        graphe.ajouterSommet(1);
        graphe.ajouterSommet(2);
        graphe.ajouterSommet(3);

        graphe.ajouterArete(new Arete(0,1));
        graphe.ajouterArete(new Arete(1,2));
        graphe.ajouterArete(new Arete(2,3));

        assertTrue(graphe.estUneChaine());

        graphe.ajouterArete(new Arete(3,0));
        assertFalse(graphe.estUneChaine());
    }

    @Test
    void testEstUnCycle(){
        assertFalse(graphe.estUnCycle());
        graphe.supprimerSommet(8);
        graphe.supprimerSommet(42);
        graphe.supprimerSommet(3);
        graphe.supprimerSommet(1);
        graphe.supprimerSommet(2);
        graphe.supprimerSommet(0);
        graphe.ajouterSommet(0);
        graphe.ajouterSommet(1);
        graphe.ajouterSommet(2);
        graphe.ajouterSommet(3);

        graphe.ajouterArete(new Arete(0,1));
        graphe.ajouterArete(new Arete(1,2));
        graphe.ajouterArete(new Arete(2,3));
        graphe.ajouterArete(new Arete(3,0));

        assertTrue(graphe.estUnCycle());

    }
    @Test
    void testClasseConnexité(){
        Set<Integer> CC=graphe.getClasseConnexite(0);
        assertTrue(CC.containsAll(Arrays.asList(0,1,2,3)));
        assertTrue(CC.size()==4);
        Set<Integer> CC2=graphe.getClasseConnexite(8);
        assertTrue(CC2.containsAll(Arrays.asList(8,42)));
        assertTrue(CC2.size()==2);
    }
    @Test
    void getEnsembleClassesConnexite() {
        Graphe graphe = new Graphe();
        graphe.ajouterSommet(0);
        graphe.ajouterSommet(1);
        Set<Set<Integer>> ensembleCC=graphe.getEnsembleClassesConnexite();
        assertEquals(2, ensembleCC.size());
        Set<Integer> CC=graphe.getClasseConnexite(0);
        Set<Integer> CC2=graphe.getClasseConnexite(1);
        assertTrue(ensembleCC.contains(CC));
        assertTrue(ensembleCC.contains(CC2));
        graphe.ajouterArete(new Arete(0,1));
        assertEquals(1, graphe.getEnsembleClassesConnexite().size());
        graphe.ajouterSommet(2);
        graphe.ajouterSommet(3);
        assertEquals(3, graphe.getEnsembleClassesConnexite().size());
        Set<Integer> CC3=graphe.getClasseConnexite(2);
        ensembleCC=graphe.getEnsembleClassesConnexite();
        assertTrue(ensembleCC.contains(CC3));
    }
    @Test
    void testSéquenceEstGraphe(){
        List<Integer> seq=Arrays.asList(1,1,1,1,2,2,2,4,4);
        assertTrue(Graphe.sequenceEstGraphe(seq));
        List<Integer> seq2=Arrays.asList(1,2,3,4,5);
        assertFalse(Graphe.sequenceEstGraphe(seq2));
        List<Integer> seq3=Arrays.asList(0,1,2,3,4);
        assertFalse(Graphe.sequenceEstGraphe(seq3));
        List<Integer> seq4=Arrays.asList(0,1,2,2,3,4);
        assertTrue(Graphe.sequenceEstGraphe(seq4));
        List<Integer> seq5=Arrays.asList(0,1,2,2,2,2,3,4,5);
        assertFalse(Graphe.sequenceEstGraphe(seq5));
        List<Integer> seq6=Arrays.asList(0,1,2,2,2,2,3,4,5,5);
        assertTrue(Graphe.sequenceEstGraphe(seq6));
        List<Integer> seq7=Arrays.asList(1,1,2,4,4);
        assertFalse(Graphe.sequenceEstGraphe(seq7));
        List<Integer> seq8=Arrays.asList(1,1,2,3,4,5);
        assertFalse(Graphe.sequenceEstGraphe(seq8));
    }
    @Test
    void testEstUnIsmthe(){
        Graphe graphe=new Graphe();
        graphe.ajouterArete(new Arete(0,1));
        assertTrue(graphe.estUnIsthme(new Arete(0,1)));
        graphe.ajouterArete(new Arete(1,2));
        graphe.ajouterArete(new Arete(2,1));
        assertFalse(graphe.estUnIsthme(new Arete(1,2)));
        graphe.ajouterArete(new Arete(2,3));
        graphe.ajouterArete(new Arete(3,0));
        assertFalse(graphe.estUnIsthme(new Arete(3,0)));
    }

    @Test
    void testSontIsomorphes() {
        Graphe grapheG = new Graphe();
        Graphe grapheH = new Graphe();
        grapheH.ajouterSommet(0);
        grapheH.ajouterSommet(1);
        grapheH.ajouterSommet(2);
        grapheH.ajouterSommet(3);
        grapheH.ajouterArete(new Arete(0,1));
        grapheH.ajouterArete(new Arete(1,2));
        grapheH.ajouterArete(new Arete(2,3));
        grapheH.ajouterArete(new Arete(3,0));
        grapheG.ajouterSommet(1);
        grapheG.ajouterSommet(0);
        grapheG.ajouterSommet(3);
        grapheG.ajouterSommet(2);
        grapheG.ajouterArete(new Arete(1,0));
        grapheG.ajouterArete(new Arete(0,3));
        grapheG.ajouterArete(new Arete(3,2));
        grapheG.ajouterArete(new Arete(2,1));
        assertTrue(grapheG.sontIsomorphes(grapheH, grapheG));
    }

    @Test
    void SontIsomorphes2() {
        Graphe grapheG = new Graphe();
        Graphe grapheH = new Graphe();
        grapheH.ajouterSommet(0);
        grapheH.ajouterSommet(1);
        grapheH.ajouterSommet(2);
        grapheH.ajouterSommet(3);
        grapheH.ajouterArete(new Arete(0,1));
        grapheH.ajouterArete(new Arete(1,2));
        grapheH.ajouterArete(new Arete(2,3));
        grapheH.ajouterArete(new Arete(3,0));
        grapheG.ajouterSommet(0);
        grapheG.ajouterSommet(1);
        grapheG.ajouterSommet(2);
        grapheG.ajouterSommet(3);
        grapheG.ajouterArete(new Arete(0,1));
        grapheG.ajouterArete(new Arete(1,2));
        grapheG.ajouterArete(new Arete(2,3));
        assertFalse(grapheG.sontIsomorphes(grapheH, grapheG));
    }

    @Test
    void testSontIsomorphes3() {
        Graphe g1 = new Graphe(3);
        g1.ajouterSommet(0);
        g1.ajouterSommet(1);
        g1.ajouterArete(new Arete(0, 1));
        g1.ajouterArete(new Arete(1, 2));

        Graphe g2 = new Graphe(3);
        g2.ajouterSommet(0);
        g2.ajouterSommet(1);
        g2.ajouterArete(new Arete(0, 1));
        g2.ajouterArete(new Arete(1, 2));

        assertTrue(Graphe.sontIsomorphes(g1, g2));

        Graphe g3 = new Graphe(3);
        g3.ajouterSommet(0);
        g3.ajouterSommet(1);
        g3.ajouterArete(new Arete(0, 1));
        g3.ajouterArete(new Arete(1, 2));

        Graphe g4 = new Graphe(4);
        g4.ajouterSommet(0);
        g4.ajouterSommet(1);
        g4.ajouterArete(new Arete(0, 1));
        g4.ajouterArete(new Arete(2, 3));

        assertFalse(Graphe.sontIsomorphes(g3, g4));
    }

    @Test
    void testSontIsomorphes5(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(1, 2));
        aretes.add(new Arete(1, 3));
        aretes.add(new Arete(2, 3));
        aretes.add(new Arete(2, 4));
        aretes.add(new Arete(3, 5));
        aretes.add(new Arete(4, 5));


        List<Arete> aretes2 = new ArrayList<>();
        aretes2.add(new Arete(6, 7));
        aretes2.add(new Arete(6, 8));
        aretes2.add(new Arete(7, 8));
        aretes2.add(new Arete(7, 9));
        aretes2.add(new Arete(8, 10));
        aretes2.add(new Arete(9, 10));
        Graphe graphe2 = new Graphe(aretes);
        Graphe graphe3 = new Graphe(aretes2);
        assertTrue(Graphe.sontIsomorphes(graphe2, graphe3));
    }

    @Test
    void testSontIsomorphes4(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(1, 2));
        aretes.add(new Arete(2, 3));
        aretes.add(new Arete(3, 4));
        aretes.add(new Arete(3, 5));
        aretes.add(new Arete(5, 4));


        List<Arete> aretes2 = new ArrayList<>();
        aretes2.add(new Arete(1, 2));
        aretes2.add(new Arete(2, 3));
        aretes2.add(new Arete(3, 4));
        aretes2.add(new Arete(2, 5));
        aretes2.add(new Arete(5, 4));

        Graphe graphe2 = new Graphe(aretes);
        Graphe graphe3 = new Graphe(aretes2);
        assertFalse(Graphe.sontIsomorphes(graphe2, graphe3));
    }


    //test estUneChaine d'ordre 10
    @Test
    void Chaine10(){
        Graphe graphe=new Graphe();
        for (int i = 0; i < 9.; i++) {
            graphe.ajouterSommet(i);
            graphe.ajouterArete(new Arete(i,i+1));
        }
        assertTrue(graphe.estUneChaine());
    }

    //test estUnCycle d'ordre 10
    @Test
    void Cycle10(){
        Graphe graphe=new Graphe();
        for (int i = 0; i < 9.; i++) {
            graphe.ajouterSommet(i);
            graphe.ajouterArete(new Arete(i,i+1));
        }
        graphe.ajouterArete(new Arete(9,0));
        assertTrue(graphe.estUnCycle());
    }

    //test estUnGrapheComplet d'ordre 10
    @Test
    void GrapheComplet10(){
        Graphe graphe=new Graphe();
        for (int i = 0; i < 10.; i++) {
            graphe.ajouterSommet(i);
        }
        for (int i = 0; i < 10.; i++) {
            for (int j = 0; j < 10.; j++) {
                if(i!=j){
                    graphe.ajouterArete(new Arete(i,j));
                }
            }
        }
        assertTrue(graphe.estComplet());
    }

    //test estUnGrapheComplet d'ordre 20
    @Test
    void GrapheComplet20(){
        Graphe graphe=new Graphe();
        for (int i = 0; i < 20.; i++) {
            graphe.ajouterSommet(i);
        }
        for (int i = 0; i < 20.; i++) {
            for (int j = 0; j < 20.; j++) {
                if(i!=j){
                    graphe.ajouterArete(new Arete(i,j));
                }
            }
        }
        assertTrue(graphe.estComplet());
    }

    //test estUneChaine un arbre d'ordre 10 ayant au moins 4 feuilles
    @Test
    void Arbre10(){
    }

    //test estNonConnexe un graphe non connexe avec 4 sommets isolés et 2 composantes d'ordre minimum 3
    @Test
    void NonConnexe(){
    }

    @Test
    void testpourdijskra(){
        Graphe graphe = new Graphe();
        graphe.ajouterArete(new Arete(0, 1,  new RouteTerrestre(new Ville("Athina", true), new Ville("Marseille", true), Couleur.VERT, 2) {
        }));
        graphe.ajouterArete(new Arete(0,2,new RouteTerrestre(new Ville("Bob", true), new Ville("Lamp", true), Couleur.ROUGE, 1) {
        }));
        graphe.ajouterArete(new Arete(0,3, new RouteTerrestre(new Ville("dza", true), new Ville("zaad", true), Couleur.JAUNE, 2) {
        }));
        graphe.ajouterArete(new Arete(1,2,new RouteTerrestre(new Ville("azaz", true), new Ville("Mardzgaseille", true), Couleur.VERT, 2) {
        }));
        graphe.ajouterArete(new Arete(3,2,new RouteTerrestre(new Ville("ffez", true), new Ville("bgre", true), Couleur.VERT, 3) {
        }));
        graphe.ajouterArete(new Arete(2,4,new RouteTerrestre(new Ville("Aterbehina", true), new Ville("zfrv", true), Couleur.VERT, 1) {
        }));
        graphe.ajouterArete(new Arete(2,5,new RouteTerrestre(new Ville("gter", true), new Ville("berbe", true), Couleur.VERT, 3) {
        }));
        graphe.ajouterArete(new Arete(4,6,new RouteTerrestre(new Ville("ertre", true), new Ville("bterb", true), Couleur.VERT, 5) {
        }));
        graphe.ajouterArete(new Arete(4,7,new RouteTerrestre(new Ville("Ateggzrbehina", true), new Ville("zegz", true), Couleur.VERT, 2) {
        }));
        graphe.ajouterArete(new Arete(7,5,new RouteTerrestre(new Ville("gzg", true), new Ville("gtrzg", true), Couleur.VERT, 1) {
        }));
        graphe.ajouterArete(new Arete(7,8,new RouteTerrestre(new Ville("zaeaz", true), new Ville("fezaf", true), Couleur.VERT, 8) {
        }));
        graphe.ajouterArete(new Arete(5,6,new RouteTerrestre(new Ville("frzfrz", true), new Ville("frzbg", true), Couleur.VERT, 2) {
        }));
        graphe.ajouterArete(new Arete(5,8,new RouteTerrestre(new Ville("gezhye", true), new Ville("hterheeht", true), Couleur.VERT, 1) {
        }));
        List<Integer> resultat = new ArrayList<>();
        resultat.add(0);
        resultat.add(2);
        resultat.add(5);
        resultat.add(8);
        assertEquals(resultat,graphe.parcoursSansRepetition(0,8,true));
    }

    @Test
    public void dijkstraListe(){
        Graphe graphe = new Graphe();
        graphe.ajouterArete(new Arete(0, 1,  new RouteTerrestre(new Ville("Athina", true), new Ville("Marseille", true), Couleur.VERT, 2) {
        }));
        graphe.ajouterArete(new Arete(0,2,new RouteTerrestre(new Ville("Bob", true), new Ville("Lamp", true), Couleur.ROUGE, 1) {
        }));
        graphe.ajouterArete(new Arete(0,3, new RouteTerrestre(new Ville("dza", true), new Ville("zaad", true), Couleur.JAUNE, 2) {
        }));
        graphe.ajouterArete(new Arete(1,2,new RouteTerrestre(new Ville("azaz", true), new Ville("Mardzgaseille", true), Couleur.VERT, 2) {
        }));
        graphe.ajouterArete(new Arete(3,2,new RouteTerrestre(new Ville("ffez", true), new Ville("bgre", true), Couleur.VERT, 3) {
        }));
        graphe.ajouterArete(new Arete(2,4,new RouteTerrestre(new Ville("Aterbehina", true), new Ville("zfrv", true), Couleur.VERT, 1) {
        }));
        graphe.ajouterArete(new Arete(2,5,new RouteTerrestre(new Ville("gter", true), new Ville("berbe", true), Couleur.VERT, 3) {
        }));
        graphe.ajouterArete(new Arete(4,6,new RouteTerrestre(new Ville("ertre", true), new Ville("bterb", true), Couleur.VERT, 5) {
        }));
        graphe.ajouterArete(new Arete(4,7,new RouteTerrestre(new Ville("Ateggzrbehina", true), new Ville("zegz", true), Couleur.VERT, 2) {
        }));
        graphe.ajouterArete(new Arete(7,5,new RouteTerrestre(new Ville("gzg", true), new Ville("gtrzg", true), Couleur.VERT, 1) {
        }));
        graphe.ajouterArete(new Arete(7,8,new RouteTerrestre(new Ville("zaeaz", true), new Ville("fezaf", true), Couleur.VERT, 8) {
        }));
        graphe.ajouterArete(new Arete(5,6,new RouteTerrestre(new Ville("frzfrz", true), new Ville("frzbg", true), Couleur.VERT, 2) {
        }));
        graphe.ajouterArete(new Arete(5,8,new RouteTerrestre(new Ville("gezhye", true), new Ville("hterheeht", true), Couleur.VERT, 1) {
        }));
        List<Integer> resultat = new ArrayList<>();
        List<Integer> sommets = new ArrayList<>();
        sommets.add(0);
        sommets.add(7);
        sommets.add(8);
        resultat.add(0);
        resultat.add(2);
        resultat.add(4);
        resultat.add(7);
        resultat.add(8);
        assertEquals(resultat,graphe.parcoursSansRepetition(sommets));
    }

    @Test
    public void cheminbloquanttest(){
        Graphe graphe = new Graphe();
        graphe.ajouterArete(new Arete(0, 1,  new RouteTerrestre(new Ville("Athina", true), new Ville("Marseille", true), Couleur.VERT, 2) {
        }));
        Route route = new RouteTerrestre(new Ville("Bob", true), new Ville("Lamp", true), Couleur.ROUGE, 1) {
        };
        graphe.ajouterArete(new Arete(0,2,route));
        graphe.ajouterArete(new Arete(0,3, new RouteTerrestre(new Ville("dza", true), new Ville("zaad", true), Couleur.JAUNE, 2) {
        }));
        graphe.ajouterArete(new Arete(1,2,new RouteTerrestre(new Ville("azaz", true), new Ville("Mardzgaseille", true), Couleur.VERT, 2) {
        }));
        graphe.ajouterArete(new Arete(3,2,new RouteTerrestre(new Ville("ffez", true), new Ville("bgre", true), Couleur.VERT, 3) {
        }));
        Route route1 = new RouteTerrestre(new Ville("Aterbehina", true), new Ville("zfrv", true), Couleur.VERT, 1) {
        };
        graphe.ajouterArete(new Arete(2,4,route1));
        graphe.ajouterArete(new Arete(2,5,new RouteTerrestre(new Ville("gter", true), new Ville("berbe", true), Couleur.VERT, 3) {
        }));
        graphe.ajouterArete(new Arete(4,6,new RouteTerrestre(new Ville("ertre", true), new Ville("bterb", true), Couleur.VERT, 5) {
        }));
        Route route2 = new RouteTerrestre(new Ville("Ateggzrbehina", true), new Ville("zegz", true), Couleur.VERT, 2) {
        };
        graphe.ajouterArete(new Arete(4,7,route2));
        graphe.ajouterArete(new Arete(7,5,new RouteTerrestre(new Ville("gzg", true), new Ville("gtrzg", true), Couleur.VERT, 1) {
        }));
        graphe.ajouterArete(new Arete(7,8,new RouteTerrestre(new Ville("zaeaz", true), new Ville("fezaf", true), Couleur.VERT, 8) {
        }));
        graphe.ajouterArete(new Arete(5,6,new RouteTerrestre(new Ville("frzfrz", true), new Ville("frzbg", true), Couleur.VERT, 2) {
        }));
        Route ville1 = new RouteTerrestre(new Ville("gezhye", true), new Ville("hterheeht", true), Couleur.VERT, 1) {
        };
        graphe.ajouterArete(new Arete(5,8,ville1));
        Set<Route> resultat2 = new HashSet<>();
        resultat2.add(route);
        resultat2.add(route1);
        resultat2.add(route2);
        //assert equals resultat ou resultat2 avec le chemin bloquant
        assertEquals(resultat2,graphe.ensembleBloquant(0,7));
    }

    @Test
    void  ensembleBloquant(){

    }

    @Test
    public void testCheminMultiple() {
        Graphe g = new Graphe(6);
        g.ajouterArete(new Arete(0, 1));
        g.ajouterArete(new Arete(1, 2));
        g.ajouterArete(new Arete(1, 3));
        g.ajouterArete(new Arete(2, 5));
        g.ajouterArete(new Arete(3, 4));
        g.ajouterArete(new Arete(4, 5));

        List<Integer> sommets = Arrays.asList(1, 5, 4);
        assertEquals(Arrays.asList(1, 2, 5, 4), g.parcoursSansRepetition(sommets));
    }

    @Test
    public void testCheminDirect() {
        Graphe g = new Graphe(3);
        g.ajouterArete(new Arete(0, 1));
        g.ajouterArete(new Arete(1, 2));

        List<Integer> sommets = Arrays.asList(0, 2);
        assertEquals(Arrays.asList(0, 1, 2), g.parcoursSansRepetition(sommets));
    }

    @Test
    public void testUnSeulSommet() {
        Graphe g = new Graphe(1);
        List<Integer> sommets = Arrays.asList(0);
        assertEquals(Arrays.asList(0), g.parcoursSansRepetition(sommets));
    }

    private boolean collectionsDansLeMemeOrdre(Iterable<Integer> listeAttendue, Iterable<Integer> listeObtenue) {
        Iterator<Integer> it1 = listeAttendue.iterator();
        Iterator<Integer> it2 = listeObtenue.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            int elem1 = it1.next();
            int elem2 = it2.next();
            if (elem1 != elem2) {
                return false;
            }
        }
        return !it1.hasNext() && !it2.hasNext();
    }
    @Test
    void testparcoursSansRepetitionSousListe1(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(1, 2));
        aretes.add(new Arete(2, 3));
        aretes.add(new Arete(2, 5));

        Graphe graphe2 = new Graphe(aretes);

        List<Integer> sousListe = Arrays.asList(1, 3, 5);

        List<Integer> parcoursAttendu = Arrays.asList(1, 2, 3, 2, 5);

        List<Integer> resultat = graphe2.parcoursSansRepetition(sousListe);

        assertTrue(collectionsDansLeMemeOrdre(parcoursAttendu, resultat));
    }

    @Test
    void testparcoursSansRepetitionSousListe2(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(1, 2));
        aretes.add(new Arete(2, 3));
        aretes.add(new Arete(2, 5));
        aretes.add(new Arete(3, 6));
        aretes.add(new Arete(5, 6));
        aretes.add(new Arete(5, 7));

        Graphe graphe2 = new Graphe(aretes);

        List<Integer> sousListe = Arrays.asList(1, 3, 7);

        List<Integer> parcoursAttendu = Arrays.asList(1, 2, 3, 6, 5, 7);
        List<Integer> parcoursAttendu2 = Arrays.asList(1, 2, 3, 2, 5, 7);

        List<Integer> resultat = graphe2.parcoursSansRepetition(sousListe);

        assertTrue(collectionsDansLeMemeOrdre(parcoursAttendu, resultat) || collectionsDansLeMemeOrdre(parcoursAttendu2, resultat));
    }

    @Test
    void testparcoursSansRepetitionSousListe3(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(1, 2));
        aretes.add(new Arete(2, 3));
        aretes.add(new Arete(1, 4));
        aretes.add(new Arete(4, 3));

        Graphe graphe2 = new Graphe(aretes);

        List<Integer> sousListe = Arrays.asList(1, 3, 2);

        List<Integer> parcoursAttendu = Arrays.asList(1, 4, 3, 2);
        List<Integer> parcoursAttendu2 = Arrays.asList(1, 2, 3, 2);

        List<Integer> resultat = graphe2.parcoursSansRepetition(sousListe);

        assertTrue(collectionsDansLeMemeOrdre(parcoursAttendu, resultat) || collectionsDansLeMemeOrdre(parcoursAttendu2, resultat));
    }
    @Test
    void testparcoursSansRepetitionSousListe4(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(1, 2));
        aretes.add(new Arete(2, 3));
        aretes.add(new Arete(1, 4));
        aretes.add(new Arete(4, 3));

        Graphe graphe2 = new Graphe(aretes);

        List<Integer> sousListe = Arrays.asList(1, 2, 3);

        List<Integer> parcoursAttendu = Arrays.asList(1, 2, 3);

        List<Integer> resultat = graphe2.parcoursSansRepetition(sousListe);

        assertTrue(collectionsDansLeMemeOrdre(parcoursAttendu, resultat));
    }

    @Test
    void testparcoursSansRepetitionSousListe5(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(1, 2));
        aretes.add(new Arete(2, 3));

        Graphe graphe2 = new Graphe(aretes);

        List<Integer> sousListe = Arrays.asList(1, 3, 2);

        List<Integer> parcoursAttendu = Arrays.asList(1,2,3,2);

        List<Integer> resultat = graphe2.parcoursSansRepetition(sousListe);

        assertTrue(collectionsDansLeMemeOrdre(parcoursAttendu, resultat));
    }
    @Test
    void testEstUneChaine2(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(0, 1));
        aretes.add(new Arete(1, 3));
        aretes.add(new Arete(1, 4));
        Graphe graphe2 = new Graphe(aretes);
        assertFalse(graphe2.estUneChaine());
    }

    @Test
    void testParcoursSansRepPions(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(1, 2,  new RouteMaritime(new Ville("Athina", true), new Ville("Marseille", true), Couleur.VERT, 1) {
        }));
        aretes.add(new Arete(1, 3,  new RouteMaritime(new Ville("Athina", true), new Ville("Paris", true), Couleur.VERT, 3) {
        }));
        aretes.add(new Arete(1, 4,  new RouteTerrestre(new Ville("Athina", true), new Ville("Montpellier", true), Couleur.VERT, 1) {
        }));
        aretes.add(new Arete(2, 3,  new RouteMaritime(new Ville("Marseille", true), new Ville("Paris", true), Couleur.VERT, 1) {
        }));
        aretes.add(new Arete(3, 7,  new RouteTerrestre(new Ville("Marseille", true), new Ville("Egypte", true), Couleur.VERT, 1) {
        }));
        aretes.add(new Arete(4, 5,  new RouteTerrestre(new Ville("Montpellier", true), new Ville("Canada", true), Couleur.VERT, 2) {
        }));
        aretes.add(new Arete(5, 7,  new RouteTerrestre(new Ville("Canada", true), new Ville("Egypte", true), Couleur.VERT, 3) {
        }));
        aretes.add(new Arete(5, 6,  new RouteTerrestre(new Ville("Canada", true), new Ville("Arabie", true), Couleur.VERT, 2) {
        }));
        aretes.add(new Arete(7, 6,  new RouteTerrestre(new Ville("Egypte", true), new Ville("Arabie", true), Couleur.VERT, 1) {
        }));

        Graphe graphe2 = new Graphe(aretes);
        int nbPionsBateaux = 2;
        int nbPionsWagons = 2;
        List<Integer> resultat1 = Arrays.asList(1,2,3,7,6);
        assertEquals(resultat1, graphe2.parcoursSansRepetition(1,6,nbPionsWagons,nbPionsBateaux ));
        nbPionsBateaux=0;
        nbPionsWagons=5;
        List<Integer> resultat2 = Arrays.asList(1,4,5,6);
        assertEquals(resultat2, graphe2.parcoursSansRepetition(1,6,nbPionsWagons, nbPionsBateaux));
    }

}