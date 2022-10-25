package edu.nyit.csci.cp2.kenken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class KenKen 
{
	public static void main(String[] args) 
	{
		KenKen k = new KenKen(3);
	}

	int size;
	ArrayList<ArrayList<Integer>> answer;

	String difficulty;

	ArrayList<ArrayList<Point>> cage;
	ArrayList<String> operator;
	ArrayList<Integer> target;
	ArrayList<Point> topLeft;

	public KenKen()
	{
		size = 3;
		answer = new ArrayList<ArrayList<Integer>>(3);
		for( int row = 0; row < size; row++)
		{
			answer.add(new ArrayList<Integer>(3));
			for( int col = 0; col < size; col++)
			{
				answer.get(row).add(0);
			}
		}
		difficulty = "hard";
		randomize();
		mathematize();
	}

	public KenKen(int size)
	{
		this.size = size;
		answer = new ArrayList<ArrayList<Integer>>(size);
		for( int row = 0; row < size; row++)
		{
			answer.add(new ArrayList<>(size));
			for( int col = 0; col < size; col++)
			{
				answer.get(row).add(0);
			}
		}
		randomize();
		mathematize();
		
		for( int row = 0; row < size; row++)
		{
			for( int col = 0; col < size; col++)
			{
				System.out.print(answer.get(row).get(col));
			}
			System.out.println();
		}

	}

	public KenKen(int[][] answer)
	{
		if( answer.length != answer[0].length )
			return;

		size = answer.length;
		this.answer = new ArrayList<ArrayList<Integer>>(size);
		for( int row = 0; row < size; row++)
		{
			this.answer.add(new ArrayList<Integer>(size));
			for( int col = 0; col < size; col++)
			{
				this.answer.get(row).add(answer[row][col]);
			}
		}
		mathematize();
	}

	public KenKen(ArrayList<ArrayList<Integer>> answer)
	{
		this.answer = new ArrayList<ArrayList<Integer>>(answer);

	}

	public void randomize()
	{
		int random = 0;

		Set<Integer> norm = new HashSet<>();
		Set<Integer> horizontal = new HashSet<>();
		Set<Integer> vertical = new HashSet<>();
		ArrayList<Integer> allotment = new ArrayList<>();
		for( int iterate = 0; iterate < size; iterate++)
			norm.add(iterate+1);

		for( int row = 0; row < size; row++)
		{
			for( int col = 0; col < size; col++)
			{
				horizontal.clear();
				vertical.clear();
				allotment.clear();

				for(int hor = 0; hor < size; hor++)
					if(hor!=col)
						horizontal.add(answer.get(row).get(hor));

				for(int ver = 0; ver < size; ver++)
					if(ver!=row)
						vertical.add(answer.get(ver).get(col));

				allotment.addAll(Sets.intersection(Sets.difference(norm, horizontal),Sets.difference(norm, vertical)));

				if(allotment.size() == 0)
				{
					col = -1;
					for( int swipe = 0; swipe < size; swipe++)
						answer.get(row).set(swipe, 0);
					continue;
				}

				random = new Random().nextInt(allotment.size());

				answer.get(row).set(col, allotment.get(random));
			}	
		}
	}

	public void mathematize()
	{
		cage = new ArrayList<ArrayList<Point>>();
		ArrayList<Point> option = new ArrayList<Point>();

		for( int row = 0; row < size; row++)
		{
			for( int col = 0; col < size; col++)
			{
				option.add(new Point(row,col));
			}
		}

		int amount = (int)Math.pow(size,2);
		int oneCage, twoCage, threeCage, level;
		int max;

		if( size <= 2 )
		{
			oneCage = 2;
			twoCage = 0;
			threeCage = 1;
		}
		else
		{
			level = size - 2;
			max = (2*level)+1; 

			if( size%2 == 2 )
				oneCage = 1;
			else
				oneCage = 2;

			if( difficulty == "hard" || difficulty == "medium" )
				threeCage = max;
			else 
				if( size%2 == 2 )
					threeCage = max/2;
				else
					threeCage = max/3;
				

			twoCage = (amount - oneCage - (threeCage*3))/2;
		}

		for( int iterate = 0; iterate < threeCage; iterate++)
		{
			ArrayList<Point> three;
			int[] random = new int[3];
			do
			{
				three = new ArrayList<Point>();
				random[0] = new Random().nextInt(option.size());
				three.add(option.get(random[0]));
				for( int i = 1; i < 3; i++)
				{
					random[i] = new Random().nextInt(option.size());
					Point p = option.get(random[i]);

					int choice = new Random().nextInt(three.size());
					if( i == 2 )
					{
						int other = choice == 1? 0:1;
						if(p.isNextTo(three.get(choice)) && !p.equals(three.get(other)))
						{
							three.add(p);
						}
						else
						{
							i--;
						}
					}
					else
					{
						if(p.isNextTo(three.get(choice)))
						{
							three.add(p);
						}
						else
						{
							i--;
						}
					}
				}
			}
			while( odds(option, three) > oneCage);
			cage.add(three);
			option.removeAll(three);

		}

		int odds = 0;
		if(odds(option) > 0 )
		{
			ArrayList<ArrayList<Point>> oddCages =  linker(option);
			for(int iterate = 0; iterate < oddCages.size(); iterate++)
			{
				if( oddCages.get(iterate).size() % 2 == 0)
				{
					oddCages.remove(iterate);
					iterate--;
				}
			}

			odds = oddCages.size();

			for(int iterate = 0; iterate < odds; iterate++)
			{
				ArrayList<Point> one;
				int random;
				do
				{
					one = new ArrayList<Point>(1);
					random = new Random().nextInt(oddCages.get(iterate).size());
					one.add(oddCages.get(iterate).get(random));
				}
				while(odds(oddCages.get(iterate),one)> 0);
				cage.add(one);
				option.removeAll(one);
			}
		}

		if( odds < oneCage )
		{
			ArrayList<ArrayList<Point>> evenCages =  linker(option);
			
			int index = new Random().nextInt(evenCages.size());
			for(int iterate= 0; iterate < (oneCage-odds); iterate++ )
			{
				ArrayList<Point> one;
				int random;
				do
				{
					one = new ArrayList<Point>(1);
					random = new Random().nextInt(evenCages.get(index).size());
					one.add(evenCages.get(index).get(random));
				}
				while(odds(evenCages.get(index),one)> ((iterate+1)%2 == 1? 1:0));
				cage.add(one);
				option.removeAll(one);
				evenCages.get(index).remove(random);
			}
		}

		for( int iterate = 0; iterate < twoCage; iterate++ )
		{
			ArrayList<Point> two;
			int[] random = new int[2];
			do
			{
				two = new ArrayList<Point>();
				random[0] = new Random().nextInt(option.size());
				two.add(option.get(random[0]));


				do
				{
					random[1] = new Random().nextInt(option.size());
					Point p = option.get(random[1]);

					if(p.isNextTo(two.get(0)))
					{
						two.add(p);
						break;
					}
				}
				while(true);
				
				if(quadsNum(option) > 0)
				{
					ArrayList<ArrayList<Point>> quadCages =  quads(option);
					for(int i = 0, s = quadCages.size(); i < s; i++)
					{
						cage.add(quadCages.get(i));
						option.removeAll(quadCages.get(i));
					}
					two = new ArrayList<Point>();
				}
			}
			while( odds(option, two) > 0);
			
			if(two.size() != 0)
			{
				cage.add(two);
				option.removeAll(two);
			}
			
			if(option.size() == 0)
				break;
		}

		operator = new ArrayList<String>();
		target = new ArrayList<Integer>();
		topLeft = new ArrayList<Point>();
		
		String[] ops = {"+","x","-","\u00F7"};
		int op;
		for( int row = 0, stopRow = cage.size(); row < stopRow; row++)
		{
			if( cage.get(row).size() == 1)
			{
				operator.add("");
				Point p = cage.get(row).get(0);
				target.add(answer.get(p.getX()).get(p.getY()));
				continue;
			}
			else if(cage.get(row).size() == 2)
			{
				op = new Random().nextInt(ops.length);
			}
			else
			{
				op = new Random().nextInt(2);
			}
			operator.add(ops[op]);
			
			if( op <= 1 )
			{
				if( op == 0)
					target.add(0);
				else
					target.add(1);
				for( int col = 0, stopCol = cage.get(row).size(); col < stopCol; col++)
				{
					Point p = cage.get(row).get(col);
					
					if( op == 0)
						target.set(row,target.get(row)+answer.get(p.getX()).get(p.getY()));
					else
						target.set(row,target.get(row)*answer.get(p.getX()).get(p.getY()));
				}
			}
			else
			{
				Point p1 = cage.get(row).get(0);
				int num1 = answer.get(p1.getY()).get(p1.getX());
				
				Point p2 = cage.get(row).get(1);
				int num2 = answer.get(p2.getY()).get(p2.getX());
				
				if(num1 < num2)
				{
					int tmp = num1;
					num1 = num2;
					num2 = tmp;
				}
				
				if(op == 2)
				{
					target.add(num1 - num2);
				}
				else
				{
					double div1 = (double)num1/num2;
					if( div1 == (int)div1 )
						target.add((int)div1);
					else
					{
						operator.set(row, ops[1]);
						target.add(num1*num2);
					}
				}
			}
		}
		
		for( int row = 0, stopRow = cage.size(); row < stopRow; row++)
		{
			Point topLeftMost = cage.get(row).get(0);
			for( int col = 0, stopCol = cage.get(row).size(); col < stopCol; col++)
			{
				if( Point.distanceFromOrigin(topLeftMost) > Point.distanceFromOrigin(cage.get(row).get(col)) )
					topLeftMost = cage.get(row).get(col);
			}
			topLeft.add(topLeftMost);
		}
		System.out.println("Herro Harray!");
	}

	private int odds(ArrayList<Point> tmp, ArrayList<Point> container )
	{
		ArrayList<Point> option = new ArrayList<Point>();
		for( int iterate = 0, stop = tmp.size(); iterate < stop; iterate++ )
			option.add(new Point(tmp.get(iterate)));

		option.removeAll(container);

		return odds(option);
	}

	private int quadsNum(ArrayList<Point> option)
	{
		ArrayList<ArrayList<Point>> quadCages =  quads(option);
		
		return quadCages.size();
	}
	
	private ArrayList<ArrayList<Point>> quads(ArrayList<Point> option)
	{
		ArrayList<ArrayList<Point>> quadCages =  linker(option);
		
		for(int iterate = 0; iterate < quadCages.size(); iterate++)
		{
			if( quadCages.get(iterate).size() != 4)
			{
				quadCages.remove(iterate);
				iterate--;
			}
		}
		
		for(int iterate = 0; iterate < quadCages.size(); iterate++)
		{
			ArrayList<Point> points = quadCages.get(iterate);
			
			HashSet<Integer> x = new HashSet<>(4);
			HashSet<Integer> y = new HashSet<>(4);
			
			int xDup = 0;
			int yDup = 0;
			
			for(int i = 0; i < 4; i++)
			{
				if(!x.add(points.get(i).getX()))
					xDup++;
				if(!y.add(points.get(i).getY()))
					yDup++;
			}
			
			if(!( (xDup == 2 && yDup == 1) || (xDup == 1 && yDup == 2) ))
				quadCages.remove(iterate);
		}
		
		return quadCages;
	}
	
	private int odds(ArrayList<Point> option)
	{
		ArrayList<ArrayList<Point>> oddCages =  linker(option);

		for(int iterate = 0; iterate < oddCages.size(); iterate++)
		{
			if( oddCages.get(iterate).size() % 2 == 0)
			{
				oddCages.remove(iterate);
				iterate--;
			}
		}

		return oddCages.size();
	}

	private ArrayList<ArrayList<Point>> linker( ArrayList<Point> input )
	{
		ArrayList<ArrayList<Point>> links = new ArrayList<ArrayList<Point>>(); 

		ArrayList<Point> option = new ArrayList<Point>(input.size());
		for(int iterate = 0, stop = input.size(); iterate < stop; iterate++)
			option.add(input.get(iterate));

		ArrayList<Point> chain = new ArrayList<Point>();
		ArrayList<Point> points = new ArrayList<Point>();
		ArrayList<Point> steps = new ArrayList<Point>();
		while(option.size() > 0)
		{
			Point now = option.get(0);
			steps.add(now);
			while(steps.size() > 0)
			{
				now = steps.get(0);
				steps.remove(0);
				points = new ArrayList<Point>(4);
				points.add(now.north());
				points.add(now.east());
				points.add(now.west());
				points.add(now.south());

				chain.add(now);
				option = Sets.remains(option, chain);
				for(int iterate = 0; iterate < 4; iterate++)
				{
					if( option.contains(points.get(iterate)) && !steps.contains(points.get(iterate)))
						steps.add(points.get(iterate));
				}
			}
			links.add(chain);
			chain = new ArrayList<Point>();
		}
		

		return links;
	}

}



