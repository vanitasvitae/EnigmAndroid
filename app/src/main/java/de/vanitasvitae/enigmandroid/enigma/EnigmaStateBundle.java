/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.vanitasvitae.enigmandroid.enigma;

/**
 * Class that contains a possible state of an enigma machine.
 * Used to store and transport configuration and settings.
 * Copyright (C) 2015  Paul Schaub
 */
public class EnigmaStateBundle{
    private String machineType;

    private int typeEntryWheel;

    private int typeRotor1;
    private int typeRotor2;
    private int typeRotor3;
    private int typeRotor4;

    private int rotationRotor1;
    private int rotationRotor2;
    private int rotationRotor3;
    private int rotationRotor4;

    private int ringSettingRotor1;
    private int ringSettingRotor2;
    private int ringSettingRotor3;
    private int ringSettingRotor4;

    private int typeReflector;

    private int rotationReflector;
    private int ringSettingReflector;

    private int[] configurationPlugboard;

    private int[] configurationReflector;

// --Commented out by Inspection START (07.11.15 19:46):
//    public String getMachineType()
//    {
//        return this.machineType;
//    }
// --Commented out by Inspection STOP (07.11.15 19:46)

// --Commented out by Inspection START (07.11.15 19:46):
//    public void setMachineType(String type)
//    {
//        this.machineType = type;
//    }
// --Commented out by Inspection STOP (07.11.15 19:46)

    public int getTypeRotor1() {
        return typeRotor1;
    }

    public void setTypeRotor1(int typeRotor1) {
        this.typeRotor1 = typeRotor1;
    }

    public int getTypeRotor2() {
        return typeRotor2;
    }

    public void setTypeRotor2(int typeRotor2) {
        this.typeRotor2 = typeRotor2;
    }

    public int getTypeRotor3() {
        return typeRotor3;
    }

    public void setTypeRotor3(int typeRotor3) {
        this.typeRotor3 = typeRotor3;
    }

    public int getTypeRotor4() {
        return typeRotor4;
    }

    public void setTypeRotor4(int typeRotor4) {
        this.typeRotor4 = typeRotor4;
    }

    public int getRotationRotor1() {
        return rotationRotor1;
    }

    public void setRotationRotor1(int rotationRotor1) {
        this.rotationRotor1 = rotationRotor1;
    }

    public int getRotationRotor2() {
        return rotationRotor2;
    }

    public void setRotationRotor2(int rotationRotor2) {
        this.rotationRotor2 = rotationRotor2;
    }

    public int getRotationRotor3() {
        return rotationRotor3;
    }

    public void setRotationRotor3(int rotationRotor3) {
        this.rotationRotor3 = rotationRotor3;
    }

    public int getRotationRotor4() {
        return rotationRotor4;
    }

    public void setRotationRotor4(int rotationRotor4) {
        this.rotationRotor4 = rotationRotor4;
    }

    public int getRingSettingRotor1() {
        return ringSettingRotor1;
    }

    public void setRingSettingRotor1(int ringSettingRotor1) {
        this.ringSettingRotor1 = ringSettingRotor1;
    }

    public int getRingSettingRotor2() {
        return ringSettingRotor2;
    }

    public void setRingSettingRotor2(int ringSettingRotor2) {
        this.ringSettingRotor2 = ringSettingRotor2;
    }

    public int getRingSettingRotor3() {
        return ringSettingRotor3;
    }

    public void setRingSettingRotor3(int ringSettingRotor3) {
        this.ringSettingRotor3 = ringSettingRotor3;
    }

    public int getRingSettingRotor4() {
        return ringSettingRotor4;
    }

    public void setRingSettingRotor4(int ringSettingRotor4) {
        this.ringSettingRotor4 = ringSettingRotor4;
    }

    public int getTypeReflector() {
        return typeReflector;
    }

    public void setTypeReflector(int typeReflector) {
        this.typeReflector = typeReflector;
    }

    public int getTypeEntryWheel() {
        return typeEntryWheel;
    }

    public void setTypeEntryWheel(int typeEntryWheel) {
        this.typeEntryWheel = typeEntryWheel;
    }

    public int[] getConfigurationPlugboard() {
        return configurationPlugboard;
    }

    public void setConfigurationPlugboard(int[] configurationPlugboard) {
        this.configurationPlugboard = configurationPlugboard;
    }

    public int[] getConfigurationReflector() {
        return configurationReflector;
    }

    public void setConfigurationReflector(int[] configurationReflector) {
        this.configurationReflector = configurationReflector;
    }

    public int getRotationReflector() {
        return rotationReflector;
    }

    public void setRotationReflector(int rotationReflector) {
        this.rotationReflector = rotationReflector;
    }

    public int getRingSettingReflector() {
        return ringSettingReflector;
    }

    public void setRingSettingReflector(int ringSettingReflector) {
        this.ringSettingReflector = ringSettingReflector;
    }
}
