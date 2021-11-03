package org.jjcouple.drug.etc;

import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jjcouple.drug.R;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class Result_loading extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_loading);

        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getRealSize(size); // or getSize(size)

        int width = size.x;
        int height = size.y;

        TextView result_text = (TextView)findViewById(R.id.side_title);
        LottieAnimationView result_animation = (LottieAnimationView)findViewById(R.id.side_animation);
        SpinKitView result_loading = (SpinKitView)findViewById(R.id.side_loading);

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height * 25 / 100);
        result_text.setLayoutParams(params1);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height * 50 / 100);
        result_animation.setLayoutParams(params2);
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(width * 20 / 100, height * 15 / 100);
        result_loading.setLayoutParams(params3);

        Intent intent = getIntent();
        ArrayList<String> arr = intent.getStringArrayListExtra("Drug");
        HashMap<String, String> side = new HashMap<String, String>();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("dataset/drug");

        Handler handler;
        handler = new Handler();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Drug drug1 = snapshot1.getValue(Drug.class);
                    for (int i = 0; i < arr.size(); i++) {
                        for (int j = i + 1; j < arr.size(); j++) {
                            if ((drug1.getDrug1().equals(arr.get(i)) && drug1.getDrug2().equals(arr.get(j)))
                                    || (drug1.getDrug2().equals(arr.get(i)) && drug1.getDrug1().equals(arr.get(j)))) {
                                side.put(drug1.getDrug1() + " - " + drug1.getDrug2(), drug1.getSide());
                                side.put(drug1.getDrug2() + " - " + drug1.getDrug1(), drug1.getSide());
                                continue;
                            }
                        }
                    }
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent2 = new Intent(getApplicationContext(), Side_Effect.class);
                        intent2.putExtra("Drug_arr", arr);
                        intent2.putExtra("Drug_side", side);
                        startActivity(intent2);
                        finish();
                    }
                }, 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivity", String.valueOf((error.toException())));
            }
        });
    }
}
