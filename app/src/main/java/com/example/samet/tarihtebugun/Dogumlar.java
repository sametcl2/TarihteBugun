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
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;

public class Dogumlar extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<String> arrayList;
    ArrayList<String> arrayListTarih;
    int sayac;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.dogumlar,container,false);
        Fetch fetch=new Fetch();
        fetch.execute("https://www.onthisday.com/today/birthdays.php");
        recyclerView=view.findViewById(R.id.recyclerView2);
        arrayList=new ArrayList<>();
        arrayListTarih=new ArrayList<>();
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        sayac=0;
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

    class Fetch extends AsyncTask<String,Void ,Void> {

        Elements elements;

        @Override
        protected Void doInBackground(String... strings) {
            try {
                org.jsoup.nodes.Document document=Jsoup.connect(strings[0]).get();
                elements=document.select("li.event-list__item");
            } catch (IOException e) {
                Snackbar.make(view.findViewById(R.id.layoutD),"Bir Hata Oluştu...",Snackbar.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (Element element:elements)
                arrayList.add(element.ownText());
            for (Element element:elements)
                arrayListTarih.add(element.select("a[href]").text());
            adapter=new Adapter2(arrayList,arrayListTarih);
            recyclerView.setAdapter(adapter);
        }
    }
}
class Adapter2 extends RecyclerView.Adapter<Adapter2.viewHolder>{

    ArrayList<String> arrayList;
    ArrayList<String> arrayListTarih;

    public Adapter2(ArrayList<String> arrayList, ArrayList<String> arrayListTarih) {
        super();
        this.arrayList=arrayList;
        this.arrayListTarih=arrayListTarih;
    }

    class viewHolder extends RecyclerView.ViewHolder{
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
    public Adapter2.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dogumlar_recycler,viewGroup,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter2.viewHolder viewHolder, int i) {
        viewHolder.textView.setText(arrayList.get(i));
        viewHolder.textView2.setText(arrayListTarih.get(i));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
