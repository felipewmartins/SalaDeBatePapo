package Sala;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import javax.swing.JOptionPane;

public class UsuarioBatePapo extends Thread {
    
	// Controla a recepção de mensagens do usuario
    private Socket conexao;
    // Construtor que recebe o socket do usuario
    public UsuarioBatePapo(Socket socket) {
        this.conexao = socket;
    }
    
    public static void main(String args[])
    {
        try {
            //Instancia do atributo conexao do tipo Socket;
            //Conecta o IP do Servidor e a Porta;
            String ip = JOptionPane.showInputDialog("Digite o ip do servidor Ex: 127.0.0.1");
            Socket socket = new Socket(ip, 5555);
            //Instancia do atributo saida, obtem os objetos que permitem;
            //Controlar o fluxo de comunicação;
            PrintStream saida = new PrintStream(socket.getOutputStream());
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Digite seu nome de usuário: ");
            String meuNome = teclado.readLine();
            //Envia o nome digitado para o servidor;
            saida.println(meuNome.toUpperCase());
            //Instancia a thread para ip e porta conectados e depois inicia ela;
            Thread thread = new UsuarioBatePapo(socket);
            thread.start();
            //Cria a variavel msg responsavel por enviar a mensagem para o servidor;
            String msg;
            while (true)
            {
                //Cria linha para digita��o da mensagem e a armazena na variavel msg;
                System.out.print("Mensagem > ");
                msg = teclado.readLine();
                //Envia a mensagem para o servidor;
                saida.println(msg);
            }
        } catch (IOException e) {
            System.out.println("Falha na Conexao... .. ." + " IOException: " + e);
        }
    }
    //Execu��o da thread;
    public void run()
    {
        try {
            //Recebe mensagens de outro cliente através do servidor;
            BufferedReader entrada =
                new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));
            //Cria variavel de mensagem;
            String msg;
            while (true)
            {
                //Pega o que o servidor enviou;
                msg = entrada.readLine();
                //Se a mensagem contiver dados, passa pelo if;
                //Caso contrario cai no break e encerra a conexao;
                if (msg == null) {
                    System.out.println("Conex�o encerrada!");
                    System.exit(0);
                }
                System.out.println();
                //Imprime a mensagem recebida;
                System.out.println(msg);
                //Cria uma linha visual para resposta;
                System.out.print("Responder > ");
            }
        } catch (IOException e) {
            //Caso ocorra alguma exce��o de E/S, mostra qual foi;
            System.out.println("Ocorreu uma Falha... .. ." +
                " IOException: " + e);
        }
    }
}