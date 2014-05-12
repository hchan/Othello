package org.apache.hchan;
import java.awt.image.*;
import java.awt.*;
import java.applet.*;

import org.apache.log4j.Logger;
/* Othello
 by Henry Chan
 May/96
*/



public class Othello extends Applet {
  Logger log = Logger.getLogger(Othello.class);
  MediaTracker tracker;
  static OthelloButton arry[][];
  Panel board;
  static Image img1, img2, img3, img4;
  static int UserParameterLevelofRecursion;
  
  public void init(){
	  log.info("in init");
	  setSize(400, 400);
    int i,j;
    UserParameterLevelofRecursion = 0;
    try {
    	UserParameterLevelofRecursion = Integer.valueOf(getParameter("iteration")).intValue();
    } catch (Exception e) {
    	UserParameterLevelofRecursion = 4;
    }
    setLayout(new GridLayout(1,1));
    arry = new OthelloButton[10][10];
    tracker = new MediaTracker(this);
    /*
    img1 = getImage(getDocumentBase(), "box1.gif");
    img2 = getImage(getDocumentBase(), "box2.gif");
    img3 = getImage(getDocumentBase(), "box3.gif");
    img4 = getImage(getDocumentBase(), "box4.gif");
  */
    
    img1 = getImage(Othello.class.getResource("box1.gif"));
    img2 = getImage(Othello.class.getResource("box2.gif"));
    img3 = getImage(Othello.class.getResource("box3.gif"));
    img4 = getImage(Othello.class.getResource("box4.gif"));
    
    tracker.addImage(img1, 0);
    tracker.addImage(img2, 0);
    tracker.addImage(img3, 0);
    tracker.addImage(img4, 0);
    try {
      tracker.waitForID(0);
    }
    catch  (InterruptedException e) {
    	log.warn("", e);
    }

    board = new Panel();
    //board.setSize(400, 400);
    board.setLayout(new GridLayout(0,8,1,0)); 

    for (j = 0; j < 10; j++) {
      for (i = 0; i < 10; i++) {
	if ((i == 4 && j == 4) || (i == 5 && j == 5))
	  arry[i][j] = new OthelloButton(img3, img2, img3, 1, i, j);
	else if  ((i == 4 && j == 5) || (i == 5 && j == 4))
	  arry[i][j] = new OthelloButton(img4, img2, img3, -1, i, j);
	else
	  arry[i][j] = new OthelloButton(img1, img2, img3, 0, i, j);
	if (!(i == 0 || i == 9 || j == 0 || j == 9))
	  board.add(arry[i][j]);
      }
    }  
    add(board);
    UpdateCan_Eats_and_any_moves_left(1);
  }

  public static boolean UpdateCan_Eats_and_any_moves_left(int who_is_moving)
    {
      int i,j;
      boolean returnval = false;
      for (j = 1; j < 9; j++)
	{
	  for (i = 1; i < 9; i++) {
	    if (N(i,j,who_is_moving) > 0 ||
		NE(i,j,who_is_moving) > 0 ||
		E(i,j,who_is_moving) > 0 ||
		SE(i,j,who_is_moving) > 0 ||
		S(i,j,who_is_moving) > 0 ||
		SW(i,j,who_is_moving) > 0 ||
		W(i,j,who_is_moving) > 0 ||
		NW(i,j,who_is_moving) > 0) {
	      arry[i][j].can_eat = true;
	      returnval = true;
	    }
	    else
	      arry[i][j].can_eat = false;
	  }
	}
      return returnval;
    }

  public boolean keyUp(Event evt, int key)
    {
      if (key == 97 || key == 65)
	{
	  System.out.println("You just pushed a or A");
	  ShowAsciiBoard();
	}
      if (key == 98 || key == 66)
	{
	  System.out.println("You just pushed b or B");
	  ShowValidMoves();
	}
      if (key == 99 || key == 67)
	{
	  System.out.println("You just pushed c or C");
	  System.out.println(CurrentScore());
	}
      else
	System.out.println(key);
      return true;
    }

  public static void ShowAsciiBoard()
    {
      int i,j;
      for (j = 1; j < 9; j++)
	{
	  for (i = 1; i < 9; i++) {
	    System.out.print(arry[i][j].occupied);
	    System.out.print('\t');
	  }
	  System.out.println();
	}
    }

