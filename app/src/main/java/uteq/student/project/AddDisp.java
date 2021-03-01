package uteq.student.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import uteq.student.project.R;

public class AddDisp extends AppCompatActivity {
    private EditText mac, alias;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_disp);
        mac= (EditText) findViewById(R.id.txtMac);
        alias= (EditText)findViewById(R.id.txtAlias);
        mDataBase = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    private long hijos;
    public void btnSaveDisp(View view){
        try {
            HashMap<String, Object> map= new HashMap<>();
            map.put("alias", alias.getText().toString());
            map.put("mac", mac.getText().toString());
            map.put("id_usuario", user.getUid());

            if(alias.getText().toString().length()>0)
            {
                if(mac.getText().toString().length()==17) {
                    mDataBase.child("dispositivos").push().setValue(map);
                    Toast toast = Toast.makeText(getApplicationContext(), "Add Successfull", Toast.LENGTH_SHORT);
                    toast.show();
                    super.onBackPressed();
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Enter device MAC", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(), "Enter device name", Toast.LENGTH_SHORT);
                toast.show();
            }
          /*  //mRefere = FirebaseDatabase.getInstance().getReference("dispositivos");
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                     hijos= snapshot.getChildrenCount();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            FirebaseDatabase.getInstance().getReference().child("dispositivos").child(user.getUid()).child(String.valueOf(hijos+1)).setValue(map);
        */
        }catch (Exception e){

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();


        if(id == R.id.opc_cerrar_sesion) {
            /*Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();*/
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(AddDisp.this,"Cerrar sesion", Toast.LENGTH_SHORT).show();
            startActivity( new Intent(AddDisp.this, LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}