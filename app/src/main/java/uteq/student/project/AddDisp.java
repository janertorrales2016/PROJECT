package uteq.student.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import uteq.student.project.Model.Info;
import uteq.student.project.R;

public class AddDisp extends AppCompatActivity {
    private EditText mac, alias;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userID = mAuth.getCurrentUser().getUid();
    String[] aux;
    Spinner spListaPacientes;
    String id_us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_disp);
        mac = (EditText) findViewById(R.id.txtMac);
        alias = (EditText) findViewById(R.id.txtAlias);
        mDataBase = FirebaseDatabase.getInstance().getReference();

        spListaPacientes = (Spinner) findViewById(R.id.splistapacientesAddDisp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listaPacientes();
    }

    private long hijos;

    public void btnSaveDisp(View view) {
        try {
            HashMap<String, Object> map = new HashMap<>();
            map.put("alias", alias.getText().toString());
            map.put("mac", mac.getText().toString());
            map.put("id_usuario", id_us);

            if (alias.getText().toString().length() > 0) {
                if (mac.getText().toString().length() == 17) {
                    String paciente = spListaPacientes.getSelectedItem().toString();
                    if(!paciente.equals("")){
                        mDataBase.child("dispositivos").push().setValue(map);
                        Toast toast = Toast.makeText(getApplicationContext(), "Add Successfull", Toast.LENGTH_SHORT);
                        toast.show();
                        super.onBackPressed();
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Enter person", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Enter device MAC", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else {
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
        } catch (Exception e) {

        }

    }

    public void listaPacientes() {

        final ArrayList<Info> ListItems = new ArrayList<>();

        //List<Info> pacientes = new ArrayList<>();
        mDataBase.child("registro").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int i = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String id = ds.getKey();
                        i++;
                    }

                    aux = new String[i];
                    int j = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String id = ds.getKey();
                        aux[j] = id;
                        j++;
                    }

                    for (int m = 0; m < aux.length; m++) {
                        mDataBase.child("usuario").child(aux[m]).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String id2 = snapshot.getKey();
                                    String nombre = snapshot.child("nombre").getValue().toString();
                                    String apellido = snapshot.child("apellido").getValue().toString();
                                    ListItems.add(new Info(id2, apellido, nombre));
                                }

                                ArrayAdapter<Info> arrayAdapter = new ArrayAdapter<>(AddDisp.this, android.R.layout.simple_dropdown_item_1line, ListItems);
                                spListaPacientes.setAdapter(arrayAdapter);
                                spListaPacientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        id_us = ListItems.get(position).getId();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.opc_cerrar_sesion) {
            /*Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();*/
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(AddDisp.this, "Cerrar sesion", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddDisp.this, LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}