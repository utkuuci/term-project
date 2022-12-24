package Project.Proxy;

public class ProxyCacheItem {
    byte[] data;
    int length;
    String lastModified;

    public ProxyCacheItem(byte[] data, int length, String lastModified) {
        this.data = data;
        this.length = length;
        this.lastModified = lastModified;
    }
}
