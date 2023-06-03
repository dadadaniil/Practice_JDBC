public class LenNumPair {
    private int len;
    private int num;

    public LenNumPair(double len, double num) {
        this.len = getDiff(len, num);
        this.num = 1;
    }

    public LenNumPair() {
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    private int round(double number) {
        return Math.round((long) number);
    }

    @Override
    public String toString() {
        return "len=" + len + ", num=" + num;
    }

    private int getDiff(double x1, double x2) {
        return round(Math.abs(x2 - x1));
    }
}
