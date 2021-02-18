package uteq.student.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
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


    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String nombre,email, id;
    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setTitle("");
        //getInfoUser();

        //FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //userID = mAuth.getCurrentUser().getUid();


        /*manageruser   =(Button) findViewById(R.id.btnmanagerusers);
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
        });*/

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
    public void btnRecordatorios(View view){
        //Toast.makeText(MainActivity.this, userID, Toast.LENGTH_SHORT).show();
        //startActivity( new Intent(MainActivity.this, RecordatoriosActivity.class));
        Intent intent = new Intent(getApplication(), RecordatoriosActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        intent.putExtras(bundle);
        startActivity(intent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        //Toast.makeText(getApplicationContext(), Integer.toString(id), Toast.LENGTH_LONG).show();
        if(id == R.id.opc_reportes) {
            //Intent intent = new Intent(this, activity_suscripciones.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            //startActivity(intent);
        }
        if(id == R.id.opc_actualizarDatosPersonales) {
            //Intent intent = new Intent(this, activity_creditos.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            //startActivity(intent);
        }

        if(id == R.id.opc_cerrar_sesion) {
            /*Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();*/
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this,"Cerrar sesion", Toast.LENGTH_SHORT).show();
            startActivity( new Intent(MainActivity.this, StartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    /*private void getInfoUser(){
        id=firebaseAuth.getCurrentUser().getUid();
        databaseReference.child("usuario").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    idUsuario=snapshot.getKey();
                    nombre=snapshot.child("apellido").getValue().toString();
                    email=snapshot.child("cedula").getValue().toString();
                    //lb_nombre.setText(nombre);
                    //lb_email.setText(email);
                    Toast.makeText(MainActivity.this,idUsuario+" "+nombre+" "+email, Toast.LENGTH_SHORT).show();
                    /*View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);
                    ((TextView) header.findViewById(R.id.txt_nav_usuario)).setText(nombre);
                    ((TextView) header.findViewById(R.id.txt_nav_correo)).setText(email);
                    mDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }*/
}