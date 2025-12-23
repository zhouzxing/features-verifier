import java.io.File;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;

public class PutObject {
    public static void main(String[] args) {
        putObjectDemo();
    }

    static void putObjectDemo() {
        // 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials("AKID8SD5mFRYS9csvkOo36vWv87tTq1yiFZf","wsiOuKex8GNK4bw3xd5KLBpGWbYVL6qX");
        // 设置bucket的区域, COS地域的简称请参照 https://www.qcloud.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region("ap-guangzhou"));
        // 生成cos客户端
        COSClient cosClient = new COSClient(cred, clientConfig);

        String bucketName = "ccc-1324882162";
        String key = "abc/abc.txt";
        String localPath = "/home/geeker/.bashrc";

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, new File(localPath));
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        System.out.println(putObjectResult.getRequestId());
    }
}