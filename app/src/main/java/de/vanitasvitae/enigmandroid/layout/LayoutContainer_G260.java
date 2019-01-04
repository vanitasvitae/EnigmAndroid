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
package de.vanitasvitae.enigmandroid.layout;

import de.vanitasvitae.enigmandroid.enigma.Enigma_G260;

/**
 * LayoutContainer for the Enigma Model G260
 * This class contains the layout and controls the layout elements such as spinners and stuff
 * Copyright (C) 2015  Paul Schaub
 */
public class LayoutContainer_G260 extends LayoutContainer_G31
{
	public LayoutContainer_G260()
	{
		super();
		main.setTitle("G260 - EnigmAndroid");
		this.resetLayout();
	}

	@Override
	public void resetLayout() {
		enigma = new Enigma_G260();
		setLayoutState(enigma.getState());
		output.setText("");
		input.setText("");
	}
}
