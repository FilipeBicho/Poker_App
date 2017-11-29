package com.filipebicho.poker;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Flip on 04/11/2017.
 * MenuOptionAdapter is an ArrayAdapter that can provide the layout for each list item based on a
 * data source, which is a list of MenuOption objects
 */
public class MenuOptionAdapter extends ArrayAdapter<MenuOption>{
    /**
     * Class Constructor
     * @param context is the current context (i.e Activity) that the adapter is being created in.
     * @param menuOptions is the list of menu options to be display
     */
    MenuOptionAdapter(Context context, ArrayList<MenuOption> menuOptions)
    {
        super(context,0, menuOptions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.option_item, parent, false);
        }

        // Get the menu option object located at this position in the list
        MenuOption currentItem = getItem(position);

        // Find the LinearLayout in the option_item.xml layout
        LinearLayout option_background = listItemView.findViewById(R.id.menu_layout);
        option_background.setBackgroundResource(currentItem.getMenuBackground());

        // Find the ImageView in the option_item.xml layout
        ImageView option_icon = listItemView.findViewById(R.id.icon);
        option_icon.setImageResource(currentItem.getMenuIcon());

        // Find the TextView in the option_item.xml layout
        TextView option_text = listItemView.findViewById(R.id.text);
        option_text.setText(currentItem.getMenuName());

        // Return the whole list item layout (containing 2 TextView) so that it can be show in the
        //list View
        return listItemView;
    }


}
