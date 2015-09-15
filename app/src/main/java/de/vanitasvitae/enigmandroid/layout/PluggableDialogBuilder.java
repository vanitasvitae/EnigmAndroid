package de.vanitasvitae.enigmandroid.layout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import de.vanitasvitae.enigmandroid.MainActivity;
import de.vanitasvitae.enigmandroid.R;
import de.vanitasvitae.enigmandroid.enigma.EnigmaStateBundle;

/**
 * Builder for the dialog that is used to get settings for the rings
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
    protected ArrayList<Button> buttons;
    protected View dialogView;
    protected MainActivity main;
    protected EnigmaStateBundle state;
    protected Drawable defaultLayout;

    protected int previouslyPressedButton = -1;

    public PluggableDialogBuilder(EnigmaStateBundle state)
    {
        this.state = state;
        main = (MainActivity) MainActivity.ActivitySingleton.getInstance().getActivity();
        initializeLayout();
        setButtonListeners();
    }

    public void showDialogPlugboard()
    {
        restoreConfigurationPlugboard();
        AlertDialog.Builder adb = new AlertDialog.Builder(main);
        adb.setTitle(R.string.title_plugboard_dialog);
        Dialog d = adb.setView(dialogView)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_positiv, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int[] plugs = new int[26];
                        for (int i = 0; i < 26; i++) {
                            int o = getConnectedButton(i);
                            if (o == -1) plugs[i] = i;
                            else plugs[i] = o;
                        }
                        state.setConfigurationPlugboard(plugs);
                        Toast.makeText(main.getApplication(), R.string.dialog_plugboard_set, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.dialog_negativ, new DialogInterface.OnClickListener() {
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

    public void showDialogReflector()
    {
        restoreConfigurationReflector();
        AlertDialog.Builder adb = new AlertDialog.Builder(main);
        adb.setTitle(R.string.title_reflector_dialog);
        Dialog d = adb.setView(dialogView)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_positiv, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int[] plugs = new int[26];
                        for (int i = 0; i < 26; i++) {
                            int o = getConnectedButton(i);
                            if (o == -1) plugs[i] = i;
                            else plugs[i] = o;
                        }
                        state.setConfigurationReflector(plugs);
                        Toast.makeText(main.getApplication(), R.string.dialog_reflector_set, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.dialog_negativ, new DialogInterface.OnClickListener() {
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
    public void initializeLayout()
    {
        buttons = new ArrayList<>();
        dialogView = View.inflate(main, R.layout.dialog_plugs, null);

        buttons.add((Button) dialogView.findViewById(R.id.A));
        buttons.add((Button) dialogView.findViewById(R.id.B));
        buttons.add((Button) dialogView.findViewById(R.id.C));
        buttons.add((Button) dialogView.findViewById(R.id.D));
        buttons.add((Button) dialogView.findViewById(R.id.E));
        buttons.add((Button) dialogView.findViewById(R.id.F));
        buttons.add((Button) dialogView.findViewById(R.id.G));
        buttons.add((Button) dialogView.findViewById(R.id.H));
        buttons.add((Button) dialogView.findViewById(R.id.I));
        buttons.add((Button) dialogView.findViewById(R.id.J));
        buttons.add((Button) dialogView.findViewById(R.id.K));
        buttons.add((Button) dialogView.findViewById(R.id.L));
        buttons.add((Button) dialogView.findViewById(R.id.M));
        buttons.add((Button) dialogView.findViewById(R.id.N));
        buttons.add((Button) dialogView.findViewById(R.id.O));
        buttons.add((Button) dialogView.findViewById(R.id.P));
        buttons.add((Button) dialogView.findViewById(R.id.Q));
        buttons.add((Button) dialogView.findViewById(R.id.R));
        buttons.add((Button) dialogView.findViewById(R.id.S));
        buttons.add((Button) dialogView.findViewById(R.id.T));
        buttons.add((Button) dialogView.findViewById(R.id.U));
        buttons.add((Button) dialogView.findViewById(R.id.V));
        buttons.add((Button) dialogView.findViewById(R.id.W));
        buttons.add((Button) dialogView.findViewById(R.id.X));
        buttons.add((Button) dialogView.findViewById(R.id.Y));
        buttons.add((Button) dialogView.findViewById(R.id.Z));
        this.defaultLayout = buttons.get(0).getBackground();
    }

    public void setButtonListeners()
    {
        for(int i=0; i<26; i++)
        {
            Button b = buttons.get(i);
            final int id = i;
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonPressed(id);
                }
            });
        }
    }

    protected void restoreConfigurationPlugboard()
    {
        restoreConfiguration(state.getConfigurationPlugboard());
    }

    protected void restoreConfigurationReflector()
    {
        restoreConfiguration(state.getConfigurationReflector());
    }

    protected void restoreConfiguration(int[] c)
    {
        for(int i=0; i<26; i++)
        {
            int o = c[i];
            if(i != o)
                setPlug(i, o);
        }
    }

    public void setPlug(int button1, int button2)
    {
        if(button1 != button2)
        {
            Button b1 = buttons.get(button1);
            Button b2 = buttons.get(button2);

            int other = getConnectedButton(button1);
            if(other != -1) setButtonFree(other);
            other = getConnectedButton(button2);
            if(other != -1) setButtonFree(other);

            b1.setText((char) (button1 + 65) + ":" + (char) (button2 + 65));
            setButtonUsed(button1);
            b2.setText((char) (button2 + 65) + ":" + (char) (button1 + 65));
            setButtonUsed(button2);
        }
        else
        {
            int other = getConnectedButton(button1);
            if(other != -1) setButtonFree(other);
            setButtonFree(button1);
        }
    }

    private int getConnectedButton(int button)
    {
        Button b = buttons.get(button);
        char c = (b.getText().charAt(2));
        if(c != ' ')
        {
            return ((int) c) - 65;
        }
        else return -1;
    }

    public void buttonPressed(int button)
    {
        if(previouslyPressedButton == -1)
        {
            previouslyPressedButton = button;
            setButtonUsed(button);
        }
        else
        {
            setPlug(previouslyPressedButton, button);
            previouslyPressedButton = -1;
        }
    }

    private void setButtonUsed(int button)
    {
        buttons.get(button).setBackgroundResource(R.drawable.button_orange);
    }
    private void setButtonFree(int button)
    {
        Button b = buttons.get(button);
        b.setBackgroundResource(R.drawable.button_grey);
        //b.setBackground(defaultLayout);
        b.setText((char) (button+65) + ": ");
    }

}