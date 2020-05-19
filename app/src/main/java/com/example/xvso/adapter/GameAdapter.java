package com.example.xvso.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xvso.Objects.Game;
import com.example.xvso.Objects.User;
import com.example.xvso.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.xvso.R.id.join_game_text_view;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private ArrayList<Game> mGameItemsList;

    private JoinGameClick listener;

    private User user = new User();

    private String UID = "";

    // constructor 1 (all games)
    public GameAdapter(JoinGameClick listener, ArrayList<Game> mGameItemsList, User user) {
        this.listener = listener;
        this.mGameItemsList = mGameItemsList;
        this.user = user;
    }


    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_item, parent, false);
        GameViewHolder gameViewHolder = new GameViewHolder(view);
        return gameViewHolder;
    }

    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game currentGame = mGameItemsList.get(position);

        if (currentGame != null) {

            String key = currentGame.getKey();
            User host = currentGame.getHost();
            UID = host.getUID();

            User guest = currentGame.getGuest();

            if (!TextUtils.isEmpty(host.getImageUrl())) {
                Picasso.get().load(host.getImageUrl()).into(holder.profilePicture);
            }

            holder.gameNumber.setText(String.valueOf(position + 1));

            if (TextUtils.isEmpty(host.getFirstName())) {
                holder.userName.setText(host.getName());
            } else {
                holder.userName.setText(host.getFirstName());
            }

            // compare the host uid with the current user uid in the adapter.
            if (UID.equals(user.getUID())) {
                holder.joinGame.setVisibility(View.INVISIBLE);
                mGameItemsList.set(0, currentGame);
            } else {
                holder.joinGame.setText("JOIN");
                holder.joinGame.setVisibility(View.VISIBLE);
                holder.joinGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onJoinGameClick(key);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mGameItemsList == null ? 0 : mGameItemsList.size();
    }

    public GameAdapter(ArrayList<Game> gameItemsList) {
        mGameItemsList = gameItemsList;
    }

    public interface JoinGameClick {
        void onJoinGameClick(String key);
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {

        public ImageView profilePicture;
        public TextView gameNumber;
        public TextView userName;
        public TextView joinGame;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePicture = itemView.findViewById(R.id.profile_image_view);
            gameNumber = itemView.findViewById(R.id.first_line_text_view);
            userName = itemView.findViewById(R.id.second_line_text_view);
            joinGame = itemView.findViewById(join_game_text_view);
        }
    }
}
