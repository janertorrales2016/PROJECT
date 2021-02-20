package uteq.student.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button register;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email= findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.registerbutton);
        auth= FirebaseAuth.getInstance();
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
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"Register user succeful", Toast.LENGTH_SHORT).show();
                    HashMap<String, Object> map= new HashMap<>();
                    map.put("nombre", "" );
                    map.put("apellido", "" );
                    map.put("direccion", "");
                    map.put("celular", "");
                    map.put("fecha_nacimiento", "" );
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    String currentDateandTime = sdf.format(new Date());
                    map.put("fecha_create", currentDateandTime);
                    map.put("fecha_update", currentDateandTime);
                    map.put("rol", "enfermero");
                    FirebaseDatabase.getInstance().getReference().child("usuario").child(user.getUid()).setValue(map);
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this,"Register Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}