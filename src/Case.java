public class Case {
    private boolean heliport;
    private int Artéfact;
    private int nivMarin;
    private int coordX, coordY;

    public Case(boolean heliport, int artéfact, int nivMarin, int coordX, int coordY) {
        this.heliport = isHeliport();
        Artéfact = artéfact;
        this.nivMarin = nivMarin;
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public boolean inondation(){
        if(nivMarin >= 2)
            return true;
        nivMarin++;
        return false;
    }

    public boolean assèchement(){
        if (nivMarin <= 0)
            return true;
        nivMarin--;
        return false;
    }

    public void ramasserArtéfact(){
        Artéfact = 0;
        return;
    }



    public boolean isHeliport() {
        return heliport;
    }

    public int getArtéfact() {
        return Artéfact;
    }

    public int getNivMarin() {
        return nivMarin;
    }

    public int getCoordX() {
        return coordX;
    }

    public int getCoordY() {
        return coordY;
    }
}
