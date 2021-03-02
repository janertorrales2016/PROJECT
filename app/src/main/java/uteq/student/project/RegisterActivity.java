package uteq.student.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button register;
    private FirebaseAuth auth;
    DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email= findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.registerbutton);
        auth= FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_paswword = password.getText().toString();
                if(TextUtils.isEmpty(txt_email)|| TextUtils.isEmpty(txt_paswword)){
                    Toast.makeText(RegisterActivity.this,"Empty Credentials", Toast.LENGTH_SHORT).show();
                }else if(txt_paswword.length()<6){
                    Toast.makeText(RegisterActivity.this,"Password too short", Toast.LENGTH_SHORT).show();
                }else{
                    registerUser(txt_email,txt_paswword);

                }
            }
        });
    }
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private void registerUser(String email, String password){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Map<String, Object> map= new HashMap<>();
                    map.put("nombre", "" );
                    map.put("apellido", "" );
                    map.put("direccion", "");
                    map.put("celular", "");
                    map.put("fecha_nacimiento", "" );
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String currentDateandTime = sdf.format(new Date());
                    map.put("fecha_create", currentDateandTime);
                    map.put("fecha_update", currentDateandTime);
                    map.put("rol", "enfermero");
                    String id=auth.getCurrentUser().getUid();
                    mDataBase.child("usuario").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
                                //startActivity( new Intent(RegisterActivity.this, MainActivity.class));
                                Toast.makeText(RegisterActivity.this, "Register user succeful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplication(), LoginActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("inicio", "inicio");
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            }
                            else
                            Toast.makeText(RegisterActivity.this,"Register Failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    Toast.makeText(RegisterActivity.this,"Register Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            startActivity( new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}