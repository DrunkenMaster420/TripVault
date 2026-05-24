package com.tripvault.TripVault.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.tripvault.TripVault.model.User;
import org.springframework.stereotype.Service;
import com.google.api.services.drive.model.File;

import java.io.ByteArrayOutputStream;


@Service
public class GoogleDriveService {

    private static final String ApplicationName="TripVault";

    private Drive getDriveService(User user){
        try{
            AccessToken accessToken=new AccessToken(user.getAccessToken(), null);

            GoogleCredentials credentials=GoogleCredentials.create(accessToken);

            return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials))
                    .setApplicationName(ApplicationName)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public String uploadChunk(byte[] chunkData,
                              String fileName,
                              User user) {
        try{
            Drive drive=getDriveService(user);
            File metaData=new File();
            metaData.setName(fileName);

            ByteArrayContent content=new ByteArrayContent("application/octet-stream",chunkData);

            File uploadFile=drive.files().create(metaData,content).setFields("id").execute();

            return uploadFile.getId();


        } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());

        }

    }

    public byte[] downloadChunk(String driveFileId,
                                User user) {

        try{

            Drive drive=getDriveService(user);

            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();

            drive.files().get(driveFileId).executeMediaAndDownloadTo(outputStream);

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to download chunk",e);
        }

    }

    public void deleteChunk(String driveFileId, User user) {

        try {

            Drive driveService = getDriveService(user);

            driveService.files()
                    .delete(driveFileId)
                    .execute();

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete chunk", e);
        }
    }


}
