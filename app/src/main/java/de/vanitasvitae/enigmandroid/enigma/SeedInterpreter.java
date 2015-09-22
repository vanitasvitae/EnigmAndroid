package de.vanitasvitae.enigmandroid.enigma;

import de.vanitasvitae.enigmandroid.enigma.rotors.Reflector;

/**
 * Created by vanitas on 18.09.15.
 */
public class SeedInterpreter
{
    /*
    12 machineType
    1-8 rotor1
    1-8 rotor2
    1-8 rotor3
    2 rotor4
    1-3 ukw
    26 pos1
    26 pos2
    26 pos3
    26 pos4/posukw
    --plugboard
    #plugs 13


     */

    public double prepareSeed(long input)
    {
        double maxIn = Long.MAX_VALUE;
        double maxOut = 100000; //TODO: Temporär!

        return (input / maxIn) * maxOut;
    }

    public static EnigmaStateBundle seedToState(long seed)
    {
        long s = seed/12;
        switch ((int) seed % 12)
        {
            case 0: return prepState_I(s);
            case 1: return prepState_M3(s);
            case 2: return prepState_M4(s);
            case 3: return prepState_G31(s);
            case 4: return prepState_G312(s);
            case 5: return prepState_G260(s);
            case 6: return prepState_D(s);
            case 7: return prepState_K(s);
            case 8: return prepState_KS(s);
            case 9: return prepState_KSA(s);
            case 10: return prepState_R(s);
            default: return prepState_T(s);
        }
    }

    public static EnigmaStateBundle prepState_I(long seed)
    {
        EnigmaStateBundle state = new EnigmaStateBundle();
        state.setMachineType("I");

        return state;
    }

    public static EnigmaStateBundle prepState_M3(long seed)
    {
        return null;
    }

    public static EnigmaStateBundle prepState_M4(long seed)
    {
        return null;
    }

    public static EnigmaStateBundle prepState_G31(long seed)
    {
        return null;
    }

    public static EnigmaStateBundle prepState_G312(long seed)
    {
        return null;
    }

    public static EnigmaStateBundle prepState_G260(long seed)
    {
        return null;
    }

    public static EnigmaStateBundle prepState_D(long seed)
    {
        return null;
    }

    public static EnigmaStateBundle prepState_K(long seed)
    {
        return null;
    }

    public static EnigmaStateBundle prepState_KS(long seed)
    {
        return null;
    }

    public static EnigmaStateBundle prepState_KSA(long seed)
    {
        return null;
    }

    public static EnigmaStateBundle prepState_R(long seed)
    {
        return null;
    }

    public static EnigmaStateBundle prepState_T(long seed)
    {
        return null;
    }

    public static int[] getPermutation(long seed)
    {
        int[] per = Reflector.ReflectorEnigma_D_KD_G31.defaultWiring_D_KD_G31;
        long maxPermutations = Long.valueOf("532985208200576");
        //long result =
        return per;

    }
}
