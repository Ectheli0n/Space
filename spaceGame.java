package application;

import java.util.ArrayList;
import java.util.Collection;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

public class spaceGame {

	private Stage _stage;
	private Scene _scene;
	private Group _root;

	private Ellipse _falcon;

	private Timeline _timeline;

	private boolean _gameOver = false;

	private ArrayList<Laser> _lasers = new ArrayList<Laser>();
	private ArrayList<Asteroids> _asts = new ArrayList<Asteroids>();

	private EventHandler<ActionEvent> _handleAst;

	private Shape _collShip;

	Boolean _collast;

	private Image explosion = new Image("application/explosion.png");
	private ImagePattern _explo = new ImagePattern(explosion);

	private Image shipMoving = new Image("application/FauconMoving.png");
	private ImagePattern _shipM = new ImagePattern(shipMoving);

	private Image shipNotMoving = new Image("application/FauconNotMoving.png");
	private ImagePattern _shipNM = new ImagePattern(shipNotMoving);

	private Image asteroid = new Image("application/asteroid.png");
	private ImagePattern _ast = new ImagePattern(asteroid);

	private Image font = new Image("application/spaceFont.png");
	private ImagePattern _font = new ImagePattern(font);

	public spaceGame(Stage primaryStage) {

		_falcon = new Ellipse();
		_falcon.setCenterX(300);
		_falcon.setCenterY(150);
		_falcon.setRadiusX(60);
		_falcon.setRadiusY(35);
		_falcon.setFill(_shipNM);

		_root = new Group();
		_root.getChildren().add(_falcon);

		_scene = new Scene(_root, 1000, 500);
		_scene.setFill(_font);
		_stage = primaryStage;
		_stage.setTitle("Game");
		_stage.setScene(_scene);
		_stage.show();

		// rotationAst();
		setTimeline();
		_timeline.play();
	}

	private void setTimeline() {

		_timeline = new Timeline();
		_handleAst = new EventHandler<ActionEvent>() {
			double dxs = 3;
			double dxm = 2;
			double dxb = 1;

			@Override
			public void handle(ActionEvent event) {

				// moveShipOnKeyPressed();
				// imageOnKeyreleased();
				moveShipMouse();
				actionOnMouseClicked();
				astCreation();

				try {
					if (_lasers.size() > 0) {
						for (int i = 0; i < _lasers.size(); i++) {
							if (_lasers.get(i).getLayoutX() < 1000) {
								_lasers.get(i).setLayoutX(_lasers.get(i).getLayoutX() + 20);

								_collast = _lasers.get(i).getBoundsInParent()
										.intersects(_asts.get(i).getBoundsInParent());

								if (_collast == true) {
									_asts.get(i).setFill(_explo);
									_root.getChildren().remove(_lasers.get(i));
									_lasers.clear();
								}
							} else {
								_root.getChildren().remove(_lasers.get(i));
								_lasers.clear();
							}
						}
					}
					for (int ia = 0; ia < _asts.size(); ia++) {
						if (_asts.get(ia).getLayoutX() > -1050) {
							// _collShip = Shape.intersect(_falcon,
							// _asts.get(ia));
							_root.getChildren().add(_collShip);
							_collShip.setFill(Color.TRANSPARENT);

							if (_collShip.computeAreaInScreen() > 1) {
								_falcon.setFill(_explo);
								_timeline.stop();
							}
							// _asts.get(ia).setLayoutX((_asts.get(ia).getLayoutX()
							// - 4));
							_root.getChildren().add(_asts.get(ia));
						} else {
							_root.getChildren().remove(_asts.get(ia));
							_asts.clear();
						}
					}
				} catch (Exception IllegalArgumentException) {
					// System.out.println("exception");
				}
			}
		};
		_timeline.getKeyFrames().addAll(new KeyFrame(Duration.millis(9), _handleAst));
		_timeline.setCycleCount(Timeline.INDEFINITE);

	}

