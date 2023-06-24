/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package br;

import static java.lang.Thread.sleep;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author gusta
 * 
 * Classe responsável por realizar a interação do cliente com o servidor SOAP e RMI para a reserva de assentos no cinema.
 * 
 */
public class SoapClientProjeto {
    public static void main(String[] args) {
        try {
            // Cria um scanner para receber a entrada do usuário
            Scanner scanner = new Scanner(System.in);
            
            // URL do endpoint SOAP e ação SOAP a ser chamada
            String soapEndpointUrl = "http://localhost:8080/SoapServer/MovieServiceService?wsdl";
            String soapAction = "http://localhost:8080/SoapServer/MovieServiceService";
            
            // Exibe uma mensagem inicial para o usuário
            System.out.print("\n\nSistema para reserva de assentos no cinema.\n\n\n");
            System.out.print("Antes de reservar, receba sugestões de filmes.\n");
            System.out.print("Digite um gênero para receber uma sugestão (ação, drama, comédia, romance, terror, animação): ");
            String CAMPO1 = scanner.nextLine();
            System.out.print("\n\n");
            
            // Chama o WebService SOAP
            SoapClient sc = new SoapClient(CAMPO1);
            sc.callSoapWebService(soapEndpointUrl, soapAction);

            // Localiza o registro RMI
            Registry registry = LocateRegistry.getRegistry("localhost");

            // Obtém a referência do objeto remoto do registro usando o nome fornecido durante o registro
            CinemaReservation cinemaReservation = (CinemaReservation) registry.lookup("CinemaReservation");

            boolean exit = false;
            
            // Solicita ao usuário que escolha uma ação
            while (!exit) {
                System.out.print("\n\nEscolha uma ação:\nreservar - Para realizar uma reserva\ncancelar - Para cancelar uma reserva\nocupados - Para mostrar os assentos ocupados \nsair - Sair do programa\n");

                String action = scanner.nextLine();
                
                String result = "";
                
                // Faz a chamada ao método remoto com base na ação escolhida
                if (action.equalsIgnoreCase("reservar")) {
                    System.out.print("Digite o número do assento (Os assentos vão de A1 até E4): ");
                    String seat = scanner.nextLine();
                    result = cinemaReservation.reservarAssento(seat);
                } else if (action.equalsIgnoreCase("cancelar")) {
                    System.out.print("Digite o número do assento (Os assentos vão de A1 até E4): ");
                    String seat = scanner.nextLine();
                    result = cinemaReservation.cancelarReserva(seat); 
                } else if (action.equalsIgnoreCase("ocupados")) {
                    List<String> result1 = new ArrayList<>();
                    result1 = cinemaReservation.mostrarAssentosOcupados();
                    if (result1.isEmpty()) {
                        System.out.print("\nTodos os assentos estão livres");
                    } else {
                        Collections.sort(result1);
                        System.out.print("\nAssentos ocupados: " + result1);
                    }
                } else if (action.equalsIgnoreCase("sair")) {
                    exit = true;
                    result = "\nSaindo do programa";
                } else {
                    result = "\nAção inválida.";
                }
                
                System.out.println(result);
                sleep(1000);
            }
            scanner.close();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}