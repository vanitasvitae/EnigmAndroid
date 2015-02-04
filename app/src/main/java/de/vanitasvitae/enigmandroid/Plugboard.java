package de.vanitasvitae.enigmandroid;

/**
 * Klasse für das Steckerbrett der Enigma
 * @author vanitas
 *
 */
public class Plugboard
{
	//Plugboard
	//	Q		W		E		R		T		Z		U		I		O
	//		A		S		D		F		G		H		J		K
	//	P		Y		X		C		V		B		N		M		L

	//Array für die Steckerverbindungen
	int[] pb;	
	//Vergleichsarray (Keine Stecker)
	public static final int[] ref = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25};
	/**
	 * Erzeuge neues Plugboard ohne Steckverbindungen
	 */
	public Plugboard()
	{
		pb = new int[26];
		resetPlugboard();		
	}

	/**
	 * Verschlüssele den gegebenen Buchstaben, den Regeln des Plugboard folgend
	 * @param x zu verschlüsselndes Zeichen
	 * @return Verschlüsseltes Zeichen
	 */
	public int encrypt(int x)
	{
		return pb[x];
	}

	/**
	 * Setze das Plugboard in den Standardzustand (Keine Stecker)
	 */
	public void resetPlugboard()
	{
		pb = new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25};
	}

	public void setPlugs(char[][] p) throws PlugAlreadyUsedException
	{
		for(int i=0; i<p.length; i++)
		{
			if(p[i]!=null)
			{
				this.setPlug(p[i][0], p[i][1]);
			}
		}
	}
	/**
	 * Steckere Buchstaben a und b auf dem Plugboard
	 * @param a first Plug
	 * @param b second Plug
	 */
	public void setPlug(char a, char b) throws PlugAlreadyUsedException
	{
		//Verhindere, dass Buchstabe mit sich selbst gesteckert wird
		if(a==b)
        {
            throw new PlugAlreadyUsedException("Unable to plug "+a +" to " +a);
        }
		int x = a-65;
		int y = b-65;
        //Check, if plugs already steckered
		if(pb[x]!=ref[x])
		{
			throw new PlugAlreadyUsedException("Plug " + a + " used twice!");
		}
        else if(pb[y]!=ref[y])
        {
            throw new PlugAlreadyUsedException("Plug " + b + " used twice!");
        }
		else	//Steckere um
		{
			int c =pb[x];
			pb[x] = pb[y];
			pb[y] = c;
		}
	}

    public static class PlugAlreadyUsedException extends Exception
    {
        public PlugAlreadyUsedException(String m)
        {
            super(m);
        }
    }
}
