package com.cuecolab.cuecolab.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "videos")
public class VideoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID videoId;

    @NotNull
    private String videoFileName;

    @NotNull
    private int videoFileSize;

    @NotNull
    //This is the time when the user requested s3 signed url to upload the video
    private LocalDateTime signedURLRequestDateTime;

    @NotNull
    private boolean isLocked;

    private String rawVideoS3URI;

    //================================================================
    //================================================================
    //Lambda function will send these things after the video gets uploaded to processed-videos-s3-bucket

    private String videoThumbnailS3URI;

    private String masterPlaylistS3URI;

    //This is the time when the processed material of this video (including .m3u8 files and .ts files)
    //got uploaded to processed-videos-s3-bucket
    private LocalDateTime processedVideoUploadDateTime;

    private String videoTitle;
    private String videoDescription;
    private String videoTags; //This string should be comma separated

    @NotNull
    private String uploadedBy;

    //================================================================
    //================================================================

    //////////////////////////////////////////////////////////////////
    //===================================
    //Parents of this video
    //===================================
    @ManyToOne
    @JoinColumn
    @NotNull
    @JsonIgnore
    private RoomEntity roomEntity;

    @ManyToOne
    @JoinColumn
    @NotNull
    @JsonIgnore
    private RoomParticipantEntity roomParticipantEntity;

    //////////////////////////////////////////////////////////////////
    //===================================
    //Children of this video
    //===================================
    //VIDEO-ENTITY HAS NO CHILDREN, AS IT IS LAST ENTITY
    //IN THIS APPLICATION
}
