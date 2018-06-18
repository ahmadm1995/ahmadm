package player.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Playlist {

	private List<Song> playlist;
	
	public Playlist() {
		playlist = new ArrayList<>();
	}
	
	
	public void loadSongs(File fin) throws IOException {
		
		FileInputStream fis = new FileInputStream(fin);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		String line= null;
		
		while ((line = br.readLine()) != null) {
			playlist.add(new Song(line));
			
		}
		br.close();
		
	}
	
	public Song get(int index) {
		return playlist.get(index);
	}
	
	public Song getSong(int currentSongIndex) {
		return playlist.get(currentSongIndex);
	}
	public int getCount() {
		return playlist.size();
	}
	
}
