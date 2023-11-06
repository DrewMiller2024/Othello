/*
 * Othello class project competition
 * Copyright 2017 Roger Jaffe
 * All rights reserved
 */
package com.mrjaffesclass.othello;

/**
 * Othello class project competition
 * @author Roger Jaffe
 * @version 1.0
 */
public class Othello {

 
 public static void simulation(int repeatNum, Player player1, Player player2) throws InterruptedException {
		int repeats = repeatNum;
		int[] results = new int[repeats];
		String p1Name = player1.getClass().getSimpleName();
		String p2Name = player2.getClass().getSimpleName();
		for(int i = 0; i < repeats; i++) {
			System.out.println("Game: " + (i+1));
			//ControllerNoPrint c = new ControllerNoPrint( 
			Controller c = new Controller(
			//ControllerRandomTesting c = new ControllerRandomTesting(
				player1, player2
			);
			c.displayMatchup();
			int result = c.run();
			results[i] = result;
		}
		int blackWins = 0;
		int whiteWins = 0;
		int ties = 0;
		for(int i : results) {
			if(i==1) blackWins++;
			if(i==2) whiteWins++;
			if(i==3) ties++;
		}
		System.out.printf("In %d games, %s won: %d, %s won: %d, and %d were ties\n", repeats,p1Name, blackWins,p2Name, whiteWins, ties);
	}
	public static void main(String[] args) throws InterruptedException {
		int repetitions = 1;
		simulation(repetitions, new RyanPlayer(Constants.BLACK), new MillerDrew(Constants.WHITE));
		//simulation(repetitions, new RyanPlayerEvolved(Constants.BLACK), new TestPlayer(Constants.WHITE));	
		System.exit(0);
	}
  
}
