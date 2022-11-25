package company.project.ui.ver_proveedores;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VerProveedoresViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public VerProveedoresViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is SensorHubicacion fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}