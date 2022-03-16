package `in`.thesupremeone.taskturbo.viewmodels

import `in`.thesupremeone.taskturbo.viewmodels.LoginViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth

class LoginViewModelFactory (private val auth: FirebaseAuth) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(auth) as T
    }
}