package de.vanitasvitae.enigmandroid.rotors;

/**
 * Created by vanitas on 11.08.15.
 */
public class Rotor
{
    protected String name;
    protected int type;
    protected Integer[] connections;
    protected Integer[] reversedConnections;
    protected int turnOverNotch;

    protected int ringSetting;
    protected int rotation;

    protected Rotor(String name, int type, Integer[] connections, Integer[] reversedConnections,
                    int turnOverNotch, int ringSetting, int rotation)
    {
        this.name = name;
        this.type = type;
        this.connections = connections;
        this.reversedConnections = reversedConnections;
        this.turnOverNotch = turnOverNotch;
        this.ringSetting = ringSetting;
        this.rotation = rotation;
    }

    public static Rotor createRotor(int type, int ringSetting, int rotation)
    {
        switch (type)
        {
            case 1: return createRotorI(ringSetting, rotation);
            case 2: return createRotorII(ringSetting, rotation);
            case 3: return createRotorIII(ringSetting, rotation);
            case 4: return createRotorIV(ringSetting, rotation);
            default: return createRotorV(ringSetting, rotation);
        }
    }

    public static Rotor createRotorI(int ringSetting, int rotation)
    {
        return new RotorI(ringSetting, rotation);
    }

    public static Rotor createRotorII(int ringSetting, int rotation)
    {
        return new RotorII(ringSetting, rotation);
    }

    public static Rotor createRotorIII(int ringSetting, int rotation)
    {
        return new RotorIII(ringSetting, rotation);
    }

    public static Rotor createRotorIV(int ringSetting, int rotation)
    {
        return new RotorIV(ringSetting, rotation);
    }

    public static Rotor createRotorV(int ringSetting, int rotation)
    {
        return new RotorV(ringSetting, rotation);
    }

    public int encryptForward(int input)
    {
        return this.connections[normalize(input)];
    }

    public int encryptBackward(int input)
    {
        return this.reversedConnections[normalize(input)];
    }

    public int getType()
    {
        return this.type;
    }

    public int getRotation()
    {
        return this.rotation - this.getRingSetting();
    }

    /**
     * increment rotation of the rotor by one.
     */
    public void rotate()
    {
        this.rotation = normalize(this.getRotation()+1);
    }

    /**
     * Return true, if rotor is at a position, where it turns over the next rotor
     *
     * @return boolean
     */
    public boolean isAtTurnoverPosition()
    {
        return this.rotation == this.turnOverNotch;
    }

    /**
     * The Double Turn Anomaly (deutsch: Doppelsprung Anomalie) is an anomaly in the rotor movement
     * caused by the mechanical implementation of the enigma.
     * Whenever the rightmost rotor turns the middle rotor AND the middle rotor is only one move
     * from turning the leftmost rotor, the middle rotor turns again with the next character.
     * So technically there are only 26*25*26 possible rotor settings for any but firmly 3 rotors.
     *
     * @return boolean
     */
    public boolean doubleTurnAnomaly()
    {
        return this.rotation == this.turnOverNotch - 1;
    }

    @SuppressWarnings("unused")
    /**
     * Returns the position of the turnover notch
     *
     * @return turnOver
     */
    public int getTurnOver()
    {
        return this.turnOverNotch;
    }

    @SuppressWarnings("unused")
    /**
     * Return ringSettings of the rotor
     * @return ringSetting
     */
    public int getRingSetting()
    {
        return this.ringSetting;
    }

    public String getName()
    {
        return this.name;
    }

    public int getRotorSize()
    {
        return this.connections.length;
    }

    public int normalize(int input)
    {
        return (input + this.getRotorSize()) % this.getRotorSize();
    }

    private static class RotorI extends Rotor
    {
        public RotorI(int ringSetting, int rotation)
        {
            super("I", 1,
                    new Integer[]{4, 10, 12, 5, 11, 6, 3, 16, 21, 25, 13, 19, 14, 22, 24, 7, 23, 20, 18, 15, 0, 8, 1, 17, 2, 9},
                    new Integer[]{20, 22, 24, 6, 0, 3, 5, 15, 21, 25, 1, 4, 2, 10, 12, 19, 7, 23, 18, 11, 17, 8, 13, 16, 14, 9},
                    17, ringSetting, rotation);
        }
    }

    private static class RotorII extends Rotor
    {
        public RotorII(int ringSetting, int rotation)
        {
            super("II", 2,
                    new Integer[]{0, 9, 3, 10, 18, 8, 17, 20, 23, 1, 11, 7, 22, 19, 12, 2, 16, 6, 25, 13, 15, 24, 5, 21, 14, 4},
                    new Integer[]{0, 9, 15, 2, 25, 22, 17, 11, 5, 1, 3, 10, 14, 19, 24, 20, 16, 6, 4, 13, 7, 23, 12, 8, 21, 18},
                    5, ringSetting, rotation);
        }
    }

    private static class RotorIII extends Rotor
    {
        public RotorIII(int ringSetting, int rotation)
        {
            super("III", 3,
                    new Integer[]{1, 3, 5, 7, 9, 11, 2, 15, 17, 19, 23, 21, 25, 13, 24, 4, 8, 22, 6, 0, 10, 12, 20, 18, 16, 14},
                    new Integer[]{19, 0, 6, 1, 15, 2, 18, 3, 16, 4, 20, 5, 21, 13, 25, 7, 24, 8, 23, 9, 22, 11, 17, 10, 14, 12},
                    22, ringSetting, rotation);
        }
    }

    private static class RotorIV extends Rotor
    {
        public RotorIV(int ringSetting, int rotation)
        {
            super("IV", 4,
                    new Integer[]{4, 18, 14, 21, 15, 25, 9, 0, 24, 16, 20, 8, 17, 7, 23, 11, 13, 5, 19, 6, 10, 3, 2, 12, 22, 1},
                    new Integer[]{7, 25, 22, 21, 0, 17, 19, 13, 11, 6, 20, 15, 23, 16, 2, 4, 9, 12, 1, 18, 10, 3, 24, 14, 8, 5},
                    10, ringSetting, rotation);
        }
    }

    private static class RotorV extends Rotor
    {
        public RotorV(int ringSetting, int rotation)
        {
            super("V", 5,
                    new Integer[]{21, 25, 1, 17, 6, 8, 19, 24, 20, 15, 18, 3, 13, 7, 11, 23, 0, 22, 12, 9, 16, 14, 5, 4, 2, 10},
                    new Integer[]{16, 2, 24, 11, 23, 22, 4, 13, 5, 19, 25, 14, 18, 12, 21, 9, 20, 3, 10, 6, 8, 0, 17, 15, 7, 1},
                    0, ringSetting, rotation);
        }
    }
}
