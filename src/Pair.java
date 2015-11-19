
public class Pair<C1,C2> {
    public C1 value1;
    public C2 value2;
    public Pair(C1 value1, C2 value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    public int hashCode() {
        return value1.hashCode() + value2.hashCode();
    }

    @Override
    public String toString() {
        return "("+value1+","+value2+")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair) {
            Pair pObj = (Pair) obj;
            if (pObj.value1.getClass() != value1.getClass() || pObj.value2.getClass() != value2.getClass())
                return false;
            return pObj.value1.equals(value1) && pObj.value2.equals(value2);
        } else {
            return false;
        }
    }
}
