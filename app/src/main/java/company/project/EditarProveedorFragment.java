package company.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class EditarProveedorFragment extends Fragment {

    private TextView tv_proveedor;
    private EditText et_name;
    private EditText et_descripcion;
    private EditText et_contacto;
    private EditText et_articulos;
    private EditText et_precio;

    private Button btnGuardar;

    private String Proveedor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_editar_proveedor, container, false);

        tv_proveedor = root.findViewById(R.id.tv_proveedor);

        et_name = root.findViewById(R.id.et_name);
        et_descripcion = root.findViewById(R.id.et_descripcion);
        et_contacto = root.findViewById(R.id.et_contacto);
        et_articulos = root.findViewById(R.id.et_articulos);
        et_precio = root.findViewById(R.id.et_precio);

        btnGuardar = root.findViewById(R.id.btn_guardar_proveedor);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getContext());

        Proveedor = sharedPreferences.getString("SelectedProveedores", "");
        tv_proveedor.setText(Proveedor);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference refUsers = FirebaseDatabase.getInstance().getReference().child("Proveedores").child(Proveedor);

                refUsers.child("name").setValue(et_name.getText().toString());
                refUsers.child("descripcion").setValue(et_descripcion.getText().toString());
                refUsers.child("contacto").setValue(et_contacto.getText().toString());
                refUsers.child("articulos").setValue(et_articulos.getText().toString());
                refUsers.child("precio").setValue(et_precio.getText().toString());

                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("SelectedProveedores", et_name.getText().toString());
                editor.apply();

                Toast.makeText(getContext(),"Cambios Aplicados",Toast.LENGTH_SHORT).show();

                Navigation.findNavController(view).navigate(R.id.lectorQRFragment);
            }
        });


        return root;
    }

}
