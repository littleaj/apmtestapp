package org.littleaj.apmtestapp.servlet31;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;

@WebServlet(urlPatterns = "/http")
public class HttpRequestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getParameter("url");
        if (url ==  null) { // TODO replace with default url from config
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameter: url");
            return;
        }

        if (StringUtils.isBlank(url)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameter: url is empty");
            return;
        }

        System.out.println("?url="+url);
        URI urlObj;
        try {
            URIBuilder b = new URIBuilder(url);
            if (b.getScheme() == null) {
                b.setScheme("http");
            }
            if (b.getHost() == null) {
                // if scheme is set, it should recognize the host
                final String[] parts = url.split("/");
                b.setHost(parts[0]);
                b.setPath(null);
                if (parts.length > 1) {
                    b.setPathSegments(Arrays.asList(ArrayUtils.remove(parts, 0)));
                }
            }
            System.out.println("URIBuilder="+b.toString());
            urlObj = b.build();
            System.out.println("URI="+b.toString());
        } catch (URISyntaxException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format("Invalid parameter: url value '%s' is an invalid URL: "+e.getLocalizedMessage(), url));
            return;
        }


        HttpGet dependencyRequest;
        try {
            dependencyRequest = new HttpGet(urlObj);
        } catch (IllegalArgumentException iae) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not construct GET request: "+iae.getLocalizedMessage());
            return;
        }

        final StopWatch timer = StopWatch.createStarted();
        try (CloseableHttpResponse dependencyResponse = client().execute(dependencyRequest)) {
            timer.stop();
            final StatusLine statusLine = dependencyResponse.getStatusLine();
            final HttpEntity entity = dependencyResponse.getEntity();

            final Header encoding = entity.getContentEncoding();
            final int size;
            if (encoding == null || encoding.getValue() == null) {
                size = IOUtils.toByteArray(entity.getContent()).length;
            } else {
                size = IOUtils.toByteArray(new InputStreamReader(entity.getContent()), encoding.getValue()).length;
            }

            req.setAttribute("httpClient", "Apache HttpClient 4.5.8");
            req.setAttribute("dependencyStatusLine", statusLine);
            req.setAttribute("dependencyTtr", timer.getTime());
            req.setAttribute("dependencyContentLength", size);
            req.setAttribute("dependencyUrl", urlObj);

            System.out.println("statusLine="+statusLine);
            System.out.println("ttr="+timer.getTime());
            System.out.println("contentLength="+size);

            req.getRequestDispatcher("/dependnecyResponse.jsp").forward(req, resp);
        }
    }

    @Override
    public void destroy() {
        try {
            client().close();
            HttpClientHolder.CONNECTION_MANAGER.close();
        } catch (IOException e) {
            System.err.println("Exception closing HTTP client: " + e.getLocalizedMessage());
        }
    }

    private static CloseableHttpClient client() {
        return HttpClientHolder.INSTANCE;
    }

    private static class HttpClientHolder {
        private static final PoolingHttpClientConnectionManager CONNECTION_MANAGER = new PoolingHttpClientConnectionManager();
        private static final CloseableHttpClient INSTANCE = initialize();

        private static CloseableHttpClient initialize() {
            CONNECTION_MANAGER.setMaxTotal(10);
            return HttpClientBuilder.create()
                    .disableAutomaticRetries()
                    .disableCookieManagement()
                    .useSystemProperties()
                    .setConnectionManager(CONNECTION_MANAGER)
                    .setConnectionManagerShared(true)
                    .build();
        }
    }
}
