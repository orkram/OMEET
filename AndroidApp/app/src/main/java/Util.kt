import java.nio.charset.Charset
import java.util.*

class Util {
    companion object{
        fun GenerateRandomString(length : Int) : String{
            val array = ByteArray(length)
            Random().nextBytes(array)
            for(i in array.indices){
                if(array[i] < 0)
                    array[i] = (-array[i]).toByte()
                array[i] = (array[i] % 26 + 97).toByte()
                System.out.println(array[i])
            }
            return String(array, Charset.forName("UTF-8"))
        }
    }
}