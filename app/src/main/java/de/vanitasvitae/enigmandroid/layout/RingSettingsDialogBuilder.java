package de.vanitasvitae.enigmandroid.layout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
public abstract class RingSettingsDialogBuilder
{
    protected abstract void showDialog(EnigmaStateBundle stateBundle, ArrayAdapter[] adapters, int[] rIDs, Actions actions);
    public abstract void createRingSettingsDialog(EnigmaStateBundle stateBundle);

    public static ArrayAdapter createAdapter(Integer[] array)
    {
        ArrayAdapter adapter = new ArrayAdapter<>(
                MainActivity.ActivitySingleton.getInstance().getActivity(),
                android.R.layout.simple_spinner_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public static ArrayAdapter createAdapter1_26()
    {
        Integer[] ringArray = new Integer[26];
        for(int i=1; i<=26; i++) {ringArray[i-1] = i;}
        return createAdapter(ringArray);
    }



    public static class RingSettingsDialogBuilderRotRotRot extends RingSettingsDialogBuilder
    {
        public void createRingSettingsDialog(final EnigmaStateBundle state)
        {
            this.showDialog(state,
                    new ArrayAdapter[]{
                            createAdapter1_26(),
                            createAdapter1_26(),
                            createAdapter1_26()},
                    new int[]{
                            R.string.hint_rotor1,
                            R.string.hint_rotor2,
                            R.string.hint_rotor3},
                    new Actions3(state) {
                        @Override
                        protected void firstSpinnerItemSelected(int pos) {
                            state.setRingSettingRotor1(pos);
                        }

                        @Override
                        protected void secondSpinnerItemSelected(int pos) {
                            state.setRingSettingRotor2(pos);
                        }

                        @Override
                        protected void thirdSpinnerItemSelected(int pos) {
                            state.setRingSettingRotor3(pos);
                        }

                        @Override
                        protected int getFirstValueFromBundle() {
                            return state.getRingSettingRotor1();
                        }

                        @Override
                        protected int getSecondValueFromBundle() {
                            return state.getRingSettingRotor2();
                        }

                        @Override
                        protected int getThirdValueFromBundle() {
                            return state.getRingSettingRotor3();
                        }
                    });
        }

        @Override
        protected void showDialog(final EnigmaStateBundle stateBundle, ArrayAdapter[] adapters, int[] rIDs, Actions actions)
        {
            if(adapters.length != 3 || rIDs.length != 3)
            {
                Log.d("Enigm|RingSettings", "Length of adapters array or length of rIDs array not equal to 3!");
            }
            final Actions3 action = (Actions3) actions;
            final MainActivity main = (MainActivity) MainActivity.ActivitySingleton.getInstance().getActivity();
            View ringSettingsView = View.inflate(main, R.layout.dialog_ringsettings_3, null);

            TextView ring1Title = (TextView) ringSettingsView.findViewById(R.id.dialog_text_rotor1);
            ring1Title.setText(rIDs[0]);
            TextView ring2Title = (TextView) ringSettingsView.findViewById(R.id.dialog_text_rotor2);
            ring2Title.setText(rIDs[1]);
            TextView ring3Title = (TextView) ringSettingsView.findViewById(R.id.dialog_text_rotor3);
            ring3Title.setText(rIDs[2]);

            final Spinner ring1 = (Spinner) ringSettingsView.findViewById(R.id.rotor1ring);
            ring1.setAdapter(adapters[0]);
            ring1.setSelection(action.getFirstValueFromBundle());

            final Spinner ring2 = (Spinner) ringSettingsView.findViewById(R.id.rotor2ring);
            ring2.setAdapter(adapters[1]);
            ring2.setSelection(action.getSecondValueFromBundle());

            final Spinner ring3 = (Spinner) ringSettingsView.findViewById(R.id.rotor3ring);
            ring3.setAdapter(adapters[2]);
            ring3.setSelection(action.getThirdValueFromBundle());

            AlertDialog.Builder builder = new AlertDialog.Builder(main);
            builder.setTitle(R.string.title_ringsetting);
            builder.setView(ringSettingsView)
                    .setCancelable(true)
                    .setPositiveButton(R.string.dialog_positiv, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            action.firstSpinnerItemSelected(ring1.getSelectedItemPosition());
                            action.secondSpinnerItemSelected(ring2.getSelectedItemPosition());
                            action.thirdSpinnerItemSelected(ring3.getSelectedItemPosition());
                            String message = main.getResources().getString(
                                    R.string.dialog_ringsettings_success) + " " +
                                    (ring1.getSelectedItemPosition()+1) + ", " +
                                    (ring2.getSelectedItemPosition()+1) + ", " +
                                    (ring3.getSelectedItemPosition()+1) + ".";
                            main.onDialogFinished(stateBundle);
                            Toast.makeText(main, message, Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton(R.string.dialog_negativ, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Toast.makeText(main, R.string.dialog_abort,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).show();
        }
    }

    public static class RingSettingsDialogBuilderRotRotRotRef extends RingSettingsDialogBuilder
    {
        @Override
        public void createRingSettingsDialog(final EnigmaStateBundle state) {
            this.showDialog(state,
                    new ArrayAdapter[]{
                            createAdapter1_26(),
                            createAdapter1_26(),
                            createAdapter1_26(),
                            createAdapter1_26()},
                    new int[]{
                            R.string.hint_rotor1,
                            R.string.hint_rotor2,
                            R.string.hint_rotor3,
                            R.string.hint_reflector},
                    new Actions4(state) {

                        @Override
                        protected void firstSpinnerItemSelected(int pos) {
                            state.setRingSettingRotor1(pos);
                        }

                        @Override
                        protected void secondSpinnerItemSelected(int pos) {
                            state.setRingSettingRotor2(pos);
                        }

                        @Override
                        protected void thirdSpinnerItemSelected(int pos) {
                            state.setRingSettingRotor3(pos);
                        }

                        @Override
                        protected int getFirstValueFromBundle() {
                            return state.getRingSettingRotor1();
                        }

                        @Override
                        protected int getSecondValueFromBundle() {
                            return state.getRingSettingRotor2();
                        }

                        @Override
                        protected int getThirdValueFromBundle() {
                            return state.getRingSettingRotor3();
                        }

                        @Override
                        protected void fourthSpinnerItemSelected(int pos) {
                            state.setRingSettingReflector(pos);
                        }

                        @Override
                        protected int getFourthValueFromBundle() {
                            return state.getRingSettingReflector();
                        }
                    });
        }
        @Override
        protected void showDialog(final EnigmaStateBundle stateBundle, ArrayAdapter[] adapters, int[] rIDs, Actions actions) {
            if(adapters.length != 4 || rIDs.length != 4)
            {
                Log.d("Enigm|RingSettings", "Length of adapters array or length of rIDs array not equal to 4!");
            }
            final Actions4 action = (Actions4) actions;
            final MainActivity main = (MainActivity) MainActivity.ActivitySingleton.getInstance().getActivity();
            View ringSettingsView = View.inflate(main, R.layout.dialog_ringsettings_4, null);

            TextView ring1Title = (TextView) ringSettingsView.findViewById(R.id.dialog_text_rotor1);
            ring1Title.setText(rIDs[0]);
            TextView ring2Title = (TextView) ringSettingsView.findViewById(R.id.dialog_text_rotor2);
            ring2Title.setText(rIDs[1]);
            TextView ring3Title = (TextView) ringSettingsView.findViewById(R.id.dialog_text_rotor3);
            ring3Title.setText(rIDs[2]);
            TextView ring4Title = (TextView) ringSettingsView.findViewById(R.id.dialog_text_rotor4);
            ring4Title.setText(rIDs[3]);

            final Spinner ring1 = (Spinner) ringSettingsView.findViewById(R.id.rotor1ring);
            ring1.setAdapter(adapters[0]);
            ring1.setSelection(action.getFirstValueFromBundle());

            final Spinner ring2 = (Spinner) ringSettingsView.findViewById(R.id.rotor2ring);
            ring2.setAdapter(adapters[1]);
            ring2.setSelection(action.getSecondValueFromBundle());

            final Spinner ring3 = (Spinner) ringSettingsView.findViewById(R.id.rotor3ring);
            ring3.setAdapter(adapters[2]);
            ring3.setSelection(action.getThirdValueFromBundle());

            final Spinner ring4 = (Spinner) ringSettingsView.findViewById(R.id.rotor4ring);
            ring4.setAdapter(adapters[3]);
            ring4.setSelection(action.getFourthValueFromBundle());

            AlertDialog.Builder builder = new AlertDialog.Builder(main);
            builder.setTitle(R.string.title_ringsetting);
            builder.setView(ringSettingsView)
                    .setCancelable(true)
                    .setPositiveButton(R.string.dialog_positiv, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            action.firstSpinnerItemSelected(ring1.getSelectedItemPosition());
                            action.secondSpinnerItemSelected(ring2.getSelectedItemPosition());
                            action.thirdSpinnerItemSelected(ring3.getSelectedItemPosition());
                            action.fourthSpinnerItemSelected(ring4.getSelectedItemPosition());
                            String message = main.getResources().getString(
                                    R.string.dialog_ringsettings_success) + " " +
                                    (ring1.getSelectedItemPosition()+1) + ", " +
                                    (ring2.getSelectedItemPosition()+1) + ", " +
                                    (ring3.getSelectedItemPosition()+1) + ", " +
                                    (ring4.getSelectedItemPosition()+1) + ".";
                            main.onDialogFinished(stateBundle);
                            Toast.makeText(main, message, Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton(R.string.dialog_negativ, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Toast.makeText(main, R.string.dialog_abort,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).show();
        }
    }

    public static class RingSettingsDialogBuilderRotRotRotRot extends RingSettingsDialogBuilderRotRotRotRef
    {
        @Override
        public void createRingSettingsDialog(final EnigmaStateBundle state) {
            this.showDialog(state,
                    new ArrayAdapter[]{
                            createAdapter1_26(),
                            createAdapter1_26(),
                            createAdapter1_26(),
                            createAdapter1_26()},
                    new int[]{
                            R.string.hint_rotor1,
                            R.string.hint_rotor2,
                            R.string.hint_rotor3,
                            R.string.hint_thin_rotor},
                    new Actions4(state) {

                        @Override
                        protected void firstSpinnerItemSelected(int pos) {
                            state.setRingSettingRotor1(pos);
                        }

                        @Override
                        protected void secondSpinnerItemSelected(int pos) {
                            state.setRingSettingRotor2(pos);
                        }

                        @Override
                        protected void thirdSpinnerItemSelected(int pos) {
                            state.setRingSettingRotor3(pos);
                        }

                        @Override
                        protected int getFirstValueFromBundle() {
                            return state.getRingSettingRotor1();
                        }

                        @Override
                        protected int getSecondValueFromBundle() {
                            return state.getRingSettingRotor2();
                        }

                        @Override
                        protected int getThirdValueFromBundle() {
                            return state.getRingSettingRotor3();
                        }

                        @Override
                        protected void fourthSpinnerItemSelected(int pos) {
                            state.setRingSettingRotor4(pos);
                        }

                        @Override
                        protected int getFourthValueFromBundle() {
                            return state.getRingSettingRotor4();
                        }
                    });
        }
    }

    public static abstract class Actions
    {
        protected EnigmaStateBundle stateBundle;
        public Actions(EnigmaStateBundle bundle)
        {
            this.stateBundle = bundle;
        }
    }

    public static abstract class Actions3 extends Actions
    {
        public Actions3(EnigmaStateBundle bundle)
        {
            super(bundle);
        }
        protected abstract void firstSpinnerItemSelected(int pos);
        protected abstract void secondSpinnerItemSelected(int pos);
        protected abstract void thirdSpinnerItemSelected(int pos);
        protected abstract int getFirstValueFromBundle();
        protected abstract int getSecondValueFromBundle();
        protected abstract int getThirdValueFromBundle();
    }

    public static abstract class Actions4 extends Actions3
    {
        public Actions4(EnigmaStateBundle bundle)
        {
            super(bundle);
        }
        protected abstract void fourthSpinnerItemSelected(int pos);
        protected abstract int getFourthValueFromBundle();
    }
}