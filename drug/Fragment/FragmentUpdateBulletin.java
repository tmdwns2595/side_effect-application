package org.jjcouple.drug.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jjcouple.drug.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FragmentUpdateBulletin extends Fragment {
    private static final String TAG = "FragmentUpdateBulletin";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseUser user2;
    private FirebaseUser reply_user;
    private FirebaseFirestore db;
    String[] update_info;
    View view;

    public FragmentUpdateBulletin() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_update_post, container, false);
        setHasOptionsMenu(true);
        view.findViewById(R.id.update_button).setOnClickListener(onClickListener);
        view.findViewById(R.id.delete_button).setOnClickListener(onClickListener);
        view.findViewById(R.id.back_button).setOnClickListener(onClickListener);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        LinearLayout scroll_li = view.findViewById(R.id.scroll_li);
        EditText title = view.findViewById(R.id.editTextBulletinTitle);
        EditText contents = view.findViewById(R.id.editTextBulletinContent);
        EditText reply_con = view.findViewById(R.id.reply_con);
        Button register = view.findViewById(R.id.register);
        Button update_btn = view.findViewById(R.id.update_button);
        Button delete_btn = view.findViewById(R.id.delete_button);
        Bundle extra = this.getArguments();
        update_info = extra.getStringArray("update_info");
        title.setText(update_info[0]);
        contents.setText(update_info[1]);
        if(!user.getEmail().equals(update_info[2])){
            title.setFocusable(false);
            title.setClickable(false);
            contents.setFocusable(false);
            contents.setClickable(false);
            update_btn.setVisibility(View.GONE);
            delete_btn.setVisibility(View.GONE);
        }
        String t_user = update_info[2];
        String t_time = update_info[3];
        db = FirebaseFirestore.getInstance();
