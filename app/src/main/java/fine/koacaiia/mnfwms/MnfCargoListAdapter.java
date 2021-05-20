package fine.koacaiia.mnfwms;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MnfCargoListAdapter extends RecyclerView.Adapter<MnfCargoListAdapter.ListViewHolder> {
    ArrayList<MnfCargoList> list;

    public MnfCargoListAdapter(ArrayList<MnfCargoList> list) {
        this.list=list;
    }

    public interface itemClicked{
        void itemOnClick(ListViewHolder listviewholder,View v,int pos);
    }
    itemClicked listener;

    public MnfCargoListAdapter(ArrayList<MnfCargoList> list,itemClicked listener){
        this.list=list;
        this.listener=listener;
    }
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.mnf_cargo_list,parent,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.bl.setText(list.get(position).getBl());
        holder.remark.setText(list.get(position).getRemark());
        holder.date.setText(list.get(position).getDate());
        holder.count.setText(list.get(position).getCount());
        holder.des.setText(list.get(position).getDes());
        holder.plt.setText(list.get(position).getPlt()+" Plt");
        holder.cbm.setText(list.get(position).getCbm()+" Cbm");
        holder.qty.setText(list.get(position).getQty()+" Ea");
        holder.location.setText(list.get(position).getLocation());

        String remarked=list.get(position).getRemark();
        holder.bl.setTextColor(Color.BLACK);
        holder.remark.setTextColor(Color.BLACK);
        holder.date.setTextColor(Color.BLACK);
        holder.count.setTextColor(Color.BLACK);
        holder.des.setTextColor(Color.BLACK);
        holder.plt.setTextColor(Color.BLACK);
        holder.cbm.setTextColor(Color.BLACK);
        holder.qty.setTextColor(Color.BLACK);
        holder.location.setTextColor(Color.BLACK);
       if(remarked==null||remarked.equals("반입 예정")){
           holder.bl.setTextColor(Color.WHITE);
           holder.remark.setTextColor(Color.WHITE);
           holder.date.setTextColor(Color.WHITE);
           holder.count.setTextColor(Color.WHITE);
           holder.des.setTextColor(Color.WHITE);
           holder.plt.setTextColor(Color.WHITE);
           holder.cbm.setTextColor(Color.WHITE);
           holder.qty.setTextColor(Color.WHITE);
           holder.location.setTextColor(Color.WHITE);
           holder.cardView.setBackgroundColor(Color.BLACK);
       }else if(remarked.equals("수입신고 수리 완료")){
            holder.cardView.setBackgroundColor(Color.WHITE);
       }else{
           holder.cardView.setBackgroundColor(Color.rgb(204,204,204));
       }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView bl;
        TextView remark;
        TextView date;
        TextView count;
        TextView des;
        TextView plt;
        TextView cbm;
        TextView qty;
        TextView location;
        LinearLayout cardView;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.bl=itemView.findViewById(R.id.txtBl);
            this.remark=itemView.findViewById(R.id.txtRemark);
            this.date=itemView.findViewById(R.id.txtDate);
            this.count=itemView.findViewById(R.id.txtCount);
            this.des=itemView.findViewById(R.id.txtDes);
            this.plt=itemView.findViewById(R.id.txtPlt);
            this.cbm=itemView.findViewById(R.id.txtCbm);
            this.qty=itemView.findViewById(R.id.txtQty);
            this.location=itemView.findViewById(R.id.txtLocation);
            this.cardView=itemView.findViewById(R.id.re_back);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.itemOnClick(ListViewHolder.this,v,getAdapterPosition());
                }
            });
        }
    }
}
