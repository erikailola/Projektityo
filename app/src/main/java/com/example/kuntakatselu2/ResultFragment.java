package com.example.kuntakatselu2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResultFragment extends Fragment {

    private static final String ARG_SEARCH_QUERY = "search_query";

    private TextView resultArea;
    private TextView employmentRateArea;
    private TextView jobSelfSufficiencyArea;
    private TextView ageGenderDistributionArea;
    private LineChart populationChart;
    private LineChart employmentRateChart;
    private LineChart jobSelfSufficiencyChart;

    public static ResultFragment newInstance(String searchQuery) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SEARCH_QUERY, searchQuery);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        resultArea = view.findViewById(R.id.resultArea);
        employmentRateArea = view.findViewById(R.id.employmentRateArea);
        jobSelfSufficiencyArea = view.findViewById(R.id.jobSelfSufficiencyArea);
        ageGenderDistributionArea = view.findViewById(R.id.ageGenderDistributionArea);
        populationChart = view.findViewById(R.id.populationChart);
        employmentRateChart = view.findViewById(R.id.employmentRateChart);
        jobSelfSufficiencyChart = view.findViewById(R.id.jobSelfSufficiencyChart);

        initializeCharts();

        String searchQuery = getArguments().getString(ARG_SEARCH_QUERY);
        fetchData(searchQuery);

        return view;
    }

    private void initializeCharts() {
        Description desc = new Description();
        desc.setText("");

        populationChart.setDescription(desc);
        populationChart.setNoDataText("No population data available");

        employmentRateChart.setDescription(desc);
        employmentRateChart.setNoDataText("No employment rate data available");

        jobSelfSufficiencyChart.setDescription(desc);
        jobSelfSufficiencyChart.setNoDataText("No job self-sufficiency data available");
    }

    private void fetchData(String searchQuery) {
        fetchPopulationData(searchQuery);
        fetchEmploymentRateData(searchQuery);
        fetchJobSelfSufficiencyData(searchQuery);
        fetchAgeGenderDistributionData(searchQuery);
    }

    private void fetchPopulationData(String areaCode) {
        String apiUrl = "https://pxdata.stat.fi/PXWeb/api/v1/en/StatFin/vaerak/statfin_vaerak_pxt_11re.px";
        String query = "{\"query\":[{\"code\":\"Alue\",\"selection\":{\"filter\":\"item\",\"values\":[\"" + areaCode + "\"]}}],\"response\":{\"format\":\"json-stat2\"}}";

        DataRetriever.fetchData(apiUrl, query, new DataRetriever.DataFetchListener() {
            @Override
            public void onDataFetched(JSONObject result) {
                try {
                    JSONArray populationArray = result.getJSONArray("value");
                    JSONObject yearObject = result.getJSONObject("dimension").getJSONObject("Vuosi").getJSONObject("category").getJSONObject("index");

                    List<Entry> populationEntries = new ArrayList<>();
                    int totalPopulation = 0;
                    for (int i = 0; i < populationArray.length(); i++) {
                        int population = populationArray.getInt(i);
                        populationEntries.add(new Entry(i, population));
                        totalPopulation += population;
                    }

                    updatePopulationChart(populationEntries);
                    resultArea.setText("Total Population: " + totalPopulation);
                } catch (Exception e) {
                    resultArea.setText("Error parsing population data: " + e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                resultArea.setText("Error fetching population data: " + error);
            }
        });
    }

    private void fetchEmploymentRateData(String areaCode) {
        String apiUrl = "https://pxdata.stat.fi/PXWeb/api/v1/en/StatFin/tyokay/statfin_tyokay_pxt_115x.px";
        String query = "{\"query\":[{\"code\":\"Alue\",\"selection\":{\"filter\":\"item\",\"values\":[\"" + areaCode + "\"]}},{\"code\":\"Tiedot\",\"selection\":{\"filter\":\"item\",\"values\":[\"tyollisyysaste\"]}}],\"response\":{\"format\":\"json-stat2\"}}";

        DataRetriever.fetchData(apiUrl, query, new DataRetriever.DataFetchListener() {
            @Override
            public void onDataFetched(JSONObject result) {
                try {
                    JSONArray employmentRateArray = result.getJSONArray("value");
                    JSONObject yearObject = result.getJSONObject("dimension").getJSONObject("Vuosi").getJSONObject("category").getJSONObject("index");

                    List<Entry> employmentRateEntries = new ArrayList<>();
                    double totalEmploymentRate = 0;
                    int count = 0;
                    for (int i = 0; i < employmentRateArray.length(); i++) {
                        double rate = employmentRateArray.getDouble(i);
                        employmentRateEntries.add(new Entry(i, (float) rate));
                        totalEmploymentRate += rate;
                        count++;
                    }

                    updateEmploymentRateChart(employmentRateEntries);
                    double averageEmploymentRate = totalEmploymentRate / count;
                    employmentRateArea.setText(String.format("Average Employment Rate: %.2f%%", averageEmploymentRate));
                } catch (Exception e) {
                    employmentRateArea.setText("Error parsing employment rate data: " + e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                employmentRateArea.setText("Error fetching employment rate data: " + error);
            }
        });
    }

    private void fetchJobSelfSufficiencyData(String areaCode) {
        String apiUrl = "https://pxdata.stat.fi/PXWeb/api/v1/en/StatFin/tyokay/statfin_tyokay_pxt_125s.px";
        String query = "{\"query\":[{\"code\":\"Alue\",\"selection\":{\"filter\":\"item\",\"values\":[\"" + areaCode + "\"]}},{\"code\":\"Tiedot\",\"selection\":{\"filter\":\"item\",\"values\":[\"tyopaikkaomavaraisuus\"]}}],\"response\":{\"format\":\"json-stat2\"}}";

        DataRetriever.fetchData(apiUrl, query, new DataRetriever.DataFetchListener() {
            @Override
            public void onDataFetched(JSONObject result) {
                try {
                    JSONArray selfSufficiencyArray = result.getJSONArray("value");
                    JSONObject yearObject = result.getJSONObject("dimension").getJSONObject("Vuosi").getJSONObject("category").getJSONObject("index");

                    List<Entry> selfSufficiencyEntries = new ArrayList<>();
                    double totalSelfSufficiency = 0;
                    int count = 0;
                    for (int i = 0; i < selfSufficiencyArray.length(); i++) {
                        double rate = selfSufficiencyArray.getDouble(i);
                        selfSufficiencyEntries.add(new Entry(i, (float) rate));
                        totalSelfSufficiency += rate;
                        count++;
                    }

                    updateJobSelfSufficiencyChart(selfSufficiencyEntries);
                    double averageSelfSufficiency = totalSelfSufficiency / count;
                    jobSelfSufficiencyArea.setText(String.format("Average Job Self-Sufficiency: %.2f%%", averageSelfSufficiency));
                } catch (Exception e) {
                    jobSelfSufficiencyArea.setText("Error parsing job self-sufficiency data: " + e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                jobSelfSufficiencyArea.setText("Error fetching job self-sufficiency data: " + error);
            }
        });
    }

    private void fetchAgeGenderDistributionData(String areaCode) {
        String apiUrl = "https://pxdata.stat.fi/PXWeb/api/v1/en/StatFin/vaerak/statfin_vaerak_pxt_11re.px";
        String query = "{\"query\":[{\"code\":\"Alue\",\"selection\":{\"filter\":\"item\",\"values\":[\"" + areaCode + "\"]}},{\"code\":\"Sukupuoli\",\"selection\":{\"filter\":\"item\",\"values\":[\"1\",\"2\"]}},{\"code\":\"Ikä\",\"selection\":{\"filter\":\"item\",\"values\":[\"SSS\"]}}],\"response\":{\"format\":\"json-stat2\"}}";

        DataRetriever.fetchData(apiUrl, query, new DataRetriever.DataFetchListener() {
            @Override
            public void onDataFetched(JSONObject result) {
                try {
                    JSONArray dataArray = result.getJSONArray("value");
                    JSONObject genderObject = result.getJSONObject("dimension").getJSONObject("Sukupuoli").getJSONObject("category").getJSONObject("index");
                    Iterator<String> genderKeys = genderObject.keys();

                    int malePopulation = 0;
                    int femalePopulation = 0;

                    while (genderKeys.hasNext()) {
                        String gender = genderKeys.next();
                        int genderIndex = genderObject.getInt(gender);
                        int population = dataArray.getInt(genderIndex);

                        if (gender.equals("1")) {
                            malePopulation = population;
                        } else if (gender.equals("2")) {
                            femalePopulation = population;
                        }
                    }

                    int totalPopulation = malePopulation + femalePopulation;
                    double malePercentage = (double) malePopulation / totalPopulation * 100;
                    double femalePercentage = (double) femalePopulation / totalPopulation * 100;

                    String ageGenderData = String.format("Male Population: %d (%.2f%%)\nFemale Population: %d (%.2f%%)",
                            malePopulation, malePercentage, femalePopulation, femalePercentage);

                    ageGenderDistributionArea.setText(ageGenderData);
                } catch (Exception e) {
                    ageGenderDistributionArea.setText("Error parsing age and gender distribution data: " + e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                ageGenderDistributionArea.setText("Error fetching age and gender distribution data: " + error);
            }
        });
    }
    private void customizeChart(LineChart chart, String label, int color) {
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(true);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);
        chart.setDrawGridBackground(false);
        chart.setMaxHighlightDistance(300f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(ContextCompat.getColor(requireContext(), R.color.selfSufficiencyColor));
        xAxis.setDrawAxisLine(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setTextColor(ContextCompat.getColor(requireContext(), R.color.selfSufficiencyColor));
        leftAxis.setDrawAxisLine(false);

        chart.getAxisRight().setEnabled(false);

        Legend legend = chart.getLegend();
        legend.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryColor));
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        chart.animateX(1500);
    }

    private LineDataSet createDataSet(List<Entry> entries, String label, int color) {
        LineDataSet dataSet = new LineDataSet(entries, label);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCubicIntensity(0.2f);
        dataSet.setDrawFilled(true);
        dataSet.setDrawCircles(false);
        dataSet.setLineWidth(2f);
        dataSet.setColor(color);
        dataSet.setFillColor(color);
        dataSet.setFillAlpha(60);
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
        return dataSet;
    }

    private void updatePopulationChart(List<Entry> entries) {
        LineDataSet dataSet = createDataSet(entries, "Population", ContextCompat.getColor(requireContext(), R.color.populationColor));
        LineData lineData = new LineData(dataSet);
        populationChart.setData(lineData);
        customizeChart(populationChart, "Population Trend", R.color.populationColor);
        populationChart.invalidate();
    }

    private void updateEmploymentRateChart(List<Entry> entries) {
        LineDataSet dataSet = createDataSet(entries, "Employment Rate", ContextCompat.getColor(requireContext(), R.color.employmentColor));
        LineData lineData = new LineData(dataSet);
        employmentRateChart.setData(lineData);
        customizeChart(employmentRateChart, "Employment Rate Trend", R.color.employmentColor);
        employmentRateChart.invalidate();
    }

    private void updateJobSelfSufficiencyChart(List<Entry> entries) {
        LineDataSet dataSet = createDataSet(entries, "Job Self-Sufficiency", ContextCompat.getColor(requireContext(), R.color.selfSufficiencyColor));
        LineData lineData = new LineData(dataSet);
        jobSelfSufficiencyChart.setData(lineData);
        customizeChart(jobSelfSufficiencyChart, "Job Self-Sufficiency Trend", R.color.selfSufficiencyColor);
        jobSelfSufficiencyChart.invalidate();
    }
}