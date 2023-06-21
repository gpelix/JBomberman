module app.jbomberman {
	requires javafx.controls;
	requires java.desktop;
	requires org.json;
	requires org.apache.logging.log4j;
	requires com.google.common;

	exports app.jbomberman;
	exports app.jbomberman.view.scenes;
	exports app.jbomberman.util;
	exports app.jbomberman.model.game.entity;
	exports app.jbomberman.model.game;
	exports app.jbomberman.audio;
	exports app.jbomberman.controller;
	exports app.jbomberman.images;
	exports app.jbomberman.model;
	opens app.jbomberman.view to javafx.graphics;
	exports app.jbomberman.model.game.entity.state;
	exports app.jbomberman.drawing;
	exports app.jbomberman.images.javafx;
	exports app.jbomberman.view;
	exports app.jbomberman.view.graphics.javafx;
	opens app.jbomberman.view.graphics.javafx to javafx.graphics;
	exports app.jbomberman.view.widgets;
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
