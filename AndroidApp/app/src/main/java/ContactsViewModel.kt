import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.orangemeet.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception
import java.util.concurrent.TimeUnit

class ContactsViewModel(context: Context?) : ViewModel() {

    public val contactList : MutableLiveData<List<Contact>> = MutableLiveData()

    private var testContactList = List<Contact>(13){ i -> Contact() }

    var context: Context? = null

    var token : MutableLiveData<String> = MutableLiveData()

    init {
        this.context = context
    }

    public fun RemoveSelectedContactsFromBackend(){

        viewModelScope.launch(Dispatchers.IO){
            var removalConfirmed = true
            //TODO("Get confirmation from backend")
            testContactList = testContactList.filter { x -> !x.selected }
            contactList.postValue(testContactList)
        }
    }

    public fun GetContactsFromBackend(){
        viewModelScope.launch(Dispatchers.IO) {
            //TODO("Get from backend")
            TimeUnit.MILLISECONDS.sleep(300)
            contactList.postValue(testContactList)
        }
    }

}