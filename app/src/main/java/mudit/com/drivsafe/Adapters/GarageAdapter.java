package mudit.com.drivsafe.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.http.conn.ConnectTimeoutException;
import org.w3c.dom.Text;

import java.util.ArrayList;

import mudit.com.drivsafe.PojoClass.Garage;
import mudit.com.drivsafe.R;

/**
 * Created by admin on 9/27/2017.
 */

public class GarageAdapter extends RecyclerView.Adapter<GarageAdapter.GarageViewHolder>{
    ArrayList<Garage> garages;
    Context context;

    public GarageAdapter(ArrayList<Garage> garages, Context context) {
        this.garages = garages;
        this.context = context;
    }

    @Override
    public GarageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=li.inflate(R.layout.list_item_garage,parent,false);
        return new GarageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GarageViewHolder holder, int position) {
        final Garage garage=garages.get(position);
        holder.tvName.setText(garage.getName());
        holder.tvDistance.setText(""+String.format("%.2f",garage.getDistance())+" mi");
        holder.tvPhone.setText(garage.getPhone_no());
        holder.imgDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+garage.getName());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                  context.startActivity(mapIntent);
//                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                        Uri.parse("http://maps.google.com/maps?saddr="+garage.getLat()","+garage.getLng()
//                                +"&daddr="+destination.getLatLng().latitude+","+destination.getLatLng().longitude));
//                context.startActivity(intent)
            }
        });
    }

    @Override
    public int getItemCount() {
        return garages.size();
    }



    class GarageViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvDistance,tvPhone;
        ImageButton imgDirection;
        public GarageViewHolder(View itemView) {
            super(itemView);
            tvName= (TextView) itemView.findViewById(R.id.tvName);
            tvDistance= (TextView) itemView.findViewById(R.id.tvDistance);
            tvPhone= (TextView) itemView.findViewById(R.id.tvPhone);
            imgDirection=(ImageButton)itemView.findViewById(R.id.imgDirections);
        }
    }
}
