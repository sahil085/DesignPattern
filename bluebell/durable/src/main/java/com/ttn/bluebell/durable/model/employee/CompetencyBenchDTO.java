package com.ttn.bluebell.durable.model.employee;

public class CompetencyBenchDTO {
    private String competency;
    private int bench;


    public CompetencyBenchDTO(String competency, int bench) {
        this.competency = competency;
        this.bench = bench;
    }

    public CompetencyBenchDTO() {
    }

    public String getCompetency() {
        return competency;
    }

    public void setCompetency(String competency) {
        this.competency = competency;
    }

    public int getBench() {
        return bench;
    }

    public void setBench(int bench) {
        this.bench = bench;
    }
}
