package org.jjcouple.drug.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jjcouple.drug.R;
import org.jjcouple.drug.etc.CustomAdapter;

import java.util.ArrayList;

public class FragmentMainBulletin extends Fragment implements TextWatcher {
    View view;
    FirebaseFirestore db;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> mList;
    EditText editText;
    private FirebaseAuth firebaseAuth;

    public FragmentMainBulletin(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.map_main, container, false);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height * 65 / 100);
        recyclerView.setLayoutParams(params1);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);;
        mList = new ArrayList<>();
        editText = view.findViewById(R.id.editSearch);
        editText.addTextChangedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        db.collection("bulletin").orderBy("bulletin_time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        mList.clear();
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                mList.add("제목 : " + (String)document.get("bulletin_title") + "\n내용 : " + (String)document.get("bulletin_contents") +
                                        "\n작성자 : " + (String)document.get("user_id") + "\n시간 : " + (String)document.get("bulletin_time"));
                            }
                            adapter.notifyDataSetChanged();
                        }else{
                            Log.v("FIRESTORE_TAG", "데이터 불러오기에 실패했습니다.");
                        }
                    }
                });
        view.findViewById(R.id.bulletin_write_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction tran = fm.beginTransaction();
                tran.replace(R.id.frameLayout, new FragmentUploadBulletin());
                tran.commit();
            }
        });
        adapter = new CustomAdapter(mList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((View v, int position) -> {

        }) ;

        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        adapter.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
