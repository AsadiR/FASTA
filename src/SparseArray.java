
import java.util.*;
import java.util.function.Consumer;

public class SparseArray<E>  implements Iterable<E> {
    private HashMap<Pair<Integer,Integer>, E> map;

    @Override
    public Iterator<E> iterator() {
        return map.values().iterator();
    }

    @Override
    public void forEach(Consumer<? super E> action) {

    }

    @Override
    public Spliterator<E> spliterator() {
        return null;
    }

    public SparseArray() {
        super();
        map = new HashMap<Pair<Integer,Integer>, E>();
    }

    public Collection<E> getNotNullValues() {
        return map.values();
    }

    public E get(int index1, int index2) {
        return map.get(new Pair<Integer, Integer>(index1,index2));
    }
    public void set(int index1, int index2, E value) {
        map.put(new Pair<Integer, Integer>(index1, index2), value);
    }
}