  public void ShowValidMoves()
    {
      int i,j;
      for (j = 1; j < 9; j++)
	{
	  for (i = 1; i < 9; i++) {
	    if (arry[i][j].ValidMove())
	      System.out.print(N(i,j,1) +
			       NE(i,j,1) +
			       E(i,j,1) +
			       SE(i,j,1) +
			       S(i,j,1) +
			       SW(i,j,1) +
			       W(i,j,1) +
			       NW(i,j,1));
	    else
	      System.out.print(0);
	    System.out.print('\t');
	  }
	  System.out.println();
	}
    }

  public static int CurrentScore()
    {
      int i,j;
      int returnval = 0;
       for (j = 1; j < 9; j++)
	{
	  for (i = 1; i < 9; i++) {
	    if (arry[i][j].occupied == 1)
	      returnval++;
	    if (arry[i][j].occupied == -1)
	      returnval--;
	  } 
	}
      return returnval;
    }
  

  // returns number of pieces ate along north
  public static int N(int i, int j, int who_is_moving)
    {
      int temp = j;
      int eat = 0;
      if (arry[i][j].occupied != 0) return 0;

      while (temp != 1) {
	if (arry[i][temp-1].occupied == (-1 * who_is_moving)) {
	  temp--;
	  eat++;
	}
	else 
	  if (arry[i][temp-1].occupied == (1 * who_is_moving)) 
	    return eat;
	  else
	    return 0;
      }
      if (arry[i][1].occupied == (1 * who_is_moving))
	return eat;
      else
	return 0;
    }

  // returns number of pieces ate along northeast
  public static int NE(int i, int j, int who_is_moving) {
    int limitx = i;
    int limity = j;
    int eat = 0;
    if (arry[i][j].occupied != 0) return 0;

    while (limitx != 8 || limity != 1) {
      if (arry[limitx+1][limity-1].occupied == (-1 * who_is_moving)) {
	limitx++;
	limity--;
	eat++;
      }
      else 
	if (arry[limitx+1][limity-1].occupied == who_is_moving)
	  return eat;
	else
	  return 0;
    }
    if (arry[limitx][limity].occupied == who_is_moving)
      return eat;
    else
      return 0;
    }

  // returns number of pieces ate along east
  public static int E(int i, int j, int who_is_moving)
    {
      int temp = i;
      int eat = 0;
      if (arry[i][j].occupied != 0) return 0;

      while (temp != 8) {
	if (arry[temp+1][j].occupied == (-1 * who_is_moving)) {
	  temp++;
	  eat++;
	}
	else 
	  if (arry[temp+1][j].occupied == (1 * who_is_moving)) 
	    return eat;
	  else
	    return 0;
      }
      if (arry[8][j].occupied == (1 * who_is_moving))
	return eat;
      else
	return 0;
    }


  // returns number of pieces ate along southeast
  public static int SE(int i, int j, int who_is_moving) {
    int limitx = i;
    int limity = j;
    int eat = 0;
    if (arry[i][j].occupied != 0) return 0;

    while (limitx != 8 || limity != 8) {
      if (arry[limitx+1][limity+1].occupied == (-1 * who_is_moving)) {
	limitx++;
	limity++;
	eat++;
      }
      else 
	if (arry[limitx+1][limity+1].occupied == who_is_moving)
	  return eat;
	else
	  return 0;
    }
    if (arry[limitx][limity].occupied == who_is_moving)
      return eat;
    else
      return 0;
    }

  // returns number of pieces ate along south
  public static int S(int i, int j, int who_is_moving)
    {
      int temp = j;
      int eat = 0;
      if (arry[i][j].occupied != 0) return 0;

      while (temp != 8) {
	if (arry[i][temp+1].occupied == (-1 * who_is_moving)) {
	  temp++;
	  eat++;
	}
	else 
	  if (arry[i][temp+1].occupied == (1 * who_is_moving)) 
	    return eat;
	  else
	    return 0;
      }
      if (arry[i][8].occupied == (1 * who_is_moving))
	return eat;
      else
	return 0;
    }

