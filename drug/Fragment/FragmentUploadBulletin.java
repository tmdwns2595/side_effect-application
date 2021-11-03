package org.jjcouple.drug.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jjcouple.drug.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FragmentUploadBulletin extends Fragment {

    private static final String TAG = "WritePostActivity";
    private FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    View view;

    public FragmentUploadBulletin() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_write_post, container, false);
        view.findViewById(R.id.upload_button).setOnClickListener(onClickListener);
        view.findViewById(R.id.back_button).setOnClickListener(onClickListener);
        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.upload_button:
                    String title = ((EditText) view.findViewById(R.id.editTextBulletinTitle)).getText().toString();
                    String contents = ((EditText) view.findViewById(R.id.editTextBulletinContent)).getText().toString();
                    // 현재시간을 msec 으로 구한다.
                    long now = System.currentTimeMillis();
                    // 현재시간을 date 변수에 저장한다.
                    Date date = new Date(now);
                    // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                    // nowDate 변수에 값을 저장한다.
                    String formatDate = sdfNow.format(date);

                    db = FirebaseFirestore.getInstance();
                    Map<String, Object> bulletin = new HashMap<>();
                    firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    bulletin.put("user_id", user.getEmail());
                    bulletin.put("bulletin_title", title);
                    bulletin.put("bulletin_contents", contents);
                    bulletin.put("bulletin_time", formatDate);

                    db.collection("bulletin").document(user.getEmail() + formatDate)
                            .set(bulletin)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("FireStore_Tag", "성공적으로 입력이 되었습니다.");
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction tran = fm.beginTransaction();
                                    tran.replace(R.id.frameLayout, new FragmentMainBulletin());
                                    tran.commit();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("FireStore_Tag", "Error 발생!");
                        }
                    });
                    break;
                case R.id.back_button:
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction tran = fm.beginTransaction();
                    tran.replace(R.id.frameLayout, new FragmentMainBulletin());
                    tran.commit();
            }
        }
    };
}

