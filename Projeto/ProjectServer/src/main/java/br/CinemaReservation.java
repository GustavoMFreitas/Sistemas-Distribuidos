/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br;

/**
 *
 * @author gusta
 * 
 * Interface que define os métodos remotos para a reserva de assentos no cinema.
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CinemaReservation extends Remote {
    String reservarAssento(String assento) throws RemoteException;
    String cancelarReserva(String assento) throws RemoteException;
    List<String> mostrarAssentosOcupados() throws RemoteException;
}