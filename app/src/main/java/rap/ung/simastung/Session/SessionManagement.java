package rap.ung.simastung.Session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";
    String SESSION_TYPE = "session_tipe";
    String SESSION_ID = "session_id";

    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(String namaPengguna, String tipePengguna){
        String nama = namaPengguna, tipe = tipePengguna;

        editor.putString(SESSION_KEY,nama).commit();
        editor.putString(SESSION_TYPE,tipe).commit();
    }
    public void saveIdAset(int idAset){
        int id = idAset;
        editor.putInt(SESSION_ID,id).commit();
    }
    public String getSession(){
        return sharedPreferences.getString(SESSION_KEY,"");
    }
    public String getTipeUser(){
        return sharedPreferences.getString(SESSION_TYPE,"");
    }
    public int getIdAset(){
        return sharedPreferences.getInt(SESSION_ID,-1);
    }
    public void removeSession(){
        editor.putString(SESSION_KEY,"").commit();
    }
}
