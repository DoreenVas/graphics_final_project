/******
 * Student name: Doreen Vaserman
 * Student ID: 308223627
 * Student name: Nadav Spitzer
 * Student ID: 302228275
 */
package Scene;

import Collision.Collidable;
import Enums.MovementEnum;
import Enums.SteerEnum;
import WorldObjects.*;
import com.jogamp.newt.Window;
import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.awt.AWTKeyAdapter;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.glu.GLU;

import Utils.Vector;

public class SceneBuilder extends KeyAdapter implements GLEventListener {

    private static GLU glu = new GLU();
    private World world;
    private Player player;
    private float alpha = (float)Math.toRadians(5);
    private static float step = 0.1f;

    public void display(GLAutoDrawable gLDrawable) {
        Vector direction = player.getDirection();
        Vector up = player.getUp();
        Vector pos = player.getPos();

        final GL2 gl = gLDrawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();  // Reset The View
        glu.gluLookAt(pos.getX(), pos.getY(), pos.getZ(),
                pos.getX() - direction.getX(),
                pos.getY() - direction.getY(),
                pos.getZ() - direction.getZ(),
                up.getX(),
                up.getY(),
                up.getZ());

        gl.glColor4f(1f, 1f, 1f, 1f); //NEEDS to be white before drawing, else stuff will tint.
        world.draw(gl);
    }

    public void displayChanged(GLAutoDrawable gLDrawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void init(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);              // Enable Smooth Shading
        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.5f);    // White Background
        gl.glClearDepth(1.0f);                      // Depth Buffer Setup
        gl.glEnable(GL2.GL_DEPTH_TEST);              // Enables Depth Testing
        gl.glDepthFunc(GL2.GL_LEQUAL);               // The Type Of Depth Testing To Do
        // Really Nice Perspective Calculations
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glEnable(GL2.GL_TEXTURE_2D);

        Cube tnt1 = new Cube(new Vector(-9,-1,1),2,"resources/pics/tnt.jpg", Collidable.Type.tnt);
        Cube tnt2 = new Cube(new Vector(-1,-1,1),2,"resources/pics/tnt.jpg", Collidable.Type.tnt);
        Cube tnt3 = new Cube(new Vector(7,-1,1),2,"resources/pics/tnt.jpg", Collidable.Type.tnt);

//        movingCube1 = new MovingCube(new Cube(new Vector(5, -1, -20), 2, "resources/pics/steel-box.jpg", Collidable.Type.stay));
        MovingCube movingCube1 = new MovingCube(new Vector(-9, -1, -10), 2,
                "resources/pics/moving_box.png", Collidable.Type.stay, 0.2f, MovementEnum.RIGHT);
        MovingCube movingCube2 = new MovingCube(new Vector(7, -1, -10), 2,
                "resources/pics/moving_box.png", Collidable.Type.stay, -0.2f, MovementEnum.LEFT);
        MovingCube movingCube3 = new MovingCube(new Vector(-9, -1, -24), 2,
                "resources/pics/moving_box.png", Collidable.Type.stay, 0.2f, MovementEnum.RIGHT);
        MovingCube movingCube4 = new MovingCube(new Vector(-1, 7, -35), 2,
                "resources/pics/moving_box.png", Collidable.Type.stay, 0.2f, MovementEnum.DOWN);

        world = new World();
        world.addToItemsList(tnt1);
        world.addToItemsList(tnt2);
        world.addToItemsList(tnt3);
        world.addToItemsList(movingCube1);
        world.addToItemsList(movingCube2);
        world.addToItemsList(movingCube3);
        world.addToItemsList(movingCube4);

        Vector p;
        for (int z = -65; z > -75; z-=2) {
            for (int y = -1; y < 9; y+=2) {
                p = new Vector(10, y, z);
                world.addToItemsList(new BreakableCube(p, 2, 3, "resources/pics/box.jpg", Collidable.Type.breakable));
            }
        }

        player = new Player();

        gl.glEnable(GL2.GL_LIGHTING);
        if (drawable instanceof Window) {
            Window window = (Window) drawable;
            window.addKeyListener(this);
        } else if (GLProfile.isAWTAvailable() && drawable instanceof java.awt.Component) {
            java.awt.Component comp = (java.awt.Component) drawable;
            new AWTKeyAdapter(this, drawable).addTo(comp);
        }
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
    }

    public void reshape(GLAutoDrawable drawable, int x,
                        int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        if(height <= 0) {
            height = 1;
        }
        float h = (float)width / (float)height;
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(50.0f, h, 1.0, 1000.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void keyPressed(KeyEvent e) {
        switch(e.getKeyChar()) {
            case 'i': // rotate up (x axis)
                player.getCoordination().rotate(SteerEnum.UP_X, -alpha);
                break;
            case 'k': // rotate down (x axis)
                player.getCoordination().rotate(SteerEnum.DOWN_X, alpha);
                break;
            case 'l': // rotate right (y axis)
                player.getCoordination().rotate(SteerEnum.RIGHT_Y, -alpha);
                break;
            case 'j': // rotate left (y axis)
                player.getCoordination().rotate(SteerEnum.LEFT_Y, alpha);
                break;
            case 'o': // rotate right (z axis)
                player.getCoordination().rotate(SteerEnum.RIGHT_Z, alpha);
                break;
            case 'u': // rotate left (z axis)
                player.getCoordination().rotate(SteerEnum.LEFT_Z, -alpha);
                break;
            case 'w': // move forward
                player.move(MovementEnum.FORWARD);
                break;
            case 's': // move backward
                player.move(MovementEnum.BACKWARD);
                break;
            case 'd': // move right
                player.move(MovementEnum.RIGHT);
                break;
            case 'a': // move left
                player.move(MovementEnum.LEFT);
                break;
            case 'e': // move up
                player.move(MovementEnum.UP);
                break;
            case 'q': // move down
                player.move(MovementEnum.DOWN);
                break;
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

}
