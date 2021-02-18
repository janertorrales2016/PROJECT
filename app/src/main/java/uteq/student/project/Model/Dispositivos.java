package uteq.student.project.Model;

public class Dispositivos {
    String id;
    String alias;
    String mac;
    String id_usuario;

    public Dispositivos(){}

    public Dispositivos(String id, String alias, String mac, String id_usuario) {
        this.id = id;
        this.alias = alias;
        this.mac = mac;
        this.id_usuario = id_usuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    @Override
    public String toString() {
        return alias ;
    }
}
