package fine.koacaiia.mnfwms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MnfCargoListAdapter.itemClicked {
    RecyclerView recyclerView;
    ArrayList<MnfCargoList> list;
    ArrayList<MnfStockList> lists;
    ArrayList<MnfRemarkList> listRemark;
    MnfCargoListAdapter adapter;
    MnfStockListAdapter mAdapter;
    FirebaseDatabase database;
    TextView txtPlt,txtPltBond,txtPltRe,txtPltCc;
    TextView txtCbm,txtCbmBond,txtCbmRe,txtCbmCc;
    TextView txtQty,txtQtyBond,txtQtyRe,txtQtyCc;
    int intPlt,intCbm,intQty,intPltBond,intCbmBond,intQtyBond,intPltCc,intCbmCc,intQtyCc,intPltRe,intCbmRe,intQtyRe;
    ArrayList<String> desList;

    static private final String SHARE_NAME="SHARE_DEPOT";
    static SharedPreferences sharedPref;
    static SharedPreferences.Editor editor;

    String depotName;
    String nickName;

    String bl;
    String des;
    String date;
    String remarkedItem;


    Button btnTitle;
    ArrayList<String> itemListBl;
    ArrayList<String> itemListDes;
    ArrayList<String> itemListProcess;
    ArrayList<String> itemListCount;

    String searchConditionResult;
    String searchConditionKey;

    static RequestQueue requestQueue;

    FloatingActionButton fltBtn;

    String [] permission_list={
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.VIBRATE,
            Manifest.permission.INTERNET,
            Manifest.permission.USE_FULL_SCREEN_INTENT,
            Manifest.permission.ANSWER_PHONE_CALLS,
    };

    int dateSince;
    int dateSinced;
    static String regId="cfAZNLWbSMSfSWnt-ptg1_:APA91bFOixRWpzvGiQFrjVJN7XcCu5brIRXjdhBVBYJ8f8bXWoMHoueyTYk-zDMggQkldkn2F7ML5_TCuK1Fc9Kv8zjlNjL4d4rOy5ntGSQ5PWAWNOfz_7lBDE1iwcTewmlCaAifbXze";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions(permission_list,0);

        sharedPref=getSharedPreferences(SHARE_NAME,MODE_PRIVATE);
        if(sharedPref==null){
            depotName="FineTrading";
            nickName="FineTrading Staff";
        }else{
            depotName=sharedPref.getString("depotName","FineTrading");
            nickName=sharedPref.getString("nickName","FineTrading Staff");
        }


        FirebaseMessaging.getInstance().subscribeToTopic("Test");
        recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        list=new ArrayList<>();
        lists=new ArrayList<>();
        desList=new ArrayList<>();
        database=FirebaseDatabase.getInstance();

//        putData();
        getRemarkListItems();

        if(getIntent().getStringExtra("Bl")!=null){
            Log.i("duatjsrb","intent getBl:"+getIntent().getStringExtra("Bl"));
            getDataSort("bl",getIntent().getStringExtra("Bl"));
        }else{

            getData();

            Log.i("duatjsrb","List Size++++:"+list.size());
        }

        adapter=new MnfCargoListAdapter(list,this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        txtPlt=findViewById(R.id.txtResultPlt);
        txtPltBond=findViewById(R.id.txtPlt_bound);
        txtPltCc=findViewById(R.id.txtPlt_customsClearance);
        txtPltRe=findViewById(R.id.txtPlt_reserved);

        txtCbm=findViewById(R.id.txtResultCbm);
        txtCbmBond=findViewById(R.id.txtCbm_bond);
        txtCbmCc=findViewById(R.id.txtCbm_customsClearance);
        txtCbmRe=findViewById(R.id.txtCbm_reserved);

        txtQty=findViewById(R.id.txtResultQty);
        txtQtyBond=findViewById(R.id.txtQty_bond);
        txtQtyCc=findViewById(R.id.txtQty_customsClearance);
        txtQtyRe=findViewById(R.id.txtQty_reserved);


        date=new SimpleDateFormat("yyyy년MM월dd일").format(new Date());

        btnTitle=findViewById(R.id.btn_title);
        btnTitle.setText(date+"재고현황 화물조회");
        btnTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertSearch();
                Toast.makeText(getApplicationContext(),"버튼을 길게 누르면 화물정보 초기화 됩니다.!",Toast.LENGTH_SHORT).show();
            }
        });

        btnTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                getData();
