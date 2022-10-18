package clientes21;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientEs21 {

    private String server_name;
    private int server_port;
    private Socket client_socket;
    public ObjectOutputStream out;

    public ClientEs21(String server, int port) throws IOException {
        server_name = server;
        server_port = port;

    }

    public ArrayList open(int scelta, int first, int second, int third) throws SocketTimeoutException, IOException, ClassNotFoundException {
        ArrayList risposta = new ArrayList();
        ArrayList richiesta = new ArrayList();
        client_socket = new Socket(server_name, server_port);
        out = new ObjectOutputStream(client_socket.getOutputStream());
        richiesta.add(scelta);
        richiesta.add(first);
        richiesta.add(second);
        richiesta.add(third);
        out.writeObject(richiesta);
        out.flush();
        ObjectInputStream in = new ObjectInputStream(client_socket.getInputStream());
        risposta = (ArrayList) in.readObject();
        return risposta;
    }

    public static void main(String args[]) throws ClassNotFoundException {
        try {
            int first = 20; //primo voto
            int second = 20; //secondo voto
            int third = 20; //terzo voto
            int codice = 10; //codice del caffe (voto medio)
            ClientEs21 client = new ClientEs21("127.0.0.1", 13);
            ArrayList risposta = new ArrayList(); //ArrayList di risposta
            boolean uscita = true; //controllo uscita 
            int conteggio = 0; //massimo 10 votanti
            StringBuilder sb = new StringBuilder();
            risposta = client.open(3, 0, 0, 0);
            String code = risposta.get(0).toString();
            for (char c : code.toCharArray()) {
                sb.append(c).append(" ");
            }
            System.out.println("I codici dei caffe sono: " + sb + "\n\n");
            while (true) {
                Scanner input = new Scanner(System.in);
                System.out.print("          MENU\n1) Votare un caffe.\n2) Consulatre il voto medio.\n3) Visualizzare i codici.\n4) Uscire. \nEffettuare una scelta: ");
                int scelta = input.nextInt();
                switch (scelta) {
                    case 1: //Votare i caffe
                        if (conteggio < 11) { //I cafe possono aver un massimo di dieci voti
                            while (first < 1 || first > 10) {
                                System.out.print("Inserire il voto del primo caffe: ");
                                first = input.nextInt();
                                if (first < 1 || first > 10) {
                                    System.out.println("VOTO NON VALIDO");
                                }
                            }
                            while (second < 1 || second > 10) {
                                System.out.print("Inserire il voto del secondo caffe: ");
                                second = input.nextInt();
                                if (second < 1 || second > 10) {
                                    System.out.println("VOTO NON VALIDO");
                                }
                            }
                            while (third < 1 || third > 10) {
                                System.out.print("Inserire il voto del terzo caffe: ");
                                third = input.nextInt();
                                if (first < 1 || third > 10) {
                                    System.out.println("VOTO NON VALIDO");
                                }
                            }
                            risposta = client.open(scelta, first, second, third);
                            if ((int) risposta.get(0) == 1) {
                                System.out.println("Grazie per aver votato i caffe. \nBuona Giornata!\n\n");
                            } else {
                                System.out.println("Errore in fase di scrittura !");
                            }
                            conteggio++;
                            break;
                        } else {
                            System.out.println("Numero massimo di voti raggiunti !");
                            break;
                        }
                    case 2: //visualizzare la media dei voti di un caffe
                        if (conteggio != 0) {
                            while (codice < 1 || codice > 3) {
                                System.out.print("Inserire il codice del caffe di cui si vuole consultare il voto medio: ");
                                codice = input.nextInt();
                                if (codice < 1 || codice > 3) {
                                    System.out.println("CODICE NON VALIDO");
                                }
                            }
                            risposta = client.open(scelta, codice, 0, 0);
                            System.out.println("La media dei voti del caffe con codice " + codice + " vale: " + risposta.get(0) + "\n\n");
                        } else {
                            System.out.println("Nessun voto presente !\n\n");
                        }
                        break;
                    case 3: //carica i codici da file, se lo ha fatto una volta non chiama piu il metodo ma stampa quelli salvati
                        System.out.println("I codici dei caffe sono: " + sb + "\n\n");
                        break;
                    case 4: //Uscita dal programma
                        System.out.println("Uscita in corso...\n\n");
                        uscita = false;
                        break;
                }
                if (uscita == false) {
                    break;
                }
            }
        } catch (SocketTimeoutException exception) {
            System.err.println("Nessuna risposta dal server!");
        } catch (IOException exception) {
            System.err.println("Errore generico di comunicazione!");
        }
    }
}
