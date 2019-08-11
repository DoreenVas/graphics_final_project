package WorldObjects;

import Utils.Vector;

import javax.media.opengl.GL2;

public class BreakableCube extends Cube{
    // members
    private int hp;

    public BreakableCube(Vector v, float l, int lives) {
        this(v,l,lives,null,null);
    }

    public BreakableCube(Vector v, float l, int lives, String texturePath, Type t) {
        super(v, l, texturePath, t);
        this.hp = lives;
    }

    public int getHp() {
        return this.hp;
    }

    public void decreaseHp() {
        this.hp--;
    }

    public void draw(GL2 gl) {
        super.draw(gl);
    }
}
