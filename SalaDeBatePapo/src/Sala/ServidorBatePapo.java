package Sala;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServidorBatePapo extends Thread {
    
	private static Map<String, PrintStream> MAP_Usuarios;
    private Socket conexao;
    private String usuario;
    private static List<String> Nomes_Usuarios = new ArrayList<String>();
    
    public ServidorBatePapo(Socket socket) {
        this.conexao = socket;
    }
    public boolean armazenaNome(String novoUsuario) {
        for (int i = 0; i < Nomes_Usuarios.size(); i++) {
            if (Nomes_Usuarios.get(i).equals(novoUsuario))
                return true;
        }
        Nomes_Usuarios.add(novoUsuario);
        return false;
    }
    public void remove(String exUsuario) {
        for (int i = 0; i < Nomes_Usuarios.size(); i++) {
            if (Nomes_Usuarios.get(i).equals(exUsuario))
            	Nomes_Usuarios.remove(exUsuario);
        }
    }
    public static void main(String args[]) {
        MAP_Usuarios = new HashMap<String, PrintStream>();
        try {
            ServerSocket server = new ServerSocket(5555);
            System.out.println("ServidorSocket rodando na porta 5555");
            while (true) {
                Socket conexao = server.accept();
                Thread t = new ServidorBatePapo(conexao);
                t.start();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }
    public void run() {
        try {
            BufferedReader entrada =
                new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));
            PrintStream saida = new PrintStream(this.conexao.getOutputStream());
            this.usuario = entrada.readLine();
            if (armazenaNome(this.usuario)) {
                saida.println("Usuario existente! Conecte novamente com outro Nome de usuário.");
                this.conexao.close();
                return;
            } else {
                //Mostra o nome do usuario conectado ao servidor;
                System.out.println(this.usuario + " : Conectado ao Servidor!");
                //Quando o usuario se conectar recebe todos que estão conectados;
                saida.println("Conectados: " + Nomes_Usuarios.toString());
            }
            if (this.usuario == null) {
                return;
            }
            //Adiciona os dados de saida do ususario no objeto MAP_Usuarios
            //A chave será o nome e valor o printstream;
            MAP_Usuarios.put(this.usuario, saida);
            String[] msg = entrada.readLine().split(":");
            while (msg != null && !(msg[0].trim().equals(""))) {
                send(saida, " escreveu: ", msg);
                msg = entrada.readLine().split(":");
            }
            System.out.println(this.usuario + " saiu do sala!");
            String[] out = {" do sala!"};
            send(saida, " saiu", out);
            remove(this.usuario);
            MAP_Usuarios.remove(this.usuario);
            this.conexao.close();
        } catch (IOException e) {
            System.out.println("Falha na Coexao... .. ." + " IOException: " + e);
        }
    }
    
    public void send(PrintStream saida, String acao, String[] msg) {
        out:
        for (Map.Entry<String, PrintStream> cliente : MAP_Usuarios.entrySet()) {
            PrintStream chat = cliente.getValue();
            if (chat != saida) {
                if (msg.length == 1) {
                    chat.println(this.usuario + acao + msg[0]);
                } else {
                    if (msg[1].equalsIgnoreCase(cliente.getKey())) {
                        chat.println(this.usuario + acao + msg[0]);
                        break out;
                    }
                }
            }
        }
    }
}

