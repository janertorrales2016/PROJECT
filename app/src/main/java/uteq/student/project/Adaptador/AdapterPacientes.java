package uteq.student.project.Adaptador;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import uteq.student.project.Model.Info;
import uteq.student.project.R;
import uteq.student.project.VistaRecordatoriosActivity;

public class AdapterPacientes extends RecyclerView.Adapter<AdapterPacientes.ViewHolder> {

    private List<Info> modelPacientesList ;
    private Context context;

    public AdapterPacientes(List<Info> modelPacientesList, Context context) {
        this.modelPacientesList = modelPacientesList;
        this.context = context;
    }

    @Override
    public AdapterPacientes.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_vista_pacientes,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Info modelActivity = modelPacientesList.get(position);

        holder.nombre.setText(modelActivity.getNombre());
        holder.apellido.setText(modelActivity.getApellido());
        holder.telefono.setText(modelActivity.getCelular());
        holder.fecha.setText(modelActivity.getFecha_nacimiento());
        holder.direccion.setText(modelActivity.getDireccion());

        //holder.description.setText(Html.fromHtml(modelActivity.getDescription()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(holder.itemView.getContext(), VistaRecordatoriosActivity.class);
                intent.putExtra("userID", modelActivity.getId());
                intent.putExtra("fecha", "sinFecha");
                holder.itemView.getContext().startActivity(intent);


                //Intent intent = new Intent(getApplication(), VistaRecordatoriosActivity.class);
               /* Bundle bundle = new Bundle();
                bundle.putString("userID", userID);
                bundle.putString("fecha", "sinFecha");
                intent.putExtras(bundle);
                startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelPacientesList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nombre,apellido,fecha,telefono,direccion;

        public ViewHolder(View itemView) {
            super(itemView);
            nombre=itemView.findViewById(R.id.rvNombrePaciente);
            apellido=itemView.findViewById(R.id.rvApellidoPaciente);
            fecha=itemView.findViewById(R.id.rvFechaNacimientoPaciente);
            telefono=itemView.findViewById(R.id.rvCelularPaciente);
            direccion=itemView.findViewById(R.id.rvDireccionPaciente);
        }
    }
}
