package com.rose.snake;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SnakeRequest extends StringRequest{

    final static private String URL = "http://wlgur0914.dothome.co.kr/SnakeScore.php";

    private Map<String, String> map;

    public SnakeRequest(String userID,String userDate, String userScore, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);

        map = new HashMap<>();
        map.put("userID",userID);
        map.put("userDate",userDate);
        map.put("userScore",userScore);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError{
        return map;
    }

}
