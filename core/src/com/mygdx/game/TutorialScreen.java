package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

public class TutorialScreen extends GameScreen{
    private final Observable<Void> mainmenu = new Observable<>();

    //UI for TutorialScreen
    private Table table;
    private Table background;
    private ScrollPane scroll;

    /**
     * @param batch  SpriteBatch to initialize the Stage with
     * @param assets AssetManager to load assets with
     */
    public TutorialScreen(SpriteBatch batch, AssetManager assets) {
        super(batch, assets);

        table = new Table();
        int textWidth = 1800;

        //Adding information for Tutorial
        Label title = new Label("Tutorial", skin);
        Label blank = new Label(" ", skin);
        Label goal = new Label("Goal Of The Game: ", skin);
        Label line1 = new Label("The goal of the game is to obtain the most amount of stars and learn as much " +
                "as you can about personal finance.\n" +
                "This game aims to deliver an interactive way to get exposed to investing in a simulated stock market environment " +
                "as well as improve or get exposed to your personal finance journey!", skin);
        Label mechanics = new Label("Game Play Mechanics:", skin);
        Label line2 = new Label("1. Each player will have a chance to move around the board and use items.", skin);
        Label line3 = new Label("2. Using items and entering the shop must be performed before moving to a tile.", skin);
        Label line4 = new Label("3. After rolling a dice, A tile will be highlighted, the player must then click on the " +
                "highlighted tile to move to the tile", skin);
        Label line5 = new Label("4. Tiles may be of 4 different types", skin);
        Label line6 = new Label("   -Star Tile: Will cost coins but each star is worth 1000 points", skin);
        Label line7 = new Label("   -Penalty Tile: The player that lands on this tile will loose a star and/or coins", skin);
        Label line8 = new Label("   -Event Tile: Global event same as the penalty tile. Everyone looses a star and/or coins", skin);
        Label line9 = new Label("5. The game ends after 26 rounds have been played", skin);
        Label line10 = new Label("6. Score will be calculated by the number of stars the player owns x 1000 plus investments and money",skin);
        Label modes = new Label("Extra Information: ", skin);
        Label line11 = new Label("Hard Mode:", skin);
        Label line12 = new Label("   -Players can go into the negatives in terms of coins", skin);
        Label line13 = new Label("   -Upon landing on an event or penalty tile, players will loose a star and coins", skin);
        Label line14 = new Label("   -Each star is worth 250 coins", skin);
        Label line15 = new Label("   -Stocks will remain unchanged", skin);
        Label line16 = new Label("Normal Mode: ", skin);
        Label line17 = new Label("   -Players can not go into a negative balance", skin);
        Label line18 = new Label("   -Players on loose coins on event or penalty nodes", skin);
        Label line19 = new Label("   -Stars only cost 100 coins to obtain", skin);
        Label line20 = new Label("At any point during the game, players can hit ESC and view their knowledge that they have learned", skin);
        Label line21 = new Label("Hit ESC to go back to main menu", skin);

        //Scale for titles and headers
        title.setFontScale(3);
        goal.setFontScale(2);
        mechanics.setFontScale(2);
        modes.setFontScale(2);

        line1.setWrap(true);

        //Setting Alignment
        title.setAlignment(Align.center);
        goal.setAlignment(Align.left);
        mechanics.setAlignment(Align.left);
        modes.setAlignment(Align.left);
        blank.setAlignment(Align.left);
        line1.setAlignment(Align.left);
        line2.setAlignment(Align.left);
        line3.setAlignment(Align.left);
        line4.setAlignment(Align.left);
        line5.setAlignment(Align.left);
        line6.setAlignment(Align.left);
        line7.setAlignment(Align.left);
        line8.setAlignment(Align.left);
        line9.setAlignment(Align.left);
        line10.setAlignment(Align.left);
        line11.setAlignment(Align.left);
        line12.setAlignment(Align.left);
        line13.setAlignment(Align.left);
        line14.setAlignment(Align.left);
        line15.setAlignment(Align.left);
        line16.setAlignment(Align.left);
        line17.setAlignment(Align.left);
        line18.setAlignment(Align.left);
        line19.setAlignment(Align.left);
        line20.setAlignment(Align.left);
        line21.setAlignment(Align.left);

        //Adding to Title
        table.add(title).width(textWidth).center();
        table.row();
        table.add(blank).width(textWidth).left();
        table.row();

        //Adding Goal
        table.add(goal).width(textWidth).left();
        table.row();
        table.add(line1).width(textWidth).left();
        table.row();
        table.add(blank).width(textWidth).left();
        table.row();

        //Adding Mechanics of the game
        table.add(mechanics).width(textWidth).left();
        table.row();
        table.add(line2).width(textWidth).left();
        table.row();
        table.add(line3).width(textWidth).left();
        table.row();
        table.add(line4).width(textWidth).left();
        table.row();
        table.add(line5).width(textWidth).left();
        table.row();
        table.add(line5).width(textWidth).left();
        table.row();
        table.add(line7).width(textWidth).left();
        table.row();
        table.add(line8).width(textWidth).left();
        table.row();
        table.add(line9).width(textWidth).left();
        table.row();
        table.add(line10).width(textWidth).left();
        table.row();
        table.add(blank).width(textWidth).left();
        table.row();

        //Adding modes that contain the extra information
        table.add(modes).width(textWidth).left();
        table.row();
        table.add(line11).width(textWidth).left();
        table.row();
        table.add(line12).width(textWidth).left();
        table.row();
        table.add(line13).width(textWidth).left();
        table.row();
        table.add(line14).width(textWidth).left();
        table.row();
        table.add(line15).width(textWidth).left();
        table.row();
        table.add(line16).width(textWidth).left();
        table.row();
        table.add(line17).width(textWidth).left();
        table.row();
        table.add(line18).width(textWidth).left();
        table.row();
        table.add(line19).width(textWidth).left();
        table.row();
        table.add(line20).width(textWidth).left();
        table.row();
        table.add(line21).width(textWidth).left();

        //Adding to scroll pane
        scroll = new ScrollPane(table, skin);
        scroll.setHeight(1000);
        scroll.setScrollBarPositions(false, true);
        scroll.setScrollbarsVisible(true);

        //Adding to background
        background = new Table();
        background.setFillParent(true);
        background.add(scroll).fillX().expandX();

        this.stage.addActor(background);

        //Adding listener to go back to menu screen
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.ESCAPE) mainmenu.notifyObservers(null);
                else return false;
                return true;
            }
        });
    }

    /**
     * @param ob Adds listener to go back to main menu screen
     */
    public void addBackToMenu(Observer<Void> ob) {mainmenu.addObserver(ob);}
}
