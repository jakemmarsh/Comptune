package storage;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class SplayTree <Key extends Comparable <Key>, Value> implements Serializable 
{

	private Song top;

	
	/**
	 * Returns either true or false depending on whether the key we are 
	 * searching for exists or not in the splay tree. 
	 */
	// =====================================================================
	public boolean contains (int key)
	{
		return (get(key) != null);
	}

	
	
	/**
	 * Takes an integer key, and splays the corresponding song node to the 
	 * top of the tree, if it exists.
	 */
	// =====================================================================
	public Song get (int key)
	{
		top = splay (top, key);
		
		/* This checks if the key we are searching for is truly the key at 
		 * the top of the tree. If it is, it returns the song path, otherwise
		 * it returns null 
		 * */
		int xyz = key - top.key;
		if (xyz == 0)
			return top;
		
		else
			return null;
	}

	
	
	/**
	 * This adds a song node to the tree. If no nodes exist, the first song
	 * node we insert becomes the top node of the tree. 
	 */
	// =====================================================================
	public void add (Song song)
	{
		
		/*
		 * Checks if the top node is empty, and if it is, the first inserted
		 * song node becomes the top node. 
		 */
		if (top == null)
		{
			top = song;
			return;
		}
		
		top = splay (top, song.key);
		
		/*
		 * This saves a value based on (our new song node - the top). 
		 * If the returned value is less than 0. It goes with the first
		 * option, and if it is greater than 0, it goes with the second option.
		 */
		int xyz = song.compareTo (top);

		// Option 1
		if (xyz < 0)
		{
			Song temp = new Song (song.key, song.f);
			temp.left = top.left;
			temp.right = top;
			top.left = null;
			top = temp;
		}

		// Option 2
		else if (xyz > 0)
		{
			Song temp = new Song (song.key, song.f);
			temp.right = top.right;
			temp.left = top;
			top.right = null;
			top = temp;
		}

		/* 
		 * In this case, the top node, and the song we are looking for are
		 * the same
		 */
		else if (xyz == 0)
		{
			top.getPath().equals(song.getPath());
		}
		
	}

	
	
	/**
	 * This removes a song, based on an integer value given.
	 */
	// =====================================================================
	public void remove (int key)
	{
		/*
		 * If the top is empty, the tree is empty, therefore we have nothing
		 * to remove.
		 */
		if (top == null)
		{
			return;
		}
		
		/*
		 * This splays the node corresponding to our key to the top of the tree.
		 * It then checks to see if our node is in fact the top node. 
		 */
		top = splay(top, key);
		int xyz = key - top.key;

		if (xyz == 0)
		{

			if (top.left == null)
			{
				top = top.right;
			}

			else
			{
				Song x = top.right;
				top = top.left;
				splay(top, key);
				top.right = x;
			}
		}
	}

	
	
	/**
	 * This is the splay function.
	 */
	// =====================================================================
	private Song splay (Song h, Integer key)
	{
		
		if (h == null)
			return null;
		
		int com = key.compareTo (h.key);
		
		if (com < 0)
		{
			if (h.left == null)
			{
				return h;
			}
			
			int xyz2 = key.compareTo (h.left.key);
		
			if (xyz2 < 0)
			{
				h.left.left = splay (h.left.left, key);
				h = rotateRight (h);
			} 
			
			else if (xyz2 > 0)
			{
				h.left.right = splay(h.left.right, key);
				if (h.left.right != null)
					h.left = rotateLeft(h.left);
			}
			
			if (h.left == null)
				return h;
			
			else
				return rotateRight(h);
			
		}
		
		else if (com > 0)
		{
			if (h.right == null)
			{
				return h;
			}
			
			int xyz2 = key.compareTo (h.right.key);
			if (xyz2 < 0)
			{
				h.right.left = splay(h.right.left, key);
				if (h.right.left != null)
					h.right = rotateRight(h.right);
			}
			
			else if (xyz2 > 0)
			{
				h.right.right = splay(h.right.right, key);
				h = rotateLeft(h);
			}
			
			if (h.right == null)
				return h;
			
			else
				return rotateLeft(h);
			
		} 
		
		else
			return h;
	}

	
	
	// =====================================================================
	private Song rotateRight (Song h)
	{
		Song z = h.left;
		h.left = z.right;
		z.right = h;
		return z;
	}

	
	// =====================================================================
	private Song rotateLeft (Song h)
	{
		Song z = h.right;
		h.right = z.left;
		z.left = h;
		return z;
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList showState (int size)
	{
		ArrayList <String> state = new ArrayList <String> ();
		
		System.out.println ("Top node" +top.getArtist());
		for (int i = 0; i < size; i++)
		{
			state.add (get(i).getSongTitle());
		}
		
		return state;
	}
	
	
}
