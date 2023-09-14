package com.grocery.on.wheels.s3.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class AmazonClient {

	private AmazonS3 s3client;

	public void upload(String path, String fileName, Optional<Map<String, String>> optionalMetaData,
			InputStream inputStream) {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		optionalMetaData.ifPresent(map -> {
			if (!map.isEmpty()) {
				map.forEach(objectMetadata::addUserMetadata);
			}
		});
		try {
			s3client.putObject(new PutObjectRequest(path, fileName, inputStream, objectMetadata)
					.withCannedAcl(CannedAccessControlList.PublicRead));
		} catch (AmazonServiceException e) {
			e.printStackTrace();
			throw new IllegalStateException("Failed to upload the file", e);
		}
	}

	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials("AKIAYWFSA4ME3WP47A6S", 
				"Kf8cf1EqvifhGqejMmTlW9xLDdyG50GXLTfQgsep");

		this.s3client = AmazonS3ClientBuilder.standard().withRegion("ap-south-1")
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	private void uploadFileTos3bucket(String fileName, File file) {
		// SMPB
		s3client.putObject(new PutObjectRequest("groceryonwheels", fileName, file)
				.withCannedAcl(CannedAccessControlList.PublicRead));

	}

	public String uploadFile(MultipartFile multipartFile) {

		String fileUrl = "";
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			// SMPB
			fileUrl = "http://s3.ap-south-1.amazonaws.com" + "/" + "groceryonwheels" + "/" + fileName;
			uploadFileTos3bucket(fileName, file);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileUrl;
	}

	public String deleteFileFromS3Bucket(String fileUrl) {
		return "NOT IMPLEMENTED";
	}

}