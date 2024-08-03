package com.example.listvoz;


import static com.example.listvoz.MainActivity.FILE_NAME;
import static com.example.listvoz.MainActivity.ITEMS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;
import java.util.stream.Collectors;

public class CustomAdapter extends ArrayAdapter<String> {

    public CustomAdapter(Context context, List<String> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.item_text);
        ImageButton deleteButton = convertView.findViewById(R.id.delete_button);

        final String item = getItem(position);
        textView.setText(item);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(item);
                removerItem(item);
                notifyDataSetChanged();
            }

            private void removerItem(String itemDeletar) {
                List<String> itensPermanecem = ITEMS.stream()
                    .filter(item -> !item.equals(itemDeletar)).collect(Collectors.toList());
                ITEMS.clear();
                ITEMS.addAll(itensPermanecem);
            }
        });

        return convertView;
    }
}
