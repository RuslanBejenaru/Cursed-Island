import java.security.Key;

public class Player {
    private String nom;
    private int [] keys;
    private boolean[] artéfact;
    private Case Player;
    private int rôle;


    public Player(String nom, int rôle){
        this.nom = nom;
        this.artéfact = new boolean[4];
        this.keys = new int[]{0, 0, 0, 0};
        this.rôle = rôle;
        return;
    }

    public boolean ajouteKeys(int key){
        if (key > 3 || key < 0)
            return false;
        this.keys[key]++;
        return true;
    }

    public boolean supprimeKeys(int key){
        if (key > 3 || key < 0 || (keys[key] == 0))
            return false;
        this.keys[key]--;
        return true;
    }
    public int ajouteArtéfact(int artéfact){
        if (artéfact > 3 || artéfact < 0)
            return 1;
        this.artéfact[artéfact] = true;
        return 0;
    }

    public void casePlayer(Case Player){
        this.Player= Player;
    }

    public String getNom() {
        return nom;
    }

    public int getKeys( int k) {
        return keys[k];
    }

    public boolean[] getArtéfact() {
        return artéfact;
    }

    public Case getPlayer() {
        return Player;
    }

    public int getRôle() {
        return rôle;
    }

    public int getPosx() { return Player.getCoordX();}

    public int getPosy() { return Player.getCoordY();}

public boolean verifKey(int key){
    if(key > 3 || key < 0) return false;
    return keys[key] > 0 ? true : false;
}

    public boolean verifArtefact(int artéfact){
        if(artéfact > 3 || artéfact < 0) return false;
        return this.artéfact[artéfact];
}


}
