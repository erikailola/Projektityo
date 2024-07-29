package com.example.kuntakatselu2;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComparisonFragment extends Fragment {

    private Spinner municipality1Spinner;
    private Spinner municipality2Spinner;
    private TextView comparisonResultArea;
    private LineChart populationComparisonChart;
    private LineChart employmentRateComparisonChart;
    private Map<String, String> municipalityMap;
    private int startYear = 2000;
    private int latestPopulation1;
    private int latestPopulation2;
    private double latestEmploymentRate1;
    private double latestEmploymentRate2;
    private TableLayout comparisonTable;
    private int dataFetchCounter = 0;
    private ProgressBar loadingIndicator;
    private static final int TOTAL_DATA_SOURCES = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comparison, container, false);

        municipality1Spinner = view.findViewById(R.id.municipality1Spinner);
        municipality2Spinner = view.findViewById(R.id.municipality2Spinner);
        comparisonResultArea = view.findViewById(R.id.comparisonResultArea);
        populationComparisonChart = view.findViewById(R.id.populationComparisonChart);
        employmentRateComparisonChart = view.findViewById(R.id.employmentRateComparisonChart);
        loadingIndicator = view.findViewById(R.id.loadingIndicator);

        Button compareButton = view.findViewById(R.id.compareButton);

        initializeMunicipalityMap();
        setupSpinners();
        initializeCharts();

        compareButton.setOnClickListener(v -> compareMunicipalities());
        comparisonTable = view.findViewById(R.id.comparisonTable);

        return view;
    }

    private void initializeMunicipalityMap() {
        municipalityMap = new HashMap<>();
        municipalityMap.put("Helsinki", "KU091");
        municipalityMap.put("Espoo", "KU049");
        municipalityMap.put("Tampere", "KU837");
        municipalityMap.put("Vantaa", "KU092");
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
        municipalityMap.put("Lohja", "KU444");
        municipalityMap.put("Imatra", "KU153");
        municipalityMap.put("Savonlinna", "KU740");
        municipalityMap.put("Riihimäki", "KU686");
        municipalityMap.put("Vihti", "KU927");
        municipalityMap.put("Kaarina", "KU202");
        municipalityMap.put("Nokia", "KU536");
        municipalityMap.put("Salo", "KU734");
        municipalityMap.put("Kangasala", "KU211");
        municipalityMap.put("Raisio", "KU680");
        municipalityMap.put("Karhula", "KU285");
        municipalityMap.put("Kajaani", "KU205");
        municipalityMap.put("Raisio", "KU680");
        municipalityMap.put("Kokkola", "KU272");
        municipalityMap.put("Kerava", "KU245");
        municipalityMap.put("Ylöjärvi", "KU980");
        municipalityMap.put("Hamina", "KU075");
        municipalityMap.put("Hollola", "KU108");
        municipalityMap.put("Valkeakoski", "KU576");
        municipalityMap.put("Siilinjärvi", "KU740");
        municipalityMap.put("Kaarina", "KU202");
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
        municipalityMap.put("Järvenpää", "KU186");
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
        municipalityMap.put("Sysmä", "KU591");
        municipalityMap.put("Kouvola", "KU286");
        municipalityMap.put("Valtimo", "KU480");
        municipalityMap.put("Lemi", "KU232");
        municipalityMap.put("Kaustinen", "KU272");
        municipalityMap.put("Liperi", "KU720");
        municipalityMap.put("Juva", "KU578");
        municipalityMap.put("Kyyjärvi", "KU317");
        municipalityMap.put("Orimattila", "KU560");
        municipalityMap.put("Lumijoki", "KU619");
        municipalityMap.put("Karijoki", "KU676");

    }

    private void setupSpinners() {
        List<String> municipalityNames = new ArrayList<>(municipalityMap.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, municipalityNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        municipality1Spinner.setAdapter(adapter);
        municipality2Spinner.setAdapter(adapter);
    }

    private void initializeCharts() {
        setupChart(populationComparisonChart, "Population Comparison");
        setupChart(employmentRateComparisonChart, "Employment Rate Comparison");
    }

    private void setupChart(LineChart chart, String title) {
        Description desc = new Description();
        desc.setText(title);
        chart.setDescription(desc);
        chart.setNoDataText("No data available");
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawLabels(false);
    }

    private void compareMunicipalities() {
        String municipality1 = municipalityMap.get(municipality1Spinner.getSelectedItem().toString());
        String municipality2 = municipalityMap.get(municipality2Spinner.getSelectedItem().toString());

        if (municipality1 != null && municipality2 != null) {
            comparisonTable.removeAllViews();
            loadingIndicator.setVisibility(View.VISIBLE); // Show loading indicator
            fetchComparisonData(municipality1, municipality2);
        } else {
            comparisonResultArea.setText("Please select both municipalities.");
        }
    }

    private void fetchComparisonData(String municipality1, String municipality2) {
        dataFetchCounter = 0;
        comparisonTable.removeAllViews();
        fetchPopulationData(municipality1, municipality2);
        fetchEmploymentRateData(municipality1, municipality2);
        fetchAgeGenderData(municipality1, municipality2);
    }

    private void updateComparisonTableWithTotals() {
        String name1 = municipality1Spinner.getSelectedItem().toString();
        String name2 = municipality2Spinner.getSelectedItem().toString();

        comparisonTable.removeAllViews(); // Clear the table before adding new rows
        addHeaderRow(name1, name2);
        updateComparisonTable(name1, name2, "Total Population", latestPopulation1, latestPopulation2);
        updateComparisonTable(name1, name2, "Employment Rate (%)", latestEmploymentRate1, latestEmploymentRate2);
    }

    private void checkAllDataFetched() {
        dataFetchCounter++;
        if (dataFetchCounter == TOTAL_DATA_SOURCES) {
            updateComparisonTableWithTotals();
        }
        loadingIndicator.setVisibility(View.GONE);
    }

    private void fetchAgeGenderData(String municipality1, String municipality2) {
        String apiUrl = "https://pxdata.stat.fi/PXWeb/api/v1/en/StatFin/vaerak/statfin_vaerak_pxt_11re.px";
        String query = "{\"query\":[{\"code\":\"Alue\",\"selection\":{\"filter\":\"item\",\"values\":[\"" + municipality1 + "\",\"" + municipality2 + "\"]}},{\"code\":\"Sukupuoli\",\"selection\":{\"filter\":\"item\",\"values\":[\"1\",\"2\"]}},{\"code\":\"Ikä\",\"selection\":{\"filter\":\"item\",\"values\":[\"SSS\"]}}],\"response\":{\"format\":\"json-stat2\"}}";

        DataRetriever.fetchData(apiUrl, query, new DataRetriever.DataFetchListener() {
            @Override
            public void onDataFetched(JSONObject result) {
                try {
                    JSONArray dataArray = result.getJSONArray("value");
                    JSONObject areaObject = result.getJSONObject("dimension").getJSONObject("Alue").getJSONObject("category");
                    JSONObject indexObject = areaObject.getJSONObject("index");
                    JSONObject labelObject = areaObject.getJSONObject("label");

                    int index1 = indexObject.getInt(municipality1);
                    int index2 = indexObject.getInt(municipality2);
                    String name1 = labelObject.getString(municipality1);
                    String name2 = labelObject.getString(municipality2);

                    int male1 = dataArray.getInt(index1 * 2);
                    int female1 = dataArray.getInt(index1 * 2 + 1);
                    int male2 = dataArray.getInt(index2 * 2);
                    int female2 = dataArray.getInt(index2 * 2 + 1);

                    updateComparisonTable(name1, name2, "Males", male1, male2);
                    updateComparisonTable(name1, name2, "Females", female1, female2);
                    checkAllDataFetched();
                } catch (Exception e) {
                    comparisonResultArea.setText("Error parsing age and gender data: " + e.getMessage());
                    loadingIndicator.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String error) {
                comparisonResultArea.setText(getErrorMessage(error));
                loadingIndicator.setVisibility(View.GONE);
            }
        });
    }

    private void fetchPopulationData(String municipality1, String municipality2) {
        String apiUrl = "https://pxdata.stat.fi/PXWeb/api/v1/en/StatFin/vaerak/statfin_vaerak_pxt_11re.px";
        String query = "{\"query\":[{\"code\":\"Alue\",\"selection\":{\"filter\":\"item\",\"values\":[\"" + municipality1 + "\",\"" + municipality2 + "\"]}}],\"response\":{\"format\":\"json-stat2\"}}";

        DataRetriever.fetchData(apiUrl, query, new DataRetriever.DataFetchListener() {
            @Override
            public void onDataFetched(JSONObject result) {
                try {
                    JSONArray populationArray = result.getJSONArray("value");
                    JSONObject areaObject = result.getJSONObject("dimension").getJSONObject("Alue").getJSONObject("category");
                    JSONObject indexObject = areaObject.getJSONObject("index");
                    JSONObject labelObject = areaObject.getJSONObject("label");

                    List<Entry> populationEntries1 = new ArrayList<>();
                    List<Entry> populationEntries2 = new ArrayList<>();

                    int index1 = indexObject.getInt(municipality1);
                    int index2 = indexObject.getInt(municipality2);
                    String name1 = labelObject.getString(municipality1);
                    String name2 = labelObject.getString(municipality2);

                    for (int i = 0; i < populationArray.length() / 2; i++) {
                        populationEntries1.add(new Entry(i, populationArray.getInt(i * 2 + index1)));
                        populationEntries2.add(new Entry(i, populationArray.getInt(i * 2 + index2)));
                    }

                    // Store the latest population values
                    latestPopulation1 = populationArray.getInt(populationArray.length() - 2 + index1);
                    latestPopulation2 = populationArray.getInt(populationArray.length() - 2 + index2);

                    updatePopulationChart(populationEntries1, populationEntries2, name1, name2);
                    checkAllDataFetched();
                } catch (Exception e) {
                    comparisonResultArea.setText("Error parsing population data: " + e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                comparisonResultArea.setText(getErrorMessage(error));
                loadingIndicator.setVisibility(View.GONE);
            }
        });
    }

    private void fetchEmploymentRateData(String municipality1, String municipality2) {
        String apiUrl = "https://pxdata.stat.fi/PXWeb/api/v1/en/StatFin/tyokay/statfin_tyokay_pxt_115x.px";
        String query = "{\"query\":[{\"code\":\"Alue\",\"selection\":{\"filter\":\"item\",\"values\":[\"" + municipality1 + "\",\"" + municipality2 + "\"]}},{\"code\":\"Tiedot\",\"selection\":{\"filter\":\"item\",\"values\":[\"tyollisyysaste\"]}}],\"response\":{\"format\":\"json-stat2\"}}";

        DataRetriever.fetchData(apiUrl, query, new DataRetriever.DataFetchListener() {
            @Override
            public void onDataFetched(JSONObject result) {
                try {
                    JSONArray employmentRateArray = result.getJSONArray("value");
                    JSONObject areaObject = result.getJSONObject("dimension").getJSONObject("Alue").getJSONObject("category");
                    JSONObject indexObject = areaObject.getJSONObject("index");
                    JSONObject labelObject = areaObject.getJSONObject("label");

                    List<Entry> employmentRateEntries1 = new ArrayList<>();
                    List<Entry> employmentRateEntries2 = new ArrayList<>();

                    int index1 = indexObject.getInt(municipality1);
                    int index2 = indexObject.getInt(municipality2);
                    String name1 = labelObject.getString(municipality1);
                    String name2 = labelObject.getString(municipality2);

                    for (int i = 0; i < employmentRateArray.length() / 2; i++) {
                        employmentRateEntries1.add(new Entry(i, (float) employmentRateArray.getDouble(i * 2 + index1)));
                        employmentRateEntries2.add(new Entry(i, (float) employmentRateArray.getDouble(i * 2 + index2)));
                    }

                    // Store the latest employment rate values
                    latestEmploymentRate1 = employmentRateArray.getDouble(employmentRateArray.length() - 2 + index1);
                    latestEmploymentRate2 = employmentRateArray.getDouble(employmentRateArray.length() - 2 + index2);

                    updateEmploymentRateChart(employmentRateEntries1, employmentRateEntries2, name1, name2);
                    checkAllDataFetched(); // Add this line
                } catch (Exception e) {
                    comparisonResultArea.setText("Error parsing employment rate data: " + e.getMessage());
                    loadingIndicator.setVisibility(View.GONE); // Hide loading indicator on error
                }
            }

            @Override
            public void onError(String error) {
                comparisonResultArea.setText(getErrorMessage(error));
                loadingIndicator.setVisibility(View.GONE); // Hide loading indicator on error
            }
        });
    }

    private void updatePopulationChart(List<Entry> entries1, List<Entry> entries2, String name1, String name2) {
        LineDataSet dataSet1 = createLineDataSet(entries1, name1, Color.RED);
        LineDataSet dataSet2 = createLineDataSet(entries2, name2, Color.BLUE);

        LineData populationData = new LineData(dataSet1, dataSet2);
        populationComparisonChart.setData(populationData);
        populationComparisonChart.invalidate();
    }

    private void updateEmploymentRateChart(List<Entry> entries1, List<Entry> entries2, String name1, String name2) {
        LineDataSet dataSet1 = createLineDataSet(entries1, name1, Color.RED);
        LineDataSet dataSet2 = createLineDataSet(entries2, name2, Color.BLUE);

        LineData employmentRateData = new LineData(dataSet1, dataSet2);
        employmentRateComparisonChart.setData(employmentRateData);
        employmentRateComparisonChart.invalidate();
    }

    private LineDataSet createLineDataSet(List<Entry> entries, String label, int color) {
        LineDataSet dataSet = new LineDataSet(entries, label);
        dataSet.setColor(color);
        dataSet.setCircleColor(color);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        dataSet.setLineWidth(2f);
        return dataSet;
    }

    private String getErrorMessage(String error) {
        switch (error) {
            case "NETWORK_ERROR":
                return "Network error. Please check your internet connection.";
            case "INVALID_CITY":
                return "The selected city is not available in the data source.";
            case "DATA_NOT_FOUND":
                return "Data not found for the selected city.";
            default:
                return "An unknown error occurred: " + error;
        }
    }
    private void updateComparisonTable(String name1, String name2, String category, double value1, double value2) {
        TableRow row = new TableRow(getContext());
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView categoryView = new TextView(getContext());
        categoryView.setText(category);
        categoryView.setPadding(5, 5, 5, 5);

        TextView value1View = new TextView(getContext());
        TextView value2View = new TextView(getContext());

        if (category.equals("Total Population")) {
            value1View.setText(String.format("%,d", (int) value1));
            value2View.setText(String.format("%,d", (int) value2));
        } else {
            value1View.setText(String.format("%.2f", value1));
            value2View.setText(String.format("%.2f", value2));
        }

        value1View.setPadding(5, 5, 5, 5);
        value2View.setPadding(5, 5, 5, 5);

        row.addView(categoryView);
        row.addView(value1View);
        row.addView(value2View);

        comparisonTable.addView(row);

        // Add header row if it's the first row
        if (comparisonTable.getChildCount() == 1) {
            addHeaderRow(name1, name2);
        }
    }

    private void addHeaderRow(String name1, String name2) {
        TableRow headerRow = new TableRow(getContext());
        headerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView categoryHeader = new TextView(getContext());
        categoryHeader.setText("Category");
        categoryHeader.setPadding(5, 5, 5, 5);
        categoryHeader.setTypeface(null, Typeface.BOLD);

        TextView value1Header = new TextView(getContext());
        value1Header.setText(name1);
        value1Header.setPadding(5, 5, 5, 5);
        value1Header.setTypeface(null, Typeface.BOLD);

        TextView value2Header = new TextView(getContext());
        value2Header.setText(name2);
        value2Header.setPadding(5, 5, 5, 5);
        value2Header.setTypeface(null, Typeface.BOLD);

        headerRow.addView(categoryHeader);
        headerRow.addView(value1Header);
        headerRow.addView(value2Header);

        comparisonTable.addView(headerRow, 0);
    }

   
}