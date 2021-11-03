package org.jjcouple.drug.Setting;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jjcouple.drug.R;

import java.util.ArrayList;

public class QueActivity extends AppCompatActivity {


    RecyclerImageTextAdapter mAdapter ;
    ArrayList<RecyclerItem> mList ;

    @SuppressLint("UseCompatLoadingForDrawables")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_que);

        if(getSupportActionBar() != null) {
            ActionBar ab = getSupportActionBar() ;
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ab.setTitle("자주 묻는 질문") ;
        }

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler1);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager) ;
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.

        mList = new ArrayList<>();
        mAdapter = new RecyclerImageTextAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        addItem(getResources().getDrawable(R.drawable.question),"구글 계정으로만 로그인이 가능한가요?","안녕하세요.고객님.오늘도 저희 어플을 사용해주셔서 감사합니다.\n현재는 구글 계정 로그인만 지원중이고, 점차 다른 방식들도 추가할 예정입니다.\n앞으로 더 좋은 서비스로 찾아뵙겠습니다.");

        mAdapter.notifyDataSetChanged();
    }
    public void addItem(Drawable icon,String title, String desc){
        RecyclerItem item = new RecyclerItem();
        item.setIcon(icon);
        item.setTitle(title);
        item.setDesc(desc);

        mList.add(item);
    }


}