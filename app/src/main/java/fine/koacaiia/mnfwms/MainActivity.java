package fine.koacaiia.mnfwms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements MnfCargoListAdapter.itemClicked {
    RecyclerView recyclerView;
    ArrayList<MnfCargoList> list;
    MnfCargoListAdapter adapter;
    FirebaseDatabase database;
    TextView txtPlt;
    TextView txtCbm;
    TextView txtQty;
    int intPlt,intCbm,intQty;
    ArrayList<String> desList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        list=new ArrayList<>();
        desList=new ArrayList<>();
        database=FirebaseDatabase.getInstance();
//        putData();
        getData();
        adapter=new MnfCargoListAdapter(list,this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        txtPlt=findViewById(R.id.txtResultPlt);
        txtCbm=findViewById(R.id.txtResultCbm);
        txtQty=findViewById(R.id.txtResultQty);

    }

    private void putData() {
        String date=new SimpleDateFormat("yyyy년MM월dd일").format(new Date());
        String day=date.substring(8,10);
        DatabaseReference databaseReference;
        for(int i=1;i<Integer.parseInt(day);i++){

            MnfCargoList mList=new MnfCargoList("2021년4월"+day+"일","remark:"+i,"date:"+"2021년4월"+day+"일","count:"+i,"Des:"+i,
                    "Plt:"+i,"Cbm:"+i,"Qty:"+i,"Location:"+i);
            databaseReference=database.getReference("MnF/"+"2021년4월"+i+"일");
            databaseReference.setValue(mList);
        }

    }

    private void getData() {
        DatabaseReference databaseReference=database.getReference("MnF");
        ValueEventListener listener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> des=new ArrayList<>();
                for(DataSnapshot data:snapshot.getChildren()){

                    MnfCargoList mList=data.getValue(MnfCargoList.class);
                    if(!mList.getQty().equals("0")){
                        list.add(mList);
                         }
                    }
                int listSize=list.size();
                intPlt=0;
                intCbm=0;
                intQty=0;
                for(int i=0;i<listSize;i++){
                    intPlt=intPlt+Integer.parseInt(list.get(i).getPlt());
                    intCbm=intCbm+Integer.parseInt(list.get(i).getCbm());
                    intQty=intQty+Integer.parseInt(list.get(i).getQty());
                    des.add(list.get(i).getDes());
                    Log.i("koacaiia","Des Add Value+++:"+list.get(i).getDes()+"Des List size"+list.size());
                    if(!des.contains(list.get(i).getDes())){
                        desList.add(list.get(i).getDes());
                        Log.i("koacaiia","Des List Value+++:"+list.get(i).getDes());
                    }

                }



                txtPlt.setText(String.valueOf(intPlt)+" PlT");
                txtCbm.setText(String.valueOf(intCbm)+" CBM");
//                txtQty.setText(String.valueOf(intQty));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addListenerForSingleValueEvent(listener);
    }

    @Override
    public void itemOnClick(MnfCargoListAdapter.ListViewHolder listviewholder, View v, int pos) {
//        String item=list.get(pos).getDes();
//        Toast.makeText(this,"Item Clicked"+item,Toast.LENGTH_SHORT).show();
        dialogItem();
    }

    private void dialogItem() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this  );
        builder.setTitle("Manage Control!")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplication(),"Alert Dialog Init",Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}