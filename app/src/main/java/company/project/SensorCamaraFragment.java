package company.project;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.FrameLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import company.project.ui.ver_proveedores.VerProveedoresViewModel;

import static android.Manifest.permission.CAMERA;


public class SensorCamaraFragment extends Fragment {


    private FirebaseAuth mAuth;

    // Define the pic id
    private static final int pic_id = 123;
    // Define the button and imageview type variable
    Button camera_open_id;
    Button save_camera_id;
    ImageView click_image_id;

    Bitmap photo;
    boolean photoTook = false;

    private VerProveedoresViewModel sensorCamaraViewModel;

    StorageReference storageRef;
    FirebaseStorage storage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sensor_camara, container, false);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        // By ID we can get each component which id is assigned in XML file get Buttons and imageview.
        camera_open_id = root.findViewById(R.id.camera_button);
        click_image_id = root.findViewById(R.id.click_image);
        click_image_id.setBackgroundColor(getResources().getColor(R.color.gray));

        save_camera_id = root.findViewById(R.id.camera_save);

        // Camera_open button is for open the camera and add the setOnClickListener in this button
        camera_open_id.setOnClickListener(v -> {
            // Create the camera_intent ACTION_IMAGE_CAPTURE it will open the camera for capture the image
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Start the activity with camera_intent, and request pic id
            startActivityForResult(camera_intent, pic_id);
        });

        save_camera_id.setOnClickListener(v -> {
            if(photoTook){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                mAuth = FirebaseAuth.getInstance();
                String photoName = mAuth.getUid();
                StorageReference photoRef = storageRef.child(photoName + ".jpg");
                StorageReference photoImagesRef = storageRef.child("images/"+ photoName +".jpg");

                UploadTask uploadTask = photoImagesRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(root.getContext(),exception.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(root.getContext(),"Foto actualizada",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Toast.makeText(root.getContext(),"Debe elegir una foto primero",Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == pic_id) {

            photo = (Bitmap) data.getExtras().get("data");


            click_image_id.setBackgroundColor(0);
            click_image_id.setImageBitmap(photo);
            photoTook = true;
        }
    }


}