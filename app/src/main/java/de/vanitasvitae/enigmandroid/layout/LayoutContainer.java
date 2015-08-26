package de.vanitasvitae.enigmandroid.layout;

import android.widget.EditText;

import de.vanitasvitae.enigmandroid.MainActivity;
import de.vanitasvitae.enigmandroid.enigma.Enigma;

/**
 * Abstract LayoutContainer for Enigma machines
 * This class contains the layout and controls the layout elements such as spinners and stuff
 * Copyright (C) 2015  Paul Schaub

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along
 with this program; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * @author vanitasvitae
 */
public abstract class LayoutContainer
{
    protected MainActivity main;
    protected Enigma enigma;

    protected EditText inputView;
    protected EditText outputView;

    protected int[] ringSettings;

    public LayoutContainer()
    {
        this.main = (MainActivity) MainActivity.ActivitySingleton.getInstance().getActivity();
    }

    abstract void initialize();
    public abstract void reset();
    public abstract void updateLayout();
    public abstract int[] createConfiguration();
    public abstract int[][] createPlugboardConfiguration();
    public abstract void showRingSettingsDialog();

    public Enigma getEnigma()
    {
        return enigma;
    }

    public EditText getInputView()
    {
        return this.inputView;
    }

    public EditText getOutputView()
    {
        return this.outputView;
    }

    public static LayoutContainer createLayoutContainer(String type)
    {
        switch (type)
        {
            case "M4":
                return new LayoutContainer_M4();
            case "M3":
                return new LayoutContainer_M3();
            default:
                return new LayoutContainer_I();
        }
    }
}

