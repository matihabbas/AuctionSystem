package com.matih.auctionsystem.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.matih.auctionsystem.Classes.AuctionItem;
import com.matih.auctionsystem.Classes.Bid;
import com.matih.auctionsystem.Managers.InitManager;
import com.matih.auctionsystem.R;
import com.matih.auctionsystem.Utils.DateUtils;

import java.text.DecimalFormat;
import java.util.Date;


public class AuctionItemDetailActivity extends Activity {

    private static final String DEBUG_TAG = AuctionItemDetailActivity.class.getSimpleName();
    public static final String EXTRA_NAME_ITEM_ID = "itemId";
    public static final String EXTRA_NAME_CURRENT_USER_ID = "currentUserId";
    private static final DecimalFormat df = new DecimalFormat("#.##");
    public static long currentUserId;
    public static long itemId;
    private Context context;
    private Bid existingBid;

    private Button btn_PlaceBid;
    private TextView txtView_ItemName;
    private TextView txtView_ItemDescription;
    private TextView txtView_TimeLeft;
    private TextView txtView_ItemPrice;
    private ImageView imgView_ItemPic;
    private TextView txtView_CurrencySymbol;
    private EditText editText_BidValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_item_detail);

        btn_PlaceBid = (Button)findViewById(R.id.button_PlaceBid);
        txtView_ItemName = (TextView)findViewById(R.id.textView_auction_detail_item_name);
        txtView_ItemDescription = (TextView)findViewById(R.id.textView_auction_detail_description);
        txtView_TimeLeft = (TextView)findViewById(R.id.textView_detail_time_left);
        txtView_ItemPrice = (TextView)findViewById(R.id.textView_auction_detail_price);
        imgView_ItemPic = (ImageView)findViewById(R.id.imageView_auction_detail);
        txtView_CurrencySymbol = (TextView)findViewById(R.id.textViewCurrencySymbol);
        editText_BidValue = (EditText)findViewById(R.id.editText_BidAmount);
        txtView_CurrencySymbol.setText(getResources().getString(R.string.symbol_currency));

        context = this;
        itemId = getIntent().getLongExtra(EXTRA_NAME_ITEM_ID, -1);
        currentUserId = getIntent().getLongExtra(EXTRA_NAME_CURRENT_USER_ID, -1);

        setData();
    }

    /*
    * Update Page Data
    * */
    private void setData(){
        if(itemId != -1){
            AuctionItem auctionItem = InitManager.getSharedInstance().getDbManager().getAuctionItemById(InitManager.getSharedInstance().getDatabase(), itemId);
            existingBid = InitManager.getSharedInstance().getDbManager().getBidByUserIdItemId(
                    InitManager.getSharedInstance().getDatabase(), currentUserId, itemId);

            txtView_ItemName.setText(auctionItem.getItemName());
            txtView_ItemDescription.setText(auctionItem.getItemDescription());

            txtView_ItemPrice.setText(getResources().getString(R.string.symbol_currency) + df.format(auctionItem.getPrice()));

            if(auctionItem.getExpiry() != null){
                if(DateUtils.isExpired(auctionItem.getExpiry())){
                    btn_PlaceBid.setEnabled(false);
                    editText_BidValue.setEnabled(false);
                    txtView_CurrencySymbol.setVisibility(View.INVISIBLE);
                }
                else{
                    btn_PlaceBid.setEnabled(true);
                    editText_BidValue.setEnabled(true);
                }
                txtView_TimeLeft.setText(DateUtils.getTimeLeftString(auctionItem.getExpiry()));
            }

            if(auctionItem.isSold()){
                imgView_ItemPic.setImageResource(R.drawable.aution_sold);
                btn_PlaceBid.setEnabled(false);
                editText_BidValue.setEnabled(false);
                txtView_TimeLeft.setText(getResources().getString(R.string.label_sold));
                txtView_CurrencySymbol.setVisibility(View.INVISIBLE);
            }
            else{
                imgView_ItemPic.setImageResource(R.drawable.aution_current);
                btn_PlaceBid.setEnabled(true);
                editText_BidValue.setEnabled(true);
            }

            if(auctionItem.getUserId() == currentUserId){
                btn_PlaceBid.setText(getResources().getString(R.string.label_button_detail_set_sold));
                editText_BidValue.setVisibility(View.INVISIBLE);
                txtView_CurrencySymbol.setVisibility(View.INVISIBLE);

                btn_PlaceBid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage(R.string.message_confirm_set_sold)
                                .setPositiveButton(R.string.label_yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        InitManager.getSharedInstance().getDbManager().setAuctionItemSold(
                                                InitManager.getSharedInstance().getDatabase(), AuctionItemDetailActivity.itemId, true);
                                        setData();

                                    }
                                })
                                .setNegativeButton(R.string.label_no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });
            }
            else{
                btn_PlaceBid.setText(getResources().getString(R.string.label_button_detail_bid));
                editText_BidValue.setVisibility(View.VISIBLE);

                if(existingBid != null){
                    editText_BidValue.setText(df.format(existingBid.getBidAmount()));
                    btn_PlaceBid.setText(getResources().getString(R.string.label_button_detail_bid_update));
                }

                btn_PlaceBid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double bidValue = Double.parseDouble(editText_BidValue.getText().toString());

                        Bid newBid = new Bid();
                        newBid.setUserId(currentUserId);
                        newBid.setBidTime(new Date());
                        newBid.setAuctionItemId(itemId);
                        newBid.setBidAmount(bidValue);

                        if(existingBid != null){
                            InitManager.getSharedInstance().getDbManager().updateBid(
                                    InitManager.getSharedInstance().getDatabase(), existingBid.getBidId(), newBid.getBidAmount()
                            );
                            Toast.makeText(context, context.getResources().getString(R.string.label_button_detail_bid_updated), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            InitManager.getSharedInstance().getDbManager().addBid(
                                    InitManager.getSharedInstance().getDatabase(), newBid
                            );

                            Toast.makeText(context, context.getResources().getString(R.string.label_button_detail_bid_submitted), Toast.LENGTH_SHORT).show();
                        }

                        setData();
                    }
                });
            }
        }
    }
}
