package com.tripvault.TripVault.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.About;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.tripvault.TripVault.dto.DriveStorageInfo;
import com.tripvault.TripVault.model.StorageAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.google.api.services.drive.model.File;


import java.io.IOException;
import java.io.OutputStream;


@Service
@RequiredArgsConstructor
public class GoogleDriveService {

    private final GoogleTokenService googleTokenService;
    private static final String ApplicationName="TripVault";

    private Drive getDriveService(StorageAccount storageAccount){
        try{
            String validAccessToken = googleTokenService.getValidAccessToken(
                    storageAccount
            );

            AccessToken accessToken=new AccessToken(validAccessToken, null);

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
                              StorageAccount storageAccount) {
        try{
            Drive drive=getDriveService(storageAccount);
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

    public void downloadChunk(String driveFileId,
                                StorageAccount storageAccount,
                                OutputStream outputStream) {

        try{

            Drive drive=getDriveService(storageAccount);


            drive.files().get(driveFileId).executeMediaAndDownloadTo(outputStream);


        } catch (Exception e) {
            e.printStackTrace();

            System.out.println(
                    "Failed file id: " + driveFileId
            );

            throw new RuntimeException(
                    "Failed to download chunk",
                    e
            );

        }

    }

    public void deleteChunk(String driveFileId, StorageAccount storageAccount) {

        try {

            Drive driveService = getDriveService(storageAccount);

            driveService.files()
                    .delete(driveFileId)
                    .execute();

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete chunk", e);
        }
    }

    public DriveStorageInfo getStorageInfo(StorageAccount storageAccount) {
        try {
            Drive drive = getDriveService(storageAccount);

            About about = drive.about()
                    .get()
                    .setFields("storageQuota")
                    .execute();

            About.StorageQuota quota = about.getStorageQuota();

            DriveStorageInfo info = new DriveStorageInfo();

            info.setTotalQuota(quota.getLimit());
            info.setUsedQuota(quota.getUsage());

            return info;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Google Drive storage information", e);
        }
    }



}
