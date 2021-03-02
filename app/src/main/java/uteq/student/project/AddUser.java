package uteq.student.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
    private EditText usuario, contraseña, nombre,apellido,telefono,cedula,direccion,nacimiento;
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

/*        mReference = FirebaseDatabase.getInstance().getReference("registro").child(IdOring);
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
*/
    }


    public void SaveUses(View view) {
        usuario = (EditText) findViewById(R.id.textusers);
        contraseña = (EditText) findViewById(R.id.textpass);
        // alias =(EditText) findViewById(R.id.textalias); nombre,apellido,telefono,cedula,direccion,nacimiento
        nombre = (EditText) findViewById(R.id.txtNombreAM);
        apellido = (EditText) findViewById(R.id.txtApellidoAM);
        telefono = (EditText) findViewById(R.id.txtCelularAM);
        cedula = (EditText) findViewById(R.id.txtCedulaAM);
        direccion = (EditText) findViewById(R.id.txtDireccionAM);
        nacimiento = (EditText) findViewById(R.id.txtNacimientoAM);

       // Toast.makeText(AddUser.this,IdOring, Toast.LENGTH_SHORT).show();

       // if (usuario.getText().toString().length() > 0 && contraseña.getText().toString().length() > 0 && nombre.getText().toString().length() > 0 && apellido.getText().toString().length() > 0) {
        if (usuario.getText().toString().length() > 0 && contraseña.getText().toString().length() > 0){
            auth.createUserWithEmailAndPassword(usuario.getText().toString(), contraseña.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("nombre", nombre.getText().toString());
                                map.put("apellido", apellido.getText().toString());
                                map.put("direccion", direccion.getText().toString());
                                map.put("celular", telefono.getText().toString());
                                map.put("fecha_nacimiento", nacimiento.getText().toString());
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                String currentDateandTime = sdf.format(new Date());
                                map.put("fecha_create", currentDateandTime);
                                map.put("fecha_update", currentDateandTime);
                                map.put("rol", "AdultoMayor");
                                FirebaseUser user2 = FirebaseAuth.getInstance().getCurrentUser();
                                String uid = user2.getUid();
                                //Toast.makeText(AddUser.this,uid, Toast.LENGTH_SHORT).show();
                                FirebaseDatabase.getInstance().getReference().child("usuario").child(uid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task2) {
                                        if(task2.isSuccessful()) {
                                            FirebaseDatabase.getInstance().getReference().child("registro")
                                                    .child(IdOring).child(uid).child("nombre").setValue("valor").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task3) {
                                                    if(task3.isSuccessful()) {
                                                        Toast toast = Toast.makeText(getApplicationContext(), "Add Succesfull", Toast.LENGTH_SHORT);
                                                        toast.show();
                                                        //FirebaseAuth.getInstance().signOut();
                                                        finish();
                                                    }
                                                    else
                                                        Toast.makeText(AddUser.this,"Register Failed", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        else {
                                            Toast.makeText(AddUser.this,"Register Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });



                            }
                            else{
                                Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    });
        }
        else
            Toast.makeText(AddUser.this,"Enter user and Password", Toast.LENGTH_SHORT).show();
    }
}