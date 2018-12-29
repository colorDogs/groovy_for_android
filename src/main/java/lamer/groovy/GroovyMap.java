package lamer.groovy;

import android.text.TextUtils;

import java.util.Map;

/**
 * Created by lamer on 2018/12/30 01:31
 * <p>
 * mail: 157688302@qq.com
 */
public class GroovyMap {


    public static <K, V> String join(Map<K, V> map) {
        return mapToString(map);
    }


    public static <K, V> String join(Map<K, V> map, MapTransfer<K, V> transfer) {
        return mapToString(map, transfer);
    }

    public static <K, V> String mapToString(Map<K, V> map) {
        return mapToString(map, new MapTransfer<K, V>() {
            @Override
            public String mapToString(K key, V value) {
                return key.toString() + ":" + value.toString();
            }

            @Override
            public String separator() {
                return ",";
            }

            @Override
            public String start() {
                return "[";
            }

            @Override
            public String end() {
                return "]";
            }
        });
    }

    public static <K, V> String mapToString(Map<K, V> map, MapTransfer<K, V> transfer) {
        StringBuilder builder = new StringBuilder();

        String separator = transfer.separator();
        if (TextUtils.isEmpty(separator)) {
            separator = "";
        }

        String stringsStart = transfer.start();
        String stringsEnd = transfer.end();

        if (!TextUtils.isEmpty(stringsStart)) {
            builder.append(stringsStart);
        }

        for (Map.Entry<K, V> entry : map.entrySet()) {
            builder.append(transfer.mapToString(entry.getKey(), entry.getValue()));
            builder.append(separator);
        }

        String message = builder.substring(0, builder.length() - separator.length());

        if (TextUtils.isEmpty(stringsEnd)) {
            return message;
        } else {
            return message + stringsEnd;
        }
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null
                || map.isEmpty();
    }

    public static <K, V> void each(Map<K, V> map, MapEach<K, V> each) {
        if (isEmpty(map)) {
            return;
        }

        for (Map.Entry<K, V> entry : map.entrySet()) {
            each.each(entry.getKey(), entry.getValue());
        }
    }

    public interface MapEach<K, V> {
        void each(K key, V value);
    }


    public interface MapTransfer<K, V> {

        String mapToString(K key, V value);

        String separator();

        String start();

        String end();
    }
}
