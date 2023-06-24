/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/WebService.java to edit this template
 */
package br.ws;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author gusta
 * 
 *         Classe que implementa o serviço web SOAP para obter filmes famosos de
 *         diferentes categorias.
 */
@WebService
public class MovieService {

    /**
     * Obtém filmes famosos de uma determinada categoria.
     *
     * @param category a categoria dos filmes desejada
     * @return um array de strings contendo os filmes famosos da categoria
     *         especificada
     */
    @WebMethod(operationName = "filme")
    public String[] getFamousMovies(@WebParam(name = "category") String category) {
        if (category.equals("acao")) {
            return new String[] { "The Dark Knight", "A Origem", "Matrix", "John Wick 4: Baba Yaga",
                    "Esquema de Risco: Operacao Fortune", "Alerta Maximo", "Anonimo", "Morbius", "A Mulher Rei",
                    "Top Gun: Maverick", "Duro de Matar", "Missao Impossivel", "Velozes e Furiosos",
                    "Vingadores: Ultimato", "O Exterminador do Futuro" };
        } else if (category.equals("drama")) {
            return new String[] { "Um Sonho de Liberdade", "A Espera de um Milagre", "Forrest Gump", "Imperdoavel",
                    "A Baleia", "Cidade de Deus", "O Lobo de Wall Street", "Clube da Luta", "Cisne Negro",
                    "O Silencio dos Inocentes", "A Vida e Bela", "A Lista de Schindler", "Bastardos Inglorios",
                    "12 Anos de Escravidao" };
        } else if (category.equals("comedia")) {
            return new String[] { "Se beber, Nao case!", "Superbad", "As Branquelas", "Uma Noite no Museu",
                    "O Mentiroso", "As Patricinhas de Beverly Hills", "Loucademia de Policia", "Entrando Numa Fria",
                    "Zoolander" };
        } else if (category.equals("romance")) {
            return new String[] { "Titanic", "Romeu + Julieta", "Simplesmente Acontece", "La La Land",
                    "Diario de uma Paixao", "Orgulho e Preconceito" };
        } else if (category.equals("terror")) {
            return new String[] { "O Iluminado", "O Exorcista", "Invocacao do Mal", "Psicose", "A Bruxa",
                    "O Babadook" };
        } else if (category.equals("animacao")) {
            return new String[] { "Toy Story", "Frozen", "Procurando Nemo", "A Bela e a Fera", "Up: Altas Aventuras",
                    "Zootopia" };
        } else {
            return new String[0];
        }
    }
}