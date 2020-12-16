package tech.mistermel.terminator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.mistermel.terminator.file.AccountsFile;
import tech.mistermel.terminator.mc.Account;
import tech.mistermel.terminator.mc.BotPlayer;
import tech.mistermel.terminator.web.WebServer;

public class TheTerminator {

	private static final Logger logger = LoggerFactory.getLogger(TheTerminator.class);
	
	private WebServer webServer;
	private AccountsFile accountsFile;
	
	private String ip;
	private int port;
	
	private List<Account> accounts = new ArrayList<>();
	private Map<Account, BotPlayer> players = new HashMap<>();
	
	public void start() {
		this.accountsFile = new AccountsFile();
		this.accounts = accountsFile.loadAccounts();
		
		this.webServer = new WebServer();
		
		logger.info("Startup completed (took {}ms)", (System.currentTimeMillis() - Launcher.startupTime));
	}
	
	public void setServer(String ip, int port) {
		logger.info("Setting server, disconnecting {} bot player(s) already connected", players.size());
		for(BotPlayer player : this.getPlayers()) {
			player.disconnect();
		}
		
		this.ip = ip;
		this.port = port;
	}
	
	public void connectAccount(int index) {
		this.connectAccount(accounts.get(index));
	}
	
	public void connectAccount(Account account) {
		if(ip == null) {
			logger.warn("Could not connect account, server not specified");
			return;
		}
		
		BotPlayer player = new BotPlayer(account);
		players.put(account, player);
		player.connect(ip, port);
	}
	
	public Collection<BotPlayer> getPlayers() {
		return players.values();
	}
	
	public List<Account> getAccounts() {
		return accounts;
	}
	
	public WebServer getWebServer() {
		return webServer;
	}
	
}