package org.jjcouple.drug.etc;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;

import org.jjcouple.drug.R;
import org.w3c.dom.Text;

import java.util.HashMap;

public class detail_side_effect extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_side);

        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getRealSize(size); // or getSize(size)

        int width = size.x;
        int height = size.y;

        TextView detail_title = (TextView)findViewById(R.id.detail_title);
        LottieAnimationView detail_animation = (LottieAnimationView)findViewById(R.id.detail_animation);
        LottieAnimationView below_animation = (LottieAnimationView)findViewById(R.id.below_animation);

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height * 4 / 100);
        detail_title.setLayoutParams(params1);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(width * 36 / 100, height * 20 / 100);
        detail_animation.setLayoutParams(params2);
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(width * 6 / 100, height * 4 / 100);
        below_animation.setLayoutParams(params3);

        Intent intent = getIntent();
        HashMap<String, String> side_name = (HashMap<String, String>)intent.getSerializableExtra("Side_name");
        HashMap<String, String> detail_side = (HashMap<String, String>)intent.getSerializableExtra("Detail_side");

        Log.e("받아오는 곳이 이상한거니???", side_name + ", " + detail_side);

        TextView side_text = (TextView)findViewById(R.id.side_name);
        TextView detail_text = (TextView)findViewById(R.id.detail_side);

        side_text.setText("상호작용 : " + side_name.get("side_name"));
        detail_text.setText("부작용 상세 설명 : " + detail_side.get("detail_side"));
    }
}
