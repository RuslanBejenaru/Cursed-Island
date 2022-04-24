public class Case {
    private boolean heliport;
    private int Artéfact;
    private int nivMarin;
    private int coordX, coordY;

    public Case(boolean heliport, int artéfact, int coordX, int coordY) {
        this.heliport = isHeliport();
        Artéfact = artéfact;
        this.nivMarin = nivMarin;
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public int inondation(){
        if(nivMarin >= 2)
            return 1;
        nivMarin++;
        return 0;
    }

    public int assèchement(){
        if (nivMarin <= 0)
            return 1;
        nivMarin--;
        return 0;
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
