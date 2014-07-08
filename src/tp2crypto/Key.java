package tp2crypto;

public class Key {
    private final int e;
    private final int n;

    public Key(int e, int n) {
        this.e =e;
        this.n = n;
    }

    public int getE() {
        return e;
    }

    public int getN() {
        return n;
    }
}
