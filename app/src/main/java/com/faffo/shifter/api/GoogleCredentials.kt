package com.faffo.shifter.api

//import com.google.api.client.auth.oauth2.Credential
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
//import com.google.api.client.http.javanet.NetHttpTransport
//import com.google.api.client.json.JsonFactory
//import com.google.api.client.json.jackson2.JacksonFactory
//import com.google.api.client.util.store.FileDataStoreFactory


class GoogleCredentials(
    val TOKENS_DIRECTORY_PATH: String = "/tokens"
//    val JSON_FACTORY: JsonFactory = JacksonFactory.getDefaultInstance()
) {;

//    @Throws(IOException::class)
//    inline fun <reified T> getCredentials(
//        HTTP_TRANSPORT: NetHttpTransport,
//        SCOPES: List<String>,
//        CREDENTIALS_FILE_PATH: String = "/credentials.json"
//    ): Credential? {
//
//        // Load client secrets.
//        val `in`: InputStream = T::class.java.getResourceAsStream(CREDENTIALS_FILE_PATH)
//            ?: throw FileNotFoundException("Resource not found: $CREDENTIALS_FILE_PATH")
//        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(`in`))
//
//        // Build flow and trigger user authorization request.
//        val codeFlowBuilder =
//            GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//                .setDataStoreFactory(FileDataStoreFactory(File(TOKENS_DIRECTORY_PATH)))
//        codeFlowBuilder.accessType = "offline"
//        //val flow = codeFlowBuilder.build()
//
//        val receiverBuilder = LocalServerReceiver.Builder(); receiverBuilder.port = 8888
//        //val receiver = receiverBuilder.build()
//        return AuthorizationCodeInstalledApp(
//            codeFlowBuilder.build(),
//            receiverBuilder.build()
//        ).authorize("user")
//    }
}