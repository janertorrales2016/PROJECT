package uteq.student.project.Model;


public class paciente {
    private String nombre;
    private String id;

    public paciente(String nombre, String id) {
        this.nombre = nombre;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public  paciente(){}

    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    @Override
    public String toString() {
        return nombre;
    }

}
