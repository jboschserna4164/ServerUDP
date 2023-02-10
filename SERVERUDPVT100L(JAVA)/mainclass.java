import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * mainclass
 */
public class mainclass {

    public static void main(String[] args) {
        // --------------------creating a udp server --------------------
        try {
            //Datagram socket que va a recibir paquetes por el puerto 11000
            DatagramSocket miSocket = new DatagramSocket(41245);
             //buffer con 1024 bytes de memoria
            byte[] buffer = new byte[1024];
             //ciclo infinito con el objetivo de que reciba todos los mensajes
            while(true){
                //paquete esperado con un limite de tamaño de 1024 bytes
                DatagramPacket msj = new DatagramPacket(buffer, buffer.length);
                //el reporte pasa a ser nuestro paquete recibido
                miSocket.receive(msj);
                //Direccion IP y puerto de la unidad
                InetAddress address = msj.getAddress();
                int port = msj.getPort();
                //Decodificar el mensaje
                String report = new String(msj.getData(), StandardCharsets.UTF_8);
                //imprimir los datos recibidos
                System.out.println("==> Mensaje de VT100L ");
                System.out.println("IP: " + address + "\nPuerto: " + port + "\nMensaje: " + report + "\nTamaño: " + report.length() + "\n");
                utilVT100L.printData(report);
            }
        } 
        catch (Exception error) {
            //imprimir error
            error.printStackTrace();
        }
        // -------------------------------------------------------------------------

        
    }



}