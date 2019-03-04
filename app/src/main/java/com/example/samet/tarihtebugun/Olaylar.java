package com.example.samet.tarihtebugun;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class Olaylar extends Fragment{

    RecyclerView recyclerView;
    ArrayList<String> arrayList;
    ArrayList<String> arrayListTarih;
    private RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    int sayac;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.olaylar,container,false);
        if(sayac==0){
            Fetch fetch=new Fetch();
            fetch.execute("https://www.onthisday.com/today/events.php");
        }
        sayac++;
        arrayList=new ArrayList<>();
        arrayListTarih=new ArrayList<>();
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        sayac=0;
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }
    class Fetch extends AsyncTask<String,Void,Void> {
        Document document;
        Elements elements;
        Elements gun;
        Elements ay;
        String ayy;
        String gunn;
        ProgressDialog progressDialog;
        @Override
        protected Void doInBackground(String... strings) {
            try {
                document=Jsoup.connect(strings[0]).get();
                elements=document.select("li.event-list__item");
                ay=document.select("span.month");
                gun=document.select("span.date");
            }catch (Exception e){
                Snackbar.make(view.findViewById(R.id.linearlayout),"Bir sorun oluştu",Snackbar.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(sayac==0){
                progressDialog=new ProgressDialog(getContext());
                progressDialog.setMessage("Bilgiler Çekiliyor...");
                progressDialog.setIndeterminate(false);
                progressDialog.show();
                sayac++;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (Element element:elements)
                arrayList.add(element.ownText());
            for (Element element:elements)
                arrayListTarih.add(element.select("a[href]").text());
            for (Element element:ay)
                ayy=element.text();
            for (Element element:gun)
                gunn=element.text();
            MainActivity.toolbar.setTitle(ayy+" "+gunn);
            mAdapter=new Adapter(arrayList, arrayListTarih);
            recyclerView.setAdapter(mAdapter);
            progressDialog.dismiss();
        }
    }
}
class Adapter extends RecyclerView.Adapter<Adapter.Hodor>{

    ArrayList<String> arrayListe;
    ArrayList<String> arrayListTarih;

    public Adapter(ArrayList<String> arrayList, ArrayList<String> arrayList2){
        arrayListTarih=arrayList2;
        arrayListe=arrayList;
    }

    @NonNull
    @Override
    public Adapter.Hodor onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.olaylar_recycler,viewGroup,false);
        return new Hodor(v);
}
    @Override
    public void onBindViewHolder(@NonNull Adapter.Hodor viewHolder, int i) {
        viewHolder.textViewEvent.setText(arrayListe.get(i));
        viewHolder.textViewTarih.setText(arrayListTarih.get(i));
    }

    @Override
    public int getItemCount() {
        return arrayListe.size();
    }

    class Hodor extends RecyclerView.ViewHolder {
        TextView textViewEvent;
        TextView textViewTarih;
        public Hodor(View v) {
            super(v);
            textViewTarih=v.findViewById(R.id.tarih);
            textViewEvent=v.findViewById(R.id.textView_event);
        }
    }
}

