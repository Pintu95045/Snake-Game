import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;

public class Panel extends JPanel implements ActionListener {
   static int width=1200;
   static int height=600;
   static int unit=50;
   boolean flag=false;
   int score=0;
   Timer timer;
   Random random;
   int fx,fy;
   int length = 3;
   char dir='R';

   int xsnake[]=new int[288];
   int ysnake[]=new int[288];
   Panel(){
       this.setPreferredSize(new Dimension(width, height));
       this.setBackground(Color.black);
       this.addKeyListener(new key());
       this.setFocusable(true);
       random=new Random();
       gamestart();
   }
   public void gamestart(){
      spawnfood();
      flag=true;
      timer=new Timer(160,this);
      timer.start();
   }
   public void spawnfood(){
       fx=random.nextInt(width/unit)*unit;
       fy=random.nextInt(height/unit)*unit;
   }
    public void paintComponent(Graphics graphic) {
        super.paintComponent(graphic);
        draw(graphic);
    }

    public void draw(Graphics graphic){
       if(flag){
           //for the food particle.
           graphic.setColor(Color.red);
           graphic.fillOval(fx,fy,50,50);
           for(int i=0;i<length;i++){
               graphic.setColor(Color.green);
               graphic.fillRect(xsnake[i],ysnake[i],50,50);
           }
           graphic.setColor(Color.cyan);
           graphic.setFont(new Font("Comic Sans",Font.BOLD,40));
           FontMetrics fme=getFontMetrics(graphic.getFont());
           graphic.drawString("Score:"+score,(width-fme.stringWidth("Score:"+score))/2,graphic.getFont().getSize());
       }
       else{
           gameover(graphic);
       }
    }
    public void gameover(Graphics graphic){
       //Display Final Score
        graphic.setColor(Color.cyan);
        graphic.setFont(new Font("Comic Sans",Font.BOLD,40));
        FontMetrics fme=getFontMetrics(graphic.getFont());
        graphic.drawString("Score:"+score,(width-fme.stringWidth("Score:"+score))/2,graphic.getFont().getSize());
        //gameover text
        graphic.setColor(Color.red);
        graphic.setFont(new Font("Comic Sans",Font.BOLD,80));
        fme=getFontMetrics(graphic.getFont());
        graphic.drawString("Game Over",(width-fme.stringWidth("Game Over"))/2,height/2);

        graphic.setColor(Color.green);
        graphic.setFont(new Font("Comic Sans",Font.BOLD,40));
        fme=getFontMetrics(graphic.getFont());
        graphic.drawString("Press R to Replay",(width-fme.stringWidth("Press R to Replay"))/2,3*height/4);
    }

    public void eat(){
       if((fx==xsnake[0]) && (fy==ysnake[0])){
           length++;
           score++;
           spawnfood();
       }
    }

    public void hit(){
       //to check hit its own body
        for(int i=length-1;i>0;i--){
            if((xsnake[0]==xsnake[i]) && (ysnake[0]==ysnake[i])){
               flag=false;
            }
        }
        if(xsnake[0]<0){
            flag=false;
        } else if (xsnake[0]>width) {
            flag=false;
        } else if (ysnake[0]<0) {
            flag=false;
        }
        else if (ysnake[0]>height){
            flag=false;
        }
        if (flag==false) {
            timer.stop();
        }
    }

    public void move(){
       // update the coordinate of the body except head
        for(int i=length-1;i>0;i--){
            xsnake[i]=xsnake[i-1];
            ysnake[i]=ysnake[i-1];
        }
        switch (dir){
            case 'U':
                ysnake[0]=ysnake[0]-unit;
                break;
            case 'D':
                ysnake[0]=ysnake[0]+unit;
                break;
            case 'R':
                xsnake[0]=xsnake[0]+unit;
                break;
            case 'L':
                xsnake[0]=xsnake[0]-unit;
                break;
        }

    }

    //code for snake moving to the direction.
    public class key extends KeyAdapter{
       public void keyPressed(KeyEvent e){
           switch (e.getKeyCode()){
               case KeyEvent.VK_UP:
                   if(dir !='D'){
                       dir='U';
                   }
                   break;
               case KeyEvent.VK_DOWN:
                   if(dir !='U'){
                       dir='D';
                   }
                   break;
               case KeyEvent.VK_LEFT:
                   if(dir !='R'){
                       dir='L';
                   }
                   break;
               case KeyEvent.VK_RIGHT:
                   if(dir !='L'){
                       dir='R';
                   }
                   break;
               case KeyEvent.VK_R:
                   if(flag==false){
                       score=0;
                       length=3;
                       dir='R';
                       Arrays.fill(xsnake,0);
                       Arrays.fill(ysnake,0);
                       gamestart();
                   }
                   break;
           }
       }
    }

    public void actionPerformed(ActionEvent e){
       if(flag){
           move();
           hit();
           eat();
       }
        repaint();
   }
}