  // returns number of pieces ate along southwest
  public static int SW(int i, int j, int who_is_moving) {
    int limitx = i;
    int limity = j;
    int eat = 0;
    if (arry[i][j].occupied != 0) return 0;

    while (limitx != 1 || limity != 8) {
      if (arry[limitx-1][limity+1].occupied == (-1 * who_is_moving)) {
	limitx--;
	limity++;
	eat++;
      }
      else 
	if (arry[limitx-1][limity+1].occupied == who_is_moving)
	  return eat;
	else
	  return 0;
    }
    if (arry[limitx][limity].occupied == who_is_moving)
      return eat;
    else
      return 0;
    }

  // returns number of pieces ate along west
  public static int W(int i, int j, int who_is_moving)
    {
      int temp = i;
      int eat = 0;
      if (arry[i][j].occupied != 0) return 0;

      while (temp != 1) {
	if (arry[temp-1][j].occupied == (-1 * who_is_moving)) {
	  temp--;
	  eat++;
	}
	else 
	  if (arry[temp-1][j].occupied == (1 * who_is_moving))
	    return eat;
	  else
	    return 0;
      }
      if (arry[1][j].occupied == (1 * who_is_moving))
	return eat;
      else
	return 0;
    }

  // returns number of pieces ate along northwest
  public static int NW(int i, int j, int who_is_moving) {
    int limitx = i;
    int limity = j;
    int eat = 0;
    if (arry[i][j].occupied != 0) return 0;

    while (limitx != 1 || limity != 1) {
      if (arry[limitx-1][limity-1].occupied == (-1 * who_is_moving)) {
	limitx--;
	limity--;
	eat++;
      }
      else 
	if (arry[limitx-1][limity-1].occupied == who_is_moving)
	  return eat;
	else
	  return 0;
    }
    if (arry[limitx][limity].occupied == who_is_moving)
      return eat;
    else
      return 0;
    }


  public static void Flip(int i, int j, int who_is_moving)
    {
      int limit, flips;
      limit = N(i,j, who_is_moving);
      for (flips = 0; flips < limit; flips++) {
	if (who_is_moving == 1) //player
	  arry[i][j-1-flips].currimg = img3;
	else
	  arry[i][j-1-flips].currimg = img4;
	arry[i][j-1-flips].repaint();
	arry[i][j-1-flips].occupied = who_is_moving;
      }
      limit = NE(i,j, who_is_moving);
      for (flips = 0; flips < limit; flips++) {
	if (who_is_moving == 1) //player
	  arry[i+1+flips][j-1-flips].currimg = img3;
	else
	  arry[i+1+flips][j-1-flips].currimg = img4;
	arry[i+1+flips][j-1-flips].repaint();
	arry[i+1+flips][j-1-flips].occupied = who_is_moving;
      }
      limit = E(i,j, who_is_moving);
      for (flips = 0; flips < limit; flips++) {
	if (who_is_moving == 1) //player
	  arry[i+1+flips][j].currimg = img3;
	else
	  arry[i+1+flips][j].currimg = img4;
	arry[i+1+flips][j].repaint();
	arry[i+1+flips][j].occupied = who_is_moving;
      }
      limit = SE(i,j, who_is_moving);
       for (flips = 0; flips < limit; flips++) {
	 if (who_is_moving == 1) //player
	   arry[i+1+flips][j+1+flips].currimg = img3;
	 else
	   arry[i+1+flips][j+1+flips].currimg = img4;
	 arry[i+1+flips][j+1+flips].repaint();
	 arry[i+1+flips][j+1+flips].occupied = who_is_moving;
       }
      limit = S(i,j, who_is_moving);
      for (flips = 0; flips < limit; flips++) {
	if (who_is_moving == 1) //player
	  arry[i][j+1+flips].currimg = img3;
	else
	  arry[i][j+1+flips].currimg = img4;
	arry[i][j+1+flips].repaint();
	arry[i][j+1+flips].occupied = who_is_moving;
      }
      limit = SW(i,j, who_is_moving);
      for (flips = 0; flips < limit; flips++) {
	if (who_is_moving == 1) //player
	  arry[i-1-flips][j+1+flips].currimg = img3;
	else
	  arry[i-1-flips][j+1+flips].currimg = img4;
	arry[i-1-flips][j+1+flips].repaint();
	arry[i-1-flips][j+1+flips].occupied = who_is_moving;
      }
      limit = W(i,j, who_is_moving);
      for (flips = 0; flips < limit; flips++) {
	if (who_is_moving == 1) //player
	  arry[i-1-flips][j].currimg = img3;
	else
	  arry[i-1-flips][j].currimg = img4;
	arry[i-1-flips][j].repaint();
	arry[i-1-flips][j].occupied = who_is_moving;
      }
      limit = NW(i,j, who_is_moving);
      for (flips = 0; flips < limit; flips++) {
	if (who_is_moving == 1) //player
	  arry[i-1-flips][j-1-flips].currimg = img3;
	else
	  arry[i-1-flips][j-1-flips].currimg = img4;
	arry[i-1-flips][j-1-flips].repaint();
	arry[i-1-flips][j-1-flips].occupied = who_is_moving;
      }
    }
  
