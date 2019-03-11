package lamer.groovy;

import java.io.File;
import java.util.List;

/**
 * Created by chaos on 2018/7/26 17:08
 * <p>
 * mail: 157688302@qq.com
 */
public class GroovyFile {

    public static boolean isExists(File file) {
        return file != null
                && file.exists();
    }

    public static void eachFile(File file, FileEach each) {
        eachFile(file, FileType.ANY, each);
    }

    public static void eachFile(File file, final FileType type, FileEach each) {
        if (isExists(file)) {
            File[] files = file.listFiles();
            List<File> array = GroovyArray.grep(files, new GroovyArray.ArrayFilter<File>() {
                @Override
                public boolean grep(File data) {
                    if (type == FileType.FILES) {
                        return data.isFile();
                    } else if (type == FileType.DIRECTORIES) {
                        return data.isDirectory();
                    } else if (type == FileType.ANY) {
                        return true;
                    } else {
                        return false;
                    }
                }

                @Override
                public boolean termination(File data) {
                    return false;
                }
            });
            GroovyArray.each(array, each);
        }
    }

    public static void eachFileRecurse(final File file, final FileEach each) {
        eachFileRecurse(file, FileType.ANY, each);
    }

    public static void eachFileRecurse(final File file, final FileType type, final FileEach each) {
        eachFile(file, FileType.ANY, data -> {
            if (data.isDirectory()) {
                eachFileRecurse(data, FileType.ANY, each);
            }

            if (type == FileType.FILES) {
                if (data.isFile()) {
                    each.each(data);
                }
            } else if (type == FileType.DIRECTORIES) {
                if (data.isDirectory()) {
                    each.each(data);
                }
            } else {
                each.each(data);
            }
        });
    }

    public interface FileEach extends GroovyArray.ArrayEach<File> {

    }

    public enum FileType {
        FILES,
        DIRECTORIES,
        ANY
    }
}
