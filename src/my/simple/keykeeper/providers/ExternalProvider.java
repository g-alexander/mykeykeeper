package my.simple.keykeeper.providers;

import my.simple.keykeeper.data.entity.FileDialogItem;

import java.io.File;
import java.util.List;

/**
 * Created by Alex on 16.09.2014.
 */
public interface ExternalProvider {
    public String getCurrentPath();
    public List<FileDialogItem> getCurrentData();
    public boolean changePath(FileDialogItem newPath);
    public boolean goUp();
}