	private void moveShipMouse() {
		_scene.setOnMouseMoved(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouse) {
				if (_gameOver == false) {
					_falcon.setCenterY(mouse.getY());
					_falcon.setCenterX(mouse.getX());
				}
			}
		});
	}

	private void actionOnMouseClicked() {

		_scene.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
				if (MouseButton.PRIMARY != null) {
					if (_gameOver == false) {
						loading();
						shoot();
					}
				}
			}
		});
	}

	private void moveShipOnKeyPressed() {

		double rx = 30;
		double ry = 20;

		_scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {

				case UP:
					if (_gameOver == false) {
						_falcon.setCenterY(_falcon.getCenterY() - ry);
						// translateTransitionUp();
					}
					break;
				case DOWN:
					if (_gameOver == false) {
						_falcon.setCenterY(_falcon.getCenterY() + ry);
						// translateTransitionDown();
					}
					break;
				case LEFT:
					if (_gameOver == false) {
						_falcon.setCenterX(_falcon.getCenterX() - rx);
						// translateTransitionBack();
						_falcon.setFill(_shipNM);
					}
					break;
				case RIGHT:
					if (_gameOver == false) {
						_falcon.setCenterX(_falcon.getCenterX() + rx);
						// translateTransitionFor();
					}
					break;
				case SPACE:
					if (_gameOver == false) {
						loading();
						shoot();
					}
					break;
				default:
				}
			}
		});
	}

	private void imageOnKeyreleased() {

		_scene.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent evo) {
				switch (evo.getCode()) {

				case UP:
					if (_gameOver == false) {
						rotationShipReset();
					}
					break;
				case DOWN:
					if (_gameOver == false) {
						rotationShipReset();
					}
					break;
				case LEFT:
					_falcon.setFill(_shipNM);
					break;
				default:
					break;
				}
			}
		});
	}

	public void loading() {
		try {
			for (int i = 1; i < 2; i++) {
				_lasers.add(new Laser(10, 2, Color.RED));
			}
		} catch (Exception IllegalArgumentException) {
		}
	}

	public void shoot() {
		try {
			for (int i = 0; i < _lasers.size(); i++) {

				_lasers.get(i).setX(_falcon.getCenterX());
				_lasers.get(i).setY(_falcon.getCenterY() - 2);

				_root.getChildren().add(_lasers.get(i));
			}
		} catch (Exception IllegalArgumentException) {
			// System.out.println("probl�me shoot");
		}
	}

	public void astCreation() {
		try {
			for (int i = 0; i < 3; i++) {
				_asts.add(new Asteroids(1000, (Math.random() * 500), (Math.random() * 30) + 15, _ast));
				_collShip = Shape.intersect(_falcon, _asts.get(i));
				_asts.get(i).setLayoutX((_asts.get(i).getLayoutX() - 4));
			}
		} catch (Exception IllegalArgumentException) {
		}
	}

	// private void colLas(){
	//
	// for (int i = 0; i < _lasers.size(); i++){
	// _collas = Shape.intersect((Shape)_lasers.get(i), (Shape) _cirAst3);
	//
	// if (_collas.computeAreaInScreen() > 10){
	// _cirAst3.setFill(_explo);
	// }
	// }
	// }

	// private void rotationAst() {
	//
	// RotateTransition _fastrt = new RotateTransition(Duration.millis(4000),
	// _cirAst);
	// _fastrt.setByAngle(360);
	// _fastrt.setCycleCount(Timeline.INDEFINITE);
	//
	// RotateTransition _mediumrt = new RotateTransition(Duration.millis(6000),
	// _cirAst2);
	// _mediumrt.setByAngle(360);
	// _mediumrt.setCycleCount(Timeline.INDEFINITE);
	//
	// RotateTransition _slowrt = new RotateTransition(Duration.millis(9000),
	// _cirAst3);
	// _slowrt.setByAngle(360);
	// _slowrt.setCycleCount(Timeline.INDEFINITE);
	//
	// _fastrt.play();
	// _mediumrt.play();
	// _slowrt.play();
	// }

	private void rotationShipUp() {
		RotateTransition _shipUp = new RotateTransition(Duration.millis(500), _falcon);
		_falcon.setLayoutY(_falcon.getLayoutY() + _falcon.getLayoutY());
		_shipUp.setToAngle(-30);
		_shipUp.play();
	}

	private void rotationShipDown() {
		RotateTransition _shipDown = new RotateTransition(Duration.millis(500), _falcon);
		_falcon.setLayoutY(_falcon.getLayoutY() + _falcon.getLayoutY());
		_shipDown.setToAngle(30);
		_shipDown.play();
	}

	private void rotationShipReset() {
		RotateTransition _shipReset = new RotateTransition(Duration.millis(500), _falcon);

		_shipReset.setToAngle(0);
		_shipReset.play();
	}

	private void translateTransitionUp() {
		TranslateTransition trUp = new TranslateTransition();
		trUp.setDuration(Duration.millis(300));
		trUp.setNode(_falcon);
		trUp.setFromY(_falcon.getTranslateY());
		trUp.setToY(_falcon.getTranslateY() - 40);
		_falcon.setFill(_shipM);
		rotationShipUp();
		trUp.play();
	}

	private void translateTransitionDown() {
		TranslateTransition trDown = new TranslateTransition();
		trDown.setDuration(Duration.millis(300));
		trDown.setNode(_falcon);
		trDown.setFromY(_falcon.getTranslateY());
		trDown.setToY(_falcon.getTranslateY() + 40);
		_falcon.setFill(_shipM);
		rotationShipDown();
		trDown.play();
	}

	private void translateTransitionFor() {
		TranslateTransition trFor = new TranslateTransition();
		trFor.setDuration(Duration.millis(400));
		trFor.setNode(_falcon);
		trFor.setFromX(_falcon.getTranslateX());
		trFor.setToX(_falcon.getTranslateX() + 60);
		_falcon.setFill(_shipM);
		trFor.play();
	}

	private void translateTransitionBack() {
		TranslateTransition trBack = new TranslateTransition();
		trBack.setDuration(Duration.millis(400));
		trBack.setNode(_falcon);
		trBack.setFromX(_falcon.getTranslateX());
		trBack.setToX(_falcon.getTranslateX() - 60);
		trBack.play();
	}
}