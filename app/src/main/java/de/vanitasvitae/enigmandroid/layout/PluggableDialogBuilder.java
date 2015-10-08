package de.vanitasvitae.enigmandroid.layout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import de.vanitasvitae.enigmandroid.MainActivity;
import de.vanitasvitae.enigmandroid.R;
import de.vanitasvitae.enigmandroid.enigma.EnigmaStateBundle;

/**
 * Builder for the dialog that is used to plug the plugboard/wire the
 * rewirable reflector.
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
public class PluggableDialogBuilder
{
    protected ArrayList<ButtonWrapper> buttons;
    protected View dialogView;
    protected MainActivity main;
    protected EnigmaStateBundle state;

    protected boolean allowIncompleteConnections;
    protected Button positive;

    protected HashSet<Integer> colors;

    protected int previouslyPressedButton = -1;

    /**
     * Constructor that prepares layout and buttons.
     * @param state EnigmaStateBundle from which dialog gets restored and which gets manipulated
     */
    public PluggableDialogBuilder(EnigmaStateBundle state)
    {
        this.state = state;
        main = (MainActivity) MainActivity.ActivitySingleton.getInstance().getActivity();
        initializeLayout();
        setButtonListeners();
    }

    /**
     * Show dialog for the plugboard
     */
    public void showDialogPlugboard()
    {
        allowIncompleteConnections = true;
        restoreConfigurationPlugboard();
        AlertDialog.Builder adb = new AlertDialog.Builder(main);
        adb.setTitle(R.string.title_plugboard_dialog);
        Dialog d = adb.setView(dialogView)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int[] plugs = new int[26];
                        for (int i = 0; i < 26; i++) {
                            plugs[i] = buttons.get(i).getConnectedButton();
                        }
                        state.setConfigurationPlugboard(plugs);
                        main.onDialogFinished(state);
                        Toast.makeText(main.getApplication(), R.string.dialog_plugboard_set, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Toast.makeText(main, R.string.dialog_abort,
                                Toast.LENGTH_SHORT).show();
                    }
                }).create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        d.show();
        d.getWindow().setAttributes(lp);
    }

    /**
     * Show the dialog for the reflector. This can only be positively closed when all
     * connections are made.
     */
    public void showDialogReflector()
    {
        allowIncompleteConnections = false;
        restoreConfigurationReflector();
        AlertDialog.Builder adb = new AlertDialog.Builder(main);
        adb.setTitle(R.string.title_reflector_dialog);
        Dialog d = adb.setView(dialogView)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int[] plugs = new int[26];
                        for (int i = 0; i < 26; i++) {
                            plugs[i] = buttons.get(i).getConnectedButton();
                        }
                        state.setConfigurationReflector(plugs);
                        main.onDialogFinished(state);
                        Toast.makeText(main.getApplication(), R.string.dialog_reflector_set, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Toast.makeText(main, R.string.dialog_abort,
                                Toast.LENGTH_SHORT).show();
                    }
                }).create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        d.show();
        positive = ((AlertDialog)d).getButton(AlertDialog.BUTTON_POSITIVE);
        if(!allConnectionsDone())
        {
            positive.setEnabled(false);
        }
        d.getWindow().setAttributes(lp);
    }

    /**
     * Initialize array of buttons, initialize background-color hashset.
     */
    public void initializeLayout()
    {
        buttons = new ArrayList<>();
        dialogView = View.inflate(main, R.layout.dialog_plugs, null);

        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.A),0));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.B),1));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.C),2));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.D),3));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.E),4));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.F),5));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.G),6));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.H),7));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.I),8));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.J),9));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.K),10));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.L),11));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.M),12));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.N),13));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.O),14));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.P),15));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.Q),16));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.R),17));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.S),18));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.T),19));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.U),20));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.V),21));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.W),22));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.X),23));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.Y),24));
        buttons.add(new ButtonWrapper((Button) dialogView.findViewById(R.id.Z),25));

        colors = new HashSet<>();
        colors.add(R.drawable.button_orange);
        colors.add(R.drawable.button_olive);
        colors.add(R.drawable.button_blue);
        colors.add(R.drawable.button_red);
        colors.add(R.drawable.button_yellow);
        colors.add(R.drawable.button_purple);
        colors.add(R.drawable.button_green);
        colors.add(R.drawable.button_cyan);
        colors.add(R.drawable.button_berry);
        colors.add(R.drawable.button_brown);
        colors.add(R.drawable.button_pink);
        colors.add(R.drawable.button_elder);
        colors.add(R.drawable.button_black);
    }

    /**
     * Set listeners for all buttons
     */
    public void setButtonListeners()
    {
        for(int i=0; i<26; i++)
        {
            Button b = buttons.get(i).getButton();
            final int id = i;
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonPressed(id);
                }
            });
        }
    }

    /**
     * Check, whether all connections are done. If so, return true.
     * return false otherwise
     * @return boolean
     */
    protected boolean allConnectionsDone()
    {
        for(int i=0; i<buttons.size(); i++)
        {
            ButtonWrapper b = buttons.get(i);
            if(b.getConnectedButton() == i || b.getConnectedButton() == -1) return false;
        }
        return true;
    }

    /**
     * restore the connections according to the plugboard
     */
    protected void restoreConfigurationPlugboard()
    {
        restoreConfiguration(state.getConfigurationPlugboard());
    }

    /**
     * restore the connections according to the reflector
     */
    protected void restoreConfigurationReflector()
    {
        restoreConfiguration(state.getConfigurationReflector());
    }

    /**
     * Connect all the buttons according to c.
     * @param c array of connections
     */
    protected void restoreConfiguration(int[] c)
    {
        for(int i=0; i<26; i++)
        {
            setPlug(i, c[i]);
        }
    }

    /**
     * Connect button1 and button2. If two buttons got pressed afterwards and they are not the same
     * they get connected. If one or both of them was connected beforehand, these connections get
     * resolved. If both events come from the same button, the buttons connections (if any) get
     * resolved.
     * @param button1 first and
     * @param button2 second button
     */
    public void setPlug(int button1, int button2)
    {
        if(button1 == button2)
        {
            setButtonFree(buttons.get(button1).getConnectedButton());
            setButtonFree(button1);
        }
        else
        {
            ButtonWrapper b1 = buttons.get(button1);
            ButtonWrapper b2 = buttons.get(button2);

            if (b1.getConnectedButton() != button1) {
                setButtonFree(b1.getConnectedButton());
            }
            if (b2.getConnectedButton() != button2) {
                setButtonFree(b2.getConnectedButton());
            }

            b1.setConnectedButton(button2);
            b2.setConnectedButton(button1);

            int res;
            Iterator<Integer> it = colors.iterator();
            res = it.next();
            if(res == 0) res = it.next();
            colors.remove(res);

            b1.setResourceID(res);
            b2.setResourceID(res);
        }
        updatePositiveButton();
    }

    /**
     * Update state of positive button. Check, if all connections are done and if so, enable positive
     * button. Otherwise disable it.
     */
    protected void updatePositiveButton()
    {
        if(!allowIncompleteConnections && positive != null)
        {
            if(allConnectionsDone()) positive.setEnabled(true);
            else positive.setEnabled(false);
        }
    }

    /**
     * Set buttons to not connected. That includes changing background to grey, set connection to
     * itself.
     * @param b index of button
     */
    private void setButtonFree(int b)
    {
        ButtonWrapper button = buttons.get(b);
        int res = button.getResourceID();
        if(res != R.drawable.button_grey)
            colors.add(button.getResourceID());
        button.setResourceID(R.drawable.button_grey);
        button.setConnectedButton(b);
    }

    /**
     * Handle button pressed events.
     * @param button button that got pressed
     */
    public void buttonPressed(int button)
    {
        if(previouslyPressedButton != -1)
        {
            setPlug(previouslyPressedButton, button);
            previouslyPressedButton = -1;
        }
        else
        {
            previouslyPressedButton = button;
            buttons.get(button).setWaiting();
        }
    }

    /**
     * Wrapper class for Buttons, that also stores the index of both, the connected button, as well
     * as the button itself and resourceID of used material.
     */
    private static class ButtonWrapper
    {
        private Button button;
        private int connectedButton;
        private int resourceID;
        private int index;

        /**
         * Create ButtonWrapper
         * @param button underlying Button
         * @param index index of the button in the buttons array
         */
        public ButtonWrapper(Button button, int index)
        {
            this.button = button;
            this.index = index;
            this.connectedButton = index;
        }

        /**
         * set the index of the connected button and update buttons text
         * @param other index
         */
        public void setConnectedButton(int other)
        {
            this.connectedButton = other;
			Resources res = MainActivity.ActivitySingleton.getInstance().getActivity().getResources();
            this.getButton().setText(String.format(
					res.getText(R.string.button_plug_title).toString(),
					""+(char) (index+65),
					""+(char) (connectedButton+65)));
        }

        /**
         * return index of the connected button
         * @return index of connected button
         */
        public int getConnectedButton()
        {
            return this.connectedButton;
        }

        /**
         * Indicate, that this button is waiting for another button to connect to.
         */
        public void setWaiting()
        {
			Resources res = MainActivity.ActivitySingleton.getInstance().getActivity().getResources();
			this.getButton().setText(String.format(
					res.getText(R.string.button_plug_title).toString(),
					""+(char) (index+65)," "));
        }

        /**
         * Set buttons background and store value in resourceID
         * @param r resourceID of background material
         */
        public void setResourceID(int r)
        {
            button.setBackgroundResource(r);
            this.resourceID = r;
        }

        /**
         * get resourceID of buttons background as store in resourceID
         * @return resourceID
         */
        public int getResourceID()
        {
            return this.resourceID;
        }

        /**
         * Return stored button object
         * @return button
         */
        public Button getButton()
        {
            return button;
        }
    }
}