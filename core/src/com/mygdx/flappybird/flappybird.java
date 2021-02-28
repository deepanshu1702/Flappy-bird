package com.mygdx.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class flappybird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture gameover;
	Texture[] birds;
	int flapstate = 0;
	float birdY=0;
	float velocity=0;
	Circle birdCircle;
	int score=0;
	int scoringTube=0;
	BitmapFont font;

	int gameState=0;
	float gravity=2;

	Texture toptube;
	Texture bottomtube;
	float gap=400;
	float maxTubeOffSet;
	Random randomGenerator;
	float tubeVelocity=4 ;
	int numberOfTubes=4;
	float[] tubeX=new float[numberOfTubes];
	float[] tubeOffset =new float[numberOfTubes];
	float distanceBetweenTubes;
	Rectangle[] toptubeRectangles;
	Rectangle[] bottomtubeRectangles;








	@Override
	public void create () {
		batch = new SpriteBatch();
		background= new Texture("bg.png");
		gameover=new Texture("gameover.jpg");
		birdCircle=new Circle();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);


		birds=new Texture[2];


		birds[0]= new Texture("bird.png");
		birds[1]= new Texture("bird2.png");

		toptube= new Texture("toptube.png");
		bottomtube= new Texture("bottomtube.png");
		maxTubeOffSet=Gdx.graphics.getHeight()/2-gap/2-100;
		randomGenerator= new Random();
		distanceBetweenTubes=Gdx.graphics.getWidth()/2;
		toptubeRectangles=new Rectangle[numberOfTubes];
		bottomtubeRectangles=new Rectangle[numberOfTubes];

		startGame();









	}
	public  void startGame() {
		birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
		for (int i = 0; i < numberOfTubes; i++) {
			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
			tubeX[i] = Gdx.graphics.getWidth() / 2 - toptube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

			toptubeRectangles[i] = new Rectangle();
			bottomtubeRectangles[i] = new Rectangle();
		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


		if (gameState==1){
			if (tubeX[scoringTube]< Gdx.graphics.getWidth()/2){
				score++;
				if (scoringTube < numberOfTubes- 1){
					scoringTube++;
				}else {
					scoringTube=0;
				}
			}

			if (Gdx.input.justTouched()){
				velocity=-30;


			}
			for (int i =0; i < numberOfTubes ;i++) {
				if (tubeX[i] < - toptube.getWidth()) {
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i]= (randomGenerator.nextFloat()- 0.5f)*(Gdx.graphics.getHeight() - gap -200);
				} else {
					tubeX[i] = tubeX[i] - tubeVelocity;

				}

				batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i]);
				toptubeRectangles[i]=new Rectangle(tubeX[i],Gdx.graphics.getHeight()/2 + gap/2+ tubeOffset[i],toptube.getWidth(),toptube.getHeight());
				bottomtubeRectangles[i]=new Rectangle(tubeX[i],Gdx.graphics.getHeight()/2 - gap/2-bottomtube.getHeight()+tubeOffset[i],bottomtube.getWidth(),bottomtube.getHeight());

			}

			if (birdY>0 ){
				velocity= velocity + gravity;
				birdY-=velocity;
			}else{
				gameState=2;
			}
		}else if (gameState==0){
			if (Gdx.input.justTouched()){
				gameState=1;
			}
		} else if (gameState==2){
			batch.draw(gameover,Gdx.graphics.getWidth()/2-gameover.getWidth()/2,Gdx.graphics.getHeight()/2-gameover.getHeight()/2);
			if (Gdx.input.justTouched()){
				gameState=1;
				startGame();
				score=0;
				scoringTube=0;
				velocity=0;
			}
		}
		if (flapstate==0){
			flapstate=1;
		} else{
			flapstate=0;

		}

		batch.draw(birds[flapstate],Gdx.graphics.getWidth()/2-birds[flapstate].getWidth()/2,birdY);
		font.draw(batch,String.valueOf(score),100,200);
		batch.end();
		birdCircle.set(Gdx.graphics.getWidth()/2,birdY+ birds[flapstate].getHeight()/2,birds[flapstate].getWidth()/2);
		for (int i=0; i < numberOfTubes; i++){
			if ( Intersector.overlaps(birdCircle,toptubeRectangles[i] ) || Intersector.overlaps(birdCircle,bottomtubeRectangles[i])) {

				gameState=2;
			}

		}



	}



}

