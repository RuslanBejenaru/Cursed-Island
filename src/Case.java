public class Case {
    private boolean heliport;
    private int Artefact;
    private int nivMarin = 2;
    private int coordX, coordY;

    public Case(boolean isHeliport, int Artefact, int coordX, int coordY) {
        this.heliport = isHeliport;
        this.Artefact = Artefact;
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
        Artefact = 0;
    }



    public boolean isHeliport() {
        return heliport;
    }

    public int getArtéfact() {
        return Artefact;
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
