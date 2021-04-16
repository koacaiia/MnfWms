package fine.koacaiia.mnfwms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    static private final String SHARE_NAME="SHARE_DEPOT";
    static SharedPreferences sharedPref;
    static SharedPreferences.Editor editor;

    String depotName;
    String nickName;

    String bl;
    String des;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref=getSharedPreferences(SHARE_NAME,MODE_PRIVATE);
        if(sharedPref==null){
            depotName="FineTrading";
            nickName="FineTrading Staff";
        }else{
            depotName=sharedPref.getString("depotName","FineTrading");
            nickName=sharedPref.getString("nickName","FineTrading Staff");
        }

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

                txtPlt.setText(String.valueOf(intPlt)+" PLT");
                txtCbm.setText(String.valueOf(intCbm)+" CBM");
                txtQty.setText(String.valueOf(intQty));
                String toDay=new SimpleDateFormat("yyyy년MM월dd일").format(new Date());
                DatabaseReference dataReference=database.getReference("MnF&StockTotal/"+toDay);
                MnfStockList mnfStockList=new MnfStockList(toDay,String.valueOf(intPlt),String.valueOf(intCbm));
                dataReference.setValue(mnfStockList);

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

        bl=list.get(pos).getBl();
        des=list.get(pos).getDes();
        MnfCargoList mnfCargoList=new MnfCargoList();
        mnfCargoList=list.get(pos);
        switch(depotName){
            case "ComanFood":
                alertComan(mnfCargoList);
                break;
            case "M&F":
                Toast.makeText(this,"Not Yet",Toast.LENGTH_SHORT).show();
                break;
            case "FineTrading":
                alertFine(mnfCargoList);
                break;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @SuppressLint("NonConstantResourcedId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
      editor=sharedPref.edit();

      switch(item.getItemId()){
          case R.id.action_account:
              putRegistName();

              break;
          case R.id.action_account_down:
//             selectDialog();
              totalIntent();
              break;
          case R.id.action_account_search:
              if(bl==null){
                  Toast.makeText(this,"화물조회 항목 비엘 다시 한번 확인 바랍니다.",Toast.LENGTH_SHORT).show();
              }else{
                  Toast.makeText(this,"비엘:"+bl+" 이 선택 되었습니다.",Toast.LENGTH_SHORT).show();
                  webview(bl);
              }

              break;
      }

    return true;
    }

    private void totalIntent() {
        Intent intent=new Intent(MainActivity.this,StockTotalData.class);
        startActivity(intent);
    }

    private void webview(String bl) {
        Intent intent=new Intent(MainActivity.this,WebList.class);
        intent.putExtra("bl",bl);
        intent.putExtra("des",des);
        startActivity(intent);
    }

    private void putRegistName() {
        ArrayList<String> arrListDepotName=new ArrayList<>();
        arrListDepotName.add("ComanFood");
        arrListDepotName.add("M&F");
        arrListDepotName.add("FineTrading");

        String[] arrDepotName=arrListDepotName.toArray(new String[arrListDepotName.size()]);
        View view=getLayoutInflater().inflate(R.layout.user_reg,null);
        EditText reg_edit=view.findViewById(R.id.user_reg_Edit);
        Button reg_button=view.findViewById(R.id.user_reg_button);
        TextView reg_depot=view.findViewById(R.id.user_reg_depot);

        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickName=reg_edit.getText().toString();
                reg_depot.setText(depotName+"_"+nickName+"으로 사용자 등록을"+"\n"+"진행할려면 하단 Confirm 버튼을 클릭 바랍니다.");
            }
        });
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(view)
                .setSingleChoiceItems(arrDepotName,0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        depotName=arrDepotName[which];
                        reg_depot.setText("부서명_"+depotName+"로 확인");
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putString("depotName",depotName);
                        editor.putString("nickName",nickName);
                        editor.apply();
                        Toast.makeText(getApplicationContext(),depotName+"_"+nickName+"로 사용자 등록 성공 하였습니다.!",Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
    public void alertComan(MnfCargoList mnfCargoList){
        ArrayList<String > coman=new ArrayList<>();
        coman.add("검역 신청");
        coman.add("검역 중");
        coman.add("식약처 검채 건");
        coman.add("검역 완료");
        coman.add("수입신고 신청");
        coman.add("수입신고 검사 선별건");
        coman.add("수입신고 수리 완료");


        String[] arrComan=coman.toArray(new String[coman.size()]);
        String[] comanProcess = new String[1];
        String childPath=
                "MnF/"+mnfCargoList.getDate()+"_"+mnfCargoList.getBl()+"_"+mnfCargoList.getDes()+"_"+mnfCargoList.getCount();
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Coman Process")
                        .setSingleChoiceItems(arrComan, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                             comanProcess[0] =arrComan[which];
                              Toast.makeText(getApplicationContext(),depotName+""+ comanProcess[0] +"으로 업무 전달 진행 됩니다.",
                                      Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("Share", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mnfCargoList.setRemark(comanProcess[0]);
                                DatabaseReference databaseReference=database.getReference(childPath);
                                databaseReference.setValue(mnfCargoList);
                                getData();
                            }
                        })
                        .show();
    }

    public void alertFine(MnfCargoList mnfCargoList){
        ArrayList<String> arrListFine=new ArrayList<>();
        arrListFine.add("보세화물 반입 진행");
        arrListFine.add("식약처 검채 확인");
        arrListFine.add("수입신고 검사 확인");
        String[] arrFine = arrListFine.toArray(new String[arrListFine.size()]);
        String finePath=
                "MnF/"+mnfCargoList.getDate()+"_"+mnfCargoList.getBl()+"_"+mnfCargoList.getDes()+"_"+mnfCargoList.getCount();
        final String[] itemName = new String[1];
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("FineTrading Progress")
                .setSingleChoiceItems(arrFine,0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                          itemName[0] =arrFine[which];
                    }
                })
                .setPositiveButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference databaseReference=database.getReference(finePath);
                            mnfCargoList.setRemark(itemName[0]);
                            databaseReference.setValue(mnfCargoList);
                            getData();
                    }
                })
                .show();


    }
    public void selectDialog(){
        ArrayList<String> arrListDepotName=new ArrayList<>();
        arrListDepotName.add("ComanFood");
        arrListDepotName.add("M&F");
        arrListDepotName.add("FineTrading");

        String[] arrDepotName=arrListDepotName.toArray(new String[arrListDepotName.size()]);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(arrDepotName,0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String depotName=arrDepotName[which];
                String depotNameSortArrayList=arrListDepotName.get(which);
                Log.i("koacaiia","depotNameSortArrayList"+depotNameSortArrayList);
                switch(depotName){
//                    case "ComanFood":
//                        alertComan(mnfCargoList);
//                        break;
//                    case "FineTrading":
//                        alertFine(mnfCargoList);
//                        break;
                }
            }
        })
                .show();
    }
    public void alarmProcess(String depotName,String nickName,String workProcess){

    }
}