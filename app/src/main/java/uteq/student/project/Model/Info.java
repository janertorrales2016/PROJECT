package uteq.student.project.Model;

public class Info {
    public String id;
    private String apellido;
    private String celular;
    private String direccion;
    private String fecha_create;
    private String fecha_nacimiento;
    private String fecha_update;
    private String nombre;
    private String rol;

    public Info(String id, String apellido, String celular, String direccion, String fecha_create, String fecha_nacimiento, String fecha_update, String nombre, String rol) {
        this.id = id;
        this.apellido = apellido;
        this.celular = celular;
        this.direccion = direccion;
        this.fecha_create = fecha_create;
        this.fecha_nacimiento = fecha_nacimiento;
        this.fecha_update = fecha_update;
        this.nombre = nombre;
        this.rol = rol;
    }

    public Info(String id, String apellido, String celular, String direccion, String fecha_nacimiento, String nombre) {
        this.id = id;
        this.apellido = apellido;
        this.celular = celular;
        this.direccion = direccion;
        this.fecha_nacimiento = fecha_nacimiento;
        this.nombre = nombre;
    }

    public Info(String id, String apellido, String nombre) {
        this.id = id;
        this.apellido = apellido;
        this.nombre = nombre;
    }

    public Info(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFecha_create() {
        return fecha_create;
    }

    public void setFecha_create(String fecha_create) {
        this.fecha_create = fecha_create;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getFecha_update() {
        return fecha_update;
    }

    public void setFecha_update(String fecha_update) {
        this.fecha_update = fecha_update;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        /*return "Info{" +
                "id='" + id + '\'' +
                ", apellido='" + apellido + '\'' +
                ", celular='" + celular + '\'' +
                ", direccion='" + direccion + '\'' +
                ", fecha_create='" + fecha_create + '\'' +
                ", fecha_nacimiento='" + fecha_nacimiento + '\'' +
                ", fecha_update='" + fecha_update + '\'' +
                ", nombre='" + nombre + '\'' +
                ", rol='" + rol + '\'' +
                '}';*/
        return  apellido+" "+nombre;
    }
}
