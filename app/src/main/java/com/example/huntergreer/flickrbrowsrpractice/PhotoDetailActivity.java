package com.example.huntergreer.flickrbrowsrpractice;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        activateToolbar(true);

        Photo photo = (Photo) getIntent().getSerializableExtra(PHOTO_TRANSFER);
        if (photo != null) {
            TextView title = (TextView) findViewById(R.id.photo_detail_title);
            TextView author = (TextView) findViewById(R.id.photo_detail_author);
            TextView dateTaken = (TextView) findViewById(R.id.photo_detail_date_taken);
            ImageView imageView = (ImageView) findViewById(R.id.photo_detail_image);

            Resources resources = getResources();
            title.setText(resources.getString(R.string.photo_title_text, photo.getTitle()));
            author.setText(resources.getString(R.string.photo_author_text, photo.getTitle()));
            dateTaken.setText(resources.getString(R.string.photo_date_taken_text, photo.getTitle()));
            Picasso.with(this).load(photo.getImage()).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(imageView);
        }
    }
}
