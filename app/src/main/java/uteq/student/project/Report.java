package uteq.student.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import uteq.student.project.Model.Dispositivos;
import uteq.student.project.Model.datareport;
import uteq.student.project.Model.paciente;
import uteq.student.project.R;

public class Report extends AppCompatActivity {

    //firebase auth to check if user is authenticated.
    private FirebaseAuth mAuth;
    private DatabaseReference payRef;
    private Spinner spinpersonas, spindospositivos;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String IdOring = user.getUid();
    //creating a list of objects constants
    List<datareport> paymentUsersList;


    //List all permission required
    public static String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static int PERMISSION_ALL = 12;

    private  Button reportButton;
    public static File pFile;
    private PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        spinpersonas = findViewById(R.id.personas);
        spindospositivos = findViewById(R.id.dispositivos);
        primero();
        mAuth = FirebaseAuth.getInstance();

        pdfView = findViewById(R.id.payment_pdf_viewer);
         reportButton = findViewById(R.id.button_disable_report);
        reportButton.setClickable(false);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previewDisabledUsersReport();

            }
        });



        //fetch payment and disabled users details;
        //fetchPaymentUsers();
    }
    private void primero(){
        List<paciente> pacientes = new ArrayList<>();
        pacientes.add(new paciente("Select ","-1"));
        pacientes.add(new paciente("YOU",IdOring));
        payRef = FirebaseDatabase.getInstance().getReference("registro").child(IdOring);
        payRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){
                        String id = ds.getKey();
                        String nombre = ds.child("nombre").getValue().toString();
                        pacientes.add(new paciente(nombre,id));
                    }
                    ArrayAdapter<paciente> arrayAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_dropdown_item_1line,pacientes);
                    spinpersonas.setAdapter(arrayAdapter);
                    spinpersonas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(pacientes.get(position).getId().equals("-1")){
                                reportButton.setClickable(false);
                                spindospositivos.setAdapter(null);

                            }else{
                                segundo(pacientes.get(position).getId());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private String NOMBRE="";
    private void segundo(String valor){

        List<Dispositivos> dispositivos = new ArrayList<>();
        dispositivos.add(new Dispositivos("-1", "Select", "",""));
        payRef = FirebaseDatabase.getInstance().getReference("dispositivos");
        payRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String id = ds.getKey();
                        String alias = ds.child("alias").getValue().toString();
                        String mac = ds.child("mac").getValue().toString();
                        String user_id=ds.child("id_usuario").getValue().toString();
                        if(user_id.equals(valor))
                            dispositivos.add(new Dispositivos(id, alias, mac,user_id));
                    }
                    ArrayAdapter<Dispositivos> arrayAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_dropdown_item_1line, dispositivos);
                    spindospositivos.setAdapter(arrayAdapter);
                    spindospositivos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if(dispositivos.get(i).getId().equals("-1")){

                            }else {
                                NOMBRE= dispositivos.get(i).getAlias();
                                fetchPaymentUsers(dispositivos.get(i).getMac());
                            }

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
    private void fetchPaymentUsers(String mac) {
        paymentUsersList = new ArrayList<>();
        payRef =  FirebaseDatabase.getInstance().getReference("datos").child("recordatorios").child(mac);
        payRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        datareport pays = new datareport();
                        pays.setEstado(snapshot.child("estado").getValue().toString());
                        pays.setFecha(snapshot.child("fecha").getValue().toString());
                        pays.setHora(snapshot.child("hora").getValue().toString());
                        pays.setMensaje(snapshot.child("mensaje").getValue().toString());
                        paymentUsersList.add(pays);
                    }
                    reportButton.setClickable(true);
                    //create a pdf file and catch exception beacause file may not be created
                    try {
                        createPaymentReport(paymentUsersList);
                    } catch (DocumentException | FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }else {
                    reportButton.setClickable(false);
                    Toast.makeText(Report.this,"no reminders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void createPaymentReport(  List<datareport> paymentUsersList) throws DocumentException, FileNotFoundException{
        pFile = new File(Environment.getExternalStorageDirectory(), NOMBRE+"REPORT.pdf");
        BaseColor colorWhite = WebColors.getRGBColor("#ffffff");
        BaseColor colorBlue = WebColors.getRGBColor("#056FAA");
        BaseColor grayColor = WebColors.getRGBColor("#425066");

        Font white = new Font(Font.FontFamily.HELVETICA, 15.0f, Font.BOLD, colorWhite);
        FileOutputStream output = new FileOutputStream(pFile);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(new float[]{15, 15, 20, 20});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(50);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        Chunk noText = new Chunk("Fecha", white);
        PdfPCell noCell = new PdfPCell(new Phrase(noText));
        noCell.setFixedHeight(50);
        noCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        noCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk nameText = new Chunk("Hora", white);
        PdfPCell nameCell = new PdfPCell(new Phrase(nameText));
        nameCell.setFixedHeight(50);
        nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        nameCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk phoneText = new Chunk("Mensaje", white);
        PdfPCell phoneCell = new PdfPCell(new Phrase(phoneText));
        phoneCell.setFixedHeight(50);
        phoneCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        phoneCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk amountText = new Chunk("Estado", white);
        PdfPCell amountCell = new PdfPCell(new Phrase(amountText));
        amountCell.setFixedHeight(50);
        amountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        amountCell.setVerticalAlignment(Element.ALIGN_CENTER);


        table.addCell(noCell);
        table.addCell(nameCell);
        table.addCell(phoneCell);
        table.addCell(amountCell);
        table.setHeaderRows(1);

        PdfPCell[] cells = table.getRow(0).getCells();

        for (PdfPCell cell : cells) {
            cell.setBackgroundColor(grayColor);
        }
        for (int i = 0; i < paymentUsersList.size(); i++) {
            datareport pay = paymentUsersList.get(i);
            String id = pay.getFecha();
            String name = pay.getHora();
            String sname = pay.getMensaje();
            String phone = pay.getEstado();
            table.addCell(id);
            table.addCell(name);
            table.addCell(sname);
            table.addCell(phone);
        }
        PdfWriter.getInstance(document, output);
        document.open();
        Font g = new Font(Font.FontFamily.HELVETICA, 25.0f, Font.NORMAL, grayColor);
        document.add(new Paragraph("Report\n\n", g));
        document.add(table);
        document.close();
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void previewDisabledUsersReport()
    {
        if (hasPermissions(this, PERMISSIONS)) {
            DisplayReport();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    private void DisplayReport()
    {
        pdfView.fromFile(pFile)
                .pages(0,2,1,3,3,3)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .load();
    }
}