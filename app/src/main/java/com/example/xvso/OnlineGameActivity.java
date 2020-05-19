package com.example.xvso;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.xvso.Objects.Game;
import com.example.xvso.Objects.User;
import com.example.xvso.databinding.ActivityOnlineGameBinding;
import com.example.xvso.viewmodel.OnlineGameViewModel;
import com.example.xvso.viewmodel.OnlineUsersViewModelFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class OnlineGameActivity extends AppCompatActivity {

    private static final String LOG_TAG = "OnlineGameActivity";
    private static final String GAME_ID = "gameId";
    private static final String GUEST = "guest";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    int activePlayer = 1;
    ArrayList<Integer> player1 = new ArrayList<>();
    ArrayList<Integer> player2 = new ArrayList<>();
    private OnlineGameViewModel onlineGameViewModel;
    private ActivityOnlineGameBinding onlineGameBinding;
    // current player user name
    private String userName = "";
    // other player user name
    private String opponentFirstName = "";
    private String gameId = "";
    private String LoginUID = "";
    private String requestType = "";
    // current user is signed in with X
    private String myGameSignIn = "X";
    private int gameState = 0;
    private User host;
    private User guest;
    private String hostUID = "";
    private String hostFirstName = "";
    private String hostName = "";
    private String guestFirstName = "";
    private String guestName = "";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onlineGameViewModel = ViewModelProviders.of(this, new OnlineUsersViewModelFactory(gameId)).get(OnlineGameViewModel.class);
        onlineGameBinding = DataBindingUtil.setContentView(this, R.layout.activity_online_game);
        onlineGameBinding.setViewModel(onlineGameViewModel);
        onlineGameBinding.setLifecycleOwner(this);

        setInitialVisibility();
        animateViews();

        if (getIntent().getExtras() != null) {

            gameId = Objects.requireNonNull(getIntent().getExtras().get(GAME_ID)).toString();

            reference.child("multiplayer").child(gameId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Game game = dataSnapshot.getValue(Game.class);

                    if (game != null) {

                        host = game.getHost();
                        guest = game.getGuest();

                        hostUID = host.getUID();

                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        String firebaseUID = firebaseUser.getUid();

                        if (hostUID.equals(firebaseUID)) {

                            hostFirstName = host.getFirstName();
                            hostName = host.getName();

                            guestFirstName = guest.getFirstName();
                            guestName = guest.getName();

                            if (TextUtils.isEmpty(hostFirstName)) {
                                onlineGameBinding.player1Text.setText(hostName);
                            } else {
                                onlineGameBinding.player1Text.setText(hostFirstName);
                            }

                            if (TextUtils.isEmpty(guestFirstName)) {
                                onlineGameBinding.player2Text.setText(guestName);
                            } else {
                                onlineGameBinding.player2Text.setText(guestFirstName);
                            }

                        } else {

                            hostFirstName = host.getFirstName();
                            hostName = host.getName();

                            guestFirstName = guest.getFirstName();
                            guestName = guest.getName();

                            if (TextUtils.isEmpty(guestFirstName)) {
                                onlineGameBinding.player1Text.setText(guestName);
                            } else {
                                onlineGameBinding.player1Text.setText(guestFirstName);
                            }

                            if (TextUtils.isEmpty(hostFirstName)) {
                                onlineGameBinding.player2Text.setText(hostName);
                            } else {
                                onlineGameBinding.player2Text.setText(hostFirstName);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        gameState = 1;

       /* if (requestType.equals("From")) {
            // the player who sends the request plays with X
            myGameSignIn = "O";
            onlineGameBinding.player1Text.setText("Your turn");
            onlineGameBinding.player2Text.setText("Your turn");
            reference.child("playing").child(playerSession).child("turn").setValue(opponentFirstName);
        } else {
            myGameSignIn = "X";
            onlineGameBinding.player1Text.setText(opponentFirstName + "\'s turn");
            onlineGameBinding.player1Text.setText(opponentFirstName + "\'s turn");
            reference.child("playing").child(playerSession).child("turn").setValue(opponentFirstName);
        }*/

        reference.child("playing").child(gameId).child("turn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    String value = (String) dataSnapshot.getValue();
                    if (value.equals(userName)) {

                        onlineGameBinding.player1Text.setText("Your turn");
                        onlineGameBinding.player2Text.setText("Your turn");
                        setEnableClick(true);
                        activePlayer = 1;
                    } else if (value.equals(opponentFirstName)) {

                        onlineGameBinding.player1Text.setText(opponentFirstName + "\'s turn");
                        onlineGameBinding.player1Text.setText(opponentFirstName + "\'s turn");
                        setEnableClick(false);
                        activePlayer = 2;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("playing").child(gameId).child("game").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    // TO DO
                    onlineGameBinding.player1Result.clearComposingText();
                    onlineGameBinding.player2Result.clearComposingText();
                    activePlayer = 2;
                    HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                    if (map != null) {
                        String value = "";
                        String firstPlayer = userName;
                        for (String key : map.keySet()) {
                            value = (String) map.get(key);
                            if (value.equals(userName)) {
                                activePlayer = 2;
                            } else {
                                activePlayer = 1;
                            }
                            firstPlayer = value;
                            String[] splitID = key.split(":");
                            // TO DO
                            //otherPlayer.(Integer.parseInt(splitID[1]));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * method used for the chips to enter the board
     *
     * @param view which represents one of the nine cells of the board
     */
    public void dropInOnline(View view) {

        // animate
        ImageView counter = (ImageView) view;
        counter.setTranslationY(-1000f);
        counter.animate().translationYBy(1000f).setDuration(300);

        // play
        onlineGameViewModel.play(Integer.parseInt((String) view.getTag()));

        if (onlineGameViewModel.checkForWin()) {
            onlineGameViewModel.gameEnded();
            isWinner();
        } else if (onlineGameViewModel.fullBoard()) {
            showToast(getString(R.string.draw));
        } else {
            onlineGameViewModel.togglePlayer();
        }
    }


    public void isWinner() {
        onlineGameViewModel.gameLiveData.observe(this, new Observer<Game>() {
            @Override
            public void onChanged(Game game) {
                switch (game.getGameResult()) {
                    case 1:
                        showToast("Player 1 has won");
                        break;
                    case 2:
                        showToast("Player 2 has won");
                        break;
                    case 3:
                        showToast("It's a draw");
                        break;
                    default:
                        showToast("Game hasn't started");
                }
            }
        });
    }

    /**
     * auxiliary method which displays a toast message only by giving the message as String parameter
     *
     * @param message
     */
    public void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void gameBoardClick(View view) {

        ImageView selectedImage = (ImageView) view;

        if (gameId.length() <= 0) {
            Intent intent = new Intent(getApplicationContext(), OnlineGameActivity.class);
            startActivity(intent);
            finish();
        } else {
            int selectedBlock = 0;
            switch (selectedImage.getId()) {
                case R.id.block_1:
                    selectedBlock = 1;
                    break;
                case R.id.block_2:
                    selectedBlock = 2;
                    break;
                case R.id.block_3:
                    selectedBlock = 3;
                    break;

                case R.id.block_4:
                    selectedBlock = 4;
                    break;
                case R.id.block_5:
                    selectedBlock = 5;
                    break;
                case R.id.block_6:
                    selectedBlock = 6;
                    break;

                case R.id.block_7:
                    selectedBlock = 7;
                    break;
                case R.id.block_8:
                    selectedBlock = 8;
                    break;
                case R.id.block_9:
                    selectedBlock = 9;
                    break;
            }
            reference.child("playing").child(gameId).child("game").child("block" + selectedBlock).setValue(userName);
            reference.child("playing").child(gameId).child(gameId).child("turn").setValue(opponentFirstName);
            setEnableClick(false);
            activePlayer = 2;

            playGame(selectedBlock, selectedImage);
        }
    }

    public void playGame(int selectedBlock, ImageView selectedImage) {

        if (gameState == 1) {
            if (activePlayer == 1) {
                selectedImage.setImageResource(R.drawable.ic_cross);
                player1.add(selectedBlock);
            } else if (activePlayer == 2) {
                selectedImage.setImageResource(R.drawable.ic_zero);
                player2.add(selectedBlock);
            }
            selectedImage.setEnabled(false);
            checkWinner();
        }
    }

    void checkWinner() {
        int winner = 0;

        /********* for Player 1 *********/
        if (player1.contains(1) && player1.contains(2) && player1.contains(3)) {
            winner = 1;
        }
        if (player1.contains(4) && player1.contains(5) && player1.contains(6)) {
            winner = 1;
        }
        if (player1.contains(7) && player1.contains(8) && player1.contains(9)) {
            winner = 1;
        }

        if (player1.contains(1) && player1.contains(4) && player1.contains(7)) {
            winner = 1;
        }
        if (player1.contains(2) && player1.contains(5) && player1.contains(8)) {
            winner = 1;
        }
        if (player1.contains(3) && player1.contains(6) && player1.contains(9)) {
            winner = 1;
        }

        if (player1.contains(1) && player1.contains(5) && player1.contains(9)) {
            winner = 1;
        }
        if (player1.contains(3) && player1.contains(5) && player1.contains(7)) {
            winner = 1;
        }


        /********* for Player 2 *********/
        if (player2.contains(1) && player2.contains(2) && player2.contains(3)) {
            winner = 2;
        }
        if (player2.contains(4) && player2.contains(5) && player2.contains(6)) {
            winner = 2;
        }
        if (player2.contains(7) && player2.contains(8) && player2.contains(9)) {
            winner = 2;
        }

        if (player2.contains(1) && player2.contains(4) && player2.contains(7)) {
            winner = 2;
        }
        if (player2.contains(2) && player2.contains(5) && player2.contains(8)) {
            winner = 2;
        }
        if (player2.contains(3) && player2.contains(6) && player2.contains(9)) {
            winner = 2;
        }

        if (player2.contains(1) && player2.contains(5) && player2.contains(9)) {
            winner = 2;
        }
        if (player2.contains(3) && player2.contains(5) && player2.contains(7)) {
            winner = 2;
        }


        if (winner != 0 && gameState == 1) {
            if (winner == 1) {
                ShowAlert(opponentFirstName + " is winner");
            } else if (winner == 2) {
                ShowAlert("You won the game");
            }
            gameState = 2;
        }

        ArrayList<Integer> emptyBlocks = new ArrayList<Integer>();
        for (int i = 1; i <= 9; i++) {
            if (!(player1.contains(i) || player2.contains(i))) {
                emptyBlocks.add(i);
            }
        }
        if (emptyBlocks.size() == 0) {
            if (gameState == 1) {
                AlertDialog.Builder b = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                ShowAlert("Draw");
            }
            gameState = 3;
        }
    }

    public void setEnableClick(boolean b) {

        onlineGameBinding.block1.setClickable(b);
        onlineGameBinding.block2.setClickable(b);
        onlineGameBinding.block3.setClickable(b);

        onlineGameBinding.block4.setClickable(b);
        onlineGameBinding.block5.setClickable(b);
        onlineGameBinding.block6.setClickable(b);

        onlineGameBinding.block7.setClickable(b);
        onlineGameBinding.block8.setClickable(b);
        onlineGameBinding.block9.setClickable(b);
    }

    public void ShowAlert(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }


    private void hideView(View view) {
        GridLayout gridLayout = (GridLayout) view.getParent();
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            if (view == gridLayout.getChildAt(i)) {
                gridLayout.removeViewAt(i);
                break;
            }
        }
    }

    public void setInitialVisibility() {
        onlineGameBinding.block1.setVisibility(View.INVISIBLE);
        onlineGameBinding.block2.setVisibility(View.INVISIBLE);
        onlineGameBinding.block3.setVisibility(View.INVISIBLE);
        onlineGameBinding.block4.setVisibility(View.INVISIBLE);
        onlineGameBinding.block5.setVisibility(View.INVISIBLE);
        onlineGameBinding.block6.setVisibility(View.INVISIBLE);
        onlineGameBinding.block7.setVisibility(View.INVISIBLE);
        onlineGameBinding.block8.setVisibility(View.INVISIBLE);
        onlineGameBinding.block9.setVisibility(View.INVISIBLE);

        onlineGameBinding.vsImageView.setVisibility(View.VISIBLE);
        onlineGameBinding.profilePictureHost.setVisibility(View.VISIBLE);
        onlineGameBinding.profilePictureGuest.setVisibility(View.VISIBLE);

        onlineGameBinding.player1Text.setVisibility(View.INVISIBLE);
        onlineGameBinding.player2Text.setVisibility(View.INVISIBLE);
        onlineGameBinding.player1Result.setVisibility(View.INVISIBLE);
        onlineGameBinding.player2Result.setVisibility(View.INVISIBLE);
    }

    public void animateViews() {
        onlineGameBinding.vsImageView.animate().alpha(0f).setDuration(3000);
        onlineGameBinding.profilePictureHost.animate().alpha(0f).setDuration(3000);
        onlineGameBinding.profilePictureGuest.animate().alpha(0f).setDuration(3000);

        onlineGameBinding.block1.setVisibility(View.VISIBLE);
        onlineGameBinding.block2.setVisibility(View.VISIBLE);
        onlineGameBinding.block3.setVisibility(View.VISIBLE);
        onlineGameBinding.block4.setVisibility(View.VISIBLE);
        onlineGameBinding.block5.setVisibility(View.VISIBLE);
        onlineGameBinding.block6.setVisibility(View.VISIBLE);
        onlineGameBinding.block7.setVisibility(View.VISIBLE);
        onlineGameBinding.block8.setVisibility(View.VISIBLE);
        onlineGameBinding.block9.setVisibility(View.VISIBLE);

        onlineGameBinding.player1Text.postDelayed(new Runnable() {
            public void run() {
                onlineGameBinding.player1Text.setVisibility(View.VISIBLE);
            }
        }, 3000);

        onlineGameBinding.player2Text.postDelayed(new Runnable() {
            public void run() {
                onlineGameBinding.player2Text.setVisibility(View.VISIBLE);
            }
        }, 3000);

        onlineGameBinding.player1Result.postDelayed(new Runnable() {
            public void run() {
                onlineGameBinding.player1Result.setVisibility(View.VISIBLE);
            }
        }, 3000);

        onlineGameBinding.player2Result.postDelayed(new Runnable() {
            public void run() {
                onlineGameBinding.player2Result.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }
}