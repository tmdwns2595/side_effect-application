package org.jjcouple.drug.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jjcouple.drug.R;
import org.jjcouple.drug.UserManagement.LoginActivity;
import org.jjcouple.drug.UserManagement.SignUp;

//public class FragmentSetting extends Fragment implements View.OnClickListener
public class FragmentSetting extends Fragment implements View.OnClickListener{

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    //view objects
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private Button textviewDelete;

    View view;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState){
//        View view = inflater.inflate(R.layout.profile, container, false);
        if (view == null) {
            view = inflater.inflate(R.layout.profile, container, false);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        //initializing views
        textViewUserEmail = view.findViewById(R.id.textviewUserEmail);
        buttonLogout = view.findViewById(R.id.buttonLogout);
        textviewDelete = view.findViewById(R.id.textviewDelete);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if(firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        textViewUserEmail.setText("반갑습니다. "+ user.getEmail()+"님!");
        buttonLogout.setOnClickListener(this);
        textviewDelete.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogout) {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getActivity());
            alert_confirm.setMessage("정말 로그아웃을 할까요?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            firebaseAuth.signOut();
                            startActivity(new Intent(getActivity(), SignUp.class));
                        }
                    }
            );
            alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getActivity(), "취소", Toast.LENGTH_LONG).show();
                }
            });
            alert_confirm.show();
        }
        //회원탈퇴를 클릭하면 회원정보를 삭제한다. 삭제전에 컨펌창을 하나 띄워야겠다.
        if(view == textviewDelete) {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getActivity());
            alert_confirm.setMessage("정말 계정을 삭제 할까요?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.delete().addOnCompleteListener(task -> {
                                Toast.makeText(getActivity(), "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getActivity(), SignUp.class));
                            });
                        }
                    }
            );
            alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getActivity(), "취소", Toast.LENGTH_LONG).show();
                }
            });
            alert_confirm.show();
        }
    }
}

