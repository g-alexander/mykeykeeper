package my.simple.keykeeper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import my.simple.keykeeper.R;
import my.simple.keykeeper.data.entity.KeyRecord;

import java.util.List;

/**
 * Created by Alex on 16.05.2014.
 */
public class KeyRecordAdapter extends ArrayAdapter<KeyRecord> {

    private Context context;
    private List<KeyRecord> records;

    public KeyRecordAdapter(Context context, List<KeyRecord> records) {
        super(context, R.layout.list_item, records);
        this.context = context;
        this.records = records;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }
        final TextView itemName = (TextView)convertView.findViewById(R.id.item_name);
        final TextView itemCategory = (TextView)convertView.findViewById(R.id.item_category);
        final TextView itemLogin = (TextView)convertView.findViewById(R.id.item_login);

        itemName.setText(this.records.get(position).getName());
        itemCategory.setText(this.records.get(position).getCategory().getName());
        itemLogin.setText(this.records.get(position).getLogin());

        return convertView;
    }
}
