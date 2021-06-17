//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.generalServicies;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Service
public class MinioService {

    @Value("${minio.ip}")
    private String ip;
    @Value("${minio.port}")
    private String port;
    @Value("${minio.secret-key}")
    private String secretKey;
    @Value("${minio.access-key}")
    private String accessKey;

    private static String avatarsBucket = "avatars";


    public void uploadImage() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            MinioClient minioClient = getClient();

            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(avatarsBucket).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(avatarsBucket).build());
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

    byte[] downloadImage()  throws IOException, NoSuchAlgorithmException, InvalidKeyException {

        try {
            MinioClient minioClient = getClient();

            try (InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(avatarsBucket)
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

    public String avatarGetUrl(String username) {
        try {
            MinioClient minioClient = getClient();

            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(avatarsBucket).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(avatarsBucket).build());
            } else {
                System.out.println("Bucket 'images' already exists.");
            }

            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(avatarsBucket)
                            .object(username)
                            .expiry(60, TimeUnit.SECONDS)
                            .method(Method.GET)
                            .build()
            );
            return url;

        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        } catch (InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String avatarPutUrl(String username) {
        try {
            MinioClient minioClient = getClient();

            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(avatarsBucket).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(avatarsBucket).build());
            } else {
                System.out.println("Bucket 'images' already exists.");
            }

            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(avatarsBucket)
                            .object(username)  //name of file resolved using username
                            .expiry(60, TimeUnit.SECONDS)
                            .method(Method.PUT)    // post for file upload
                            .build()
            );
            return url;

        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        } catch (InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private MinioClient getClient(){
        return MinioClient.builder()
                        .endpoint("http://" + ip + ":" + port +"")
                        .region("eu-central-1")
                        .credentials(accessKey, secretKey)
                        .build();

    }
}
