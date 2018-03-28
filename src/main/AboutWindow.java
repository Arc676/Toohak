//Written by Alessandro Vinciguerra <alesvinciguerra@gmail.com>
//Copyright (C) 2017  Arc676/Alessandro Vinciguerra

//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation (version 3).

//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.

//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.
//See README.txt and LICENSE.txt for more details

package main;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Window containing legal information and some
 * information about the project.
 * @author Ale
 *
 */
public class AboutWindow extends JFrame {

	private static final long serialVersionUID = -5929273713052488865L;
	
	public AboutWindow() {
		setTitle("About Toohak v1.0");
		setBounds(100, 100, 600, 400);
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		JScrollPane scroll = new JScrollPane();
		JTextArea text = new JTextArea();
		text.setText("Created by Arc676/Alessandro Vinciguerra <alesvinciguerra@gmail.com>\n\n" +
				"Project available under GPLv3\n" + 
				"Copyright (C) 2017  Arc676/Alessandro Vinciguerra\nBased on work by Matthew Chen and Arc676/Alessandro Vinciguerra based on MIT License\n\n" +
				"This program is free software: you can redistribute it and/or modify\n" + 
				"it under the terms of the GNU General Public License as published by\n" + 
				"the Free Software Foundation (version 3)\n\n" +
				"This program is distributed in the hope that it will be useful,\n" + 
				"but WITHOUT ANY WARRANTY; without even the implied warranty of\n" + 
				"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" + 
				"GNU General Public License for more details.\n\n" +
				"You should have received a copy of the GNU General Public License\n" + 
				"along with this program.  If not, see <http://www.gnu.org/licenses/>.\n\n" +
				"MIT License:\n\nPermission is hereby granted, free of charge, to any person obtaining a copy\n" + 
				"of this software and associated documentation files (the \"Software\"), to deal\n" + 
				"in the Software without restriction, including without limitation the rights\n" + 
				"to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n" + 
				"copies of the Software, and to permit persons to whom the Software is\n" + 
				"furnished to do so, subject to the following conditions:\n\n" +
				"The above copyright notice and this permission notice shall be included in all\n" + 
				"copies or substantial portions of the Software.\n\n" +
				"THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" + 
				"IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" + 
				"FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" + 
				"AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" + 
				"LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n" + 
				"OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\n" + 
				"SOFTWARE.\n\n" +
				"Toohak uses the Apache Commons CSV library. This library is available under\n" +
				"the Apache 2.0 license. See 'APACHE LICENSE.txt' for the full license text.\n\n" +
				"Music available under CC0.");
		text.setEditable(false);
		scroll.setViewportView(text);
		getContentPane().add(scroll);
	}

}