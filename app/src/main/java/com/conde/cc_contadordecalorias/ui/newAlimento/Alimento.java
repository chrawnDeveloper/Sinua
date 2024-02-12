package com.conde.cc_contadordecalorias.ui.newAlimento;

import java.io.Serializable;

public class Alimento implements Serializable {

    private Long id;
    private String nomeAlimento;
    private String caloriaAlimento;

    private String dataAlimento;

    public String getDataAlimento() {
        return dataAlimento;
    }

    public void setDataAlimento(String dataAlimento) {
        this.dataAlimento = dataAlimento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeAlimento() {
        return nomeAlimento;
    }

    public void setNomeAlimento(String nomeAlimento) {
        this.nomeAlimento = nomeAlimento;
    }

    public String getCaloriaAlimento() {
        return caloriaAlimento;
    }

    public void setCaloriaAlimento(String caloriaAlimento) {
        this.caloriaAlimento = caloriaAlimento;
    }
}
