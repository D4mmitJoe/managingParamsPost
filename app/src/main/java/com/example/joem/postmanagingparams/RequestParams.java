package com.example.joem.postmanagingparams;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by JoeM on 10/5/2017.
 */

public class RequestParams {
    //hashMap that holds parameters
    //hashMapName = parameters
    private HashMap<String, String> hashMapName;
    //need a builder if we want to add params to string
    private StringBuilder stringBuilderName;

    public RequestParams() {
        hashMapName = new HashMap<>();
        stringBuilderName = new StringBuilder();
    }

    //need to make sure 'value' is encoded correctly
    //an example is a user could type a space but space is not encoded
    //therefore "URLEncoder.encode" is used
    //we handle the exception here by adding 'try-catch block'
    //says if you try to add the parameter and encoding fails we have the 'try-catch block'
    //could add an 'exception' instead of 'try-catch block' (if something goes wrong it will add an exception)
    public RequestParams addParameter(String key, String value){
        try {
            hashMapName.put(key, URLEncoder.encode(value, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //'this' will enable us to do some chaining
        return this;
    }
    //need URL to look like this:
    //http://api.theappsdr.com/params.php?name=Bob&age=10&email=bob@bob.com&password=asdfjkl;
    //in other words http...key=value&key=value...
    //we do this with the following method
    public String getEncodedParameters(){
        //Loop over the params 'hashMapName' in 'RequestParams' class and then we add it to a string
        for (String key:hashMapName.keySet()) {//gets 'keys'
            //need to 'append' every 'key-value' pair except first one, which is handled below
            if (stringBuilderName.length() > 0){//if stringBuilderName's length > 0 then it's not the first
                stringBuilderName.append("&");
            }
            //handles base case, which is a URL with 1 'key-value' pair (as opposed to multiple)
            stringBuilderName.append(key + "=" + hashMapName.get(key));
        }
        //return some kind of string (look something like: name=Bob&age=10&email=bob@bob.com&password=asdfjkl;)
        return stringBuilderName.toString();
    }
    //returns URL string (http://api.theappsdr.com/params.php) + ? + parameters (name=Bob&age=10&email=bob@bob.com&password=asdfjkl;)
    public String getEncodedUrl(String url){
        return url + "?" + getEncodedParameters();
    }
    //method; pushes something in the body of the request
    //added exception to method to throw exception caught in asyncTask (as opposed to handled in 'try-catch block' of asyncTask)
    public void encodePostParameters(HttpURLConnection connection) throws IOException {
        connection.setDoOutput(true);//indicates that we're doing to do some output
        OutputStreamWriter writerName = new OutputStreamWriter(connection.getOutputStream());
        writerName.write(getEncodedParameters());//writes encoded parameters from 'getEncodedParameters' (using 'key=value&' format)
        writerName.flush(); //makes sure writerName is written (puts data on outputStream and sends it to receiver)
    }
}