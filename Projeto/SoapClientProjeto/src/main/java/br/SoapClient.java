/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br;

/**
 *
 * @author gusta
 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

/**
 * Classe responsável por realizar chamadas ao serviço SOAP.
 */
public class SoapClient {

    /**
     * Campo de entrada utilizado na chamada do serviço SOAP.
     */
    public static String CAMPO1;

    /**
     * Construtor da classe SoapClient.
     *
     * @param CAMPO1 o valor do campo de entrada para a chamada do serviço SOAP
     */
    public SoapClient(String CAMPO1) {
        this.CAMPO1 = CAMPO1;
    }

    /**
     * Realiza a chamada para o serviço SOAP.
     *
     * @param soapEndpointUrl o URL do ponto de extremidade do serviço SOAP
     * @param soapAction a ação SOAP a ser executada
     */
    public void callSoapWebService(String soapEndpointUrl, String soapAction) {
        try {
            // Criar conexão SOAP
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Enviar mensagem SOAP para o servidor
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(soapAction), soapEndpointUrl);

            // Imprimir resposta
            Random gerador = new Random();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            soapResponse.writeTo(outputStream);
            String resultado = outputStream.toString();
            Document document = Jsoup.parse(resultado);
            Elements returnElements = document.select("return");

            ArrayList<String> result = new ArrayList<>();

            for (Element element : returnElements) {
                result.add(element.text());
            }

            System.out.println(result.get(gerador.nextInt(result.size())));
            System.out.print("\n\n");

            soapConnection.close();
        } catch (Exception e) {
            System.out.println("ERRO:");
            System.out.println(e.getMessage());
        }
    }

    private static SOAPMessage createSOAPRequest(String soapAction) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        // Criar envelope SOAP
        createSoapEnvelope(soapMessage);

        soapMessage.saveChanges();
        return soapMessage;
    }

    private static void createSoapEnvelope(SOAPMessage soapMessage) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();

        // Verificar o namespace utilizado no WSDL
        String myNamespace = "ns2";
        String myNamespaceURI = "http://ws.br/";

        // Preencher o SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);

        // Preencher o SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("filme", myNamespace);
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("category");
        soapBodyElem1.addTextNode(CAMPO1);
    }
}