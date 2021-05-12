package fine.koacaiia.mnfwms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WebList extends AppCompatActivity {
    WebView webView;
    WebSettings webSettings;
    FirebaseDatabase database;
    RecyclerView recyclerview;
    MnfCargoListAdapter adapter;
    ArrayList<MnfCargoList> list;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_list);


        Intent intent=getIntent();
        String bl=intent.getStringExtra("Bl");

        list=new ArrayList<>();
        database=FirebaseDatabase.getInstance();
        recyclerview=findViewById(R.id.web_recyclerview);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        DatabaseReference databaseReference=database.getReference("MnF");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    MnfCargoList mList=data.getValue(MnfCargoList.class);
                    if(bl.equals(mList.getBl())){
                        list.add(mList);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        adapter=new MnfCargoListAdapter(list);
        recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        webView=findViewById(R.id.web_list);

        webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("https://www.tradlinx.com/unipass?type=2&blNo="+bl+"&blYr=2021");

        textView=findViewById(R.id.web_txttitle);
        textView.setText(bl+"_화물정보 조회 자료");

    }
}