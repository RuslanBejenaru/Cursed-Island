// ------------------------
// Projet POGL : Île Interdite
// Par Alexandre l'Heritier
// ------------------------
// Classe Main
// ------------------------

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    /*
     * Méthode permettant de demander les noms des joueurs et leurs roles.
     */
    private static Player[] entrerJoueur(){
        // On crée un scanner pour prendre l'entrer de l'utilisateur.
        Scanner sc = new Scanner(System.in);

        // On crée une variable pour entrer le nom du joueur.
        String nom;

        // Variable contenant le nombre de joueur.
        int nbJ, nbJ1;

        // Permet de redemander si un caractère a été entré au lieu d'un chiffre.
        boolean erreur;

        // Tant qu'on a une erreur.
        do {
            // Gestion d'erreur au cas où un charactère a été entrée.
            try {
                erreur = false;
                System.out.print("Entrer nombre de joueur (1-9) : ");
                nbJ = sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                sc.nextLine();
                System.out.println("Erreur.");
                nbJ = 0;
                erreur = true;
            }
            // On ne peux avoir que 9 joueurs au max.
            if(nbJ < 1 || nbJ > 9) erreur = true;
        }while(erreur);

        // Pour le retour à la ligne.
        sc.nextLine();

        // On crée une liste des joueurs.
        Player[] joueurs = new Player[nbJ];

        // Liste permettant d'eviter d'attribuer deux même role à differantes personnes.
        ArrayList<Integer> rolesAttribue = new ArrayList<>();

        // On entre tous les noms des joueurs et leurs roles.
        for(int i = 0; i < nbJ; i++){
            System.out.print("Entrer le nom du joueur n°" + Integer.toString(i+1) + " : ");
            nom = sc.nextLine();

            System.out.println("\nRôle 0 : Aucun\nRôle 1 : Pilote : peut se déplacer vers une zone non submergée arbitraire (coûte une action).\n" +
                    "Rôle 2 : Ingénieur :  peut assécher deux zones pour une seule action.\n" +
                    "Rôle 3 : Explorateur : peut se déplacer et assécher diagonalement.\n" +
                    "Rôle 4 : Navigateur : peut déplacer un autre joueur (coûte une action).\n" +
                    "Rôle 5 : Plongeur : peut traverser une zone submergée (coûte une action).\n" +
                    "Rôle 6 : Messager : peut donner une clé qu’il possède à un joueur distant (coûte une action).\n");

            // Tant qu'on a une erreur.
            do {
                // Gestion d'erreur au cas où un charactère a été entrée.
                try {
                    erreur = false;
                    System.out.print("Entrer role (0-6) : ");
                    nbJ1 = sc.nextInt();
                } catch (java.util.InputMismatchException e) {
                    sc.nextLine();
                    System.out.println("Erreur.");
                    nbJ1 = 0;
                    erreur = true;
                }
                // On a que 6 roles + 1.
                if(nbJ1 < 0 || nbJ1 > 6) erreur = true;
                    // Pas deux fois les même roles.
                else if(rolesAttribue.contains(nbJ1)) erreur = true;
                    // Sinon, c'est bon, on crée le nouveau joueur.
                else{
                    rolesAttribue.add(nbJ1);
                    joueurs[i] = new Player(nom, nbJ1);
                }
            }while(erreur);
            sc.nextLine();
        }

        return joueurs;
    }

    public static void main(String[] args){
        System.out.println("-------------------------\nProjet POGL : Île Interdite\nPar Alexandre l'Heritier\n-------------------------");
        Game j = new Game(9, 6, entrerJoueur());
        j.start();
        return;
    }
}
/*
Changelog :
-----
Version Beta : v0.x
Version non stable : version pouvant planter à tous moments.
Version stable : version pouvant planter si une ou plusieurs actions précises sont effectuées.
Version SuperStable : version testé plusieurs fois avec des situations particulières et qui n'a pas planté.
-----

v3.1 (build 180513.1) :
Version SuperStable
- Correction d'un bug dans le déplacement et l'assechement de l'explorateur.
- Ajout de la couleur des pions dans les demandes d'actions.
- Changement dans les prioritées dans verifPerdu().
- Ajout de commentaires.
- Correction d'un bug au niveau des clefs.

(build 180514.1) :
- Ajout du vrai paquet de carte d'action (28 cartes).


v3.0 (build 180513.1) :
Version stable
(Mise en place des règles de la feuille cachée (dispo en pdf mais non distribuée)).
- Ajout de la possibilité d'assecher une case adjacente (ancien code dans condificon Centre, sinon reprise de la
  méthode deplacement()).
- Ajout de la possibilité de s'échanger des clefs. 											("Echanges de clés")
- Ajout de l'obligation d'avoir 4 clefs ou plus pour avoir un artefact. 					("Echanges de clés")
- Modification de l'affichage pour integrer les 4 clefs (ajout d'un découpage de case). 	("Echanges de clés")
- Découpage de la méthode tour() de la classe Jeu pour faire plus clair : création de la méthode action().
- Modification de l'affichage pour permettre l'affichage des positions des cases.
- Ajout des deux actions spéciales. 														("Actions spéciales")
- Ajout de la classe Paquet.																("Simulation d’un vrai paquet de cartes")
- Modification des méthodes innondation() et rechercheClef() pour supporter Paquet.			("Simulation d’un vrai paquet de cartes")
- Modification de la classe Joueur() :														("Personnages particuliers")
-- Constructeur avec role et sans caseJoueur créer.
-- Ajout des accesseurs pour l'attribut role.
- Modification de la fonction entrerJoueur() (nommé auparavant entrerNom()) pour supporter	("Personnages particuliers")
  l'entrer des roles des joueurs.
- Modification du constructeur de Jeu() pour supporter l'entrer d'une liste de Joueur.		("Personnages particuliers")
- Modification de différantes méthodes pour prendre en compte les particularités des 		("Personnages particuliers")
  personnages.
- Ajout de l'affichage de la particularité du personnage dans les lignes d'info.
- Ajout de l'affichage de la particularité du personnage dans l'aide.



v2.0 (build 180510.1) :
Version SuperStable.
- Ajout des noms des cases spéciales dans les cases spéciales.
- Ajout de lignes d'informations en haut et en bas dans l'interface.
- Ajout d'un update() dans tour() pour le changement de joueur dans les lignes d'infos.

(build 180511.1) :
- Ajout d'une défaite si une case spécial devient inaccessible.
- Ajout d'un mode Debug accessible sans modifier le code
  (activant tous les mécanismes debug créés lors du développement du programme).
- Modification les identifiants des clefs et des artefacts dans joueur et dans les autres classes
  pour faciliter la comprehention du code (0, 1, 2, 3 au lieu de 1, 2, 3, 4).
- Déplacement de la méthode "entrerNom()" de la classe Jeu à la classe Main pour plus de logique.

(build 180512.1) :
- Ajout des tests "assert".



v1.1 (build 180509.1) :
Version SuperStable.
- Correction du positionnement aléatoire des cases spéciales.
- Autres corrections dans le code.



v1.0 (build 180428.1) :
Version stable.
- Divers corrections dans les affichages.
- Prise en compte de l'entrée des minuscules.
- Correction de certaines régles du jeu mal gérées ou oubliées (assèchement des cases submergé,
  au plus 3 actions au lieu de 3 actions).
- Changement de la couleur de la terre (marron -> vert) pour
  éviter de la confondre avec le feu.

(build 180428.2) :
- Optimisation du code et écriture des commentaires des classes Dessin, Case et Grille.

(build 180429.3) :
- Fin de l'écriture des commentaires.
- Correction de certaine verification anti-erreur.



v0.5 (build 180427.1) :
- Dans Jeu :
-- Amélioration de l'affichage de la méthode "tour()".
-- Ajout de la boucle anti-erreur dans "tour()".
-- Ajout de sécurité anti-dépassement de grille dans "deplacement()".

- Dans Dessin :
-- La couleur de l'héliport peux changer selon le niveau de la case.
-- Changement complet du stockage des couleurs des cases pour rendre plus lisible le code.
-- Ajout des couleurs des cases artefacts.
-- Ajout d'un calcul dans "calculMarge()" permettant de prendre en compte les grilles rectangulaire.

- Ajout de verifications sur toutes les entrées utilisateur.

(build 180427.2) :
- Dans Dessin :
-- Ajout d'une fenetre complète d'aide (création des méthodes "drawNotice()" et "afficherAide()").

- Dans Jeu :
-- Ajout de la commande pour afficher l'aide.
-- Ajout de la méthode "verifPerdu()" pour verifier si le joueur à perdu.
-- Ajout de la verification de partie gagnante.

- Dans Grille :
--Ajout d'une méthode et d'un attribut pour faciliter l'analyse des cases spéciales.



v0.4 (build 180426.1) :
- Dans Dessin :
-- Suppression du tableau fait auparavant.
-- Reprise de ma fonction "création_grille()" de PySnake et adaptation pour ce projet en méthode "drawGrille()".
-- Modification de cette méthode pour prendre en compte les int de drawLine() (tkinter prenant des floats donc pas de pb)
	(sinon, dépassement des lignes en bas et à droite donc modification des y1temp).
-- Création de la méthode "calculMarge()" permettant de centrer la grille et de la rendre orthonormal.

(build 180426.2) :
- Dans Dessin :
-- Ajout de la méthode "drawCases()" qui permet de dessinner les cases avec couleurs qui dépendent de la
	situation de la case.
-- Ajout d'attribut Joueur et modification du constructeur.
-- Ajout de la méthode "drawJoueur()" qui permet de placer les pions selon le nombre de joueur sur la case.
-- Ajout de la méthode "placePions()" qui place les pions de la liste que l'on lui a envoyer.

(build 180426.3) :
- Dans Dessin :
-- Optimisation de "drawJoueur()" (liste de Figure au lieu de int).
-- Refonte de la méthode "placePions()" qui place maintenant un pion selon la position donnée.



v0.3 (build 180425.2) :
- Dans Dessin :
-- Désormais, étend JPanel.
-- Ajout de la méthode permettant d'afficher une fenètre.
-- Ajout d'une méthode qui dessinne dans cette fenètre.
-- Base d'un dessin d'un tableau fixe composé de rectangles.



v0.2 (build 180414.2) :
- Accesseurs ajoutés.

- Création des classes :
-- Jeu
-- Dessin

- Ajout de la méthode "tour()" dans Jeu créée selon les régles du jeu.
- Ajout de définitions de méthode dans Dessin, à remplir plus tard.



v0.1 (build 180409.4) :
- Création des classes principales et remplissage avec des méthodes/attibuts triviaux :
-- Grille
-- Case
-- Joueur
-- Main

- Méthodes et attributs principaux ajoutés.

 */