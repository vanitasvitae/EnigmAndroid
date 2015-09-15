package de.vanitasvitae.enigmandroid.enigma;

/**
 * Class that contains a possible state of an enigma machine.
 * Used to store and transport configuration and settings.
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
public class EnigmaStateBundle{
    private int typeEntryWheel;

    private int typeRotor1;
    private int typeRotor2;
    private int typeRotor3;
    private int typeRotor4;
    private int typeRotor5;
    private int typeRotor6;
    private int typeRotor7;
    private int typeRotor8;

    private int rotationRotor1;
    private int rotationRotor2;
    private int rotationRotor3;
    private int rotationRotor4;
    private int rotationRotor5;
    private int rotationRotor6;
    private int rotationRotor7;
    private int rotationRotor8;

    private int ringSettingRotor1;
    private int ringSettingRotor2;
    private int ringSettingRotor3;
    private int ringSettingRotor4;
    private int ringSettingRotor5;
    private int ringSettingRotor6;
    private int ringSettingRotor7;
    private int ringSettingRotor8;

    private int typeReflector;

    private int rotationReflector;
    private int ringSettingReflector;

    private int[] configurationPlugboard;

    private int[] configurationReflector;

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

    public int getTypeRotor5() {
        return typeRotor5;
    }

    public void setTypeRotor5(int typeRotor5) {
        this.typeRotor5 = typeRotor5;
    }

    public int getTypeRotor6() {
        return typeRotor6;
    }

    public void setTypeRotor6(int typeRotor6) {
        this.typeRotor6 = typeRotor6;
    }

    public int getTypeRotor7() {
        return typeRotor7;
    }

    public void setTypeRotor7(int typeRotor7) {
        this.typeRotor7 = typeRotor7;
    }

    public int getTypeRotor8() {
        return typeRotor8;
    }

    public void setTypeRotor8(int typeRotor8) {
        this.typeRotor8 = typeRotor8;
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

    public int getRotationRotor5() {
        return rotationRotor5;
    }

    public void setRotationRotor5(int rotationRotor5) {
        this.rotationRotor5 = rotationRotor5;
    }

    public int getRotationRotor6() {
        return rotationRotor6;
    }

    public void setRotationRotor6(int rotationRotor6) {
        this.rotationRotor6 = rotationRotor6;
    }

    public int getRotationRotor7() {
        return rotationRotor7;
    }

    public void setRotationRotor7(int rotationRotor7) {
        this.rotationRotor7 = rotationRotor7;
    }

    public int getRotationRotor8() {
        return rotationRotor8;
    }

    public void setRotationRotor8(int rotationRotor8) {
        this.rotationRotor8 = rotationRotor8;
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

    public int getRingSettingRotor5() {
        return ringSettingRotor5;
    }

    public void setRingSettingRotor5(int ringSettingRotor5) {
        this.ringSettingRotor5 = ringSettingRotor5;
    }

    public int getRingSettingRotor6() {
        return ringSettingRotor6;
    }

    public void setRingSettingRotor6(int ringSettingRotor6) {
        this.ringSettingRotor6 = ringSettingRotor6;
    }

    public int getRingSettingRotor7() {
        return ringSettingRotor7;
    }

    public void setRingSettingRotor7(int ringSettingRotor7) {
        this.ringSettingRotor7 = ringSettingRotor7;
    }

    public int getRingSettingRotor8() {
        return ringSettingRotor8;
    }

    public void setRingSettingRotor8(int ringSettingRotor8) {
        this.ringSettingRotor8 = ringSettingRotor8;
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
