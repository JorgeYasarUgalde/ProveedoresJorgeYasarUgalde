package company.project;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import company.project.R;
import company.project.ui.ver_proveedores.VerProveedoresViewModel;

public class LectorQRFragment extends Fragment {

    private FirebaseAuth mAuth;

    private VerProveedoresViewModel sensorCamaraViewModel;

    private TextView tvName;
    private TextView tvDescripcion;
    private TextView tvContacto;
    private TextView tvArticulos;
    private TextView tvPrecio;

    private Button qr;
    private Button btnEditar;
    private Button btnEliminar;

    String Proveedor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_lector_q_r, container, false);

        tvName = root.findViewById(R.id.tv_prov_name);
        tvDescripcion = root.findViewById(R.id.tv_prov_descripcion);
        tvContacto = root.findViewById(R.id.tv_prov_contacto);
        tvArticulos = root.findViewById(R.id.tv_prov_articulos);
        tvPrecio = root.findViewById(R.id.tv_prov_precio);

        btnEditar = root.findViewById(R.id.btn_prov_editar);
        btnEliminar = root.findViewById(R.id.btn_prov_eliminar);
        btnEliminar.setBackgroundColor(Color.parseColor("#FF0000"));

        mAuth = FirebaseAuth.getInstance();
        DatabaseReference refUsers = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        refUsers.child(mAuth.getUid()).child("role").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    //Toast.makeText(getApplicationContext(),task.getResult().getValue().toString(),Toast.LENGTH_SHORT).show();
                    if(task.getResult().getValue().toString().equals("ADMIN")){
                        btnEditar.setVisibility(View.VISIBLE);
                        btnEliminar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        qr = root.findViewById(R.id.qr);

        //SharedPreferences sharedPref = getActivity().getSharedPreferences("application", getContext().MODE_PRIVATE);

        //Proveedor = sharedPref.getString("SelectedProveedores", "");

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getContext());

        Proveedor = sharedPreferences.getString("SelectedProveedores", "");

        if( Proveedor.equals(""))
            Proveedor = "No Data";

        tvName.setText("Proveedor : " + Proveedor);

        setShowData(tvDescripcion,"descripcion");
        setShowData(tvContacto,"contacto");
        setShowData(tvArticulos,"articulos");
        setShowData(tvPrecio,"precio");

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Proveedor.equals("No Data")){
                    Toast.makeText(getContext(),"Proveedor no seleccionado",Toast.LENGTH_SHORT).show();
                }
                else{
                    Navigation.findNavController(view).navigate(R.id.editarProveedorFragment);
                }
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Proveedor.equals("No Data")){
                    Toast.makeText(getContext(),"Proveedor no seleccionado",Toast.LENGTH_SHORT).show();
                }
                else{
                    SharedPreferences sharedPreferences = PreferenceManager
                            .getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("SelectedProveedores", "");
                    editor.apply();

                    FirebaseDatabase.getInstance().getReference().child("Proveedores").child(Proveedor).removeValue();

                    Toast.makeText(getContext(),"Proveedor Eliminado",Toast.LENGTH_SHORT).show();

                    Navigation.findNavController(view).navigate(R.id.lectorQRFragment);
                }
            }
        });


        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Proveedor.equals("No Data")){
                    Toast.makeText(getContext(),"Proveedor no seleccionado",Toast.LENGTH_SHORT).show();
                }else {
                    try{
                        com.google.zxing.Writer writer = new QRCodeWriter();
                        // String finaldata = Uri.encode(data, "utf-8");
                        int width = 1024;
                        int height = 1024;
                        BitMatrix bm = writer
                                .encode(Proveedor, BarcodeFormat.QR_CODE, width, height);
                        Bitmap ImageBitmap = Bitmap.createBitmap(width, height,
                                Bitmap.Config.ARGB_8888);

                        for (int i = 0; i < width; i++) {// width
                            for (int j = 0; j < height; j++) {// height
                                ImageBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK
                                        : Color.WHITE);
                            }
                        }

                        ImageView image = new ImageView(getContext());
                        image.setImageBitmap(ImageBitmap);

                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(getContext()).
                                        setMessage("QR CODE").
                                        setPositiveButton("Hecho", (DialogInterface.OnClickListener) (dialog, which) -> {
                                            dialog.dismiss();
                                        }).
                                        setView(image);
                        builder.create().show();

                    }
                    catch (Exception ex){
                        Toast.makeText(getContext(),"Error al generar QR",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



        return root;
    }


    private void setShowData(TextView tv, String field){
        DatabaseReference refUsers =  FirebaseDatabase.getInstance().getReference().child("Proveedores");
        refUsers.child(Proveedor).child(field).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "No Data", task.getException());
                }
                else {
                    if(task != null){
                        String text = "";
                        if(field.equals("descripcion"))
                            text = "Descripcion : ";
                        else if(field.equals("articulos"))
                            text = "Articulos : ";
                        else if(field.equals("contacto"))
                            text = "Contacto : ";
                        else if(field.equals("precio"))
                            text = "Precio : ";

                        if( task.getResult().getValue() != null )
                            tv.setText(text + task.getResult().getValue().toString());
                        else
                            tv.setText(text + "No Data");
                    }

                }
            }
        });
    }
}