//                getRemarkListItems();
                Toast.makeText(getApplicationContext(),"화물정보를 초기화 합니다.",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(getApplicationContext());
        }

//        fltBtn=findViewById(R.id.floatingActionButton);
//        fltBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pushMessage("업무 변동사항 발생", "200589");
//            }
//        });


    }

    private void getDateSince() {

        lists.clear();

        DatabaseReference databaseReference=database.getReference("MnF&StockTotal");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    MnfStockList mList=data.getValue(MnfStockList.class);
                    lists.add(mList);
                }
               dateSinced =lists.get(lists.size()-1).getUntilDate();

//                dateSince=lists.get(lists.size()-1).getUntilDate();
//                mAdapter.notifyDataSetChanged();
//                String strDateSince;
//                Log.i("duatjsrb","dateSince:"+dateSince+"lists size:"+lists.size());
                transferDated(dateSinced);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        Log.i("duatjsrb","Return Value:"+dateSince+"Return Values+++:");

    }

    private void transferDated(int dateSinced) {
        this.dateSince=dateSinced;

        dateSince=untilDate()-dateSinced;

    }

    private void getDataSort(String order,String value) {
        list.clear();
        DatabaseReference databaseReference=database.getReference("MnF");
        ValueEventListener listener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    MnfCargoList mList=data.getValue(MnfCargoList.class);
                    list.add(mList);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        };
        Query itemSortResult=databaseReference.orderByChild(order).equalTo(value);
        itemSortResult.addListenerForSingleValueEvent(listener);

    }
    private void alertSearch() {
        ArrayList<String> itemSearch=new ArrayList<>();
        itemSearch.add("B/L 별 조회");
        itemSearch.add("찻수 별 조회");
        itemSearch.add("품목 별 조회");
        itemSearch.add("반입입 별 조회");
        itemSearch.add("진행상황 별 조회");

        String[] arrItemSearch=itemSearch.toArray(new String[itemSearch.size()]);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        AlertDialog dialog=builder.create();
        builder.setTitle("화물조회")
                .setSingleChoiceItems(arrItemSearch,0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String searchCondition=arrItemSearch[which];
                        dialog.dismiss();
                        switch(which){
                            case 0:
                                searchConditionKey="bl";
                                alertSearchSort(itemListBl,searchCondition);

                                break;
                            case 1:
                                searchConditionKey="count";
                                alertSearchSort(itemListCount, searchCondition);

                                break;
                            case 2:
                                searchConditionKey="des";
                                alertSearchSort(itemListDes, searchCondition);

                                break;
                            case 3:
                                alertSearchDate();
                                break;
                            case 4:
                                searchConditionKey="remark";
                                alertSearchSort(itemListProcess, searchCondition);
                                break;
                        }
                    }
                })
                .show();
    }

    private void alertSearchSort(ArrayList<String> list, String searchCondition) {
        String[] getSpinnerItemList=list.toArray(new String[list.size()]);

        View view=getLayoutInflater().inflate(R.layout.sort_itemprocess_simple,null);
        Spinner spinner=view.findViewById(R.id.spinner_simple);
        ArrayAdapter<String> searchConditionAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item
                ,getSpinnerItemList);
        spinner.setAdapter(searchConditionAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            searchConditionResult=getSpinnerItemList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnSearch_simple=view.findViewById(R.id.btnSearch_simple);

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        AlertDialog dialog=builder.create();
        builder.setTitle(searchCondition);
                builder.setView(view);
                builder.setPositiveButton("검색", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getSearchConditionResult();
                    }
                });

                builder.show();
        btnSearch_simple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("duatjsrb","before clicked spinner value:"+searchConditionResult);
                dialog.dismiss();
                getSearchConditionResult();
            }
        });

    }

    private void getSearchConditionResult() {
        list.clear();
        Log.i("duatjsrb","after clicked Value:"+searchConditionResult);
        DatabaseReference databaseReference=database.getReference("MnF");
        ValueEventListener listener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    MnfCargoList mList=data.getValue(MnfCargoList.class);
                    list.add(mList);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        Query sortResult=databaseReference.orderByChild(searchConditionKey).equalTo(searchConditionResult);
        sortResult.addListenerForSingleValueEvent(listener);

    }


    private void alertSearchDate(){

    DatePickerFragment datePickerFragment=new DatePickerFragment();
    datePickerFragment.show(getSupportFragmentManager(),"datePicker");

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
        list.clear();
        DatabaseReference databaseReference=database.getReference("MnF");
        ValueEventListener listener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> des=new ArrayList<>();
                for(DataSnapshot data:snapshot.getChildren()){

                    MnfCargoList mList=data.getValue(MnfCargoList.class);
                   String cbmGet = mList.getCbm();
                   Log.i("duatjsrb","GetCbm:"+mList.getCbm());
                   if(cbmGet==null){
                       String name= data.getKey();
                       DatabaseReference updateRemarkItems=database.getReference("MnF/"+name);//
                       updateRemarkItems.setValue(null);
                   }else{
                       if(!cbmGet.equals("0")){
                           list.add(mList);
                       }
                   }

                    }

                itemListBl=new ArrayList<>();
                itemListDes=new ArrayList<>();
                itemListProcess=new ArrayList<>();
                itemListCount=new ArrayList<>();


                int listSize=list.size();
                intPlt=0;
                intPltBond=0;
                intPltCc=0;
                intPltRe=0;
                intCbm=0;
                intCbmBond=0;
                intCbmCc=0;
                intCbmRe=0;
                intQty=0;
                intQtyBond=0;
                intQtyCc=0;
                intQtyRe=0;

                for(int i=0;i<listSize;i++){
                    if(list.get(i).getRemark().equals("수입신고 수리 완료")){
                        intPltCc=intPltCc+Integer.parseInt(list.get(i).getPlt());
                        intCbmCc=intCbmCc+Integer.parseInt(list.get(i).getCbm());
                        intQtyCc=intQty+Integer.parseInt(list.get(i).getQty());
                    }else if(list.get(i).getRemark().equals("반입 예정")){
                        intPltRe=intPltRe+Integer.parseInt(list.get(i).getPlt());
                        intCbmRe=intCbmRe+Integer.parseInt(list.get(i).getCbm());
                        intQtyRe=intQtyRe+Integer.parseInt(list.get(i).getQty());
                    }else{
                        intPltBond=intPltBond+Integer.parseInt(list.get(i).getPlt());
                        intCbmBond=intCbmBond+Integer.parseInt(list.get(i).getCbm());
                        intQtyBond=intQtyBond+Integer.parseInt(list.get(i).getQty());
                    }
                    intPlt=intPltCc+intPltBond;
                    intCbm=intCbmCc+intCbmBond;
                    intQty=intQtyCc+intQtyBond;
                    des.add(list.get(i).getDes());
//                    Log.i("koacaiia","Des Add Value+++:"+list.get(i).getDes()+"Des List size"+list.size());
                    if(!des.contains(list.get(i).getDes())){
                        desList.add(list.get(i).getDes());
//                        Log.i("koacaiia","Des List Value+++:"+list.get(i).getDes());
                    }
                    itemListBl.add(list.get(i).getBl());
                    itemListDes.add(list.get(i).getDes());
                    itemListProcess.add(list.get(i).getRemark());
                    itemListCount.add(list.get(i).getCount());
                }

                txtPlt.setText(String.valueOf(intPlt)+" PLT");
                txtPltBond.setText(intPltBond+" PLT");
                txtPltCc.setText(intPltCc+" PLt");
                txtPltRe.setText(intPltRe+" PLT");

                txtCbm.setText(String.valueOf(intCbm)+" CBM");
                txtCbmBond.setText(intCbmBond+" CBM");
                txtCbmCc.setText(intCbmCc+" CBM");
                txtCbmRe.setText(intCbmRe+" CBM");

                Double dQty=Double.valueOf(intCbm);
                Double dQtyAvgCc=(Double)(intCbmCc/dQty)*100;
                String sQtyAvgCc=String.format("%.2f",dQtyAvgCc);
                Double dQtyBond=100-dQtyAvgCc;
                String sQtyBond=String.format("%.2f",dQtyBond);
                txtQty.setText(String.valueOf(intQty));
                txtQtyBond.setText(sQtyBond+"%");
                txtQtyCc.setText(sQtyAvgCc+"%");
                txtQtyRe.setText(String.valueOf(intQtyRe));
                String toDay=new SimpleDateFormat("yyyy년MM월dd일").format(new Date());


                DatabaseReference untilRef=database.getReference("MnF&StockTotal");
                untilRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                      for(DataSnapshot data:snapshot.getChildren()){
                          MnfStockList uList=data.getValue(MnfStockList.class);
                          lists.add(uList);
                      }
                      int uDate=lists.get(lists.size()-1).getUntilDate();
                      Log.i("duatjsrb","uDate Value:"+uDate+"originalDate"+lists.get(lists.size()-1).getTotalDate()+"unTilDate " +
                              "Value:::"+untilDate());
                      dateSince=untilDate()-uDate;
                        if(dateSince>1){

                            for(int i=0;i<=dateSince;i++){
                                Log.i("duatjsrb","for Count:"+dateSince+"i Value"+i);
                                Date today=new Date();
                                Calendar cal=Calendar.getInstance();
                                cal.setTime(today);
                                cal.add(Calendar.DATE,-i);
                                Date untilDate=new Date(cal.getTimeInMillis());
                                String untilToDay=new SimpleDateFormat("yyyy년MM월dd일").format(untilDate);
                                MnfStockList mnfStockList=new MnfStockList(untilToDay,String.valueOf(intPlt),String.valueOf(intCbm),
                                        untilDate()-i);
                                DatabaseReference dataReference=database.getReference("MnF&StockTotal/"+untilToDay);
                                dataReference.setValue(mnfStockList);
                            }

                            Log.i("duatjsrb","Dated until Message:"+dateSince);
                        }else{
                            Log.i("duatjsrb","Dated until Message:"+dateSince);
                            DatabaseReference dataReference=database.getReference("MnF&StockTotal/"+toDay);
                            MnfStockList mnfStockList=new MnfStockList(toDay,String.valueOf(intPlt),String.valueOf(intCbm),untilDate());
                            dataReference.setValue(mnfStockList);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });




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
        intent.putExtra("Bl",bl);
        intent.putExtra("des",des);
        startActivity(intent);
    }

    private void putRegistName() {
        ArrayList<String> arrListDepotName=new ArrayList<>();
        arrListDepotName.add("FineTrading");
        arrListDepotName.add("ComanFood");
        arrListDepotName.add("M&F");


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
        arrListFine.add("반입 예정");
        arrListFine.add("화물검수 완료,반입진행 요청");
        arrListFine.add("보세화물 반입 진행");
        arrListFine.add("식약처 검채 확인");
        arrListFine.add("수입신고 검사 확인");
        arrListFine.add("수입신고 수리 완료");
        String[] arrFine = arrListFine.toArray(new String[arrListFine.size()]);
        String finePath=
                "MnF/"+mnfCargoList.getDate()+"_"+mnfCargoList.getBl()+"_"+mnfCargoList.getDes()+"_"+mnfCargoList.getCount();
        String finePathRemark=
                "MnFRemark/"+mnfCargoList.getDate()+"_"+mnfCargoList.getBl()+"_"+mnfCargoList.getDes()
                +"_"+mnfCargoList.getCount();
        final String[] itemName = new String[1];
        String cargoBl="Bl:"+mnfCargoList.getBl();
        String count="찻수:"+mnfCargoList.getCount();
        String des="품명:"+mnfCargoList.getDes();
        String cargoInfo=cargoBl+"_"+count+"\n"+des;
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(cargoInfo)
                .setSingleChoiceItems(arrFine,0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                          remarkedItem =arrFine[which];
                    }
                })
                .setPositiveButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String updateTime=new SimpleDateFormat("MM월dd일HH시mm분").format(new Date());
                        pushMessage(cargoInfo+"\n"+updateTime+"에"+"\n"+remarkedItem+"으로 진행상황 Updated 되었습니다.!",
                                mnfCargoList.getBl());

                            DatabaseReference remarkDatabaseReference=database.getReference(finePathRemark);
                            DatabaseReference databaseReference=database.getReference(finePath);
                            mnfCargoList.setRemark(remarkedItem);

                        MnfRemarkList mnfRemarkList=new MnfRemarkList(mnfCargoList.getDate(),mnfCargoList.getBl(),
                                mnfCargoList.getDes(),mnfCargoList.getCount(),remarkedItem);
