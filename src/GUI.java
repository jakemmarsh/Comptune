import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import javazoom.jl.decoder.JavaLayerException;

import org.w3c.dom.stylesheets.MediaList;

import storage.Song;


import com.mpatric.mp3agic.Mp3File;

@SuppressWarnings("serial")
public class GUI extends JFrame implements ActionListener {

	/*
	 * Initalize variables.
	 */
	private FileManager fm;

	private JButton search_button;

	public DefaultTableModel defaultTable;
	public String file;
	public Mp3File mp3file;

	public int state;
	public String delete;

	public int row;
	public int id;

	public JTable mediaTable;

	public TableColumnModel columnModel;

	public PausablePlayer player;

	public Color panel_dark = new Color(18, 18, 18);
	public Color panel_light = new Color(43, 42, 37);
	public Color button_dark = new Color(196, 194, 195);
	public Color button_light = new Color(249, 244, 246);

	/**
	 * Initializes our GUI
	 */
	// =====================================================================
	public GUI() {
		/*
		 * Create FileManager
		 */
		fm = new FileManager();

		/*
		 * Set options for frame
		 */
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("C O M P T U N E ");
		setSize(500, 450);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(null);

		/*
		 * Create JPanel top_panel to contain various components. Painted with a
		 * gradient.
		 */
		// =====================================================================
		JPanel top_panel = new JPanel() {

			@Override
			protected void paintComponent(Graphics grphcs) {
				super.paintComponent(grphcs);

				Graphics2D g2d = (Graphics2D) grphcs;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);

				GradientPaint gp = new GradientPaint(0, 0, panel_light, 0,
						getHeight(), panel_dark);

				g2d.setPaint(gp);
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		top_panel.setBounds(0, 0, 494, 87);
		getContentPane().add(top_panel);
		top_panel.setLayout(null);

		/*
		 * Create "last" button to change to previous song.
		 */
		final JButton last_button = new JButton();
		last_button.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"img/last.png")));
		last_button.setBorderPainted(false);
		last_button.setFocusPainted(false);
		last_button.setContentAreaFilled(false);
		last_button.addActionListener(this);
		last_button.setBounds(180, 14, 54, 54);
		top_panel.add(last_button);
		last_button.setVisible(false);

		/*
		 * Initialize state to "0", meaning no song is playing.
		 */
		state = 0;

		/*
		 * Create "play" button, which will play the current song.
		 */
		final JButton play_button = new JButton();
		play_button.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"img/play.png")));
		play_button.setBorderPainted(false);
		play_button.setFocusPainted(false);
		play_button.setContentAreaFilled(false);
		play_button.addActionListener(this);
		play_button.setBounds(225, 14, 54, 54);
		top_panel.add(play_button);

		/*
		 * Create "pause" button, which will pause the song that is currently
		 * playing. Only visible once a song is playing.
		 */
		final JButton pause_button = new JButton();
		pause_button.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"img/pause.png")));
		pause_button.setBorderPainted(false);
		pause_button.setFocusPainted(false);
		pause_button.setContentAreaFilled(false);
		pause_button.addActionListener(this);
		pause_button.setBounds(225, 14, 54, 54);
		top_panel.add(pause_button);
		pause_button.setVisible(false);

		/*
		 * Add listener to "pause" button to pause the current song, set the
		 * state to "0", show the "Play" button, and hide itself.
		 */
		pause_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.pause();
				state = 0;
				play_button.setVisible(true);
				pause_button.setVisible(false);
			}
		});

		/*
		 * Add listener to "play" button to play the current song, set the state
		 * to "1", show the "Pause" button, and hide itself.
		 */
		play_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.resume();
				state = 1;
				play_button.setVisible(false);
				pause_button.setVisible(true);
			}
		});

		/*
		 * Create "next" button to change to next song.
		 */
		final JButton next_button = new JButton();
		next_button.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"img/next.png")));
		next_button.setBorderPainted(false);
		next_button.setFocusPainted(false);
		next_button.setContentAreaFilled(false);
		next_button.addActionListener(this);
		next_button.setBounds(270, 14, 54, 54);
		top_panel.add(next_button);
		next_button.setVisible(false);

		JSlider volume_slider = new JSlider();
		volume_slider.setOpaque(false);
		volume_slider.setBounds(362, 35, 122, 17);
		top_panel.add(volume_slider);
		volume_slider.setVisible(false);

		/*
		 * Create a JLabel to contain the name of the song currently playing.
		 * Initially invisible.
		 */
		final JLabel song_name = new JLabel("Song name");
		song_name.setForeground(Color.WHITE);
		song_name.setBounds(15, 11, 160, 17);
		top_panel.add(song_name);
		song_name.setVisible(false);

		/*
		 * Create a JLabel to contain the artist of the song currently playing.
		 * Initially invisible.
		 */
		final JLabel artist_name = new JLabel("Artist name");
		artist_name.setForeground(Color.WHITE);
		artist_name.setBounds(15, 33, 160, 14);
		top_panel.add(artist_name);
		artist_name.setVisible(false);

		/*
		 * Create a JLabel to contain the album of the song currently playing.
		 * Initially invisible.
		 */
		final JLabel album_name = new JLabel("Album name");
		album_name.setForeground(Color.WHITE);
		album_name.setBounds(15, 55, 160, 14);
		top_panel.add(album_name);
		album_name.setVisible(false);

		/*
		 * Add action listener to "last" button. Decrements the current row and
		 * plays the corresponding song. If decrementing will place the count
		 * out of index, loop back to bottom of the table and play the newly
		 * selected song.
		 */
		last_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((row - 1) >= 0) {
					row = row - 1;
				} else {
					row = defaultTable.getRowCount() - 1;
				}
				id = Integer.parseInt((String) defaultTable.getValueAt(row, 3));
				Song song = fm.storage.get(id);
				for (int i = 0; i <= defaultTable.getColumnCount(); i++) {
					if (defaultTable.getColumnName(i) == "Song") {
						song_name.setText(song.getSongTitle());
					} else if (defaultTable.getColumnName(i) == "Artist") {
						artist_name.setText(song.getArtist());
					} else if (defaultTable.getColumnName(i) == "Album") {
						album_name.setText(song.getAlbum());
					}
				}
				try {
					FileInputStream input = new FileInputStream(song.getPath());
					if (player != null
							&& (player.getStatus() == 1 || player.getStatus() == 2)) {
						player.close();
					}
					player = new PausablePlayer(input);
					player.play();
					last_button.setVisible(true);
					next_button.setVisible(true);
					state = 1;
					play_button.setVisible(false);
					pause_button.setVisible(true);

				}

				catch (JavaLayerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		/*
		 * Add action listener to "next" button. Increments the current row and
		 * plays the corresponding song. If incrementing will place the count
		 * out of index, loop back to top of table and play the newly selected
		 * song.
		 */
		next_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((row + 1) <= defaultTable.getRowCount() - 1) {
					row = row + 1;
				} else {
					row = 0;
				}
				id = Integer.parseInt((String) defaultTable.getValueAt(row, 3));
				Song song = fm.storage.get(id);
				for (int i = 0; i <= defaultTable.getColumnCount(); i++) {
					if (defaultTable.getColumnName(i) == "Song") {
						song_name.setText(song.getSongTitle());
						song_name.setVisible(true);
					} else if (defaultTable.getColumnName(i) == "Artist") {
						artist_name.setText(song.getArtist());
						artist_name.setVisible(true);
					} else if (defaultTable.getColumnName(i) == "Album") {
						album_name.setText(song.getAlbum());
						album_name.setVisible(true);
					}
				}
				try {
					FileInputStream input = new FileInputStream(song.getPath());
					if (player != null
							&& (player.getStatus() == 1 || player.getStatus() == 2)) {
						player.close();
					}
					player = new PausablePlayer(input);
					player.play();
					last_button.setVisible(true);
					next_button.setVisible(true);
					state = 1;
					play_button.setVisible(false);
					pause_button.setVisible(true);

				}

				catch (JavaLayerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		/*
		 * Initialize a "MediaList" JPanel to contain the table of songs. The
		 * columns are "Song", "Artist", "Album", and an invisible column to
		 * contain the song's "ID" used for retrieval from the splay tree.
		 */
		// =====================================================================
		class MediaList extends JPanel {
			private static final long serialVersionUID = 1L;
			private JTable mediaTable;

			/*
			 * Define methods for the MediaList.
			 */
			MediaList() {
				createComponents();
				makeLayout();
			}

			/*
			 * Initialize the table content and options.
			 */
			public void createComponents() {
				Object[] header = new String[] { "Song", "Artist", "Album",
						"ID" };
				Object[][] model = new String[0][0][0][0];

				defaultTable = new DefaultTableModel(model, header);
				mediaTable = new JTable() {
					private static final long serialVersionUID = 1L;

					public boolean isCellEditable(int rowIndex, int colIndex) {
						return false; // Disallow the editing of any cell
					}
				};

				/*
				 * Initialize the model for the table.
				 */
				mediaTable.setModel(defaultTable);
				mediaTable.setShowVerticalLines(false);
				mediaTable.setFocusable(false);
				JTableHeader h = mediaTable.getTableHeader();
				h.setBackground(panel_dark);
				h.setForeground(Color.WHITE);
				columnModel = mediaTable.getColumnModel();
				TableColumn column = columnModel.getColumn(3);
				mediaTable.removeColumn(column);
				/*
				 * Add a mouse listener to the table. If any row is
				 * double-clicked, update the JLabels to the corresponding info
				 * and begin playing the song pertaining to the selected row.
				 */
				mediaTable.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2) {
							JTable target = (JTable) e.getSource();
							row = target.getSelectedRow();
							id = Integer.parseInt((String) defaultTable
									.getValueAt(row, 3));
							Song song = fm.storage.get(id);
							for (int i = 0; i <= defaultTable.getColumnCount(); i++) {
								if (defaultTable.getColumnName(i) == "Song") {
									song_name.setText(song.getSongTitle());
									song_name.setVisible(true);
								} else if (defaultTable.getColumnName(i) == "Artist") {
									artist_name.setText(song.getArtist());
									artist_name.setVisible(true);
								} else if (defaultTable.getColumnName(i) == "Album") {
									album_name.setText(song.getAlbum());
									album_name.setVisible(true);
								}
							}
							try {
								FileInputStream input = new FileInputStream(
										song.getPath());
								if (player != null
										&& (player.getStatus() == 1 || player
												.getStatus() == 2)) {
									player.close();
								}
								player = new PausablePlayer(input);
								player.play();
								last_button.setVisible(true);
								next_button.setVisible(true);
								state = 1;
								play_button.setVisible(false);
								pause_button.setVisible(true);

							}

							catch (JavaLayerException e1) {
								// TODO Auto-generated catch block
								System.out.println("error");
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						}
					}
				});
			}

			public void makeLayout() {
				setLayout(new BorderLayout());
				add(new JScrollPane(mediaTable));
			}
		}

		/*
		 * Create MediaView JPanel to contain the mediaList.
		 */
		class MediaView extends JPanel {
			private static final long serialVersionUID = 1L;
			MediaList mediaList;

			public MediaView() {
				setBounds(0, 87, 494, 270);
				createComponents();
				makeLayout();
			}

			public void createComponents() {
				mediaList = new MediaList();
			}

			public void makeLayout() {
				setLayout(new BorderLayout());
				add(mediaList);
			}
		}

		/*
		 * Add mediaList and MediaView to the frame.
		 */
		getContentPane().add(new MediaView());

		/*
		 * Create bottom JPanel, painted with a gradient.
		 */
		JPanel bottom_panel = new JPanel() {

			protected void paintComponent(Graphics grphcs) {
				super.paintComponent(grphcs);

				Graphics2D g2d = (Graphics2D) grphcs;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);

				GradientPaint gp = new GradientPaint(0, 0, panel_light, 0,
						getHeight(), panel_dark);

				g2d.setPaint(gp);
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		bottom_panel.setForeground(Color.WHITE);
		bottom_panel.setBounds(0, 357, 494, 65);
		getContentPane().add(bottom_panel);
		bottom_panel.setLayout(null);

		/*
		 * Create "Search" button in order to call a JFileChooser, which will be
		 * used to add songs to the table and splay tree.
		 */
		class search_button extends JButton {
			private static final long serialVersionUID = 1L;

			public search_button() {
				super("Browse");
				setOpaque(false);
				setBounds(210, 22, 89, 23);
				setContentAreaFilled(false);
				setForeground(Color.darkGray);
				setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0,
						Color.BLACK));
			}

			public void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				int width = getWidth();
				int height = getHeight();
				Paint gradient = new GradientPaint(0, 0, button_light, 0,
						height, button_dark);
				g2.setPaint(gradient);
				g2.fillRect(0, 0, width, height);
				super.paintComponent(g);
			}
		}

		search_button = new search_button();
		/*
		 * Give search button the action to call a JFileChooser.
		 */
		search_button.addActionListener(this);

		bottom_panel.add(search_button);

		// drawOldSongs();

		setVisible(true);
	}

	/*
	 * This handles all the of actions performed by button clicks.
	 */
	public void actionPerformed(ActionEvent e) {
		/**
		 * If the search button is clicked
		 */
		if (e.getSource() == search_button) {

			FileNameExtensionFilter filter = new FileNameExtensionFilter("MP3",
					"MP3");

			UIManager.put("FileChooser.readOnly", Boolean.TRUE);
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(filter);
			chooser.setAcceptAllFileFilterUsed(false);

			int returnVal = chooser.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				/*
				 * This draws the new song, and sends the song file to the file
				 * manager.
				 */
				drawNewSong(fm.addSong(chooser.getSelectedFile()));
			}
		}
	}

	public void drawOldSongs() {
		// for (Song s : fm.storage) {
		// String[] songRow = { s.getSongTitle(), s.getArtist(), s.getAlbum() };
		//
		// defaultTable.addRow(songRow);
		// }
	}

	/*
	 * Method to add selected song to table by collecting all its info from the
	 * splay tree, adding it to an array, and adding it as a row.
	 */
	public void drawNewSong(Song ns) {

		String key = Integer.toString(ns.key);
		String[] songRow = { ns.getSongTitle(), ns.getArtist(), ns.getAlbum(),
				key };

		defaultTable.addRow(songRow);

	}

}