  public static void ComputerMove()
    {
      boolean found_valid_move = false;
      int i,j, a,b, update_intarry;
      int max_now = 0;
      int position_value, position_coordinate_x = 0, position_coordinate_y = 0;
      int intarry[][], intarry_orig[][];
      intarry = new int[10][10];
      intarry_orig = new int[10][10];
      int pos_val_after_recursion;
      int position_valueN, position_valueNE, position_valueE, 
      position_valueSE, position_valueS, position_valueSW, position_valueW, 
      position_valueNW;
      for (b = 1; b < 9; b++) {
	for (a = 1; a < 9; a++) {
	  intarry_orig[a][b] = arry[a][b].occupied;
	  intarry[a][b]      = intarry_orig[a][b];
	}
      }

      for (j = 1; j < 9; j++) {
	for (i = 1; i < 9; i++) {
	  position_valueN = N(i,j,-1);
	  if (position_valueN > 0) {
	    for (update_intarry = 0; update_intarry < position_valueN;
		 update_intarry++) 
	      intarry[i][j-1-update_intarry] = -1;
	    intarry[i][j] = -1;
	  }
	  
	  position_valueNE = NE(i,j,-1);
	  if (position_valueNE > 0) {
	    for (update_intarry = 0; update_intarry < position_valueNE;
		 update_intarry++) 
	      intarry[i+1+update_intarry][j-1-update_intarry] = -1;
	    intarry[i][j] = -1;
	  }
	  
	  position_valueE = E(i,j,-1);
	  if (position_valueE > 0) {
	    for (update_intarry = 0; update_intarry < position_valueE;
		 update_intarry++) 
	      intarry[i+1+update_intarry][j] = -1;
	    intarry[i][j] = -1;
	  }
	    
	  position_valueSE = SE(i,j,-1);
	  if (position_valueSE > 0) {
	    for (update_intarry = 0; update_intarry < position_valueSE;
		 update_intarry++) 
	      intarry[i+1+update_intarry][j+1+update_intarry] = -1;
	    intarry[i][j] = -1;
	  }
	     
	  position_valueS = S(i,j,-1);
	  if (position_valueS > 0) {
	    for (update_intarry = 0; update_intarry < position_valueS;
		 update_intarry++) 
	      intarry[i][j+1+update_intarry] = -1;
	    intarry[i][j] = -1;
	  }
	  
	  position_valueSW = SW(i,j,-1);
	  if (position_valueSW > 0) {
	    for (update_intarry = 0; update_intarry < position_valueSW;
		 update_intarry++) 
	      intarry[i-1-update_intarry][j+1+update_intarry] = -1;
	    intarry[i][j] = -1;
	  }
	  
	  position_valueW = W(i,j,-1);
	  if (position_valueW > 0) {
	    for (update_intarry = 0; update_intarry < position_valueW;
		 update_intarry++) 
	      intarry[i-1-update_intarry][j] = -1;
	    intarry[i][j] = -1;
	  }

	  position_valueNW = NW(i,j,-1);
	  if (position_valueNW > 0) {
	    for (update_intarry = 0; update_intarry < position_valueNW;
		 update_intarry++) 
	      intarry[i-1-update_intarry][j-1-update_intarry] = -1;
	    intarry[i][j] = -1;
	  }
	  
	  position_value = position_valueN + position_valueNE + 
	    position_valueE + position_valueSE + position_valueS + 
	      position_valueSW + position_valueW + position_valueNW;

	  if (position_value > 0) { // pos i,j is a possible move
	    /** corner bias **/
	    if (((i == 2) && (j == 2) && intarry[1][1] == 0) ||
		((i == 7) && (j == 2) && intarry[8][1] == 0) ||
		((i == 7) && (j == 7) && intarry[8][8] == 0) ||
		((i == 2) && (j == 7) && intarry[1][8] == 0))
	      position_value = position_value - 50;
	    if (((i == 1) && (j == 1)) ||
		((i == 8) && (j == 1)) ||
		((i == 8) && (j == 8)) ||
		((i == 1) && (j == 8)))
	      position_value = position_value + 100;
	    /** corner bias **/

	    pos_val_after_recursion = 
	      RecurseFindBestMove
		(intarry, -position_value, 1, UserParameterLevelofRecursion);
	    for (b = 1; b < 9; b++) {
	      for (a = 1; a < 9; a++) {
		intarry[a][b] = intarry_orig[a][b]; // reinit
	      }
	    }
	   
	    if (pos_val_after_recursion > max_now || !(found_valid_move)) {
	      position_coordinate_x = i;
	      position_coordinate_y = j;
	      max_now = pos_val_after_recursion;
	      found_valid_move = true;
	    }
	  }
	}
      }
      if (found_valid_move) { 
	Flip(position_coordinate_x, position_coordinate_y, -1);
	arry[position_coordinate_x][position_coordinate_y].occupied = -1;
	arry[position_coordinate_x][position_coordinate_y].currimg = img4;
	arry[position_coordinate_x][position_coordinate_y].repaint();
	if (UpdateCan_Eats_and_any_moves_left(1)) 
	  // restore eating power to player
	  return;
	else {
	  Pass(1);
	  return;
	}
      }
      else 
	Pass(-1);
    }

