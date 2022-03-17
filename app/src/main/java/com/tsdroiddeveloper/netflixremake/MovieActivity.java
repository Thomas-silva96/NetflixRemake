package com.tsdroiddeveloper.netflixremake;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tsdroiddeveloper.netflixremake.model.Movie;

import java.util.ArrayList;
import java.util.List;


public class MovieActivity extends AppCompatActivity {

    private TextView txtTitle;
    private TextView txtDesc;
    private TextView txtCast;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        txtTitle = findViewById(R.id.text_view_title);
        txtDesc = findViewById(R.id.text_View_desc);
        txtCast = findViewById(R.id.text_View_cast);
        recyclerView = findViewById(R.id.recyclerview_similar);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        }

        LayerDrawable drawable =
                (LayerDrawable) ContextCompat.getDrawable(this, R.drawable.shadows);
        if (drawable != null) {
            Drawable movieCover = ContextCompat.getDrawable(this, R.drawable.movie_4);
            drawable.setDrawableByLayerId(R.id.cover_drawable, movieCover);
            ((ImageView) findViewById(R.id.imageView_cover)).setImageDrawable(drawable);
        }
        txtTitle.setText("Batman Begins");
        txtDesc.setText("O jovem Bruce Wayne viaja para o Extremo Oriente, onde recebe treinamento em artes marciais do mestre Henri Ducard, um membro da misteriosa Liga das Sombras. Quando Ducard revela que a verdadeira proposta da Liga é a destruição completa da cidade de Gotham, Wayne retorna à sua cidade com o intuito de livrá-la de criminosos e assassinos. Com a ajuda do mordomo Alfred e do expert Lucius Fox, nasce Batman.");
        txtCast.setText(getString(R.string.cast, "Christian Bale" + "Katie Holmes" + "Michael Caine" + "Cillian Murphy" + "Liam Neeson" + "Morgan Freeman" + "Ken Watanabe" + "Gary Oldman\n"));

        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Movie movie = new Movie();
            movies.add(movie);
        }

        recyclerView.setAdapter(new MovieAdapter(movies));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

    }

    static class MovieHolder extends RecyclerView.ViewHolder {

        final ImageView imageViewCover;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCover = itemView.findViewById(R.id.imageView_cover);
        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {

        private final List<Movie> movies;

        private MovieAdapter(List<Movie> movies) {
            this.movies = movies;
        }

        @NonNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieHolder(getLayoutInflater().inflate(R.layout.movie_item_similar, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
            Movie movie = movies.get(position);
            // holder.imageViewCover.setImageResource(movie.getCoverUrl());
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }

}