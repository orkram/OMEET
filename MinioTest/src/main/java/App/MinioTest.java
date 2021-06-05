package App;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

public class MinioTest {


    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        MinioTest.uploadImage();
    }

    public static void uploadImage() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint("http://127.0.0.1:9000")
                            .region("eu-central-1")
                            .credentials("AKIAIOSFODNN7EXAMPLE", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY")
                            .build();

            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket("avatars").build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("avatars").build());
            } else {
                System.out.println("Bucket 'images' already exists.");
            }
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket("avatars")
                            .object("testimage.jpeg")
                            .filename("/home/arkansweet/Downloads/testimage.jpeg")
                            .build());
            System.out.println(
                    "'/home/arkansweet/Downloads/demo.txt' is successfully uploaded as "
                            + "object 'demo.txt' to bucket 'avatars'.");
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
    }

    public static byte[] downloadImage()  throws IOException, NoSuchAlgorithmException, InvalidKeyException {

        try {
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint("http://127.0.0.1:9000")
                            .region("eu-central-1")
                            .credentials("AKIAIOSFODNN7EXAMPLE", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY")
                            .build();

            try (InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket("avatars")
                            .object("testimage.jpeg")
                            .build())) {
             byte[] content = stream.readAllBytes();
                for (byte b: content) {
                    System.out.print((char)b);
                }

                return content;
            }

        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
        return null;
    }

    public static String getPresignedGetUrl(String username) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint("http://127.0.0.1:9000")
                            .region("eu-central-1")
                            .credentials("AKIAIOSFODNN7EXAMPLE", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY")
                            .build();
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket("avatars")
                            .object(username)
                            .expiry(30)
                            .method(Method.GET)
                            .build()
            );
            System.out.println(url);
            return "{ \"url\": \"" + url+ "\"}";

        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
        return null;
    }

    public static String getPresignedPostUrl(String username) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint("http://127.0.0.1:9000")
                            .credentials("AKIAIOSFODNN7EXAMPLE", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY")
                            .region("eu-central-1")
                            .build();
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket("avatars")
                            .object(username)  //name of file resolved using username
                            .expiry(60, TimeUnit.SECONDS)
                            .method(Method.PUT)    // post for file upload
                            .build()
            );
            System.out.println(url);
            return "{ \"url\": \"" + url+ "\"}";

        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
        return null;
    }
}
