package lamer.groovy;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 实现Groovy中对于Array的匕首方法
 * 例如
 * {@link #join(Object[], String)} -> 数组变成String
 * {@link #collect(Object[], ArrayCollectTransform)} -> 数组变成另一个结构的数据的方法
 * {@link #each(Object[], ArrayEach)} -> 遍历数组
 * {@link #eachReverse(Object[], ArrayEach)} -> 反向遍历数组
 * {@link #find(Object[], ArrayFinder)} -> 从数组中查找
 * {@link #findIndex(Object[], ArrayFinder)} -> 找到索引
 * {@link #grep(Object[], ArrayFilter)} -> 过滤数组
 * {@link #plus(Object[][])} -> 数组相加
 * {@link #first(Object[])} {@link #last(Object[])} 获得数组第一个和最后一个
 * {@link #isEmpty(Object[])} {@link #count(List)} 数组是否为空以及数组数量
 * {@link #inject(Object[], ArrayInject)} 数组内元素相加
 * {@link #every(Object[], ArrayElementCondition)} -> 如果数组中每个元素都满足{@link ArrayElementCondition#condition(int, Object)}，则为true，否则为false.
 * {@link #any(Object[], ArrayElementCondition)} -> 如果数组中任一元素满足{@link ArrayElementCondition#condition(int, Object)}，则为true，否则为false.
 * {@link #range(int, int)} 创建一个指定范围内的数据
 * {@link #rangeEach(int, int, ArrayEach)} 创建一个指定范围内的数据，并可迭代实现
 *
 */
public class GroovyArray {

    // -------------------- JOIN -----------------------------

    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>List<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    public static <T> String join(List<T> ds, String separator) {
        return join(ds, separator, null);
    }

    public static <T> String join(List<T> ds, String separator, ArrayNameTransform<T> transform) {
        if (isEmpty(ds)) {
            return null;
        }

        final int count = ds.size();
        StringBuilder builder = new StringBuilder(count * 2 - 1);
        for (int i = 0; i < count; i++) {
            T data = ds.get(i);
            String named;
            if (transform != null) {
                named = transform.transform(data);
            } else {
                named = data.toString();
            }
            builder.append(named);
            if (i != count - 1) {
                builder.append(separator);
            }
        }

        return builder.toString();
    }

    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>SparseArray<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T> String join(SparseArray<T> ds, String separator) {
        return join(ds, separator, null);
    }

    public static <T> String join(SparseArray<T> ds, String separator, ArrayNameTransform<T> transform) {
        if (isEmpty(ds)) {
            return null;
        }

        final int count = ds.size();
        StringBuilder builder = new StringBuilder(count * 2 - 1);
        for (int i = 0; i < count; i++) {
            T data = ds.get(i);
            if (data == null) {
                continue;
            }
            String named;
            if (transform != null) {
                named = transform.transform(data);
            } else {
                named = data.toString();
            }
            builder.append(named);
            if (i != count - 1) {
                builder.append(separator);
            }
        }

        return builder.toString();
    }
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>T[]<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T> String join(T[] ds, String separator) {
        return join(ds, separator, null);
    }

    public static <T> String join(T[] ds, String separator, ArrayNameTransform<T> transform) {
        if (isEmpty(ds)) {
            return null;
        }

        final int count = count(ds);
        StringBuilder builder = new StringBuilder(count * 2 - 1);
        for (int i = 0; i < count; i++) {
            T data = ds[i];
            if (data == null) {
                continue;
            }
            String named;
            if (transform != null) {
                named = transform.transform(data);
            } else {
                named = data.toString();
            }
            builder.append(named);
            if (i != count - 1) {
                builder.append(separator);
            }
        }

        return builder.toString();
    }

    // -------------------- GREP -----------------------------

    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>List<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T> List<T> grep(List<T> ds, ArrayFilter<T> filter) {
        return grep(ds, filter, null);
    }

    public static <T> List<T> grep(List<T> ds, ArrayFilter<T> filter, ArrayFactory<T> factory) {
        if (isEmpty(ds)) {
            return null;
        }

        List<T> array = newList(factory, ds.size());
        for (T data : ds) {
            if (filter.grep(data)) {
                array.add(data);
            }
            if (filter.termination(data)) {
                break;
            }
        }
        return array;
    }

    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>SparseArray<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T> List<T> grep(SparseArray<T> ds, ArrayFilter<T> filter) {
        return grep(ds, filter, null);
    }

    public static <T> List<T> grep(SparseArray<T> ds, ArrayFilter<T> filter, ArrayFactory<T> factory) {
        if (isEmpty(ds)) {
            return null;
        }

        List<T> array = newList(factory, ds.size());
        for (int i = 0; i < ds.size(); i++) {
            T data = ds.get(i);

            if (filter.grep(data)) {
                array.add(data);
            }
            if (filter.termination(data)) {
                break;
            }
        }
        return array;
    }

    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>T[]<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T> List<T> grep(T[] ds, ArrayFilter<T> filter) {
        return grep(ds, filter, null);
    }

    public static <T> List<T> grep(T[] ds, ArrayFilter<T> filter, ArrayFactory<T> factory) {
        if (isEmpty(ds)) {
            return null;
        }

        List<T> array = newList(factory, count(ds));
        for (T data : ds) {
            if (filter.grep(data)) {
                array.add(data);
            }
            if (filter.termination(data)) {
                break;
            }
        }
        return array;
    }

    // -------------------- FIRST-LAST -----------------------------
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>List<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T> T first(List<T> ds) {
        if (isEmpty(ds)) {
            return null;
        } else {
            return ds.get(0);
        }
    }

    public static <T> T last(List<T> ds) {
        if (isEmpty(ds)) {
            return null;
        } else {
            return ds.get(ds.size() - 1);
        }
    }

    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>SparseArray<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T> T first(SparseArray<T> ds) {
        if (isEmpty(ds)) {
            return null;
        } else {
            return ds.get(0);
        }
    }

    public static <T> T last(SparseArray<T> ds) {
        if (isEmpty(ds)) {
            return null;
        } else {
            return ds.get(ds.size() - 1);
        }
    }

    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>T[]<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T> T first(T[] ds) {
        if (isEmpty(ds)) {
            return null;
        } else {
            return ds[0];
        }
    }

    public static <T> T last(T[] ds) {
        if (isEmpty(ds)) {
            return null;
        } else {
            return ds[ds.length - 1];
        }
    }

    // -------------------- PLUS-SUB -----------------------------
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>List<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T> List<T> plus(List<T>... item) {
        if (item == null
                || item.length < 0) {

            return null;
        }

        if (item.length == 1) {
            return item[0];
        }

        final List<T> first = GroovyArray.first(item);
        GroovyArray.eachWithIndex(item,
                (List<T> data, int index) -> {
                    if (index != 0) {
                        first.addAll(data);
                    }
                }
        );

        return first;
    }

    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>SparseArray<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T> SparseArray<T> plus(SparseArray<T>... item) {
        if (item == null
                || item.length < 0) {

            return null;
        }

        if (item.length == 1) {
            return item[0];
        }

        final SparseArray<T> first = GroovyArray.first(item);
        GroovyArray.eachWithIndex(item, (array, index) -> {
            if (index != 0) {
                GroovyArray.eachWithIndex(array, (data, index1) -> first.put(array.keyAt(index1), data));
            }
        });
        return first;
    }


    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>T[]<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T> T[] plus(T[]... item) {
        if (item == null
                || item.length < 0) {

            return null;
        }

        if (item.length == 1) {
            return item[0];
        }

        final int[] count = new int[1];

        GroovyArray.each(item, data -> count[0] += data.length);

        final Object[] objs = new Object[count[0]];

        final int[] index = new int[1];
        index[0] = 0;
        GroovyArray.each(item, data -> each(data, data1 -> {
            objs[index[0]] = data1;
            index[0] = index[0] + 1;
        }));

        return (T[]) objs;
    }

    // -------------------- COLLECT -----------------------------
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>List<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T, R> List<R> collect(List<T> ds, ArrayCollectTransform<T, R> transform) {
        return collect(ds, transform, null);
    }

    public static <T, R> List<R> collect(List<T> ds, ArrayCollectTransform<T, R> transform, ArrayFactory<R> factory) {
        if (isEmpty(ds)) {
            return null;
        } else {
            List<R> collects = newList(factory, ds.size());
            for (T data : ds) {
                R collect = transform.transform(data);
                if (collect != null) {
                    collects.add(collect);
                }
            }
            return collects;
        }
    }
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>SparseArray<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T, R> List<R> collect(SparseArray<T> ds, ArrayCollectTransform<T, R> transform) {
        return collect(ds, transform, null);
    }

    public static <T, R> List<R> collect(SparseArray<T> ds, final ArrayCollectTransform<T, R> transform, ArrayFactory<R> factory) {
        if (isEmpty(ds)) {
            return null;
        } else {
            final List<R> collects = newList(factory, ds.size());
            each(ds, data -> {
                R collect = transform.transform(data);
                if (collect != null) {
                    collects.add(collect);
                }
            });
            return collects;
        }
    }
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>T[]<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T, R> List<R> collect(T[] ds, ArrayCollectTransform<T, R> transform) {
        return collect(ds, transform, null);
    }

    public static <T, R> List<R> collect(T[] ds, final ArrayCollectTransform<T, R> transform, ArrayFactory<R> factory) {
        if (isEmpty(ds)) {
            return null;
        } else {
            final List<R> collects = newList(factory, ds.length);
            each(ds, data -> {
                R collect = transform.transform(data);
                if (collect != null) {
                    collects.add(collect);
                }
            });
            return collects;
        }
    }

    // -------------------- INJECT -----------------------------
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>List<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T, R> T inject(List<R> ds, ArrayInject<T, R> inject) {
        if (isEmpty(ds)) {
            return null;
        }
        T resp = null;
        for (R data : ds) {
            resp = inject.plus(resp, data);
        }
        return resp;
    }
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>SparseArray<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T, R> T inject(SparseArray<R> ds, ArrayInject<T, R> inject) {
        if (isEmpty(ds)) {
            return null;
        }
        T resp = null;

        int count = ds.size();
        for (int i = 0; i < count; i++) {
            R data = ds.get(i);
            resp = inject.plus(resp, data);
        }
        return resp;
    }
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>T[]<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T, R> T inject(R[] ds, ArrayInject<T, R> inject) {
        if (isEmpty(ds)) {
            return null;
        }
        T resp = null;
        int count = ds.length;

        for (int i = 0; i < count; i++) {
            R data = ds[i];
            resp = inject.plus(resp, data);
        }
        return resp;
    }

    // -------------------- EACH -----------------------------
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>List<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T> void each(List<T> ds, final ArrayEach<T> each) {
        if (isEmpty(ds)) {
            return;
        }

        eachWithIndex(ds, (data, index) -> each.each(data));
    }

    public static <T> void eachWithIndex(List<T> ds, ArrayEachWithIndex<T> each) {
        if (isEmpty(ds)) {
            return;
        }

        final int count = ds.size();
        for (int i = 0; i < count; i++) {
            each.each(ds.get(i), i);
        }
    }

    public static <T> void eachReverse(List<T> ds, final ArrayEach<T> each) {
        if (isEmpty(ds)) {
            return;
        }

        eachReverseWithIndex(ds, (data, index) -> each.each(data));
    }

    public static <T> void eachReverseWithIndex(List<T> ds, ArrayEachWithIndex<T> each) {
        if (isEmpty(ds)) {
            return;
        }

        final int count = ds.size();
        for (int i = count - 1; i >= 0; i--) {
            each.each(ds.get(i), i);
        }
    }

    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>SparseArray<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T> void each(SparseArray<T> ds, final ArrayEach<T> each) {
        if (isEmpty(ds)) {
            return;
        }

        eachWithIndex(ds, (data, index) -> each.each(data));
    }

    public static <T> void eachWithIndex(SparseArray<T> ds, ArrayEachWithIndex<T> each) {
        if (isEmpty(ds)) {
            return;
        }

        final int count = ds.size();
        for (int i = 0; i < count; i++) {
            each.each(ds.get(i), i);
        }
    }

    public static <T> void eachReverse(SparseArray<T> ds, final ArrayEach<T> each) {
        if (isEmpty(ds)) {
            return;
        }

        eachReverseWithIndex(ds, new ArrayEachWithIndex<T>() {
            @Override
            public void each(T data, int index) {
                each.each(data);
            }
        });
    }

    public static <T> void eachReverseWithIndex(SparseArray<T> ds, ArrayEachWithIndex<T> each) {
        if (isEmpty(ds)) {
            return;
        }

        final int count = ds.size();
        for (int i = count - 1; i >= 0; i--) {
            each.each(ds.get(i), i);
        }
    }

    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>T[]<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T> void each(T[] array, final ArrayEach<T> each) {
        if (isEmpty(array)) {
            return;
        }

        eachWithIndex(array, (data, index) -> each.each(data));
    }

    public static <T> void eachWithIndex(T[] array, final ArrayEachWithIndex<T> each) {
        if (isEmpty(array)) {
            return;
        }

        final int count = array.length;
        for (int i = 0; i < count; i++) {
            each.each(array[i], i);
        }
    }

    public static <T> void eachReverse(T[] ds, final ArrayEach<T> each) {
        if (isEmpty(ds)) {
            return;
        }

        eachReverseWithIndex(ds, (data, index) -> each.each(data));
    }

    public static <T> void eachReverseWithIndex(T[] ds, ArrayEachWithIndex<T> each) {
        if (isEmpty(ds)) {
            return;
        }

        final int count = ds.length;
        for (int i = count - 1; i >= 0; i--) {
            each.each(ds[i], i);
        }
    }
    // -------------------- FIND -----------------------------
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>List<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T> T find(List<T> ds, final ArrayFinder<T> finder) {
        if (isEmpty(ds)) {
            return null;
        }

        final int count = ds.size();
        for (int i = 0; i < count; i++) {
            T data = ds.get(i);
            boolean find = finder.find(data);
            if (find) {
                return data;
            }
        }
        return null;
    }

    public static <T> int findIndex(List<T> ds, final ArrayFinder<T> finder) {
        if (isEmpty(ds)) {
            return -1;
        }

        final int count = ds.size();
        for (int i = 0; i < count; i++) {
            T data = ds.get(i);
            boolean find = finder.find(data);
            if (find) {
                return i;
            }
        }
        return -1;
    }
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>SparseArray<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T> T find(SparseArray<T> ds, final ArrayFinder<T> finder) {
        if (isEmpty(ds)) {
            return null;
        }

        final int count = ds.size();
        for (int i = 0; i < count; i++) {
            T data = ds.get(i);
            boolean find = finder.find(data);
            if (find) {
                return data;
            }
        }
        return null;
    }

    public static <T> int findIndex(SparseArray<T> ds, final ArrayFinder<T> finder) {
        if (isEmpty(ds)) {
            return -1;
        }

        final int count = ds.size();
        for (int i = 0; i < count; i++) {
            T data = ds.get(i);
            boolean find = finder.find(data);
            if (find) {
                return i;
            }
        }
        return -1;
    }
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>T[]<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static <T> T find(T[] array, final ArrayFinder<T> finder) {
        if (isEmpty(array)) {
            return null;
        }

        final int count = array.length;
        for (int i = 0; i < count; i++) {
            T data = array[i];
            boolean find = finder.find(data);
            if (find) {
                return data;
            }
        }
        return null;
    }

    public static <T> int findIndex(T[] ds, final ArrayFinder<T> finder) {
        if (isEmpty(ds)) {
            return -1;
        }

        final int count = ds.length;
        for (int i = 0; i < count; i++) {
            T data = ds[i];
            boolean find = finder.find(data);
            if (find) {
                return i;
            }
        }
        return -1;
    }

    // -------------------- COUNT-EMPTY -----------------------------
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>List<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    public static int count(List array) {
        if (isEmpty(array)) {
            return -1;
        } else {
            return array.size();
        }
    }

    public static <T> boolean isEmpty(List<T> ds) {
        return ds == null || ds.isEmpty();
    }

    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>SparseArray<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    public static int count(SparseArray array) {
        if (isEmpty(array)) {
            return -1;
        } else {
            return array.size();
        }
    }

    public static <T> boolean isEmpty(SparseArray<T> ds) {
        return ds == null || ds.size() <= 0;
    }

    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓>T[]<↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    public static <T> int count(T[] arrays) {
        if (isEmpty(arrays)) {
            return -1;
        } else {
            return arrays.length;
        }
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length <= 0;
    }


    // -------------------- INTERFACE -----------------------------

    public interface ArrayFinder<T> {
        boolean find(T it);
    }

    public interface ArrayEach<T> {
        void each(T it);
    }

    public interface ArrayEachWithIndex<T> {
        void each(T it, int index);
    }

    public interface ArrayInject<T, R> {
        T plus(T total, R it);
    }

    public abstract static class GrepFilter<T> implements ArrayFilter<T> {

        @Override
        public boolean termination(T it) {
            return false;
        }
    }

    public interface ArrayFilter<T> {
        boolean grep(T it);

        boolean termination(T it);
    }

    public interface ArrayNameTransform<T> {
        String transform(T it);
    }

    public interface ArrayCollectTransform<T, R> {
        R transform(T it);
    }

    public interface ArrayFactory<T> {
        List<T> newArray(int capacity);
    }

    public static <T> List<T> newList(ArrayFactory<T> factory, int capacity) {
        if (factory == null) {
            return new ArrayList<T>(capacity);
        } else {
            return factory.newArray(capacity);
        }
    }


    //range TODO support <step> mode
    public static List<Integer> range(int start, int end) {
        int count = end - start;
        if (count <= 0) {
            return null;
        }
        List<Integer> arrays = new ArrayList<>(count);
        for (int i = start; i < end; i++) {
            arrays.add(i);
        }
        return arrays;
    }

    public static boolean rangeEach(int start, int end, ArrayEach<Integer> each) {
        List<Integer> arrays = range(start, end);
        if (isEmpty(arrays)) {
            return false;
        } else {
            each(arrays, each);
            return true;
        }
    }

    public static <T> boolean any(List<T> arrays, ArrayElementCondition<T> condition) {
        if (isEmpty(arrays)) {
            return false;
        }

        boolean cond = false;

        final int count = count(arrays);
        for (int i = 0; i < count; i++) {
            T item = arrays.get(i);
            cond = condition.condition(i, item);
            if (cond) {
                break;
            }
        }

        return cond;
    }

    public static <T> boolean any(T[] arrays, ArrayElementCondition<T> condition) {
        if (isEmpty(arrays)) {
            return false;
        }

        boolean cond = false;

        final int count = count(arrays);
        for (int i = 0; i < count; i++) {
            T item = arrays[i];
            cond = condition.condition(i, item);
            if (cond) {
                break;
            }
        }

        return cond;
    }


    public static <T> boolean any(SparseArray<T> arrays, ArrayElementCondition<T> condition) {
        if (isEmpty(arrays)) {
            return false;
        }

        boolean cond = false;

        final int count = count(arrays);
        for (int i = 0; i < count; i++) {
            T item = arrays.get(i);
            cond = condition.condition(i, item);
            if (cond) {
                break;
            }
        }

        return cond;
    }


    public static <T> boolean every(List<T> arrays, ArrayElementCondition<T> condition) {
        if (isEmpty(arrays)) {
            return false;
        }

        boolean cond = true;

        final int count = count(arrays);
        for (int i = 0; i < count; i++) {
            T item = arrays.get(i);
            cond = condition.condition(i, item);
            if (!cond) {
                break;
            }
        }

        return cond;
    }

    public static <T> boolean every(T[] arrays, ArrayElementCondition<T> condition) {
        if (isEmpty(arrays)) {
            return false;
        }

        boolean cond = true;

        final int count = count(arrays);
        for (int i = 0; i < count; i++) {
            T item = arrays[i];
            cond = condition.condition(i, item);
            if (!cond) {
                break;
            }
        }

        return cond;
    }


    public static <T> boolean every(SparseArray<T> arrays, ArrayElementCondition<T> condition) {
        if (isEmpty(arrays)) {
            return false;
        }

        boolean cond = true;

        final int count = count(arrays);
        for (int i = 0; i < count; i++) {
            T item = arrays.get(i);
            cond = condition.condition(i, item);
            if (!cond) {
                break;
            }
        }

        return cond;
    }

    public interface ArrayElementCondition<T> {
        boolean condition(int index, T it);
    }
}
