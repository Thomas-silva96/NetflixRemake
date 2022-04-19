package com.tsdroiddeveloper.netflixremake.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.tsdroiddeveloper.netflixremake.model.Movie;
import com.tsdroiddeveloper.netflixremake.model.MovieDatail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MovieDetailTask extends AsyncTask<String, Void, MovieDatail> {

    private final WeakReference<Context> context;
    private ProgressDialog dialog;
    private MovieDetailLoader movieDetailLoader;

    public MovieDetailTask(Context context) {
        this.context = new WeakReference<>(context);
    }

    public void setMovieDetailLoader(MovieDetailLoader movieDetailLoader) {
        this.movieDetailLoader = movieDetailLoader;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Context context = this.context.get();

        if (context != null)
            dialog = ProgressDialog.show(context, "Carregando", "", true);
    }

    @Override
    protected MovieDatail doInBackground(String... params) {
        String url = params[0];

        try {
            URL requestUrl = new URL(url);

            HttpsURLConnection urlConnection = (HttpsURLConnection) requestUrl.openConnection();
            urlConnection.setReadTimeout(2000);
            urlConnection.setConnectTimeout(2000);

            int responseCode = urlConnection.getResponseCode();
            if (responseCode > 400) {
                throw new IOException("Erro na comunicação do servidor");
            }

            urlConnection.getInputStream();

            BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());

            String jsonAsString = toString(in);

            MovieDatail movieDatail = getMovieDetail(new JSONObject(jsonAsString));
            in.close();

            return movieDatail;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public MovieDatail getMovieDetail(JSONObject json) throws JSONException{
        int id = json.getInt("id");
        String title = json.getString("title");
        String desc = json.getString("desc");
        String cast = json.getString("cast");
        String coverUrl = json.getString("cover_url");

        List<Movie> movies = new ArrayList<>();
        JSONArray movieArray = json.getJSONArray("movie");
        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject movie = movieArray.getJSONObject(i);
            String c = movie.getString("cover_url");
            int idSimilar = movie.getInt("id");

            Movie similar = new Movie();
            similar.setId(idSimilar);
            similar.setCoverUrl(c);

            movies.add(similar);
        }

        Movie movie = new Movie();
        movie.setId(id);
        movie.setCoverUrl(coverUrl);
        movie.setTitle(title);
        movie.setDesc(desc);
        movie.setCast(cast);

        return new MovieDatail(movie, movies);
    }

    @Override
    protected void onPostExecute(MovieDatail movieDatail) {
        super.onPostExecute(movieDatail);
        dialog.dismiss();

        if (movieDetailLoader != null)
            movieDetailLoader.onResult(movieDatail);
    }

    private String toString(InputStream is) throws IOException {
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int lidos;
        while ((lidos = is.read(bytes)) > 0) {
            baos.write(bytes, 0, lidos);
        }

        return baos.toString();
    }

    public interface MovieDetailLoader {
        void onResult(MovieDatail movieDatail);
    }
}
