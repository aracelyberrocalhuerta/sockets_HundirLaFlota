import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Alumno2 {
    static Random rn = new Random();
    static int[][] tablero = new int[10][10];
    static String mensaje = "";
    static int fila = 0;
    static int columna = 0;
    static Boolean tableroCompletado = false;
    static int cont = 0;
    static List<String> respuestas = new ArrayList<String>();

    public static void main(String[] args) {
        generarTablero();
        try {
            ServerSocket serverSocket = new ServerSocket(5555);
            Socket alumno = serverSocket.accept();
            InputStream is = alumno.getInputStream();
            OutputStream os = alumno.getOutputStream();

            while (!tableroCompletado) {
                enviarCoordenadas();
                os.write(mensaje.getBytes());
                byte[] mensajeBytes = new byte[25];
                is.read(mensajeBytes);
                mensaje = new String(mensajeBytes);
                recibirCoordenada(mensaje);
            }
            alumno.close();
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generarTablero() {
        System.out.println("generando tablero");
        for (int i = 0; i < 10; i++) {
            int ran1 = rn.nextInt(9);
            int ran2 = rn.nextInt(9);
            tablero[ran1][ran2] = 1;
        }
        System.out.println("TABLERO");
        for (int i = 0; i < 10; i++) {
            System.out.println(Arrays.toString(tablero[i]));
        }
    }

    private static void recibirCoordenada(String mensaje) {
        System.out.println("recibiendo coordenadas");
        fila = Character.getNumericValue(mensaje.charAt(0));
        columna = Character.getNumericValue(mensaje.charAt(1));
        System.out.println("FILA "+ fila);
        System.out.println("COLUMNA "+ columna);
        System.out.println("COORDENADA"+fila+" "+columna);
        if (tablero[fila][columna] == 1) {
            cont++;
            tablero[fila][columna] = 0;
        }
        comprobarTablero();
    }

    private static void enviarCoordenadas() {
        generarCoordenada();
        System.out.println("enviando coordenadas");
        if(respuestas.contains(mensaje)){
            generarCoordenada();
        }else{
            respuestas.add(mensaje);
        }
    }
    private static void generarCoordenada() {
        System.out.println("GENERAR CORDENADA");
        fila = rn.nextInt(9);
        columna = rn.nextInt(9);
        System.out.println("FILA "+ fila);
        System.out.println("COLUMNA "+ columna);
        System.out.println("COORDENADA"+fila+" "+columna);
        mensaje = String.valueOf(fila)+ String.valueOf(columna);
        System.out.println("MENSAJE PARA ENVIAR "+mensaje);
    }
    private static void comprobarTablero() {
        System.out.println("comprobando tablero");
        System.out.println(cont);
        System.out.println("LISTA"+respuestas);
        System.out.println("CONTADOR"+cont);
        if (cont == 10) {
            tableroCompletado = true;
            System.out.println("Ha ganado Alumno 1");
        }
    }
}
