package uteq.student.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import uteq.student.project.Model.Dispositivos;
import uteq.student.project.Model.paciente;

public class VistaRecordatoriosActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String fecha,userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_recordatorios);

        //CARGAR TOOLBAR
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fecha = getIntent().getStringExtra("fecha");
        userID=getIntent().getStringExtra("userID");
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) findViewById(R.id.rcl_dispositivos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*List<Dispositivos> pacientes = new ArrayList<>();
        databaseReference.child("registro").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    FirebaseRecyclerOptions<Dispositivos> options=null;
                    FirebaseRecyclerAdapter<Dispositivos, myviewholder> adapter;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String id = ds.getKey();
                        String nombre = ds.child("nombre").getValue().toString();
                        //String mac = ds.child("mac").getValue().toString();
                        //String user_id = ds.child("id_usuario").getValue().toString();
                        //if (user_id.equals(userID))
                        //pacientes.add(new paciente(nombre,id));

                        options=
                                new FirebaseRecyclerOptions.Builder<Dispositivos>()
                                        .setQuery(FirebaseDatabase.getInstance().getReference().child("dispositivos").orderByChild("id_usuario").equalTo(id), Dispositivos.class)
                                        .build();

                        //Toast.makeText(VistaRecordatoriosActivity.this,String.valueOf(options), Toast.LENGTH_SHORT).show();
                    }
                        adapter=
                                new FirebaseRecyclerAdapter<Dispositivos, myviewholder>(options) {
                                    @Override
                                    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull Dispositivos model) {
                                        holder.nombreDispositivo.setText(model.getAlias());
                                        holder.macDispositivo.setText(model.getMac());

                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //String mac = getRef(position).getKey();
                                                // String mac = model.getMac();

                                                if(fecha.equals("sinFecha")) {
                                                    //Toast.makeText(VistaRecordatoriosActivity.this,"NO HAY FECHA", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplication(), ListaRecordatoriosActivity.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("fecha", fecha);
                                                    bundle.putString("userID", userID);
                                                    bundle.putString("mac", model.getMac());
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);
                                                }
                                                else{
                                                    Intent intent = new Intent(getApplication(), ListaRecordatoriosActivity.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("fecha", fecha);
                                                    bundle.putString("userID", userID);
                                                    bundle.putString("mac", model.getMac());
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }


                                    @NonNull
                                    @Override
                                    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_vista_dispositivos, parent, false);
                                        myviewholder viewHolder = new myviewholder(view);
                                        return viewHolder;
                                    }
                                };
                        recyclerView.setAdapter(adapter);
                        adapter.startListening();


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        FirebaseRecyclerOptions<Dispositivos> options =
                new FirebaseRecyclerOptions.Builder<Dispositivos>()
                        //.setQuery(FirebaseDatabase.getInstance().getReference().child("dispositivos"), Dispositivos.class)
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("dispositivos").orderByChild("id_usuario").equalTo(userID), Dispositivos.class)
                        .build();

        FirebaseRecyclerAdapter<Dispositivos, myviewholder> adapter =
                new FirebaseRecyclerAdapter<Dispositivos, myviewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull Dispositivos model) {
                        holder.nombreDispositivo.setText(model.getAlias());
                        holder.macDispositivo.setText(model.getMac());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //String mac = getRef(position).getKey();
                               // String mac = model.getMac();

                                if(fecha.equals("sinFecha")) {
                                    //Toast.makeText(VistaRecordatoriosActivity.this,"NO HAY FECHA", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplication(), ListaRecordatoriosActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("fecha", fecha);
                                    bundle.putString("userID", userID);
                                    bundle.putString("mac", model.getMac());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                                else{
                                    Intent intent = new Intent(getApplication(), ListaRecordatoriosActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("fecha", fecha);
                                    bundle.putString("userID", userID);
                                    bundle.putString("mac", model.getMac());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            }
                        });
                    }


                    @NonNull
                    @Override
                    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_vista_dispositivos, parent, false);
                        myviewholder viewHolder = new myviewholder(view);
                        return viewHolder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
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
            Toast.makeText(VistaRecordatoriosActivity.this,"Cerrar sesion", Toast.LENGTH_SHORT).show();
            startActivity( new Intent(VistaRecordatoriosActivity.this, StartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
