package com.churrascoapp.app;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import com.churrascoapp.controller.CompraController;
import com.churrascoapp.controller.EventoController;
import com.churrascoapp.controller.UsuarioController;
import com.churrascoapp.model.Churrasco;
import com.churrascoapp.model.Compra;
import com.churrascoapp.model.Usuario;

public class MainApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        UsuarioController uc = AppContext.get().usuarios();
        EventoController cc = AppContext.get().eventos();
        CompraController compc = AppContext.get().compras();

        System.out.println("=== Churrasco App (demo CLI) ===");

        while(true){
            System.out.println("\n1 Registrar usuario");
            System.out.println("2 Login");
            System.out.println("3 Criar churrasco");
            System.out.println("4 Listar churrascos");
            System.out.println("5 Registrar compra");
            System.out.println("0 Sair");
            System.out.print("Opção: ");
            String opc = sc.nextLine();
            if (opc.equals("0")) break;

            try{
                if (opc.equals("1")){
                    System.out.print("Nome: "); String nome = sc.nextLine();
                    System.out.print("Email: "); String email = sc.nextLine();
                    System.out.print("Senha: "); String senha = sc.nextLine();
                    System.out.print("Tipo (Usuário/Administrador): "); String tipo = sc.nextLine();
                    Usuario u = uc.cadastrar(nome, email, senha, tipo);
                    System.out.println("Registrado: " + u.getEmail());

                } else if (opc.equals("2")){
                    System.out.print("Email: "); String email = sc.nextLine();
                    System.out.print("Senha: "); String senha = sc.nextLine();
                    Usuario u = uc.authenticate(email, senha);
                    System.out.println(u==null ? "Credenciais inválidas" : "Logado: " + u.getEmail());

                } else if (opc.equals("3")){
                    System.out.print("Título: "); String t = sc.nextLine();
                    System.out.print("Data (AAAA-MM-DD): "); String d = sc.nextLine();
                    System.out.print("Hora (HH:MM): "); String h = sc.nextLine();
                    System.out.print("Local: "); String l = sc.nextLine();
                    System.out.print("Tipo do evento: "); String tipoEvt = sc.nextLine();
                    System.out.print("Chave PIX (opcional): "); String pix = sc.nextLine();
                    System.out.print("Preço participante: "); double p = Double.parseDouble(sc.nextLine());
                    System.out.print("Descrição: "); String desc = sc.nextLine();

                    Churrasco churr = cc.criar(t, d, h, l, tipoEvt, pix, p, desc);
                    System.out.println("Criado: ID = " + churr.getId());

                } else if (opc.equals("4")){
                    List<Churrasco> lista = cc.listar();
                    for (Churrasco c: lista)
                        System.out.println(c.toCSV());

                } else if (opc.equals("5")){
                    System.out.print("Churrasco ID: "); String churrId = sc.nextLine();
                    System.out.print("Item: "); String item = sc.nextLine();
                    System.out.print("Quantidade: "); int qtd = Integer.parseInt(sc.nextLine());
                    System.out.print("Valor total: "); double val = Double.parseDouble(sc.nextLine());
                    System.out.print("Fornecedor (opcional): "); String forn = sc.nextLine();
                    System.out.print("Data (AAAA-MM-DD): "); String data = sc.nextLine();
                    System.out.print("Comprovante (path ou vazio): "); String comp = sc.nextLine();

                    Compra c = new Compra();
                    c.setChurrascoId(UUID.fromString(churrId));
                    c.setItem(item);
                    c.setQuantidade(qtd);
                    c.setValor(val);
                    c.setFornecedor(forn);
                    c.setData(data);
                    c.setComprovantePath(comp);

                    boolean ok = compc.registrar(c);
                    System.out.println(ok ? "Compra registrada com sucesso!" : "Erro ao registrar compra.");
                }

            } catch(Exception e){
                System.out.println("Erro: " + e.getMessage());
            }
        }

        sc.close();
        System.out.println("Saindo.");
    }
}

