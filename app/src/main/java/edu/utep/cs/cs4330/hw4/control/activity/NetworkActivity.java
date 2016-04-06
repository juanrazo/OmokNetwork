package edu.utep.cs.cs4330.hw4.control.activity;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import edu.utep.cs.cs4330.hw4.R;
import edu.utep.cs.cs4330.hw4.control.fragment.GameFragment;
import edu.utep.cs.cs4330.hw4.control.fragment.NetworkFragment;
import edu.utep.cs.cs4330.hw4.model.Board;
import edu.utep.cs.cs4330.hw4.model.Computer;
import edu.utep.cs.cs4330.hw4.model.Human;
import edu.utep.cs.cs4330.hw4.model.Network;
import edu.utep.cs.cs4330.hw4.model.OmokGame;
import edu.utep.cs.cs4330.hw4.model.StrategyRandom;
import edu.utep.cs.cs4330.hw4.model.StrategySmart;

/**
 * Created by juanrazo on 4/5/16.
 */
public class NetworkActivity extends GameActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        omokGame = new OmokGame(1);
    }

    @Override
    protected void assignLayout(Bundle savedInstanceState) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            viewPager = (ViewPager) findViewById(R.id.pager);
            viewPager.setAdapter(new NetworkFragmentAdapter(getSupportFragmentManager()));
        } else {
            setContentView(R.layout.activity_network_play);
        }
    }

    @Override
    public void startGame() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                NetworkFragment settingsFragment = findNetworkFragment();
                GameFragment gameFragment = findGameFragment();
                ((Human) omokGame.getPlayers()[0]).setName(settingsFragment.getEditTextPlayerOne().getText().toString());
                if (settingsFragment.getRadioButtonRandom().isSelected())
                    ((Network) omokGame.getPlayers()[1]).randomWebService();
                else
                    ((Network) omokGame.getPlayers()[1]).smartWebService();
                ((Network) omokGame.getPlayers()[1]).startStrategy();
                omokGame.setBoard(new Board());
                omokGame.setGameRunning(true);
                omokGame.setTurn(0);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                    viewPager.setCurrentItem(1);
                gameFragment.getBoardView().invalidate();
                Toast.makeText(getBaseContext(), R.string.game_started, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(builder.getContext(), R.string.new_game_canceled, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setMessage(R.string.new_game_prompt);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private NetworkFragment findNetworkFragment() {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return (NetworkFragment) ((GameFragmentAdapter) viewPager.getAdapter()).getRegisteredFragment(0);
        } else {
            return (NetworkFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_network_play);
        }
    }

    class NetworkFragmentAdapter extends GameFragmentAdapter {
        public NetworkFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position
         */
        @Override
        public Fragment getItem(int position) {
            // TODO Auto-generated method stub
            Fragment fragment = null;
            if (position == 0) {
                fragment = new NetworkFragment();
            } else if (position == 1) {
                fragment = new GameFragment();
            }
            return fragment;
        }

    }
}
