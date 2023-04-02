import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.HashMap;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class servidor {

    private static Socket socket;
    private static ServerSocket server;

    private static BufferedReader entrada;
    private static PrintWriter saida;

    public final static Path path = Paths.get("fortune-br.txt");
    private int NUM_FORTUNES = 0;
    private int porta = 1025;

    public class FileReader {

        public int countFortunes() throws FileNotFoundException {

            int lineCount = 0;

            InputStream is = new BufferedInputStream(new FileInputStream(path.toString()));
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line = "";
                while (!(line == null)) {

                    if (line.equals("%"))
                        lineCount++;

                    line = br.readLine();

                } // fim while

                // System.out.println(lineCount);
            } catch (IOException e) {
                System.out.println("SHOW: Excecao na leitura do arquivo.");
            }
            return lineCount;
        }

        public void parse(HashMap<Integer, String> hm) throws FileNotFoundException {

            InputStream is = new BufferedInputStream(new FileInputStream(path.toString()));
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                int lineCount = 0;
                String line = "";

                while (!(line == null)) {
                    if (line.equals("%"))
                        lineCount++;
                    line = br.readLine();
                    StringBuffer fortune = new StringBuffer();
                    while (!(line == null) && !line.equals("%")) {
                        fortune.append(line + "\n");
                        line = br.readLine();
                    }
                    hm.put(lineCount, fortune.toString());
                    // System.out.println(lineCount);
                    // System.out.println(fortune.toString());

                } // fim while

            } catch (IOException e) {
                System.out.println("SHOW: Excecao na leitura do arquivo.");
            }
        }

        public String read(HashMap<Integer, String> hm) throws FileNotFoundException {
            Random gerador = new Random();
            int rand = gerador.nextInt(NUM_FORTUNES);
            return hm.get(rand);
        }

        public void write(HashMap<Integer, String> hm, String entrada) throws FileNotFoundException {
            try (FileWriter fw = new FileWriter(path.toString(), true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter pw = new PrintWriter(bw)) {

                pw.println(entrada);
            } catch (IOException e) {
                System.out.println("SHOW: Excecao na escrita do arquivo.");
            }
        }

    }

    public void iniciar() {
        System.out.println("Servidor iniciado na porta: " + porta);
        try {
            FileReader fr = new FileReader();
            NUM_FORTUNES = fr.countFortunes();
            HashMap<Integer, String> hm = new HashMap<Integer, String>();
            fr.parse(hm);

            // Criar porta de recepcao
            server = new ServerSocket(porta);
            socket = server.accept(); // Processo fica bloqueado, ah espera de conexoes

            // Criar os fluxos de entrada e saida
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            saida = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            String line = entrada.readLine();
            JSONParser parser = new JSONParser();
            JSONObject pedido = (JSONObject) parser.parse(line);

            String metodo = (String) pedido.get("method");
            JSONArray args = (JSONArray) pedido.get("args");

            if (metodo.equals("read")) {
                // Implementação da operação de leitura
                String data = fr.read(hm);
                System.out.println(data);
                saida.println(data);
            } else if (metodo.equals("write")) {
                // Implementação da operação de escrita
                String data = "";
                fr.write(hm, "%");
                for (int i = 0; i < args.size(); i++) {
                    data = (String) args.get(i);
                    fr.write(hm, data);
                }
            } else {
                saida.println("Método inválido");
            }
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new servidor().iniciar();
    }

}