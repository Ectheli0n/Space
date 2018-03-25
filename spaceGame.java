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

	private Circle _cirAst, _cirAst2, _cirAst3;

	private Ellipse _falcon;

	private Rectangle _laser;

	private Shape _coll1, _coll2, _coll3;

	private Timeline _timeline;

	private boolean _gameOver = false;

	private ArrayList<Laser> _lasers = new ArrayList<Laser>();

	private EventHandler<ActionEvent> _handleAst;

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

		_cirAst = new Circle();
		_cirAst.setCenterX(1000);
		_cirAst.setCenterY(400);
		_cirAst.setRadius(20);
		_cirAst.setFill(_ast);

		_cirAst2 = new Circle();
		_cirAst2.setCenterX(1000);
		_cirAst2.setCenterY(300);
		_cirAst2.setRadius(30);
		_cirAst2.setFill(_ast);

		_cirAst3 = new Circle();
		_cirAst3.setCenterX(1000);
		_cirAst3.setCenterY(100);
		_cirAst3.setRadius(40);
		_cirAst3.setFill(_ast);

		_root = new Group();
		_root.getChildren().add(_cirAst);
		_root.getChildren().add(_cirAst2);
		_root.getChildren().add(_cirAst3);
		_root.getChildren().add(_falcon);

		_scene = new Scene(_root, 1000, 500);
		_scene.setFill(_font);
		_stage = primaryStage;
		_stage.setTitle("Game");
		_stage.setScene(_scene);
		_stage.show();

		// moveShipMouse();
		setTimeline();
		_timeline.play();
	}

	private void setTimeline() {

		moveShipOnKeyPressed();
		imageOnKeyreleased();
		rotationAst();

		_timeline = new Timeline();
		_handleAst = new EventHandler<ActionEvent>() {
			double dxs = 3;
			double dxm = 2;
			double dxb = 1;

			@Override
			public void handle(ActionEvent event) {
				_cirAst.setLayoutX(_cirAst.getLayoutX() - dxs);
				_cirAst2.setLayoutX(_cirAst2.getLayoutX() - dxm);
				_cirAst3.setLayoutX(_cirAst3.getLayoutX() - dxb);
				
				_coll1 = Shape.intersect((Shape) _falcon, (Shape) _cirAst);
				_coll2 = Shape.intersect((Shape) _falcon, (Shape) _cirAst2);
				_coll3 = Shape.intersect((Shape) _falcon, (Shape) _cirAst3);

				_root.getChildren().add(_coll1);
				_root.getChildren().add(_coll2);
				_root.getChildren().add(_coll3);
				
				if (_coll1.computeAreaInScreen() > 10 || _coll2.computeAreaInScreen() > 10
						|| _coll3.computeAreaInScreen() > 10) {
					_falcon.setFill(_explo);
					_timeline.stop();
					_gameOver = true;
				}
				if (_cirAst.getLayoutX() < (-1000 - _cirAst.getRadius())) {
					_cirAst.setLayoutX(1000);
					_cirAst.setCenterY(Math.random() * 500);
				}
				if (_cirAst2.getLayoutX() < (-1000 - _cirAst2.getRadius())) {
					_cirAst2.setLayoutX(1000);
					_cirAst2.setCenterY(Math.random() * 500);
				}
				if (_cirAst3.getLayoutX() < (-1000 - _cirAst3.getRadius())) {
					_cirAst3.setLayoutX(1000);
					_cirAst3.setCenterY(Math.random() * 500);
				}
			}
		};
		_timeline.getKeyFrames().addAll(new KeyFrame(Duration.millis(9), _handleAst));
		_timeline.setCycleCount(Timeline.INDEFINITE);
	}

	// private void moveShipMouse() {
	// _scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
	//
	// @Override
	// public void handle(MouseEvent mouse) {
	// _recShip.setY(mouse.getY() - 55);
	// _recShip.setX(mouse.getX() - 60);
	// }
	// });
	// }

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
//						tir();
//						_lasers.get(1).setX(_laser.getX() + rx);
						shoot();
					}
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

	public void tir() {
		
		try{
			for(int i = 0; i < 2; i++){
				_lasers.add(new Laser(_falcon.getCenterX(), _falcon.getCenterY(), 2, 10));
		}
			_root.getChildren().addAll(_lasers);
		System.out.println(_lasers.size());
		System.out.println(_lasers);
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public void shoot() {

		_laser = new Rectangle(_falcon.getCenterX(), _falcon.getCenterY() - 3, 10, 2);

		_laser.setFill(Color.RED);

		Timeline timeline = new Timeline();

		timeline.setCycleCount(1);
		timeline.setAutoReverse(false);

		KeyValue kv = new KeyValue(_laser.xProperty(), 2000);
		KeyFrame kf = new KeyFrame(Duration.millis(2000), kv);
		
		_root.getChildren().add(_laser);
		
		timeline.getKeyFrames().add(kf);
		timeline.play();
	}

	private void rotationAst() {

		RotateTransition _fastrt = new RotateTransition(Duration.millis(4000), _cirAst);
		_fastrt.setByAngle(360);
		_fastrt.setCycleCount(Timeline.INDEFINITE);

		RotateTransition _mediumrt = new RotateTransition(Duration.millis(6000), _cirAst2);
		_mediumrt.setByAngle(360);
		_mediumrt.setCycleCount(Timeline.INDEFINITE);

		RotateTransition _slowrt = new RotateTransition(Duration.millis(9000), _cirAst3);
		_slowrt.setByAngle(360);
		_slowrt.setCycleCount(Timeline.INDEFINITE);

		_fastrt.play();
		_mediumrt.play();
		_slowrt.play();
	}

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