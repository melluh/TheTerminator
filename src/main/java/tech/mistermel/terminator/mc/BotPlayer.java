package tech.mistermel.terminator.mc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;

public class BotPlayer extends SessionAdapter {

	private static final Logger logger = LoggerFactory.getLogger(BotPlayer.class);
	private static final String DISCONNECT_REASON = "Disconnected"; // Same as vanilla
	
	private MinecraftProtocol protocol;
	private Client client;

	public BotPlayer(String email, String password) {
		try {
			this.protocol = new MinecraftProtocol(email, password);
			System.out.println("ClientToken: " + protocol.getClientToken());
			System.out.println("AccessToken: " + protocol.getAccessToken());
		} catch (RequestException e) {
			logger.error("Error occurred while attempting to log in", e);
		}
	}
	
	public BotPlayer(Account account) {
		try {
			this.protocol = new MinecraftProtocol(account.getUsername(), account.getClientToken(), account.getAccessToken());
		} catch (RequestException e) {
			logger.error("Error occurred while attempting to log in", e);
		}
	}
	
	public void connect(String ip, int port) {
		this.client = new Client(ip, port, protocol, new TcpSessionFactory());
		client.getSession().connect();
		
		logger.info("Connecting {} ({}) to {}:{}", protocol.getProfile().getName(), protocol.getProfile().getIdAsString(), ip, port);
	}
	
	public void disconnect() {
		client.getSession().disconnect(DISCONNECT_REASON);
	}
	
	public MinecraftProtocol getProtocol() {
		return protocol;
	}
	
}
