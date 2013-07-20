import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import storage.Song;
import storage.SplayTree;


/**
 * This class is used as an intermediate between the GUI and the 
 * splay tree storage device. 
 */
public class FileManager 
{
		
	private File library; // our music library
	private File libText;

	@SuppressWarnings("rawtypes")
	public  SplayTree storage;
	private int       songID;
	

	/*
	 * This constructs the file manager, creating the splay tree
	 * and a folder in which the serial file is stored.
	 */
	// =====================================================================
	@SuppressWarnings("rawtypes")
	public FileManager () 
	{
		
		songID  = 0;
		
		storage = new SplayTree ();
		
		// The library is a folder that stores serial file.
		library = new File ("Library");
		library.mkdir();
	
		// The serial file 
		libText = new File (library, "LibText.ser");

		// Load the last set up
		//load();
		
	}
	
	
	/**
	 * This command receives a file from the file browser, and makes a 
	 * song object from it, giving it an ID number to label it as.
	 */
	// =====================================================================
	public Song addSong (File songFile)
	{
		
		/*
		 * This creates a new song object with the current songID
		 * and the song file given by the GUI.
		 */
		Song newSong = new Song (songID, songFile);
		
		/*
		 * This sends the new song object that we made to the splay tree.
		 */
		storage.add (newSong);
		
		/*
		 *  Increments the ID number, so that the next song object is also
		 *  unique.
		 */
		songID += 1;
		
		/*
		 * This saves the state of the splay tree.
		 */
		//save ();
		
		
		// shows the state after uploading a new song
		//System.out.println (storage.showState (songID));
		
		/*
		 * Returns the song object, that it can be drawn by the GUI.
		 */
		return newSong;
	}
	
	
	/*
	 * Removes a song from our tree
	 */
	// =====================================================================
	public void remove (int key)
	{
		storage.remove (key);
	}

	
	/*
	 * Saves each song object to text.
	 */
	// =====================================================================
	public void save ()
	{
		
		FileOutputStream fileOut;
		ObjectOutputStream out;

		try
		{
			fileOut = new FileOutputStream   (libText);
			out     = new ObjectOutputStream (fileOut);

			/*
			 * Saves the state of each node in the splay tree
			 */
			//for (int i = 0; i < songID; i++)
			//{
				Song s = (storage.get (0));
				//System.out.println (s);
				out.writeObject (s);
			//}
			
			out.close();
			fileOut.close();

		}

		catch (IOException i)
		{
			i.printStackTrace();
		}

	}


	/**
	 * This 
	 */
	// =====================================================================
	/*
	public void load ()
	{
		
		FileInputStream   fileIn;
		ObjectInputStream in;
		
		try 
		{
			fileIn = new FileInputStream   (libText);
			in     = new ObjectInputStream (fileIn);
			

			songs = (ArrayList <Song>) in.readObject();
			
			//for (Song s : songs)
			//	System.out.println (s.getSongTitle());
			
			in.close ();
		}
		
		catch (FileNotFoundException e)
		{
			e.printStackTrace ();
		}
		
		catch (IOException e)
		{
			e.printStackTrace ();
		} 
		
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
	}
	*/

	
}
