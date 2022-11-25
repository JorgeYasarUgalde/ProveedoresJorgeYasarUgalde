package company.project.ui.ver_proveedores;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import company.project.MainActivity;
import company.project.Proveedor;
import company.project.R;

public class VerProveedoresFragment extends Fragment {

    private VerProveedoresViewModel slideshowViewModel;


    private ListView listview;
    private ArrayList<String> proveedoresNames;
    Context context;

    // creating a variable for database reference.
    DatabaseReference reference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(VerProveedoresViewModel.class);
        View root = inflater.inflate(R.layout.fragment_ver_proveedores, container, false);
        context = getContext();

        listview = (ListView) root.findViewById(R.id.listNames);
        proveedoresNames = new ArrayList<String>();

        FirebaseDatabase.getInstance()
                .getReference()
                .child("Proveedores")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnap : dataSnapshot.getChildren() ) {
                            System.out.println(dataSnap.getKey());
                            proveedoresNames.add(dataSnap.getKey());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, proveedoresNames);
                        listview.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("SelectedProveedores", proveedoresNames.get(position));
                editor.apply();
                Navigation.findNavController(view).navigate(R.id.lectorQRFragment);
            }
        });


        return root;
    }



}

/*
*
*
*
* void saveLastButtonPressed(int buttonNumber) {
 SharedPreferences sharedPref = getSharedPreferences("application", Context.MODE_PRIVATE);
 SharedPreferences.Editor editor = sharedPref.edit();
 editor.putInt("LAST_BUTTON", buttonNumber);
 editor.apply();
}
*
*
* int readLastButtonPressed() {
 SharedPreferences sharedPref = getSharedPreferences("application", Context.MODE_PRIVATE);
 return sharedPref.getInt("LAST_BUTTON", 0);
}
*
* */