  public static int RecurseFindBestMove(int intarry_given[][], 
					int current_weight,
					int who_is_moving, 
					int iteration) {
    boolean found_valid_move = false;
    int i,j, a,b;
    int max_now = 0;
    int position_value, position_coordinate_x = 0, position_coordinate_y = 0;
    int intarry_orig[][], intarry[][], pos_val_after_recursion;
    int update_intarry;
    int position_valueN, position_valueNE, position_valueE, 
    position_valueSE, position_valueS, position_valueSW, position_valueW, 
    position_valueNW;
    intarry_orig = new int[10][10];
    intarry      = new int[10][10];
    
    for (b = 1; b < 9; b++)
      {
	for (a = 1; a < 9; a++) {
	  intarry_orig[a][b] = intarry_given[a][b];
	  intarry[a][b] = intarry_given[a][b];
	}
      }

    if (iteration == 1) {
      intarry_orig = null;
      intarry = null;
      intarry_given = null;
      return -current_weight; // finished!
    }

    for (j = 1; j < 9; j++) {
      for (i = 1; i < 9; i++) {
	position_valueN = Nint(i,j,who_is_moving,intarry);
	if (position_valueN > 0) {
	  for (update_intarry = 0; update_intarry < position_valueN;
		 update_intarry++) 
	      intarry[i][j-1-update_intarry] = who_is_moving;
	    intarry[i][j] = who_is_moving;
	}

	position_valueNE = NEint(i,j,who_is_moving,intarry);
	  if (position_valueNE > 0) {
	    for (update_intarry = 0; update_intarry < position_valueNE;
		 update_intarry++) 
	      intarry[i+1+update_intarry][j-1-update_intarry] = who_is_moving;
	    intarry[i][j] = who_is_moving;
	  }
	  
	  position_valueE = Eint(i,j,who_is_moving,intarry);
	  if (position_valueE > 0) {
	    for (update_intarry = 0; update_intarry < position_valueE;
		 update_intarry++) 
	      intarry[i+1+update_intarry][j] = who_is_moving;
	    intarry[i][j] = who_is_moving;
	  }
	    
	  position_valueSE = SEint(i,j,who_is_moving,intarry);
	  if (position_valueSE > 0) {
	    for (update_intarry = 0; update_intarry < position_valueSE;
		 update_intarry++) 
	      intarry[i+1+update_intarry][j+1+update_intarry] = who_is_moving;
	    intarry[i][j] = who_is_moving;
	  }
	     
	  position_valueS = Sint(i,j,who_is_moving,intarry);
	  if (position_valueS > 0) {
	    for (update_intarry = 0; update_intarry < position_valueS;
		 update_intarry++) 
	      intarry[i][j+1+update_intarry] = who_is_moving;
	    intarry[i][j] = who_is_moving;
	  }
	  
	  position_valueSW = SWint(i,j,who_is_moving,intarry);
	  if (position_valueSW > 0) {
	    for (update_intarry = 0; update_intarry < position_valueSW;
		 update_intarry++) 
	      intarry[i-1-update_intarry][j+1+update_intarry] = who_is_moving;
	    intarry[i][j] = who_is_moving;
	  }
	  
	  position_valueW = Wint(i,j,who_is_moving,intarry);
	  if (position_valueW > 0) {
	    for (update_intarry = 0; update_intarry < position_valueW;
		 update_intarry++) 
	      intarry[i-1-update_intarry][j] = who_is_moving;
	    intarry[i][j] = who_is_moving;
	  }

	  position_valueNW = NWint(i,j,who_is_moving,intarry);
	  if (position_valueNW > 0) {
	    for (update_intarry = 0; update_intarry < position_valueNW;
		 update_intarry++) 
	      intarry[i-1-update_intarry][j-1-update_intarry] = who_is_moving;
	    intarry[i][j] = who_is_moving;
	  }

	position_value = position_valueN + position_valueNE + 
	  position_valueE + position_valueSE + position_valueS + 
	    position_valueSW + position_valueW + position_valueNW;
	 
	if (position_value > 0) { // pos i,j is a possible move
	  /** corner bias **/
	  if (((i == 2) && (j == 2) && intarry[1][1] == 0) ||
	      ((i == 7) && (j == 2) && intarry[8][1] == 0) ||
	      ((i == 7) && (j == 7) && intarry[8][8] == 0) ||
	      ((i == 2) && (j == 7) && intarry[1][8] == 0))
	      position_value = position_value - 50;
	  if (((i == 1) && (j == 1)) ||
	      ((i == 8) && (j == 1)) ||
	      ((i == 8) && (j == 8)) ||
	      ((i == 1) && (j == 8)))
	    position_value = position_value + 100;
	  /** corner bias **/
	  pos_val_after_recursion = RecurseFindBestMove
	    (intarry, 
	     -current_weight + position_value * -1 * who_is_moving,
	     who_is_moving*-1, iteration-1);
	  for (b = 1; b < 9; b++) {
	    for (a = 1; a < 9; a++) {
	      intarry[a][b] = intarry_orig[a][b];
	    }
	  }
	  if (pos_val_after_recursion > max_now || !(found_valid_move)) {
	    max_now = pos_val_after_recursion;
	    found_valid_move = true;
	  }
	}
      }
    }
    intarry_orig = null;
    intarry = null;
    intarry_given = null;
    return -max_now;
  }