//        게시물 댓글 읽어오는 부분
        db.collection(t_user + t_time).orderBy("bulletin_time", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e(TAG, document.getId() + " => " + document.getData().toString().split("=")[3]);
                                LinearLayout list_li = new LinearLayout(getContext());
                                list_li.setOrientation(LinearLayout.VERTICAL);
                                list_li.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                LinearLayout small_list_li = new LinearLayout(getContext());
                                small_list_li.setOrientation(LinearLayout.VERTICAL);
                                small_list_li.setGravity(Gravity.CENTER);
                                small_list_li.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                LinearLayout back_list_li = new LinearLayout(getContext());
                                back_list_li.setOrientation(LinearLayout.HORIZONTAL);
                                back_list_li.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                TextView text_id = new TextView(getContext());
                                text_id.setText(document.getData().toString().split("=")[2].split(",")[0]);
                                text_id.setTextSize(20);
                                text_id.setTextColor(Color.BLACK);
                                small_list_li.addView(text_id);
                                TextView text = new TextView(getContext());
                                text.setText(document.getData().toString().split("=")[1].split(",")[0]);
                                text.setTextSize(18);
                                text.setTextColor(Color.BLACK);
                                small_list_li.addView(text);
                                small_list_li.setPadding(10,0,0,0);
                                LinearLayout.LayoutParams mLayoutParams = (LinearLayout.LayoutParams) back_list_li.getLayoutParams();
                                mLayoutParams.setMargins(0,10,0,10);
                                back_list_li.setLayoutParams(mLayoutParams);
                                back_list_li.setBackgroundResource(R.drawable.reply_back);
                                Button trash_bt = new Button(getContext());
                                trash_bt.setBackgroundResource(R.drawable.trash);
                                back_list_li.addView(small_list_li);
                                back_list_li.addView(trash_bt);
                                back_list_li.setGravity(Gravity.CENTER_VERTICAL);
                                LinearLayout.LayoutParams mLayoutParams2 = (LinearLayout.LayoutParams) small_list_li.getLayoutParams();
                                mLayoutParams2.weight = 0.8f;
                                small_list_li.setLayoutParams(mLayoutParams2);
                                LinearLayout.LayoutParams mLayoutParams3 = (LinearLayout.LayoutParams) trash_bt.getLayoutParams();
                                mLayoutParams3.weight = 0.2f;
                                trash_bt.setLayoutParams(mLayoutParams3);
                                list_li.addView(back_list_li);
                                scroll_li.addView(list_li);
                                db = FirebaseFirestore.getInstance();
                                firebaseAuth = FirebaseAuth.getInstance();
                                user2 = firebaseAuth.getCurrentUser();
                                trash_bt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(user2.getEmail().equals(document.getData().toString().split("=")[2].split(",")[0])){
                                            AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                                            dlg.setTitle("댓글 삭제");
                                            dlg.setMessage("정말 댓글을 삭제하시겠습니까?");
                                            dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    if(user2.getEmail().equals(document.getData().toString().split("=")[2].split(",")[0])){
                                                        db.collection(t_user+t_time).document(document.getId())
                                                                .delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d("FireStore_Tag", "성공적으로 삭제되었습니다.");
                                                                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                                        ft.detach(FragmentUpdateBulletin.this).attach(FragmentUpdateBulletin.this).commit();
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w("FireStore_Tag", "삭제하는 과정에서 error가 발생했습니다.");
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                            dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Toast.makeText(getContext(),"취소를 누르셨습니다.",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            dlg.show();
                                        }
                                        else{
                                            Toast.makeText(getContext(),"삭제할 권한이 없습니다.",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String t_user = update_info[2];
                String t_time = update_info[3];
                // 현재시간을 msec 으로 구한다.
                long now = System.currentTimeMillis();
                // 현재시간을 date 변수에 저장한다.
                Date date = new Date(now);
                // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                // nowDate 변수에 값을 저장한다.
                String formatDate = sdfNow.format(date);

                db = FirebaseFirestore.getInstance();
                Map<String, Object> rep_bulletin = new HashMap<>();
                firebaseAuth = FirebaseAuth.getInstance();
                reply_user = firebaseAuth.getCurrentUser();
                rep_bulletin.put("rep_user_id", reply_user.getEmail());
                rep_bulletin.put("rep_contents", reply_con.getText().toString());
                rep_bulletin.put("bulletin_time", formatDate);

                db.collection(t_user + t_time).document(reply_user.getEmail() + formatDate)
                        .set(rep_bulletin)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("FireStore_Tag", "성공적으로 입력이 되었습니다.");
                                reply_con.setText("");
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(FragmentUpdateBulletin.this).attach(FragmentUpdateBulletin.this).commit();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FireStore_Tag", "Error 발생!");
                    }
                });
            }
        });
        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.update_button:
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
                    user = firebaseAuth.getCurrentUser();
                    bulletin.put("user_id", user.getEmail());
                    bulletin.put("bulletin_title", title);
                    bulletin.put("bulletin_contents", contents);
                    bulletin.put("bulletin_time", formatDate);

                    db.collection("bulletin").document(user.getEmail() + update_info[3])
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
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
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("FireStore_Tag", "Error가 발생했습니다.");
                        }
                    });
                    break;
                case R.id.delete_button:

                    db = FirebaseFirestore.getInstance();
                    firebaseAuth = FirebaseAuth.getInstance();
                    user = firebaseAuth.getCurrentUser();

                    AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                    dlg.setTitle("게시글 삭제");
                    dlg.setMessage("정말 해당 게시글을 삭제하시겠습니까?");
                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.collection("bulletin").document(user.getEmail() + update_info[3])
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("FireStore_Tag", "성공적으로 삭제되었습니다.");
                                            FragmentManager fm = getFragmentManager();
                                            FragmentTransaction tran = fm.beginTransaction();
                                            tran.replace(R.id.frameLayout, new FragmentMainBulletin());
                                            tran.commit();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("FireStore_Tag", "삭제하는 과정에서 error가 발생했습니다.");
                                }
                            });
                        }
                    });
                    dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getContext(),"취소를 누르셨습니다.",Toast.LENGTH_SHORT).show();
                        }
                    });
                    dlg.show();
                    break;

                case R.id.back_button:
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction tran = fm.beginTransaction();
                    tran.replace(R.id.frameLayout, new FragmentMainBulletin());
                    tran.commit();
                    break;
            }
        }
    };
}

