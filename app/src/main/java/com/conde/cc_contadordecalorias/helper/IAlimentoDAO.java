package com.conde.cc_contadordecalorias.helper;

import com.conde.cc_contadordecalorias.ui.newAlimento.Alimento;

import java.util.List;

public interface IAlimentoDAO {

    public boolean salvar(Alimento alimento);
    public boolean atualizar (Alimento alimento);
    public boolean deletar (Alimento alimento);
    public boolean deletarTudo();
    public List<Alimento> listar();
}
