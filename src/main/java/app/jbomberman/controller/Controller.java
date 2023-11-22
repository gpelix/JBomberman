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
 
package app.jbomberman.controller;

import app.jbomberman.Properties;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.image.FlightWeightImagesFactory;
import app.jbomberman.image.javafx.JavaFXImageBuilder;
import app.jbomberman.image.swing.SwingImageBuilder;
import app.jbomberman.model.Model;
import app.jbomberman.util.Observer;
import app.jbomberman.view.View;
import app.jbomberman.view.graphics.javafx.JavaFXView;
import app.jbomberman.view.graphics.swing.SwingView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controller is the controller class in the MVC pattern on which the program
 * is based. The controller is responsible for the communication between the graphic
 * layer and the model in the application. It also sets up the View and the View
 *
 * @author gp
 * @see Model
 * @see View
 */
public class Controller
{
	/**
	 * view is the View in our program
	 */
	private final View view;
	/**
	 * logger is an instance of a logger for the Controller
	 */
	private final Logger logger;

	/**
	 * the controller. We don't want to use the singleton pattern as there is no
	 * way to initialize it but at the start of the program
	 */
	public static Controller controller;

	/**
	 * Controller() is the only constructor of the controller. It must not be called
	 * by other classes, as, to do the startup, the static function Controller.launch()
	 * must be called
	 *
	 * @param view the newly created view
	 */
	private Controller(View view)
	{
		controller = this;

		logger = LogManager.getLogger(getClass());

		this.view = view;

		Model.setController(this);
//		Model.getInstance().attachObserver(view);

		view.setup();

		view.getGraphicFactory().getLoopTimer(view::update).start();

		view.setTitle(Properties.getInstance().getApplicationName());
		view.setCloseOperationCallBack(Model.getInstance()::close);
		view.setIcon(Properties.getInstance().getIconFileName());
	}

	/**
	 * close() function must be used during the shutdown process of the
	 * application
	 * <p>
	 * it closes the view and stops the AudioPlayer. Not closing the
	 * audio player will prevent the JVM from exiting
	 */
	public void close()
	{
		logger.info("Closing Controller");
		AudioPlayer.getInstance().close();
		view.close();
	}

	/**
	 * attachKeyBoardObserver() must be used to attach an observer to the
	 * keyBoardController
	 *
	 * @param observer Observer of the controller
	 * @see Observer
	 */
	public void attachKeyBoardObserver(Observer observer)
	{
		view.getKeyBoardController().attachObserver(observer);
	}

	/**
	 * detachKeyBoardObserver() must be used to detach an observer from the
	 * keyBoardController
	 *
	 * @param keyBoardObserver the observer of the KeyDoardController
	 * @see Observer
	 */
	public void detachKeyBoardObserver(Observer keyBoardObserver)
	{
		view.getKeyBoardController().detachObserver(keyBoardObserver);
	}

	/**
	 * launch() is the function that must be called during the bootstrap process
	 * to initialize the Graphic library and then initialize the controller
	 */
	public static void launch()
	{
		if (Properties.getInstance().getGraphicLibrary() == Properties.GraphicLibrary.JAVAFX) {
			FlightWeightImagesFactory.initializeImageBuilder(new JavaFXImageBuilder());
			JavaFXView.initialize(() -> new Controller(new JavaFXView()));
		} else {
			FlightWeightImagesFactory.initializeImageBuilder(new SwingImageBuilder());
			new Controller(new SwingView());
		}
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
