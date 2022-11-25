package company.project;

import java.util.function.BiPredicate;

public class Proveedor {

    public String nombre;
    public String descripcion;
    public String contacto;
    public String articulos;
    public double precio;

    public double totalAPagar;
    public Boolean existencias;

    public Proveedor() {

    }

    public Proveedor(String role,String descripcion,String contacto,String articulos,double precio,double totalAPagar,Boolean existencias) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.contacto = contacto;
        this.articulos = articulos;
        this.precio = precio;

        this.totalAPagar = totalAPagar;
        this.existencias = existencias;
    }

}