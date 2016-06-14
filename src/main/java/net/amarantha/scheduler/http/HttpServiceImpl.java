package net.amarantha.scheduler.http;

import com.google.inject.Singleton;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.util.Timer;
import java.util.TimerTask;

@Singleton
public class HttpServiceImpl implements HttpService {

    @Override
    public String get(String host, String path, Param... params) {
        String result = null;
        Response response = null;
        try {
            response = getEndpoint(host, path, params).get();
            result = response.readEntity(String.class);
        } catch ( NoRouteToHostException | ConnectException e ) {
            e.printStackTrace();
        } finally {
            if ( response!=null ) response.close();
        }
        return result;
    }

    @Override
    public void getAsync(HttpCallback callback, String host, String path, Param... params) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = getEndpoint(host, path, params).get();
                } catch ( Exception e ) {
                    e.printStackTrace();
                } finally {
                    if ( response!=null ) response.close();
                }
                if ( callback!=null ) {
                    callback.call(response);
                }
            }
        }, 0);
    }

    @Override
    public String post(String host, String path, String payload, Param... params) {
        String result = null;
        Response response = null;
        try {
            response = getEndpoint(host, path, params).post(Entity.entity(payload, MediaType.TEXT_PLAIN));
            result = response.readEntity(String.class);
            System.out.println(result);
        } catch ( NoRouteToHostException | ConnectException e ) {
            e.printStackTrace();
        } finally {
            if ( response!=null ) response.close();
        }
        return result;
    }

    @Override
    public void postAsync(HttpCallback callback, String host, String path, String payload, Param... params) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = getEndpoint(host, path, params).post(Entity.entity(payload, MediaType.TEXT_PLAIN));
                } catch ( Exception e ) {
                    e.printStackTrace();
                } finally {
                    if ( response!=null ) response.close();
                }
                if ( callback!=null ) {
                    callback.call(response);
                }
            }
        }, 0);
    }

    private Invocation.Builder getEndpoint(String host, String path, Param... params) throws NoRouteToHostException, ConnectException {
        Client client = ClientBuilder.newClient();
        WebTarget endpoint = client.target("http://"+host).path(path);
        for ( Param param : params ) {
            endpoint.queryParam(param.getName(), param.getValue());
        }
        return endpoint.request();
    }

}
