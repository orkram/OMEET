import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangemeet.Contact
import com.example.orangemeet.Meeting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MeetingsViewModel : ViewModel() {
    public val meetingsList : MutableLiveData<List<Meeting>> = MutableLiveData()

    private var testMeetingsList = List<Meeting>(13){ i -> Meeting() }

    public fun GetContactsFromBackend(){
        viewModelScope.launch(Dispatchers.IO) {
            TimeUnit.MILLISECONDS.sleep(300)
            meetingsList.postValue(testMeetingsList)
        }
    }
}