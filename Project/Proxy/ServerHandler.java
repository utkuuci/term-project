package Project.Proxy;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Paths;

import Project.Core.CoreFactory;
import Project.Core.LoggingService;
import Project.Core.ReadFileService;
import Project.Core.WriteFileService;

public class ServerHandler extends Thread{
    
    Socket clientSocket;
    DataInputStream dis;
    DataOutputStream dos;
    int mgb;
    LoggingService logService;

    public ServerHandler(Socket clienSocket) throws IOException {
        this.clientSocket = clienSocket;
        dis = new DataInputStream(this.clientSocket.getInputStream());
        dos = new DataOutputStream(this.clientSocket.getOutputStream());
        this.mgb = 314572800;
        logService = CoreFactory.creatLoggingService(new File(Paths.get("Project", "Proxy", "logs.log").toAbsolutePath().toString()));
    }

    @Override
    public void run() {
        try{    
            byte[] headerArr = new byte[5000];
            int hc = 0;

            while(true){ 
                byte i = (byte) dis.read();
                headerArr[hc++] = i;
                if (headerArr[hc - 1] == '\n' && headerArr[hc - 2] == '\r' && headerArr[hc - 3] == '\n'
						&& headerArr[hc - 4] == '\r') {
					break;
				}
            }
            String header = new String(headerArr, 0, hc);
            System.out.println("Header From Client");
            System.out.println(header);

            

            if(header.contains("Ban-Url: ")) {
                WriteFileService writeFileService = CoreFactory.creaWriteFileService(new File(Paths.get("Project", "Proxy", "DarkList.txt").toAbsolutePath().toString()));
                String s = header.substring(header.indexOf(" "), header.indexOf("\r\n"));
                URL url = new URL(s);
                writeFileService.append(url.getHost());
                this.clientSocket.close();
                logService.InfoLog(this.clientSocket.getInetAddress().toString() + " " + header);
                return;
            }

            if(header.contains("Get-filtered-hosts")) {
                ReadFileService readFileService = CoreFactory.creatReadFileService(new File(Paths.get("Project", "Proxy", "DarkList.txt").toAbsolutePath().toString()));
                String s = readFileService.getTExt();
                dos.writeBytes(s);
                this.clientSocket.close();
                logService.InfoLog(this.clientSocket.getInetAddress().toString() + " " + header);
                return;
            }

            if(header.contains("Get-log")) {
                logService.InfoLog(this.clientSocket.getInetAddress().toString() + " " + header);
                ReadFileService readFileService = CoreFactory.creatReadFileService(new File(Paths.get("Project", "Proxy", "logs.log").toAbsolutePath().toString()));
                String s = readFileService.getTExt();
                dos.writeBytes(s);
                this.clientSocket.close();
                return;
            }

            logService.InfoLog(this.clientSocket.getInetAddress().toString() + " " + header.replace("\n", " ").replace("\r", " "));

            int fsp = header.indexOf(' ');
            int ssp = header.indexOf(' ', fsp + 1);
            int eol = header.indexOf("\r\n");
            String methodName = header.substring(0, fsp);
            String restHeader = header.substring(eol + 2);
            String modHeader = restHeader;
            
            
            if(methodName.equals("GET") || methodName.equals("HEAD")
                || methodName.equals("POST") || methodName.equals("OPTIONS") || methodName.equals("CONNECT")) {

                // Deleting operations from headers
                if(modHeader.contains("Proxy-Connection")) {
                    int proxIndex = modHeader.indexOf("Proxy-Connection");
                    int eolProxIndex = modHeader.indexOf("\r\n", proxIndex);

                    modHeader = modHeader.substring(0, proxIndex) + modHeader.substring(eolProxIndex + 2);
                }
                if(modHeader.contains("Cookie: Unwanted Cookie")) {
                    int cookieIndex = modHeader.indexOf("Cookie: Unwanted Cookie");
                    int eolCookieIndex = modHeader.indexOf("\r\n", cookieIndex);

                    modHeader = modHeader.substring(0, cookieIndex) + modHeader.substring(eolCookieIndex + 2);
                }

                System.out.println("---- MOD HEADER ----");
                System.out.println(modHeader);
                
                String fullPath = header.substring(fsp+1, ssp);
                
                // Convert String to URL format
                System.out.println(fullPath);
                URL url = new URL(fullPath);
                String domain = url.getHost();
                
                ReadFileService readFileService = CoreFactory.creatReadFileService(new File(Paths.get("Project", "Proxy", "DarkList.txt").toAbsolutePath().toString()));
                String fromFile = readFileService.getTExt();
                String[] arr = fromFile.split("\n");
                for(int i = 0; i < arr.length; i++){ 
                    if(domain.equals(arr[i])) {
                        String html =	"<html>\r\n" +
                                            "<head>\r\n" +
                                                "<title>307 Temporary Redirect</title>\r\n" +
                                            "</head>\r\n" +
                                            "<body>\r\n" +
                                                "<h1>307 Temporary Redirect</h1>\r\n" +
                                                "<h1>The HTTP Method " + methodName + " is not allowed</h1>\r\n" +
                                                "<h1>"+ arr[i] +"</h1>\r\n" +
                                            "</body>\r\n" +
                                        "</html>\r\n";
                        String response =	"HTTP/1.1 307 Temporary Redirect\r\n" +
                                            "Server: CSE471Proxy\r\n" +
                                            "Content-Type: text/html\r\n" +
                                            "Content-Length: " + html.length() + "\r\n" +
                                            "\r\n" +
                                            html;
                        dos.writeBytes(response);
                        this.clientSocket.close();
                        return;
                    }
                }
                String shortPath = url.getPath().equals("") ? "/" : url.getPath();
                System.out.println(domain);

                if(!methodName.equals("POST")) {
                    handleProxy(methodName, modHeader, null, domain, shortPath);
                }
                else {
                    int contIndex = header.indexOf("Content-Length: ");
                    int eol2 = header.indexOf("\r\n", contIndex);
                    int contSize = Integer.parseInt(header.substring(contIndex + 16, eol2));

                    byte[] headerPayload = new byte[contSize];
                    byte[] buffer = new byte[1024];

                    int sum = 0;
                    int read;

                    while(sum < contSize) {
                        read = dis.read(buffer);
                        System.arraycopy(buffer, 0, headerPayload, sum, read);
                        sum += read;
                    }
                    handleProxy(methodName, modHeader, headerPayload, domain, shortPath);
                }
            }
            else {
                String html =	"<html>\r\n" +
									"<head>\r\n" +
										"<title>405 Method Not Allowed</title>\r\n" +
									"</head>\r\n" +
									"<body>\r\n" +
										"<h1>405 Method Not Allowed</h1>\r\n" +
										"<h1>The HTTP Method " + methodName + " is not allowed</h1>\r\n" +
									"</body>\r\n" +
								"</html>\r\n";

				String response =	"HTTP/1.1 405 Method Not Allowed\r\n" +
									"Server: CSE471Proxy\r\n" +
									"Content-Type: text/html\r\n" +
									"Content-Length: " + html.length() + "\r\n" +
									"\r\n" +
									html;

                dos.writeBytes(response);
                this.clientSocket.close();
                return;
            }
            
            this.clientSocket.close();

        } catch(Exception e) { 
            e.printStackTrace();
        }
    }
    public void handleProxy(String methodName, String restHeader, byte[] headerPayload, String domain, String shortPath) throws Exception{
        Socket proxiedSocket = new Socket(domain, 80);

        DataInputStream dis1 = new DataInputStream(proxiedSocket.getInputStream());
        DataOutputStream dos1 = new DataOutputStream(proxiedSocket.getOutputStream());

        String header;
        ProxyCacheItem cacheItem = null;

        boolean contain = ProxyServer.getCache().keySet().contains(domain + shortPath);

        if(contain) {
            cacheItem = ProxyServer.getCache().get(domain+shortPath);
            header = methodName + " " + shortPath + " HTTP/1.1\r\n" + "If-Modified-Since: " + cacheItem.lastModified + "\r\n" + restHeader;
        } else {
            header = methodName + " " + shortPath + " HTTP/1.1\r\n" + restHeader;
        }

        System.out.println("-- HEADER TO THE WEB SERVER --");
        System.out.println(header);
        dos1.writeBytes(header);
        if(methodName.equals("POST") && headerPayload != null) {
            dos.write(headerPayload);
        }

        byte[] responseHeaderArr = new byte[5000];
        int rc = 0;
        while(true) {
            byte i = (byte) dis1.read();
            responseHeaderArr[rc++] = i;
            if (responseHeaderArr[rc - 1] == '\n' && responseHeaderArr[rc - 2] == '\r' && responseHeaderArr[rc - 3] == '\n'
                        && responseHeaderArr[rc - 4] == '\r') {
                    break;
            }
        }

        System.out.println("-----RESPONSE HEADER FROM WEBSERVER-----");
        String responseHeader = new String(responseHeaderArr, 0, rc);

        int fsp = responseHeader.indexOf(' ');
        int ssp = responseHeader.indexOf(' ', fsp + 1);
        String status = responseHeader.substring(fsp + 1, ssp);
        int statusCode = Integer.parseInt(status);

        System.out.println(responseHeader);

        if(statusCode == 200) {
            int contIndex = responseHeader.indexOf("Content-Length: ");
            int eol = responseHeader.indexOf("\r\n", contIndex);

            int size = Integer.parseInt(responseHeader.substring(contIndex + 16, eol));

            System.out.println("Response size: " + size);

            if(size <= this.mgb) {
                byte[] p = new byte[size];

                byte[] buffer = new byte[1024];

                int sum = 0;
                int read;

                while (sum < size) {
                    read = dis1.read(buffer);
                    System.arraycopy(buffer, 0, p, sum, read);
                    sum += read;
                }
                
                dos.write(responseHeaderArr, 0, rc);
                
                dos.write(p);

                dos.flush();
                
                if (responseHeader.contains("Last-Modified: ")) {
                    
                    int index= responseHeader.indexOf("Last-Modified: ");
                    
                    eol = responseHeader.indexOf("\r\n", index);
                    String date = responseHeader.substring(index + 15, eol);
                    
                    ProxyServer.getCache().put(domain + shortPath, new ProxyCacheItem(p, size, date));
                }
            }
            else {
                
                dos.write(responseHeaderArr, 0, rc);
                
                byte[] buffer = new byte[20480];

                int count = 0;
                int byteFrom;
                
                while (count < size) {
                    byteFrom = dis1.read(buffer);
                    dos.write(buffer,0,byteFrom);
                    count += byteFrom;
                }
            }
        }
        if(statusCode == 304) {
            int eol= responseHeader.indexOf("\r\n");
            responseHeader="HTTP/1.1 200 OK\nContent-Length: "+cacheItem.length+responseHeader.substring(eol+1);
            
            dos.write(responseHeader.getBytes());
            
            dos.write(cacheItem.data);
            
            dos.flush();
        }
        proxiedSocket.close();
    }
}
