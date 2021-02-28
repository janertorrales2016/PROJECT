package uteq.student.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uteq.student.project.Model.Dispositivos;
import uteq.student.project.Model.paciente;

public class AddRecordatorioActivity extends AppCompatActivity {

    TextView tvHora, tvFecha, tvTitulo;
    private int horas, minutos;

    String userID;

    DatabaseReference mDataBase;
    Spinner spListaDispositivos, repeticionrecordatorio;

    String hour = "";
    String minute = "";

    String macSeleccionada = "";
    String tipoRepeticion = "No repeat";

    int intervaloHoras = 0, veces = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recordatorio);

        //CARGAR TOOLBAR
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvTitulo = (TextView) findViewById(R.id.tituloRecordatorio);
        tvHora = (TextView) findViewById(R.id.hora);
        tvFecha = (TextView) findViewById(R.id.fecha);
        spListaDispositivos = findViewById(R.id.splistadispositivos);

        mDataBase = FirebaseDatabase.getInstance().getReference();

        String fecha = getIntent().getStringExtra("fecha");
        userID = getIntent().getStringExtra("userID");

        tvFecha.setText(fecha);
        listaDispositivos();


        repeticionrecordatorio = (Spinner) findViewById(R.id.sprepetirrecordatorio);
        //String[] valores = {"Sin repetición", "Intervalo de horas", "Todos los dias", "Lunes a Viernes", "Sábado y Domingo", "Personalizar"};
        String[] valores = {"No repeat", "Interval hours", "Everyday"};
        repeticionrecordatorio.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, valores));
        repeticionrecordatorio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String a = (String) adapterView.getItemAtPosition(position);
                if (a.equals("No repeat")) {
                    repeticionrecordatorio.setSelection(0);

                } else if (a.equals("Interval hours")) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddRecordatorioActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.cantidad_horas, null);
                    mBuilder.setTitle("Enter data");
                    mBuilder.setCancelable(false);
                    TextView intervaloXhoras = (TextView) mView.findViewById(R.id.intervaloXhoras);
                    TextView vecesXhoras = (TextView) mView.findViewById(R.id.vecesXhoras);

                    mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (String.valueOf(intervaloXhoras.getText().toString()).length() > 0 && String.valueOf(vecesXhoras.getText().toString()).length() > 0) {
                                intervaloHoras = Integer.parseInt(intervaloXhoras.getText().toString());
                                veces = Integer.parseInt(vecesXhoras.getText().toString());
                            }
                            if (intervaloHoras > 0 && veces > 0) {
                                dialogInterface.dismiss();
                            } else {
                                Toast.makeText(adapterView.getContext(), "Enter values", Toast.LENGTH_SHORT).show();
                                repeticionrecordatorio.setSelection(0);
                            }
                        }
                    });
                    mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            repeticionrecordatorio.setSelection(0);
                            dialogInterface.dismiss();
                        }
                    });
                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();

                } else if (a.equals("Everyday")) {
                    //Toast.makeText(adapterView.getContext(), "ENTRA", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddRecordatorioActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.cantidad_dias, null);
                    mBuilder.setTitle("Enter data");
                    mBuilder.setCancelable(false);
                    TextView vecesXdias = (TextView) mView.findViewById(R.id.vecesXdias);

                    mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (String.valueOf(vecesXdias.getText().toString()).length() > 0) {
                                veces = Integer.parseInt(vecesXdias.getText().toString());
                            }
                            if (veces > 0) {
                                // tipoRepeticion = "Intervalo de horas";
                                dialogInterface.dismiss();
                            } else {
                                Toast.makeText(adapterView.getContext(), "Enter values", Toast.LENGTH_SHORT).show();
                                repeticionrecordatorio.setSelection(0);
                            }
                        }
                    });

                    mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            repeticionrecordatorio.setSelection(0);
                            dialogInterface.dismiss();
                        }
                    });
                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();

                } else if (a.equals("Personalizar")) {
                    //Toast.makeText(adapterView.getContext(), (String) adapterView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddRecordatorioActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                    mBuilder.setTitle("Seleccionar");
                    //   androidx.appcompat.app.AlertDialog dialog = builder.create();

                    CheckBox ckLunes = (CheckBox) mView.findViewById(R.id.ckLunes);
                    CheckBox ckMartes = (CheckBox) mView.findViewById(R.id.ckMartes);
                    CheckBox ckMiercoles = (CheckBox) mView.findViewById(R.id.ckMiercoles);
                    CheckBox ckJueves = (CheckBox) mView.findViewById(R.id.ckJueves);
                    CheckBox ckViernes = (CheckBox) mView.findViewById(R.id.ckViernes);
                    CheckBox ckSabado = (CheckBox) mView.findViewById(R.id.ckSabado);
                    CheckBox ckDomingo = (CheckBox) mView.findViewById(R.id.ckDomingo);

                    mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(adapterView.getContext(), (String) adapterView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    });

                    mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();
                    //mBuilder.show();

                } else {
                    Toast.makeText(adapterView.getContext(), a, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //String a = (String) adapterView.getSelectedItem();//getItemAtPosition(position);
                //Toast.makeText(adapterView.getContext(), "Sin seleccion", Toast.LENGTH_SHORT).show();

                //  String spinnerText = ((TextView) findViewById(R.id.sprepetirrecordatorio)).getText().toString();

            }
        });
    }
    public void listaDispositivos() {
        List<Dispositivos> dispositivos = new ArrayList<>();
        mDataBase.child("dispositivos").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String id = ds.getKey();
                        String alias = ds.child("alias").getValue().toString();
                        String mac = ds.child("mac").getValue().toString();
                        String user_id=ds.child("id_usuario").getValue().toString();
                        if(user_id.equals(userID))
                        dispositivos.add(new Dispositivos(id, alias, mac,user_id));
                    }

                    ArrayAdapter<Dispositivos> arrayAdapter = new ArrayAdapter<>(AddRecordatorioActivity.this, android.R.layout.simple_dropdown_item_1line, dispositivos);
                    spListaDispositivos.setAdapter(arrayAdapter);
                    spListaDispositivos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            //macSeleccionada=adapterView.getItemAtPosition(i).toString();
                            macSeleccionada = dispositivos.get(i).getMac();
                            //int valor = getIntent().getExtras().getInt("dia");    //getStringExtra("dia");

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void listaPacientes(){
        List<paciente> pacientes = new ArrayList<>();
        mDataBase.child("registro").orderByChild("id_usuario").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void agregarRecordatorio(View view) {
        tipoRepeticion = repeticionrecordatorio.getSelectedItem().toString();
        if (tipoRepeticion.equals("No repeat")) {
            ingresarRecordatorio(tvFecha.getText().toString(), tvHora.getText().toString(), tvTitulo.getText().toString());

        } else if (tipoRepeticion.equals("Interval hours")) {
            try {
                //Calendar calendario = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
                String dateInString = tvFecha.getText().toString() + " " + tvHora.getText().toString() + ":00";
                Date date = sdf.parse(dateInString);
                //calendario.setTime(date);

                for (int i = 0; i < veces; i++) {
                    //Calendar calendario = Calendar.getInstance();

                    String dia = String.valueOf(date.getDate());
                    String mes = String.valueOf(date.getMonth() + 1);
                    String anio = String.valueOf(date.getYear());
                    String horas = String.valueOf(date.getHours());
                    String minutos = String.valueOf(date.getMinutes());

                    String aux = anio.substring(0, 1);
                    anio = anio.substring(1, 3);

                    if (aux.equals("0"))
                        anio = "19" + anio;
                    else if (aux.equals("1"))
                        anio = "20" + anio;
                    else anio = "21" + anio;

                    if (dia.length() == 1)
                        dia = "0" + dia;
                    if (mes.length() == 1)
                        mes = "0" + mes;
                    if (horas.length() == 1)
                        horas = "0" + horas;
                    if (minutos.length() == 1)
                        minutos = "0" + minutos;

                    ingresarRecordatorio(dia+"-"+mes+"-"+anio,horas+":"+minutos,tvTitulo.getText().toString());

                    //  String spinnerText = spListaDispositivos.getSelectedItem().toString();

                    date = sumarRestarHorasFecha(date, intervaloHoras);
                    /*Toast toast1 = Toast.makeText(getApplicationContext(), spinnerText, Toast.LENGTH_SHORT);
                    toast1.show();*/
                }

                //String ax = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
                //Toast toast1 = Toast.makeText(getApplicationContext(), ax, Toast.LENGTH_SHORT);
                //toast1.show();
            } catch (Exception e) {
            }
            //String fechaRecordatorio = tvFecha.getText().toString() + " " + tvHora.getText().toString() + ":00";


        } else if (tipoRepeticion.equals("Everyday")) {
            try {
                //Calendar calendario = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
                String dateInString = tvFecha.getText().toString() + " " + tvHora.getText().toString() + ":00";
                Date date = sdf.parse(dateInString);
                //calendario.setTime(date);

                for (int i = 0; i < veces; i++) {
                    String dia = String.valueOf(date.getDate());
                    String mes = String.valueOf(date.getMonth() + 1);
                    String anio = String.valueOf(date.getYear());
                    String horas = String.valueOf(date.getHours());
                    String minutos = String.valueOf(date.getMinutes());

                    String aux = anio.substring(0, 1);
                    anio = anio.substring(1, 3);

                    if (aux.equals("0"))
                        anio = "19" + anio;
                    else if (aux.equals("1"))
                        anio = "20" + anio;
                    else anio = "21" + anio;

                    if (dia.length() == 1)
                        dia = "0" + dia;
                    if (mes.length() == 1)
                        mes = "0" + mes;
                    if (horas.length() == 1)
                        horas = "0" + horas;
                    if (minutos.length() == 1)
                        minutos = "0" + minutos;

                    ingresarRecordatorio(dia + "-" + mes + "-" + anio, horas + ":" + minutos, tvTitulo.getText().toString());
                    date = sumarRestarDiasFecha(date, 1);

                }
            } catch (Exception e) {
                Toast toast = Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT);
                toast.show();
            }
        }


    }


    public void ingresarRecordatorio(String fecha, String hora, String titulo) {
        Map<String, Object> recordatorioMap = new HashMap<>();
        recordatorioMap.put("estado", "activo");
        recordatorioMap.put("fecha", fecha);
        recordatorioMap.put("hora", hora);
        recordatorioMap.put("mensaje", titulo);

        if (titulo.length() > 0 && hora.length() > 0) {
            mDataBase.child("datos").child("recordatorios").child(macSeleccionada).push().setValue(recordatorioMap);
            Toast toast = Toast.makeText(getApplicationContext(), "Add Successfull", Toast.LENGTH_SHORT);
            toast.show();
            super.onBackPressed();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter all data", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // SUMA LAS HORAS RECIBIDAS A LA FECHA
    public Date sumarRestarHorasFecha(Date fecha, int horas) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.HOUR, horas);  // numero de horas a añadir, o restar en caso de horas<0
        return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas
    }

    // SUMA LOS DIAS RECIBIDAS A LA FECHA
    public Date sumarRestarDiasFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de horas a añadir, o restar en caso de horas<0
        return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas
    }

    public void clickTextView(View view) {
        final Calendar c = Calendar.getInstance();

        if (hour.equals("")) {
            horas = c.get(Calendar.HOUR);
            minutos = c.get(Calendar.MINUTE);
            int am_pm = c.get(Calendar.AM_PM);
            if(am_pm==1)
                horas=horas+12;
        } else {
            horas = Integer.parseInt(hour);
            minutos = Integer.parseInt(minute);
        }

        TimePickerDialog ingresarHora = new TimePickerDialog(AddRecordatorioActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int tpHoras, int tpMinutos) {
                        hour = Integer.toString(tpHoras);
                        minute = Integer.toString(tpMinutos);
                        if (hour.length() == 1)
                            hour = "0" + hour;
                        if (minute.length() == 1)
                            minute = "0" + minute;
                        tvHora.setText(hour + ":" + minute);
                    }
                },
                horas,
                minutos,
                false);
        ingresarHora.setTitle("Select hour");
        ingresarHora.show();
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
            Toast.makeText(AddRecordatorioActivity.this,"Cerrar sesion", Toast.LENGTH_SHORT).show();
            startActivity( new Intent(AddRecordatorioActivity.this, StartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}