package com.matih.auctionsystem.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.matih.auctionsystem.Classes.AuctionItem;
import com.matih.auctionsystem.R;
import com.matih.auctionsystem.Utils.DateUtils;

import java.util.List;

/**
 * Created by Matih on 25/2/2015.
 */
public class ItemsListViewAdapter extends BaseAdapter {

    private List<AuctionItem> auctionItems;

    @Override
    public int getCount() {
        return auctionItems.size();
    }

    @Override
    public Object getItem(int position) {
        return auctionItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.cell_auction_item, parent, false);
        }

        TextView txtView_ItemName = (TextView)convertView.findViewById(R.id.textView_cell_item_name);
        TextView txtView_ItemDescription = (TextView)convertView.findViewById(R.id.textView_cell_item_description);
        TextView txtView_ItemPrice = (TextView)convertView.findViewById(R.id.textView_cell_item_price);
        TextView txtView_TimeLeft = (TextView)convertView.findViewById(R.id.textView_cell_timeLeft);
        ImageView imgView_ItemImage = (ImageView)convertView.findViewById(R.id.imageView_auction_item);

        txtView_ItemName.setText(auctionItems.get(position).getItemName());
        txtView_ItemDescription.setText(auctionItems.get(position).getItemDescription());
        txtView_ItemPrice.setText(convertView.getResources().getString(R.string.symbol_currency)
                                    + String.valueOf(auctionItems.get(position).getPrice()));

        // Get time left string
        String timeLeftString = DateUtils.getTimeLeftString(auctionItems.get(position).getExpiry());
        if(auctionItems.get(position).isSold()){
            imgView_ItemImage.setImageResource(R.drawable.aution_sold);
            timeLeftString = "Sold";
        }
        else{
            imgView_ItemImage.setImageResource(R.drawable.aution_current);
        }
        txtView_TimeLeft.setText(timeLeftString);

        return convertView;
    }

    public void setAuctionItems(List<AuctionItem> auctionItems) {
        this.auctionItems = auctionItems;
    }
}
