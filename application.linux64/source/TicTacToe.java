import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class TicTacToe extends PApplet {

// This code is far too clever, but it works, so I'm not going to mess with it.

final int FULL = 511;
int[] used = new int[9]; // getbit(used[z], w) is true if board z space w is occupied
int[] os = new int[9]; // getbit(used[z], w) is true if board z space w contains an O
int active = FULL; // getbit(active, z) is true if board z is active
boolean oturn = true; // oturn is true if it is O's turn
int won = 0;  // getbit(won, z) is true if somebody has won on board z
int owon = 0; // getbit(owon, z) is true if O has won on board z

// 3x3 indexing:
//
// 0 | 1 | 2
// 3 | 4 | 5
// 6 | 7 | 8

public void setup() {
  size(270, 270);
  textFont(loadFont("TimesNewRomanPSMT-48.vlw"), 24);
}

public void draw() {
  // z is a 3x3 index of boards
  for (int z = 0; z < 9; z++) {
    // board z is at coordinates (i, j)
    int i = z % 3;
    int j = z / 3;
    // w is a 3x3 index of spaces
    for (int w = 0; w < 9; w++) {
      // space w is at coordinates (u, v)
      int u = w % 3;
      int v = w / 3;
      if (getbit(owon, z)) {
        // color blue
        fill(0, 0, 128 + (getbit(active, z) ? 64 : 0));
      } else if (getbit(won, z)) {
        // color red
        fill(128 + (getbit(active, z) ? 64 : 0), 0, 0);
      } else {
        // color white
        fill(128 + (getbit(active, z) ? 127 : 0));
      }
      // draw space
      stroke(0, 0, 0);
      rect(i*90+u*30, j*90+v*30, i*90+(u+1)*30, j*90+(v+1)*30);
      noStroke();
      fill(getbit(used[z] ^ os[z], w) ? 255 : 0, 0, getbit(used[z] & os[z], w) ? 255 : 0);
      if(getbit(used[z], w)) {
        // draw character
        textAlign(CENTER);
        text(getbit(os[z], w) ? "O" : "X", i*90 + u*30 + 15, j*90 + v*30 + 25);
      }
    }
    noFill();
    stroke(0, 0, 0);
    strokeWeight(4);
    rect(i*90, j*90, (i+1)*90, (j+1)*90);
    strokeWeight(1);
  }
  if (active == 0) {
    fill(0, 0, 0, 128);
    rect(0, 0, 270, 270);
    fill(oturn ? 0 : 255, 0, oturn ? 255 : 0);
    text(oturn ? "O's win!" : "X's win!", 135, 135);
  }
}

public boolean getbit(int bits,int n) {
  // return true if nth bit of bits is set
  return (bits & (1 << n)) > 0;
}

public void mousePressed() {
  // calculate board clicked
  int i = mouseX / 90;
  int j = mouseY / 90;
  int z = i + j*3;
  
  // calculate space clicked
  int u = (mouseX - i*90) / 30;
  int v = (mouseY - j*90) / 30;
  int w = u + v*3;
  
  if (getbit(active, z) && !getbit(used[z], w)) { // move is valid
    // update board
    used[z] |= (1 << w);
    if (oturn)
      os[z] |= (1 << w);
    if (!getbit(won, z) && wins(used[z], os[z], w)) { // move results in a win
      won |= (1 << z);
      if (oturn)
        owon |= (1 << z);
      if(wins(won, owon, z))
        active = 0;
    }
    if (active > 0) {
      active = 1 << w;
      oturn = !oturn;
    }
    if (used[w] == FULL && active != 0)
      active = FULL;
  }
}

public boolean wins(int usedn,int osn,int m) {
  boolean olast = getbit(osn, m); // true if O made last move
  // returns true if the letter added at space m results in a win
  if(getbit(usedn, (m+3)%9) && getbit(osn, (m+3)%9) == olast &&
     getbit(usedn, (m+6)%9) && getbit(osn, (m+6)%9) == olast)
    return true; // vertical win
  if(getbit(usedn, (m/3)*3)   && getbit(osn, (m/3)*3)   == olast &&
     getbit(usedn, (m/3)*3+1) && getbit(osn, (m/3)*3+1) == olast &&
     getbit(usedn, (m/3)*3+2) && getbit(osn, (m/3)*3+2) == olast)
    return true; // horizontal win
  if(m%2 == 0) {
    if(getbit(usedn, 0) && getbit(osn, 0) == olast &&
       getbit(usedn, 4) && getbit(osn, 4) == olast &&
       getbit(usedn, 8) && getbit(osn, 8) == olast)
      return true; // left diagonal win
    if(getbit(usedn, 2) && getbit(osn, 2) == olast &&
       getbit(usedn, 4) && getbit(osn, 4) == olast &&
       getbit(usedn, 6) && getbit(osn, 6) == olast)
      return true; // right diagonal win
  }
  return false;
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "TicTacToe" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
