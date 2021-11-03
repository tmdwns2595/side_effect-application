package org.jjcouple.drug.Setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.jjcouple.drug.R;

public class SettingFragment extends PreferenceFragment {

    SharedPreferences pref;
    Preference dark_mode;
    EditTextPreference edt;
    String shared = "file";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//별도의 화면 레이아웃파일(layout폴더)을 사용하지 않고
//설정 xml문서를 총해 화면이 자동 생성
//res폴더 안에 xml폴더 안에 .xml문서를 만들고
//<PregerenceScreen>클래스를 통해 화면 설계 시작..

        addPreferencesFromResource(R.xml.setting);


//SharedPreference객체를 참조하여 설정상태에 대한 제어 가능..
        pref =PreferenceManager.getDefaultSharedPreferences(getActivity());

    }


    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,Preference preference){
        if(preference.getKey().equals("ver_info")){
            Toast.makeText(getActivity(),getString(R.string.version),Toast.LENGTH_SHORT).show();
        }
        return false;
}

    private void setOnPreferenceChange(Preference mPreference) {
        // 리스너에 연결
        mPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
        // 리스너의 onPreferenceChange() 메소드를 호출함, 초기 Summeary를 설정하기 위함
        onPreferenceChangeListener.onPreferenceChange(
                mPreference,
                PreferenceManager.getDefaultSharedPreferences(
                        mPreference.getContext()).getString(
                        mPreference.getKey(), ""));
    }
    private Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {

        // 리스너 안에 if문을 넣어주면 됨. (Preference : 변경된 Preference, Object : 변경된 값)
        // 이를 이용해 어떤 Preference에서 값변동이 일어났는지 확인하고, 3가지 경우에 따라 선택한 값을 띔움
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();

            // EditTextPreference는 newValue가 변경된 값
            if (preference instanceof EditTextPreference) {
                preference.setSummary(stringValue);

            }
                // ListPreference 는 android:entryValues의 값으로 newValue가 옴.
            return true;
        }
    };

    @Override
    public void onResume() {
        super.onResume();

//설정값 변경리스너..등록
        pref.registerOnSharedPreferenceChangeListener(listener);
    }//onResume() ..

    @Override
    public void onPause() {
        super.onPause();

        pref.unregisterOnSharedPreferenceChangeListener(listener);

    }



    //설정값 변경리스너 객체 맴버변수
    SharedPreferences.OnSharedPreferenceChangeListener listener= new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals("sound_setting")){
                boolean b= pref.getBoolean("sound_setting", false);
                Toast.makeText(getActivity(), "소리알림 : "+ b, Toast.LENGTH_SHORT).show();

            }else if(key.equals("vibrate")) {
                boolean v = pref.getBoolean("vibrate", false);
                Toast.makeText(getActivity(), "진동알림 :" + v, Toast.LENGTH_SHORT).show();
            }
        }

    };

}