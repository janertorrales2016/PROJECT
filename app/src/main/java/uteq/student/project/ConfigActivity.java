package uteq.student.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import uteq.student.project.Model.Info;


public class ConfigActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{
    String selecionado="";
    private static final String CERO = "0";
    private static final String BARRA = "/";
    private DatabaseReference mReference;
    private EditText nombre, apellido, celular, direccion, fecha, pass,passrepet;
    private TextView rol;

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    //Widgets
    EditText etFecha;
    ImageButton ibObtenerFecha;
    private FirebaseAuth auth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);


        //Widget EditText donde se mostrara la fecha obtenida
        etFecha = (EditText) findViewById(R.id.et_mostrar_fecha_picker);
        //Widget ImageButton del cual usaremos el evento clic para obtener la fecha
        ibObtenerFecha = (ImageButton) findViewById(R.id.ib_obtener_fecha);
        //Evento setOnClickListener - clic
        ibObtenerFecha.setOnClickListener(this);
        nombre= (EditText) findViewById(R.id.txtnombre);
        apellido = (EditText) findViewById(R.id.txtapellido);
        celular = (EditText) findViewById(R.id.txtcelular);
        direccion = (EditText) findViewById(R.id.txtdireccion);
        fecha = (EditText) findViewById(R.id.et_mostrar_fecha_picker);
        rol =(TextView) findViewById(R.id.rol);
        pass=(EditText) findViewById(R.id.txtpassword);
        passrepet =(EditText) findViewById(R.id.txtrepeatpassword);

        mReference = FirebaseDatabase.getInstance().getReference("usuario").child(user.getUid());
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Info inf = snapshot.getValue(Info.class);
                nombre.setText(inf.getNombre());
                apellido.setText(inf.getApellido());
                direccion.setText(inf.getDireccion());
                celular.setText(inf.getCelular());
                fecha.setText(inf.getFecha_nacimiento());
                rol.setText(inf.getRol());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        selecionado = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void btnUpdatePassword(View view){

            user.updatePassword(pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(ConfigActivity.this,"Update Successful", Toast.LENGTH_SHORT).show();
                        startActivity( new Intent(ConfigActivity.this, StartActivity.class));

                    }else{
                        Toast.makeText(ConfigActivity.this,"Error Update", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }
    public void btnsave(View view){
        try {
                nombre= findViewById(R.id.txtnombre);
               apellido= findViewById(R.id.txtapellido);
              EditText celular= findViewById(R.id.txtcelular);
              EditText direccion= findViewById(R.id.txtdireccion);
              EditText fecha= findViewById(R.id.et_mostrar_fecha_picker);
            /*  EditText pass= findViewById(R.id.txtpass);
            EditText pass2= findViewById(R.id.txtpass2);*/
            HashMap<String, Object> map= new HashMap<>();
            map.put("nombre", nombre.getText().toString() );
            map.put("apellido", apellido.getText().toString() );
            map.put("direccion", direccion.getText().toString());
            map.put("celular", celular.getText().toString() );
            map.put("fecha_nacimiento", fecha.getText().toString() );
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String currentDateandTime = sdf.format(new Date());
            map.put("fecha_update", currentDateandTime);
            map.put("rol", selecionado);
            FirebaseDatabase.getInstance().getReference().child("usuario").child(user.getUid()).updateChildren(map);
            Toast.makeText(ConfigActivity.this,"corecto", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ConfigActivity.this, MainActivity.class));
        }catch (Exception e){

            Toast.makeText(ConfigActivity.this,"erro", Toast.LENGTH_SHORT).show();

        }

    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_obtener_fecha:
                obtenerFecha();
                break;
        }
    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                etFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }

}