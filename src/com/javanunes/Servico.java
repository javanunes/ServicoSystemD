/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javanunes;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.File;
import static java.lang.System.exit;
import java.util.Scanner;

/**
 *
 * @author ricardo
 */
public class Servico {
    
    static final String caminhoFile = "/etc/systemd/system/";
    
    static String colheEventoDisparadorMenu(){
        int opcao = 0;
        Scanner teclado = new Scanner(System.in);
        String[] eventos = {"network-online.target", "multi-user.target", "graphical.target", "sysinit.target"};
        System.out.println("Digite o numero do evento que acionará seu servico: ");
        for(String evento : eventos){
            System.out.printf("[%d] %s | ", opcao, evento);
            opcao ++;
        }
        try{
          opcao = teclado.nextInt();
          return eventos[opcao];
        }
        catch(Exception e){
          System.out.println("Evento inválido, retornarei multi-user.target");
          return "multi-user.target";
        }
    }
    
    
    static void escreve(String nome, String usuario, String comandoInicial, String comandoFinal, String descricao){
        try{
            
            if(nome.isEmpty() || comandoInicial.isEmpty()){
               System.out.println("Mínimo necessário não oferecido, saindo...");
               exit(0);
            }
            
            if(nome.indexOf(" ") > -1 || nome.indexOf(".") > -1){
               System.out.println("Esse nome não pode conter espaços ou pontos, saindo...");
               exit(0);
            }
            
            String eventoDisparador = colheEventoDisparadorMenu();
            File fl = new File(caminhoFile+nome+".service");
            FileWriter fw = new FileWriter(fl);
            final String nln= "\n";
            
            if(descricao.isEmpty()){
                descricao = nome;
            }
            
            fw.write("[Unit]"+nln);
            fw.write("  Description = "+descricao+nln);
            fw.write("  Before="+eventoDisparador+nln);
            fw.write(nln+nln);
            fw.write("[Service]"+nln);
            fw.write("  Type = Forking"+nln);
            fw.write("  ExecStart = "+ comandoInicial+nln);
            fw.write("  ExecStop = "+comandoFinal+nln);
            fw.write("  User = "+usuario+nln);
            fw.write("  Group = "+usuario+nln);
            fw.write("  RestartSec = 10s"+nln);
            fw.write(nln+nln);
            fw.write("[Install]"+nln);       
            fw.write("  WantedBy="+eventoDisparador+nln); 
            fw.write("");  
            fw.close();
            System.out.println(nln+nln+"Pronto, use systemctl enable "+nome +nln +"para habilitar o serviço no systemd");
        }
        catch(Exception e){
            
        }
    }
    
    
    static void dialoga(){
        String quemIniciou = System.getenv("USER");
        if(quemIniciou.equals("root")){
            String nomeArquivo = "";
            String usuario = "";
            String comandoInicial = "";
            String comandoFinal ="";
            String descricao = "";
            Scanner teclado = new Scanner(System.in);
            System.out.println("O serviço se chamará : ");
            nomeArquivo = teclado.nextLine();
            System.out.println("Rodar como o usário : ");
            usuario =  teclado.nextLine();
            System.out.println("Comando a ser executado no start : ");
            comandoInicial = teclado.nextLine();
            System.out.println("Comando a ser executado no fim: ");
            comandoFinal = teclado.nextLine();
            System.out.println("Digite uma breve descrição desse serviço");
            descricao = teclado.nextLine();
            escreve(nomeArquivo, usuario, comandoInicial, comandoFinal, descricao); 
        }
        else{
            System.out.println("\nErro: esse programa deve ser executado como root, saindo... \n");
        }
    }
    
    public static void main(String[] args) {
          dialoga();
    }
    
}
