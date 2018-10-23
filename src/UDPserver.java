/**
 * Classe che rappresenta un processo server UDP che fornisce la data e ora attuali ad un client
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;

public class UDPserver extends Thread
{

	private DatagramSocket socket;
	
	public UDPserver (int port) throws SocketException
	{
		socket=new DatagramSocket(port);
		socket.setSoTimeout(1000);
		
	}
	
	public void run()
	{
		byte[] buffer= new byte[8192];
		DatagramPacket request=new DatagramPacket(buffer, buffer.length);
		DatagramPacket answer;
		String risposta;
		LocalDateTime dateTime;
		while (!interrupted())
		{
			try 
			{
				socket.receive(request);
				
				dateTime=LocalDateTime.now();
				risposta=dateTime.toString();
				buffer=risposta.getBytes("ISO-8859-1");
				answer=new DatagramPacket(buffer, buffer.length, request.getAddress(), request.getPort());
				socket.send(answer);		
			} 
			catch (SocketTimeoutException e) 
			{
				System.err.println("Timeout");
			}
			catch (IOException e) 
			{
				
				e.printStackTrace();
			}
		}
		closeSocket();
		
	}
	
	public void closeSocket()
	{
		socket.close();
	}
	public static void main(String[] args)
	{
		ConsoleInput tastiera= new ConsoleInput();
		try 
		{
			UDPserver echoServer= new UDPserver(7);
			echoServer.start();
			tastiera.readLine();
			echoServer.interrupt();
			
		} 
		catch (SocketException e) 
		{
			System.err.println("Impossibile istanziare il socket");
		} 
		catch (IOException e) 
		{
			System.out.println("Errore generico di I/O dalla tastiera");
		}

	}

}
