package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

/**
 * This screen implements the Agility Test, a test of elementary math skills. Activated by the Agility Test Node.
 */
public class AgilityTestScreen extends GameScreen {
    // Event returns to board and reports the player's earnings
    private Observable<Integer> boardEvent = new Observable<>();
    private Table table;
    private Table promptTable;
    private Table inputTable;

    private int earnings = 0;
    private double difficulty = 0.5f; // Scalable difficulty.
    private int first; // First part of the expression
    private int second; // Second part of the expression
    private Character operationType; // Type of operation applied
    private int answer; // Result of operation
    private int level = 1; // Player gets 12 questions. Each question is worth $20
    private long endTime; // Time when the question is automatically failed
    private boolean hardMode = false;

    private Label title;
    private Label earningLabel;
    private Label timerLabel;
    private Label questionLabel;
    private Label exprLabel;
    private Label answerLabel;
    private TextField inputField;
    private TextButton submitButton;

    /**
     * Constructor initializes the screen.
     *
     * @param batch  SpriteBatch to initialize the Stage with
     * @param assets AssetManager to load assets with
     */
    public AgilityTestScreen(SpriteBatch batch, AssetManager assets) {
        super(batch, assets);

        // Setup keyboard shortcuts
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    if (submitButton.isTouchable()) submitButton.fire(new ChangeListener.ChangeEvent());
                }
                else return false;

