package de.vanitasvitae.enigmandroid;


/**
 * Enigma-machine
 * @author vanitasvitae
 *
 */
public class Enigma 
{
	private Plugboard plugboard;

	private Rotor r1;
	private Rotor r2;
	private Rotor r3;

	private Rotor ukw;

	private static final int[] STANDARD_CONFIGURATION = {1,2,3,1,1,1,1};
	private boolean showSteps;
	private boolean verbose = false;
	private String output = null;

	/**
	 * @param pbconf two-dimensional Array with plugs, that need to be switched
	 * @param conf Configuration of the enigma
	 */
	public Enigma(char[][] pbconf, int[] conf) throws Plugboard.PlugAlreadyUsedException
	{
		if(conf!=null) konfiguration(conf);
		else konfiguration(Enigma.STANDARD_CONFIGURATION);

        this.setPlugboard(pbconf);
	}

	/**
	 * Encrypts / Decrypts a given String
	 * @param w Text to decrypt/encrypt
	 * @return 
	 */
	public String encrypt(String w)
	{
		String k=w;
		String c = "";
		//for each char x in k 
		for(int i=0; i<k.length(); i++)
		{
			char x = k.charAt(i);
			c = c + this.encryptChar(x);
		}
		return c;
	}

	/**
	 * Verschlüssele den übergebenen char k.
	 * Zähle danach Konfiguration hoch.
	 * @param k zu verschlüsselnder char
	 * @return verschlüsselter char
	 */
	public char encryptChar(char k)
	{
		//Move rotors
		r1.incrementCounter();
		if(r1.getCounter() == r1.getRingsetting()) {
            r2.incrementCounter();
            if (r2.getCounter() == r2.getRingsetting()) {
                r3.incrementCounter();
            }
        }
		int x = (int) k;
		x = x-65; 	//Remove Unicode Offset (A=65 in Unicode.)
	
		//Encryption
		//forward direction
		x = plugboard.encrypt(x);
		x =(x + r1.getCounter())%26;
		x = r1.encryptForward(x);
		x = (x + r2.getCounter()- r1.getCounter())%26;
		x = r2.encryptForward(x);
		x = (x + r3.getCounter()- r2.getCounter())%26;
		x = r3.encryptForward(x);
		x = (26 + x - r3.getCounter())%26;
		//backward direction
		x = ukw.encryptForward(x);
		x = (26 + x + r3.getCounter())%26;
		x = r3.encryptBackward(x);
		x = (26 + x - r3.getCounter()+ r2.getCounter())%26;
		x = r2.encryptBackward(x);
		x = (26 + x - r2.getCounter()+ r1.getCounter())%26;
		x = r1.encryptBackward(x);
		x = (26 + x - r1.getCounter())%26;
		x = plugboard.encrypt(x);

		return (char) (x+65);                               //Add Offset
	}

