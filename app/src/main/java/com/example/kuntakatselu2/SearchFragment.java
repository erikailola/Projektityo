package com.example.kuntakatselu2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuntakatselu2.adapter.FavoriteAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment implements FavoriteAdapter.OnFavoriteClickListener {

    private AutoCompleteTextView searchField;
    private ImageButton favoriteButton;
    private RecyclerView lastSearchesRecyclerView;
    private FavoriteAdapter favoriteAdapter;
    private Map<String, String> municipalityMap;
    private List<String> municipalityCodes;
    private List<String> municipalityNames;
    private ArrayAdapter<String> searchAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchField = view.findViewById(R.id.searchField);
        favoriteButton = view.findViewById(R.id.favoriteButton);
        ImageButton searchButton = view.findViewById(R.id.searchButton);
        Button comparisonButton = view.findViewById(R.id.comparisonButton);
        Button municipalityListButton = view.findViewById(R.id.municipalityListButton);
        lastSearchesRecyclerView = view.findViewById(R.id.lastSearchesRecyclerView);

        favoriteAdapter = new FavoriteAdapter(getContext(), this);
        lastSearchesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        lastSearchesRecyclerView.setAdapter(favoriteAdapter);

        searchButton.setOnClickListener(v -> performSearch());
        favoriteButton.setOnClickListener(v -> toggleFavorite());
        comparisonButton.setOnClickListener(v -> openComparisonFragment());
        municipalityListButton.setOnClickListener(v -> showMunicipalityListDialog());

        initializeMunicipalityMap();
        initializeSearchField();

        return view;
    }

    private void performSearch() {
        String searchQuery = searchField.getText().toString().trim();
        if (!searchQuery.isEmpty()) {
            favoriteAdapter.addSearch(searchQuery);
            ((MainActivity) requireActivity()).loadFragment(ResultFragment.newInstance(searchQuery));
        }
    }

    private void toggleFavorite() {
        String searchQuery = searchField.getText().toString().trim();
        if (!searchQuery.isEmpty()) {
            boolean isFavorite = favoriteAdapter.toggleFavorite(searchQuery);
            favoriteButton.setImageResource(isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        }
    }

    private void openComparisonFragment() {
        ((MainActivity) requireActivity()).loadFragment(new ComparisonFragment());
    }

    private void showMunicipalityListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_municipality_list, null);
        builder.setView(dialogView);

        ListView municipalityListView = dialogView.findViewById(R.id.municipalityListView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, municipalityNames);
        municipalityListView.setAdapter(adapter);

        AlertDialog dialog = builder.create();
        dialog.show();

        municipalityListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedMunicipality = municipalityNames.get(position);
            String municipalityCode = municipalityMap.get(selectedMunicipality);
            searchField.setText(municipalityCode);
            dialog.dismiss();
        });
    }

    private void initializeMunicipalityMap() {
        municipalityMap = new HashMap<>();
        municipalityMap.put("Helsinki", "KU091");
        municipalityMap.put("Espoo", "KU049");
        municipalityMap.put("Tampere", "KU837");
        municipalityMap.put("Vantaa", "KU092");
        municipalityMap.put("Oulu", "KU564");
        municipalityMap.put("Turku", "KU853");
        municipalityMap.put("Jyväskylä", "KU179");
        municipalityMap.put("Lahti", "KU398");
        municipalityMap.put("Kuopio", "KU297");
        municipalityMap.put("Pori", "KU609");
        municipalityMap.put("Lappeenranta", "KU405");
        municipalityMap.put("Vaasa", "KU905");
        municipalityMap.put("Kouvola", "KU286");
        municipalityMap.put("Rovaniemi", "KU698");
        municipalityMap.put("Seinäjoki", "KU743");
        municipalityMap.put("Mikkeli", "KU491");
        municipalityMap.put("Kotka", "KU285");
        municipalityMap.put("Joensuu", "KU167");
        municipalityMap.put("Hämeenlinna", "KU109");
        municipalityMap.put("Porvoo", "KU638");
        municipalityMap.put("Hyvinkää", "KU106");
        municipalityMap.put("Järvenpää", "KU186");
        municipalityMap.put("Nurmijärvi", "KU543");
        municipalityMap.put("Rauma", "KU684");
        municipalityMap.put("Lohja", "KU444");
        municipalityMap.put("Karhula", "KU286");
        municipalityMap.put("Kajaani", "KU205");
        municipalityMap.put("Raisio", "KU680");
        municipalityMap.put("Kokkola", "KU272");
        municipalityMap.put("Kerava", "KU245");
        municipalityMap.put("Ylöjärvi", "KU980");
        municipalityMap.put("Imatra", "KU153");
        municipalityMap.put("Savonlinna", "KU740");
        municipalityMap.put("Riihimäki", "KU686");
        municipalityMap.put("Vihti", "KU927");
        municipalityMap.put("Kaarina", "KU202");
        municipalityMap.put("Nokia", "KU536");
        municipalityMap.put("Salo", "KU734");
        municipalityMap.put("Kangasala", "KU211");
        municipalityMap.put("Hamina", "KU075");
        municipalityMap.put("Hollola", "KU108");
        municipalityMap.put("Valkeakoski", "KU576");
        municipalityMap.put("Siilinjärvi", "KU740");
        municipalityMap.put("Varkaus", "KU922");
        municipalityMap.put("Raahe", "KU678");
        municipalityMap.put("Laukaa", "KU410");
        municipalityMap.put("Iisalmi", "KU140");
        municipalityMap.put("Pietarsaari", "KU499");
        municipalityMap.put("Heinola", "KU111");
        municipalityMap.put("Tornio", "KU851");
        municipalityMap.put("Akaa", "KU020");
        municipalityMap.put("Eura", "KU050");
        municipalityMap.put("Karkkila", "KU224");
        municipalityMap.put("Tammela", "KU834");
        municipalityMap.put("Orimattila", "KU560");
        municipalityMap.put("Lieto", "KU423");
        municipalityMap.put("Uusikaupunki", "KU895");
        municipalityMap.put("Paimio", "KU257");
        municipalityMap.put("Mäntsälä", "KU505");
        municipalityMap.put("Harjavalta", "KU079");
        municipalityMap.put("Naantali", "KU529");
        municipalityMap.put("Kemi", "KU240");
        municipalityMap.put("Forssa", "KU061");
        municipalityMap.put("Keuruu", "KU249");
        municipalityMap.put("Loimaa", "KU304");
        municipalityMap.put("Nivala", "KU535");
        municipalityMap.put("Nousiainen", "KU536");
        municipalityMap.put("Pornainen", "KU611");
        municipalityMap.put("Pudasjärvi", "KU614");
        municipalityMap.put("Kuhmo", "KU290");
        municipalityMap.put("Hanko", "KU149");
        municipalityMap.put("Kitee", "KU258");
        municipalityMap.put("Saarijärvi", "KU580");
        municipalityMap.put("Sastamala", "KU790");
        municipalityMap.put("Muurame", "KU495");
        municipalityMap.put("Uurainen", "KU908");
        municipalityMap.put("Mynämäki", "KU444");
        municipalityMap.put("Masku", "KU481");
        municipalityMap.put("Lapua", "KU408");
        municipalityMap.put("Pedersören kunta", "KU595");
        municipalityMap.put("Kauhajoki", "KU232");
        municipalityMap.put("Lieksa", "KU420");
        municipalityMap.put("Pyhäjärvi", "KU889");
        municipalityMap.put("Kristiinankaupunki", "KU297");
        municipalityMap.put("Oulainen", "KU564");
        municipalityMap.put("Alavus", "KU009");
        municipalityMap.put("Paltamo", "KU620");
        municipalityMap.put("Äänekoski", "KU992");
        municipalityMap.put("Orivesi", "KU631");
        municipalityMap.put("Ulvila", "KU630");
        municipalityMap.put("Eurajoki", "KU051");
        municipalityMap.put("Kankaanpää", "KU216");
        municipalityMap.put("Kärkölä", "KU213");
        municipalityMap.put("Pyhtää", "KU707");
        municipalityMap.put("Parikkala", "KU580");
        municipalityMap.put("Tohmajärvi", "KU468");
        municipalityMap.put("Lappajärvi", "KU408");
        municipalityMap.put("Sievi", "KU826");
        municipalityMap.put("Ilmajoki", "KU145");
        municipalityMap.put("Puolanka", "KU620");
        municipalityMap.put("Utajärvi", "KU564");
        municipalityMap.put("Hämeenkyrö", "KU105");
        municipalityMap.put("Joroinen", "KU790");
        municipalityMap.put("Humppila", "KU065");
        municipalityMap.put("Haapajärvi", "KU045");
        municipalityMap.put("Kaavi", "KU143");
        municipalityMap.put("Juankoski", "KU171");
        municipalityMap.put("Sysmä", "KU591");
        municipalityMap.put("Halsua", "KU048");
        municipalityMap.put("Suomussalmi", "KU809");
        municipalityMap.put("Juupajoki", "KU257");
        municipalityMap.put("Koski Tl", "KU326");
        municipalityMap.put("Inari", "KU320");
        municipalityMap.put("Jämsä", "KU179");
        municipalityMap.put("Marttila", "KU503");
        municipalityMap.put("Simo", "KU408");
        municipalityMap.put("Hirvensalmi", "KU401");
        municipalityMap.put("Siikajoki", "KU312");
        municipalityMap.put("Ranua", "KU840");
        municipalityMap.put("Polvijärvi", "KU505");
        municipalityMap.put("Virrat", "KU934");
        municipalityMap.put("Leppävirta", "KU925");
        municipalityMap.put("Pielavesi", "KU202");
        municipalityMap.put("Vehmaa", "KU934");
        municipalityMap.put("Tyrnävä", "KU541");
        municipalityMap.put("Sonkajärvi", "KU366");
        municipalityMap.put("Kinnula", "KU615");
        municipalityMap.put("Ähtäri", "KU844");
        municipalityMap.put("Karvia", "KU251");
        municipalityMap.put("Pöytyä", "KU722");
        municipalityMap.put("Valtimo", "KU480");
        municipalityMap.put("Lemi", "KU232");
        municipalityMap.put("Kaustinen", "KU272");
        municipalityMap.put("Liperi", "KU720");
        municipalityMap.put("Juva", "KU578");
        municipalityMap.put("Kyyjärvi", "KU317");
        municipalityMap.put("Lumijoki", "KU619");
        municipalityMap.put("Karijoki", "KU676");

        municipalityCodes = new ArrayList<>(municipalityMap.values());
        municipalityNames = new ArrayList<>(municipalityMap.keySet());
    }

    private void initializeSearchField() {
        searchAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, municipalityCodes);
        searchField.setAdapter(searchAdapter);
        searchField.setThreshold(1);

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim();

                if (municipalityMap.containsKey(input)) {
                    searchField.setError("Please enter the code, not the name.");
                } else {
                    searchField.setError(null);
                }

                searchAdapter.getFilter().filter(input);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onFavoriteClick(String favorite) {
        searchField.setText(favorite);
        performSearch();
    }
}
