package tp2crypto;

public class FakeGenerator extends Generator{
    private final String ret;

    public FakeGenerator(String ret) {
        this.ret = ret;
    }

    @Override
    public String genRandomK() {
        return ret;
    }

    @Override
    public String genRandomN() {
        return ret;
    }

    @Override
    public String genRandomIV() {
        return ret;
    }
}
