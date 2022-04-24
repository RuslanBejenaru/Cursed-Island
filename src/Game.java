import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Game {

    private boolean DEBUG;

    private Plateau plateau;
    private Affichage affichage;
    protected Player[] players;
    private Deck deck;
    private Deck secialDeck;

    Scanner sc = new Scanner(System.in);

    private String[] CJOUEUR = {"Pion Rouge", "Pion Bleu", "Pion Cyan", "Pion Vert", "Pion Magenta",
            "Pion Orange", "Pion Gris", "Pion Rose", "Pion Jaune"};



    public Game(int longueur, int largeur, Player [] players){


        plateau = new Plateau(longueur, largeur);


        this.players = players.clone();


        affichage = new Affichage(plateau, players);


        Case héliport = plateau.init();

        for (int i = 0; i < players.length; i++)
            players[i].casePlayer(héliport);

        deck = new Deck(longueur * largeur);
        secialDeck = new Deck(28);

        return;

    }

    public void start(){
        affichage.intialize();

        System.out.println("\nAffichage de l'aide, appuyer sur \"Entrer\" pour continuer.");
        sc.nextLine();

// On enleve l'affichage de l'aide.
        affichage.afficherAide();
        affichage.update();

        int compt = 0;
        String str;
        // On relance les tours jusqu'a qu'un message soit envoyé.
        do {
            str = tour(compt);
            compt++;
        }while(str == "");
        System.out.println(str + "\nAppuyer sur \"Entrer\" pour quitter.");
        sc.nextLine();
        System.exit(0);
        return;
    }

    /*
     * Méthode permettant de faire un tour de jeu.
     * @param nbTour : le nombre de tour à afficher.
     * @return rien si la partie doit continuer, sinon un message de fin.
     */
    protected String tour(int nbTour){
        // Variable qui stockera ce que l'utilisateur entrera.
        String str;

        // Variable qui stockera 0 pour sortir de la boucle while ou 1 sinon.
        int passe;

        // Variable qui va permettre à l'ingé d'assecher deux cases pour une action.
        boolean assecheInge = false;

        clearConsole();
        System.out.println("--------\nTour n°" + Integer.toString(nbTour+1));

        // Pour tous les joueurs.
        for(Player j : players){

            // On donne le joueur actuel à dessin.
            affichage.setJoueurActuel(j);

            // On update pour afficher les actions.
            affichage.update();

			/*
			// On affiche les infos du joueur.
			System.out.println("--------\nAu tour de " + j.getName());
			System.out.println("Vos clefs : " + Integer.toString(j.getClef(0)) + "x Air - "
					+ Integer.toString(j.getClef(1)) + "x Eau - "
					+ Integer.toString(j.getClef(2)) + "x Terre - "
					+ Integer.toString(j.getClef(3)) + "x Feu");

			System.out.println("Vos artefacts : " + (j.verifArtefact(0) ? "Air ":"___ ")
					+ (j.verifArtefact(1) ? "Eau ":"___ ")
					+ (j.verifArtefact(2) ? "Terre ":"_____ ")
					+ (j.verifArtefact(3) ? "Feu ":"___ "));
			*/

            // 3 actions.
            for(int i = 0; i < 3; i++) {
                // Si une action est impossible, on boucle.
                do {
                    // On demande au joueur l'action qu'il veut faire.
                    if(DEBUG) System.out.println("Mode Debug");
                    System.out.print("--------\nAction n°" + Integer.toString(i+1) + "\n--------\nAffichage de l'aide (H) - Fin de votre tour (Q)\n" +
                            "Déplacement du pion (D) - Assèchement d'une case adjacente (A)\nDon de clef (C)" +
                            (j.getPlayer().getArtéfact() != 0 ? " - Recherche artefact (R) :" : ""));

                    // Si le joueur à une capacité spéciales qui ajoute une action possible.
                    if(j.getRôle() == 1) System.out.print("\nPilote : Hélico (T)");
                    if(j.getRôle() == 4) System.out.print("\nNavigateur : Déplacer un autre joueur (J)");

                    System.out.print("\n\nVotre choix : ");
                    str = sc.nextLine();

                    // On envoi la demande du joueur à action.
                    // return 0, pas d'erreur.
                    // return 1, erreur donc on redemande sans consommer d'actions.
                    // return 2, utilisateur à finison tour.
                    passe = action(str, j);

                    // Si passe == 2, on fini le for.
                    if(passe == 2) i = 3;

                    // On update pour afficher les actions.
                    affichage.update();

                }while(passe != 0 && passe != 2);

                // Si on est un ingénieur, on peux assecher deux cases donc la première fois, on ne bouge pas le nombre d'action restante.
                if((str.equals("A") || str.equals("a")) && j.getRôle() == 2 && !assecheInge) {
                    i--;
                    assecheInge = true;
                }

                if(DEBUG)i--;
            }

            // On recherche une clé.
            int recup = rechercheClef(j);

            if(recup == 0)
                System.out.println("Votre case prend l'eau !");

            else if(recup <= 4)
                System.out.println("Ajout d'une clef " + (recup == 1 ? "Air" : recup == 2 ? "Eau" : recup == 3 ? "Terre" : "Feu") + ".\n");

            else if(recup == 5)
                System.out.println("Vous avez pris l'hélico !");

            else
                System.out.println("La case à été asséchée.");

            // On inonde 3 cases.
            inondation();

            // On update la fenètre pour afficher les inondations.
            affichage.update();

            System.out.println("Appuyer sur \"Entrer\" pour continuer.\n");
            sc.nextLine();

            // On verifie si on a perdu ou gagner.
            str = verifPerdu();

            // Si verifPerdu() retourne autre chose que vide, c'est que la partie est gagner ou perdu.
            if(str != "") return str;
        }
        return "";
    }

    /*
     * Méthode permettant au joueur de faire une action.
     * @param action : L'action demandé par le joueur.
     * @param j : Le joueur qui a demander l'action.
     * @return 0 si pas d'erreur, 1 si erreur, 2 qui on veut quitter le tour.
     */
    private int action(String action, Player j){

        // Variable qui stockera 0 pour sortir de la boucle while ou 1 sinon.
        int passe;

        // Si on demande un déplacement.
        if (action.equals("D") || action.equals("d")) {

            // On demande où aller (si explorateur, deplacement diagonal).
            if(j.getRôle() != 3)
                System.out.print("\n   (H)\n(G)-|-(D)\n   (B)\nVotre choix : ");
            else
                System.out.print("\n(HG)(H)(HD)\n(G) -|- (D)\n(BG)(B)(BD)\nVotre choix : ");

            action = sc.nextLine();

            // On envoi le choix à deplacement().
            passe = deplacement(action, j);

            // Si deplacement retourne 1, alors il y a eu une erreur et donc on boucle.
            if (passe == 1) System.out.println("Impossible d'effectuer le déplacement " + action + ".\n");
            else System.out.println("Déplacement effectué.\n");
            return passe;
        }

        // Si on demande un assechement.
        else if (action.equals("A") || action.equals("a")) {

            // On demande où assecher (si explorateur, assechement diagonal).
            if(j.getRôle() != 3)
                System.out.print("\n   (H)\n(G)(C)(D)\n   (B)\nVotre choix : ");
            else
                System.out.print("\n(HG)(H)(HD)\n(G) (C) (D)\n(BG)(B)(BD)\nVotre choix : ");

            action = sc.nextLine();

            // On envoi le choix à assechement().
            passe = assechement(action, j);

            // Si assechement retourne 1, alors il y a eu une erreur et donc on boucle.
            if (passe == 1) System.out.println("Impossible d'assécher la case.\n");
            else System.out.println("Assèchement effectué.\n");
            return passe;
        }

        // Si on demande une recherche.
        else if (action.equals("R") || action.equals("r")) {
            passe = recupArtefact(j);
            if (passe == 1) System.out.println("Impossible de récuperer un artefact ici.\n");
            else System.out.println("Vous avez récupéré un artefact !\n");
            return passe;
        }

        // Si on demande l'aide.
        else if (action.equals("H") || action.equals("h")) {
            // J'ai choisi de faire un interrupteur pour qu'en cas de redimentionnement, l'aide reste.
            // On active l'interrupteur.
            affichage.afficherAide();

            // On update l'affichage pour afficher l'aide.
            affichage.update();

            // On attend que l'utilisateur finisse.
            System.out.println("Appuyer sur \"Entrer\" pour continuer.\n");
            sc.nextLine();

            // On désactive l'interrupteur.
            affichage.afficherAide();

            // On update l'affichage pour afficher la grille.
            affichage.update();

            // On boucle.
            return 1;
        }

        // Si on veut donner une clef.
        else if(action.equals("C") || action.equals("c")) {
            System.out.print("\nClef Air (A) - Clef Eau (E) - Clef Terre (T) - Clef Feu (F) \nVotre choix : ");
            action = sc.nextLine();

            System.out.println("\nA quel joueur ?");
            for(int k = 0; k < players.length; k++) {
                // Si le joueur est un messager, il peux donner une clef a tout le monde.
                if(j.getRôle() == 6)
                    System.out.println(players[k].getNom() + " - " + CJOUEUR[k] + " (" + Integer.toString(k) + ")");
                else {
                    // On ne met que les joueurs qui sont sur la même case.
                    if (players[k].getPlayer().equals(j.getPlayer()))
                        System.out.println(players[k].getNom() + " - " + CJOUEUR[k] + " (" + Integer.toString(k) + ")");
                }
            }

            System.out.print("\nVotre choix : ");
            String str = sc.nextLine();

            passe = donClef(action, str, j);
            if (passe == 1) System.out.println("Don impossible à effectuer.\n");
            else System.out.println("Vous avez donné une clef !\n");

            return passe;
        }

        // Si on veux finir le tour.
        else if(action.equals("Q") || action.equals("q")) {
            return 2;
        }

        // Si le joueur est un pilote et qu'il veut se déplacer.
        else if((action.equals("T") || action.equals("t")) && j.getRôle() == 1){
            // On téléporte le joueur (ne renvoie pas d'erreurs).
            teleporte(j, true);
            System.out.println("Déplacement effectué.\n");
            return 0;
        }

        // Si le joueur est un navigateur
        else if((action.equals("J") || action.equals("j")) && j.getRôle() == 4){

            // On demande quel joueur déplacer.
            System.out.println("\nDéplacer quel joueur ?");
            for(int k = 0; k < players.length; k++)
                System.out.println(players[k].getNom() + " - " + CJOUEUR[k] + " (" + Integer.toString(k) + ")");

            System.out.print("\nVotre choix : ");
            String str = sc.nextLine();

            // On converti le str entré par le joueur en int.
            int aDonner;
            try {
                aDonner = Integer.decode(str);
            }
            catch(NumberFormatException e){
                return 1;
            }

            // Si le joueur entré n'existe pas.
            if(aDonner < 0 || aDonner >= players.length) return 1;

            // Si le joueur veut se déplacer lui même.
            if(j.equals(players[aDonner])) return 1;

            // On demande où aller
            System.out.print("\n   (H)\n(G)-|-(D)\n   (B)\nVotre choix : ");

            action = sc.nextLine();

            // On envoi le choix à deplacement().
            passe = deplacement(action, players[aDonner]);

            // Si deplacement retourne 1, alors il y a eu une erreur et donc on boucle.
            if (passe == 1) System.out.println("Impossible d'effectuer le déplacement " + action + ".\n");
            else System.out.println("Déplacement effectué.\n");
            return passe;
        }

        // Si le mode debug est actif.
        else if(DEBUG) {
            if(action.equals("0")) j.ajouteKeys(0);
            else if(action.equals("1")) j.ajouteKeys(1);
            else if(action.equals("2")) j.ajouteKeys(2);
            else if(action.equals("3")) j.ajouteKeys(3);
            return 1;
        }

        // Si l'utilisateur met une autre lettre, on recommence.
        else return 1;
    }

    /*
     * Méthode pour déplacer un joueur.
     * @param HBGD : le déplacement demandé.
     * @param j : le joueur à déplacer.
     * @return 0 si le déplacement a été fait, 1 sinon.
     */
    protected int deplacement(String HBGD, Player j){

        // Si on demande un déplacement haut.
        if(HBGD.equals("H") || HBGD.equals("h")){

            // Si il n'y a pas de case au dessus.
            if(j.getPosy() == 0) return 1;

            // Si la case du dessus est submergé (sauf si c'est un Nageur).
            if(plateau.getCase(j.getPosx(), j.getPosy()-1).getNivMarin() == 0 && !DEBUG && j.getRôle() != 5) return 1;

            // Sinon, on donne la case du dessus au joueur.
            j.casePlayer(plateau.getCase(j.getPosx(), j.getPosy()-1));
        }

        // Si on demande un déplacement bas.
        else if(HBGD.equals("B") || HBGD.equals("b")){
            if(j.getPosy() + 1 == plateau.getLargeur()) return 1;
            if(plateau.getCase(j.getPosx(), j.getPosy()+1).getNivMarin() == 0 && !DEBUG && j.getRôle() != 5) return 1;
            j.casePlayer(plateau.getCase(j.getPosx(), j.getPosy()+1));
        }

        // Si on demande un déplacement gauche.
        else if(HBGD.equals("G") || HBGD.equals("g")){
            if(j.getPosx() == 0) return 1;
            if(plateau.getCase(j.getPosx()-1, j.getPosy()).getNivMarin() == 0 && !DEBUG && j.getRôle() != 5) return 1;
            j.casePlayer(plateau.getCase(j.getPosx()-1, j.getPosy()));
        }

        // Si on demande un déplacement droit.
        else if(HBGD.equals("D") || HBGD.equals("d")){
            if(j.getPosx() + 1 == plateau.getLongueur()) return 1;
            if(plateau.getCase(j.getPosx()+1, j.getPosy()).getNivMarin() == 0 && !DEBUG && j.getRôle() != 5) return 1;
            j.casePlayer(plateau.getCase(j.getPosx()+1, j.getPosy()));
        }

        else if(j.getRôle() == 3){
            if(HBGD.equals("HD") || HBGD.equals("hd")){
                if(j.getPosx() + 1 == plateau.getLongueur() || j.getPosy() == 0) return 1;
                if(plateau.getCase(j.getPosx()+1, j.getPosy()-1).getNivMarin() == 0 && !DEBUG) return 1;
                j.casePlayer(plateau.getCase(j.getPosx()+1, j.getPosy()-1));
            }

            else if(HBGD.equals("HG") || HBGD.equals("hg")){
                if(j.getPosx() == 0 || j.getPosy() == 0) return 1;
                if(plateau.getCase(j.getPosx()-1, j.getPosy()-1).getNivMarin() == 0 && !DEBUG) return 1;
                j.casePlayer(plateau.getCase(j.getPosx()-1, j.getPosy()-1));
            }

            else if(HBGD.equals("BD") || HBGD.equals("bd")){
                if(j.getPosx() + 1 == plateau.getLongueur() || j.getPosy() + 1 == plateau.getLargeur()) return 1;
                if(plateau.getCase(j.getPosx()+1, j.getPosy()+1).getNivMarin() == 0 && !DEBUG) return 1;
                j.casePlayer(plateau.getCase(j.getPosx()+1, j.getPosy()+1));
            }

            else if(HBGD.equals("BG") || HBGD.equals("bg")){
                if(j.getPosx() == 0 || j.getPosy() + 1 == plateau.getLargeur()) return 1;
                if(plateau.getCase(j.getPosx()-1, j.getPosy()+1).getNivMarin() == 0 && !DEBUG) return 1;
                j.casePlayer(plateau.getCase(j.getPosx()-1, j.getPosy()+1));
            }
        }

        // Si on a une autre lettre, erreur.
        else return 1;

        return 0;
    }

    /*
     * Méthode pour assecher une case.
     * @param j : le joueur avec la case à assecher.
     * @return 0 si l'assechement a été fait, 1 sinon.
     */
    protected int assechement(String HBGD, Player j){

        // Si on demande un assechement de la case au dessus du joueur.
        if(HBGD.equals("H") || HBGD.equals("h")){
            // Si il n'y a pas de case au dessus.
            if(j.getPosy() == 0) return 1;
            // Si la case du dessus est submergé.
            if(plateau.getCase(j.getPosx(), j.getPosy()-1).getNivMarin() == 0) return 1;
            // Sinon, on asseche.
            return plateau.getCase(j.getPosx(), j.getPosy()-1).inondation();
        }

        // Si on demande un assechement de la case en dessous du joueur.
        else if(HBGD.equals("B") || HBGD.equals("b")){
            if(j.getPosy() + 1 == plateau.getLargeur()) return 1;
            if(plateau.getCase(j.getPosx(), j.getPosy()+1).getNivMarin() == 0) return 1;
            return plateau.getCase(j.getPosx(), j.getPosy()+1).inondation();
        }

        // Si on demande un assechement de la case à gauche du joueur.
        else if(HBGD.equals("G") || HBGD.equals("g")){
            if(j.getPosx() == 0) return 1;
            if(plateau.getCase(j.getPosx()-1, j.getPosy()).getNivMarin() == 0) return 1;
            return plateau.getCase(j.getPosx()-1, j.getPosy()).inondation();
        }

        // Si on demande un assechement de la case à droite du joueur.
        else if(HBGD.equals("D") || HBGD.equals("d")){
            if(j.getPosx() + 1 == plateau.getLongueur()) return 1;
            if(plateau.getCase(j.getPosx()+1, j.getPosy()).getNivMarin() == 0) return 1;
            return plateau.getCase(j.getPosx()+1, j.getPosy()).inondation();
        }

        // Si on demande un assechement de la case du joueur.
        else if(HBGD.equals("C") || HBGD.equals("c")){
            // Pour le mode debug, si la case est seche, on l'a coule,
            // si elle est coulée, on l'a submerge et sinon on la coule.
            if(DEBUG){
                if(j.getPlayer().getNivMarin() == 2 || j.getPlayer().getNivMarin() == 1) return j.getPlayer().assèchement();
                if(j.getPlayer().getNivMarin() == 0) return j.getPlayer().getNivMarin();
            }
            // Si la case est déjà submergé, impossible à assecher.
            if(j.getPlayer().getNivMarin() == 0) return 1;

            // Si la case est inondé, on asseche et on retourne 0, si elle est normale, on retourne 1.
            return j.getPlayer().inondation();
        }

        // Si le joueur est un explorateur, on ajoute des déplacements.
        else if(j.getRôle() == 3){
            if(HBGD.equals("HD") || HBGD.equals("hd")){
                if(j.getPosx() + 1 == plateau.getLongueur() || j.getPosy() == 0) return 1;
                if(plateau.getCase(j.getPosx()+1, j.getPosy()-1).getNivMarin() == 0) return 1;
                return plateau.getCase(j.getPosx()+1, j.getPosy()-1).inondation();
            }

            else if(HBGD.equals("HG") || HBGD.equals("hg")){
                if(j.getPosx() == 0 || j.getPosy() == 0) return 1;
                if(plateau.getCase(j.getPosx()-1, j.getPosy()-1).getNivMarin() == 0) return 1;
                return plateau.getCase(j.getPosx()-1, j.getPosy()-1).inondation();
            }

            else if(HBGD.equals("BD") || HBGD.equals("bd")){
                if(j.getPosx() + 1 == plateau.getLongueur() || j.getPosy() + 1 == plateau.getLargeur()) return 1;
                if(plateau.getCase(j.getPosx()+1, j.getPosy()+1).getNivMarin() == 0) return 1;
                return plateau.getCase(j.getPosx()+1, j.getPosy()+1).inondation();
            }

            else if(HBGD.equals("BG") || HBGD.equals("bg")){
                if(j.getPosx() == 0 || j.getPosy() + 1 == plateau.getLongueur()) return 1;
                if(plateau.getCase(j.getPosx()-1, j.getPosy()+1).getNivMarin() == 0) return 1;
                return plateau.getCase(j.getPosx()-1, j.getPosy()+1).inondation();
            }
            else return 1;
        }

        // Si on a une autre lettre, erreur.
        else return 1;
    }

    /*
     * Méthode pour récupérer un artefact.
     * @param j : le joueur avec la case artefact.
     * @return 0 si l'artefact a été récuperer, 1 sinon.
     */
    protected int recupArtefact(Player j){

        // On prend le numéro d'artefact.
        int artefactCase = j.getPlayer().getArtéfact();

        // Si c'est = 0, pas d'artefact.
        if(artefactCase != 0){

            // Si le joueur a quatre clefs ou plus.
            if(j.getKeys(artefactCase-1) >= 4){

                // On retire l'artefact de la case.
                j.getPlayer().ramasserArtéfact();

                // On ajoute l'artefact au joueur.
                j.ajouteArtéfact(artefactCase-1);
                return 0;
            }
        }
        return 1;
    }

    /*
     * Méthode pour rechercher une clef.
     * @param j : le joueur.
     * @return 0 si rien a été fait, 1 si la case a été inondé, 2-5 si une clé a été trouvé,
     * 6 si le joueur a pris l'hélico et 7 s'il a asseché une case.
     */
    protected int rechercheClef(Player j){

        // On prend une carte sur le paquet (si aucune case ne peut etre asseché et que l'on tombe sur une carte
        // assechement, on repioche).
        int alea = secialDeck.firstCard();
        while(!plateau.niveauMarin(1) && alea > 25)
            alea = secialDeck.firstCard();


        // Si inferieur egal 2, on inonde.
        if(alea <= 2) {
            j.getPlayer().assèchement();
            deck.melangeDeckDefausse();
            return 0;
        }

        // Si entre 1 et 22, on donne une clé.
        else if(alea <= 7) {
            j.ajouteKeys(0);
            return 1;
        }

        else if(alea <= 12) {
            j.ajouteKeys(1);
            return 2;
        }

        else if(alea <= 17) {
            j.ajouteKeys(2);
            return 3;
        }

        else if(alea <= 22) {
            j.ajouteKeys(3);
            return 4;
        }

        // Si entre 23 et 25, on appel l'hélico.
        else if(alea <= 25) {
            System.out.println("\nHélico :\nVous pouvez aller où vous voulez.");
            teleporte(j, false);
            return 5;
        }

        // Sinon, on donne un sac de sable au joueur.
        else{
            System.out.println("\nSac de sable :\nVous pouvez assecher la case que vous voulez.");
            // Variable contenant le nombre entré par le joueur.
            int nbJ;

            // Permet de redemander si un caractère a été entré au lieu d'un chiffre.
            boolean erreur;

            affichage.nameBoxes(true);
            affichage.update();

            // Tant qu'on a une erreur.
            do {
                // Gestion d'erreur au cas où un charactère a été entrée.
                try {
                    erreur = false;
                    System.out.print("\nMettre la position de la case à assecher : ");
                    nbJ = sc.nextInt();
                } catch (InputMismatchException e) {
                    sc.nextLine();
                    System.out.println("Erreur.");
                    nbJ = 0;
                    erreur = true;
                }
                // Si on sort du tableau.
                if(nbJ < 0 || nbJ > (plateau.getLongueur() * plateau.getLargeur()) - 1) erreur = true;

                // Si la case choisie n'est pas assechable.
                if(!erreur && plateau.getCase(nbJ % plateau.getLongueur(), nbJ / plateau.getLongueur()).getNivMarin() != 1) erreur = true;
            }while(erreur);

            // Pour le retour à la ligne.
            sc.nextLine();

            // On asseche la case choisie.
            plateau.getCase(nbJ % plateau.getLongueur(), nbJ / plateau.getLongueur()).inondation();

            // On remet les noms des cases spéciales.
            affichage.nameBoxes(false);
            return 6;
        }
    }

    /*
     * Méthode permettant de téléporter un joueur.
     * @param j : le joueur à téléporter.
     * @param role : true si le joueur est un pilote, false si c'est une rechercheClef.
     */
    private void teleporte(Player j, boolean role) {
        // Variable contenant le nombre entré par le joueur.
        int nbJ;

        // Permet de redemander si un caractère a été entré au lieu d'un chiffre.
        boolean erreur;

        // On met les positions sur le dessin.
        affichage.nameBoxes(true);
        affichage.update();

        // Tant qu'on a une erreur.
        do {
            // Gestion d'erreur au cas où un charactère a été entrée.
            try {
                erreur = false;
                System.out.print("\nMettre la position de la case où se déplacer : ");
                nbJ = sc.nextInt();
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("Erreur.");
                nbJ = 0;
                erreur = true;
            }
            // Si on sort du tableau.
            if (nbJ < 0 || nbJ > (plateau.getLongueur() * plateau.getLargeur()) - 1) erreur = true;

            // Si la case choisie n'est pas accessible.
            if (!erreur && plateau.getCase(nbJ % plateau.getLongueur(), nbJ / plateau.getLongueur()).getNivMarin() == 0)
                erreur = true;
        } while (erreur);

        // Pour le retour à la ligne.
        sc.nextLine();

        // On remet les noms des cases spéciales.
        affichage.nameBoxes(false);
        affichage.update();

        // On regarde s'il y a un autre joueur sur la case, sinon inutile de lui demander avec qui il veux venir.
        // On réutilise la variable erreur.
        for (int k = 0; k < players.length; k++) {
            if (players[k].getPlayer().equals(j.getPlayer()) && !players[k].equals(j)) {
                erreur = true;
                break;
            }
        }

        // Si un joueur à été trouvé et que c'est pas un role de Pilote.
        if (erreur && !role) {
            // Tant que le format entré n'est pas valide.
            do {

                // On demande au joueur avec qui il souhaite partir.
                System.out.println("\nAvec quels joueurs ? (exemple format : 0,1,2 ou rien si aucun joueur)");
                for (int k = 0; k < players.length; k++) {

                    // On ne met que les joueurs qui sont sur la même case.
                    if (players[k].getPlayer().equals(j.getPlayer()))
                        System.out.println(players[k].getNom() + " - " + CJOUEUR[k] + " (" + Integer.toString(k) + ")");
                }

                System.out.print("\nVotre choix : ");
                String str = sc.nextLine();

                // Si le joueur à mis une liste vide, c'est qu'il veux partir avec personne.
                if (str.equals("")) erreur = false;

                else {
                    // On découpe le string que le joueur nous a donné.
                    ArrayList<Integer> joueursQuiViens = splitString(str);

                    // Si la taille du tableau est de 0, c'est qu'il y a eu une erreur donc on boucle.
                    if (joueursQuiViens.size() != 0) {
                        // On met erreur a false pour qu'on puisse sortir de la boucle si les éléments de la liste sont bon.
                        erreur = false;

                        for (Integer i : joueursQuiViens) {

                            // Si le joueur n'est pas dans la liste des joueurs, pas bon.
                            if (i < 0 || i >= players.length) {
                                erreur = true;
                                break;
                            }
                            // Si le joueur entré n'est pas sur la même case, pas bon non plus.
                            else if (!players[i].getPlayer().equals(j.getPlayer())) {
                                erreur = true;
                                break;
                            }
                        }
                        // Si la liste est bonne, on déplace tous les joueurs qui sont dedans.
                        if (!erreur) {
                            for (Integer i : joueursQuiViens) {
                                players[i].casePlayer(plateau.getCase(nbJ % plateau.getLongueur(), nbJ / plateau.getLongueur()));
                            }
                        }
                    }
                }

            } while (erreur);
        }

        // On se déplace.
        j.casePlayer(plateau.getCase(nbJ % plateau.getLongueur(), nbJ / plateau.getLongueur()));
    }

    /*
     * Méthode permettant de split un string.
     * @param str : le string.
     * @return la liste avec les éléments du str ou vide si erreur.
     */
    protected ArrayList<Integer> splitString(String str){
        // On crée la liste a return.
        ArrayList<Integer> ints = new ArrayList<>();

        // On crée une liste de String avec une découpe entre les virgules.
        String[] lstr = str.split(",");

        // Pour tous les éléments du tableau.
        for(String s : lstr){
            // On verifie que c'est bien des entiers.
            try{
                ints.add(Integer.decode(s));
            }
            catch(java.lang.NumberFormatException e){
                // Sinon on return une liste vide.
                ints.clear();
                return ints;
            }
        }
        return ints;
    }

    /*
     * Méthode pour inonder 3 cases.
     */
    protected void inondation(){
        int tirage = 0;
        for(int i = 0; i < 3; i++) {
            // On tire une première carte.
            tirage = deck.firstCard();

            // Si la carte qu'on a tiré designe une zone submergé, on pioche la suivante.
            while (plateau.getCase(tirage % plateau.getLongueur(), tirage / plateau.getLongueur()).assèchement() == 1)
                tirage = deck.firstCard();
        }
        return;
    }

    /*
     * Méthode permettant de donner une clef à un autre joueur.
     * @param str : Le type de clef à donner.
     * @param str1 : Le joueur à qui donner la clef.
     * @param j : Le joueur qui donne sa clef.
     * @return 0 si pas d'erreur, 1 sinon.
     */
    protected int donClef(String str, String str1, Player j){

        // On convertit le str entré par le joueur en int.
        int aDonner;
        try {
            aDonner = Integer.decode(str1);
        }
        catch(NumberFormatException e){
            return 1;
        }

        // Si le joueur entré n'existe pas.
        if(aDonner < 0 || aDonner >= players.length) return 1;

        // Si le joueur n'est pas un messager et qu'il n'est pas sur la même case que l'autre joueur,
        // erreur.
        if(j.getRôle() != 6 && !players[aDonner].getPlayer().equals(j.getPlayer())) return 1;

        // Sinon, on verifie que le joueur a bien la clef et on lui donne.
        if(str.equals("A") || str.equals("a")){
            if(j.supprimeKeys(0) == 1) return 1;
            return players[aDonner].ajouteKeys(0);
        }

        else if(str.equals("E") || str.equals("e")){
            if(j.supprimeKeys(1) == 1) return 1;
            return players[aDonner].ajouteKeys(1);
        }

        else if(str.equals("T") || str.equals("t")){
            if(j.supprimeKeys(2) == 1) return 1;
            return players[aDonner].ajouteKeys(2);
        }

        else if(str.equals("F") || str.equals("f")){
            if(j.supprimeKeys(3) == 1) return 1;
            return players[aDonner].ajouteKeys(3);
        }

        else return 1;
    }

    /*
     * Méthode pour effacer la console.
     */
    private void clearConsole() {
        for(int i = 0; i < 20; i++){
            System.out.print("\n");
        }
    }

    /*
     * Méthode pour verifier si la partie est gagner, perdu ou ni l'un, ni l'autre.
     * @return le message de fin ou rien.
     */
    protected String verifPerdu(){
        int compt = 0, max = 0;

        // Si tous les joueurs sont sur la case heliport.
        for(Player j : players){
            if(j.getPlayer().isHeliport()) compt++;
        }

        // Et qu'il n'y a plus d'artefact dans la grille.
        for(int i = 0; i < 5; i++) max += plateau.getSpecialCase(i).getArtéfact();

        // On a gagné.
        if(compt == players.length && max == 0)
            return "Vous avez gagné !";

        // On verifie si le joueur est entouré de cases inondé.
        // max permet de compter le nombre de case autour du joueur (au cas où le joueur est à coté d'un mur).
        // compt permet de compter les cases submergé autour.
        // Pour tous les joueurs.
        for(Player j : players) {

            // Case du dessus.
            if (j.getPosy() != 0) {
                max++;
                if (plateau.getCase(j.getPosx(), j.getPosy() - 1).getNivMarin() == 0)
                    compt++;
            }

            // Case du dessous.
            if (j.getPosy() + 1 != plateau.getLargeur()) {
                max++;
                if (plateau.getCase(j.getPosx(), j.getPosy() + 1).getNivMarin() == 0)
                    compt++;
            }

            // Case à gauche.
            if (j.getPosx() != 0) {
                max++;
                if (plateau.getCase(j.getPosx() - 1, j.getPosy()).getNivMarin() == 0)
                    compt++;
            }

            // Case de droite.
            if (j.getPosx() + 1 != plateau.getLongueur()) {
                max++;
                if (plateau.getCase(j.getPosx() + 1, j.getPosy()).getNivMarin() == 0)
                    compt++;
            }

            // Si il y a autant de case submergé autour du joueur que de cases autour du joueur.
            if(max - compt == 0) return "Vous avez perdu !\nLe joueur " + j.getNom() + " est bloqué !";
            max = 0; compt = 0;
        }


        // Si l'une des cases spéciales est entouré de cases submergées, inaccessible donc perdu.
        for(int i = 0; i < 5; i++) {
            // Case du dessus.
            if (plateau.getSpecialCase(i).getCoordY() != 0) {
                max++;
                if (plateau.getCase(plateau.getSpecialCase(i).getCoordX(), plateau.getSpecialCase(i).getCoordY() - 1).getNivMarin() == 0)
                    compt++;
            }

            // Case du dessous.
            if (plateau.getSpecialCase(i).getCoordY() + 1 != plateau.getLargeur()) {
                max++;
                if (plateau.getCase(plateau.getSpecialCase(i).getCoordX(), plateau.getSpecialCase(i).getCoordY() + 1).getNivMarin() == 0)
                    compt++;
            }

            // Case à gauche.
            if (plateau.getSpecialCase(i).getCoordX() != 0) {
                max++;
                if (plateau.getCase(plateau.getSpecialCase(i).getCoordX() - 1, plateau.getSpecialCase(i).getCoordY()).getNivMarin() == 0)
                    compt++;
            }

            // Case de droite.
            if (plateau.getSpecialCase(i).getCoordX() + 1 != plateau.getLongueur()) {
                max++;
                if (plateau.getCase(plateau.getSpecialCase(i).getCoordX() + 1, plateau.getSpecialCase(i).getCoordY()).getNivMarin() == 0)
                    compt++;
            }

            // Si il y a autant de case submergé autour de la case spéciale que de cases autour de la case spéciale.
            if (max - compt == 0 && (plateau.getSpecialCase(i).getArtéfact() != 0 || plateau.getSpecialCase(i).isHeliport())) return "Vous avez perdu !\nUne case spéciale est inaccessible !";
            max = 0; compt = 0;
        }

        // On verifie si les cases spéciales ne sont pas submergé.
        for(int i = 0; i < 5; i++){
            if(plateau.getSpecialCase(i).isHeliport() || plateau.getSpecialCase(i).getArtéfact() != 0){
                if(plateau.getSpecialCase(i).getNivMarin() == 0){
                    return "Vous avez perdu !\nUne case spécial est bloquée !";
                }
            }
        }

        return "";
    }
}
