package edu.utep.cs.cs4330.hw4.control.fragment;

/**
 * Created by juanrazo on 4/5/16.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import edu.utep.cs.cs4330.hw4.R;
import edu.utep.cs.cs4330.hw4.control.activity.GameActivity;
import edu.utep.cs.cs4330.hw4.model.Board;
import edu.utep.cs.cs4330.hw4.model.Computer;
import edu.utep.cs.cs4330.hw4.model.Coordinates;
import edu.utep.cs.cs4330.hw4.model.Human;
import edu.utep.cs.cs4330.hw4.model.Network;
import edu.utep.cs.cs4330.hw4.model.OmokGame;
import edu.utep.cs.cs4330.hw4.model.Player;
import edu.utep.cs.cs4330.hw4.view.BoardView;

public class GameFragment extends Fragment {
    private BoardView boardView;
    private TextView textViewTurn;
    private boolean net = false;
    private Coordinates previous = new Coordinates();
    private Coordinates playCoordinates = new Coordinates();
    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game, container, false);
        textViewTurn = (TextView) v.findViewById(R.id.textViewTurn);
        boardView = (BoardView) v.findViewById(R.id.board_view);
        boardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                OmokGame omokGame = ((GameActivity) getActivity()).getOmokGame();
                if (omokGame.isGameRunning()) {
                    int x, y;
                    int stepX, stepY;
                    stepX = boardView.getWidth() / 9;
                    stepY = boardView.getHeight() / 9;
                    x = (int) (event.getX() / stepX);
                    if (event.getX() % stepX > stepX / 2) {
                        x++;
                    }
                    y = (int) (event.getY() / stepY);
                    if (event.getY() % stepY > stepY / 2) {
                        y++;
                    }


                    Player player = omokGame.getCurrentPlayer();

                    if (player instanceof Computer)
                        playCoordinates = ((Computer) omokGame.getCurrentPlayer()).findCoordinates(omokGame.getBoard().getBoard());

                    if (player instanceof Human) {
                        previous.setX(x);
                        previous.setY(y);
                        playCoordinates = new Coordinates(x, y);
                        Log.i("Human Coordinates ", " " + x + ", " + y);
                        net = true;
                    }
                    if (net){
                        ((Network) omokGame.getPlayers()[1]).sendCoordinates(previous);
                        net=false;
                    }
                    if (omokGame.placeStone(playCoordinates)) {
                        boardView.invalidate();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        if (player instanceof Human)
                            builder.setMessage(((Human) player).getName() + getResources().getString(R.string.win_message));
                        else
                            builder.setMessage(getResources().getString(R.string.loss_message));
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                    if (player instanceof Network){
                        try {
                            TimeUnit.SECONDS.sleep(3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        playCoordinates = ((Network) omokGame.getPlayers()[1]).getCoordinates();

                        if (omokGame.placeStone(playCoordinates)) {
                            boardView.invalidate();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            if (player instanceof Human)
                                builder.setMessage(((Human) player).getName() + getResources().getString(R.string.win_message));
                            else
                                builder.setMessage(getResources().getString(R.string.loss_message));
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }


                    if (omokGame.getTurn() == 0)
                        textViewTurn.setText(R.string.player_one_turn);
                    else
                        textViewTurn.setText(R.string.player_two_turn);
                    boardView.updateBoard(omokGame.getBoard().getBoard());
                    boardView.invalidate();
                    return true;
                }
                return false;
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        boardView.updateBoard(((GameActivity) getActivity()).getOmokGame().getBoard().getBoard());
        if (((GameActivity) getActivity()).getOmokGame().getTurn() == 0)
            textViewTurn.setText(R.string.player_one_turn);
        else
            textViewTurn.setText(R.string.player_two_turn);
    }

    public BoardView getBoardView() {
        return boardView;
    }

    public void setBoardView(BoardView boardView) {
        this.boardView = boardView;
    }
}
