import java.awt.Canvas;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;


public class OthelloButton extends Canvas{

  Image box1, box2, box3, currimg;
  int occupied, coordinate_x, coordinate_y;
  boolean can_eat;
  // player = 1     - the light colored piece
  // computer = -1  - the dark  colored piece

  public OthelloButton
    (Image img1, Image img2, Image img3, int piece, int i, int j)
    {
      coordinate_x = i;
      coordinate_y = j;
      occupied = piece;
      box1 = img1;
      box2 = img2;
      box3 = img3;
      currimg = box1;
      can_eat = false;
      resize(img1.getWidth(this), img1.getHeight(this));
    }
  
  public void paint(Graphics graphics) {
    graphics.drawImage(currimg, 0, 0, this);
  }

  public boolean ValidMove()
    {
      if (can_eat)
	return true;
      else
	return false;
    }
  
  // returns if something has been eated
  public boolean mouseDown(Event e, int x, int y)
    {
      if (ValidMove()) {
	  currimg = box2;
	  repaint();
	  return true;
	}
      else
	return false;

    }

  // returns if something has been eated
  public boolean mouseUp(Event e, int x, int y)
    {
      if (ValidMove()) {
	if((x >= 0) && (y >= 0) && (x < size().width) && (y < size().height)) {	
	  Othello.Flip(coordinate_x,coordinate_y, 1);
	  // the 2nd argument in Flip is who_is_moving
	  // 1 is me  -1 is computer
	  occupied = 1;
	  currimg = box3;
	  repaint();
	  Othello.ComputerMove();
	}
	else
	  {
	    currimg = box1;
	    repaint();
	  }
	return true;
      }
      else
	return false;
    }
  
  public boolean action(Event e, Object what)
    {
      System.out.println(e);
      System.out.println(what);
      return true;
    }
}