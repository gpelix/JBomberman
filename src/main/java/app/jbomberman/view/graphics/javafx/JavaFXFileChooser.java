/*
 * Copyright (c) 2023, Gabriele Pelissetto
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement:
 *      This product includes software developed by Gabriele Pelissetto
 *
 * 4. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY COPYRIGHT HOLDER "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL COPYRIGHT HOLDER BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
package app.jbomberman.view.graphics.javafx;

import app.jbomberman.util.Maybe;
import app.jbomberman.view.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * JavaFXFileChooser is the javafx implementation of the FileChooser
 *
 * @author gp
 */
public class JavaFXFileChooser extends FileChooser
{
	/**
	 * ]
	 * Stage is the stage to which the FileChooser must be attached
	 */
	private final Stage stage;

	/**
	 * JavaFXFileChooser needs to know the stage to which it must be attached
	 *
	 * @param stage the stage to which the FileChooser must be attached
	 */
	/*~*/JavaFXFileChooser(Stage stage)
	{
		this.stage = stage;
	}

	/**
	 * getFile() is the method that must be called to wait for the user selecting
	 * the file
	 *
	 * @return the file that the user inserted. It's a Maybe as the user might
	 * have pressed exit without selecting any file at all
	 */
	@Override
	public Maybe<File> getFile()
	{
		javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

		File f = fileChooser.showOpenDialog(stage);

		return f == null ? Maybe.nothing() : Maybe.just(f);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
