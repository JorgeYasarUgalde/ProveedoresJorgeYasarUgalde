package company.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProveedor extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Context contextHere;

    private EditText nombre;
    private EditText descripcion;
    private EditText contacto;
    private EditText articulos;
    private EditText precio;

    private Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_proveedor);

        contextHere = getApplicationContext();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nombre = findViewById(R.id.et_nombre_proveedor);
        descripcion = findViewById(R.id.et_descripcion);
        contacto = findViewById(R.id.et_contacto);
        articulos = findViewById(R.id.et_articulos);
        precio = findViewById(R.id.et_precio);

        btnAgregar = findViewById(R.id.btn_agregar_proveedor);
        
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarProveedor(
                        nombre.getText().toString(),
                        descripcion.getText().toString(),
                        contacto.getText().toString(),
                        articulos.getText().toString(),
                        Double.valueOf(precio.getText().toString())
                );

                //**lanzar lista de proveedores*/
            }
        });
    }


    private void agregarProveedor(String nombre, String descripcion, String contacto, String articulos, double precio){
        Proveedor proveedor = new Proveedor(nombre,descripcion,contacto,articulos,precio,0,false);
        mDatabase.child("Proveedores").child(nombre).setValue(proveedor);

        SimpleAlertDialog myAlert = new SimpleAlertDialog();
        myAlert.show(AddProveedor.this,"Proveedor agregado Correctamente");
    }


}