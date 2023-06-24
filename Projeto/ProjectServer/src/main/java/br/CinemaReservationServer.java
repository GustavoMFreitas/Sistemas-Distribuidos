/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package br;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gusta
 *
 * A classe CinemaReservationServer é a implementação do servidor de reserva de
 * cinema. Implementa a interface CinemaReservation, que define os métodos
 * remotos disponíveis para reserva e cancelamento de assentos.
 */
public class CinemaReservationServer implements CinemaReservation {

    private Map<String, Boolean> disponibilidadeAssentos; // Mapa para armazenar a disponibilidade dos assentos
    private Object lock; // Objeto para sincronização de bloqueio

    /**
     * Construtor da classe CinemaReservationServer. Inicializa a
     * disponibilidade dos assentos do cinema e o objeto de bloqueio.
     */
    public CinemaReservationServer() {
        disponibilidadeAssentos = new HashMap<>();
        inicializarDisponibilidadeAssentos();
        lock = new Object();
    }

    /**
     * Inicializa a disponibilidade dos assentos do cinema. Supõe que o cinema
     * possui 20 assentos numerados de "A1" a "E4".
     */
    private void inicializarDisponibilidadeAssentos() {
        for (char fileira = 'A'; fileira <= 'E'; fileira++) {
            for (int numAssento = 1; numAssento <= 4; numAssento++) {
                String assento = fileira + String.valueOf(numAssento);
                disponibilidadeAssentos.put(assento, true);
            }
        }
    }

    /**
     * Método remoto para reservar um assento. Verifica se o assento está
     * disponível e realiza a reserva se possível. Retorna uma mensagem
     * informando o resultado da operação.
     *
     * @param assento o assento a ser reservado
     * @return uma mensagem indicando o resultado da reserva
     * @throws RemoteException se ocorrer algum erro durante a execução remota
     */
    @Override
    public String reservarAssento(String assento) throws RemoteException {
        synchronized (lock) {
            if (disponibilidadeAssentos.containsKey(assento)) {
                boolean estaDisponivel = disponibilidadeAssentos.get(assento);
                if (estaDisponivel) {
                    disponibilidadeAssentos.put(assento, false);
                    return "\nAssento " + assento + " reservado com sucesso.";
                } else {
                    return "\nAssento " + assento + " já está reservado.";
                }
            } else {
                return "\nNúmero de assento inválido.";
            }
        }
    }

    /**
     * Método remoto para cancelar uma reserva de assento. Verifica se o assento
     * está reservado e cancela a reserva se possível. Retorna uma mensagem
     * informando o resultado da operação.
     *
     * @param assento o assento a ter a reserva cancelada
     * @return uma mensagem indicando o resultado do cancelamento da reserva
     * @throws RemoteException se ocorrer algum erro durante a execução remota
     */
    @Override
    public String cancelarReserva(String assento) throws RemoteException {
        synchronized (lock) {
            if (disponibilidadeAssentos.containsKey(assento)) {
                boolean estaDisponivel = disponibilidadeAssentos.get(assento);
                if (!estaDisponivel) {
                    disponibilidadeAssentos.put(assento, true);
                    return "\nReserva para o assento " + assento + " cancelada.";
                } else {
                    return "\nAssento " + assento + " não está reservado.";
                }
            } else {
                return "\nNúmero de assento inválido.";
            }
        }
    }

    /**
     * Método remoto para mostrar todos os assentos ocupados no cinema.
     *
     * @return uma lista contendo os assentos ocupados no cinema
     * @throws RemoteException se ocorrer algum erro durante a execução remota
     */
    @Override
    public List<String> mostrarAssentosOcupados() throws RemoteException {
        List<String> assentosOcupados = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : disponibilidadeAssentos.entrySet()) {
            if (!entry.getValue()) {
                assentosOcupados.add(entry.getKey());
            }
        }
        return assentosOcupados;
    }

    /**
     * Método principal que inicia o servidor de reserva de cinema. Cria um
     * registro RMI, instancia o servidor, exporta o objeto remoto, associa o
     * objeto remoto a um nome no registro e inicia a execução do servidor.
     *
     * @param args os argumentos de linha de comando (não são utilizados neste
     * código)
     */
    public static void main(String[] args) {
        try {
            // Crie um registro RMI na porta desejada (padrão: 1099)
            Registry registro = LocateRegistry.createRegistry(1099);

            // Crie uma instância do servidor
            CinemaReservationServer servidor = new CinemaReservationServer();

            // Exporte o objeto remoto
            CinemaReservation stub = (CinemaReservation) UnicastRemoteObject.exportObject(servidor, 0);

            // Associe o objeto remoto a um nome no registro
            registro.bind("CinemaReservation", stub);

            System.out.println("Servidor de reserva de cinema está em execução.");
        } catch (Exception e) {
            System.err.println("Exceção do servidor: " + e.toString());
            e.printStackTrace();
        }
    }
}
