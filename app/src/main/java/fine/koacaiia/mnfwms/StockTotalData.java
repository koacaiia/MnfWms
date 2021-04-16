package fine.koacaiia.mnfwms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StockTotalData extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<MnfStockList> list;
    MnfStockListAdapter adapter;
    FirebaseDatabase database;
    TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_total_data);

        recyclerView=findViewById(R.id.totalRecyclerview);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        list=new ArrayList<>();
        database=FirebaseDatabase.getInstance();
        getData();
        adapter=new MnfStockListAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        txtResult=findViewById(R.id.txtResult);
    }

    private void getData() {
        DatabaseReference databaseReference=database.getReference("MnF&StockTotal");
        ValueEventListener listener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    MnfStockList mList=data.getValue(MnfStockList.class);
                    list.add(mList);
                }
                int cbm=0;
                for(int i=0;i<list.size();i++){

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addListenerForSingleValueEvent(listener);
    }
}