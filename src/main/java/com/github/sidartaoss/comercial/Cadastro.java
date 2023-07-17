package com.github.sidartaoss.comercial;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cadastro {

    private static final Logger logger = Logger.getLogger(Consulta.class.getName());
    private static final Scanner ENTRADA = new Scanner(System.in);

    private static final String URL_CONEXAO = "jdbc:mysql://localhost:3306/comercial";
    private static final String USUARIO = "root";
    private static final String SENHA = "root";

    public static void main(String[] args) {
        String dml = """
            insert into venda (
                nome_cliente, 
                valor_total, 
                data_pagamento
            ) values (?, ?, ?)
            """;
        try (Connection conexao = DriverManager.getConnection(URL_CONEXAO, USUARIO, SENHA);
             PreparedStatement comandoPreparado = conexao.prepareStatement(dml, Statement.RETURN_GENERATED_KEYS)) {
            try {
                conexao.setAutoCommit(false);
                do {
                    System.out.print("Nome: ");
                    String nome = ENTRADA.nextLine();

                    System.out.print("Valor total: ");
                    String valorTotal = ENTRADA.nextLine();

                    System.out.print("Data de pagamento: ");
                    LocalDate dataPagamento = LocalDate.parse(ENTRADA.nextLine(),
                            DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                    comandoPreparado.setString(1, nome);
                    comandoPreparado.setBigDecimal(2, new BigDecimal(valorTotal));
                    comandoPreparado.setDate(3, Date.valueOf(dataPagamento));
                    comandoPreparado.executeUpdate();

                    ResultSet codigoResultSet = comandoPreparado.getGeneratedKeys();
                    codigoResultSet.next();
                    long codigoGerado = codigoResultSet.getLong(1);

                    System.out.printf("Venda cadastrada com código: %d!%n", codigoGerado);
                    System.out.print("Continuar? ");
                } while ("sim".equalsIgnoreCase(ENTRADA.nextLine()));
                conexao.commit();
            } catch (Exception e) {
                conexao.rollback();
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro cadastrando venda.", e);
        }
    }
}
