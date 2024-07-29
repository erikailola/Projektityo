package com.example.kuntakatselu2.model;


public class MunicipalityData {
    private String name;
    private int population;
    private double employmentRate;
    private double jobSelfSufficiency;
    // Add other relevant data fields

    // Constructor, getters, and setters

    public MunicipalityData(String name, int population, double employmentRate, double jobSelfSufficiency) {
        this.name = name;
        this.population = population;
        this.employmentRate = employmentRate;
        this.jobSelfSufficiency = jobSelfSufficiency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public double getEmploymentRate() {
        return employmentRate;
    }

    public void setEmploymentRate(double employmentRate) {
        this.employmentRate = employmentRate;
    }

    public double getJobSelfSufficiency() {
        return jobSelfSufficiency;
    }

    public void setJobSelfSufficiency(double jobSelfSufficiency) {
        this.jobSelfSufficiency = jobSelfSufficiency;
    }
}