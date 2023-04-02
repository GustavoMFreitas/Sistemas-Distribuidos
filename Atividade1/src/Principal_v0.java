import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Principal_v0 {

	public final static Path path = Paths.get("fortune-br.txt");
	private int NUM_FORTUNES = 0;

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

		public void parser(HashMap<Integer, String> hm) throws FileNotFoundException {

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
					System.out.println(lineCount);
					System.out.println(fortune.toString());

				} // fim while

			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
			}
		}

		public void read(HashMap<Integer, String> hm) throws FileNotFoundException {
			Random gerador = new Random();
			int rand = gerador.nextInt(NUM_FORTUNES);
			System.out.println("\n\n\n" + rand);
			System.out.println(hm.get(rand));
		}

		public void write(HashMap<Integer, String> hm) throws FileNotFoundException {
			Scanner sc = new Scanner(System.in);
			String msg = "";
			try (FileWriter fw = new FileWriter(path.toString(), true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter pw = new PrintWriter(bw)) {
				pw.println("\n%");
				System.out.println("Digite sua mensagem e para finalizar basta digitar o caracter \" % \" sozinho");
				while (!msg.equals("%")) {
					msg = sc.nextLine();
					pw.println(msg);
				}
				sc.close();
			} catch (IOException e) {
				System.out.println("SHOW: Excecao na escrita do arquivo.");
			}
		}
	}

	public void iniciar() {

		FileReader fr = new FileReader();
		try {
			NUM_FORTUNES = fr.countFortunes();
			HashMap<Integer, String> hm = new HashMap<Integer, String>();
			fr.parser(hm);
			fr.read(hm);
			fr.write(hm);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Principal_v0().iniciar();
	}
}