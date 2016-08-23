package com.jifenke.lepluslive.filemanage.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
* Created by wcg on 16/3/10.
*/
@Service
public class FileImageService {

  @Value("${bucket.image}")
  private String imageBucket;

  @Value("${bucket.barcode}")
  private String barCodeBucket;

  private OSSClient ossClient;

  @Value("${bucket.endpoint}")
  private String entryPoint;

  @Value("${bucket.ossAccessId}")
  private String ossAccessId;

  @Value("${bucket.ossAccessSecret}")
  private String ossAccessSecret;

  private synchronized OSSClient getOssClient(){
      if(ossClient!=null){
        return ossClient;
      }
    return new OSSClient(entryPoint,ossAccessId,ossAccessSecret);
  }

  public void saveImage(MultipartFile filedata,String filePath) throws IOException {
    ossClient = getOssClient();
    InputStream is = filedata.getInputStream();

    // 创建上传Object的Metadata
    ObjectMetadata meta = new ObjectMetadata();

    // 必须设置ContentLength
    meta.setContentLength(filedata.getSize());
    PutObjectResult putObjectResult = ossClient.putObject(imageBucket, filePath, is, meta);
  }

  public void SaveBarCode(byte[] bytes,String filePath){
    InputStream is = new ByteArrayInputStream(bytes);
    ObjectMetadata meta = new ObjectMetadata();
    ossClient = getOssClient();
    // 必须设置ContentLength
    meta.setContentLength(bytes.length);
    PutObjectResult putObjectResult = ossClient.putObject(barCodeBucket, filePath, is, meta);


  }

}
