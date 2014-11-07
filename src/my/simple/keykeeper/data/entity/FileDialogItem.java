package my.simple.keykeeper.data.entity;

import java.util.Date;

/**
 * Created by Alex on 16.09.2014.
 */
public class FileDialogItem {
    private boolean directory;
    private boolean file;
    private boolean up;
    private Date changed;
    private String name;

    public FileDialogItem() {
    }

    public FileDialogItem(boolean directory, boolean file, boolean up, String name, Date changed) {
        this.directory = directory;
        this.file = file;
        this.changed = changed;
        this.name = name;
        this.setUp(up);
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public boolean isFile() {
        return file;
    }

    public void setFile(boolean file) {
        this.file = file;
    }

    public Date getChanged() {
        return changed;
    }

    public void setChanged(Date changed) {
        this.changed = changed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }
}
