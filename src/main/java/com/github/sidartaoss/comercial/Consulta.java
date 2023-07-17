package com.github.sidartaoss.comercial;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Consulta {

    private static final Logger logger = Logger.getLogger(Consulta.class.getName());
    private static final Scanner ENTRADA = new Scanner(System.in);

    private static final String URL_CONEXAO =
            "jdbc:mysql://localhost:3306/comercial";
    private static final String USUARIO =
            "root";
    private static final String SENHA =
            "root";

    public static void main(String[] args) {
        System.out.print("Pesquisa por nome: ");
        String nomePesquisa = ENTRADA.nextLine();
        String query = "select * from venda where nome_cliente like ?";
        try (Connection conexao = DriverManager.getConnection(URL_CONEXAO, USUARIO, SENHA);
             PreparedStatement comandoPreparado = conexao.prepareStatement(query)) {
            comandoPreparado.setString(1, "%" + nomePesquisa + "%");
            ResultSet resultado = comandoPreparado.executeQuery();
            while (resultado.next()) {
                Long id = resultado.getLong("id");
                String nomeCliente = resultado.getString("nome_cliente");
                BigDecimal valorTotal = resultado.getBigDecimal("valor_total");
                Date dataPagamento = resultado.getDate("data_pagamento");
                System.out.printf("%d - %s R$ %.2f - %s%n",
                        id, nomeCliente, valorTotal, dataPagamento);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro de banco de dados.", e);
        }
    }
}
