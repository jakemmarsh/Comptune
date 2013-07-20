package storage;
import  java.io.File;
import  java.io.IOException;
import  java.io.Serializable;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

/**
 * This class creates a song object which is used to store the path 
 * of a song, and information about the MP3 file itself.
 */
public class Song implements Serializable, Comparable <Song>
{
		
	private static final long serialVersionUID = 7526472295622776147L;
	
	private Mp3File mp3file = null;
	private String  path    = null;
	private String  artist  = null;
	private String  title   = null;
	private String  album   = null;
	
	public  int     key     = 0;
	public  Song    left    = null;
	public  Song    right   = null;
	public  File    f       = null;
	
	/*
	 * Constructs the song object, takes a key for look up purposes, and
	 * a file. It then uses the file to extract information about the 
	 * MP3. 
	 */
	// =====================================================================
	public Song (int key, File song) 
	{
		
		this.f   = song;
		this.key = key;
		
		try
		{
			path  = song.getAbsolutePath();
			mp3file = new Mp3File (path);
		} 
		
		catch (UnsupportedTagException e) {
			e.printStackTrace();
		}
		
		catch (InvalidDataException e)
		{
			e.printStackTrace();
		} 
		
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		

		if (mp3file.hasId3v1Tag())
		{
			ID3v1 id3v1Tag = mp3file.getId3v1Tag();
			artist = id3v1Tag.getArtist();
			title  = id3v1Tag.getTitle ();
			album  = id3v1Tag.getAlbum ();
		} 
		
		else if (mp3file.hasId3v2Tag()) 
		{
			ID3v2 id3v2Tag = mp3file.getId3v2Tag();
			artist = id3v2Tag.getArtist();
			title  = id3v2Tag.getTitle ();
			album  = id3v2Tag.getAlbum ();
		}
		
	}
	
	/*
	 * Returns the path of the song
	 */
	public String getPath ()
	{
		return path;
	}
	
	/*
	 * Returns the song's title
	 */
	public String getSongTitle ()
	{
		return title;
	}
	
	/*
	 * Returns the artist of the song
	 */
	public String getArtist ()
	{
		return artist;
	}
	
	/*
	 * Returns the name of the album
	 */
	public String getAlbum ()
	{
		return album;
	}

	/*
	 * Used to compare too song nodes by key value
	 */
	@Override
	public int compareTo (Song otherSongNode)
	{
		return key - otherSongNode.key;
	}

}
