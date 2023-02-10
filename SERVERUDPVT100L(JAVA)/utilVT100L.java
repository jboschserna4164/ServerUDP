import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class utilVT100L {
    // PENDIENTE CORREGIR
    public static String CalcularChecksum(String cadenaForChecksum) {
        // variable para almacenar el total de la suma de decimales
        int byte2 = 0;
        // Obtener los bytes en decimal
        byte[] bytes = cadenaForChecksum.getBytes();
        // Separar cada byte y sumar cada uno en la variable byte2
        for (int i = 0; i < cadenaForChecksum.length(); i++) {
            byte byte1 = bytes[i];
            byte2 += byte1;
        }
        // Hacer la operacion modulo 256
        int byteChecksum = byte2 % 256;
        // Convertir el resultado a un numero hexadecimal
        String finalbyte = Integer.toHexString(byteChecksum);
        // Retornar el resultado
        return finalbyte;
    }

    // TIPO DE EVENTO
    public static String parseSpecificEventsFromDevice(String report) {
        System.out.println("Trama de Datos: " + report.toString());

        String reportType = report.toString().split(",")[3];
        System.out.println("Number Event: " + reportType);

        switch (reportType) {
            case "0":
                return "REPORTE POR TIEMPO";
            case "3":
                return "VEHICULO ENCENDIDO)";
            case "4":
                return "VEHICULO APAGADO";
            case "1":
                return "BOTON DE PANICO";
            case "22":
                return "EXCESO DE VELOCIDAD";
            case "30":
                return "REPORTE POR DISTANCIA";
            case "31":
                return "REPORTE POR TIEMPO EN MODO SLEEP";
            case "36":
                return "INGRESO EN MODO ESTACIONAMIENTO";
            case "37":
                return "INGRESO EN MODO CONDUCCION";
            case "39":
                return "ACELERACION EN BRUSCO";
            case "40":
                return "FRENADO EN BRUSCO";
            case "41":
                return "GIRO BRUSCO";
            case "18":
                return "DESCONEXION DE BATERIA";
            case "19":
                return "CONEXION DE BATERIA";
            case "20":
                return "BATERIA INTERNA BAJO NIVEL";
            case "23":
                return "INGRESO A MODO SUEÑO";
            case "24":
                return "SALIDA DE MODO SUEÑO)";
            case "35":
                return "ALARMA POR REMOLQUE";
            case "27":
                return "ALARMA PERDIDA SEÑAL GPS";
            case "28":
                return "ALARMA RECUPERACION SEÑAL GPS";

            default:
                return "EVENTO DESCONOCIDO";

        }

    }

    // ESTADO DE LA IGNCION

    public static String ignitionState(String report) {
        String ignicion = "";

        if ((report.toString().split(",")[18]) == "02") {
            ignicion = "Encendido";

        } else {
            ignicion = "Apagado";

        }
        return ignicion;
    }

    // CALCULAR FECHA
    public static String getDate(String date) {
        String yearfinal = date.substring(0, 2);
        String temp = String.valueOf(new GregorianCalendar().get(Calendar.YEAR));
        String initYear = temp.toString().substring(0, 2);
        String year = initYear + yearfinal;
        String month = date.substring(2, 4);
        String day = date.substring(4, 6);
        String hour = String.valueOf(Integer.parseInt(date.substring(6, 8)) - 5); // Se le resta 5 para que quede acorde
                                                                                  // con la zona horaria de Colombia
        String minute = date.substring(8, 10);
        String second = date.substring(10, 12);
        String finaldate = year + "-" + month + "-" + day + "T" + hour + "-" + minute + "-" + second; // "2023-01-28T05:30:55"
        return finaldate;
    }

    public static void printData(String report) {
        if (report.indexOf("OK") == -1) {
            /* EVENTO REPORTE */
            String event = parseSpecificEventsFromDevice(report);
            System.out.println("Evento: " + event);

            /* ID UNIDAD */
            String idUnidad = report.toString().split(",")[1];
            System.out.println("idUnidad: " + idUnidad);

            /* FECHA */
            String date = report.toString().split(",")[5];
            String gpsDate = getDate(date);
            System.out.println("Fecha/Hora GPS  : " + gpsDate);

            /* LATITUD */
            String latitud = report.toString().split(",")[7];
            System.out.println("Latitud  : " + latitud);

            /* LONGITUD */
            String longitud = report.toString().split(",")[8];
            System.out.println("Longitud : " + longitud);

            /* ALTITUD */
            String altitud = report.toString().split(",")[13];
            System.out.println("Altitud (Metros) : " + altitud);

            /* NUMERO DE SATELITES */
            String numberSatelites = report.toString().split(",")[9];
            System.out.println("Numero Satelites : " + numberSatelites);

            /* VELOCIDAD */
            String speed = report.toString().split(",")[11];
            System.out.println("Velocidad km/h : " + speed);

            /* CURSO */
            String course = report.toString().split(",")[12];
            System.out.println("Curso (º) : " + course);

            /* ODOMETRO */
            String odometer = report.toString().split(",")[14];
            System.out.println("Odometro (Metros) : " + odometer);

            /* ESTADO DE LA IGNICION */
            String ignicionState = ignitionState(report);
            System.out.println("Estado Ignición : " + ignicionState);
        }
    }

    // * COMANDO DE ENCENDIDO *//
    public static String cadenaComandoEncendido(String idUnidad) {

        String lonCadenaEncendido = ',' + idUnidad + ',' + "900,1,0";
        String lonFinalEncendido = String.valueOf(lonCadenaEncendido.length());
        String cadenaForChecksumOn = "$$:" + lonFinalEncendido + ',' + idUnidad + ',' + "900,1,0";
        String checksumEncendido = utilVT100L.CalcularChecksum(cadenaForChecksumOn);
        String cadenaEncendido = cadenaForChecksumOn + checksumEncendido + "\r" + "\n";

        return cadenaEncendido;

        // commandOn = '$$:14,11900,900,1,08C\r\n'
        // commandOnEnv= Buffer.from( cadenaEncendido.toString('hex'));
    }

    // * COMANDO DE APAGADO *//
    public static String cadenaComandoApagado(String idUnidad) {
        String lonCadenaApagado = ',' + idUnidad + ',' + "900,1,1";
        String lonFinalApagado = String.valueOf(lonCadenaApagado.length());
        String cadenaForChecksumOff = "$$:" + lonFinalApagado + ',' + idUnidad + ',' + "900,1,1";
        String checksumApagado = utilVT100L.CalcularChecksum(cadenaForChecksumOff);
        String cadenaApagado = cadenaForChecksumOff + checksumApagado + "\r" + "\n";

        return cadenaApagado;

        // let commandOff = '$$:14,11900,900,1,18D\r\n'
        // commandOffEnv= Buffer.from( cadenaApagado.toString('hex'));
    }

    public static void enviarMensaje(DatagramSocket socketTransmisor, String msj, InetAddress address, int port) {
        try {
            // Obtener los bytes del mensaje
            byte[] mensaje = msj.getBytes();
            // Construimos un datagrama para enviar el mensaje al servidor
            DatagramPacket peticion = new DatagramPacket(mensaje, mensaje.length, address, port);
            socketTransmisor.send(peticion);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }
}
