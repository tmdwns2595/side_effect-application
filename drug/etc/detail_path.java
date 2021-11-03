package org.jjcouple.drug.etc;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jjcouple.drug.R;

import java.util.HashMap;

public class detail_path extends AppCompatActivity {
    public String[] arr;
    public HashMap<String, Float> rating = new HashMap<String, Float>();
//    public HashMap<String, String> total_side = new HashMap<String, String>();
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_path);

        load_rate();

        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getRealSize(size); // or getSize(size)

        int width = size.x;
        int height = size.y;

        TextView detail_title = (TextView)findViewById(R.id.detail_title);
        LottieAnimationView detail_animation = (LottieAnimationView)findViewById(R.id.detail_animation);
        LottieAnimationView below_animation = (LottieAnimationView)findViewById(R.id.below_animation);
        LinearLayout pass_li = (LinearLayout)findViewById(R.id.pass_li);
        RatingBar ratingBar = (RatingBar)findViewById(R.id.star);
        Button decagon_btn = (Button)findViewById(R.id.Decagon_btn);

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height * 4 / 100);
        detail_title.setLayoutParams(params1);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(width * 36 / 100, height * 20 / 100);
        detail_animation.setLayoutParams(params2);
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(width * 6 / 100, height * 4 / 100);
        below_animation.setLayoutParams(params3);

        Intent intent = getIntent();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("dataset/drug");

        HashMap<String, String> side_name = (HashMap<String, String>)intent.getSerializableExtra("Side_name");
        HashMap<String, String> side_pass = (HashMap<String, String>)intent.getSerializableExtra("Side_pass");

        TextView side_text = (TextView)findViewById(R.id.side_name);
        side_text.setText("예상되는 상호작용\n" + side_name.get("side_name"));

        ratingBar.setRating(rating.get(side_name.get("side_name")));

        decagon_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Decagon_project.class);
                startActivity(intent);
            }
        });

        arr = side_pass.get("side_pass").split(",");
        if(arr[0].equals("6개 이상의 긴 경로가 있는 것으로 추정됩니다.")){
            LinearLayout list_li = new LinearLayout(getBaseContext());
            list_li.setOrientation(LinearLayout.VERTICAL);
            list_li.setGravity(Gravity.CENTER);
            list_li.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView pass = (TextView)findViewById(R.id.side_pass);
            pass.setVisibility(View.GONE);
            TextView pass_text = new TextView(getApplicationContext());
            pass_text.setText(arr[0]);
            pass_text.setTextColor(Color.RED);
            pass_text.setGravity(Gravity.CENTER);
            pass_text.setTextSize(20);
            pass_text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            list_li.addView(pass_text);
            pass_li.addView(list_li);
        }
        else{
            TextView pass = (TextView)findViewById(R.id.side_pass);
            pass.setText("부작용과 관련된 약물경로는 아래와 같이 추정된다.");

            LinearLayout list_li = new LinearLayout(getBaseContext());
            list_li.setOrientation(LinearLayout.VERTICAL);
            list_li.setGravity(Gravity.CENTER);
            list_li.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            for(int i=0;i<arr.length;i++){
                if(i != arr.length - 1){
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, 80);
                    TextView pass_text = new TextView(getApplicationContext());
                    ImageView pass_img = new ImageView(getApplicationContext());
                    pass_img.setBackgroundResource(R.drawable.pass_down);
                    pass_img.setLayoutParams(params);
                    pass_text.setText(arr[i]);
                    pass_text.setTextColor(Color.RED);
                    pass_text.setGravity(Gravity.CENTER);
                    pass_text.setTextSize(20);
                    pass_text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    list_li.addView(pass_text);
                    list_li.addView(pass_img);
                }
                else{
                    TextView pass_text = new TextView(getApplicationContext());
                    pass_text.setText(arr[i]);
                    pass_text.setTextColor(Color.RED);
                    pass_text.setGravity(Gravity.CENTER);
                    pass_text.setTextSize(20);
                    pass_text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    list_li.addView(pass_text);
                }
            }
            pass_li.addView(list_li);
        }
    }

    public void load_rate(){
        rating.put("프레즈코빅스정 <-> 제파티어정", (float)3);
        rating.put("스타레보필름코팅정125/31.25/200밀리그램 <-> 유시락스시럽", (float)2.98);
        rating.put("오니바이드주 <-> 에보타즈정", (float)2.96);
        rating.put("리납틴듀오정2.5/500밀리그램 <-> 제로시드정20/1100밀리그램", (float)2.94);
        rating.put("비키라정 <-> 트루패스구강붕해정8밀리그램", (float)2.92);
        rating.put("인베가트린자주사 <-> 도비드정", (float)2.9);
        rating.put("퍼마곤주120밀리그램 <-> 순베프라캡슐100밀리그램", (float)2.88);
        rating.put("콤비글라이즈서방정5/1000밀리그램 <-> 엑스비라정", (float)2.86);
        rating.put("에보타즈정 <-> 제파티어정", (float)2.84);
        rating.put("영풍니페디핀연질캡슐 <-> 휴메딕스덱사메타손포스페이트이나트륨주사", (float)2.82);
        rating.put("안제메트주사 <-> 아미로정", (float)2.8);
        rating.put("자이카디아캡슐150밀리그램 <-> 다비트란캡슐150밀리그램", (float)2.78);
        rating.put("비키라정 <-> 순베프라캡슐100밀리그램", (float)2.76);
        rating.put("록스펜주 <-> 코러스오메프라졸캡슐10mg", (float)2.74);
        rating.put("코오롱브렉신정10밀리그램 <-> 판베시서방캡슐30밀리그램", (float)2.72);
        rating.put("베믈리디정 <-> 제피드정200밀리그램", (float)2.7);
        rating.put("트리멕정 <-> 이미실키트주사250밀리그람", (float)2.68);
        rating.put("사이폴-엔연질캡슐50밀리그램 <-> 스타레보필름코팅정125/31.25/200밀리그램", (float)2.66);
        rating.put("튜비스정 <-> 입랜스캡슐100mg", (float)2.64);
        rating.put("에이신에스외용액 <-> 순베프라캡슐100밀리그램", (float)2.62);
        rating.put("싸이문주 <-> 알엠서방정750/5밀리그램", (float)2.6);
        rating.put("제라부딘정 <-> 마인트롤정", (float)2.58);
        rating.put("아스토건조시럽 <-> 에피네프린프리필드시린지주1:10000", (float)2.56);
        rating.put("테넬리아엠서방정20/1000밀리그램 <-> 아그레녹스서방캡슐", (float)2.54);
        rating.put("피타렉스캡슐0.5밀리그램 <-> 실타니딘정1밀리그램", (float)2.52);
        rating.put("오니바이드주 <-> 화레스톤정40밀리그램", (float)2.5);
        rating.put("쎄글루로메트정7.5/500밀리그램 <-> 밀타오디정7.5mg", (float)2.48);
        rating.put("시그마트주12밀리그람 <-> 에르고탄주", (float)2.46);
        rating.put("이미실키트주사250밀리그람 <-> 디어미순정", (float)2.44);
        rating.put("테포비어정 <-> 타크롤캡슐1밀리그램", (float)2.42);
        rating.put("슈프라제정 <-> 멜로텍스캡슐15mg", (float)2.4);
        rating.put("보스민액 <-> 테노퀄정", (float)2.38);
        rating.put("대한뉴팜메로페넴주500mg <-> 레플록스주", (float)2.36);
        rating.put("메가벡정400밀리그램 <-> 이오센스370주", (float)2.34);
        rating.put("한화쿼테아핀정300밀리그램 <-> 티비케이정50밀리그램", (float)2.32);
        rating.put("하나염산페치딘주사25밀리그람 <-> 알모그란정", (float)2.3);
        rating.put("미가드정2.5mg <-> 한올리네졸리드주2밀리그램/밀리리터", (float)2.28);
        rating.put("로벨리토정300/10밀리그램 <-> 한올덱사메타손정", (float)2.26);
        rating.put("마이팜케토코나졸정 <-> 경동린코마이신염산염수화물주", (float)2.24);
        rating.put("옥시마이신정500밀리그람 <-> 엑스비라정", (float)2.22);
        rating.put("대웅프리미돈정 <-> 아이오피딘0.5%점안액", (float)2.2);
        rating.put("파미레이200주사액 <-> 푸로질캅셀", (float)2.18);
        rating.put("환인크로픽솔정25밀리그램 <-> 하보니정", (float)2.16);
        rating.put("리납틴듀오정2.5/500밀리그램 <-> 가스크린액", (float)2.14);
        rating.put("테라싸이클린캅셀250밀리그람 <-> 하보니정", (float)2.12);
        rating.put("레플록스주 <-> 케트로주사", (float)2.1);
        rating.put("삼양아트로핀주사제 <-> 베믈리디정", (float)2.08);
        rating.put("레포리아정500mg <-> 셉타네스트주1/200,000", (float)2.06);
        rating.put("판베시서방캡슐30밀리그램 <-> 스토파제주", (float)2.04);
        rating.put("에나폰정5밀리그램 <-> 테넬리아엠서방정20/1000밀리그램", (float)2.02);
        rating.put("미가텍정 <-> 디스톨정", (float)2);
        rating.put("자바토정2.5밀리그램 <-> 에라이신건조시럽5%", (float)1.98);
        rating.put("록스펜주 <-> 슈가메트서방정2.5/500밀리그램", (float)1.96);
        rating.put("푸리딘정 <-> 디스톨정", (float)1.94);
        rating.put("구주염산모르핀주 <-> 록소드린현탁액", (float)1.92);
        rating.put("시노카자일로나잘스프레이액0.1% <-> 일성이솦틴주사", (float)1.9);
        rating.put("넥시움주 <-> 신플랙스세이프정500/20밀리그램", (float)1.88);
        rating.put("푸리딘정 <-> 레바넥스정200밀리그램", (float)1.86);
        rating.put("에듀란트정25밀리그램 <-> 리트모놈정주70mg/20ml", (float)1.84);
        rating.put("피타렉스캡슐0.5밀리그램 <-> 대한염화칼륨-20", (float)1.82);
        rating.put("아그레녹스서방캡슐 <-> 임플라논임플란트", (float)1.8);
        rating.put("명인피모짓4밀리그람정 <-> 엠빅스정100밀리그램", (float)1.78);
        rating.put("칼도롤주사액 <-> 한올리네졸리드주2밀리그램/밀리리터", (float)1.76);
        rating.put("동화세레콕시브캡슐200밀리그램 <-> 제이모다정200밀리그램", (float)1.74);
        rating.put("제이모다정200밀리그램 <-> 베아로신서방정0.4밀리그램", (float)1.72);
        rating.put("부벤톨이지할러200 <-> 크렌블시럽", (float)1.7);
        rating.put("오엠피에스캡슐40밀리그램 <-> 비키라정", (float)1.68);
        rating.put("경동겐타마이신황산염주160밀리그람 <-> 오메라스캡슐", (float)1.66);
        rating.put("컴비비어정 <-> 프레미나정0.625밀리그램", (float)1.64);
        rating.put("에르고탄주 <-> 알파돌연질캡슐", (float)1.62);
        rating.put("수마트란정25밀리그람 <-> 서튜러정100밀리그램", (float)1.6);
        rating.put("보그메트정0.2/500밀리그램 <-> 에필렙톨정100밀리그램", (float)1.58);
        rating.put("노피린주 <-> 산도스아지트로마이신정500밀리그램", (float)1.56);
        rating.put("시타메폴엑스알서방정100/1000밀리그램 <-> 구주염산모르핀주", (float)1.54);
        rating.put("종근당염산에페드린정 <-> 타드린캅셀300밀리그람", (float)1.52);
        rating.put("레토프라정40밀리그램 <-> 렉토지연고", (float)1.5);
        rating.put("삼양툴로부테롤패취2밀리그램 <-> 카타스주", (float)1.48);
        rating.put("신플랙스세이프정500/20밀리그램 <-> 오로릭스정150밀리그람", (float)1.46);
        rating.put("에듀란트정25밀리그램 <-> 알파아세클로페낙정", (float)1.44);
        rating.put("실타니딘정1밀리그램 <-> 올트릴앰플주사액", (float)1.42);
        rating.put("리마클로건조시럽250mg/5ml <-> 제네틱스350주사", (float)1.4);
        rating.put("트레인연질캡슐 <-> 소프라정15밀리그램", (float)1.38);
        rating.put("아디센정50밀리그람 <-> 오엠피에스캡슐40밀리그램", (float)1.36);
        rating.put("한독세로자트정30밀리그램 <-> 테노퀄정", (float)1.34);
        rating.put("화레스톤정40밀리그램 <-> 제로시드정20/1100밀리그램", (float)1.32);
        rating.put("케랄주 <-> 뉴바스트정80밀리그람", (float)1.3);
        rating.put("마인트롤정 <-> 메트비정", (float)1.28);
        rating.put("데파코트서방정500㎎ <-> 자이카디아캡슐150밀리그램", (float)1.26);
        rating.put("실타니딘정1밀리그램 <-> 올트릴앰플주사액", (float)1.24);
        rating.put("사노렉스정 <-> 덱실란트디알캡슐60밀리그램", (float)1.22);
        rating.put("리메타손주 <-> 대원페노바르비탈정100밀리그람", (float)1.2);
        rating.put("카타스주 <-> 부광미졸렌정10밀리그램", (float)1.18);
        rating.put("유니독시캅셀 <-> 브이펜드정50밀리그람", (float)1.16);
        rating.put("시노카자일로나잘스프레이액0.1% <-> 데스베라서방정100밀리그램", (float)1.14);
        rating.put("제로시드정20/1100밀리그램 <-> 도랄정20밀리그람", (float)1.12);
        rating.put("가브스메트정50/500밀리그램 <-> 산도스아지트로마이신정500밀리그램", (float)1.1);
        rating.put("코러스오메프라졸캡슐10mg <-> 카프렐사정300밀리그램", (float)1.08);
        rating.put("임듈지속정30밀리그램 <-> 슈다펜액", (float)1.06);
        rating.put("크렌블시럽 <-> 듀오도파장내겔", (float)1.04);
        rating.put("올트릴앰플주사액 <-> 데파코트서방정500㎎", (float)1.02);
        rating.put("제파티어정 <-> 프레즈코빅스정", (float)3);
        rating.put("유시락스시럽 <-> 스타레보필름코팅정125/31.25/200밀리그램", (float)2.98);
        rating.put("에보타즈정 <-> 오니바이드주", (float)2.96);
        rating.put("제로시드정20/1100밀리그램 <-> 리납틴듀오정2.5/500밀리그램", (float)2.94);
        rating.put("트루패스구강붕해정8밀리그램 <-> 비키라정", (float)2.92);
        rating.put("도비드정 <-> 인베가트린자주사", (float)2.9);
        rating.put("순베프라캡슐100밀리그램 <-> 퍼마곤주120밀리그램", (float)2.88);
        rating.put("엑스비라정 <-> 콤비글라이즈서방정5/1000밀리그램", (float)2.86);
        rating.put("제파티어정 <-> 에보타즈정", (float)2.84);
        rating.put("휴메딕스덱사메타손포스페이트이나트륨주사 <-> 영풍니페디핀연질캡슐", (float)2.82);
        rating.put("아미로정 <-> 안제메트주사", (float)2.8);
        rating.put("다비트란캡슐150밀리그램 <-> 자이카디아캡슐150밀리그램", (float)2.78);
        rating.put("순베프라캡슐100밀리그램 <-> 비키라정", (float)2.76);
        rating.put("코러스오메프라졸캡슐10mg <-> 록스펜주", (float)2.74);
        rating.put("판베시서방캡슐30밀리그램 <-> 코오롱브렉신정10밀리그램", (float)2.72);
        rating.put("제피드정200밀리그램 <-> 베믈리디정", (float)2.7);
        rating.put("이미실키트주사250밀리그람 <-> 트리멕정", (float)2.68);
        rating.put("스타레보필름코팅정125/31.25/200밀리그램 <-> 사이폴-엔연질캡슐50밀리그램", (float)2.66);
        rating.put("입랜스캡슐100mg <-> 튜비스정", (float)2.64);
        rating.put("순베프라캡슐100밀리그램 <-> 에이신에스외용액", (float)2.62);
        rating.put("알엠서방정750/5밀리그램 <-> 싸이문주", (float)2.6);
        rating.put("마인트롤정 <-> 제라부딘정", (float)2.58);
        rating.put("에피네프린프리필드시린지주1:10000 <-> 아스토건조시럽", (float)2.56);
        rating.put("아그레녹스서방캡슐 <-> 테넬리아엠서방정20/1000밀리그램", (float)2.54);
        rating.put("실타니딘정1밀리그램 <-> 피타렉스캡슐0.5밀리그램", (float)2.52);
        rating.put("화레스톤정40밀리그램 <-> 오니바이드주", (float)2.5);
        rating.put("밀타오디정7.5mg <-> 쎄글루로메트정7.5/500밀리그램", (float)2.48);
        rating.put("에르고탄주 <-> 시그마트주12밀리그람", (float)2.46);
        rating.put("디어미순정 <-> 이미실키트주사250밀리그람", (float)2.44);
        rating.put("타크롤캡슐1밀리그램 <-> 테포비어정", (float)2.42);
        rating.put("멜로텍스캡슐15mg <-> 슈프라제정", (float)2.4);
        rating.put("테노퀄정 <-> 보스민액", (float)2.38);
        rating.put("레플록스주 <-> 대한뉴팜메로페넴주500mg", (float)2.36);
        rating.put("이오센스370주 <-> 메가벡정400밀리그램", (float)2.34);
        rating.put("티비케이정50밀리그램 <-> 한화쿼테아핀정300밀리그램", (float)2.32);
        rating.put("알모그란정 <-> 하나염산페치딘주사25밀리그람", (float)2.3);
        rating.put("한올리네졸리드주2밀리그램/밀리리터 <-> 미가드정2.5mg", (float)2.28);
        rating.put("한올덱사메타손정 <-> 로벨리토정300/10밀리그램", (float)2.26);
        rating.put("경동린코마이신염산염수화물주 <-> 마이팜케토코나졸정", (float)2.24);
        rating.put("엑스비라정 <-> 옥시마이신정500밀리그람", (float)2.22);
        rating.put("아이오피딘0.5%점안액 <-> 대웅프리미돈정", (float)2.2);
        rating.put("푸로질캅셀 <-> 파미레이200주사액", (float)2.18);
        rating.put("하보니정 <-> 환인크로픽솔정25밀리그램", (float)2.16);
        rating.put("가스크린액 <-> 리납틴듀오정2.5/500밀리그램", (float)2.14);
        rating.put("하보니정 <-> 테라싸이클린캅셀250밀리그람", (float)2.12);
        rating.put("케트로주사 <-> 레플록스주", (float)2.1);
        rating.put("베믈리디정 <-> 삼양아트로핀주사제", (float)2.08);
        rating.put("셉타네스트주1/200,000 <-> 레포리아정500mg", (float)2.06);
        rating.put("스토파제주 <-> 판베시서방캡슐30밀리그램", (float)2.04);
        rating.put("테넬리아엠서방정20/1000밀리그램 <-> 에나폰정5밀리그램", (float)2.02);
        rating.put("디스톨정 <-> 미가텍정", (float)2);
        rating.put("에라이신건조시럽5% <-> 자바토정2.5밀리그램", (float)1.98);
        rating.put("슈가메트서방정2.5/500밀리그램 <-> 록스펜주", (float)1.96);
        rating.put("디스톨정 <-> 푸리딘정", (float)1.94);
        rating.put("록소드린현탁액 <-> 구주염산모르핀주", (float)1.92);
        rating.put("일성이솦틴주사 <-> 시노카자일로나잘스프레이액0.1%", (float)1.9);
        rating.put("신플랙스세이프정500/20밀리그램 <-> 넥시움주", (float)1.88);
        rating.put("레바넥스정200밀리그램 <-> 푸리딘정", (float)1.86);
        rating.put("리트모놈정주70mg/20ml <-> 에듀란트정25밀리그램", (float)1.84);
        rating.put("대한염화칼륨-20 <-> 피타렉스캡슐0.5밀리그램", (float)1.82);
        rating.put("임플라논임플란트 <-> 아그레녹스서방캡슐", (float)1.8);
        rating.put("엠빅스정100밀리그램 <-> 명인피모짓4밀리그람정", (float)1.78);
        rating.put("한올리네졸리드주2밀리그램/밀리리터 <-> 칼도롤주사액", (float)1.76);
        rating.put("제이모다정200밀리그램 <-> 동화세레콕시브캡슐200밀리그램", (float)1.74);
        rating.put("베아로신서방정0.4밀리그램 <-> 제이모다정200밀리그램", (float)1.72);
        rating.put("크렌블시럽 <-> 부벤톨이지할러200", (float)1.7);
        rating.put("비키라정 <-> 오엠피에스캡슐40밀리그램", (float)1.68);
        rating.put("오메라스캡슐 <-> 경동겐타마이신황산염주160밀리그람", (float)1.66);
        rating.put("프레미나정0.625밀리그램 <-> 컴비비어정", (float)1.64);
        rating.put("알파돌연질캡슐 <-> 에르고탄주", (float)1.62);
        rating.put("서튜러정100밀리그램 <-> 수마트란정25밀리그람", (float)1.6);
        rating.put("에필렙톨정100밀리그램 <-> 보그메트정0.2/500밀리그램", (float)1.58);
        rating.put("산도스아지트로마이신정500밀리그램 <-> 노피린주", (float)1.56);
        rating.put("구주염산모르핀주 <-> 시타메폴엑스알서방정100/1000밀리그램", (float)1.54);
        rating.put("타드린캅셀300밀리그람 <-> 종근당염산에페드린정", (float)1.52);
        rating.put("렉토지연고 <-> 레토프라정40밀리그램", (float)1.5);
        rating.put("카타스주 <-> 삼양툴로부테롤패취2밀리그램", (float)1.48);
        rating.put("오로릭스정150밀리그람 <-> 신플랙스세이프정500/20밀리그램", (float)1.46);
        rating.put("알파아세클로페낙정 <-> 에듀란트정25밀리그램", (float)1.44);
        rating.put("올트릴앰플주사액 <-> 실타니딘정1밀리그램", (float)1.42);
        rating.put("제네틱스350주사 <-> 리마클로건조시럽250mg/5ml", (float)1.4);
        rating.put("소프라정15밀리그램 <-> 트레인연질캡슐", (float)1.38);
        rating.put("오엠피에스캡슐40밀리그램 <-> 아디센정50밀리그람", (float)1.36);
        rating.put("테노퀄정 <-> 한독세로자트정30밀리그램", (float)1.34);
        rating.put("제로시드정20/1100밀리그램 <-> 화레스톤정40밀리그램", (float)1.32);
        rating.put("뉴바스트정80밀리그람 <-> 케랄주", (float)1.3);
        rating.put("메트비정 <-> 마인트롤정", (float)1.28);
        rating.put("자이카디아캡슐150밀리그램 <-> 데파코트서방정500㎎", (float)1.26);
        rating.put("올트릴앰플주사액 <-> 실타니딘정1밀리그램", (float)1.24);
        rating.put("덱실란트디알캡슐60밀리그램 <-> 사노렉스정", (float)1.22);
        rating.put("대원페노바르비탈정100밀리그람 <-> 리메타손주", (float)1.2);
        rating.put("부광미졸렌정10밀리그램 <-> 카타스주", (float)1.18);
        rating.put("브이펜드정50밀리그람 <-> 유니독시캅셀", (float)1.16);
        rating.put("데스베라서방정100밀리그램 <-> 시노카자일로나잘스프레이액0.1%", (float)1.14);
        rating.put("도랄정20밀리그람 <-> 제로시드정20/1100밀리그램", (float)1.12);
        rating.put("산도스아지트로마이신정500밀리그램 <-> 가브스메트정50/500밀리그램", (float)1.1);
        rating.put("카프렐사정300밀리그램 <-> 코러스오메프라졸캡슐10mg", (float)1.08);
        rating.put("슈다펜액 <-> 임듈지속정30밀리그램", (float)1.06);
        rating.put("듀오도파장내겔 <-> 크렌블시럽", (float)1.04);
        rating.put("데파코트서방정500㎎ <-> 올트릴앰플주사액", (float)1.02);
    }
}
