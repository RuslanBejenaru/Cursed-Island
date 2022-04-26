import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class Affichage extends JPanel {

    private static class Objet{
        protected int x, y;
        protected String texte;
        protected Color couleur;
        public Objet(int x, int y, Color couleur){
            this.x = x;
            this.y = y;
            this.texte = "";
            this.couleur = couleur;
        }
    }


    private Objet[][] boxs;
    private Objet[] pawns;

    private Plateau plateau;
    private Player[] players;

    private boolean afficheAide;

    private Player currentPlayer;

    // Differantes couleurs pour la grille.
    private Color[] CNORMAL = {Color.white, Color.gray, Color.black};
    private Color[] CHELI = {new Color(255, 0, 0), new Color(77, 0, 0), Color.black};
    private Color[] CAIR = {new Color(0, 201, 255), new Color(0, 66, 84), Color.black};
    private Color[] CEAU = {new Color(79, 79, 255), new Color(0, 0, 128), Color.black};
    private Color[] CTERRE = {new Color(44, 231, 0), new Color(23, 95, 0), Color.black};
    private Color[] CFEU = {new Color(255, 114, 0), new Color(99, 49, 0), Color.black};
    private Color[] CJOUEUR = {Color.red, Color.blue, Color.cyan, Color.green, Color.magenta,
            Color.orange, Color.darkGray, Color.pink, Color.yellow};

public Affichage(Plateau plateau, Player[] p){
    this.plateau = plateau;
    boxs = new Objet[this.plateau.getLongueur()][this.plateau.getLargeur()];
    players = p;
    pawns = new Objet[p.length];

    afficheAide = true;
}


public void intialize(){
    for(int i = 0; i < plateau.getLongueur(); i++){
        for (int j = 0; j < plateau.getLargeur(); j++)
            boxs[i][j] = new Objet(i, j, CNORMAL[0]);
    }

    for (int i = 0; i < pawns.length; i++)
        pawns[i] = new Objet(players[i].getPosx(), players[i].getPosy(), CJOUEUR[i]);

    createWindow();
    update();
    nameBoxes(false);

}

    public void nameBoxes(boolean posBox){
    if(posBox){
        for (int i = 0; i < boxs.length; i++) {
            for (int j = 0; j < boxs[i].length; j++)
                boxs[i][j].texte = Integer.toString(j * plateau.getLongueur() + i);
        }
        } else {
            if (boxs[0][0].texte.equals("0")){
                for (Objet[] box : boxs) {
                    for (Objet objet : box) objet.texte = "";
                }
            }

            boxs[plateau.getSpecialCase(0).getCoordX()][plateau.getSpecialCase(0).getCoordY()].texte = "Héliport";
            boxs[plateau.getSpecialCase(1).getCoordX()][plateau.getSpecialCase(1).getCoordY()].texte = "Air";
            boxs[plateau.getSpecialCase(2).getCoordX()][plateau.getSpecialCase(2).getCoordY()].texte = "Eau";
            boxs[plateau.getSpecialCase(3).getCoordX()][plateau.getSpecialCase(3).getCoordY()].texte = "Terre";
            boxs[plateau.getSpecialCase(4).getCoordX()][plateau.getSpecialCase(4).getCoordY()].texte = "Feu";


    }

    }


private void createWindow(){
    JFrame window = new JFrame("Cursed-Island");

    JButton boutonDroite = new JButton(">");
    this.add(boutonDroite);

    JButton boutonGauche = new JButton("<");
    this.add(boutonGauche);

    JButton boutonHaut = new JButton("^");
    this.add(boutonHaut);

    JButton boutonBas = new JButton("↧");
    this.add(boutonBas);

    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    window.getContentPane().add(this);

    window.setPreferredSize(new Dimension(600, 1200));


    window.pack();

    window.setVisible(true);


    return;
    }

    public void paintComponent(Graphics g){
    super.paintComponent(g);
    if (afficheAide)
        drawNotice(g);
    else {
        int[] margin = new int[2];

        int sizeBox = calculMargin(margin, getHeight() / 10);

        drawBoard (g, margin[1], margin[0], getWidth()- margin[1], getHeight() - margin[0]);

        drawBoxs(g, sizeBox, margin[0], margin[1]);

        drawPlayers(g, sizeBox, margin[0], margin[1]);

        drawInfo(g,sizeBox);
    }
    return;
    }

    private int calculMargin(int[] tab, int marginMin){

        // Calcul permettant d'inclure la taille des cases en plus de la hauteur (dans le cas ou la grille représenterai un rectangle).
        int tailleCase = (plateau.getLongueur() - plateau.getLargeur()) * (getHeight() - marginMin*2) / plateau.getLargeur();

        // Si on a une fenètre "en mode paysage" (en prenant en compte le "mode" de la grille).
        if(getWidth() > getHeight() + tailleCase){

            // On prend la hauteur de la fenètre, on enleve les deux marges donc on aura la hauteur que doit faire
            // la grille. Après, suffit de diviser par le nombre de lignes pour avoir la taille d'une case (que
            // l'on veux carré).
            tailleCase = (getHeight() - marginMin*2) / plateau.getLargeur();

            // Les marges Haute et Basse prennent la marge minimal
            tab[0] = marginMin;

            // On prend la largeur, on enleve la largeur de la grille donc il nous reste les deux marges,
            // on divise par deux pour avoir la marge GaucheDroite.
            tab[1] = (getWidth() - (tailleCase * plateau.getLongueur())) / 2;
        }

        // Si on a une fenètre "en mode portrait".
        else{
            tailleCase = (getWidth() - marginMin*2) / plateau.getLongueur();
            tab[0] = (getHeight() - (tailleCase * plateau.getLargeur())) / 2;
            tab[1] = marginMin;
        }
        return tailleCase;
    }


    private void drawBoard(Graphics g, int a, int b, int c, int d) {
        for (int i = 0; i < plateau.getLongueur() + 1; i++) {
            int atemp = a + ((c - a) / (plateau.getLongueur())) * i;
            int ctemp = a + ((c - a) / (plateau.getLargeur())) * i;

            int dtemp = b + ((d - b) / (plateau.getLargeur())) * (plateau.getLargeur());
            g.drawLine(atemp, b, ctemp, dtemp);
        }
        for (int i = 0; i < plateau.getLargeur() + 1; i++) {
            int btemp = b + ((d - b) / (plateau.getLargeur())) * i;
            int ctemp = a + ((c - a) / (plateau.getLongueur())) * (plateau.getLongueur());

            int dtemp = b + ((d - b) / (plateau.getLargeur())) * i;
            g.drawLine(a, btemp, ctemp, dtemp);
        }
    }
    private void drawBoxs(Graphics g, int sizeBox, int marginUpDown, int marginLeftRight){
    Font fontBox = new Font("Arial", Font.BOLD, sizeBox/5);

    int sizeText;

        // Pour toutes les cases, on defini les couleurs et les positions.
        for(Objet[] tf : boxs){
            for(Objet f : tf) {
                g.setColor(f.couleur);
                g.fillRect(f.x * sizeBox + marginLeftRight + 1, f.y * sizeBox + marginUpDown + 1, sizeBox - 1, sizeBox - 1);

                // Si la case doit avoir un texte.
                if(!Objects.equals(f.texte, "")) {

                    // On met le texte en noir.
                    g.setColor(Color.black);

                    // On defini la police.
                    g.setFont(fontBox);

                    // On calcul la taille du texte (pour le mettre au milieu).
                    sizeText = g.getFontMetrics().stringWidth(f.texte);

                    // On affiche le texte devant la case.
                    g.drawString(f.texte, (f.x * sizeBox + marginLeftRight + 1) + sizeBox / 2 - sizeText / 2, (f.y * sizeBox + marginUpDown + 1) + sizeBox / 2 + sizeBox/10);
                }
            }
        }
    }

    /*
     * Méthode permettant de dessiner les pions des joueurs.
     * @param g : contient tous les éléments dessinées.
     * @param tailleCase : taille des cases à dessiner.
     * @param margeHB : la marge du haut et du bas.
     * @param margeHB : la marge de droite et de gauche.
     */
    private void drawPlayers(Graphics g, int tailleCase, int margeHB, int margeGD){
        // Liste contenant toutes les pos des pions à analyser.
        ArrayList<Integer> pionsDis = new ArrayList();

        // Liste contenant les regroupements des pions dans les cases (les pions dans les mêmes cases).
        ArrayList<ArrayList> caseJ = new ArrayList<>();

        // On rempli le pionsdis avec les pos des pions (de 0 a size).
        for(int i = 0; i < pawns.length; i++)
            pionsDis.add(i);

        // On vide progressivement la liste.
        while(!pionsDis.isEmpty()){

            // On crée une liste dans la liste.
            caseJ.add(new ArrayList<Objet>());

            // A la fin de cette liste, on met le premier pion de pions dis, qui sera étudié puis supprimé.
            caseJ.get(caseJ.size()-1).add(pawns[pionsDis.get(0)]);

            // i=1 pour eviter de réetudier le premier pion.
            for(int i = 1; i < pionsDis.size(); i++){

                // On compare les deux pions.
                if(pawns[pionsDis.get(0)].x == pawns[pionsDis.get(i)].x && pawns[pionsDis.get(0)].y == pawns[pionsDis.get(i)].y){

                    // Si ils sont sur la même case, on l'ajout avec le premier pion (donc même liste = même case).
                    caseJ.get(caseJ.size()-1).add(pawns[pionsDis.get(i)]);

                    // Puis on le supprime.
                    pionsDis.remove(i);

                    // On enleve un element de la liste en cours donc i--;
                    i--;
                }
            }
            // On retire le premier pion car on l'a automatiquement placer dans caseJ.
            pionsDis.remove(0);
        }

        // On defini les positions les pions sur une case selon le nombre de pions sur la case
        // (comme sur un dé). Ici, les chiffres représente le paré numérique d'un clavier (
        // comme si le pavé était une case et les touches de celui-ci les differantes positions).
        int[] posPions;
        for(ArrayList<Objet> l : caseJ){
            if(l.size() == 1) 	   posPions = new int[]{5};
            else if(l.size() == 2) posPions = new int[]{7, 3};
            else if(l.size() == 3) posPions = new int[]{7, 5, 3};
            else if(l.size() == 4) posPions = new int[]{7, 1, 9, 3};
            else if(l.size() == 5) posPions = new int[]{7, 1, 9, 3, 5};
            else if(l.size() == 6) posPions = new int[]{7, 8, 9, 1, 2, 3};
            else if(l.size() == 7) posPions = new int[]{7, 8, 9, 1, 2, 3, 5};
            else if(l.size() == 8) posPions = new int[]{7, 8, 9, 1, 2, 3, 4, 6};
            else 				   posPions = new int[]{7, 8, 9, 1, 2, 3, 4, 5, 6};

            // Pour tous les pions, on les place comme défini.
            for(int i = 0; i < l.size(); i++)
                placePions(g, posPions[i], l.get(i), tailleCase, margeHB, margeGD);
        }
        return;
    }

    /*
     * Méthode permettant de placer un pion sur une position d'une case.
     * @param g : contient tous les éléments dessinées.
     * @param pos : la position sur laquelle mettre le pion (le chiffre correspond au pavé num).
     * @param pion : le pion à dessiner.
     * @param tailleCase : taille des cases à dessiner.
     * @param margeHB : la marge du haut et du bas.
     * @param margeHB : la marge de droite et de gauche.
     */
    private void placePions(Graphics g, int pos, Objet pion, int tailleCase, int margeHB, int margeGD){
        // Selon la position qu'on veux, on donne un x et un y.
        int x = 0, y = 0;
        if(pos == 7 || pos == 8 || pos == 9) y = pion.y * tailleCase + margeHB + tailleCase * 3 / 16 - (tailleCase / 6);
        if(pos == 4 || pos == 5 || pos == 6) y = pion.y * tailleCase + margeHB + tailleCase / 2 - (tailleCase / 6);
        if(pos == 1 || pos == 2 || pos == 3) y = pion.y * tailleCase + margeHB + tailleCase * 13 / 16 - (tailleCase / 6);

        if(pos == 7 || pos == 4 || pos == 1) x = pion.x * tailleCase + margeGD + tailleCase * 3 / 16 - (tailleCase / 6);
        if(pos == 8 || pos == 5 || pos == 2) x = pion.x * tailleCase + margeGD + tailleCase / 2 - (tailleCase / 6);
        if(pos == 9 || pos == 6 || pos == 3) x = pion.x * tailleCase + margeGD + tailleCase * 13 / 16 - (tailleCase / 6);

        // Et on place le pion à la position voulu (rond plein coloré et rond vide bord noir).
        g.setColor(pion.couleur);
        g.fillOval(x, y, tailleCase / 3, tailleCase / 3);
        g.setColor(Color.black);
        g.drawOval(x, y, tailleCase / 3, tailleCase / 3);

        return;
    }

    /*
     * Méthode permettant de dessiner les instructions dans la fenètre.
     * @param g : contient tous les éléments dessinées.
     */
    private void drawNotice(Graphics g){

        // On place des carrés.
        g.setColor(CAIR[0]);
        // getWidth()/2 milieu de la fenètre, -25 moitié du carré(donc carré au milieu),
        // -75 moitié du carré + carré.
        g.fillRect(getWidth()/2-25-75, getHeight()/2-100, 50, 50);

        g.setColor(CEAU[0]);
        g.fillRect(getWidth()/2-25-25, getHeight()/2-100, 50, 50);

        g.setColor(CTERRE[0]);
        g.fillRect(getWidth()/2-25+25, getHeight()/2-100, 50, 50);

        g.setColor(CFEU[0]);
        g.fillRect(getWidth()/2-25+75, getHeight()/2-100, 50, 50);

        g.setColor(CNORMAL[0]);
        g.fillRect(getWidth()/2-25-50, getHeight()/2-50, 50, 50);

        g.setColor(CNORMAL[1]);
        g.fillRect(getWidth() / 2 - 25, getHeight()/2-50, 50, 50);

        g.setColor(CNORMAL[2]);
        g.fillRect(getWidth()/2-25+50, getHeight()/2-50, 50, 50);

        // On met les bords noirs.
        g.setColor(Color.black);
        g.drawRect(getWidth()/2-25+25, getHeight()/2-100, 50, 50);
        g.drawRect(getWidth()/2-25-25, getHeight()/2-100, 50, 50);
        g.drawRect(getWidth()/2-25-75, getHeight()/2-100, 50, 50);
        g.drawRect(getWidth()/2-25+75, getHeight()/2-100, 50, 50);

        g.drawRect(getWidth()/2-25-50, getHeight()/2-50, 50, 50);
        g.drawRect(getWidth() / 2 - 25, getHeight()/2-50, 50, 50);
        g.drawRect(getWidth()/2-25+50, getHeight()/2-50, 50, 50);

        // On met un titre.
        g.drawString("Projet POGL",   getWidth()/2-65, getHeight()/2-120);

        // On met les légendes des cases dans ses cases.
        g.drawString("Air",   getWidth()/2-25-75+18, getHeight()/2-75);
        g.drawString("Eau",   getWidth()/2-25-25+15, getHeight()/2-75);
        g.drawString("Terre", getWidth()/2-25+25+12, getHeight()/2-75);
        g.drawString("Feu",   getWidth()/2-25+75+15, getHeight()/2-75);

        g.drawString("Normale",   getWidth()/2-25-50+1, getHeight()/2-25);
        g.drawString("Inondé",   getWidth() / 2 - 25 +6, getHeight()/2-25);
        g.setColor(Color.white);
        g.drawString("Sub", getWidth()/2-25+50+15, getHeight()/2-25);

        // Variable pour écrive les roles des joueurs (pour que le code soit plus lisible).
        String aEcrire;

        // On place les pions sous les cases, avec les noms des joueurs associés.
        for(int i = 0; i < players.length; i++){
            g.setColor(CJOUEUR[i]);
            g.fillOval(getWidth()/2-50, getHeight()/2 + (i*30) + 10, 20, 20);
            g.setColor(Color.black);
            g.drawOval(getWidth()/2-50, getHeight()/2 + (i*30) + 10, 20, 20);

            aEcrire = ": " + players[i].getNom();

            // On cherche le role du joueur.
            if(players[i].getRole() == 1) aEcrire += " - le Pilote";
            else if(players[i].getRole() == 2) aEcrire += " - l'Ingénieur";
            else if(players[i].getRole() == 3) aEcrire += " - l'Explorateur";
            else if(players[i].getRole() == 4) aEcrire += " - le Navigateur";
            else if(players[i].getRole() == 5) aEcrire += " - le Plongeur";
            else if(players[i].getRole() == 6) aEcrire += " - le Messager";

            g.drawString(aEcrire, getWidth()/2-25, getHeight()/2 + (i*30) + 25);
        }

        // On met un joli rectangle autour.
        g.drawRect(getWidth()/2-150, getHeight()/2 - 150, 300, players.length*30 + 160);

        return;
    }

    /*
     * Accesseur.
     * @param j : le joueur à mettre dans l'attribut joueurActuel.
     */
    public void setJoueurActuel(Player j){
        currentPlayer = j;
        return;
    }

    /*
     * Méthode permettant de créer les deux lignes, au dessus et en dessous.
     * @param g : contient tous les éléments dessinées.
     * @param tailleCase : taille des cases à dessiner.
     */
    private void drawInfo(Graphics g, int tailleCase){
        // Couleur des lignes.
        g.setColor(new Color(182, 182, 182));

        // Dessin des lignes.
        g.fillRect(0, 0, this.getWidth(), tailleCase / 2);
        g.fillRect(0, this.getHeight() - tailleCase / 2, this.getWidth(), tailleCase / 2);

        // On cherche la position du joueur dans la liste de joueur.
        int posJoueur;
        for(posJoueur = 0; posJoueur < players.length; posJoueur++){
            if(players[posJoueur] == currentPlayer)
                break;
        }

        // On crée le texte à afficher, la police et on calcul la taille du texte.
        String aEcrire = "C'est au tour de : " + currentPlayer.getNom();

        if(currentPlayer.getRole() == 1) aEcrire += " - le Pilote ";
        else if(currentPlayer.getRole() == 2) aEcrire += " - l'Ingénieur";
        else if(currentPlayer.getRole() == 3) aEcrire += " - l'Explorateur";
        else if(currentPlayer.getRole() == 4) aEcrire += " - le Navigateur";
        else if(currentPlayer.getRole() == 5) aEcrire += " - le Plongeur";
        else if(currentPlayer.getRole() == 6) aEcrire += " - le Messager";

        Font fontCase = new Font("Arial", Font.BOLD, tailleCase/4);
        int tailleTexte = g.getFontMetrics().stringWidth(aEcrire);

        // On dessine le texte.
        g.setColor(Color.black);
        g.setFont(fontCase);
        g.drawString(aEcrire, this.getWidth() / 2 - tailleTexte, tailleCase);


        // On change le texte à écrire et on recalcul sa taille.
        aEcrire = "  Clefs :";
        tailleTexte = g.getFontMetrics().stringWidth(aEcrire);

        // On écrit le texte.
        g.drawString(aEcrire, 0, this.getHeight() - tailleCase / 6);

        // On met un fond blanc si il y a moins de 4 clefs (parce que sinon, inutile d'en mettre un).
        g.setColor(Color.white);
        if(currentPlayer.getKeys(0) < 4)
            g.fillRect(tailleTexte + this.getWidth() / 100, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);

        if(currentPlayer.getKeys(1) < 4)
            g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);

        if(currentPlayer.getKeys(2) < 4)
            g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);

        if(currentPlayer.getKeys(3) < 4)
            g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase + tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);


        // On dessine 1 à 3 petits carrés dans le carré blanc, selon le nombre de clef.
        g.setColor(CAIR[0]);

        // Si le joueur à plus de 3 clefs on fait un grand carré au lieu de 4 petits carrés.
        if(currentPlayer.getKeys(0) >= 4)
            g.fillRect(tailleTexte + this.getWidth() / 100, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);

            // Un else if pour éviter les 4 autres test inutile si le joueur n'a pas de clef.
        else if(currentPlayer.getKeys(0) != 0){

            // Si le joueur a plus d'une clef.
            if (currentPlayer.getKeys(0) >= 1)
                g.fillRect(tailleTexte + this.getWidth() / 100, this.getHeight() - tailleCase / 2, tailleCase / 4, tailleCase / 4);

            // Plus de deux.
            if (currentPlayer.getKeys(0) >= 2)
                g.fillRect(tailleTexte + this.getWidth() / 100, this.getHeight() - tailleCase / 2 + tailleCase / 4, tailleCase / 4, tailleCase / 4);


            if (currentPlayer.getKeys(0) >= 3)
                g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase / 4, this.getHeight() - tailleCase / 2, tailleCase / 4, tailleCase / 4);
        }


        g.setColor(CEAU[0]);
        if(currentPlayer.getKeys(1) >= 4)
            g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);
        else if(currentPlayer.getKeys(1) != 0){
            if (currentPlayer.getKeys(1) >= 1)
                g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 4, tailleCase / 4);
            if (currentPlayer.getKeys(1) >= 2)
                g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase / 2, this.getHeight() - tailleCase / 2 + tailleCase / 4, tailleCase / 4, tailleCase / 4);
            if (currentPlayer.getKeys(1) >= 3)
                g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase / 2 + tailleCase / 4, this.getHeight() - tailleCase / 2, tailleCase / 4, tailleCase / 4);
        }

        g.setColor(CTERRE[0]);
        if(currentPlayer.getKeys(2) >= 4)
            g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);
        else if(currentPlayer.getKeys(2) != 0){
            if (currentPlayer.getKeys(2) >= 1)
                g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase, this.getHeight() - tailleCase / 2, tailleCase / 4, tailleCase / 4);
            if (currentPlayer.getKeys(2) >= 2)
                g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase, this.getHeight() - tailleCase / 2 + tailleCase / 4, tailleCase / 4, tailleCase / 4);
            if (currentPlayer.getKeys(2) >= 3)
                g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase + tailleCase / 4, this.getHeight() - tailleCase / 2, tailleCase / 4, tailleCase / 4);
        }


        g.setColor(CFEU[0]);
        if(currentPlayer.getKeys(3) >= 4)
            g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase + tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);
        else if(currentPlayer.getKeys(3) != 0){
            if (currentPlayer.getKeys(3) >= 1)
                g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase + tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 4, tailleCase / 4);
            if (currentPlayer.getKeys(3) >= 2)
                g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase + tailleCase / 2, this.getHeight() - tailleCase / 2 + tailleCase / 4, tailleCase / 4, tailleCase / 4);
            if (currentPlayer.getKeys(3) >= 3)
                g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase + tailleCase / 2 + tailleCase / 4, this.getHeight() - tailleCase / 2, tailleCase / 4, tailleCase / 4);
        }

        // On entoure les cases de contours noirs.
        g.setColor(Color.black);
        g.drawRect(tailleTexte + this.getWidth() / 100, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);
        g.drawRect(tailleTexte + this.getWidth() / 100 + tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);
        g.drawRect(tailleTexte + this.getWidth() / 100 + tailleCase, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);
        g.drawRect(tailleTexte + this.getWidth() / 100 + tailleCase + tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);


        // On change le texte à écrire et on recalcul sa taille.
        aEcrire = ": Artefacts  ";
        tailleTexte = g.getFontMetrics().stringWidth(aEcrire);

        // On écrit le texte.
        g.drawString(aEcrire, this.getWidth() - tailleTexte, this.getHeight() - tailleCase / 6);

        // On dessine des cases à coté du texte et on met la couleur blanche si le joueur n'a pas la clé, la couleur de l'artefact sinon.
        g.setColor(currentPlayer.verifArtefact(0) ? CAIR[0] : Color.white);
        g.fillRect(this.getWidth() - tailleTexte - tailleCase/2 - this.getWidth() / 100 - tailleCase - tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);

        g.setColor(currentPlayer.verifArtefact(1) ? CEAU[0] : Color.white);
        g.fillRect(this.getWidth() - tailleTexte - tailleCase/2 - this.getWidth() / 100 - tailleCase, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);

        g.setColor(currentPlayer.verifArtefact(2) ? CTERRE[0] : Color.white);
        g.fillRect(this.getWidth() - tailleTexte - tailleCase/2 - this.getWidth() / 100 - tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);

        g.setColor(currentPlayer.verifArtefact(3) ? CFEU[0] : Color.white);
        g.fillRect(this.getWidth() - tailleTexte - tailleCase/2 - this.getWidth() / 100, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);

        // On entoure les cases de contours noirs.
        g.setColor(Color.black);
        g.drawRect(this.getWidth() - tailleTexte - tailleCase/2 - this.getWidth() / 100 - tailleCase - tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);
        g.drawRect(this.getWidth() - tailleTexte - tailleCase/2 - this.getWidth() / 100 - tailleCase, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);
        g.drawRect(this.getWidth() - tailleTexte - tailleCase/2 - this.getWidth() / 100 - tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);
        g.drawRect(this.getWidth() - tailleTexte - tailleCase/2 - this.getWidth() / 100, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);

        return;
    }

    /*
     * Méthode permettant de mettre à jour les données les tableaux cases et joueurs pour redessiner après.
     */
    public void update(){
        // Variable dans laquelle on mettera les cases à étudier.
        Case casse;

        // On parcours les cases.
        for(int i = 0; i < plateau.getLongueur(); i++){
            for(int j = 0; j < plateau.getLargeur(); j++) {
                // On pointe la case i, j.
                casse = plateau.getCase(i, j);

                // Si cette case est l'héliport, on met la couleur de l'héliport selon son niveau.
                if(casse.isHeliport())
                    boxs[i][j].couleur = casse.getNivMarin() == 2 ? CHELI[0] : casse.getNivMarin() == 1 ? CHELI[1] : CHELI[2];

                    // Sinon si c'est un artefact, on met la couleur de l'artefact selon son niveau.
                else if(casse.getArtéfact() != 0) {
                    if (casse.getArtéfact() == 1)
                        boxs[i][j].couleur = casse.getNivMarin() == 2 ? CAIR[0] : casse.getNivMarin() == 1 ? CAIR[1] : CAIR[2];
                    else if (casse.getArtéfact() == 2)
                        boxs[i][j].couleur = casse.getNivMarin() == 2 ? CEAU[0] : casse.getNivMarin() == 1 ? CEAU[1] : CEAU[2];
                    else if (casse.getArtéfact() == 3)
                        boxs[i][j].couleur = casse.getNivMarin() == 2 ? CTERRE[0] : casse.getNivMarin() == 1 ? CTERRE[1] : CTERRE[2];
                    else if (casse.getArtéfact() == 4)
                        boxs[i][j].couleur = casse.getNivMarin() == 2 ? CFEU[0] : casse.getNivMarin() == 1 ? CFEU[1] : CFEU[2];
                }

                // Sinon c'est une case normal donc on applique les couleurs normales.
                else
                    boxs[i][j].couleur = casse.getNivMarin() == 2 ? CNORMAL[0] : casse.getNivMarin() == 1 ? CNORMAL[1] : CNORMAL[2];
            }
        }

        // On met à jour les positions des joueurs.
        for(int i = 0; i < pawns.length; i++){
            pawns[i].x = players[i].getPosx();
            pawns[i].y = players[i].getPosy();
        }

        // On actualise l'affichage avec les valeurs mise à jour.
        repaint();
        return;
    }

    /*
     * Méthode permettant de choisir si on veux afficher les instructions ou non.
     */
    public void afficherAide(){
        // Interrupteur.
        afficheAide = !afficheAide;
    }
}

