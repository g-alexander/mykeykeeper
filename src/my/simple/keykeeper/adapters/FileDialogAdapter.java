package my.simple.keykeeper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import my.simple.keykeeper.R;
import my.simple.keykeeper.data.entity.FileDialogItem;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Alex on 15.09.2014.
 */
public class FileDialogAdapter extends ArrayAdapter<FileDialogItem> {

    private final List<FileDialogItem> data;
    private final Context context;

    private final String directory;
    private final String changed;
    private final String up;

    private final DateFormat shortDateFormat;


    public FileDialogAdapter(Context context, List<FileDialogItem> data) {
        super(context, R.layout.file_dialog_item, data);
        this.data = data;
        this.context = context;
        shortDateFormat = android.text.format.DateFormat.getDateFormat(context.getApplicationContext());
        directory = context.getResources().getString(R.string.directory);
        changed = context.getResources().getString(R.string.changed);
        up = context.getResources().getString(R.string.up);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.file_dialog_item, parent, false);
        }
        TextView header = (TextView)convertView.findViewById(R.id.item_file_name);
        TextView change = (TextView)convertView.findViewById(R.id.item_file_change_data);
        ImageView image = (ImageView)convertView.findViewById(R.id.item_image);

        FileDialogItem item = data.get(position);

        header.setText(item.getName());
        if (item.isDirectory()) {
            change.setText(this.directory);
            image.setImageResource(R.drawable.folder);
        }
        if (item.isFile()) {
            if (item.getChanged() != null) {
                change.setText(this.changed + " " + shortDateFormat.format(data.get(position).getChanged()));
            }
            image.setImageResource(R.drawable.file);
        }
        if (item.isUp()) {
            header.setText(up);
            change.setText("");
            image.setImageResource(R.drawable.up);
        }
        return convertView;
    }
}
