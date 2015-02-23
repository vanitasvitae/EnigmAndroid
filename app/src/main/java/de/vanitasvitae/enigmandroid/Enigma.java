package de.vanitasvitae.enigmandroid;


/**
 * Enigma-machine
 *
 * @author vanitasvitae
 */
public class Enigma
{
    private Plugboard plugboard;

    //Slots for the rotors
    private Rotor r1;
    private Rotor r2;
    private Rotor r3;
    //Slot for the reflector
    private Rotor reflector;
    private boolean anomaly;
    //Standard configuration (rotors 1-3, reflector B, all three rotors set to position 1, rings too)
    public static final int[] STANDARD_CONFIGURATION = {1, 2, 3, 2, 1, 1, 1, 0, 0, 0};

    /**
     * Create new Enigma with given configuration.
     * If pbconf == null no plugs will be set (no scrambling in the plugboard).
     * If conf == null the enigma will be set to STANDARD_CONFIGURATION.
     *
     * @param pbconf two-dimensional array containing the chars symbolizing plugs that need to be switched over.
     * @param conf   configuration of the enigma (a,b,c,d,e,f,g - a-c rotors, d reflector, e-g positions of the rotors)
     */
    public Enigma(char[][] pbconf, int[] conf) throws Plugboard.PlugAlreadyUsedException
    {
        if (conf != null) setConfiguration(conf);
        else setConfiguration(Enigma.STANDARD_CONFIGURATION);

        this.setPlugboard(pbconf);
    }

    /**
     * Encrypt / Decrypt a given String
     *
     * @param w Text to decrypt/encrypt
     * @return encrypted/decrypted string
     */
    public String encrypt(String w)
    {
        //output string
        String c = "";
        //for each char x in k
        for (int i = 0; i < w.length(); i++)
        {
            char x = w.charAt(i);
            //encrypt char
            c = c + this.encryptChar(x);
        }
        //return en-/decrypted string
        return c;
    }

    /**
     * Perform crypto on char.
     * Beforehand rotate rotors. Also implement the rotor anomaly.
     *
     * @param k input char
     * @return output char
     */
    public char encryptChar(char k)
    {
        //Rotate rotors
        r1.incrementCounter();
        if (r1.isAtTurnoverPosition() || this.anomaly)
        {
            r2.incrementCounter();
            //Handle Anomaly
            this.anomaly = r2.doubleTurnAnomaly();
            if (r2.isAtTurnoverPosition())
            {
                r3.incrementCounter();
            }
        }
        int x = (int) k;
        x = x - 65;    //Remove Unicode Offset (A=65 in Unicode.)

        //Encryption
        //forward direction
        x = plugboard.encrypt(x);
        x = (x + r1.getCounter()) % 26;
        x = r1.encryptForward(x);
        x = (x + r2.getCounter() - r1.getCounter()) % 26;
        x = r2.encryptForward(x);
        x = (x + r3.getCounter() - r2.getCounter()) % 26;
        x = r3.encryptForward(x);
        x = (26 + x - r3.getCounter()) % 26;
        //backward direction
        x = reflector.encryptForward(x);
        x = (26 + x + r3.getCounter()) % 26;
        x = r3.encryptBackward(x);
        x = (26 + x - r3.getCounter() + r2.getCounter()) % 26;
        x = r2.encryptBackward(x);
        x = (26 + x - r2.getCounter() + r1.getCounter()) % 26;
        x = r1.encryptBackward(x);
        x = (26 + x - r1.getCounter()) % 26;
        x = plugboard.encrypt(x);

        return (char) (x + 65);     //Add Offset
    }

    /**
     * Prepare String for encryption via enigma
     * Replace . , ! ? : with X
     * Remove all other chars that are not A..Z
     *
     * @param word string
     * @return prepared string
     */
    public static String prepare(String word)
    {
        String w = word.toUpperCase();
        String c = "";
        for (int i = 0; i < w.length(); i++)
        {
            char x = w.charAt(i);
            if (x >= 65 && x <= 90)  //If x in [A..Z]
            {
                c = c + x;      //Append to String
            }
            //if x is special symbol
            else
            {
                if (x == '.' || x == ',' || x == '!' || x == '?' || x == ':')
                {
                    //replace x with X and encrypt
                    c = c + 'X';
                }
            }
        }
        return c;
    }

