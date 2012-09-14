final int FULL = 511;
int[] used = new int[9];
int[] os = new int[9];
int active = FULL;
int oturn = 1;
int i,j,u,v,w,z;
int won = 0;
int owon = 0;

void setup() {
  size(270,270);
  textFont(loadFont("TimesNewRomanPSMT-48.vlw"),24);
}

void draw() {
  for(z=0;z<9;z++) {
    i = z%3;
    j = z/3;
    for(w=0;w<9;w++) {
      u = w%3;
      v = w/3;
      if(getbit(owon,z)==1) {
        fill(0,0,128+getbit(active,z)*64);
      }else if(getbit(won,z)==1) {
        fill(128+getbit(active,z)*64,0,0);
      }else{
        fill(128+getbit(active,z)*127);
      }
      stroke(0,0,0);
      rect(i*90+u*30,j*90+v*30,i*90+(u+1)*30,j*90+(v+1)*30);
      noStroke();
      fill(getbit(used[z]^os[z],w)*255,0,getbit(used[z]&os[z],w)*255);
      if(getbit(used[z],w)==1) {
        textAlign(CENTER);
        text(getbit(os[z],w)==1?"O":"X",i*90+u*30+15,j*90+v*30+25);
      }
    }
    noFill();
    stroke(0,0,0);
    strokeWeight(4);
    rect(i*90,j*90,(i+1)*90,(j+1)*90);
    strokeWeight(1);
  }
  if(active==0) {
    fill(0,0,0,128);
    rect(0,0,270,270);
    fill((1-oturn)*255,0,oturn*255);
    text(oturn==1?"O's win!":"X's win!",135,135);
  }
}

int getbit(int bits,int bit) {
  return (bits&(1<<bit))>>bit;
}

void mousePressed() {
  i = mouseX/90;
  j = mouseY/90;
  z = i+j*3;
  
  u = (mouseX-i*90)/30;
  v = (mouseY-j*90)/30;
  w = u+v*3;
  
  if(getbit(active,z)==1 && getbit(used[z],w)==0) {
    used[z] |= (1<<w);
    os[z] |= (oturn<<w);
    if(getbit(won,z)==0 && wins(used[z],os[z],w)) {
      won |= (1<<z);
      owon |= (oturn<<z);
      if(wins(won,owon,z))
        active = 0;
    }
    if(active > 0) {
      active = 1<<w;
      oturn = 1-oturn;
    }
    if(used[w]==FULL && active!=0)
      active = FULL;
  }
}

boolean wins(int usedn,int osn,int m) {
  if(getbit(usedn,(m+3)%9)==1 && getbit(osn,(m+3)%9)==getbit(osn,m) &&
     getbit(usedn,(m+6)%9)==1 && getbit(osn,(m+6)%9)==getbit(osn,m))
    return true; // vertical win
  if(getbit(usedn,(m/3)*3)==1 && getbit(osn,(m/3)*3)==getbit(osn,m) &&
     getbit(usedn,(m/3)*3+1)==1 && getbit(osn,(m/3)*3+1)==getbit(osn,m) &&
     getbit(usedn,(m/3)*3+2)==1 && getbit(osn,(m/3)*3+2)==getbit(osn,m))
    return true; // horizontal win
  if(m%2==0) {
    if(getbit(usedn,0)==1 && getbit(osn,0)==getbit(osn,m) &&
       getbit(usedn,4)==1 && getbit(osn,4)==getbit(osn,m) &&
       getbit(usedn,8)==1 && getbit(osn,8)==getbit(osn,m))
      return true; // left diagonal win
    if(getbit(usedn,2)==1 && getbit(osn,2)==getbit(osn,m) &&
       getbit(usedn,4)==1 && getbit(osn,4)==getbit(osn,m) &&
       getbit(usedn,6)==1 && getbit(osn,6)==getbit(osn,m))
      return true; // right diagonal win
  }
  return false;
}
