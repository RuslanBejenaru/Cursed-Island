import java.util.ArrayList;
import java.util.Random;

public class Deck {
    protected ArrayList<Integer> deck;
    protected ArrayList<Integer> deckDefausse;
    private Random hazard = new Random();

    public Deck(int taille){
        deck = new ArrayList<>();
        deckDefausse = new ArrayList<>();

        for (int i = 0; i < taille; i++)
            deck.add(i);

        melangeDeck();
    }


    public void melangeDeck(){
        ArrayList<Integer> temp = new ArrayList<>(deck.size());

        int draw;

        while (!deck.isEmpty()){
            draw = hazard.nextInt(deck.size());

            temp.add(deck.get(draw));

            deck.remove(draw);
        }
        deck.addAll(temp);
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
        ArrayList<Integer> temp = new
    }


}
