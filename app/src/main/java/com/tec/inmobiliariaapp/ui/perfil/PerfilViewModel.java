package com.tec.inmobiliariaapp.ui.perfil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tec.inmobiliariaapp.model.Propietario;

public class PerfilViewModel extends ViewModel {

    private final MutableLiveData<Propietario> propietarioLiveData = new MutableLiveData<>();

    public PerfilViewModel() {
        // Datos simulados para arrancar, luego se reemplaza con datos reales
        Propietario p = new Propietario(1, "Maximiliano", "Balestrieri", "12345678",
                "11659541611", "maxi@gmail.com", "1234");
        propietarioLiveData.setValue(p);
    }

    public LiveData<Propietario> getPropietario() {
        return propietarioLiveData;
    }

    public void actualizarPropietario(int idPropietario, String dni, String nombre, String apellido,
                                      String email, String contrasena, String telefono) {
        Propietario p = propietarioLiveData.getValue();
        if (p != null) {
            p.setIdPropietario(idPropietario);
            p.setDni(dni);
            p.setNombre(nombre);
            p.setApellido(apellido);
            p.setEmail(email);
            p.setClave(contrasena);
            p.setTelefono(telefono);
            propietarioLiveData.setValue(p);
        }
    }
}