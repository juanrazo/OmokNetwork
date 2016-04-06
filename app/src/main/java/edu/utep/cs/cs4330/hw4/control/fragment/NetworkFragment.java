package edu.utep.cs.cs4330.hw4.control.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import edu.utep.cs.cs4330.hw4.R;
import edu.utep.cs.cs4330.hw4.control.activity.GameActivity;
import edu.utep.cs.cs4330.hw4.control.activity.NetworkActivity;
import edu.utep.cs.cs4330.hw4.model.Computer;
import edu.utep.cs.cs4330.hw4.model.Network;
import edu.utep.cs.cs4330.hw4.model.StrategyRandom;
import edu.utep.cs.cs4330.hw4.model.StrategySmart;
import edu.utep.cs.cs4330.hw4.model.WebServiceHandler;

/**
 * Created by juanrazo on 4/5/16.
 */
public class NetworkFragment extends Fragment {
    private EditText editTextPlayerOne;
    private RadioButton radioButtonRandom;
    private RadioButton radioButtonSmart;
    private Button buttonNewGame;
    public NetworkFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network_play, container, false);
        editTextPlayerOne = (EditText) view.findViewById(R.id.editTextPlayerOneName);
        radioButtonRandom = (RadioButton) view.findViewById(R.id.radioButtonRandom);
        radioButtonRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });
        radioButtonSmart = (RadioButton) view.findViewById(R.id.radioButtonSmart);
        radioButtonSmart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });
        buttonNewGame = (Button) view.findViewById(R.id.buttonNewGame);
        buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NetworkActivity) getActivity()).startGame();
            }
        });
        return view;
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radioButtonRandom:
                if (checked) {
                    ((Network) ((GameActivity) getActivity()).
                            getOmokGame().getPlayers()[1]).randomWebService();
                }
                break;
            case R.id.radioButtonSmart:
                if (checked) {
                    ((Network) ((GameActivity) getActivity()).
                            getOmokGame().getPlayers()[1]).smartWebService();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(((Network) ((GameActivity) getActivity()).
                getOmokGame().getPlayers()[1]).isStrategy())
            radioButtonSmart.setSelected(true);

        else
            radioButtonRandom.setSelected(true);
    }

    public EditText getEditTextPlayerOne() {
        return editTextPlayerOne;
    }

    public RadioButton getRadioButtonRandom() {
        return radioButtonRandom;
    }

    public RadioButton getRadioButtonSmart() {
        return radioButtonSmart;
    }

    public Button getButtonNewGame() {
        return buttonNewGame;
    }
}