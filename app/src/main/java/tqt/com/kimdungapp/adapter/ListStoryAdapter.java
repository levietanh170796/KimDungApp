package tqt.com.kimdungapp.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import tqt.com.kimdungapp.R;
import tqt.com.kimdungapp.entry.Story;

/**
 * Created by Admin on 6/2/2017.
 */

public class ListStoryAdapter extends ArrayAdapter<Story> {

    Activity context;

    public ListStoryAdapter(Activity context, int resource, List<Story> objects) {
        super(context, resource, objects);
        this.context= context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_list_story,null);
        }

        TextView txtName =(TextView)convertView.findViewById(R.id.text_title_item);

        Story item = getItem(position) ;

        txtName.setText(item.getStName());




        return convertView;
    }
}
