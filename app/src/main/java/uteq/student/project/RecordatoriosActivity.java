package uteq.student.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uteq.student.project.Model.Dispositivos;

public class RecordatoriosActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener{

    //DECLARACION DE VARIABLES
    private CalendarView calendarView;
    //int val = 0;
    String sDia = "", sMes = "", sAnio = "";
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorios);

        //CARGAR TOOLBAR
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userID = getIntent().getStringExtra("userID");

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        long a = calendarView.getDate();
        calendarView.setOnDateChangeListener(this);
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        CharSequence[] items = null;

        //CONVERTIR A STRING LA FECHA SELECCIONADA
        sDia = String.valueOf(i2);
        sMes = String.valueOf(i1 + 1);
        sAnio = String.valueOf(i);
        if (sDia.length() == 1)
            sDia = "0" + sDia;
        if (sMes.length() == 1)
            sMes = "0" + sMes;

        String fecha = sDia + "-" + sMes + "-" + sAnio;

        //OBTENER FECHA Y HORA ACTUAL
        Calendar calendario = Calendar.getInstance();
        String kdia = String.valueOf(calendario.get(Calendar.DAY_OF_MONTH));
        String kmes = String.valueOf(calendario.get(Calendar.MONDAY) + 1);
        String kanio = String.valueOf(calendario.get(Calendar.YEAR));
        String khoras = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY));
        String kminutos = String.valueOf(calendario.get(Calendar.MINUTE));
        if (kdia.length() == 1)
            kdia = "0" + kdia;
        if (kmes.length() == 1)
            kmes = "0" + kmes;
        if (khoras.length() == 1)
            khoras = "0" + khoras;
        if (kminutos.length() == 1)
            kminutos = "0" + kminutos;
        String fechaActual = kdia + "-" + kmes + "-" + kanio + " " + khoras + ":" + kminutos + ":00";

        //OBTENER FECHA SELECCIONADA EN CALENDARIO
        String fechaSeleccionada = sDia + "-" + sMes + "-" + sAnio + " " + khoras + ":" + kminutos + ":00";

        try {
            //DAR FORMATO A LA FECHA
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            Date dateActual = sdf.parse(fechaActual);
            Date dateSeleccionado = sdf.parse(fechaSeleccionada);

            //COMPARAR LAS FECHAS
            String com = String.valueOf(dateActual.compareTo(dateSeleccionado));

            //SI LA FECHA ES MAYOR O IGUAL SE ABRE UN DIALOG
            if (com.equals("-1") || com.equals("0")) {
                items = new CharSequence[3];
                items[0] = "Add Reminder";
                items[1] = "Reminder View";
                items[2] = "Cancel";

                builder.setTitle("Select Activity");
                builder.setCancelable(false);
                builder.setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Intent intent = new Intent(getApplication(), AddRecordatorioActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("fecha", fecha);
                            bundle.putString("userID",userID);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        } else if (i == 1) {
                            Intent intent = new Intent(getApplication(), VistaRecordatoriosActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("fecha", fecha);
                            bundle.putString("userID",userID);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            return;
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            //CASO CONTRARIO OTRO DIALOG SIN LA OPCION DE AGREGAR RECORDATORIO
            else {
                items = new CharSequence[2];
                items[0] = "Reminder View";
                items[1] = "Cancel";

                builder.setTitle("Select Activity");
                builder.setCancelable(false);
                builder.setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            /*Intent intent = new Intent(getApplication(), view_EventsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("fecha", fecha);
                            intent.putExtras(bundle);
                            startActivity(intent);*/
                            Intent intent = new Intent(getApplication(), VistaRecordatoriosActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("fecha", fecha);
                            bundle.putString("userID",userID);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            return;
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }


        } catch (Exception e) {
            e.printStackTrace();
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
            Toast.makeText(RecordatoriosActivity.this,"Cerrar sesion", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}