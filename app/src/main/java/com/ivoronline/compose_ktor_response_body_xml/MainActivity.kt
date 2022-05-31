package com.ivoronline.compose_ktor_response_body_xml

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.xml.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

//==================================================================
// MAIN ACTIVITY
//==================================================================
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {

      var person by remember { mutableStateOf(PersonDTOXML(0, "No name", 0)) }
      val coroutineScope = rememberCoroutineScope()

      Button(onClick = { coroutineScope.launch { person = callURL() } }) {
        Text("RESPONSE: $person")
      }

    }
  }
}

//==================================================================
// CALL URL
//==================================================================
suspend fun callURL() : PersonDTOXML {

  //CONFIGURE CLIENT
  val client = HttpClient(CIO) {
    install(ContentNegotiation){ xml() }
  }

  //CAL URL
  val person: PersonDTOXML = client.get("http://192.168.0.108:8080/ReceiveBodyXML").body()

  //CLOSE CLIENT
  client.close()

  //RETURN PERSON
  println(person)  //Person(id=1, name=John, age=20)
  return person;

}

//==================================================================
// PERSON DTO XML
//==================================================================
@Serializable
data class PersonDTOXML(
                    val id  : Int,     //Serialize Property into XML Property      (default)
  @XmlElement(true) val name: String,  //Serialize Property into XML Child Element
  @XmlElement(true) val age : Int      //Serialize Property into XML Child Element
)