package com.conde.cc_contadordecalorias.ui.history;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.conde.cc_contadordecalorias.R;
import com.conde.cc_contadordecalorias.adapter.Adapter;
import com.conde.cc_contadordecalorias.databinding.FragmentHistoryBinding;
import com.conde.cc_contadordecalorias.helper.AlimentoDAO;
import com.conde.cc_contadordecalorias.helper.RecyclerItemClickListener;
import com.conde.cc_contadordecalorias.ui.newAlimento.Alimento;
import com.conde.cc_contadordecalorias.ui.newAlimento.NewAlimentoActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private List<Alimento> listaAlimentos = new ArrayList<>();
    private Alimento alimentoSelecionado;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerView;

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {//Editar item do histórico
                        //Recuperar tarefa

                        Alimento alimentoSelecionado = listaAlimentos.get(position);
                        Intent intent = new Intent(getActivity(), NewAlimentoActivity.class);
                        intent.putExtra("alimentoSelecionado", alimentoSelecionado); //Key e valor
                        startActivity(intent);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) { //Excluir item do histórico
                        alimentoSelecionado = listaAlimentos.get(position);

                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity()); //Alert Dialog

                        dialog.setTitle("Excluir");
                        dialog.setMessage(Html.fromHtml("Deseja excluir o item " + "<b>" + alimentoSelecionado.getNomeAlimento() + "</b>" + "?"));

                        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                AlimentoDAO alimentoDAO = new AlimentoDAO(getContext());

                                if (alimentoDAO.deletar(alimentoSelecionado)) {
                                    carregarLista();
                                    Toast.makeText(getContext(), "Item excluído", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Erro ao excluir item", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                        dialog.setNegativeButton("Não", null);

                        dialog.create();
                        dialog.show(); //Mostra a dialog

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
                )
        );

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_historico, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.i_apagar) {
            AlertDialog.Builder dialog2 = new AlertDialog.Builder(getActivity()); //Alert Dialog

            dialog2.setTitle("Limpar histórico");
            dialog2.setMessage(Html.fromHtml("Tem certeza que deseja " + "<b>" + "apagar tudo" + "</b>" + "?"));
            dialog2.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlimentoDAO alimentoDAO = new AlimentoDAO(getContext());
                    if (alimentoDAO.deletarTudo()) {
                        carregarLista();
                        Toast.makeText(getContext(), "Histórico deletado.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Erro ao deletar histórico", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog2.setNegativeButton("Não", null);
            dialog2.create();
            dialog2.show();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        carregarLista();
        super.onStart();
    }

    public void carregarLista() {

        AlimentoDAO alimentoDAO = new AlimentoDAO(getContext());
        listaAlimentos = alimentoDAO.listar();
        Collections.reverse(listaAlimentos);
        //adapter
        adapter = new Adapter(listaAlimentos);
        //RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext()); //Layout Manager
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}