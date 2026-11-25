package ru.mipt.bit.platformer.ai;

/** A grid-based advisor independent of the game's command types. */
public final class AimAdvisor {
    public enum Advice { WAIT, NORTH, SOUTH, EAST, WEST, SHOOT }

    public Advice advise(int dx, int dy, int facingX, int facingY, boolean clear) {
        if (!clear || (dx == 0 && dy == 0) || (dx != 0 && dy != 0)) return Advice.WAIT;
        int x = Integer.signum(dx);
        int y = Integer.signum(dy);
        if (x == facingX && y == facingY) return Advice.SHOOT;
        if (x != 0) return x > 0 ? Advice.EAST : Advice.WEST;
        return y > 0 ? Advice.NORTH : Advice.SOUTH;
    }
}
