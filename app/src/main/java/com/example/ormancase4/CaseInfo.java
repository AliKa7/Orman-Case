package com.example.ormancase4;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.ArrayList;
public class CaseInfo extends AppCompatActivity {
    RecyclerView recyclerView;
    List<CaseItem> caseItemsList = new ArrayList<>();
    CaseItemAdapter adapter;
    String[] symptoms;
    ArrayAdapter<String> symptomAdapter;
    AutoCompleteTextView autoCompleteTextView;
    TextView adviceLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.case_info);
        recyclerView = findViewById(R.id.recyclerView);
        init();
        getSupportActionBar().setTitle("Состав ящика");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.white_back_arrow);
        adviceLabel = findViewById(R.id.adviceLabel);
        symptoms = new String[]{"Сильное кровотечение",
                "Термический ожог", "Отравление угарным газом", "Дезинфицировать рану",
                "Тепловой удар", "Проблемы с сердцем"};
        autoCompleteTextView = findViewById(R.id.autoCompleteText);
        symptomAdapter = new ArrayAdapter<String>(this, R.layout.symptom_item, symptoms);
        autoCompleteTextView.setAdapter(symptomAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                String advice = "";
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                switch(item) {
                    case "Сильное кровотечение":
                        layoutManager.smoothScrollToPosition(recyclerView, new RecyclerView.State(), 6);
                        advice = "Используйте жгут для перевязки раны"; break;
                    case "Термический ожог":
                        layoutManager.smoothScrollToPosition(recyclerView, new RecyclerView.State(), 3);
                        advice = "Используйте марлевые бинты и перекись водорода"; break;
                    case "Отравление угарным газом":
                        layoutManager.smoothScrollToPosition(recyclerView, new RecyclerView.State(), 9);
                        advice = "Используйте активированный уголь"; break;
                    case "Дезинфицировать рану":
                        layoutManager.smoothScrollToPosition(recyclerView, new RecyclerView.State(), 8);
                        advice = "Используйте лейкопластырь или бриллиантовый зеленый"; break;
                    case "Тепловой удар":
                        layoutManager.smoothScrollToPosition(recyclerView, new RecyclerView.State(), 2);
                        advice = "Используйте ацетилсалициловую кислоту"; break;
                    case "Проблемы с сердцем":
                        layoutManager.smoothScrollToPosition(recyclerView, new RecyclerView.State(), 4);
                        advice = "Используйте нитроглицерин"; break;
                }
                adviceLabel.setText(advice);
            }
        });
    }
    private void init() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        caseItemsList.add(new CaseItem(R.drawable.self_rescuirer, getResources().getString(R.string.item1name), getResources().getString(R.string.item1desc)));
        caseItemsList.add(new CaseItem(R.drawable.water_bottle, getResources().getString(R.string.item2name), getResources().getString(R.string.item2desc)));
        caseItemsList.add(new CaseItem(R.drawable.acid, getResources().getString(R.string.item3name), getResources().getString(R.string.item3desc)));
        caseItemsList.add(new CaseItem(R.drawable.bint, getResources().getString(R.string.item4name), getResources().getString(R.string.item4desc)));
        caseItemsList.add(new CaseItem(R.drawable.nitroglycerine, getResources().getString(R.string.item5name), getResources().getString(R.string.item5desc)));
        caseItemsList.add(new CaseItem(R.drawable.green_brilliant, getResources().getString(R.string.item6name), getResources().getString(R.string.item6desc)));
        caseItemsList.add(new CaseItem(R.drawable.wisp, getResources().getString(R.string.item7name), getResources().getString(R.string.item7desc)));
        caseItemsList.add(new CaseItem(R.drawable.peroxide, getResources().getString(R.string.item8name), getResources().getString(R.string.item8desc)));
        caseItemsList.add(new CaseItem(R.drawable.plaster, getResources().getString(R.string.item9name), getResources().getString(R.string.item9desc)));
        caseItemsList.add(new CaseItem(R.drawable.coal, getResources().getString(R.string.item10name), getResources().getString(R.string.item10desc)));
        caseItemsList.add(new CaseItem(R.drawable.ammiak, getResources().getString(R.string.item11name), getResources().getString(R.string.item11desc)));
        caseItemsList.add(new CaseItem(R.drawable.corvalol, getResources().getString(R.string.item12name), getResources().getString(R.string.item12desc)));
        caseItemsList.add(new CaseItem(R.drawable.validol, getResources().getString(R.string.item13name), getResources().getString(R.string.item13desc)));
        caseItemsList.add(new CaseItem(R.drawable.vata, getResources().getString(R.string.item14name), getResources().getString(R.string.item14desc)));

        adapter = new CaseItemAdapter(caseItemsList);
        recyclerView.setAdapter(adapter);
    }
}