	/**
	 * Prepare String for encryption via enigma
     * Replace . , ! ? : with X
     * Remove all other chars that are not A..Z
	 * @param word string
	 * @return prepared string
	 */
	public static String prepare(String word)
	{
		String w = word.toUpperCase();
        String c = "";
		for(int i=0; i<w.length(); i++)
		{
			char x = w.charAt(i);
			if(x>=65 && x<=90)  //If x in [A..Z]
			{
				c = c + x;      //Append to String
			}
			//if x is special symbol
			else
			{
				if(x == '.' || x == ',' || x == '!' || x == '?' || x == ':')
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
     * String must be in format XY:AZ and so on.
     * X and Y are plugs, that will be switched over.
     * Dont do things such as AA or AB:CA. This will cause Exceptions
	 * @param p String
	 * @return Array
	 */
	public static char[][] parsePlugs(String p)	throws InvalidPlugboardConfigurationFormatException
	{
        if(p.length()==0)
        {
            return null;
        }
        String[] in = p.toUpperCase().split(",");

        if(in.length!=(p.length()+1)/3)
        {
            throw new InvalidPlugboardConfigurationFormatException("Fehler beim Parsen der Plugs: Fehlercode 1");
        }
        else
        {
            char[][] plugs = new char[(p.length()+1)/3][2];
            int i=0;
            for(String x:in)
            {
                if(x.length()!=2)
                {
                    throw new InvalidPlugboardConfigurationFormatException("Fehler beim Parsen der Plugs: Fehlercode 2");
                }
                else
                {
                    plugs[i] = x.toCharArray();
                    i++;
                }
            }
            return plugs;
        }
	}

    public void setPlugboard(char[][] c) throws Plugboard.PlugAlreadyUsedException
    {
        plugboard = new Plugboard();
        if(c!=null)
        {
            for(int i=0; i<c.length; i++)
            {
                plugboard.setPlug(c[i][0], c[i][1]);
            }
        }
    }

	/**
	 * Set config of the enigma
	 * @param conf configuration
	 */
	public void konfiguration(int[] conf)
	{
		if(conf.length!=7)
		{
			konfiguration(Enigma.STANDARD_CONFIGURATION);
		}
		else
		{
			int ro1 = conf[0];
			int ro2 = conf[1];
			int ro3 = conf[2];
			int ukwN = conf[3];
			int r1stand = 26+conf[4]-1;
			int r2stand = 26+conf[5]-1;
			int r3stand = 26+conf[6]-1;

			//Lege erste Walze fest
			switch(ro1)
			{
			case 1:
			{	
				r1 = new Rotor('1',(r1stand)%26,0);
				break;
			}
			case 2:
			{
				r1 = new Rotor('2',(r1stand)%26,0);
				break;
			}
			case 3:
			{
				r1 = new Rotor('3',(r1stand)%26,0);
				break;
			}
			case 4:
			{
				r1 = new Rotor('4',(r1stand)%26,0);
				break;
			}
			case 5:
			{
				r1 = new Rotor('5',(r1stand)%26,0);
				break;
			}
			}
			//Lege zweite Walze fest
			switch(ro2)
			{
			case 1:
			{
				r2 = new Rotor('1',(r2stand)%26,0);
				break;
			}
			case 2:
			{
				r2 = new Rotor('2',(r2stand)%26,0);
				break;
			}
			case 3:
			{
				r2 = new Rotor('3',(r2stand)%26,0);
				break;
			}
			case 4:
			{
				r2 = new Rotor('4',(r2stand)%26,0);
				break;
			}
			case 5:
			{
				r2 = new Rotor('5',(r2stand)%26,0);
				break;
			}
			}
			//Lege dritte Walze fest
			switch(ro3)
			{
			case 1:
			{
				r3 = new Rotor('1',(r3stand)%26,0);
				break;
			}
			case 2:
			{
				r3 = new Rotor('2',(r3stand)%26,0);
				break;
			}
			case 3:
			{
				r3 = new Rotor('3',(r3stand)%26,0);
				break;
			}
			case 4:
			{
				r3 = new Rotor('4',(r3stand)%26,0);
				break;
			}
			case 5:
			{
				r3 = new Rotor('5',(r3stand)%26,0);
				break;
			}
			}
			//Lege Umkehrwalze fest
			switch(ukwN)
			{
			case 1:
			{
				ukw = new Rotor('A',0,0);
				break;
			}
			case 2:
			{
				ukw = new Rotor('B',0,0);
				break;
			}
			case 3:
			{
				ukw = new Rotor('C',0,0);
				break;
			}
			}
		}
	}
    public int[] getKonfiguration()
    {
        int[] c = new int[7];
        {
            c[0] = r1.getType();
            c[1] = r2.getType();
            c[2] = r3.getType();
            c[3] = ukw.getType();
            c[4] = r1.getCounter();
            c[5] = r2.getCounter();
            c[6] = r3.getCounter();
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
