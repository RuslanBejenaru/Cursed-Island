import java.util.ArrayList;
import java.util.Random;

// ------------------------
// Classe Deck qui représente un paquet de carte, carte représenté par des valeurs.
// ------------------------


public class Deck {
    protected ArrayList<Integer> deck;
    protected ArrayList<Integer> deckDefausse;
    private Random hazard = new Random();

    /*
     * Constructeur
     * @param size : la taille du paquet.
     */

    public void mixDeck(){

        // On cree un deck temporaire
        ArrayList<Integer> temp = new ArrayList<>(deck.size());

        // On sauvegarde
        int draw;

        while (!deck.isEmpty()){
            draw = hazard.nextInt(deck.size());

            temp.add(deck.get(draw));

            deck.remove(draw);
        }
        deck.addAll(temp);
    }


    public Deck(int size){
        deck = new ArrayList<>();
        deckDefausse = new ArrayList<>();

        // On met size nombres
        for (int i = 0; i < size; i++)
            deck.add(i);

        // On melange le deck
        mixDeck();
    }



    public int firstCard(){
        if(deck.isEmpty()) melangeDeckDefausse();

        int card = deck.get(deck.size() - 1);

        deck.remove(deck.size() - 1);

        versDefausse(card);

        return card;
    }

    public void versDefausse(int card){
        deckDefausse.add(card);
    }

    public void melangeDeckDefausse(){
        ArrayList<Integer> temp = new ArrayList<>(deckDefausse.size());
        int draw;
        while (!deckDefausse.isEmpty()){
            draw = hazard.nextInt(deckDefausse.size());
            temp.add(deckDefausse.get(draw));
            deckDefausse.remove(draw);
        }
        deck.addAll(temp);
    }


}
