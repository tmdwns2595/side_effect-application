package org.jjcouple.drug.etc;

import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.github.ybq.android.spinkit.SpinKitView;

import org.jjcouple.drug.R;
import org.jjcouple.drug.UserManagement.SignUp;

public class loading extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       setContentView(R.layout.loading_activity);

        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getRealSize(size); // or getSize(size)
        int width = size.x;
        int height = size.y;
        Log.d("화면의 크기 출력 : ", width + ", " + height);

        LottieAnimationView title = (LottieAnimationView)findViewById(R.id.lottie_title);
        LottieAnimationView logo = (LottieAnimationView)findViewById(R.id.lottie_logo);
        SpinKitView loading_ani = (SpinKitView)findViewById(R.id.loading_animation);

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height * 6 / 100);
        title.setLayoutParams(params1);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height * 40 / 100);
        logo.setLayoutParams(params2);
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height * 10 / 100);
        loading_ani.setLayoutParams(params3);

        Handler handler;
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }
}
