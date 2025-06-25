package game;

public class Skor {  
    private String username;
    private String oyunAdi;
    private double skor;

    public Skor(String username, String oyunAdi, double skor) {
        this.username = username;
        this.oyunAdi = oyunAdi;
        this.skor = skor;
    }

    public String getUsername() {
        return username;
    }

    public String getOyunAdi() {
        return oyunAdi;
    }

    public double getSkor() {
        return skor;
    }

    @Override
    public String toString() {
        return oyunAdi + ": " + skor;
    }
}
