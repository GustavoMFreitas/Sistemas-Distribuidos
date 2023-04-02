import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class cliente {

    private static Socket socket;
    private static BufferedReader entrada;
    private static PrintWriter saida;

    private int porta = 1025;

    public void iniciar(int mtd) {
        System.out.println("Cliente iniciado na porta: " + porta);
        Scanner newscan = new Scanner(System.in);
        try {
            socket = new Socket("127.0.0.1", porta);

            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            saida = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            if (mtd == 0) {
                JSONObject lerJSON = new JSONObject();
                lerJSON.put("method", "read");
                lerJSON.put("args", new JSONArray());
                saida.println(lerJSON.toJSONString());
                String resposta = "";
                while (!(resposta == null)) {
                    resposta = entrada.readLine();
                    if (resposta != null)
                        System.out.println(resposta);
                }
            } else if (mtd == 1) {

                JSONObject escreverJSON = new JSONObject();
                escreverJSON.put("method", "write");
                JSONArray ecreverARGS = new JSONArray();
                String msg = "";
                System.out.println("Digite sua mensagem e para finalizar basta digitar o caracter \" % \" sozinho");
                while (!msg.equals("%")) {
                    msg = newscan.nextLine();
                    ecreverARGS.add(msg);
                }
                escreverJSON.put("args", ecreverARGS);
                saida.println(escreverJSON.toJSONString());
                newscan.close();
            } else {
                System.out.println("Método Inválido");
            }
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Digite o Método desejado, read ou write:  ");
        String enter = sc.nextLine();
        if (enter.equals("read")) {
            new cliente().iniciar(0);
        } else if (enter.equals("write")) {
            new cliente().iniciar(1);
        }
        sc.close();
    }

}
