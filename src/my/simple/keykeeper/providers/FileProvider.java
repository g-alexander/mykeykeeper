package my.simple.keykeeper.providers;

import my.simple.keykeeper.data.entity.FileDialogItem;

import java.io.File;
import java.util.*;

/**
 * Created by Alex on 16.09.2014.
 */
public class FileProvider implements ExternalProvider {

    private String currentPath = "/sdcard";

    @Override
    public String getCurrentPath() {
        return currentPath;
    }

    @Override
    public List<FileDialogItem> getCurrentData() {
        List<FileDialogItem> res = new LinkedList<FileDialogItem>();
        try {
            File path = new File(currentPath);
            List<File> files = new LinkedList<File>(Arrays.asList(path.listFiles()));
            Collections.sort(files, new Comparator<File>() {
                @Override
                public int compare(File file, File file2) {
                    if (file.isDirectory() && !file2.isDirectory()) {
                        return -1;
                    }
                    if (!file.isDirectory() && file2.isDirectory()) {
                        return 1;
                    }
                    return file.getName().compareTo(file2.getName());
                }
            });
            if (!currentPath.equals("/")) {
                res.add(new FileDialogItem(false, false, true, "", null));
            }
            for (File file : files) {
                if (file.isDirectory() || file.isFile()) {
                    res.add(new FileDialogItem(file.isDirectory(), file.isFile(), false, file.getName(), new Date(file.lastModified())));
                }
            }
            return res;
        } catch (Exception e) {
            if (res.isEmpty()) {
                res.add(new FileDialogItem(false, false, true, "", null));
            }
            return res;
        }
    }

    @Override
    public boolean changePath(FileDialogItem newPath) {
        if (newPath.isUp()) {
            goUp();
        } else {
            currentPath += "/" + newPath.getName();
        }
        return true;
    }

    @Override
    public boolean goUp() {
        if (currentPath.equals("/")) {
            return false;
        }
        int pos = currentPath.lastIndexOf("/");
        if (pos > -1) {
            if (pos == 0) {
                pos = 1;
            }
            currentPath = currentPath.substring(0, pos);
        }
        return true;
    }
}
