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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

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

    public void btnHistorial(View view){
        /*Intent intent = new Intent(getApplication(), VistaRecordatoriosActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        bundle.putString("fecha", "sinFecha");
        intent.putExtras(bundle);
        startActivity(intent);*/
        Intent intent = new Intent(getApplication(), PacientesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        bundle.putString("fecha", "sinFecha");
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
                           /* Intent intent= new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);*/
                            finish();
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
        if(id == R.id.opc_add_Paciente) {
            Intent intent = new Intent(this, AddUser.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        if(id == R.id.opc_actualizar_Datos_Personales) {
            Intent intent = new Intent(this, ConfigActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }

        if(id == R.id.opc_Reporte) {
            Intent intent = new Intent(this, Report.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }

        if(id == R.id.opc_asignar_dispositivo) {
            Intent intent = new Intent(this, AddDisp.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        if(id == R.id.opc_ver_dispositivos) {
            //Intent intent = new Intent(this, activity_creditos.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            //startActivity(intent);
        }

        if(id == R.id.opc_cerrar_sesion) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this,"Cerrar sesion", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}