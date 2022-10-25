package edu.nyit.csci.cp2.kenken;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Sets 
{
	//Code utilized from http://www.java2s.com/Code/Java/Collections-Data-Structure/Setoperationsunionintersectiondifferencesymmetricdifferenceissubsetissuperset.htm
	public static <T> Set<T> union(Set<T> setA, Set<T> setB)
	{
		Set<T> tmp = new HashSet<T>(setA);
		tmp.addAll(setB);
		return tmp;
	}

	public static <T> Set<T> intersection(Set<T> setA, Set<T> setB)
	{
		Set<T> tmp = new HashSet<T>();
		for (T x : setA)
			if (setB.contains(x))
				tmp.add(x);
		return tmp;
	}

	public static <T> Set<T> difference(Set<T> setA, Set<T> setB)
	{
		Set<T> tmp = new HashSet<T>(setA);
		tmp.removeAll(setB);
		return tmp;
	}

	public static ArrayList<Point> remains(ArrayList<Point> setA, ArrayList<Point> setB)
	{
		ArrayList<Point> output = new ArrayList<Point>();
		for(int iterate = 0, stop = setA.size(); iterate < stop; iterate++)
		{
			if( setB.indexOf(setA.get(iterate)) == -1 )
			{
				output.add(setA.get(iterate));
			}
		}
		return output;
	}
	
	public static <T> Set<T> symDifference(Set<T> setA, Set<T> setB)
	{
		Set<T> tmpA;
		Set<T> tmpB;

		tmpA = union(setA, setB);
		tmpB = intersection(setA, setB);
		return difference(tmpA, tmpB);
	}

	public static <T> boolean isSubset(Set<T> setA, Set<T> setB) 
	{
		return setB.containsAll(setA);
	}

	public static <T> boolean isSuperset(Set<T> setA, Set<T> setB)
	{
		return setA.containsAll(setB);
	}
}
