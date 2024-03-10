import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WatchThread extends Thread {

    public DrawPanel canvas;

    public WatchThread(DrawPanel canvas) {
        this.canvas = canvas;
    }

    public void run() {

        Timer timer = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               /* synchronized (canvas) {
                    System.out.println(canvas.angle);
                    canvas.angle += canvas.numPoints / 60;
                    if (canvas.angle >= canvas.numPoints) {
                        canvas.angle -= canvas.numPoints;
                    }
                }*/
                canvas.repaint();
            }
        });

        timer.start();
    }
}