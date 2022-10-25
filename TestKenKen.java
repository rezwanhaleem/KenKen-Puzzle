package edu.nyit.csci.cp2.kenken;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class TestKenKen {

	private JFrame frame;
	private JDesktopPane startPane;
	private JDesktopPane kenPane;
	private CardLayout card;

	private KenKen puzzle;
	private int size;

	private JLabel lblheading;
	private JButton btn5;
	private JButton btn6;
	private JButton btn9;

	private JButton btnSubmit;
	private JLabel lblResult;
	
	private JLabel[][] lbl;
	private JLabel[][] subLbl;
	
	private boolean[][] selected;

	private Border[][] border;

	private int[][][] thickness;
	
	private String[][] target;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					TestKenKen window = new TestKenKen();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public TestKenKen() 
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		card = new CardLayout(0, 0);

		frame = new JFrame();
		frame.setBounds(77, 77, 77 + (75*11), 77 + (75*11));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(card);
		frame.setFocusable(true);
		frame.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) 
			{
				char pressed = e.getKeyChar();

				if(pressed >= '1' && pressed <= '6' )
					for(int row = 0; row < size; row++)
						for(int col = 0; col < size; col++)
							if(selected[row][col])
								lbl[row][col].setText(new String() + pressed);
			}
		});

		startPane = new JDesktopPane();
		startPane.setBackground(Color.WHITE);
		frame.getContentPane().add(startPane, "startPane");

		kenPane = new JDesktopPane();
		kenPane.setBackground(Color.WHITE);
		frame.getContentPane().add(kenPane, "kenPane");

		card.show(frame.getContentPane(), "startPane");

		lblheading = new JLabel("Choose a Ken Ken Puzzle: ");
		lblheading.setForeground(new Color(30, 144, 255));
		lblheading.setFont(new Font("Trajan Pro", Font.BOLD, 16));
		lblheading.setHorizontalAlignment(SwingConstants.CENTER);
		lblheading.setBounds(153, 29, 351, 130);
		startPane.add(lblheading);

		btn5 = new JButton("5 x 5");
		btn5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				puzzle = new KenKen(5);
				card.show(frame.getContentPane(), "kenPane");
				size = 5;
				start();
			}
		});
		btn5.setForeground(new Color(30, 144, 255));
		btn5.setFont(new Font("Vani", Font.BOLD, 17));
		btn5.setBounds(272, 170, 99, 57);
		startPane.add(btn5);

		btn6 = new JButton("6 x 6");
		btn6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				puzzle = new KenKen(6);
				card.show(frame.getContentPane(), "kenPane");
				size = 6;
				start();
			}
		});
		btn6.setForeground(new Color(30, 144, 255));
		btn6.setFont(new Font("Vani", Font.BOLD, 17));
		btn6.setBounds(272, 263, 99, 57);
		startPane.add(btn6);

		btn9 = new JButton("9 x 9");
		btn9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				puzzle = new KenKen(9);
				card.show(frame.getContentPane(), "kenPane");
				size = 9;
				start();
			}
		});
		btn9.setForeground(new Color(30, 144, 255));
		btn9.setFont(new Font("Vani", Font.BOLD, 17));
		btn9.setBounds(272, 359, 99, 57);
		startPane.add(btn9);

	}

	private void start() 
	{
		thickness = new int[size][size][4];
		for(int row = 0; row < size; row++)
			for(int col = 0; col < size; col++)
				for(int i = 0; i < 4; i++)
					thickness[row][col][i] = 5;
		
		for (int row = 0, stopRow = puzzle.cage.size(); row < stopRow; row++) 
		{
			for (int col = 0, stopCol = puzzle.cage.get(row).size(); col < stopCol ; col++) 
			{
				Point point = puzzle.cage.get(row).get(col);
				int x = point.getX();
				int y = point.getY();
				if (puzzle.cage.get(row).contains(point.west()))
					thickness[x][y][0] = 1;
				if (puzzle.cage.get(row).contains(point.south()))
					thickness[x][y][1] = 1;
				if (puzzle.cage.get(row).contains(point.east()))
					thickness[x][y][2] = 1;
				if (puzzle.cage.get(row).contains(point.north()))
					thickness[x][y][3] = 1;
			}
		}

		lbl = new JLabel[size][size];
		selected = new boolean[size][size];
		border = new Border[size][size];
		subLbl = new JLabel[size][size];
		
		
		target = new String[size][size];
		for(int row = 0; row < size; row++)
			for(int col = 0; col < size; col++)
				target[row][col] = " ";
		
		for (int iterate = 0, stop = puzzle.target.size(); iterate < stop; iterate++) 
		{
			Point p = puzzle.topLeft.get(iterate);
			String hint = Integer.toString(puzzle.target.get(iterate)) + puzzle.operator.get(iterate); 
			target[p.getX()][p.getY()] = hint;
		}
		
		int row = 0;
		int col = 0;
		for( row = 0; row < size; row++) 
		{
			for ( col = 0; col < size; col++) 
			{
				lbl[row][col] = new JLabel("");
				lbl[row][col].setFont(new Font("Tahoma", Font.PLAIN, 17));
				lbl[row][col].setForeground(Color.BLACK);
				lbl[row][col].setHorizontalAlignment(SwingConstants.CENTER);
				lbl[row][col].setBounds(77 + (75*col), 77 + (75*row), 77, 77);
				border[row][col] = BorderFactory.createMatteBorder(thickness[row][col][0], thickness[row][col][1], thickness[row][col][2], thickness[row][col][3], new Color(30, 144, 255));
				lbl[row][col].setBorder(border[row][col]);
				
				lbl[row][col].addMouseListener( new SpeacialMouseListener(row,col) );
					
				
				subLbl[row][col] = new JLabel(target[row][col]);
				subLbl[row][col].setFont(new Font("Tahoma", Font.PLAIN, 15));
				subLbl[row][col].setForeground(new Color(30, 144, 255));
				subLbl[row][col].setHorizontalAlignment(SwingConstants.LEFT);
				subLbl[row][col].setBounds(5, 0, 77, 31);
				lbl[row][col].add(subLbl[row][col]);
				
				kenPane.add(lbl[row][col]);
			}
		}
		
		btnSubmit = new JButton("Submit Puzzle");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean done = true, incomplete = false;
				for(int row = 0; row < size; row++)
				{
					for(int col = 0; col < size; col++)
					{
						try
						{
							if( puzzle.answer.get(row).get(col) != Integer.parseInt(lbl[row][col].getText()) )
							{
								done = false;
							}
						}
						catch(NumberFormatException nfe)
						{
							done = false;
							incomplete = true;
						}
					}
				}
				
				if(done)
					lblResult.setText("Solved!");
				else if(incomplete)
					lblResult.setText("Incomplete!");
				else
					lblResult.setText("Wrong!");
			}
		});
		btnSubmit.setBounds(90, 7 + (75*(row+1)), 153, 45);
		kenPane.add(btnSubmit);
		
		lblResult = new JLabel();
		lblResult.setForeground(Color.RED);
		lblResult.setFont(new Font("Trajan Pro", Font.PLAIN, 16));
		lblResult.setHorizontalAlignment(SwingConstants.CENTER);
		lblResult.setBounds(273, (75*(row+1)), 153, 45);
		kenPane.add(lblResult);
	}

	private class SpeacialMouseListener extends MouseAdapter 
	{
		private final int inRow;
		private final int inCol;
		
    	public SpeacialMouseListener(int inRow, int inCol) 
    	{
        	this.inRow = inRow;
        	this.inCol = inCol;
    	}

    	public void mousePressed(MouseEvent e) 
		{
		}

		public void mouseReleased(MouseEvent e) 
		{
		}

		public void mouseEntered(MouseEvent e) 
		{
			lbl[inRow][inCol].setBackground(new Color(125, 171, 250));
			lbl[inRow][inCol].setOpaque(true);
		}

		public void mouseExited(MouseEvent e) 
		{
			if(!selected[inRow][inCol])
				lbl[inRow][inCol].setBackground(Color.WHITE);
		}
		

		public void mouseClicked(MouseEvent e)
		{
			for(int x = 0; x < size; x++)
				for(int y = 0; y < size; y++)
					if( x == inRow && y == inCol)	
						selected[x][y] = true;
					else
						selected[x][y] = false;
			
			lbl[inRow][inCol].setBackground(new Color(125, 171, 250));
			lbl[inRow][inCol].setOpaque(true);
			
			for(int row = 0; row < size; row++)
				for(int col = 0; col < size; col++)
					if(!selected[row][col])
						lbl[row][col].setBackground(Color.WHITE);
		}
	}
}
