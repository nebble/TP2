package tp2crypto;

import tp2crypto.Generator;

public class FakeGenerator extends Generator{
    private final String k;
    private final String n;
    private final String iv;

    public FakeGenerator(String ret) {
        this(ret, ret, ret);
    }
    
    public FakeGenerator(String k, String n, String iv) {
        this.k = k;
        this.n = n;
        this.iv = iv;
    }

    @Override
    public String genRandomK() {
        return k;
    }

    @Override
    public String genRandomN() {
        return n;
    }

    @Override
    public String genRandomIV() {
        return iv;
    }
}
