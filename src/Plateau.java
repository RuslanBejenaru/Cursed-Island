// ------------------------
// Classe Plateau : Représente un tableau 2D de Case et s'occupe
// de l'initialiser.
// ------------------------



import java.util.Random;


public class Plateau {
    private int longueur;
    private int largeur;
    private Random hasard = new Random();
    private Case [][] listCase;
    private Case [] specialCase;



    /*
     * Constructeur.
     * @param x et y le nombre de lignes et de colonnes.
     */

    public Plateau(int x, int y) {
        longueur = x;
        largeur = y;
        specialCase = new Case[5];

        listCase = new Case[x][y];
        for (int i = 0; i < x; i++){
            for (int j = 0; j < y; j++)
                listCase [i][j] = new Case(false, 2, i, j);
        }
        return;
    }

    /*
     * Méthode permettant d'initialiser le plateau (affecter des spécificités aux cases).
     * @return la case de l'héliport.
     */
    public Case init(){
        int [] position = new int [10];

        int compteur = 0, x ,y;
        Case Aeroport = null;

        while (compteur < 5){
            do {
                x = hasard.nextInt(longueur);
                y = hasard.nextInt(largeur);
            }
            while (contient(x, y, position));
            if (compteur == 0){
                listCase[x][y] = new Case (true, 0,  x, y);
                Aeroport = listCase[x][y];
            }
            else
                listCase[x][y] = new Case(false, compteur,  x, y);
            position[compteur * 2] = x;
            position[compteur * 2 + 1] = y;

            specialCase[compteur] = listCase[x][y];
            compteur++;
        }
        return Aeroport;
    }

    protected boolean contient (int x, int y, int [] position) {
        for (int i = 1; i < position.length; i+=2){
            if (position[i - 1] == x && position[i] == y)
                return true;
        }
        return false;
    }

    protected boolean niveauMarin (int niveauAct) {
        for (Case[] n : listCase){
            for (Case c : n)
                if (c.getNivMarin() == niveauAct);
        }
        return false;

    }

    public Case getCase(int x, int y){
        if (x < 0 || x >= longueur || y < 0 || y >= largeur)
            return new Case(false, 0, -1, -1);
        return listCase[x][y];
    }



    public int getLongueur() {
        return longueur;
    }

    public int getLargeur() {
        return largeur;
    }

    public Case[][] getListCase() {
        return listCase;
    }

    public Case getSpecialCase(int position) {
        if (position < 0 || position > 4) return new Case(false, 0, -1, -1);
        return specialCase[position];
    }
}