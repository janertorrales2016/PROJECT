package uteq.student.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uteq.student.project.Model.AddDisp;
import uteq.student.project.Model.AddUser;
import uteq.student.project.Model.Report;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference ref;
    private Button  manageruser;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userID = mAuth.getCurrentUser().getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manageruser   =(Button) findViewById(R.id.btnmanagerusers);
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("usuario").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if ("AdultoMayor" == snapshot.child("rol").getValue(String.class)){
                        manageruser.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Fallo la lectura: " + error.getCode());
            }
        });

    }

    public void BtnConfig(View view){
        startActivity( new Intent(MainActivity.this, ConfigActivity.class));
    }

    public void btnReport(View view){
        startActivity( new Intent(MainActivity.this, Report.class));
    }

    public void btnManagerUser(View view){
        startActivity( new Intent(MainActivity.this, AddUser.class));
    }
    public void btnAddDispo(View view){
        startActivity( new Intent(MainActivity.this, AddDisp.class));
    }
   public void BtnCerrarSesion(View view){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(MainActivity.this,"Cerrar sesion", Toast.LENGTH_SHORT).show();
        startActivity( new Intent(MainActivity.this, StartActivity.class));
    }
    public void BtnRecor(View view){
        //startActivity( new Intent(MainActivity.this, RecordatorioActivity.class));
    }
    //Se controla la pulsacion del boton atras
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_BACK){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿DESEA SALIR DE LA APP?")
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent= new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }
}