  // returns number of pieces ate along north
  public static int Nint(int i, int j, int who_is_moving, int intarry[][])
    {
      int temp = j;
      int eat = 0;
      if (intarry[i][j] != 0) return 0;

      while (temp != 1) {
	if (intarry[i][temp-1] == (-1 * who_is_moving)) {
	  temp--;
	  eat++;
	}
	else 
	  if (intarry[i][temp-1] == (1 * who_is_moving)) 
	    return eat;
	  else
	    return 0;
      }
      if (intarry[i][1] == (1 * who_is_moving))
	return eat;
      else
	return 0;
    }

  // returns number of pieces ate along northeast
  public static int NEint(int i, int j, int who_is_moving, int intarry[][]) {
    int limitx = i;
    int limity = j;
    int eat = 0;
    if (intarry[i][j] != 0) return 0;

    while (limitx != 8 || limity != 1) {
      if (intarry[limitx+1][limity-1] == (-1 * who_is_moving)) {
	limitx++;
	limity--;
	eat++;
      }
      else 
	if (intarry[limitx+1][limity-1] == who_is_moving)
	  return eat;
	else
	  return 0;
    }
    if (intarry[limitx][limity] == who_is_moving)
      return eat;
    else
      return 0;
    }

  // returns number of pieces ate along east
  public static int Eint(int i, int j, int who_is_moving, int intarry[][])
    {
      int temp = i;
      int eat = 0;
      if (intarry[i][j] != 0) return 0;

      while (temp != 8) {
	if (intarry[temp+1][j] == (-1 * who_is_moving)) {
	  temp++;
	  eat++;
	}
	else 
	  if (intarry[temp+1][j] == (1 * who_is_moving)) 
	    return eat;
	  else
	    return 0;
      }
      if (intarry[8][j] == (1 * who_is_moving))
	return eat;
      else
	return 0;
    }


