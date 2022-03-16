package `in`.thesupremeone.taskturbo.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginViewModel(private val auth: FirebaseAuth) : ViewModel() {
    private val userMutable = MutableLiveData<FirebaseUser?>()

    val user: LiveData<FirebaseUser?>
    get() = userMutable

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                loadingMutable.postValue(false)
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LoginViewModel", "signInWithCredential:success")
                    val user = auth.currentUser
                    userMutable.postValue(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LoginViewModel", "signInWithCredential:failure", task.exception)
                    userMutable.postValue(null);
                }
            }
    }


    fun postUser(user: FirebaseUser?){
        userMutable.postValue(user)
    }

    private val loadingMutable = MutableLiveData<Boolean>(false)

    val loading: LiveData<Boolean>
    get() = loadingMutable

    fun setLoading(loading: Boolean){
        loadingMutable.postValue(loading)
    }
}