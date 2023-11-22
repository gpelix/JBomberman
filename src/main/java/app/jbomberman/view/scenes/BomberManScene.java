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
 
package app.jbomberman.view.scenes;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.view.View;

import java.util.function.Supplier;

/**
 * BomberManScene is one of the Scenes of the Application. It's an abstract class
 * that provides an easy way to handle what the screen is currently displaying
 *
 * @author gp
 */
public abstract class BomberManScene
{
	/**
	 * a reference to the view
	 */
	private final View view;

	/**
	 * protected constructor that sets up the reference to the view
	 *
	 * @param view the view
	 */
	protected BomberManScene(View view)
	{
		this.view = view;
	}

	/**
	 * update() should be called regularly by a timer like AnimationTimer in javafx
	 * package. It should be used to update what gets displayed on the screen
	 *
	 * @return the Drawing of the Scene
	 */
	public abstract Drawing update();

	/**
	 * cleanup() must be called before transition to a new scene
	 */
	public abstract void cleanup();


	/**
	 * doTransition allows subclasses to make smooth painless uniform transitions
	 * from one scene to another one
	 * <p>
	 * we pass a supplier in almost all the cases we want to defer
	 * the creation and initialization of the new scene up to the moment after
	 * which we did the cleanup of the previous scene
	 *
	 * @param nextSceneSupplier a supplier of a new scene
	 */
	protected void doTransition(Supplier<BomberManScene> nextSceneSupplier)
	{
		cleanup();
		view.setScene(nextSceneSupplier.get());
	}

	/**
	 * doTransition() takes a sceneConstructor and adapts that to a Supplier
	 * and finally calls doTransition(Supplier)
	 *
	 * @param sceneConstructor the constructor taking a view as parameter of the
	 *                         next scene
	 */
	protected void doTransition(SceneConstructor sceneConstructor)
	{
		doTransition(() -> sceneConstructor.makeANew(view));
	}

	/**
	 * SceneConstructor is a SAM whose only abstract method has the same signature
	 * of the constructors of the scenes
	 */
	@FunctionalInterface
	protected interface SceneConstructor
	{
		/**
		 * makeAnew will be set to the constructor of the scenes
		 *
		 * @param view the view that must be passed to the constructor of the scene
		 * @return the constructed scene
		 */
		BomberManScene makeANew(View view);
	}

	/**
	 * getView() allows subclasses to get the view
	 *
	 * @return the view
	 */
	protected View getView()
	{
		return view;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
