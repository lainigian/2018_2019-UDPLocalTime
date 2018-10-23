import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class UDPclient 
{

	private DatagramSocket socket;
	
	public UDPclient() throws SocketException
	{
		socket= new DatagramSocket();
		socket.setSoTimeout(1000);
	}
	
	public void closeSocket()
	{
		socket.close();
	}
	
	public String sendAndReceive(String host, int port, String messaggio) throws UnsupportedEncodingException, UnknownHostException, IOException
	{
		byte[] buffer=new byte[8192];
		byte[] bufferResponse=new byte [100];
		DatagramPacket datagram;
		DatagramPacket response;
		
		InetAddress address=InetAddress.getByName(host);
		String rispostaServer;
		
		response=new DatagramPacket(bufferResponse, bufferResponse.length);
		buffer=messaggio.getBytes("ISO-8859-1");
		datagram=new DatagramPacket(buffer, buffer.length, address, port);
		socket.send(datagram);
		socket.receive(response);
		
		bufferResponse=response.getData();
		rispostaServer=new String(bufferResponse, "ISO-8859-1");
		
		return rispostaServer;
	}
	
	public static void main(String[] args) 
	{
		String rispostaServer;
		String host="127.0.0.1";
		int port=7;
		String messaggio="Date Time";
		try 
		{
			UDPclient echoClient=new UDPclient();
			rispostaServer=echoClient.sendAndReceive(host, port, messaggio);
			System.out.println("Risposta dal server: "+rispostaServer);
		} 
		catch (SocketTimeoutException e)
		{
			System.err.println("Il server non risponde");
		}
		catch (SocketException e)
		{
			System.err.println("Impossibile istanziare il socket");
		} 
		catch (UnsupportedEncodingException e)
		{
			System.err.println("Charset non supportato");
		} 
		catch (UnknownHostException e) 
		{
			System.err.println("Host sconosciuto");
		} 
		catch (IOException e) 
		{
			System.err.println("Errore generico di I/O");
		}
		

	}

}
