package my.simple.keykeeper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import my.simple.keykeeper.R;
import my.simple.keykeeper.data.entity.Category;

import java.util.List;

/**
 * Created by Alex on 26.05.2014.
 */
public class CategoryAdapter extends ArrayAdapter<Category> {

    private final Context context;
    private final List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        super(context, R.layout.category_item, categories);
        this.context = context;
        this.categories = categories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.category_item, parent, false);
        }
        TextView categoryName = (TextView)convertView.findViewById(R.id.category_item_name);
        categoryName.setText(categories.get(position).getName());
        return convertView;
    }
}
