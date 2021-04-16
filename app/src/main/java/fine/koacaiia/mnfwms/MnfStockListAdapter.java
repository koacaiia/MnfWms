package fine.koacaiia.mnfwms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MnfStockListAdapter extends RecyclerView.Adapter<MnfStockListAdapter.ListViewHolder>{
    ArrayList<MnfStockList> list;
    public MnfStockListAdapter(ArrayList<MnfStockList> list){
        this.list=list;
    }
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.stocktotal,parent,false);

        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.date.setText(list.get(position).getTotalDate());
        holder.plt.setText(list.get(position).getTotalPlt()+" PLT");
        holder.cbm.setText(list.get(position).getTotalCbm()+" CBM");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView plt;
        TextView cbm;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.date=itemView.findViewById(R.id.txtListDate);
            this.plt=itemView.findViewById(R.id.txtListPlt);
            this.cbm=itemView.findViewById(R.id.txtListCbm);
        }
    }
}
