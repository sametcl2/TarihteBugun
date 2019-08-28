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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;

public class Olumler extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> arrayList;
    ArrayList<String> arrayListTarih;
    int sayac;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.olumler,container,false);
        Fetch fetch=new Fetch();
        fetch.execute("https://www.onthisday.com/today/deaths.php");
        recyclerView=view.findViewById(R.id.recyclerView3);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        arrayList=new ArrayList<>();
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        arrayListTarih=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        sayac=0;
        return view;
    }

    class Fetch extends AsyncTask<String, Void, Void>{
        Elements elements;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (Element element:elements)
                arrayList.add(element.ownText());
            for (Element element:elements)
                arrayListTarih.add(element.select("a[href]").text());
            adapter=new Adapter3(arrayList, arrayListTarih);
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                org.jsoup.nodes.Document document=Jsoup.connect(strings[0]).get();
                elements=document.select("li.person");
            } catch (IOException e) {
                Snackbar.make(view.findViewById(R.id.layout),"Bir sorun oluştu",Snackbar.LENGTH_LONG).show();
            }
            return null;
        }
    }
}
class Adapter3 extends RecyclerView.Adapter<Adapter3.viewHolder>{

    ArrayList<String> arrayList;
    ArrayList<String> arrayListTarih;

    public Adapter3(ArrayList<String> arrayList, ArrayList<String> arrayList2){
        super();
        this.arrayList=arrayList;
        this.arrayListTarih=arrayList2;

    }

    class viewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView2;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            textView2=itemView.findViewById(R.id.textViewTarih);
            textView=itemView.findViewById(R.id.textView);
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.olumler_recycler,viewGroup,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        viewHolder.textView.setText(arrayList.get(i));
        viewHolder.textView2.setText(arrayListTarih.get(i));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
