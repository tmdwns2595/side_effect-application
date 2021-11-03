package org.jjcouple.drug.etc;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jjcouple.drug.R;
import org.w3c.dom.Text;

public class Decagon_project extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decagon_project);

        LinearLayout decagon_git = (LinearLayout)findViewById(R.id.decagon_git);
        LinearLayout decagon_paper = (LinearLayout)findViewById(R.id.decagon_paper);

        decagon_git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://github.com/mims-harvard/decagon";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()));
                startActivity(intent);
            }
        });

        decagon_paper.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String url = "https://academic.oup.com/bioinformatics/article/34/13/i457/5045770";
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()));
                startActivity(intent2);
            }
        });

    }
}
