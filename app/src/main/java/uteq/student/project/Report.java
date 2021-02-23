package uteq.student.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import uteq.student.project.R;

public class Report extends AppCompatActivity {

    BarChart graficBarras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        graficBarras = (BarChart) findViewById(R.id.graficabarras);
    }

    public void btnGenerate(View view){
        //entrada con los valores de entrada
        List<BarEntry> entradas = new ArrayList<>();

        entradas.add( new BarEntry(0,2));
        entradas.add( new BarEntry(1,2));
        entradas.add( new BarEntry(2,2));
        entradas.add( new BarEntry(3,2));
        entradas.add( new BarEntry(4,8));
        entradas.add( new BarEntry(5,2));

        //mandamos los datos para crear la grafica
        BarDataSet datos = new BarDataSet(entradas, "Reporte de recordatorios");
        BarData data = new  BarData(datos);

        //ponemos el color a cada barra
        datos.setColors(ColorTemplate.COLORFUL_COLORS);

        //separacion etre las barras
        data.setBarWidth(0.9f);

        graficBarras.setData(data);

        //poner las barras centrada
        graficBarras.setFitBars(true);

        graficBarras.invalidate(); //hacer refresh


    }
}