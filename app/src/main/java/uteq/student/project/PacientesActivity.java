package uteq.student.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import uteq.student.project.Adaptador.AdapterPacientes;
import uteq.student.project.Model.Info;
import uteq.student.project.Model.paciente;

public class PacientesActivity extends AppCompatActivity {
    //private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;

    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String fecha, userID;
    String[] aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacientes);

        //CARGAR TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fecha = getIntent().getStringExtra("fecha");
        userID = getIntent().getStringExtra("userID");
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) findViewById(R.id.rcl_pacientes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }


    @Override
    protected void onStart() {
        super.onStart();

        final ArrayList<Info> ListItems= new ArrayList<>();

        List<Info> pacientes = new ArrayList<>();
        databaseReference.child("registro").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int i =0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String id = ds.getKey();
                        i++;
                    }

                    aux = new String[i];
                    int j = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String id = ds.getKey();
                        aux[j]=id;
                        j++;
                    }

                    for(int m=0;m<aux.length;m++)
                    {
                        databaseReference.child("usuario").child(aux[m]).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {

                                        String id2 = snapshot.getKey();
                                        String nombre = snapshot.child("nombre").getValue().toString();
                                        String apellido = snapshot.child("apellido").getValue().toString();
                                        String celular = snapshot.child("celular").getValue().toString();
                                        String direccion = snapshot.child("direccion").getValue().toString();
                                        String nacimiento = snapshot.child("fecha_nacimiento").getValue().toString();
                                        ListItems.add(new Info(id2, apellido, celular, direccion, nacimiento, nombre));
                                }
                                //Toast.makeText(PacientesActivity.this, String.valueOf(ListItems), Toast.LENGTH_SHORT).show();

                                recyclerView = (RecyclerView) findViewById(R.id.rcl_pacientes);
                                manager=new LinearLayoutManager(PacientesActivity.this);
                                recyclerView.setLayoutManager(manager);
                                adapter = new AdapterPacientes(ListItems, PacientesActivity.this);
                                recyclerView.setAdapter(adapter);

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

       // for (int i = 0; i < aux.length; i++) {
//            Toast.makeText(PacientesActivity.this, aux[i], Toast.LENGTH_SHORT).show();
        //}

        /*FirebaseRecyclerOptions<Info> options =
                new FirebaseRecyclerOptions.Builder<Info>()
                        //.setQuery(FirebaseDatabase.getInstance().getReference().child("dispositivos"), Dispositivos.class)
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("dispositivos").orderByChild("id_usuario").equalTo(userID), Info.class)
                        .build();*/


    }

    class myviewholder extends RecyclerView.ViewHolder {
        TextView nombreDispositivo, macDispositivo;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            // urlimagen=(ImageView) itemView.findViewById(R.id.imgPortada_user);
            nombreDispositivo = (TextView) itemView.findViewById(R.id.rvNombreDispositivo);
            macDispositivo = (TextView) itemView.findViewById(R.id.rvMacDispositivo);

        }
    }

}