package lamer.groovy;

import java.util.Set;

/**
 * Created by lamer on 2018/12/30 01:31
 * <p>
 * mail: 157688302@qq.com
 */
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
