package de.vanitasvitae.enigmandroid.layout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.vanitasvitae.enigmandroid.MainActivity;
import de.vanitasvitae.enigmandroid.R;

/**
 * Builder for the dialog that is used to obtain a passphrase to generate
 * a enigma configuration from it.
 * Alternatively the user can enter the content String from a EnigmAndroid QR-Code here.
 * That would have the same effect as scanning the QR-Code.
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
public class PassphraseDialogBuilder
{
    private MainActivity main;
    private View passphraseDialogView;
    private EditText passphrase;
    private Button positive;
    public PassphraseDialogBuilder()
    {
        main = (MainActivity) MainActivity.ActivitySingleton.getInstance().getActivity();
        passphraseDialogView = View.inflate(main, R.layout.dialog_passphrase, null);
        passphrase = (EditText) passphraseDialogView.findViewById(R.id.passphrase);
        passphrase.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Count input text and enable positive button if length > 0.
                //Disable else
                if(s.length() > 0) positive.setEnabled(true);
                else positive.setEnabled(false);
            }
        });
    }

    /**
     * create and show the dialog
     */
    public void showDialog()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(main);
        builder.setTitle(R.string.hint_passphrase);
        Dialog d = builder.setView(passphraseDialogView)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        String pass = passphrase.getText().toString();
                        if(pass.startsWith(MainActivity.APP_ID+"/"))
						{
							main.restoreStateFromCode(pass);
							Toast.makeText(main, R.string.dialog_passphrase_was_coded_state, Toast.LENGTH_LONG).show();
						}
						else
						{
							main.createStateFromSeed(pass);
                            String message = String.format(main.getResources().getString(
                                    R.string.dialog_passphrase_set), " \'"+pass+"\'");
							Toast.makeText(main, message, Toast.LENGTH_LONG).show();
						}
                    }
                })
                .setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Toast.makeText(main, R.string.dialog_abort,
                                Toast.LENGTH_SHORT).show();
                    }
                }).create();
        d.show();
        positive = ((AlertDialog)d).getButton(AlertDialog.BUTTON_POSITIVE);
        positive.setEnabled(false);
    }
}