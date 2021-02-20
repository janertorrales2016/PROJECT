package uteq.student.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import uteq.student.project.Model.Dispositivos;
import uteq.student.project.Model.Recordatorios;

public class ListaRecordatoriosActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String fecha,userID,mac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_recordatorios);

        //CARGAR TOOLBAR
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fecha = getIntent().getStringExtra("fecha");
        userID=getIntent().getStringExtra("userID");
        mac=getIntent().getStringExtra("mac");
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) findViewById(R.id.rcl_lista_dispositivos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Recordatorios> options;
        if(fecha.equals("sinFecha")) {
             options =
                    new FirebaseRecyclerOptions.Builder<Recordatorios>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("datos").child("recordatorios").child(mac), Recordatorios.class)
                            .build();
        }
        else{
            options =
                    new FirebaseRecyclerOptions.Builder<Recordatorios>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("datos").child("recordatorios").child(mac).orderByChild("fecha").equalTo(fecha), Recordatorios.class)
                            .build();
        }

        FirebaseRecyclerAdapter<Recordatorios, ListaRecordatoriosActivity.myviewholder> adapter =
        new FirebaseRecyclerAdapter<Recordatorios, myviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull Recordatorios model) {
                holder.tituloRecordatorio.setText(model.getMensaje());
                holder.fechaRecordatorio.setText(model.getFecha());
                holder.horaRecordatorio.setText(model.getHora());
                holder.estadoRecordatorio.setText(model.getEstado());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = getRef(position).getKey();
                        //Toast.makeText(ListaRecordatoriosActivity.this,id, Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListaRecordatoriosActivity.this);
                        CharSequence[] items =new CharSequence[2];
                        items[0] = "Eliminar Recordatorio";
                        items[1] = "Cancelar";
                        //builder.setTitle("Seleccione una Actividad");
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0)
                                {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("datos").child("recordatorios").child(mac).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ListaRecordatoriosActivity.this, "Remove Successfull", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(ListaRecordatoriosActivity.this, "Remove Failed" , Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        builder.setCancelable(false);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }

            @NonNull
            @Override
            public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_vista_recordatorios, parent, false);
                ListaRecordatoriosActivity.myviewholder viewHolder = new ListaRecordatoriosActivity.myviewholder(view);//new VistaRecordatoriosActivity.myviewholder(view);
                return viewHolder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    class myviewholder extends RecyclerView.ViewHolder {
        TextView tituloRecordatorio, fechaRecordatorio,horaRecordatorio, estadoRecordatorio;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            // urlimagen=(ImageView) itemView.findViewById(R.id.imgPortada_user);
            tituloRecordatorio = (TextView) itemView.findViewById(R.id.rvTituloListaRecordatorios);
            fechaRecordatorio=(TextView) itemView.findViewById(R.id.rvFechaListaRecordatorios);
            horaRecordatorio = (TextView) itemView.findViewById(R.id.rvHoraListaRecordatorios);
            estadoRecordatorio=(TextView) itemView.findViewById(R.id.rvEstadoListaRecordatorios);
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
            Toast.makeText(ListaRecordatoriosActivity.this,"Cerrar sesion", Toast.LENGTH_SHORT).show();
            startActivity( new Intent(ListaRecordatoriosActivity.this, StartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}