
package com.churrascoapp.dao;

import com.churrascoapp.model.Pagamento;
import com.churrascoapp.utils.ArquivoUtils;
import java.io.IOException;
import java.util.*;

public class PagamentoDAO {
    private final String path = "data/pagamentos.csv";

    public void salvar(Pagamento p) throws IOException {
        ArquivoUtils.appendLine(path, p.toCSV());
    }

    public List<Pagamento> listar() throws IOException {
        List<String> lines = ArquivoUtils.readAllLines(path);
        List<Pagamento> res = new ArrayList<>();
        for (String l: lines){
            Pagamento p = Pagamento.fromCSV(l);
            if (p != null) res.add(p);
        }
        return res;
    }

    public Pagamento buscarPorId(String id) throws IOException {
        for (Pagamento p: listar()){
            if (p.getId().equals(id)) return p;
        }
        return null;
    }
}
