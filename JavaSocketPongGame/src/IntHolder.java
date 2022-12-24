public class IntHolder {
    private int value = 0;

    public IntHolder() {
    }

    public IntHolder(int value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return "" + value;
    }
}