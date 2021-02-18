package uteq.student.project.Model;

public class Recordatorios {

    String estado;
    String fecha;
    String hora;
    String mensaje;

    public Recordatorios(){

    }

    public Recordatorios(String estado, String fecha, String hora, String mensaje) {
        this.estado = estado;
        this.fecha = fecha;
        this.hora = hora;
        this.mensaje = mensaje;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