    /**
     * Create Plugboard configuration from String.
     * String must be in format XY,AZ and so on.
     * X and Y are plugs, that will be switched over.
     * Don't use plugs twice such as in AA or AB,CA. This will cause Exceptions.
     *
     * @param p String
     * @return Array containing plugboard configuration
     */
    public static char[][] parsePlugs(String p) throws InvalidPlugboardConfigurationFormatException
    {
        p = p.toUpperCase();
        //Check, if empty
        if (p.length() == 0)
        {
            return null;
        }
        //Ensure uppercase and split string
        String[] in = p.toUpperCase().split(",");

        //Check, whether input have had a correct length. Length+1 divided by 3 should be exactly how much fields there are in the array.
        //(2 chars or 2 chars followed by any times a comma and two chars)
        if (in.length != (p.length() + 1) / 3)
        {
            throw new InvalidPlugboardConfigurationFormatException("Error parsing plugs! Maybe you missed a ','?");
        } else
        {
            //Create new 2 dimensional array for pairs of plugs
            char[][] plugs = new char[(p.length() + 1) / 3][2];
            //Fill the array
            int i = 0;
            for (String x : in)
            {
                //Check, whether string is not representing a pair
                if (x.length() != 2)
                {
                    throw new InvalidPlugboardConfigurationFormatException("Error parsing plugs! Maybe you didn't enter a pair somewhere?");
                }
                //If it does
                else
                {
                    char[] pair = x.toCharArray();
                    //Check, if Plugs are in alphabet
                    if(pair[0]<65 || pair[1]<65 || pair[0]>90 || pair[1]>90) throw new InvalidPlugboardConfigurationFormatException("Error parsing plugs! Maybe you entered a number or a special character?");
                    else
                    {
                        //add it to the array
                        plugs[i] = pair;
                        i++;
                    }
                }
            }
            return plugs;
        }
    }

    /**
     * Set the plugboard to a new created object and set the configuration
     *
     * @param c configuration
     * @throws Plugboard.PlugAlreadyUsedException
     */
    public void setPlugboard(char[][] c) throws Plugboard.PlugAlreadyUsedException
    {
        plugboard = new Plugboard();
        if (c != null)
        {
            //Set each plug pair
            for (char[] x : c)
            {
                plugboard.setPlugPair(x[0], x[1]);
            }
        }
    }

    /**
     * Set config of the enigma
     *
     * @param conf configuration
     */
    public void setConfiguration(int[] conf)
    {
        if (conf.length != 10)
        {
            setConfiguration(Enigma.STANDARD_CONFIGURATION);
        } else
        {
            int ro1 = conf[0];
            int ro2 = conf[1];
            int ro3 = conf[2];
            int ref = conf[3];
            int r1rot = 26 + conf[4] - 1;
            int r2rot = 26 + conf[5] - 1;
            int r3rot = 26 + conf[6] - 1;
            int ro1Ring = conf[7];
            int ro2Ring = conf[8];
            int ro3Ring = conf[9];

            //Set first rotor
            switch (ro1)
            {
                case 1:
                {
                    r1 = new Rotor('1', (r1rot) % 26, ro1Ring);
                    break;
                }
                case 2:
                {
                    r1 = new Rotor('2', (r1rot) % 26, ro1Ring);
                    break;
                }
                case 3:
                {
                    r1 = new Rotor('3', (r1rot) % 26, ro1Ring);
                    break;
                }
                case 4:
                {
                    r1 = new Rotor('4', (r1rot) % 26, ro1Ring);
                    break;
                }
                case 5:
                {
                    r1 = new Rotor('5', (r1rot) % 26, ro1Ring);
                    break;
                }
            }
            //Set second rotor
            switch (ro2)
            {
                case 1:
                {
                    r2 = new Rotor('1', (r2rot) % 26, ro2Ring);
                    break;
                }
                case 2:
                {
                    r2 = new Rotor('2', (r2rot) % 26, ro2Ring);
                    break;
                }
                case 3:
                {
                    r2 = new Rotor('3', (r2rot) % 26, ro2Ring);
                    break;
                }
                case 4:
                {
                    r2 = new Rotor('4', (r2rot) % 26, ro2Ring);
                    break;
                }
                case 5:
                {
                    r2 = new Rotor('5', (r2rot) % 26, ro2Ring);
                    break;
                }
            }
            //Set third rotor
            switch (ro3)
            {
                case 1:
                {
                    r3 = new Rotor('1', (r3rot) % 26, ro3Ring);
                    break;
                }
                case 2:
                {
                    r3 = new Rotor('2', (r3rot) % 26, ro3Ring);
                    break;
                }
                case 3:
                {
                    r3 = new Rotor('3', (r3rot) % 26, ro3Ring);
                    break;
                }
                case 4:
                {
                    r3 = new Rotor('4', (r3rot) % 26, ro3Ring);
                    break;
                }
                case 5:
                {
                    r3 = new Rotor('5', (r3rot) % 26, ro3Ring);
                    break;
                }
            }
            //Set reflector
            switch (ref)
            {
                case 1:
                {
                    reflector = new Rotor('A', 0, 0);
                    break;
                }
                case 2:
                {
                    reflector = new Rotor('B', 0, 0);
                    break;
                }
                case 3:
                {
                    reflector = new Rotor('C', 0, 0);
                    break;
                }
            }
        }
    }

    /**
     * Return the configuration, the enigma machine is in right NOW
     *
     * @return array containing configuration
     */
    public int[] getConfiguration()
    {
        int[] c = new int[10];
        {
            c[0] = r1.getType();
            c[1] = r2.getType();
            c[2] = r3.getType();
            c[3] = reflector.getType();
            c[4] = r1.getCounter();
            c[5] = r2.getCounter();
            c[6] = r3.getCounter();
            c[7] = r1.getRingsetting();
            c[8] = r2.getRingsetting();
            c[9] = r3.getRingsetting();
        }
        return c;
    }

    public static class InvalidPlugboardConfigurationFormatException extends Exception
    {
        public InvalidPlugboardConfigurationFormatException(String m)
        {
            super(m);
        }
    }
}
