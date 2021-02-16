package uteq.student.project.Model;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import uteq.student.project.Model.paciente;
import uteq.student.project.R;

public class AddUser extends AppCompatActivity {
    private EditText usuario, contraseña, alias;
    private FirebaseAuth auth= FirebaseAuth.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String IdOring = user.getUid();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mReference;
    private List<paciente> pacientesList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        mReference = FirebaseDatabase.getInstance().getReference("registro").child(IdOring);
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    paciente pacien = dataSnapshot.getValue(paciente.class);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void SaveUses(View view){
        usuario=(EditText)findViewById(R.id.textusers);
        contraseña=(EditText)findViewById(R.id.textpass);
        alias =(EditText) findViewById(R.id.textalias);

        auth.createUserWithEmailAndPassword(usuario.getText().toString(), contraseña.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
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
                            map.put("rol", "AdultoMayor");
                            FirebaseUser user2 = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = user2.getUid();
                            FirebaseDatabase.getInstance().getReference().child("usuario").child(uid).setValue(map);
                            FirebaseDatabase.getInstance().getReference().child("registro")
                                    .child(IdOring).child(uid).child("nombre").setValue(alias.getText().toString());
                        }
                    }
                });

    }
}