  // returns number of pieces ate along southeast
  public static int SEint(int i, int j, int who_is_moving, int intarry[][]) {
    int limitx = i;
    int limity = j;
    int eat = 0;
    if (intarry[i][j] != 0) return 0;

    while (limitx != 8 || limity != 8) {
      if (intarry[limitx+1][limity+1] == (-1 * who_is_moving)) {
	limitx++;
	limity++;
	eat++;
      }
      else 
	if (intarry[limitx+1][limity+1] == who_is_moving)
	  return eat;
	else
	  return 0;
    }
    if (intarry[limitx][limity] == who_is_moving)
      return eat;
    else
      return 0;
    }

  // returns number of pieces ate along south
  public static int Sint(int i, int j, int who_is_moving, int intarry[][])
    {
      int temp = j;
      int eat = 0;
      if (intarry[i][j] != 0) return 0;

      while (temp != 8) {
	if (intarry[i][temp+1] == (-1 * who_is_moving)) {
	  temp++;
	  eat++;
	}
	else 
	  if (intarry[i][temp+1] == (1 * who_is_moving)) 
	    return eat;
	  else
	    return 0;
      }
      if (intarry[i][8] == (1 * who_is_moving))
	return eat;
      else
	return 0;
    }

  // returns number of pieces ate along southwest
  public static int SWint(int i, int j, int who_is_moving, int intarry[][]) {
    int limitx = i;
    int limity = j;
    int eat = 0;
    if (intarry[i][j] != 0) return 0;

    while (limitx != 1 || limity != 8) {
      if (intarry[limitx-1][limity+1] == (-1 * who_is_moving)) {
	limitx--;
	limity++;
	eat++;
      }
      else 
	if (intarry[limitx-1][limity+1] == who_is_moving)
	  return eat;
	else
	  return 0;
    }
    if (intarry[limitx][limity] == who_is_moving)
      return eat;
    else
      return 0;
    }

  // returns number of pieces ate along west
  public static int Wint(int i, int j, int who_is_moving, int intarry[][])
    {
      int temp = i;
      int eat = 0;
      if (intarry[i][j] != 0) return 0;

      while (temp != 1) {
	if (intarry[temp-1][j] == (-1 * who_is_moving)) {
	  temp--;
	  eat++;
	}
	else 
	  if (intarry[temp-1][j] == (1 * who_is_moving))
	    return eat;
	  else
	    return 0;
      }
      if (intarry[1][j] == (1 * who_is_moving))
	return eat;
      else
	return 0;
    }

  // returns number of pieces ate along northwest
  public static int NWint(int i, int j, int who_is_moving, int intarry[][]) 
    {
      int limitx = i;
      int limity = j;
      int eat = 0;
      if (intarry[i][j] != 0) return 0;
      
      while (limitx != 1 || limity != 1) {
	if (intarry[limitx-1][limity-1] == (-1 * who_is_moving)) {
	  limitx--;
	  limity--;
	  eat++;
	}
	else 
	  if (intarry[limitx-1][limity-1] == who_is_moving)
	    return eat;
	  else
	    return 0;
      }
      if (intarry[limitx][limity] == who_is_moving)
	return eat;
      else
	return 0;
      }



  public static void GameOver()
    {
      System.out.println("Game Over");
      int i,j;
      int current_score = CurrentScore();
      if (current_score == 0)
	System.out.println("Tie Game");
      if (current_score > 0)
	System.out.println("You win");
      if (current_score == 0)
	 System.out.println("You lose");
      for (j = 1; j < 9; j++) {
	for (i = 1; i < 9; i++) {
	  arry[i][j].disable();
	}
      }
    }

  public static void Pass(int who_is_moving)
    {
      if (UpdateCan_Eats_and_any_moves_left(who_is_moving * -1))
	{
	  System.out.println("Pass");
	  return;
	}
      else
	GameOver();
      }

}