                return true;
            }
        });

        // Initialize GUI elements
        table = new Table();
        table.setFillParent(true);
        promptTable = new Table();
        inputTable = new Table();

        title = new Label("Financial Agility Game", skin, "menu");

        earningLabel = new Label("Earnings", skin);
        earningLabel.setColor(Color.GREEN);
        timerLabel = new Label("Timer", skin);
        timerLabel.setColor(Color.RED);
        questionLabel = new Label("Question:", skin, "menu");
        exprLabel = new Label("expression", skin, "menu");
        answerLabel = new Label("Answer:", skin, "menu");
        inputField = new TextField("", skin);
        submitButton = new TextButton("Submit", skin);
        submitButton.setTouchable(Touchable.disabled);
        submitButton.setColor(Color.GRAY);
        submitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                answerLabel.setVisible(true);
                answerLabel.setText("Answer: " + answer);
                timerLabel.setVisible(false);

                int input;
                try {
                    input = Integer.parseInt(inputField.getText());
                } catch (NumberFormatException e) {
                    input = 1337; // Default value
                }

                if (input == answer) {
                    earnings += 20;
                    // Speed bonus
                    int bonus = 0;
                    if (endTime - System.currentTimeMillis() > 5000) {
                        // Every second over 5 is worth $2
                        bonus = (int) ((endTime - System.currentTimeMillis()) / 500);
                    }
                    earnings += bonus;
                    Vector2 mousePos = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
                    if (bonus > 0) ActionTextSystem.addText("Speed Bonus! +$" + bonus, mousePos.x, mousePos.y, 0.5f, Color.YELLOW);
                    earningLabel.setText("$" + earnings);

                    difficulty = Math.pow(difficulty, 0.85); // Increase difficulty

                    inputField.setColor(Color.GREEN);
                    SoundSystem.getInstance().playSound("success_bell.mp3");
                }
                else {
                    difficulty = Math.pow(difficulty, 1.15); // Decrease difficulty

                    inputField.setColor(Color.RED);
                    SoundSystem.getInstance().playSound("error.wav");
                }

                // Wait 3 seconds before next question
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        level++;
                        if (level > 12) {
                            Dialog gameCompleteDialog = new Dialog("Game Complete", skin) {
                                @Override
                                protected void result(Object object) {
                                    endGame(); // Change screen to board
                                }
                            };
                            gameCompleteDialog.button("Ok");
                            gameCompleteDialog.show(stage);
                            return;
                        }
                        generateProblem();
                    }
                }, 3);

                submitButton.setTouchable(Touchable.disabled);
                submitButton.setColor(Color.GRAY);
            }
        });


        // Layout GUI
        table.row().pad(25);
        table.add(title).top().expandX();
        table.add(earningLabel).top();
        table.row();
        table.add(promptTable).center().expand();
        table.row();
        table.add(inputTable).expand().top();
        promptTable.row();
        promptTable.add(timerLabel);
        promptTable.row();
        promptTable.add(questionLabel);
        promptTable.row();
        promptTable.add(exprLabel);
        inputTable.add(answerLabel).colspan(2);
        inputTable.row().pad(25);
        inputTable.add(inputField);
        inputTable.add(submitButton);


        stage.addActor(table);
    }

    /**
     * Runs when setting to this screen
     */
    @Override
    public void show() {
        stage.setKeyboardFocus(inputField);

        difficulty = 0.5f;
        earnings = 0;
        level = 1;
        endTime = System.currentTimeMillis() + 3000; // Wait 3 seconds before starting
        inputTable.setVisible(false);
        questionLabel.setText("Starting soon...");
        exprLabel.setVisible(false);
        earningLabel.setText("$0");
        timerLabel.setVisible(false);

        // Wait 3 seconds before next question
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                generateProblem();
                submitButton.setTouchable(Touchable.enabled);
                submitButton.setColor(Color.WHITE);
                inputTable.setVisible(true);
                exprLabel.setVisible(true);
                timerLabel.setVisible(true);
            }
        }, 3);

        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Render the screen and update the timer
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        super.render(delta);
        long timeLeft = endTime - System.currentTimeMillis();
        if (timeLeft <= 0) {
            timerLabel.setText("0.0s");
            // Auto-submit response if possible
            if (submitButton.isTouchable()) submitButton.fire(new ChangeListener.ChangeEvent());
        }
        else {
            timerLabel.setText(String.format("%.1fs", timeLeft / 1000.0));
        }

        stage.getBatch().begin();
        ActionTextSystem.render(stage.getBatch(), Gdx.graphics.getDeltaTime());
        stage.getBatch().end();
    }

    /**
     * Generate a problem based on current difficulty.
     */
    private void generateProblem() {
        int minValue;
        int maxValue;
        int operationChoice = Utility.getRandom(0, 3);
        switch (operationChoice) {
            case 0:
                operationType = '+';
                minValue = 5;
                maxValue = (int)(minValue + 245.0 * difficulty);
                if (hardMode) maxValue *= 2; // This should make things much harder

                first = Utility.getRandom(minValue, maxValue);
                second = Utility.getRandom(minValue, maxValue);
                answer = first + second;
                break;
            case 1:
                operationType = '-';
                minValue = 5;
                maxValue = (int)(minValue + 245.0 * difficulty);
                if (hardMode) maxValue *= 2;

                first = Utility.getRandom(minValue, maxValue);
                second = Utility.getRandom(minValue, maxValue);
                if (first < second) {
                    // Reroll, to bias towards positive final values
                    first = Utility.getRandom(minValue, maxValue);
                }
                answer = first - second;
                break;
            case 2:
                operationType = '*';
                minValue = 2;
                maxValue = minValue + (int)(20.0 * difficulty);
                if (hardMode) maxValue *= 2;

                first = Utility.getRandom(minValue, maxValue);
                second = Utility.getRandom(minValue, maxValue);
                answer = first * second;
                break;
            case 3:
                operationType = '/';
                minValue = 2;
                maxValue = minValue + (int)(20.0 * difficulty);
                if (hardMode) maxValue *= 2;

                // To prevent decimals, construct the value expression through multiplication
                second = Utility.getRandom(minValue, maxValue);
                answer = Utility.getRandom(minValue, maxValue);
                first = second * answer;
        }

        submitButton.setTouchable(Touchable.enabled);
        submitButton.setColor(Color.WHITE);
        endTime = System.currentTimeMillis() + 10000; // Give 10 seconds to answer the question
        questionLabel.setText("Question " + level + "/12:");
        exprLabel.setText(first + " " + operationType + " " + second + " =");
        answerLabel.setVisible(false);
        inputField.setText("");
        inputField.setColor(Color.WHITE);
        timerLabel.setVisible(true);
    }

    /**
     * End the game and report earnings to the listener
     */
    private void endGame() {
        boardEvent.notifyObservers(earnings);
    }

    /**
     * Enable hard mode
     *
     * @param hardMode true or false
     */
    public void setHardMode(boolean hardMode) {
        this.hardMode = hardMode;
    }

    /**
     * Assigns an observer to listen for the event to return to main menu.
     * @param ob Observer
     */
    public void addBoardListener(Observer<Integer> ob) { boardEvent.addObserver(ob); }
}