import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * Tarea 4. Implementación de un token-ring
 * Becerril Saldivar Luis Alejandro
*/

class Token{

  static DataInputStream entrada;
  static DataOutputStream salida;
  static boolean primera_vez = true;
  static String ip;
  static int token;
  static int nodo;
  static int contador = 0;

  static class Worker extends Thread{
    public void run(){
		/*
			Algoritmo 1
		*/
      try {
		//1. Declarar la variable servidor de tipo ServerSocket
        ServerSocket servidor;
		//2. Asignar a la variable servidor el objeto: new ServerSocket(50000)
		servidor= new ServerSocket(50000);
		//3. Declarar la variable conexion de tipo ﻿Socket.
        Socket conexion;
		//4. Asignar a la variable conexion el objeto servidor.accept()
		conexion = servidor.accept();
		//Asignar a la variable entrada el objeto new DataInputStream(conexion.getInputStream())
        entrada = new DataInputStream(conexion.getInputStream());

      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }

  public static void main(String[] args) throws Exception{
    if (args.length != 2){
      System.err.println("Se debe pasar como parametros el numero de nodo y la IP del siguiente nodo");
      System.exit(1);
    }

    nodo = Integer.valueOf(args[0]);  // el primer parametro es el numero de nodo
    ip = args[1];  // el segundo parametro es la IP del siguiente nodo en el anillo
	
	/*
		Algoritmo 2
	*/
	
	//1. Declarar la variable w de tipo Worker.
    Worker w;
	//2. Asignar a la variable w el objeto new Worker().
	w =  new Worker();
	//3. Invocar el método w.start().
    w.start();
	//4. Declarar la variable conexion de tipo Socket. Asignar null a la variable conexion.
    Socket conexion = null;
	//5. En un ciclo:
    for(;;){
		//5.1 En un bloque try:
		  try {
			//5.1.1 Asignar a la variable conexion el objeto Socket(ip,50000).
			conexion = new Socket(ip,50000);
			//5.1.2 Ejecutar break para salir del ciclo.
			break;
			//5.2 En el bloque catch:
		  } catch (Exception e) {
			// 5.2.1 Invocar el método Thread.sleep(500).
			Thread.sleep(500);
		  }
    }

	//6. Asignar a la variable salida el objeto new DataOutputStream(conexion.getOutputStream()).
    salida = new DataOutputStream(conexion.getOutputStream());
	//7. Invocar el método w.join().
    w.join();
	
	//8. En un ciclo:
    for(;;){
	  //8.1 Si la variable nodo es cero:
      if (nodo == 0){
		  //8.1.1 Si la variable primera_vez es true:
        if (primera_vez == true){
		  //8.1.1.1 Asignar false a la variable primera_vez.
          primera_vez = false;
		  //8.1.1.2 Asignar 1 a la variable token.
          token = 1;
        }
		//8.1.2 Si la variable primera_vez es false:
        else if( primera_vez == false){
		  //8.1.2.1 Asignar a la variable token el resultado del método entrada.readInt().
          token = entrada.readInt();
		  //8.1.2.2 Incrementar la variable contador.
          contador++;
		  //8.1.2.3 Desplegar las variables nodo, contador y token.
          System.out.println("Nodo: "+nodo+" Contador: "+contador+"  Token: "+token);
        }
      }
	  //8.2 Si la variable nodo no es cero:
      else if(nodo != 0){
		//8.2.1 Asignar a la variable token el resultado del método entrada.readInt().
        token = entrada.readInt();
		//8.2.2 Incrementar la variable contador.
        contador++;
		//8.2.3 Desplegar las variables nodo, contador y token.
        System.out.println("Nodo: "+nodo+" Contador: "+contador+"  Token: "+token);
      }
	  //8.3 Si la variable nodo es cero y la variable contador es igual a 1000:
      if(nodo == 0 && contador == 1000){
		//8.3.1 Salir del ciclo.
        break;
      }
	  //8.4 Invocar el método salida.writeInt(token).
      salida.writeInt(token);
    }
  }
}
