package com.example.diego_000.chatsocket;

import java.net.*;
import java.io.*;
import java.util.*;

public class ServidorChat {

  public static List<Socket> listaClientes;

  public static void main(String[] args){
    ServerSocket servidor = null;
    listaClientes = new ArrayList<Socket>();

    try{
      servidor = new ServerSocket(1234);
      while(true){
        Socket cliente = servidor.accept();
        listaClientes.add(cliente);
        BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        String nickname = in.readLine();
        System.out.println(nickname+" entró a la sala de Chat");
        HiloXCliente hiloCliente = new HiloXCliente(cliente, nickname);
        hiloCliente.start();
      }
    }
    catch(Exception e){
      System.out.println("Error: "+e.getMessage());
    }
    finally{
      try{ servidor.close(); } catch(Exception e){ }
    }

  }

}


class HiloXCliente extends Thread{
      Socket cliente;
      BufferedReader in;
      PrintWriter out;
      String nickname;

      public HiloXCliente(Socket cliente, String nickname){
        this.cliente = cliente;
        this.nickname = nickname;
      }

      public void run(){
        try{
          in = new BufferedReader(new InputStreamReader(this.cliente.getInputStream()));
          out = new PrintWriter(this.cliente.getOutputStream(), true);
          out.println("¡Bienvenido a la Sala de Chat!");
          String eco = "";
          while(!eco.equals("bye")){
            eco = in.readLine();
            System.out.println("["+nickname+"]: "+eco);

            for(int i=0;i<ServidorChat.listaClientes.size();i++){
              out = new PrintWriter((ServidorChat.listaClientes.get(i)).getOutputStream(), true);
              out.println("["+nickname+"]: "+eco);
            }

          }
        }
        catch(Exception e){
          System.out.println("Error: "+e.getMessage());
        }
        finally{
          try{ this.cliente.close(); } catch(Exception e){ }
        }

      }

}