//                         mnfRemarkList.setRemark(remarkedItem);
                            databaseReference.setValue(mnfCargoList);

                        remarkDatabaseReference.setValue(mnfRemarkList);
                            getData();

                    }
                })
                .show();


    }

    public void datePickerResult(String date){
       searchConditionDate(date);
        Toast.makeText(this,date,Toast.LENGTH_SHORT).show();
    }

    private void searchConditionDate(String date) {
        list.clear();
        DatabaseReference databaseReference=database.getReference("MnF");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    MnfCargoList mList=data.getValue(MnfCargoList.class);
                    if(mList.getDate().equals(date)){
                        list.add(mList);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void pushMessage(String message, String bl){
        JSONObject requestData=new JSONObject();
        try{
            requestData.put("priority","high");
            JSONObject dataOBJ=new JSONObject();
            dataOBJ.put("content",message);
            dataOBJ.put("depotName",depotName);
            dataOBJ.put("Bl",bl);


            requestData.put("data",dataOBJ);
            requestData.put("to","/topics/Test");

        }catch(Exception e){
            e.printStackTrace();
        }
        sendData(requestData,new SendResponseListener(){

            @Override
            public void onRequestStarted() {
                Toast.makeText(getApplicationContext(),depotName+"에서"+message+"내용의 진행상황이 Update 되었습니다.",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRequestCompleted() {
                Toast.makeText(getApplicationContext
                        (),"Completed",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRequestWithError(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();

            }
        });
    }
    private void sendData(JSONObject requestData, SendResponseListener sendResponseListener) {
        JsonObjectRequest request=new JsonObjectRequest(
                Request.Method.POST,
                "https://fcm.googleapis.com/fcm/send",
                requestData,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        sendResponseListener.onRequestCompleted();
                        Log.i("koacaiia","Response Completed");
                    }},
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sendResponseListener.onRequestWithError(error);
                        Log.i("koacaiia","Response Error");
                    }
                })
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                return params;
            }
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError{
                Map<String,String> headers=new HashMap<String,String>();
                headers.put("Authorization","key=AAAAOH76F-g:APA91bHwrh95DdbEElsFfLYvP_BBcSi9GsoKu5Kc9Ig_512ERnkagGf06T3qy7uAboOZrE3LKrEW2WITYnKevLJjrMZOLeiUEs0tJ07SCyvhi8d6ITlBBAP8byol6FrzBSA4hgAShajp");
                return headers;
            }
            @Override
            public String getBodyContentType(){
                return "application/json";
            }

        };

        request.setShouldCache(false);
        sendResponseListener.onRequestStarted();
        requestQueue.add(request);
    }



    public interface SendResponseListener {
        void onRequestStarted();
        void onRequestCompleted();
        void onRequestWithError(VolleyError error);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int result:grantResults){
            if(result== PackageManager.PERMISSION_DENIED){
                Toast.makeText(this, "permission denied"+permissions[requestCode], Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }

    public void getRemarkListItems(){
        listRemark=new ArrayList<>();
        DatabaseReference databaseReference=database.getReference("MnFRemark");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    MnfRemarkList mList=data.getValue(MnfRemarkList.class);
                    String date=mList.getDate();
                    String bl=mList.getBl();
                    String des=mList.getDes();
                    String count=mList.getCount();
                    String remark=mList.getRemark();
                    String referencePath="MnF/"+date+"_"+bl+"_"+des+"_"+count;
                    DatabaseReference databasePutRemark=database.getReference(referencePath);

//                    MnfCargoList mnfCargoList=new MnfCargoList();
//                    mnfCargoList.setRemark(remark);
//                    databasePutRemark.setValue("remark",remark);
                   Map<String,Object> reMarkedItems=new HashMap<>();
                   reMarkedItems.put("remark",remark);
                   databasePutRemark.updateChildren(reMarkedItems);
                    }
              adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

//        getData();
    }

    public int untilDate(){
        Date today=new Date();
        Calendar cal= Calendar.getInstance();
        cal.setTime(today);
        Calendar cal2=Calendar.getInstance();
        cal2.set(2020,00,01);
        int count=0;
        while(!cal2.after(cal)){
            count++;
            cal2.add(Calendar.DATE,1);

        }
        return count;

    }


}