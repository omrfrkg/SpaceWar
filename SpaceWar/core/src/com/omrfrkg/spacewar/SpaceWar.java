package com.omrfrkg.spacewar;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class SpaceWar extends ApplicationAdapter {

	SpriteBatch batch;
	Texture background;
	Texture spaceship;
	Texture enemyPunch;
	Texture enemySpaceship;
	Texture enemyUfo;
	float spaceshipX = 0;
	float spaceshipY = 0;
	int gameState = 0;
	float velocity = 10.0f;
	int touchCounter = 0;
	Boolean downOrUp;
	int numberOfEnemies = 4;
	float[] enemyX = new float[numberOfEnemies];
	float[] enemyOffSet = new float[numberOfEnemies];
	float[] enemyOffSet2 = new float[numberOfEnemies];
	float[] enemyOffSet3 = new float[numberOfEnemies];
	float distance = 0;
	int enemyVelocity = 6;
	Random random;

	Circle spaceshipCircle;
	Circle[] enemyCircle;
	Circle[] enemyCircle2;
	Circle[] enemyCircle3;
	ShapeRenderer shapeRenderer;
	int score = 0;
	int scoreEnemy = 0;
	BitmapFont font;
	BitmapFont font2;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.png");
		spaceship = new Texture("spaceship.png");
		enemyPunch = new Texture("enemy_punch.png");
		enemySpaceship = new Texture("enemy_spaceship.png");
		enemyUfo = new Texture("enemy_ufo.png");
		downOrUp = false;

		spaceshipX = (Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() / 2) + 280;
		spaceshipY = (float) Gdx.graphics.getHeight() / 2.3f;

		shapeRenderer = new ShapeRenderer();


		spaceshipCircle = new Circle();
		enemyCircle = new Circle[numberOfEnemies];
		enemyCircle2 = new Circle[numberOfEnemies];
		enemyCircle3 = new Circle[numberOfEnemies];

		distance = Gdx.graphics.getWidth() / 2;

		random = new Random();

		for (int i = 0; i < numberOfEnemies; i++){

			enemyOffSet[i] = (random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - 200);
			enemyOffSet2[i] = (random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - 200);
			enemyOffSet3[i] = (random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - 200);

			enemyX[i] = Gdx.graphics.getWidth() - enemyPunch.getWidth() / 2 + i * distance;

			enemyCircle[i] = new Circle();
			enemyCircle2[i] = new Circle();
			enemyCircle3[i] = new Circle();
		}

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(2);

		font2 = new BitmapFont();
		font2.setColor(Color.WHITE);
		font2.getData().setScale(4);

	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


		if (gameState == 1){

			if(Gdx.input.justTouched()){
				touchCounter ++;
			}

			if (enemyX[scoreEnemy] < Gdx.graphics.getWidth() / 2 - Gdx.graphics.getHeight() / 2){
				score++;

				if (scoreEnemy < numberOfEnemies - 1){
					scoreEnemy++;
				}
				else{
					scoreEnemy = 0;
				}
			}

			for (int i = 0; i < numberOfEnemies; i++){
				if (0 <= enemyX[i] && enemyX[i] < Gdx.graphics.getWidth()/15){

					enemyX[i] = enemyX[i]+numberOfEnemies*distance;
				    enemyOffSet[i] = (random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - 200);
					enemyOffSet2[i] = (random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - 200);
					enemyOffSet3[i] = (random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - 200);

				}
				else{
					enemyX[i] = enemyX[i] - enemyVelocity;
				}
				batch.draw(enemyPunch,enemyX[i],Gdx.graphics.getHeight()/2+enemyOffSet[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
				batch.draw(enemySpaceship,enemyX[i]*1.5f,Gdx.graphics.getHeight()/2+enemyOffSet2[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
				batch.draw(enemyUfo,enemyX[i]*2,Gdx.graphics.getHeight()/2+enemyOffSet3[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);

				enemyCircle[i] = new Circle(enemyX[i]+Gdx.graphics.getWidth()/30 , Gdx.graphics.getHeight()/2+enemyOffSet[i]+ Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);
				enemyCircle2[i] = new Circle(enemyX[i]*1.5f+Gdx.graphics.getWidth()/30 , Gdx.graphics.getHeight()/2+enemyOffSet2[i]+ Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);
				enemyCircle3[i] = new Circle(enemyX[i]*2+Gdx.graphics.getWidth()/30 , Gdx.graphics.getHeight()/2+enemyOffSet3[i]+ Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);
			}

			if (spaceshipY < (float) background.getHeight()/2.5f && touchCounter%2 != 0){
					spaceshipY += velocity;
			}
			if (spaceshipY > (float)background.getHeight()/28.0f && touchCounter%2 == 0){
				spaceshipY -= velocity;
			}


		}
		else if(gameState == 0){
			if (Gdx.input.justTouched()){
				gameState = 1;
			}
		}
		else if (gameState == 2){
			font2.draw(batch,"GAME OVER! TAP TO PLAY AGAIN!",350,Gdx.graphics.getHeight()/2);
			if (Gdx.input.justTouched()){

				gameState = 1;
				spaceshipY = (float) Gdx.graphics.getHeight() / 2.3f;

				for (int i = 0; i < numberOfEnemies; i++){

					enemyOffSet[i] = (random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - 200);
					enemyOffSet2[i] = (random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - 200);
					enemyOffSet3[i] = (random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - 200);

					enemyX[i] = Gdx.graphics.getWidth() - enemyPunch.getWidth() / 2 + i * distance;

					enemyCircle[i] = new Circle();
					enemyCircle2[i] = new Circle();
					enemyCircle3[i] = new Circle();
				}

				velocity = 10.0f;
				scoreEnemy = 0;
				score = 0;
				touchCounter=0;

			}
		}
		batch.draw(spaceship,spaceshipX,spaceshipY,Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
		font.draw(batch,String.valueOf(score),100,200);
		batch.end();

		spaceshipCircle.set(spaceshipX + Gdx.graphics.getWidth()/30,spaceshipY + Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30+8);

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.BLUE);
		//shapeRenderer.circle(spaceshipCircle.x,spaceshipCircle.y,spaceshipCircle.radius);


		for (int i = 0;i<numberOfEnemies;i++){
			//shapeRenderer.circle(enemyX[i]+Gdx.graphics.getWidth()/30 , Gdx.graphics.getHeight()/2+enemyOffSet[i]+ Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);
			//shapeRenderer.circle(enemyX[i]*1.5f+Gdx.graphics.getWidth()/30 , Gdx.graphics.getHeight()/2+enemyOffSet2[i]+ Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);
			//shapeRenderer.circle(enemyX[i]*2+Gdx.graphics.getWidth()/30 , Gdx.graphics.getHeight()/2+enemyOffSet3[i]+ Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);

			if (Intersector.overlaps(spaceshipCircle,enemyCircle[i]) || Intersector.overlaps(spaceshipCircle,enemyCircle2[i]) || Intersector.overlaps(spaceshipCircle,enemyCircle3[i])){
				gameState = 2;
			}
		}

		//shapeRenderer.end();
	}
	
	@Override
	public void dispose () {

	}

}
