package player.controller;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import player.model.Playlist;
import player.model.PlaylistsContainer;
import view.View;

import javax.imageio.ImageIO;
import javax.media.Manager;
import javax.media.Player;


public class Controller implements ActionListener, ChangeListener {
	private Player player;
	private PlaylistsContainer playlists;
	private View GUI; 
	
	
	private int currentPlaylistIndex;
	private int currentSongIndex;
	private boolean isItPlaying;
	
	
	public Controller() throws Exception{
		this.playlists= new PlaylistsContainer();
		this.GUI = new View();
		
		addActionListeners();
		
		loadSongsIntoPlaylist("assaf");
		loadSongsIntoPlaylist("ahmad2");
		
		GUI.getVolumeSlider().addChangeListener(this);
		
		//Load First Song
		currentPlaylistIndex = 0;
		currentSongIndex =0;
		initializePlayer(currentPlaylistIndex, currentSongIndex);
		isItPlaying=false; 
		
	}
	
	
	//Create a Playlist and load songs into it
	
	private void loadSongsIntoPlaylist(String playlistName) throws Exception {
		URL path = Controller.class.getResource("playlists/" + playlistName + ".txt");
		File file = new File(path.getFile());
		playlists.add(new Playlist());
		playlists.loadSongs(file, playlists.getNumberOfPlaylists()-1);
	}
	
	private void initializePlayer(int currentPlaylistIndex, int currentSongIndex) throws Exception {
		
		player = Manager.createRealizedPlayer(
						(playlists.getSong(currentPlaylistIndex, currentSongIndex)).toURI().toURL()
						);
	}
	
	
	private void loadSongAndPlay(int currentPlaylistIndex, int currentSongIndex) throws Exception {
		stopCurrentSong();
		initializePlayer(currentPlaylistIndex, currentSongIndex);
		start();
		
	}
	
	private void addActionListeners() {
		GUI.getPrevPlaylistButton().addActionListener(this);
		GUI.getBackButton().addActionListener(this);
		GUI.getPlayButton().addActionListener(this);
		GUI.getSkipButton().addActionListener(this);
		GUI.getNextPlaylistButton().addActionListener(this);
		GUI.getNextPlaylistButton().addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		resetIcons();
		
		
		if((((JButton) e.getSource())== GUI.getPrevPlaylistButton())) {
			try {
				prevPlaylist();
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}
		
		if((((JButton) e.getSource())== GUI.getBackButton())) {
			try {
				back();
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}
		
		if((((JButton) e.getSource())== GUI.getPlayButton())) {
			if(!isItPlaying) {
				start();
				changePlayToPause();
			} else {
				stopCurrentSong();
				changePauseToPlay();
			}
		}
		
		
		if((((JButton) e.getSource())== GUI.getSkipButton())) {
			try {
				skip();
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}
		
		if((((JButton) e.getSource())== GUI.getNextPlaylistButton())) {
			try {
				nextPlaylist();
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}
		
	}
	
	private void start() {
		player.start();
		(player.getGainControl()).setLevel((float)GUI.getVolumeSlider().getValue()/150.0f);
		GUI.setTitle(playlists.getSong(currentPlaylistIndex, currentSongIndex).getName());
		isItPlaying= true;
		
	}
	private void stopCurrentSong() {
		player.stop();
		isItPlaying = false;
	}

	private void prevPlaylist() throws Exception {
		if(currentPlaylistIndex==0) {
			GUI.getPrevPlaylistButton().setText(null);
			changePrevPlaylistToError();
		}else {
			currentSongIndex=0;
			loadSongAndPlay(--currentPlaylistIndex, currentSongIndex);
			changePlayToPause();
		}
		
	}
	
	private void back() throws Exception {
		if(currentSongIndex==0) {
			changeBackToError();
		}else {
			loadSongAndPlay(currentPlaylistIndex, --currentSongIndex);
			changePlayToPause();
		}
	}
	
	private void skip() throws Exception {
		if(currentSongIndex == playlists.getPlaylist(currentPlaylistIndex).getCount()-1) {
			changeSkipToError();
		} else {
			loadSongAndPlay(currentPlaylistIndex, ++currentSongIndex);
			changePlayToPause();
		}
	}
	
	private void nextPlaylist() throws Exception {
		if(currentPlaylistIndex== playlists.getNumberOfPlaylists()-1) {
			GUI.getNextPlaylistButton().setText(null);
			changeNextPlaylistToError();
		} else {
			currentSongIndex =0;
			loadSongAndPlay(++currentPlaylistIndex, currentSongIndex);
			changePlayToPause();
		}
	}
	
	private void changePlayToPause() {
		try {
			Image icon=ImageIO.read(View.class.getResource("icons/pause.png"));
			GUI.getPlayButton().setIcon(new ImageIcon(icon));
			
		} catch (IOException ex) {
			System.out.println("icons/pause.png not found.");
		}
	}
	
	
	private void changePauseToPlay() {
		try {
			Image icon=ImageIO.read(View.class.getResource("icons/play.png"));
			GUI.getPlayButton().setIcon(new ImageIcon(icon));
			
		} catch (IOException ex) {
			System.out.println("icons/play.png not found.");
		}
	}
	
	private void changePrevPlaylistToError() {
		try {
			Image icon=ImageIO.read(View.class.getResource("icons/error.png"));
			GUI.getPrevPlaylistButton().setIcon(new ImageIcon(icon));
			
		} catch (IOException ex) {
			System.out.println("icons/error.png not found.");
		}
	}
	
	private void changeBackToError() {
		try {
			Image icon=ImageIO.read(View.class.getResource("icons/error.png"));
			GUI.getBackButton().setIcon(new ImageIcon(icon));
			
		} catch (IOException ex) {
			System.out.println("icons/error.png not found.");
		}
	}
	
	
	private void changeSkipToError() {
		try {
			Image icon=ImageIO.read(View.class.getResource("icons/error.png"));
			GUI.getSkipButton().setIcon(new ImageIcon(icon));
			
		} catch (IOException ex) {
			System.out.println("icons/error.png not found.");
		}
	}
	
	private void changeNextPlaylistToError() {
		try {
			Image icon=ImageIO.read(View.class.getResource("icons/error.png"));
			GUI.getNextPlaylistButton().setIcon(new ImageIcon(icon));
			
		} catch (IOException ex) {
			System.out.println("icons/error.png not found.");
		}
	}
	
	
	private void resetIcons() {
		try {
			Image icon = ImageIO.read(View.class.getResource("icons/prev.png"));
			GUI.getBackButton().setIcon(new ImageIcon(icon));
		} catch (IOException ex) {
			System.out.println("icons/prev.png not found.");
		}
		try {
			Image icon = ImageIO.read(View.class.getResource("icons/next.png"));
			GUI.getSkipButton().setIcon(new ImageIcon(icon));
		} catch (IOException ex) {
			System.out.println("icons/next.png not found.");
		}
		
		GUI.getPrevPlaylistButton().setIcon(null);
		GUI.getPrevPlaylistButton().setText("PP");
		
		GUI.getNextPlaylistButton().setIcon(null);
		GUI.getNextPlaylistButton().setText("NP");
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		(player.getGainControl()).setLevel((float)GUI.getVolumeSlider().getValue()/150.0f);
	}
	
	
}
