package fine.koacaiia.mnfwms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MnfCargoListAdapter extends RecyclerView.Adapter<MnfCargoListAdapter.ListViewHolder> {
    ArrayList<MnfCargoList> list;

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
        holder.plt.setText(list.get(position).getPlt());
        holder.cbm.setText(list.get(position).getCbm());
        holder.qty.setText(list.get(position).getQty());
        holder.location.setText(list.get(position).getLocation());

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.itemOnClick(ListViewHolder.this,v,getAdapterPosition());
                }
            });
        }
    }
}
