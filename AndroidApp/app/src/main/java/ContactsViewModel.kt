import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangemeet.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ContactsViewModel : ViewModel() {

    public val contactList : MutableLiveData<List<Contact>> = MutableLiveData()

    private var testContactList = List<Contact>(13){ i -> Contact() }

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
            TimeUnit.MILLISECONDS.sleep(300)
            contactList.postValue(testContactList)
        }
    }

}