package lamer.groovy;

import java.util.Set;

public class GroovySet {

    public static <T> void each(Set<T> ds, final SetEach<T> each) {
        if (isEmpty(ds)) {
            return;
        }

        for (T data : ds) {
            each.each(data);
        }
    }

    public interface SetEach<T> {
        void each(T data);
    }

    public static <T> boolean isEmpty(Set<T> ds) {
        return ds == null || ds.isEmpty();
    }

}
