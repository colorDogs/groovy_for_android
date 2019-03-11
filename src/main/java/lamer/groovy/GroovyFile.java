package lamer.groovy;

import android.text.TextUtils;

import java.io.File;
import java.util.List;

import lamer.groovy.utils.FileIOUtils;

/**
 * Created by chaos on 2018/7/26 17:08
 * <p>
 * mail: 157688302@qq.com
 */
public class GroovyFile {

    private static final boolean NOT_USE_CACHED = false;

    private String mPath;
    private Cached mCached;

    public GroovyFile(String path) {
        this.mPath = path;
    }

    public boolean isExists() {
        return isExists(new File(mPath));
    }

    public String text(boolean useCached) {
        if (!isExists()) {
            return null;
        } else {
            return getCacheOrWait(mPath, useCached).text;
        }
    }

    public String text() {
        return text(NOT_USE_CACHED);
    }

    public byte[] bytes(boolean useCached) {
        if (!isExists()) {
            return null;
        } else {
            return getCacheOrWait(mPath, useCached).bytes;
        }
    }

    public byte[] bytes() {
        return bytes(NOT_USE_CACHED);
    }

    public synchronized boolean setText(String text) {
        return FileIOUtils.writeFileFromString(mPath, text);
    }

    public synchronized boolean setBytes(byte[] bytes) {
        return FileIOUtils.writeFileFromBytesByStream(mPath, bytes);
    }

    public void eachLine(GroovyArray.ArrayEach<String> text) {
        eachLine(text, NOT_USE_CACHED);
    }

    public void eachLine(GroovyArray.ArrayEach<String> each, boolean useCached) {
        String text = text(useCached);
        if (TextUtils.isEmpty(text)) {
            return;
        }

        String token = "\n";

        if (text.contains(token)) {
            String[] args = text.split(token);
            for (String arg : args) {
                each.each(arg);
            }
        } else {
            each.each(text);
        }
    }


    public synchronized Cached getCacheOrWait(String path, boolean forceCreated) {
        if (mCached == null
                || forceCreated) {
            //create cached
            byte[] bytes = FileIOUtils.readFile2BytesByChannel(path);
            mCached = new Cached(bytes);
        }
        return mCached;
    }


    /**
     * only support utf-8
     */
    private class Cached {
        public final byte[] bytes;
        public final String text;

        private Cached(byte[] bytes) {
            this.bytes = bytes;
            this.text = new String(this.bytes);
        }
    }

    //----------------------- static method -----------------------

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
