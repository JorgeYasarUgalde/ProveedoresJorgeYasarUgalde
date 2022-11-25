package company.project.ui.add_proveedor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import company.project.AddProveedor;
import company.project.HomeAdminMenu;
import company.project.Proveedor;
import company.project.R;





public class AddProveedorFragment extends Fragment {

    private AddProveedorViewModel addProveedorViewModel;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Context contextHere;

    private EditText nombre;
    private EditText descripcion;
    private EditText contacto;
    private EditText articulos;
    private EditText precio;

    private Button btnAgregar;

    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addProveedorViewModel =
                new ViewModelProvider(this).get(AddProveedorViewModel.class);
        root = inflater.inflate(R.layout.fragment_add_proveedor, container, false);


        contextHere = root.getContext();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nombre = root.findViewById(R.id.et_nombre_proveedor);
        descripcion = root.findViewById(R.id.et_descripcion);
        contacto = root.findViewById(R.id.et_contacto);
        articulos = root.findViewById(R.id.et_articulos);
        precio = root.findViewById(R.id.et_precio);

        btnAgregar = root.findViewById(R.id.btn_agregar_proveedor);

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








        //final TextView textView = root.findViewById(R.id.text_gallery);
        addProveedorViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }











    private void agregarProveedor(String nombre, String descripcion, String contacto, String articulos, double precio){
        Proveedor proveedor = new Proveedor(nombre,descripcion,contacto,articulos,precio,0,false);
        mDatabase.child("Proveedores").child(nombre).setValue(proveedor);

        AlertDialog.Builder builder = new AlertDialog.Builder(contextHere);

        // Set the message show for the Alert time
        builder.setMessage("El Proveedor se ha agregado con exito");

        // Set Alert Title
        builder.setTitle("Operacion concluida");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Hecho", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            //finish();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }
}