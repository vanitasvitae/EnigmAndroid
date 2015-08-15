package de.vanitasvitae.enigmandroid.rotors;

/**
 * Created by vanitas on 11.08.15.
 */
public class Reflector
{
    protected String name;
    protected int type;
    protected Integer[] connections;

    protected Reflector(String name, int type, Integer[] connections)
    {
        this.name = name;
        this.type = type;
        this.connections = connections;
    }

    public static Reflector createReflectorA()
    {
        return new ReflectorA();
    }

    public static Reflector createReflectorB()
    {
        return new ReflectorB();
    }

    public static Reflector createReflectorC()
    {
        return new ReflectorC();
    }

    public static Reflector createReflector(int type)
    {
        switch (type)
        {
            case 1: return createReflectorA();
            case 2: return createReflectorB();
            default: return createReflectorC();
        }
    }

    public int encrypt(int input)
    {
        return this.connections[normalize(input)];
    }

    public int getType()
    {
        return this.type;
    }

    public String getName()
    {
        return this.name;
    }

    private int getRotorSize()
    {
        return this.connections.length;
    }

    private int normalize(int input)
    {
        return (input + this.getRotorSize()) % this.getRotorSize();
    }

    private static class ReflectorA extends Reflector
    {
        public ReflectorA()
        {
            super("A", 1, new Integer[]{4, 9, 12, 25, 0, 11, 24, 23, 21, 1, 22, 5, 2, 17, 16, 20, 14, 13, 19, 18, 15, 8, 10, 7, 6, 3});
        }
    }

    private static class ReflectorB extends Reflector
    {
        public ReflectorB()
        {
            super("B", 2, new Integer[]{24, 17, 20, 7, 16, 18, 11, 3, 15, 23, 13, 6, 14, 10, 12, 8, 4, 1, 5, 25, 2, 22, 21, 9, 0, 19});
        }
    }

    private static class ReflectorC extends Reflector
    {
        public ReflectorC()
        {
            super("C", 3, new Integer[]{5, 21, 15, 9, 8, 0, 14, 24, 4, 3, 17, 25, 23, 22, 6, 2, 19, 10, 20, 16, 18, 1, 13, 12, 7, 11});
        }
    }

}
