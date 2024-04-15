package com.example.hextesting;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Region;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class HexView extends SurfaceView implements View.OnTouchListener{

    HexTile tile;

    private TextView textView;

    int gridSize = 11;

    private float hexSize = 40; // Adjust hexagon size as needed

    HexTile[][] grid;

    // Size and shape variables
    private Path hexagonPath;
    private Path hexagonBorderPath;
    private float radius;
    private float width, height;
    private float height_SurfaceView;
    private float width_SurfaceView;


    //game stuff
    boolean player1_turn = true;

    int playerColor = Color.RED;

    public HexView(Context context, AttributeSet attrs) {

        super(context, attrs);

        grid = new HexTile[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {

                float x =  100 + (i * 35) + j * (float) (hexSize * 1.9);

                float y = 100 + (float) (i * hexSize * 1.7);


                grid[i][j] = new HexTile(x, y, Color.GRAY);
            }
        }

        /*tile = new HexTile(100, 100, Color.GRAY);
        tile2 = new HexTile(190, 100, Color.GRAY);
        tile3 = new HexTile(280, 100, Color.GRAY);*/


        this.setOnTouchListener(this);
        setWillNotDraw(false);
    }





    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j].draw(canvas);
            }
        }

        canvas.save();

    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Check if touch is inside any hex tile
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid.length; j++) {
                    if (grid[i][j].isTouched(x, y)) {
                        grid[i][j].setColor(playerColor);
                        invalidate();

                        Turn();


                    }
                }
            }
        }
        return false;

    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public void Turn() {

        if (textView != null) {
            if (player1_turn) {

                textView.setText("Blue's turn");
                playerColor = Color.BLUE;

                checkWinner();
            } else {
                textView.setText("Red's turn");
                playerColor = Color.RED;

                checkWinner();
            }
            player1_turn = !player1_turn;
        }
    }

    public void checkWinner() {
        if(blueWins()) {
            textView.setText("BLUE WINS");

        } else if (redWins()) {
            textView.setText("RED WINS");
        }
    }

    public boolean blueWins() {
        //loops through up and down the grid board
        for (int col = 0; col < grid.length; col++) {

            //checks if there is a blue tile on each index of the colums and checks if they are connected
            if (grid[0][col].getColor() == Color.BLUE && isConnected(0, col, new boolean[grid.length][grid.length], Color.BLUE)) {
                return true;
            }
        }
        return false;
    }

    /*
     * checks if red wins
     */
    public boolean redWins() {
        //loops through left and right of the grid board
        for (int row = 0; row < grid.length; row++) {

            //checks if there is red tile on each index of the rows and checks if they are connected
            if (grid[row][0].getColor() == Color.RED && isConnected(row, 0, new boolean[grid.length][grid.length], Color.RED)) {
                return true;
            }
        }
        return false;
    }


    public boolean isValid(int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid.length;
    }

    public boolean isConnected(int row, int col, boolean[][] visited, int playerColor) {
        // Base cases: Check if we reached an opposite side
        if (playerColor == Color.BLUE && row == grid.length - 1) {
            return true; // Blue player connected top and bottom
        }
        if (playerColor == Color.RED && col == grid.length - 1) {
            return true; // Red player connected left and right
        }

        //tile has been checked
        visited[row][col] = true;

        // Offsets for neighboring cells in a hexagonal grid
        int[] dx = {-1, -1, 0, 0, 1, 1};
        int[] dy = {0, -1, -1, 1, 0, 1};

        //loops through each case of offsets
        for (int i = 0; i < 6; i++) {
            int newRow = row + dx[i];
            int newCol = col + dy[i];

            // Check if the neighboring cell is within bounds and belongs to the same player
            if (isValid(newRow, newCol) && grid[newRow][newCol].getColor() == playerColor && !visited[newRow][newCol]) {
                if (isConnected(newRow, newCol, visited, playerColor)) {
                    return true;
                }
            }
        }
        return false;
    }


}